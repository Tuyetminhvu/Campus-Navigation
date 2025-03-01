package campusMap;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import javax.swing.border.LineBorder;

/**
 * GUIDemo class represents the graphical user interface for a campus navigation
 * system.
 * 
 * <p>
 * This application allows users to:
 * <ul>
 * <li>Select start and end locations to find the shortest route.</li>
 * <li>View building and floor details.</li>
 * <li>Display navigation maps dynamically.</li>
 * </ul>
 * 
 * <p>
 * The GUI includes features such as dropdowns for building selection, buttons
 * for interaction, and a map panel for visualizing routes or building floors.
 * 
 * <p>
 * Author: Minh Vu, Jaylin Mendoza, Sheila Ortiz <br>
 * Version: 1.0 <br>
 * Year: 2024
 */
public class GUIDemo {
	private JFrame frame;
	private CampusMap campusMap;
	private SearchEngine searchEngine;

	/**
	 * Initializes the GUI with specified dimensions and sets up the navigation
	 * components.
	 * 
	 * @param h The height of the main frame.
	 * @param w The width of the main frame.
	 */
	public GUIDemo(int h, int w) {
		frame = new JFrame("Campus Navigation");
		frame.setSize(w, h);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		this.campusMap = new CampusMap();
		this.campusMap.loadData("src/campusMap/Resources/building_connections.csv");
		this.searchEngine = new SearchEngine(this.campusMap);
	}

	/**
	 * Creates the top panel of the GUI, including a title and menu options.
	 * 
	 * @return A JPanel configured with the top fragment components.
	 */
	private JPanel createTopFragment() {
		JPanel topPanel = new JPanel();
		topPanel.setBackground(new Color(109, 153, 57));
		topPanel.setPreferredSize(new Dimension(1250, 50));
		topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

		JLabel topTitleLabel = new JLabel("Campus Navigation System");
		topTitleLabel.setFont(new Font("Arial", Font.BOLD, 20));
		topTitleLabel.setForeground(Color.WHITE);
		topPanel.add(topTitleLabel);

		JButton menuButton = new JButton("☰ Menu");
		menuButton.setFocusPainted(false);
		menuButton.setBackground(Color.WHITE);
		menuButton.setFont(new Font("Arial", Font.PLAIN, 14));

		JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem aboutItem = new JMenuItem("About");
		aboutItem.addActionListener(e -> showAboutDialog());
		popupMenu.add(aboutItem);

		JMenuItem homeItem = new JMenuItem("Home");
		homeItem.addActionListener(e -> frame.getContentPane());
		popupMenu.add(homeItem);

		JMenuItem floorDetailItem = new JMenuItem("Map Detail");
		floorDetailItem.addActionListener(e -> showFloorDetailWindow());
		popupMenu.add(floorDetailItem);

		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(e -> System.exit(0));
		popupMenu.add(exitItem);

		menuButton.addActionListener(e -> popupMenu.show(menuButton, menuButton.getWidth(), menuButton.getHeight()));
		topPanel.add(menuButton);

		return topPanel;
	}

