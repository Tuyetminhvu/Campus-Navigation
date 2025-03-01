package campusMap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The CampusMap class is responsible for managing data related to the campus buildings and the paths between them.
 * It loads building information and path distances from a CSV file, stores the data in appropriate data structures, 
 * and provides methods to retrieve the buildings, paths, and building indices.
 * 
 * @author Minh Vu + Jaylin Mendoza + Sheila Ortiz
 */
public class CampusMap {
    private ArrayList<Path> distances;
    private ArrayList<Buildings> buildings;
    private HashMap<String, Integer> buildingMap;

    /**
     * Constructor to initialize the CampusMap object.
     * Initializes the distances, buildings, and buildingMap data structures.
     */
    public CampusMap() {
        distances = new ArrayList<>();
        buildings = new ArrayList<>();
        buildingMap = new HashMap<>();
    }

    /**
     * Loads campus map data from a CSV file.
     * The CSV file should contain rows of the form: start building, end building, distance.
     * The method reads the file line by line, extracts building names and distances, and populates the 
     * `buildings`, `distances`, and `buildingMap`.
     * 
     * @param filePath
     */
    public void loadData(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
            
                if (parts.length == 3) {
                    String startLocation = parts[0].trim();
                    String endLocation = parts[1].trim();
                    int distance = Integer.parseInt(parts[2].trim());

                  
                    if (!buildingMap.containsKey(startLocation)) {
                        buildings.add(new Buildings(startLocation));
                        buildingMap.put(startLocation, buildings.size() - 1);
                    }
                    if (!buildingMap.containsKey(endLocation)) {
                        buildings.add(new Buildings(endLocation));
                        buildingMap.put(endLocation, buildings.size() - 1);
                    }
                    distances.add(new Path(startLocation, endLocation, distance));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading data from file: " + filePath);
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.err.println("Error parsing distance value. Please check the CSV file format.");
        }
    }

    /**
     * Returns the list of all buildings in the campus map.
     * 
     * @return A list of `Buildings` objects representing all the buildings on the campus.
     */
    public ArrayList<Buildings> getBuildings() {
        return buildings;
    }

    /**
     * Returns the list of all paths (distances) between buildings in the campus map.
     * 
     * @return A list of `Path` objects representing the distances between buildings.
     */
    public ArrayList<Path> getDistances() {
        return distances;
    }

    /**
     * Retrieves the index of a building given its name.
     * 
     * @param buildingName The name of the building to look up.
     * @return The index of the building in the `buildings` list, or null if the building is not found.
     */
    public Integer getBuildingIndex(String buildingName) {
        return buildingMap.get(buildingName.trim());
    }

    /**
     * Main method for testing the functionality of the CampusMap class.
     */
    public static void main(String[] args) {
        CampusMap campusMap = new CampusMap();
        campusMap.loadData("src/campusMap/Resources/building_connections.csv");

        System.out.println("All Paths:");
        for (Path path : campusMap.getDistances()) {
            System.out.println(path);
        }

        System.out.println("\nBuildings:");
        for (Buildings building : campusMap.getBuildings()) {
            System.out.println(building.getName());
        }

        System.out.println("\nBuilding Map: " + campusMap.buildingMap);
    }
}
