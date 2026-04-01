package com.api.tester.model;

public class TestResult {

    private String testName;
    private int status;
    private boolean success;
    private String issue;
    private String err;
    private long responseTime;
    
    // No-arg constructor for MongoDB
    public TestResult() {
    }

    public TestResult(String testName, int status, boolean success, String issue, String error, long responseTime) {
        this.testName = testName;
        this.status = status;
        this.success = success;
        this.issue = issue;
        this.err= error;
        this.responseTime = responseTime;
    }

    public TestResult(Object testName2, int status2, boolean success2, String analyzeIssue, String error, long responseTime2) {
        //TODO Auto-generated constructor stub
    }

    public String getTestName() {
        return testName;
    }

    public int getStatus() {
        return status;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getIssue() {
        return issue;
    }
    
    public String getError() {
        return err;
    }
    public long getResponseTime() {
        return responseTime;
    }
}