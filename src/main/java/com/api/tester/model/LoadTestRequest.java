package com.api.tester.model;

import java.util.Map;

public class LoadTestRequest {

    private String url;
    private String method;
    private Map<String, String> headers;
    private Object body;
    private int requestCount;
    private String mode; // "SEQUENTIAL" or "CONCURRENT"

    public LoadTestRequest() {
    }

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Object getBody() {
        return body;
    }

    public int getRequestCount() {
        return requestCount;
    }

    public String getMode() {
        return mode;
    }
}