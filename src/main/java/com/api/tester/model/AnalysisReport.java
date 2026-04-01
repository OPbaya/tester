package com.api.tester.model;

import java.util.List;

public class AnalysisReport {

    private List<TestResult> results;
    private List<String> suggestions;
    private String overallStatus;
    private String summary;

    public AnalysisReport() {
    }
    public AnalysisReport(List<TestResult> results, String overallStatus, String summary, List<String> suggestions) {
        this.results = results;
        this.overallStatus = overallStatus;
        this.summary = summary;
        this.suggestions = suggestions;
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

    public List<String> getSuggestions() {
        return suggestions;
    }
}