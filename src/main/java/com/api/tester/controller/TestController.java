package com.api.tester.controller;

import com.api.tester.model.ApiResponse;
import com.api.tester.service.ApiTestService;

import java.util.Map;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TestController {

    private final ApiTestService apiTestService;

    public TestController(ApiTestService apiTestService) {
        this.apiTestService = apiTestService;
    }

    @PostMapping("/test")
    public ApiResponse testGetApi(@RequestBody Map<String, String> request) {
        String url = request.get("url");
        return apiTestService.testGetApi(url);
    }
}