package com.api.tester.service;

import com.api.tester.model.AnalysisReport;
import com.api.tester.model.ApiRequest;
import com.api.tester.model.HistoryEntry;
import com.api.tester.repository.HistoryRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class HistoryService {

    private final HistoryRepository historyRepository;

    public HistoryService(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    // Save a new history entry after every analysis
    public HistoryEntry saveHistory(ApiRequest request, AnalysisReport report) {
        HistoryEntry entry = new HistoryEntry(
                request.getUrl(),
                request.getMethod(),
                LocalDateTime.now(),
                report);
        return historyRepository.save(entry);
    }

    // Get all history entries latest first
    public List<HistoryEntry> getAllHistory() {
        return historyRepository.findAllByOrderByTestedAtDesc();
    }

    // Get history entries for a specific URL
    public List<HistoryEntry> getHistoryByUrl(String url) {
        return historyRepository.findByUrlOrderByTestedAtDesc(url);
    }

    // Get a single history entry by ID
    public Optional<HistoryEntry> getHistoryById(String id) {
        return historyRepository.findById(id);
    }

    // Delete a history entry by ID
    public void deleteHistory(String id) {
        historyRepository.deleteById(id);
    }
}