package gamification;

public class Level {

    private String name;
    private int minPoints;

    public Level(String name, int minPoints) {
        this.name = name;
        this.minPoints = minPoints;
    }

    public String getName() {
        return name;
    }

    public int getMinPoints() {
        return minPoints;
    }

    @Override
    public String toString() {
        return "Level[" + name + " - from " + minPoints + " pts]";
    }
}
