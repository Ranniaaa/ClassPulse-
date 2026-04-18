package gamification;

import java.util.*;

public class GamificationEngine {

    private Map<String, Integer> studentTotalPoints;
    private List<PointRecord> allRecords;
    private Leaderboard leaderboard;
    private List<Badge> availableBadges;
    private List<Level> availableLevels;

    public GamificationEngine() {
        this.studentTotalPoints = new HashMap<>();
        this.allRecords = new ArrayList<>();
        this.leaderboard = new Leaderboard();
        this.availableBadges = initBadges();
        this.availableLevels = initLevels();
    }

    private List<Badge> initBadges() {
        List<Badge> badges = new ArrayList<>();
        badges.add(new Badge("First Step", "Earned your first points", 10));
        badges.add(new Badge("Active", "Reached 50 points", 50));
        badges.add(new Badge("Challenger", "Reached 100 points", 100));
        badges.add(new Badge("Expert", "Reached 200 points", 200));
        badges.add(new Badge("Champion", "Reached 500 points", 500));
        return badges;
    }

    private List<Level> initLevels() {
        List<Level> levels = new ArrayList<>();
        levels.add(new Level("Beginner", 0));
        levels.add(new Level("Intermediate", 50));
        levels.add(new Level("Advanced", 150));
        levels.add(new Level("Expert", 300));
        levels.add(new Level("Master", 500));
        return levels;
    }

    public void addPoints(String studentId, int points, String reason)
            throws InvalidScoreException {
        if (points < 0) {
            throw new InvalidScoreException("Points cannot be negative. Got: " + points);
        }
        if (studentId == null || studentId.isEmpty()) {
            throw new InvalidScoreException("Student ID cannot be empty.");
        }
        PointRecord record = new PointRecord(studentId, points, reason);
        allRecords.add(record);
        int current = studentTotalPoints.getOrDefault(studentId, 0);
        int newTotal = current + points;
        studentTotalPoints.put(studentId, newTotal);
        leaderboard.addOrUpdateScore(studentId, newTotal);
        System.out.println("+ Points added: " + record);
    }

    public int calculateSpeedBonus(long responseTimeSeconds) {
        if (responseTimeSeconds <= 5) {
            return 10;
        }
        if (responseTimeSeconds <= 10) {
            return 7;
        }
        if (responseTimeSeconds <= 20) {
            return 4;
        }
        if (responseTimeSeconds <= 30) {
            return 2;
        }
        return 0;
    }

    public void addPointsWithSpeedBonus(String studentId, int basePoints,
            long responseTimeSeconds, String reason) throws InvalidScoreException {
        int bonus = calculateSpeedBonus(responseTimeSeconds);
        int total = basePoints + bonus;
        addPoints(studentId, total,
                reason + " (base: " + basePoints + " + speed bonus: " + bonus + ")");
    }

    public List<Badge> getEarnedBadges(String studentId) {
        int total = studentTotalPoints.getOrDefault(studentId, 0);
        List<Badge> earned = new ArrayList<>();
        for (Badge badge : availableBadges) {
            if (total >= badge.getPointsRequired()) {
                earned.add(badge);
            }
        }
        return earned;
    }

    public Level getCurrentLevel(String studentId) {
        int total = studentTotalPoints.getOrDefault(studentId, 0);
        Level current = availableLevels.get(0);
        for (Level level : availableLevels) {
            if (total >= level.getMinPoints()) {
                current = level;
            }
        }
        return current;
    }

    public Leaderboard getLeaderboard() throws RankingGenerationException {
        if (studentTotalPoints.isEmpty()) {
            throw new RankingGenerationException(
                    "Cannot generate leaderboard: no students have points yet."
            );
        }
        return leaderboard;
    }

    public int getTotalPoints(String studentId) {
        return studentTotalPoints.getOrDefault(studentId, 0);
    }

    public List<PointRecord> getAllRecords() {
        return allRecords;
    }

    public List<PointRecord> getRecordsForStudent(String studentId) {
        List<PointRecord> result = new ArrayList<>();
        for (PointRecord r : allRecords) {
            if (r.getStudentId().equals(studentId)) {
                result.add(r);
            }
        }
        return result;
    }

    public Map<String, Integer> getStudentTotalPoints() {
        return studentTotalPoints;
    }
}
