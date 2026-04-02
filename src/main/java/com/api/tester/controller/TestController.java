package com.api.tester.controller;

// import com.api.tester.config.CorsConfig;
import com.api.tester.model.AnalysisReport;
import com.api.tester.model.ApiRequest;
import com.api.tester.model.ApiResponse;
import com.api.tester.model.TestResult;
import com.api.tester.service.ApiTestService;
import com.api.tester.service.HistoryService;

import jakarta.annotation.PostConstruct;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;

// @CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api")
public class TestController {
    @Autowired
    private MongoTemplate mongoTemplate;

    @PostConstruct
    public void checkDb() {
        System.out.println("DB Name: " + mongoTemplate.getDb().getName());
    }

    private final ApiTestService apiTestService;
    private final HistoryService historyService;

    public TestController(ApiTestService apiTestService, HistoryService historyService) {
        this.apiTestService = apiTestService;
        this.historyService = historyService;
    }

    @PostMapping("/test")
    public ApiResponse testGetApi(@RequestBody Map<String, String> request) {
        String url = request.get("url");
        return apiTestService.testGetApi(url);
    }

    // @GetMapping("/test-multiple")
    // public List<ApiResponse> testMultiple(@RequestParam String url) {
    // return apiTestService.runMultipleTests(url);
    // }

    // GET
    // http://localhost:8080/api/analyze?url=https://jsonplaceholder.typicode.com/posts
    // @GetMapping("/analyze")
    // public List<TestResult> testMultiple(@RequestParam String url) {
    // return apiTestService.runMultipleTests(url);
    // }

    // GET
    // http://localhost:8080/api/full-analysis?url=https://jsonplaceholder.typicode.com/posts
    // @GetMapping("/full-analysis")
    // public AnalysisReport fullAnalysis(@RequestParam String url) {
    // return apiTestService.analyzeFullApi(url);
    // }

    // @PostMapping("/full-analysis")
    // public AnalysisReport fullAnalysis(@RequestBody ApiRequest request) {
    // return apiTestService.analyzeFullApi(request);
    // }

    @PostMapping("/full-analysis")
    public AnalysisReport fullAnalysis(@RequestBody ApiRequest request) {
        AnalysisReport report = apiTestService.analyzeFullApi(request);
        // return apiTestService.analyzeFullApi(request);
        historyService.saveHistory(request, report); // auto save to MongoDB
        return report;
    }
}