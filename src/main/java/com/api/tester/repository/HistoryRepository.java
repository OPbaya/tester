package com.api.tester.repository;

import com.api.tester.model.HistoryEntry;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HistoryRepository extends MongoRepository<HistoryEntry, String> {

    // Fetch all history entries for a specific URL
    List<HistoryEntry> findByUrlOrderByTestedAtDesc(String url);

    // Fetch all entries ordered by latest first
    List<HistoryEntry> findAllByOrderByTestedAtDesc();
}