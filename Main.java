import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Main extends JFrame {
    private JTextField serviceField, departmentField, locationField;
    private JTextArea displayArea;
    private JTextField fleetSizeField, pollutionLevelField;

    private final String URL = "jdbc:mysql://localhost:3306/smartcity";
    private final String USER = "root";
    private final String PASS = "password";

    public Main() {
        setTitle("Smart City Management");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(Color.DARK_GRAY);
        tabbedPane.setForeground(Color.WHITE);

        tabbedPane.addTab("Services", createServicePanel());
        tabbedPane.addTab("Fleet & Pollution", createFleetPanel());

        add(tabbedPane, BorderLayout.CENTER);
        setVisible(true);
    }

    private JPanel createServicePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.GRAY);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2, 5, 5));
        inputPanel.setBackground(Color.GRAY);

        serviceField = new JTextField();
        departmentField = new JTextField();
        locationField = new JTextField();
        JButton addButton = new JButton("Add Service");
        JButton displayButton = new JButton("Show Services");

        inputPanel.add(new JLabel("Service:"));
        inputPanel.add(serviceField);
        inputPanel.add(new JLabel("Department:"));
        inputPanel.add(departmentField);
        inputPanel.add(new JLabel("Location:"));
        inputPanel.add(locationField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.GRAY);
        buttonPanel.add(addButton);
        buttonPanel.add(displayButton);

        displayArea = new JTextArea(10, 30);
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);
        panel.add(scrollPane, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addService());
        displayButton.addActionListener(e -> displayServices());

        return panel;
    }

    private JPanel createFleetPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.LIGHT_GRAY);

        JPanel fleetPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        fleetPanel.setBackground(Color.LIGHT_GRAY);

        fleetSizeField = new JTextField();
        pollutionLevelField = new JTextField();
        JButton saveButton = new JButton("Save Fleet Data");

        fleetPanel.add(new JLabel("Fleet Size:"));
        fleetPanel.add(fleetSizeField);
        fleetPanel.add(new JLabel("Pollution Level:"));
        fleetPanel.add(pollutionLevelField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.LIGHT_GRAY);
        buttonPanel.add(saveButton);

        panel.add(fleetPanel);
        panel.add(buttonPanel);

        saveButton.addActionListener(e -> saveFleetData());

        return panel;
    }

    private Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database connection failed!");
            return null;
        }
    }

    private void addService() {
        Connection conn = getConnection();
        if (conn != null) {
            try {
                PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO services (service_name, department, location) VALUES (?, ?, ?)");
                stmt.setString(1, serviceField.getText());
                stmt.setString(2, departmentField.getText());
                stmt.setString(3, locationField.getText());
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Service added!");
                stmt.close();
                conn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error adding service.");
            }
        }
    }

    private void displayServices() {
        Connection conn = getConnection();
        if (conn != null) {
            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM services");
                displayArea.setText("");
                while (rs.next()) {
                    displayArea.append(rs.getString("service_name") + " | " +
                            rs.getString("department") + " | " +
                            rs.getString("location") + "\n");
                }
                rs.close();
                stmt.close();
                conn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error fetching services.");
            }
        }
    }

    private void saveFleetData() {
        JOptionPane.showMessageDialog(this, "Fleet size and pollution data saved!");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main());
    }
}
