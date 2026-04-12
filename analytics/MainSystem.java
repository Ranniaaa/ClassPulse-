package analytics;

import java.util.*;

public class MainSystem {

    public static void main(String[] args) {

        // 1. Create objects
        AnalyticsManager analytics = new AnalyticsManager();
        ReportGenerator reportGen = new ReportGenerator();
        FileManager fileManager = new FileManager();

        // 2. Simulate data 
        Map<String, Integer> scores = fileManager.loadScores("scores.txt");

        int totalStudents = 20;
        int activeStudents = 15;

        // 3. Analytics
        double participation = analytics.calculateParticipation(totalStudents, activeStudents);

        // 4. Generate report
        String report = reportGen.generateReport(scores, participation);

        // 5. Display report
        System.out.println(report);

        // 6. Save report to file
        fileManager.saveToFile(report, "report.txt");

        System.out.println("Report saved to file!");
     
       // Question analytics
        QuestionAnalytics qa = new QuestionAnalytics();

        Map<String, Integer> correct = new HashMap<>();
        correct.put("Q1", 5);
        correct.put("Q2", 15);
        correct.put("Q3", 3);

        Map<String, Integer> total = new HashMap<>();
        total.put("Q1", 20);
        total.put("Q2", 20);
        total.put("Q3", 20);

        List<String> difficult = qa.getDifficultQuestions(correct, total);

        System.out.println("\nDifficult Questions:");
        for (String q : difficult) {
        System.out.println(q);
        }
    }    
}
