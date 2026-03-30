package com.api.tester.model;

import java.util.Map;

public class ApiRequest {

    private String url;
    private String method;
    private Map<String, String> headers;
    private Map<String, Object> body;

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }
    
    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, Object> getBody() {
        return body;
    }
}