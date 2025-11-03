package railwayTicketing;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Set;

public class SearchTrainsFrame extends JFrame {
    
    private User loggedInUser;
    private DatabaseOperations dbOps;
    
    private JComboBox<String> comboSource, comboDestination;
    private JTable trainsTable;
    private DefaultTableModel tableModel;
    private JTextArea trainDetailsArea;
    
    public SearchTrainsFrame(User user) {
        this.loggedInUser = user;
        this.dbOps = new DatabaseOperations();
        
        setTitle("Search Trains");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = Color.decode("#137DC5");
                Color color2 = Color.decode("#00CFC8");
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setLayout(null);
        add(panel);
        
        JLabel lblWelcome = new JLabel("Welcome, " + loggedInUser.getFullName());
        lblWelcome.setBounds(20, 10, 300, 30);
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 20));
        lblWelcome.setForeground(Color.WHITE);
        panel.add(lblWelcome);
        
        JLabel lblSource = new JLabel("Source:");
        lblSource.setBounds(30, 60, 80, 25);
        lblSource.setForeground(Color.WHITE);
        panel.add(lblSource);
        
        comboSource = new JComboBox<>();
        comboSource.setBounds(100, 60, 150, 25);
        panel.add(comboSource);
        
        JLabel lblDestination = new JLabel("Destination:");
        lblDestination.setBounds(300, 60, 90, 25);
        lblDestination.setForeground(Color.WHITE);
        panel.add(lblDestination);
        
        comboDestination = new JComboBox<>();
        comboDestination.setBounds(390, 60, 150, 25);
        panel.add(comboDestination);
        
        JButton btnSearch = new JButton("Search");
        btnSearch.setBounds(570, 60, 100, 30);
        btnSearch.setBackground(Color.decode("#FF6F59"));
        btnSearch.setForeground(Color.WHITE);
        panel.add(btnSearch);
        
        // Table for train list
        String[] columns = {"Train ID", "Train Name", "Source", "Destination"};
        tableModel = new DefaultTableModel(columns, 0);
        trainsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(trainsTable);
        scrollPane.setBounds(30, 100, 640, 150);
        panel.add(scrollPane);
        
        // TextArea to show train details
        trainDetailsArea = new JTextArea();
        trainDetailsArea.setEditable(false);
        trainDetailsArea.setBounds(30, 260, 640, 150);
        trainDetailsArea.setBackground(Color.WHITE);
        trainDetailsArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        panel.add(trainDetailsArea);
        
        // Back button
        JButton btnBack = new JButton("Back");
        btnBack.setBounds(30, 420, 100, 30);
        btnBack.setBackground(Color.GRAY);
        btnBack.setForeground(Color.WHITE);
        panel.add(btnBack);
        
        // Logout Button
        JButton btnLogout = new JButton("Logout");
        btnLogout.setBounds(570, 420, 100, 30);
        btnLogout.setBackground(Color.GRAY);
        btnLogout.setForeground(Color.WHITE);
        panel.add(btnLogout);
        
        // Populate combo boxes with data
        populateSourceDestination();
        
        // Button listeners
        btnSearch.addActionListener(e -> searchTrains());
        
        trainsTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = trainsTable.getSelectedRow();
                if (row >= 0) {
                    int trainId = (int) trainsTable.getValueAt(row, 0);
                    showTrainDetails(trainId);
                }
            }
        });
        
        btnBack.addActionListener(e -> {
            dispose();
            new MainMenuFrame(loggedInUser).setVisible(true);
        });
        
        btnLogout.addActionListener(e -> {
            dispose();
            new LoginSignupFrame().setVisible(true);
        });
    }
    
    private void populateSourceDestination() {
        comboSource.removeAllItems();
        comboDestination.removeAllItems();
        
        Set<String> sources = dbOps.getAllSources();
        sources.forEach(comboSource::addItem);
        
        Set<String> destinations = dbOps.getAllDestinations();
        destinations.forEach(comboDestination::addItem);
    }
    
    private void searchTrains() {
        String source = (String) comboSource.getSelectedItem();
        String destination = (String) comboDestination.getSelectedItem();
        
        if (source == null || destination == null) {
            JOptionPane.showMessageDialog(this, "Please select both source and destination.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        List<Train> trains = dbOps.searchTrains(source, destination);
        tableModel.setRowCount(0);
        for (Train train : trains) {
            Object[] row = {train.getTrainId(), train.getTrainName(), train.getSource(), train.getDestination()};
            tableModel.addRow(row);
        }
        trainDetailsArea.setText("");
        if (trains.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No trains found for selected route.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void showTrainDetails(int trainId) {
        Train train = dbOps.getTrainById(trainId);
        if (train != null) {
            String details = String.format(
                "Train ID: %d\nTrain Name: %s\nSource: %s\nDestination: %s\nDeparture Time: %s\nArrival Time: %s\nFare: %.2f",
                train.getTrainId(), train.getTrainName(), train.getSource(), train.getDestination(),
                train.getDepartureTime().toString(), train.getArrivalTime().toString(), train.getFare().doubleValue());
            trainDetailsArea.setText(details);
        } else {
            trainDetailsArea.setText("Details not found.");
        }
    }
}
