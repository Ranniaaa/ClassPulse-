package gamification;

import java.util.*;

public class Leaderboard {

    private Map<String, Integer> studentScores;

    public Leaderboard() {
        this.studentScores = new HashMap<>();
    }

    public void addOrUpdateScore(String studentId, int totalPoints) {
        studentScores.put(studentId, totalPoints);
    }

    public List<Map.Entry<String, Integer>> getRankedList() {
        List<Map.Entry<String, Integer>> list = new ArrayList<>(studentScores.entrySet());
        list.sort((a, b) -> b.getValue() - a.getValue());
        return list;
    }

    public int getRank(String studentId) {
        List<Map.Entry<String, Integer>> ranked = getRankedList();
        for (int i = 0; i < ranked.size(); i++) {
            if (ranked.get(i).getKey().equals(studentId)) {
                return i + 1;
            }
        }
        return -1;
    }

    public Map<String, Integer> getStudentScores() {
        return studentScores;
    }
}
