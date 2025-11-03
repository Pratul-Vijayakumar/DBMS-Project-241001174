package railwayTicketing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginSignupFrame extends JFrame {
    
    private JTextField loginUsername, signupUsername, signupFullName, signupContact;
    private JPasswordField loginPassword, signupPassword;
    private JButton btnLogin, btnSignup;
    private DatabaseOperations dbOps;

    // Custom gradient panel for background
    private class GradientPanel extends JPanel {
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
    }

    public LoginSignupFrame() {
        setTitle("Railway Ticketing System - Login / Signup");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 400);
        setLocationRelativeTo(null); // Center on screen
        setResizable(false);

        dbOps = new DatabaseOperations();

        GradientPanel panel = new GradientPanel();
        panel.setLayout(null);
        add(panel);

        // --- Login Section ---

        JLabel lblLoginTitle = new JLabel("Login");
        lblLoginTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblLoginTitle.setForeground(Color.WHITE);
        lblLoginTitle.setBounds(120, 20, 100, 30);
        panel.add(lblLoginTitle);

        JLabel lblLoginUsername = new JLabel("Username:");
        lblLoginUsername.setForeground(Color.WHITE);
        lblLoginUsername.setBounds(50, 70, 80, 25);
        panel.add(lblLoginUsername);

        loginUsername = new JTextField();
        loginUsername.setBounds(140, 70, 150, 25);
        panel.add(loginUsername);

        JLabel lblLoginPassword = new JLabel("Password:");
        lblLoginPassword.setForeground(Color.WHITE);
        lblLoginPassword.setBounds(50, 110, 80, 25);
        panel.add(lblLoginPassword);

        loginPassword = new JPasswordField();
        loginPassword.setBounds(140, 110, 150, 25);
        panel.add(loginPassword);

        btnLogin = new JButton("Login");
        btnLogin.setBounds(140, 150, 150, 30);
        btnLogin.setBackground(Color.decode("#FF6F59"));
        btnLogin.setForeground(Color.WHITE);
        panel.add(btnLogin);

        JLabel lblLoginMessage = new JLabel("");
        lblLoginMessage.setForeground(Color.YELLOW);
        lblLoginMessage.setBounds(50, 190, 250, 25);
        panel.add(lblLoginMessage);

        // --- Signup Section ---

        JLabel lblSignupTitle = new JLabel("Signup");
        lblSignupTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblSignupTitle.setForeground(Color.WHITE);
        lblSignupTitle.setBounds(450, 20, 100, 30);
        panel.add(lblSignupTitle);

        JLabel lblSignupUsername = new JLabel("Username:");
        lblSignupUsername.setForeground(Color.WHITE);
        lblSignupUsername.setBounds(370, 70, 80, 25);
        panel.add(lblSignupUsername);

        signupUsername = new JTextField();
        signupUsername.setBounds(460, 70, 180, 25);
        panel.add(signupUsername);

        JLabel lblSignupPassword = new JLabel("Password:");
        lblSignupPassword.setForeground(Color.WHITE);
        lblSignupPassword.setBounds(370, 110, 80, 25);
        panel.add(lblSignupPassword);

        signupPassword = new JPasswordField();
        signupPassword.setBounds(460, 110, 180, 25);
        panel.add(signupPassword);

        JLabel lblSignupFullName = new JLabel("Full Name:");
        lblSignupFullName.setForeground(Color.WHITE);
        lblSignupFullName.setBounds(370, 150, 80, 25);
        panel.add(lblSignupFullName);

        signupFullName = new JTextField();
        signupFullName.setBounds(460, 150, 180, 25);
        panel.add(signupFullName);

        JLabel lblSignupContact = new JLabel("Contact No:");
        lblSignupContact.setForeground(Color.WHITE);
        lblSignupContact.setBounds(370, 190, 80, 25);
        panel.add(lblSignupContact);

        signupContact = new JTextField();
        signupContact.setBounds(460, 190, 180, 25);
        panel.add(signupContact);

        btnSignup = new JButton("Signup");
        btnSignup.setBounds(460, 230, 180, 30);
        btnSignup.setBackground(Color.decode("#FF6F59"));
        btnSignup.setForeground(Color.WHITE);
        panel.add(btnSignup);

        JLabel lblSignupMessage = new JLabel("");
        lblSignupMessage.setForeground(Color.YELLOW);
        lblSignupMessage.setBounds(370, 270, 280, 25);
        panel.add(lblSignupMessage);

        // --- Button Actions ---

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = loginUsername.getText().trim();
                String password = new String(loginPassword.getPassword()).trim();

                if (username.isEmpty() || password.isEmpty()) {
                    lblLoginMessage.setText("Please enter username and password.");
                    return;
                }

                User user = dbOps.loginUser(username, password);
                if (user != null) {
                    lblLoginMessage.setForeground(Color.GREEN);
                    lblLoginMessage.setText("Login successful! Redirecting...");
                    // Open Main Menu after short delay
                    SwingUtilities.invokeLater(() -> {
                        dispose();
                        new MainMenuFrame(user).setVisible(true);
                    });
                } else {
                    lblLoginMessage.setForeground(Color.YELLOW);
                    lblLoginMessage.setText("Invalid username or password.");
                }
            }
        });

        btnSignup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = signupUsername.getText().trim();
                String password = new String(signupPassword.getPassword()).trim();
                String fullName = signupFullName.getText().trim();
                String contactNo = signupContact.getText().trim();

                if (username.isEmpty() || password.isEmpty() || fullName.isEmpty() || contactNo.isEmpty()) {
                    lblSignupMessage.setText("All fields are required.");
                    return;
                }

                if (dbOps.usernameExists(username)) {
                    lblSignupMessage.setText("Username already exists!");
                    return;
                }

                boolean registered = dbOps.registerUser(username, password, fullName, contactNo);
                if (registered) {
                    lblSignupMessage.setForeground(Color.GREEN);
                    lblSignupMessage.setText("Signup successful! You can login now.");
                } else {
                    lblSignupMessage.setForeground(Color.YELLOW);
                    lblSignupMessage.setText("Error during signup. Try again.");
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginSignupFrame().setVisible(true);
        });
    }
}
