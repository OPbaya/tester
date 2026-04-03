package com.api.tester.controller;

import com.api.tester.model.LoadTestHistory;
import com.api.tester.model.LoadTestReport;
import com.api.tester.model.LoadTestRequest;
import com.api.tester.repository.LoadTestHistoryRepository;
import com.api.tester.service.LoadTestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class LoadTestController {

    private final LoadTestService loadTestService;

    // Repository to save load test results to MongoDB
    private final LoadTestHistoryRepository loadTestHistoryRepository;

    public LoadTestController(LoadTestService loadTestService, LoadTestHistoryRepository loadTestHistoryRepository) {
        this.loadTestService = loadTestService;
        this.loadTestHistoryRepository = loadTestHistoryRepository;
    }

    @PostMapping("/load-test")
    public ResponseEntity<LoadTestReport> runLoadTest(@RequestBody LoadTestRequest request) {

        // Validate — url and method are required
        if (request.getUrl() == null || request.getUrl().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // Validate HTTP method
        if (request.getMethod() == null || request.getMethod().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // Validate mode — must be SEQUENTIAL or CONCURRENT
        String mode = request.getMode();
        if (mode == null ||
                (!mode.equalsIgnoreCase("SEQUENTIAL") &&
                        !mode.equalsIgnoreCase("CONCURRENT"))) {
            return ResponseEntity.badRequest().build();
        }

        // Validate request count — must be between 1 and 50
        if (request.getRequestCount() < 1 || request.getRequestCount() > 50) {
            return ResponseEntity.badRequest().build();
        }

        // Run the load test and get the report
        LoadTestReport report = loadTestService.runLoadTest(request);

        // Save the result to MongoDB so we can view it in history later
        // We create a LoadTestHistory document with all the request details + report
        LoadTestHistory history = new LoadTestHistory(
                request.getUrl(),
                request.getMethod(),
                request.getRequestCount(),
                request.getMode(),
                LocalDateTime.now(), // timestamp of when the test was run
                report
            );

        // Actually save it to the "load_test_history" collection in MongoDB
        loadTestHistoryRepository.save(history);

        return ResponseEntity.ok(report);
    }
}