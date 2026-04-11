package gamification;

public class TestGamification {

    public static void main(String[] args) {

        GamificationEngine engine = new GamificationEngine();
        ParticipationCalculator calculator = new ParticipationCalculator();

        try {
            // ─── ADD POINTS ──────────────────────────────────────
            System.out.println("========== ADDING POINTS ==========");
            engine.addPoints("student_01", 10, "correct answer");
            engine.addPoints("student_02", 10, "correct answer");
            engine.addPoints("student_01", 10, "correct answer");
            engine.addPoints("student_03", 10, "correct answer");

            // ─── SPEED BONUS ─────────────────────────────────────
            System.out.println("\n========== SPEED BONUS ==========");
            engine.addPointsWithSpeedBonus("student_01", 10, 4, "quiz answer");
            engine.addPointsWithSpeedBonus("student_02", 10, 15, "quiz answer");
            engine.addPointsWithSpeedBonus("student_03", 10, 25, "quiz answer");

            // ─── TOTAL POINTS ────────────────────────────────────
            System.out.println("\n========== TOTAL POINTS ==========");
            System.out.println("student_01 total: " + engine.getTotalPoints("student_01"));
            System.out.println("student_02 total: " + engine.getTotalPoints("student_02"));
            System.out.println("student_03 total: " + engine.getTotalPoints("student_03"));

            // ─── LEADERBOARD ─────────────────────────────────────
            System.out.println("\n========== LEADERBOARD ==========");
            Leaderboard lb = engine.getLeaderboard();
            int rank = 1;
            for (var entry : lb.getRankedList()) {
                System.out.println("#" + rank + " " + entry.getKey()
                        + " → " + entry.getValue() + " pts");
                rank++;
            }

            // ─── LEVELS ──────────────────────────────────────────
            System.out.println("\n========== LEVELS ==========");
            System.out.println("student_01 level: "
                    + engine.getCurrentLevel("student_01").getName());
            System.out.println("student_02 level: "
                    + engine.getCurrentLevel("student_02").getName());
            System.out.println("student_03 level: "
                    + engine.getCurrentLevel("student_03").getName());

            // ─── BADGES ──────────────────────────────────────────
            System.out.println("\n========== BADGES ==========");
            for (Badge b : engine.getEarnedBadges("student_01")) {
                System.out.println("student_01 earned: " + b.getName());
            }

            // ─── PARTICIPATION GRADE ─────────────────────────────
            System.out.println("\n========== PARTICIPATION GRADE ==========");
            System.out.println(calculator.getParticipationSummary(
                    "student_01", engine.getTotalPoints("student_01")));
            System.out.println(calculator.getParticipationSummary(
                    "student_02", engine.getTotalPoints("student_02")));
            System.out.println(calculator.getParticipationSummary(
                    "student_03", engine.getTotalPoints("student_03")));

            // ─── EXCEPTION TEST ───────────────────────────────────
            System.out.println("\n========== EXCEPTION TEST ==========");
            engine.addPoints("student_01", -5, "bad input");

        } catch (InvalidScoreException e) {
            System.out.println("✔ InvalidScoreException caught: " + e.getMessage());
        } catch (RankingGenerationException e) {
            System.out.println("✔ RankingGenerationException caught: " + e.getMessage());
        }
    }
}
