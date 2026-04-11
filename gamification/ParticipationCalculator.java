package gamification;

public class ParticipationCalculator {

    private static final int MAX_POINTS_FOR_FULL_GRADE = 500;
    private static final double MAX_GRADE = 20.0;

    // converts total points into a grade out of 20
    public double calculateGrade(int totalPoints) throws InvalidScoreException {
        if (totalPoints < 0) {
            throw new InvalidScoreException(
                    "Total points cannot be negative. Got: " + totalPoints
            );
        }

        if (totalPoints >= MAX_POINTS_FOR_FULL_GRADE) {
            return MAX_GRADE;
        }

        double grade = ((double) totalPoints / MAX_POINTS_FOR_FULL_GRADE) * MAX_GRADE;

        // round to 2 decimal places
        return Math.round(grade * 100.0) / 100.0;
    }

    // returns a summary string for a student
    public String getParticipationSummary(String studentId, int totalPoints)
            throws InvalidScoreException {

        double grade = calculateGrade(totalPoints);
        return "Student: " + studentId
                + " | Points: " + totalPoints
                + " | Participation Grade: " + grade + "/20";
    }
}
