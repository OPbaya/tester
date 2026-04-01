package com.api.tester.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "history")
public class HistoryEntry {

    @Id
    private String id;

    private String url;
    private String method;
    private LocalDateTime testedAt;
    private AnalysisReport report;

    public HistoryEntry() {
    }

    public HistoryEntry(String url, String method, LocalDateTime testedAt, AnalysisReport report) {
        this.url = url;
        this.method = method;
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

    public LocalDateTime getTestedAt() {
        return testedAt;
    }

    public AnalysisReport getReport() {
        return report;
    }
}