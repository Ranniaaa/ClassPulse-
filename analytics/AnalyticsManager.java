package analytics;

import java.util.*;

public class AnalyticsManager {

    // 1. Calculate participation %
    //Participation =(number of students who answered / total students) × 100

    public double calculateParticipation(int totalStudents, int activeStudents) {
        if (totalStudents == 0) return 0;
        return (activeStudents * 100.0) / totalStudents;
    }

    // 2. Get top 3 students by score
    public List<String> getTopStudents(Map<String, Integer> studentScores) {
        List<Map.Entry<String, Integer>> list = new ArrayList<>(studentScores.entrySet());

        // sort descending
        list.sort((a, b) -> b.getValue() - a.getValue());

        List<String> topStudents = new ArrayList<>();
        for (int i = 0; i < Math.min(3, list.size()); i++) {
            topStudents.add(list.get(i).getKey());
        }
        return topStudents;
    }

    // 3. Temporary test
    public static void main(String[] args) {
        AnalyticsManager am = new AnalyticsManager();

        double participation = am.calculateParticipation(20, 15);
        System.out.println("Participation: " + participation + "%");

        Map<String, Integer> scores = new HashMap<>();
        scores.put("Ali", 100);
        scores.put("Sara", 120);
        scores.put("Yacine", 90);

        System.out.println("Top students: " + am.getTopStudents(scores));
    }
}
