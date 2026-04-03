package com.api.tester.service;

import com.api.tester.model.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class LoadTestService {

    private final WebClient webClient;

    // Max allowed requests to prevent abuse
    private static final int MAX_REQUESTS = 50;

    public LoadTestService(WebClient webClient) {
        this.webClient = webClient;
    }

    // ─────────────────────────────────────────────
    // MAIN ENTRY POINT
    // Called by the controller with the load test request
    // ─────────────────────────────────────────────
    public LoadTestReport runLoadTest(LoadTestRequest request) {

        // Cap requests at MAX_REQUESTS — no matter what user sends
        int count = Math.min(request.getRequestCount(), MAX_REQUESTS);

        List<LoadTestResult> results;

        // Run sequential or concurrent based on mode
        if ("CONCURRENT".equalsIgnoreCase(request.getMode())) {
            results = runConcurrent(request, count);
        } else {
            results = runSequential(request, count);
        }

        // Analyze all results and build the report
        return buildReport(results, request.getMode());
    }

    // ─────────────────────────────────────────────
    // SEQUENTIAL MODE
    // Sends requests one after another
    // Good for testing basic reliability and response time trend
    // ─────────────────────────────────────────────
    private List<LoadTestResult> runSequential(LoadTestRequest request, int count) {

        List<LoadTestResult> results = new ArrayList<>();

        for (int i = 1; i <= count; i++) {
            // Send each request and collect the result
            LoadTestResult result = sendSingleRequest(request, i);
            results.add(result);
        }

        return results;
    }

    // ─────────────────────────────────────────────
    // CONCURRENT MODE
    // Sends all requests at the same time using a thread pool
    // Good for testing how the API handles load/stress
    // ─────────────────────────────────────────────
    private List<LoadTestResult> runConcurrent(LoadTestRequest request, int count) {

        // Create a thread pool with as many threads as requests
        // Each thread will fire one request simultaneously
        ExecutorService executor = Executors.newFixedThreadPool(count);

        // List to hold the Future results of each thread
        List<Future<LoadTestResult>> futures = new ArrayList<>();

        for (int i = 1; i <= count; i++) {
            final int requestNumber = i;

            // Submit each request as a separate task to the thread pool
            // Callable is like Runnable but returns a value
            Future<LoadTestResult> future = executor.submit(() -> sendSingleRequest(request, requestNumber));

            futures.add(future);
        }

        // Shut down executor — no new tasks will be accepted
        // but existing tasks will complete
        executor.shutdown();

        try {
            // Wait for all threads to finish — max 60 seconds
            executor.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Collect results from all futures
        List<LoadTestResult> results = new ArrayList<>();

        for (Future<LoadTestResult> future : futures) {
            try {
                // future.get() blocks until the result is available
                results.add(future.get());
            } catch (Exception e) {
                // If a thread failed entirely, record it as a failed request
                results.add(new LoadTestResult(
                        results.size() + 1, 500, 0, false, e.getMessage()));
            }
        }

        return results;
    }

    // ─────────────────────────────────────────────
    // SEND A SINGLE REQUEST
    // Used by both sequential and concurrent modes
    // Same logic as ApiTestService.sendRequest()
    // ─────────────────────────────────────────────
    private LoadTestResult sendSingleRequest(LoadTestRequest request, int requestNumber) {

        long startTime = System.currentTimeMillis();

        try {
            String method = request.getMethod().toUpperCase();
            boolean hasBody = request.getBody() != null
                    && ("POST".equals(method) || "PUT".equals(method));

            WebClient.RequestHeadersSpec<?> headersSpec;

            if (hasBody) {
                headersSpec = webClient
                        .method(HttpMethod.valueOf(method))
                        .uri(request.getUrl())
                        .headers(h -> {
                            if (request.getHeaders() != null) {
                                h.setAll(request.getHeaders());
                            }
                        })
                        .bodyValue(request.getBody());
            } else {
                // GET and DELETE must not have a body
                headersSpec = webClient
                        .method(HttpMethod.valueOf(method))
                        .uri(request.getUrl())
                        .headers(h -> {
                            if (request.getHeaders() != null) {
                                h.setAll(request.getHeaders());
                            }
                        });
            }

            ResponseEntity<String> response = headersSpec
                    .exchangeToMono(res -> res.toEntity(String.class))
                    .block();

            long endTime = System.currentTimeMillis();

            return new LoadTestResult(
                    requestNumber,
                    response.getStatusCode().value(),
                    endTime - startTime,
                    response.getStatusCode().is2xxSuccessful(),
                    null);

        } catch (Exception e) {
            long endTime = System.currentTimeMillis();

            // Extract real status code if it's a WebClient error
            int status = 500;
            if (e instanceof WebClientResponseException wcre) {
                status = wcre.getStatusCode().value();
            }

            return new LoadTestResult(
                    requestNumber, status,
                    endTime - startTime, false, e.getMessage());
        }
    }

    // ─────────────────────────────────────────────
    // BUILD REPORT
    // Takes all results and calculates stats
    // ─────────────────────────────────────────────
    private LoadTestReport buildReport(List<LoadTestResult> results, String mode) {

        int totalRequests = results.size();

        // Count successes and failures
        int successCount = (int) results.stream().filter(LoadTestResult::isSuccess).count();
        int failureCount = totalRequests - successCount;

        // Success rate as a percentage
        double successRate = ((double) successCount / totalRequests) * 100;

        // Response time stats
        long minResponseTime = results.stream()
                .mapToLong(LoadTestResult::getResponseTime)
                .min().orElse(0);

        long maxResponseTime = results.stream()
                .mapToLong(LoadTestResult::getResponseTime)
                .max().orElse(0);

        long avgResponseTime = (long) results.stream()
                .mapToLong(LoadTestResult::getResponseTime)
                .average().orElse(0);

        // Status code breakdown — group by status and count
        // e.g { 200: 18, 404: 2 }
        Map<Integer, Long> statusCodeBreakdown = results.stream()
                .collect(Collectors.groupingBy(
                        LoadTestResult::getStatus,
                        Collectors.counting()));

        // Response time trend — only meaningful for sequential
        // Compare average of first half vs second half
        String responseTimeTrend = "N/A";
        if ("SEQUENTIAL".equalsIgnoreCase(mode)) {
            responseTimeTrend = calculateTrend(results);
        }

        // Generate verdict and summary
        String verdict = generateVerdict(successRate, avgResponseTime);
        String summary = generateSummary(successRate, avgResponseTime,
                failureCount, responseTimeTrend, mode);

        return new LoadTestReport(
                results, totalRequests, successCount, failureCount,
                successRate, minResponseTime, maxResponseTime, avgResponseTime,
                statusCodeBreakdown, responseTimeTrend, verdict, summary);
    }

    // ─────────────────────────────────────────────
    // RESPONSE TIME TREND (Sequential only)
    // Compares avg response time of first half vs second half
    // If second half is significantly slower → GETTING_SLOWER
    // If second half is significantly faster → GETTING_FASTER
    // Otherwise → STABLE
    // ─────────────────────────────────────────────
    private String calculateTrend(List<LoadTestResult> results) {

        int mid = results.size() / 2;

        // Average response time of first half
        double firstHalfAvg = results.subList(0, mid).stream()
                .mapToLong(LoadTestResult::getResponseTime)
                .average().orElse(0);

        // Average response time of second half
        double secondHalfAvg = results.subList(mid, results.size()).stream()
                .mapToLong(LoadTestResult::getResponseTime)
                .average().orElse(0);

        // If second half is more than 20% slower → GETTING_SLOWER
        if (secondHalfAvg > firstHalfAvg * 1.2) {
            return "GETTING_SLOWER";
        }

        // If second half is more than 20% faster → GETTING_FASTER
        if (secondHalfAvg < firstHalfAvg * 0.8) {
            return "GETTING_FASTER";
        }

        return "STABLE";
    }

    // ─────────────────────────────────────────────
    // VERDICT
    // UNSTABLE → too many failures
    // SLOW → high average response time
    // STABLE → everything looks good
    // ─────────────────────────────────────────────
    private String generateVerdict(double successRate, long avgResponseTime) {

        if (successRate < 80) {
            return "UNSTABLE";
        }

        if (avgResponseTime > 1000) {
            return "SLOW";
        }

        return "STABLE";
    }

    // ─────────────────────────────────────────────
    // SUMMARY
    // Human readable summary of the load test
    // ─────────────────────────────────────────────
    private String generateSummary(double successRate, long avgResponseTime,
            int failureCount, String trend, String mode) {

        StringBuilder summary = new StringBuilder();

        summary.append(String.format("%.1f%% success rate with avg response time of %dms. ",
                successRate, avgResponseTime));

        if (failureCount > 0) {
            summary.append(String.format("%d request(s) failed. ", failureCount));
        }

        if ("SEQUENTIAL".equalsIgnoreCase(mode) && !"N/A".equals(trend)) {
            if ("GETTING_SLOWER".equals(trend)) {
                summary.append("API is getting slower over time — possible memory leak or resource exhaustion. ");
            } else if ("GETTING_FASTER".equals(trend)) {
                summary.append("API is getting faster over time — possible warm-up effect or caching kicking in. ");
            } else {
                summary.append("Response times are consistent throughout the test. ");
            }
        }

        if ("CONCURRENT".equalsIgnoreCase(mode) && avgResponseTime > 1000) {
            summary.append("API struggles under concurrent load. ");
        }

        return summary.toString().trim();
    }
}