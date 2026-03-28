package com.api.tester.model;

import java.util.List;

public class AnalysisReport {

    private List<TestResult> results;
    private String overallStatus;
    private String summary;

    public AnalysisReport(List<TestResult> results, String overallStatus, String summary) {
        this.results = results;
        this.overallStatus = overallStatus;
        this.summary = summary;
    }

    public List<TestResult> getResults() {
        return results;
    }

    public String getOverallStatus() {
        return overallStatus;
    }

    public String getSummary() {
        return summary;
    }
}