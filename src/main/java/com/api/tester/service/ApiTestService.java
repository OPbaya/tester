package com.api.tester.service;

import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.api.tester.model.AnalysisReport;
import com.api.tester.model.ApiRequest;
import com.api.tester.model.ApiResponse;
import com.api.tester.model.TestResult;

import org.springframework.http.ResponseEntity;

@Service
public class ApiTestService {

    private final WebClient webClient;

    // Constructor Injection (IMPORTANT)
    public ApiTestService(WebClient webClient) {
        this.webClient = webClient;
    }

    // Method to test GET API
    public ApiResponse testGetApi(String url) {

        long startTime = System.currentTimeMillis();

        try {
            // get request using WebClient
            ResponseEntity<String> response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .toEntity(String.class)
                    .block();

            long endTime = System.currentTimeMillis();

            return new ApiResponse(
                    response.getStatusCode().value(),
                    (endTime - startTime),
                    true,
                    null);

        } catch (Exception e) {

            long endTime = System.currentTimeMillis();

            return new ApiResponse(
                    500,
                    (endTime - startTime),
                    false,
                    e.getMessage());
        }
    }
    // Method to run multiple test cases

    // public List<TestResult> runMultipleTests(String url) {

    //     List<TestResult> results = new ArrayList<>();

    //     // 1. Normal Request
    //     ApiResponse normal = testGetApi(url);
    //     results.add(new TestResult(
    //             "Normal Request",
    //             normal.getStatus(),
    //             normal.isSuccess(),
    //             analyzeIssue(normal.getStatus(), normal.getError()),
    //             normal.getError(),
    //             normal.getResponseTime()));

    //     // 2. Invalid URL
    //     ApiResponse invalid = testGetApi(url + "/invalid");
    //     results.add(new TestResult(
    //             "Invalid URL",
    //             invalid.getStatus(),
    //             invalid.isSuccess(),
    //             analyzeIssue(invalid.getStatus(), invalid.getError()),
    //             invalid.getError(),
    //             invalid.getResponseTime()));

    //     // 3. Empty URL
    //     ApiResponse empty;
    //     try {
    //         empty = testGetApi("");
    //     } catch (Exception e) {
    //         empty = new ApiResponse(500, 0, false, "Empty URL");
    //     }

    //     results.add(new TestResult(
    //             "Empty URL",
    //             empty.getStatus(),
    //             empty.isSuccess(),
    //             analyzeIssue(empty.getStatus(), empty.getError()),
    //             empty.getError(),
    //             empty.getResponseTime()));

    //     return results;
    // }


    private List<TestResult> generateTests(ApiRequest request) {

    List<TestResult> results = new ArrayList<>();

    String url = request.getUrl();
    String method = request.getMethod();

    // Always run normal test
    ApiResponse normal = testGetApi(url);
    results.add(new TestResult(
            "Normal Request",
            normal.getStatus(),
            normal.isSuccess(),
            analyzeIssue(normal.getStatus(), normal.getError()),
            normal.getError(),
            normal.getResponseTime()
    ));

    // Dynamic logic based on method
    if ("GET".equalsIgnoreCase(method)) {

        ApiResponse invalid = testGetApi(url + "/invalid");

        results.add(new TestResult(
                "Invalid Endpoint Test",
                invalid.getStatus(),
                invalid.isSuccess(),
                analyzeIssue(invalid.getStatus(), invalid.getError()),
                invalid.getError(),
                invalid.getResponseTime()
        ));
    }

    if ("POST".equalsIgnoreCase(method)) {

        ApiResponse empty = testGetApi(""); // simulate bad input

        results.add(new TestResult(
                "Empty Input Test",
                empty.getStatus(),
                empty.isSuccess(),
                analyzeIssue(empty.getStatus(), empty.getError()),
                empty.getError(),
                empty.getResponseTime()
        ));
    }

    return results;
}


    // Method to analyze issues based on status and error message
    private String analyzeIssue(int status, String error) {

        if (status == 200) {
            return "No issue";
        }

        if (status == 404) {
            return "Endpoint not found";
        }

        if (status == 500) {
            return "Server error or crash - possible poor error handling";
        }

        if (error != null && error.contains("timeout")) {
            return "Request timeout - API may be slow";
        }

        return "Unknown issue";
    }

    // Method to detect patterns and summarize results
    private String detectSummary(List<TestResult> results) {

        int failures = 0;
        long totalTime = 0;

        for (TestResult r : results) {
            if (!r.isSuccess()) {
                failures++;
            }
            totalTime += r.getResponseTime();
        }

        long avgTime = totalTime / results.size();

        // Pattern 1: Too many failures
        if (failures >= 2) {
            return "API is unstable - multiple test cases failed";
        }

        // Pattern 2: Slow API
        if (avgTime > 1000) {
            return "API is slow - high average response time";
        }

        return "API is stable";
    }

    // Main method to run full analysis
    
    public AnalysisReport analyzeFullApi(ApiRequest request) {

        List<TestResult> results = generateTests(request);

        String summary = detectSummary(results);

        String overallStatus = summary.contains("API is stable") ? "GOOD" : "ISSUE";

        List<String> suggestions = generateSuggestions(results);

        return new AnalysisReport(results, overallStatus, summary, suggestions);
    }

    // Method to generate suggestions based on test results
    private List<String> generateSuggestions(List<TestResult> results) {

        List<String> suggestions = new ArrayList<>();

        for (TestResult r : results) {

            // Case 1: Server error
            if (r.getStatus() == 500) {
                suggestions.add("Improve error handling to prevent server crashes");
            }

            // Case 2: Not found
            if (r.getStatus() == 404) {
                suggestions.add("Check API endpoint URL and routing configuration");
            }

            // Case 3: Slow response
            if (r.getResponseTime() > 1000) {
                suggestions.add("Optimize API performance or add caching");
            }
        }

        // Remove duplicates
        return suggestions.stream().distinct().toList();
    }
}