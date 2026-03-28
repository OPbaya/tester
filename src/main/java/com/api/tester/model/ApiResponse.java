package com.api.tester.model;

public class ApiResponse {

    private int status;
    private long responseTime;
    private boolean success;
    private String error;

    public ApiResponse(int status, long responseTime, boolean success, String error) {
        this.status = status;
        this.responseTime = responseTime;
        this.success = success;
        this.error = error;
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