	/**
	 * Sets up the main GUI components, including panels for input, buttons, and
	 * result display.
	 */
	public void setUpGUI() {
		frame.getContentPane().add(createTopFragment(), BorderLayout.NORTH);

		String[] buildings = { "AA - Alder Amphitheater", "AAB - Academic & Administration Building",
				"CT - Construction Trades", "ECDL - Eccles Early Childhood Lab School",
				"GFSB - Gundersen Facilities Services Building", "GMBB - Gail Miller Business Building",
				"LAC - Lifetime Activities Center", "LIB - Markosian Library", "SI - Science & Industry Building",
				"STC - Student Center", "TAB - Technical Arts Building", "TB - Technology Building" };

		JPanel leftPanel = new JPanel(new GridBagLayout());
		leftPanel.setPreferredSize(new Dimension(400, 800));
		leftPanel.setBorder(new LineBorder(new Color(0, 0, 0)));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		gbc.gridx = 0;

		addLabel(leftPanel, gbc, "Campus Navigation", 0, 24, true);
		addLabel(leftPanel, gbc, "Find the shortest way on campus!", 1, 14, false);

		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.WEST;
		JLabel startTitle = new JLabel("Start Location:");
		startTitle.setFont(new Font("Arial", Font.PLAIN, 14));
		leftPanel.add(startTitle, gbc);

		JComboBox<String> startComboBox = new JComboBox<>(buildings);
		addComponent(leftPanel, gbc, startComboBox, 3);

		gbc.gridy = 4;
		gbc.anchor = GridBagConstraints.WEST;
		JLabel endTitle = new JLabel("End Location:");
		endTitle.setFont(new Font("Arial", Font.PLAIN, 14));
		leftPanel.add(endTitle, gbc);

		JComboBox<String> endComboBox = new JComboBox<>(buildings);
		addComponent(leftPanel, gbc, endComboBox, 5);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
		JButton btnSearch = new JButton("Search");
		JButton btnRetry = new JButton("Retry");
		buttonPanel.add(btnSearch);
		buttonPanel.add(btnRetry);
		addComponent(leftPanel, gbc, buttonPanel, 6);

		addLabel(leftPanel, gbc, "Result", 7, 24, true);
		addLabel(leftPanel, gbc, "We found the shortest way on campus!", 8, 14, false);

		gbc.gridy = 9;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weighty = 1.0;
		JTextArea resultArea = new JTextArea(5, 20);
		resultArea.setEditable(false);
		resultArea.setLineWrap(true);
		resultArea.setWrapStyleWord(true);
		JScrollPane scrollPane = new JScrollPane(resultArea);
		leftPanel.add(scrollPane, gbc);

		JPanel rightPanel = createMapPanel();
		frame.add(leftPanel, BorderLayout.WEST);
		frame.add(rightPanel, BorderLayout.CENTER);

		btnSearch.addActionListener(e -> {
			String start = (String) startComboBox.getSelectedItem();
			String end = (String) endComboBox.getSelectedItem();

			if (start == null || end == null) {
				JOptionPane.showMessageDialog(frame, "Invalid input. Please select different start and end locations.",
						"Input Error", JOptionPane.WARNING_MESSAGE);
				return;
			}

			String result = searchEngine.findShortestPath(start, end);
			resultArea.setText("● Optimal Routes:\n" + result);

			String startCode = start.split(" - ")[0];
			String endCode = end.split(" - ")[0];
			String imagePath = FilePathSearch.getFilePathWay(startCode, endCode);

			((MapPanel) rightPanel).updateBackground(imagePath);

			if (!FilePathSearch.fileExists(imagePath)) {
				JOptionPane.showMessageDialog(frame, "Image not found for the selected path: " + imagePath,
						"Image Error", JOptionPane.ERROR_MESSAGE);
			}
		});

		btnRetry.addActionListener(e -> {
			startComboBox.setSelectedIndex(0);
			endComboBox.setSelectedIndex(0);

			resultArea.setText("");

			((MapPanel) rightPanel).updateBackground("src/campusMap/Resources/campus.png");
		});

		frame.setVisible(true);
	}

	/**
	 * Creates the map panel for displaying navigation maps.
	 * 
	 * @return A JPanel for map rendering.
	 */
	private JPanel createMapPanel() {
		return new MapPanel();
	}

