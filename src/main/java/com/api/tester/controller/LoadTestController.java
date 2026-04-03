package com.api.tester.controller;

import com.api.tester.model.LoadTestReport;
import com.api.tester.model.LoadTestRequest;
import com.api.tester.service.LoadTestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class LoadTestController {

    private final LoadTestService loadTestService;

    public LoadTestController(LoadTestService loadTestService) {
        this.loadTestService = loadTestService;
    }

    @PostMapping("/load-test")
    public ResponseEntity<LoadTestReport> runLoadTest(@RequestBody LoadTestRequest request) {

        // Validate — url and method are required
        if (request.getUrl() == null || request.getUrl().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

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

        LoadTestReport report = loadTestService.runLoadTest(request);
        return ResponseEntity.ok(report);
    }
}