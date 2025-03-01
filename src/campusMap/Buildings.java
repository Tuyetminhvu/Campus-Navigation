package campusMap;

/**
 * The Buildings class represents a building on the campus.
 * It stores the building's name and provides access to it.
 * 
 * @author  Jaylin Mendoza
 */
public class Buildings {
    private String name;

    /**
     * Constructs a Buildings object with the specified name.
     * 
     * @param name the name of the building
     */
    public Buildings(String name) {
        this.name = name;
    }

    /**
     * Retrieves the name of the building.
     * 
     * @return the name of the building
     */
    public String getName() {
        return name;
    }

    /**
     * Returns a string representation of the building.
     * 
     * @return the name of the building as a string
     */
    @Override
    public String toString() {
        return name;
    }
}
