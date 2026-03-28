package com.api.tester.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.api.tester.model.ApiResponse;
import org.springframework.http.ResponseEntity;

@Service
public class ApiTestService {

    private final WebClient webClient;

    // Constructor Injection (IMPORTANT)
    public ApiTestService(WebClient webClient) {
        this.webClient = webClient;
    }

    public ApiResponse testGetApi(String url) {

        long startTime = System.currentTimeMillis();

        try {
            //get request using WebClient
            ResponseEntity<String> response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .toEntity(String.class)
                    .block();

            long endTime = System.currentTimeMillis();

            return new ApiResponse(
                    response.getStatusCode().value(),
                    (endTime - startTime),
                    true,
                    null);

        } catch (Exception e) {

            long endTime = System.currentTimeMillis();

            return new ApiResponse(
                    500,
                    (endTime - startTime),
                    false,
                    e.getMessage());
        }
    }

}