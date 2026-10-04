public class Building {
    private final String name;
    private final double landArea;
    private final int floors;

    public Building(String name, double landArea, int floors) {
        this.name = name;
        this.landArea = landArea;
        this.floors = floors;
    }

    public String getName() {
        return name;
    }

    public double getLandArea() {
        return landArea;
    }

    public int getFloors() {
        return floors;
    }
}