	/**
	 * Displays the "About" dialog with application details.
	 */
	private void showAboutDialog() {
		String aboutMessage = """
				Campus Navigation System
				------------------------
				Authors:
				Tuyet Minh Vu
				Jaylin Mendoza
				Sheila Ortiz

				Version: 1.0
				Year: 2024
				""";
		JOptionPane.showMessageDialog(frame, aboutMessage, "About", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Displays the floor detail window for selecting and viewing floor maps.
	 */
	private void showFloorDetailWindow() {
		JFrame detailFrame = new JFrame("Floor Details");
		detailFrame.setSize(1000, 800);
		detailFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		detailFrame.setLayout(new BorderLayout());

		JPanel selectionPanel = new JPanel(new FlowLayout());
		selectionPanel.setBorder(BorderFactory.createTitledBorder("Select Building and Floor"));

		String[] buildings = { "AA", "AAB", "CT", "ECDL", "GFSB", "GMBB", "LAC", "LIB", "SI", "STC", "TAB", "TB" };
		JComboBox<String> buildingDropdown = new JComboBox<>(buildings);
		selectionPanel.add(new JLabel("Building:"));
		selectionPanel.add(buildingDropdown);

		String[] floors = { "0", "1", "2", "3", "4" };
		JComboBox<String> floorDropdown = new JComboBox<>(floors);
		selectionPanel.add(new JLabel("Floor:"));
		selectionPanel.add(floorDropdown);

		JButton loadButton = new JButton("Load Map");
		selectionPanel.add(loadButton);
		detailFrame.add(selectionPanel, BorderLayout.NORTH);

		JLabel filePathLabel = new JLabel("No file loaded yet", SwingConstants.CENTER);
		filePathLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		detailFrame.add(filePathLabel, BorderLayout.CENTER);

		loadButton.addActionListener(e -> {
			String buildingCode = (String) buildingDropdown.getSelectedItem();
			String floorCode = (String) floorDropdown.getSelectedItem();
			String filePath = FilePathSearch.getFilePathFloor(buildingCode, floorCode);

			File file = new File(filePath);
			if (file.exists()) {
				ImageIcon floorImage = new ImageIcon(filePath);
				filePathLabel.setIcon(floorImage);
				filePathLabel.setText("");
			} else {
				filePathLabel.setText("Invalid floor or building! Please select again.");
				filePathLabel.setIcon(null);
			}
		});

		detailFrame.setVisible(true);
	}

	/**
	 * Adds a label to a panel with specified text and styling.
	 * 
	 * @param panel    The JPanel to add the label to.
	 * @param gbc      GridBagConstraints for positioning the label.
	 * @param text     The label text.
	 * @param y        The grid y position.
	 * @param fontSize The font size for the label.
	 * @param bold     Whether the label text should be bold.
	 */
	private void addLabel(JPanel panel, GridBagConstraints gbc, String text, int y, int fontSize, boolean bold) {
		gbc.gridy = y;
		JLabel label = new JLabel(text, SwingConstants.CENTER);
		label.setFont(new Font("Arial", bold ? Font.BOLD : Font.PLAIN, fontSize));
		panel.add(label, gbc);
	}

	/**
	 * Adds a component to a panel at the specified position.
	 * 
	 * @param panel     The JPanel to add the component to.
	 * @param gbc       GridBagConstraints for positioning the component.
	 * @param component The component to add.
	 * @param y         The grid y position.
	 */
	private void addComponent(JPanel panel, GridBagConstraints gbc, JComponent component, int y) {
		gbc.gridy = y;
		panel.add(component, gbc);
	}

	/**
	 * The main method to launch the application.
	 * 
	 * @param args Command-line arguments.
	 */
	public static void main(String[] args) {
		GUIDemo gui = new GUIDemo(1250, 1150);
		gui.setUpGUI();
	}

	/**
	 * MapPanel is a custom JPanel for rendering campus maps.
	 */
	private static class MapPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		private Image backgroundImage;

		public MapPanel() {
			updateBackground("src/campusMap/Resources/campus.png");
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (backgroundImage != null) {
				g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
			} else {
				g.setColor(Color.GRAY);
				g.fillRect(0, 0, getWidth(), getHeight());
				g.setColor(Color.WHITE);
				g.drawString("Map could not be loaded.", getWidth() / 2 - 50, getHeight() / 2);
			}
		}

		/**
		 * Updates the background image of the map panel.
		 * 
		 * @param filePath The path to the image file.
		 */
		public void updateBackground(String filePath) {
			File imageFile = new File(filePath);
			if (imageFile.exists()) {
				backgroundImage = new ImageIcon(filePath).getImage();
			} else {
				backgroundImage = null;
			}
			repaint();
		}
	}
}
