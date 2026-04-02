package com.api.tester.service;

import com.api.tester.model.CompareReport;
import com.api.tester.model.HistoryEntry;
import com.api.tester.model.TestResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CompareService {

    public CompareReport compare(HistoryEntry entry1, HistoryEntry entry2) {

        List<TestResult> results1 = entry1.getReport().getResults();
        List<TestResult> results2 = entry2.getReport().getResults();

        // 1. Success rate
        int successRate1 = calculateSuccessRate(results1);
        int successRate2 = calculateSuccessRate(results2);
        String successRateResult = compareSuccessRate(successRate1, successRate2);

        // 2. Average response time
        long avgTime1 = calculateAvgResponseTime(results1);
        long avgTime2 = calculateAvgResponseTime(results2);
        String responseTimeResult = compareResponseTime(avgTime1, avgTime2);

        // 3. Issues count
        int issueCount1 = countIssues(results1);
        int issueCount2 = countIssues(results2);
        String issuesResult = compareIssues(issueCount1, issueCount2);

        // 4. Suggestions count
        int suggestionCount1 = entry1.getReport().getSuggestions().size();
        int suggestionCount2 = entry2.getReport().getSuggestions().size();
        String suggestionsResult = compareSuggestions(suggestionCount1, suggestionCount2);

        // 5. Status code changes
        List<String> statusCodeChanges = compareStatusCodes(results1, results2);

        // 6. Overall verdict
        String verdict = generateVerdict(successRate1, successRate2, avgTime1, avgTime2,
                issueCount1, issueCount2);

        return new CompareReport(
                verdict,
                successRateResult,
                responseTimeResult,
                issuesResult,
                suggestionsResult,
                statusCodeChanges,
                successRate1, successRate2,
                avgTime1, avgTime2,
                issueCount1, issueCount2);
    }

    private int calculateSuccessRate(List<TestResult> results) {
        if (results.isEmpty())
            return 0;
        long passed = results.stream().filter(TestResult::isSuccess).count();
        return (int) ((passed * 100) / results.size());
    }

    private long calculateAvgResponseTime(List<TestResult> results) {
        if (results.isEmpty())
            return 0;
        return (long) results.stream()
                .mapToLong(TestResult::getResponseTime)
                .average()
                .orElse(0);
    }

    private int countIssues(List<TestResult> results) {
        return (int) results.stream()
                .filter(r -> r.getIssue() != null && !r.getIssue().equals("No issue"))
                .count();
    }

    private String compareSuccessRate(int rate1, int rate2) {
        if (rate1 > rate2)
            return "Run 1 had a better success rate (" + rate1 + "% vs " + rate2 + "%)";
        if (rate2 > rate1)
            return "Run 2 had a better success rate (" + rate2 + "% vs " + rate1 + "%)";
        return "Both runs had the same success rate (" + rate1 + "%)";
    }

    private String compareResponseTime(long time1, long time2) {
        if (time1 < time2)
            return "Run 1 was faster (" + time1 + "ms vs " + time2 + "ms)";
        if (time2 < time1)
            return "Run 2 was faster (" + time2 + "ms vs " + time1 + "ms)";
        return "Both runs had the same average response time (" + time1 + "ms)";
    }

    private String compareIssues(int issues1, int issues2) {
        if (issues1 < issues2)
            return "Run 1 had fewer issues (" + issues1 + " vs " + issues2 + ")";
        if (issues2 < issues1)
            return "Run 2 had fewer issues (" + issues2 + " vs " + issues1 + ")";
        return "Both runs had the same number of issues (" + issues1 + ")";
    }

    private String compareSuggestions(int s1, int s2) {
        if (s1 < s2)
            return "Run 1 had fewer suggestions (" + s1 + " vs " + s2 + ")";
        if (s2 < s1)
            return "Run 2 had fewer suggestions (" + s2 + " vs " + s1 + ")";
        return "Both runs had the same number of suggestions (" + s1 + ")";
    }

    // Compare status codes test by test
    private List<String> compareStatusCodes(List<TestResult> results1, List<TestResult> results2) {
        List<String> changes = new ArrayList<>();
        int limit = Math.min(results1.size(), results2.size());

        for (int i = 0; i < limit; i++) {
            TestResult r1 = results1.get(i);
            TestResult r2 = results2.get(i);

            if (r1.getStatus() != r2.getStatus()) {
                changes.add(r1.getTestName() + ": status changed from "
                        + r1.getStatus() + " to " + r2.getStatus());
            }
        }

        if (changes.isEmpty()) {
            changes.add("No status code changes between runs");
        }

        return changes;
    }

    // Generate an overall verdict based on scores
    private String generateVerdict(int successRate1, int successRate2,
            long avgTime1, long avgTime2, int issues1, int issues2) {

        int score1 = 0;
        int score2 = 0;

        // Success rate — most important
        if (successRate1 > successRate2)
            score1 += 3;
        else if (successRate2 > successRate1)
            score2 += 3;

        // Response time
        if (avgTime1 < avgTime2)
            score1 += 2;
        else if (avgTime2 < avgTime1)
            score2 += 2;

        // Issues count
        if (issues1 < issues2)
            score1 += 1;
        else if (issues2 < issues1)
            score2 += 1;

        if (score1 > score2)
            return "Run 1 is better overall";
        if (score2 > score1)
            return "Run 2 is better overall";
        return "Both runs are equal";
    }
}