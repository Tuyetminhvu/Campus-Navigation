package campusMap;


import edu.princeton.cs.algs4.DijkstraSP;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;

/**
 * The SearchEngine class is responsible for finding the shortest path between two buildings on campus.
 * It constructs a graph representing the campus layout based on building distances and uses Dijkstra's
 * algorithm to compute the shortest path. Additionally, it calculates the total distance and approximate
 * walking time for a given path.
 * 
 * The class utilizes DijkstraSP from the Princeton algorithms library to perform the shortest path search.
 * @author Minh Vu + Jaylin Mendoza + Sheila Ortiz
 */
public class SearchEngine {
    private static final double WALKING_SPEED_METERS_PER_SEC = 1.25;
    private EdgeWeightedDigraph graph;
    private CampusMap campusMap;

    /**
     * Constructor to initialize the SearchEngine with a CampusMap.
     * The constructor sets up the campus map and builds the underlying graph representing the campus layout.
     *
     * @param campusMap The campus map containing building information and distances.
     */
    public SearchEngine(CampusMap campusMap) {
        this.campusMap = campusMap;
        this.graph = new EdgeWeightedDigraph(campusMap.getBuildings().size());
        createGraph();
    }

    /**
     * Creates a graph representing the campus by adding edges between buildings.
     * Each edge represents a path between two buildings, with the weight of the edge being the distance
     * between them.
     */
    private void createGraph() {
    	// Loop through all paths in the campus map and add corresponding edges to the graph
        for (Path path : campusMap.getDistances()) {
            Integer startIndex = campusMap.getBuildingIndex(path.getStartLocation());
            Integer endIndex = campusMap.getBuildingIndex(path.getEndLocation());
            if (startIndex != null && endIndex != null) {
                graph.addEdge(new DirectedEdge(startIndex, endIndex, path.getDistance()));
                graph.addEdge(new DirectedEdge(endIndex, startIndex, path.getDistance()));
            }
        }
    }

    /**
     * Finds the shortest path between two buildings using Dijkstra's algorithm.
     *
     * @param start The full name of the starting building.
     * @param end The full name of the destination building.
     * @return A String of building names representing the shortest path, or an empty list if no path is found.
     */
    public String findShortestPath(String start, String end) {
        int startIndex = campusMap.getBuildingIndex(start);
        int endIndex = campusMap.getBuildingIndex(end);

        if (startIndex == -1 || endIndex == -1) {
            return "Error: Invalid building name(s). Please check the input.";
        }

        // Compute shortest path using Dijkstra's algorithm
        DijkstraSP sp = new DijkstraSP(graph, startIndex);
        if (sp.hasPathTo(endIndex)) {
            StringBuilder result = new StringBuilder();
            for (DirectedEdge edge : sp.pathTo(endIndex)) {
                result.append(campusMap.getBuildings().get(edge.from()).getName()).append(" → ");
            }
            result.append(campusMap.getBuildings().get(endIndex).getName());

            // Calculate total distance and approximate walking time
            double totalDistance = sp.distTo(endIndex);
            double approximateTimeMinutes = (totalDistance / WALKING_SPEED_METERS_PER_SEC) / 60;

            return result.toString() +
                    "\n● Distance: " + String.format("%.2f meters", totalDistance) +
                    "\n● Approximate time: " + String.format("%.2f mins", approximateTimeMinutes);
        } else {
            return "No path found between " + start + " and " + end + ".";
        }
    }


    /**
     * Main method for testing the functionality of the SearchEngine.
     * It loads the campus map data and performs multiple shortest path searches.
     */
    public static void main(String[] args) {
        CampusMap campusMap = new CampusMap();
        campusMap.loadData("src/campusMap/Resources/building_connections.csv");

        SearchEngine searchEngine = new SearchEngine(campusMap);

        System.out.println(searchEngine.findShortestPath("AA - Alder Amphitheater", "AAB - Academic & Administration Building"));
        System.out.println(searchEngine.findShortestPath("GMBB - Gail Miller Business Building", "STC - Student Center"));
        
    }
}
