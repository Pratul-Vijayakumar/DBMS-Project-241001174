package railwayTicketing;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ViewBookingsFrame extends JFrame {

    private final User loggedInUser;
    private final DatabaseOperations dbOps;
    private JTable table;
    private DefaultTableModel model;

    public ViewBookingsFrame(User user) {
        this.loggedInUser = user;
        this.dbOps = new DatabaseOperations();

        setTitle("View My Bookings");
        setSize(800, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setPaint(new GradientPaint(0, 0, Color.decode("#137DC5"), 0, getHeight(), Color.decode("#00CFC8")));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setLayout(null);
        add(panel);

        JLabel lbl = new JLabel("My Bookings");
        lbl.setBounds(320, 10, 200, 30);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Arial", Font.BOLD, 22));
        panel.add(lbl);

        String[] cols = {"Ticket ID","Passenger ID","Train ID","Date of Journey","Status","Seat","Name","Age","Gender","Contact"};
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(20, 60, 760, 360);
        panel.add(sp);

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(20, 440, 110, 32);
        btnBack.setBackground(Color.GRAY);
        btnBack.setForeground(Color.WHITE);
        panel.add(btnBack);

        JButton btnLogout = new JButton("Logout");
        btnLogout.setBounds(670, 440, 110, 32);
        btnLogout.setBackground(Color.GRAY);
        btnLogout.setForeground(Color.WHITE);
        panel.add(btnLogout);

        loadBookings();

        btnBack.addActionListener(e -> {
            dispose();
            new MainMenuFrame(loggedInUser).setVisible(true);
        });

        btnLogout.addActionListener(e -> {
            dispose();
            new LoginSignupFrame().setVisible(true);
        });
    }

    private void loadBookings() {
        model.setRowCount(0);
        List<Ticket> list = dbOps.getTicketsByUserId(loggedInUser.getUserId());
        for (Ticket t : list) {
            model.addRow(new Object[]{
                    t.getTicketId(), t.getPassengerId(), t.getTrainId(),
                    t.getDateOfJourney(), t.getBookingStatus(), t.getSeat(),
                    t.getPassengerName(), t.getAge(), t.getGender(), t.getContactNo()
            });
        }
        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No bookings found.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
