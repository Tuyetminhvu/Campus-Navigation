package campusMap;

import java.io.File;

/**
 * Utility class for generating file paths for campus navigation maps.
 * 
 * @author Minh Vu
 */
public class FilePathSearch {

	/**
	 * Constructs the file path for a given building and floor.
	 *
	 * @param buildingCode The building code (e.g., "AAB", "CT").
	 * @param floorCode    The floor code (e.g., "1", "2").
	 * @return The file path as a String.
	 */
	public static String getFilePathFloor(String buildingCode, String floorCode) {
		String basePathFloor = "src/campusMap/Resources/mapFloor/";
		return basePathFloor + buildingCode + "_FP" + floorCode + ".png";
	}

	/**
	 * Constructs the file path for a route map between two buildings. If the file path is not exist, try the reverse because it duplicated.
	 *
	 * @param startBuilding The start building code (e.g., "AAB").
	 * @param endBuilding   The end building code (e.g., "CT").
	 * @return The file path as a String.
	 */
	public static String getFilePathWay(String startBuilding, String endBuilding) {
		String basePathWay = "src/campusMap/Resources/Path/";

		String filePath = basePathWay + startBuilding + "-" + endBuilding + ".png";

		if (fileExists(filePath)) {
			return filePath;
		}

		String reverseFilePath = basePathWay + endBuilding + "-" + startBuilding + ".png";
		if (fileExists(reverseFilePath)) {
			return reverseFilePath;
		}

		return "src/campusMap/Resources/campus.png";
	}

	/**
	 * Checks if a file exists at the specified path.
	 *
	 * @param filePath The file path to check.
	 * @return True if the file exists, false otherwise.
	 */
	public static boolean fileExists(String filePath) {
		File file = new File(filePath);
		return file.exists();
	}

	public static void main(String[] args) {

		String floorPath = getFilePathFloor("AAB", "1");
		System.out.println("Floor Map Path: " + floorPath);
		System.out.println("File exists: " + fileExists(floorPath));

		String routePath = getFilePathWay("AAB", "CT");
		System.out.println("Route Map Path: " + routePath);
		System.out.println("File exists: " + fileExists(routePath));
		
		String routePathReverse = getFilePathWay("CT", "AAB");
		System.out.println("Route Map Path: " + routePath);
		System.out.println("File exists: " + fileExists(routePath));
		
	}
}
