package railwayTicketing;

import javax.swing.*;
import java.awt.*;

public class CancelBookingFrame extends JFrame {

    private final User loggedInUser;
    private final DatabaseOperations dbOps;
    private JTextField txtTicketId;

    public CancelBookingFrame(User user) {
        this.loggedInUser = user;
        this.dbOps = new DatabaseOperations();

        setTitle("Cancel Booking");
        setSize(520, 280);
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

        JLabel lbl = new JLabel("Cancel Booking");
        lbl.setBounds(180, 10, 200, 30);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Arial", Font.BOLD, 22));
        panel.add(lbl);

        JLabel lblId = new JLabel("Enter Ticket ID:");
        lblId.setBounds(60, 80, 120, 25);
        lblId.setForeground(Color.WHITE);
        panel.add(lblId);

        txtTicketId = new JTextField();
        txtTicketId.setBounds(190, 80, 120, 25);
        panel.add(txtTicketId);

        JButton btnCancel = new JButton("Confirm Cancellation");
        btnCancel.setBounds(150, 120, 220, 35);
        btnCancel.setBackground(Color.decode("#FF6F59"));
        btnCancel.setForeground(Color.WHITE);
        panel.add(btnCancel);

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(60, 190, 110, 30);
        btnBack.setBackground(Color.GRAY);
        btnBack.setForeground(Color.WHITE);
        panel.add(btnBack);

        JButton btnLogout = new JButton("Logout");
        btnLogout.setBounds(350, 190, 110, 30);
        btnLogout.setBackground(Color.GRAY);
        btnLogout.setForeground(Color.WHITE);
        panel.add(btnLogout);

        btnCancel.addActionListener(e -> doCancel());

        btnBack.addActionListener(e -> {
            dispose();
            new MainMenuFrame(loggedInUser).setVisible(true);
        });

        btnLogout.addActionListener(e -> {
            dispose();
            new LoginSignupFrame().setVisible(true);
        });
    }

    private void doCancel() {
        String idStr = txtTicketId.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Ticket ID.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ticket ID must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Optional: verify ticket belongs to logged-in user
        Ticket t = dbOps.getTicketById(id);
        if (t == null) {
            JOptionPane.showMessageDialog(this, "Ticket not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (t.getPassengerId() != loggedInUser.getUserId()) {
            JOptionPane.showMessageDialog(this, "You can only cancel your own tickets.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to cancel Ticket ID: " + id + " ?",
                "Confirm", JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            boolean ok = dbOps.cancelTicket(id);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Ticket " + id + " has been successfully cancelled!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                txtTicketId.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Cancellation failed. Try again.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
