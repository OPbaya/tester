package com.api.tester.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

// This tells MongoDB to store these documents in the "load_test_history" collection
@Document(collection = "load_test_history")
public class LoadTestHistory {

    @Id
    private String id; // MongoDB auto-generates this

    private String url;
    private String method;
    private int requestCount;
    private String mode; // "SEQUENTIAL" or "CONCURRENT"
    private LocalDateTime testedAt;
    private LoadTestReport report; // full report stored as nested document

    // No-arg constructor required by MongoDB for deserialization
    public LoadTestHistory() {
    }

    public LoadTestHistory(String url, String method, int requestCount,
            String mode, LocalDateTime testedAt, LoadTestReport report) {
        this.url = url;
        this.method = method;
        this.requestCount = requestCount;
        this.mode = mode;
        this.testedAt = testedAt;
        this.report = report;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }

    public int getRequestCount() {
        return requestCount;
    }

    public String getMode() {
        return mode;
    }

    public LocalDateTime getTestedAt() {
        return testedAt;
    }

    public LoadTestReport getReport() {
        return report;
    }
}