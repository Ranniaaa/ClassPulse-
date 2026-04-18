package gamification;

import analytics.AnalyticsManager;
import analytics.FileManager;
import analytics.ReportGenerator;
import java.util.List;
import java.util.Map;

public class AnalyticsBridge {

    private GamificationEngine engine;
    private AnalyticsManager analyticsManager;
    private ReportGenerator reportGenerator;
    private FileManager fileManager;

    public AnalyticsBridge(GamificationEngine engine) {
        this.engine = engine;
        this.analyticsManager = new AnalyticsManager();
        this.reportGenerator = new ReportGenerator();
        this.fileManager = new FileManager();
    }

    // Generate full report using Yousra's ReportGenerator
    public String generateReport(int totalStudents) {
        Map<String, Integer> scores = engine.getStudentTotalPoints();
        int activeStudents = scores.size();
        double participation = analyticsManager
                .calculateParticipation(totalStudents, activeStudents);
        return reportGenerator.generateReport(scores, participation);
    }

    // Save report to file
    public void saveReport(String filename, int totalStudents) {
        String report = generateReport(totalStudents);
        fileManager.saveToFile(report, filename);
        System.out.println("Report saved to " + filename);
    }

    // Get top 3 students
    public List<String> getTopStudents() {
        return analyticsManager.getTopStudents(
                engine.getStudentTotalPoints());
    }
}
