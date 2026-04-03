package com.api.tester.model;

import java.util.List;
import java.util.Map;

public class LoadTestReport {

    private List<LoadTestResult> results;

    // Basic stats
    private int totalRequests;
    private int successCount;
    private int failureCount;
    private double successRate;

    // Response time stats
    private long minResponseTime;
    private long maxResponseTime;
    private long avgResponseTime;

    // Status code breakdown — e.g { "200": 18, "404": 2 }
    private Map<Integer, Long> statusCodeBreakdown;

    // Trend — only meaningful for sequential mode
    // "GETTING_SLOWER", "GETTING_FASTER", "STABLE"
    private String responseTimeTrend;

    // Overall verdict
    private String verdict; // "STABLE", "UNSTABLE", "SLOW"
    private String summary;

    public LoadTestReport() {
    }

    public LoadTestReport(List<LoadTestResult> results, int totalRequests, int successCount,
            int failureCount, double successRate, long minResponseTime, long maxResponseTime,
            long avgResponseTime, Map<Integer, Long> statusCodeBreakdown,
            String responseTimeTrend, String verdict, String summary) {

        this.results = results;
        this.totalRequests = totalRequests;
        this.successCount = successCount;
        this.failureCount = failureCount;
        this.successRate = successRate;
        this.minResponseTime = minResponseTime;
        this.maxResponseTime = maxResponseTime;
        this.avgResponseTime = avgResponseTime;
        this.statusCodeBreakdown = statusCodeBreakdown;
        this.responseTimeTrend = responseTimeTrend;
        this.verdict = verdict;
        this.summary = summary;
    }

    public List<LoadTestResult> getResults() {
        return results;
    }

    public int getTotalRequests() {
        return totalRequests;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public int getFailureCount() {
        return failureCount;
    }

    public double getSuccessRate() {
        return successRate;
    }

    public long getMinResponseTime() {
        return minResponseTime;
    }

    public long getMaxResponseTime() {
        return maxResponseTime;
    }

    public long getAvgResponseTime() {
        return avgResponseTime;
    }

    public Map<Integer, Long> getStatusCodeBreakdown() {
        return statusCodeBreakdown;
    }

    public String getResponseTimeTrend() {
        return responseTimeTrend;
    }

    public String getVerdict() {
        return verdict;
    }

    public String getSummary() {
        return summary;
    }
}