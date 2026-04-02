package com.api.tester.model;

import java.util.List;

public class CompareReport {

    private String verdict; // "Run 1 is better", "Run 2 is better", "Both are equal"
    private String successRateResult; // which run had better success rate
    private String responseTimeResult;// which run was faster
    private String issuesResult; // which run had fewer issues
    private String suggestionsResult; // which run had fewer suggestions
    private List<String> statusCodeChanges; // what status codes changed
    private int run1SuccessRate;
    private int run2SuccessRate;
    private long run1AvgResponseTime;
    private long run2AvgResponseTime;
    private int run1IssueCount;
    private int run2IssueCount;

    public CompareReport(String verdict, String successRateResult, String responseTimeResult,
            String issuesResult, String suggestionsResult, List<String> statusCodeChanges,
            int run1SuccessRate, int run2SuccessRate, long run1AvgResponseTime,
            long run2AvgResponseTime, int run1IssueCount, int run2IssueCount) {

        this.verdict = verdict;
        this.successRateResult = successRateResult;
        this.responseTimeResult = responseTimeResult;
        this.issuesResult = issuesResult;
        this.suggestionsResult = suggestionsResult;
        this.statusCodeChanges = statusCodeChanges;
        this.run1SuccessRate = run1SuccessRate;
        this.run2SuccessRate = run2SuccessRate;
        this.run1AvgResponseTime = run1AvgResponseTime;
        this.run2AvgResponseTime = run2AvgResponseTime;
        this.run1IssueCount = run1IssueCount;
        this.run2IssueCount = run2IssueCount;
    }

    public String getVerdict() {
        return verdict;
    }

    public String getSuccessRateResult() {
        return successRateResult;
    }

    public String getResponseTimeResult() {
        return responseTimeResult;
    }

    public String getIssuesResult() {
        return issuesResult;
    }

    public String getSuggestionsResult() {
        return suggestionsResult;
    }

    public List<String> getStatusCodeChanges() {
        return statusCodeChanges;
    }

    public int getRun1SuccessRate() {
        return run1SuccessRate;
    }

    public int getRun2SuccessRate() {
        return run2SuccessRate;
    }

    public long getRun1AvgResponseTime() {
        return run1AvgResponseTime;
    }

    public long getRun2AvgResponseTime() {
        return run2AvgResponseTime;
    }

    public int getRun1IssueCount() {
        return run1IssueCount;
    }

    public int getRun2IssueCount() {
        return run2IssueCount;
    }
}