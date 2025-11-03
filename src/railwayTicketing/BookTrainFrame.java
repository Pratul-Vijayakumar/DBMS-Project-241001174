package railwayTicketing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class BookTrainFrame extends JFrame {

    private final User loggedInUser;
    private final DatabaseOperations dbOps;

    // Passenger fields
    private JTextField txtName, txtAge, txtContact;
    private JComboBox<String> comboGender;

    // Train selection
    private JComboBox<String> comboTrain; // shows "train_id - train_name"
    private JTextArea trainInfoArea;

    // Seat type
    private JComboBox<String> comboSeat;
    private JSpinner dateSpinner;

    public BookTrainFrame(User user) {
        this.loggedInUser = user;
        this.dbOps = new DatabaseOperations();

        setTitle("Book Train Ticket");
        setSize(750, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color c1 = Color.decode("#137DC5");
                Color c2 = Color.decode("#00CFC8");
                g2d.setPaint(new GradientPaint(0, 0, c1, 0, getHeight(), c2));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setLayout(null);
        add(panel);

        JLabel lblTitle = new JLabel("Book Train Ticket");
        lblTitle.setBounds(260, 10, 300, 30);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        panel.add(lblTitle);

        // Passenger box
        JPanel boxPassenger = makeCardPanel();
        boxPassenger.setBounds(25, 60, 350, 210);
        panel.add(boxPassenger);

        JLabel l1 = label("Passenger Name:");
        l1.setBounds(15, 20, 130, 25);
        boxPassenger.add(l1);

        txtName = new JTextField();
        txtName.setBounds(155, 20, 170, 25);
        boxPassenger.add(txtName);

        JLabel l2 = label("Age:");
        l2.setBounds(15, 60, 130, 25);
        boxPassenger.add(l2);

        txtAge = new JTextField();
        txtAge.setBounds(155, 60, 60, 25);
        boxPassenger.add(txtAge);

        JLabel l3 = label("Gender:");
        l3.setBounds(15, 100, 130, 25);
        boxPassenger.add(l3);

        comboGender = new JComboBox<>(new String[]{"Male","Female","Other"});
        comboGender.setBounds(155, 100, 120, 25);
        boxPassenger.add(comboGender);

        JLabel l4 = label("Contact No:");
        l4.setBounds(15, 140, 130, 25);
        boxPassenger.add(l4);

        txtContact = new JTextField();
        txtContact.setBounds(155, 140, 170, 25);
        boxPassenger.add(txtContact);

        // Train select box
        JPanel boxTrain = makeCardPanel();
        boxTrain.setBounds(385, 60, 340, 210);
        panel.add(boxTrain);

        JLabel lt1 = label("Select Train:");
        lt1.setBounds(15, 20, 120, 25);
        boxTrain.add(lt1);

        comboTrain = new JComboBox<>();
        comboTrain.setBounds(130, 20, 190, 25);
        boxTrain.add(comboTrain);

        JButton btnView = new JButton("Show Details");
        btnView.setBounds(130, 55, 190, 28);
        btnView.setBackground(Color.decode("#FF6F59"));
        btnView.setForeground(Color.WHITE);
        boxTrain.add(btnView);

        trainInfoArea = new JTextArea();
        trainInfoArea.setEditable(false);
        trainInfoArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane sp = new JScrollPane(trainInfoArea);
        sp.setBounds(15, 90, 305, 100);
        boxTrain.add(sp);

        // Journey and seat box
        JPanel boxSeat = makeCardPanel();
        boxSeat.setBounds(25, 280, 700, 110);
        panel.add(boxSeat);

        JLabel d1 = label("Date of Journey:");
        d1.setBounds(15, 20, 130, 25);
        boxSeat.add(d1);

        dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(editor);
        dateSpinner.setBounds(150, 20, 140, 25);
        boxSeat.add(dateSpinner);

        JLabel s1 = label("Seat Type:");
        s1.setBounds(330, 20, 100, 25);
        boxSeat.add(s1);

        comboSeat = new JComboBox<>(new String[]{
                "1st AC","2nd AC","3rd AC","3rd Economy AC","Chaircar","Sleeper"
        });
        comboSeat.setBounds(430, 20, 240, 25);
        boxSeat.add(comboSeat);

        // Buttons row
        JButton btnBook = new JButton("Book Ticket");
        btnBook.setBounds(240, 410, 200, 40);
        btnBook.setBackground(Color.decode("#FF6F59"));
        btnBook.setForeground(Color.WHITE);
        panel.add(btnBook);

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(25, 470, 110, 32);
        btnBack.setBackground(Color.GRAY);
        btnBack.setForeground(Color.WHITE);
        panel.add(btnBack);

        JButton btnLogout = new JButton("Logout");
        btnLogout.setBounds(615, 470, 110, 32);
        btnLogout.setBackground(Color.GRAY);
        btnLogout.setForeground(Color.WHITE);
        panel.add(btnLogout);

        // Load trains in dropdown
        populateTrainDropdown();

        // Actions
        btnView.addActionListener(e -> showSelectedTrainDetails());
        comboTrain.addActionListener(e -> { /* optional: auto-update */ });

        btnBook.addActionListener(e -> performBooking());

        btnBack.addActionListener(e -> {
            dispose();
            new MainMenuFrame(loggedInUser).setVisible(true);
        });

        btnLogout.addActionListener(e -> {
            dispose();
            new LoginSignupFrame().setVisible(true);
        });
    }

    private JPanel makeCardPanel() {
        JPanel p = new JPanel(null);
        p.setBackground(new Color(255, 255, 255, 210));
        p.setBorder(BorderFactory.createLineBorder(new Color(255,255,255,140), 1, true));
        return p;
    }

    private JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(new Color(40,40,40));
        return l;
    }

    private void populateTrainDropdown() {
        comboTrain.removeAllItems();
        List<Train> trains = dbOps.getAllTrains();
        for (Train t : trains) {
            comboTrain.addItem(t.getTrainId() + " - " + t.getTrainName());
        }
    }

    private Integer getSelectedTrainId() {
        Object sel = comboTrain.getSelectedItem();
        if (sel == null) return null;
        String s = sel.toString();
        int dash = s.indexOf(" - ");
        if (dash <= 0) return null;
        try {
            return Integer.parseInt(s.substring(0, dash).trim());
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private void showSelectedTrainDetails() {
        Integer id = getSelectedTrainId();
        if (id == null) {
            JOptionPane.showMessageDialog(this, "Please select a train.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        Train t = dbOps.getTrainById(id);
        if (t == null) {
            trainInfoArea.setText("Train not found.");
            return;
        }
        String details = """
                Train ID   : %d
                Name       : %s
                Route      : %s -> %s
                Departure  : %s
                Arrival    : %s
                Fare       : %.2f
                """.formatted(
                t.getTrainId(), t.getTrainName(), t.getSource(), t.getDestination(),
                t.getDepartureTime().toString(), t.getArrivalTime().toString(), t.getFare().doubleValue()
        );
        trainInfoArea.setText(details);
    }

    private void performBooking() {
        // Validate passenger fields
        String name = txtName.getText().trim();
        String ageStr = txtAge.getText().trim();
        String gender = (String) comboGender.getSelectedItem();
        String contact = txtContact.getText().trim();
        Integer trainId = getSelectedTrainId();
        String seat = (String) comboSeat.getSelectedItem();

        if (name.isEmpty() || ageStr.isEmpty() || contact.isEmpty() || trainId == null) {
            JOptionPane.showMessageDialog(this, "Please fill all fields and select a train.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int age;
        try {
            age = Integer.parseInt(ageStr);
            if (age <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid age.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Date
        java.util.Date utilDate = (java.util.Date) dateSpinner.getValue();
        LocalDate ld = new java.sql.Date(utilDate.getTime()).toLocalDate();
        Date doj = Date.valueOf(ld);

        // Book via DB
        int ticketId = dbOps.bookTicket(
                loggedInUser.getUserId(),
                trainId,
                doj,
                seat,
                name,
                age,
                gender,
                contact
        );

        if (ticketId > 0) {
            JOptionPane.showMessageDialog(this, "Ticket booked successfully! Ticket ID: " + ticketId,
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            showSelectedTrainDetails(); // keep details visible
        } else {
            JOptionPane.showMessageDialog(this, "Booking failed. Please try again.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
