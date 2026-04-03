package com.api.tester.model;

public class LoadTestResult {

    private int requestNumber;
    private int status;
    private long responseTime;
    private boolean success;
    private String error;

    // No-arg constructor required for MongoDB deserialization of nested documents
    public LoadTestResult() {
    }

    public LoadTestResult(int requestNumber, int status, long responseTime, boolean success, String error) {
        this.requestNumber = requestNumber;
        this.status = status;
        this.responseTime = responseTime;
        this.success = success;
        this.error = error;
    }

    public int getRequestNumber() {
        return requestNumber;
    }

    public int getStatus() {
        return status;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getError() {
        return error;
    }
}