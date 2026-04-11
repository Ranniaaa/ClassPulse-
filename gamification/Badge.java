package gamification;

public class Badge {

    private String name;
    private String description;
    private int pointsRequired;

    public Badge(String name, String description, int pointsRequired) {
        this.name = name;
        this.description = description;
        this.pointsRequired = pointsRequired;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getPointsRequired() {
        return pointsRequired;
    }

    @Override
    public String toString() {
        return "Badge[" + name + " - requires " + pointsRequired + " pts]";
    }
}
