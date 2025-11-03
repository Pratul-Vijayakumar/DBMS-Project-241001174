package railwayTicketing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuFrame extends JFrame {

    private User loggedInUser;

    public MainMenuFrame(User user) {
        this.loggedInUser = user;

        setTitle("Railway Ticketing System - Main Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setResizable(false);

        // Background gradient panel
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
        lblWelcome.setForeground(Color.WHITE);
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 20));
        lblWelcome.setBounds(120, 20, 300, 30);
        panel.add(lblWelcome);

        // Buttons
        JButton btnSearchTrains = new JButton("Search Trains");
        btnSearchTrains.setBounds(150, 80, 200, 40);
        btnSearchTrains.setBackground(Color.decode("#FF6F59"));
        btnSearchTrains.setForeground(Color.WHITE);
        panel.add(btnSearchTrains);

        JButton btnBookTicket = new JButton("Book Train Ticket");
        btnBookTicket.setBounds(150, 130, 200, 40);
        btnBookTicket.setBackground(Color.decode("#FF6F59"));
        btnBookTicket.setForeground(Color.WHITE);
        panel.add(btnBookTicket);

        JButton btnViewBookings = new JButton("View Bookings");
        btnViewBookings.setBounds(150, 180, 200, 40);
        btnViewBookings.setBackground(Color.decode("#FF6F59"));
        btnViewBookings.setForeground(Color.WHITE);
        panel.add(btnViewBookings);

        JButton btnCancelBookings = new JButton("Cancel Bookings");
        btnCancelBookings.setBounds(150, 230, 200, 40);
        btnCancelBookings.setBackground(Color.decode("#FF6F59"));
        btnCancelBookings.setForeground(Color.WHITE);
        panel.add(btnCancelBookings);

        JButton btnLogout = new JButton("Logout");
        btnLogout.setBounds(190, 290, 120, 35);
        btnLogout.setBackground(Color.GRAY);
        btnLogout.setForeground(Color.WHITE);
        panel.add(btnLogout);

        // Button actions
        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LoginSignupFrame().setVisible(true);
            }
        });

        btnSearchTrains.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new SearchTrainsFrame(loggedInUser).setVisible(true);
            }
        });

        btnBookTicket.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new BookTrainFrame(loggedInUser).setVisible(true);
            }
        });

        btnViewBookings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new ViewBookingsFrame(loggedInUser).setVisible(true);
            }
        });

        btnCancelBookings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new CancelBookingFrame(loggedInUser).setVisible(true);
            }
        });
    }
}
