package com.api.tester.repository;

import com.api.tester.model.LoadTestHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LoadTestHistoryRepository extends MongoRepository<LoadTestHistory, String> {

    // Find all load test history entries for a specific URL, latest first
    List<LoadTestHistory> findByUrlOrderByTestedAtDesc(String url);

    // Find all entries ordered by latest first
    List<LoadTestHistory> findAllByOrderByTestedAtDesc();
}