package analytics;

import java.util.List;
import java.util.Map;

public class ReportGenerator {

    // Generate a simple report as a String
    public String generateReport(Map<String, Integer> studentScores, double participation) {
        StringBuilder report = new StringBuilder();
        report.append("=== ClassPulse Report ===\n\n");

        report.append("Participation: ").append(participation).append("%\n\n");

        report.append("Top Students:\n");

        // Sort students by score descending
        studentScores.entrySet()
                .stream()
                .sorted((a, b) -> b.getValue() - a.getValue())
                .limit(3)
                .forEach(e -> report.append(e.getKey()).append(": ").append(e.getValue()).append(" pts\n"));

        return report.toString();
    }

    // Temporary test
    public static void main(String[] args) {
        ReportGenerator rg = new ReportGenerator();
        Map<String, Integer> scores = Map.of(
                "Ali", 100,
                "Sara", 120,
                "Yacine", 90
        );
        double participation = 75.0;

        String report = rg.generateReport(scores, participation);
        System.out.println(report);
    }
}
