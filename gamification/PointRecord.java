package gamification;

public class PointRecord {

    private String studentId;
    private int points;
    private String reason;
    private long timestamp;

    public PointRecord(String studentId, int points, String reason) {
        this.studentId = studentId;
        this.points = points;
        this.reason = reason;
        this.timestamp = System.currentTimeMillis();
    }

    public String getStudentId() {
        return studentId;
    }

    public int getPoints() {
        return points;
    }

    public String getReason() {
        return reason;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "PointRecord[" + studentId + " | +" + points + " | " + reason + "]";
    }
}
