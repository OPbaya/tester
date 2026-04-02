package com.api.tester.controller;

import com.api.tester.model.CompareReport;
import com.api.tester.model.HistoryEntry;
import com.api.tester.service.CompareService;
import com.api.tester.service.HistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/history")
public class HistoryController {

    private final HistoryService historyService;
    private final CompareService compareService;

    public HistoryController(HistoryService historyService, CompareService compareService ) {
        this.historyService = historyService;
        this.compareService = compareService;
    }

    // Get all history
    @GetMapping
    public List<HistoryEntry> getAllHistory() {
        return historyService.getAllHistory();
    }

    // Get history by URL
    @GetMapping("/search")
    public List<HistoryEntry> getHistoryByUrl(@RequestParam String url) {
        return historyService.getHistoryByUrl(url);
    }

    // Get single history entry
    @GetMapping("/{id}")
    public ResponseEntity<HistoryEntry> getHistoryById(@PathVariable String id) {
        Optional<HistoryEntry> entry = historyService.getHistoryById(id);
        return entry.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete history entry
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteHistory(@PathVariable String id) {
        historyService.deleteHistory(id);
        return ResponseEntity.ok("History entry deleted successfully");
    }

    // Compare two history entries
    @GetMapping("/compare/{id1}/{id2}")
    public ResponseEntity<CompareReport> compare(@PathVariable String id1, @PathVariable String id2) {

        Optional<HistoryEntry> entry1 = historyService.getHistoryById(id1);
        Optional<HistoryEntry> entry2 = historyService.getHistoryById(id2);

        if (entry1.isEmpty() || entry2.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        CompareReport report = compareService.compare(entry1.get(), entry2.get());
        return ResponseEntity.ok(report);
    }
}