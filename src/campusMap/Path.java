package campusMap;

/**
 * The Path class represents a connection between two campus buildings
 * with a specified distance.
 * @author Sheila Ortiz
 */
public class Path {
    private String startLocation;
    private String endLocation;
    private double distance;

    /**
     * Constructs a Path object with the given start location, end location, and distance.
     * 
     * @param startLocation the name of the starting building
     * @param endLocation the name of the destination building
     * @param distance the distance between the two locations (must be positive)
     * @throws IllegalArgumentException if the distance is negative
     */
    public Path(String startLocation, String endLocation, double distance) throws IllegalArgumentException {
        if (distance < 0) {
            throw new IllegalArgumentException("Distance should be positive!");
        }
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.distance = distance;
    }

    /**
     * Gets the starting location of the path.
     * 
     * @return the name of the starting building
     */
    public String getStartLocation() {
        return startLocation;
    }

    /**
     * Gets the destination location of the path.
     * 
     * @return the name of the destination building
     */
    public String getEndLocation() {
        return endLocation;
    }

    /**
     * Gets the distance of the path.
     * 
     * @return the distance between the two buildings
     */
    public double getDistance() {
        return distance;
    }

    /**
     * Returns a string representation of the Path object.
     * 
     * @return a string in the format "From: <startLocation> | To: <endLocation> | Distance: <distance>"
     */
    @Override
    public String toString() {
        return "From: " + startLocation + " | To: " + endLocation + " | Distance: " + distance;
    }
}
