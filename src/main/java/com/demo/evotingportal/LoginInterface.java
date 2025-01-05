package com.demo.evotingportal;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.sql.*;
import java.awt.event.*;


public class LoginInterface {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/evoting_portal";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Ashmit_06!";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("E-Voting Portal");
            frame.setSize(400, 200);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new GridBagLayout());
            frame.setLocationRelativeTo(null); // Center the window

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);

            JLabel label = new JLabel("Login as:");
            label.setForeground(Color.BLACK);
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 3;
            gbc.anchor = GridBagConstraints.CENTER;
            frame.add(label, gbc);

            JButton userButton = new JButton("User");
            userButton.setBackground(new Color(100, 149, 237));
            userButton.setForeground(Color.BLACK);
            gbc.gridy = 1;
            gbc.gridwidth = 1;
            gbc.anchor = GridBagConstraints.CENTER;
            frame.add(userButton, gbc);

            JButton ecButton = new JButton("EC (Super Admin)");
            ecButton.setBackground(new Color(255, 165, 0));
            ecButton.setForeground(Color.BLACK);
            gbc.gridx = 2;
            frame.add(ecButton, gbc);

            JButton adminButton = new JButton("Admin");
            adminButton.setBackground(new Color(34, 139, 34));
            adminButton.setForeground(Color.BLACK);
            gbc.gridx = 1;
            frame.add(adminButton, gbc);

            frame.setVisible(true);

            ecButton.addActionListener(e -> {
                frame.dispose();
                showEcLoginPage();
            });

            userButton.addActionListener(e -> {
                frame.dispose();
                showUserLoginPage();
            });

            adminButton.addActionListener(e -> {
                frame.dispose();
                showAdminLoginPage();
            });
        });
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    private static void showEcLoginPage() {
        JFrame ecLoginFrame = new JFrame("EC (Super Admin) Login");
        ecLoginFrame.setSize(500, 500);
        ecLoginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ecLoginFrame.setLayout(new GridBagLayout());
        ecLoginFrame.setLocationRelativeTo(null);
        ecLoginFrame.getContentPane().setBackground(new Color(211, 211, 211)
); // Choose your color


        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel userLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        ecLoginFrame.add(userLabel, gbc);

        JTextField usernameField = new JTextField(20);
        gbc.gridx = 1;
        ecLoginFrame.add(usernameField, gbc);

        JLabel passLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        ecLoginFrame.add(passLabel, gbc);

        JPasswordField passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        ecLoginFrame.add(passwordField, gbc);

        JLabel captchaLabel = new JLabel("CAPTCHA:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        ecLoginFrame.add(captchaLabel, gbc);

        String[] captchaText = new String[1];
        captchaText[0] = generateCaptchaText();
        JLabel captchaImage = new JLabel(new ImageIcon(generateCaptchaImage(captchaText[0])));
        JButton refreshCaptchaButton = new JButton("🔄");
        refreshCaptchaButton.setPreferredSize(new Dimension(50, 30));
        refreshCaptchaButton.setBackground(new Color(255, 165, 0));
        refreshCaptchaButton.setForeground(Color.BLACK);

        JPanel captchaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        captchaPanel.setBackground(new Color(255, 248, 220));
        captchaPanel.add(captchaImage);
        captchaPanel.add(refreshCaptchaButton);

        gbc.gridx = 1;
        gbc.gridy = 2;
        ecLoginFrame.add(captchaPanel, gbc);

        JTextField captchaField = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 3;
        ecLoginFrame.add(captchaField, gbc);

        JButton backButton = new JButton("Back");
        backButton.setBackground(new Color(255, 69, 0));
        backButton.setForeground(Color.BLACK);
        gbc.gridx = 1;
        gbc.gridy = 4;
        ecLoginFrame.add(backButton, gbc);

        backButton.addActionListener(e -> {
            ecLoginFrame.dispose();
            main(null);
        });



        JButton loginButton = new JButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 4;
        ecLoginFrame.add(loginButton, gbc);

        loginButton.addActionListener(e -> {
            String inputCaptcha = captchaField.getText();
            if (!inputCaptcha.equals(captchaText[0])) {
                JOptionPane.showMessageDialog(ecLoginFrame, "Invalid CAPTCHA input! Please try again.");
                return;
            }

            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (authenticateEc(username, password)) {
                JOptionPane.showMessageDialog(ecLoginFrame, "EC Login Successful!");
                ECAdminPage.main(new String[]{});
                ecLoginFrame.dispose();
            } else {
                JOptionPane.showMessageDialog(ecLoginFrame, "EC Login Failed! Check your username or password.");
            }
        });

        refreshCaptchaButton.addActionListener(e -> {
            captchaText[0] = generateCaptchaText();
            captchaImage.setIcon(new ImageIcon(generateCaptchaImage(captchaText[0])));
        });

        ecLoginFrame.setVisible(true);
    }

    private static boolean authenticateEc(String username, String password) {
        try (Connection conn = getConnection()) {
            String query = "SELECT * FROM ec_admin WHERE username = ? AND password = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                stmt.setString(2, password);
                try (ResultSet rs = stmt.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void showUserLoginPage() {
        JFrame loginFrame = new JFrame("User Login");
        loginFrame.setSize(600, 600);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLayout(new GridBagLayout());
        loginFrame.setLocationRelativeTo(null);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        loginFrame.getContentPane().setBackground(new Color(211, 211, 211));

        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 0;
        loginFrame.add(userLabel, gbc);

        JTextField usernameField = new JTextField(20);
        gbc.gridx = 1;
        loginFrame.add(usernameField, gbc);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 1;
        loginFrame.add(passLabel, gbc);

        JPasswordField passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        loginFrame.add(passwordField, gbc);

        JLabel captchaLabel = new JLabel("CAPTCHA:");
        captchaLabel.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 2;
        loginFrame.add(captchaLabel, gbc);


        String[] captchaText = new String[1];
        captchaText[0] = generateCaptchaText();
        JLabel captchaImage = new JLabel(new ImageIcon(generateCaptchaImage(captchaText[0])));
        JButton refreshCaptchaButton = new JButton("🔄");
        refreshCaptchaButton.setPreferredSize(new Dimension(50, 30));
        refreshCaptchaButton.setBackground(new Color(255, 165, 0));
        refreshCaptchaButton.setForeground(Color.BLACK);

        JPanel captchaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        captchaPanel.setBackground(new Color(255, 248, 220));
        captchaPanel.add(captchaImage);
        captchaPanel.add(refreshCaptchaButton);

        gbc.gridx = 1;
        gbc.gridy = 2;
        loginFrame.add(captchaPanel, gbc);

        JTextField captchaField = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 3;
        loginFrame.add(captchaField, gbc);

        JLabel secQuestionLabel1 = new JLabel("What is your birth city?");
        secQuestionLabel1.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 4;
        loginFrame.add(secQuestionLabel1, gbc);

        JTextField answerField1 = new JTextField(20);
        gbc.gridx = 1;
        loginFrame.add(answerField1, gbc);

        JLabel secQuestionLabel2 = new JLabel("What is your favorite color?");
        secQuestionLabel2.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 5;
        loginFrame.add(secQuestionLabel2, gbc);

        JTextField answerField2 = new JTextField(20);
        gbc.gridx = 1;
        loginFrame.add(answerField2, gbc);

        JButton submitButton = new JButton("Submit");
        submitButton.setBackground(new Color(100, 149, 237));
        submitButton.setForeground(Color.BLACK);
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.CENTER;
        loginFrame.add(submitButton, gbc);

        JButton backButton = new JButton("Back");
        backButton.setBackground(new Color(255, 69, 0));
        backButton.setForeground(Color.BLACK);
        gbc.gridx = 0;
        loginFrame.add(backButton, gbc);

        backButton.addActionListener(e -> {
            loginFrame.dispose();
            main(null);
        });

 submitButton.addActionListener(e -> {
    String inputCaptcha = captchaField.getText();
    if (!inputCaptcha.equals(captchaText[0])) {
        JOptionPane.showMessageDialog(loginFrame, "Invalid CAPTCHA input! Please enter the above details.");
        return;
    }

    String username = usernameField.getText();
    String password = new String(passwordField.getPassword());
    String answer1 = answerField1.getText();
    String answer2 = answerField2.getText();

    if (!isElectionActive()) {
        JOptionPane.showMessageDialog(loginFrame, "Elections are currently on hold. Please try again later.");
        return;
    }

    if (hasVoted(username)) {
        JOptionPane.showMessageDialog(loginFrame, "You have already cast your vote. You cannot log in again.", "Login Error", JOptionPane.WARNING_MESSAGE);
        return;
    }

    if (authenticateUser(username, password, answer1, answer2)) {
        JOptionPane.showMessageDialog(loginFrame, "Login Successful!");

        loginFrame.dispose();
        VotingPortal.launchVotingPortal(username);
    } else {
        JOptionPane.showMessageDialog(loginFrame, "Login Failed! Check your username, password, or security answers.");
    }
});


        refreshCaptchaButton.addActionListener(e -> {
            captchaText[0] = generateCaptchaText();
            captchaImage.setIcon(new ImageIcon(generateCaptchaImage(captchaText[0])));
        });

        loginFrame.setVisible(true);
    }

    private static boolean hasVoted(String username) {
    String url = "jdbc:mysql://localhost:3306/evoting_portal";
    String dbUsername = "root";
    String dbPassword = "Ashmit_06!";
    String query = "SELECT COUNT(*) AS count FROM votes WHERE voter_username = ?";

    try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword);
         PreparedStatement stmt = conn.prepareStatement(query)) {

        stmt.setString(1, username);

        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                int count = rs.getInt("count");
                return count > 0;
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

    private static boolean isElectionActive() {
    try (Connection conn = getConnection()) {
        String query = "SELECT is_active FROM election_status WHERE id = 1";

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getBoolean("is_active");
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

    private static boolean authenticateUser(String username, String password, String secAnswer1, String secAnswer2) {
        try (Connection conn = getConnection()) {
            String query = "SELECT * FROM voter WHERE username = ? AND password = ? AND LOWER(sec_answer1) = LOWER(?) AND LOWER(sec_answer2) = LOWER(?)";

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                stmt.setString(2, password);
                stmt.setString(3, secAnswer1);
                stmt.setString(4, secAnswer2);

                try (ResultSet rs = stmt.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void showAdminLoginPage() {
        JFrame loginFrame = new JFrame("Admin Login");
        loginFrame.setSize(500, 500);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLayout(new GridBagLayout());
        loginFrame.setLocationRelativeTo(null);


        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        loginFrame.getContentPane().setBackground(new Color(211, 211, 211));

        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 0;
        loginFrame.add(userLabel, gbc);

        JTextField usernameField = new JTextField(20);
        gbc.gridx = 1;
        loginFrame.add(usernameField, gbc);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 1;
        loginFrame.add(passLabel, gbc);

        JPasswordField passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        loginFrame.add(passwordField, gbc);

        JLabel captchaLabel = new JLabel("CAPTCHA:");
        captchaLabel.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 2;
        loginFrame.add(captchaLabel, gbc);

        String[] captchaText = new String[1];
        captchaText[0] = generateCaptchaText();
        JLabel captchaImage = new JLabel(new ImageIcon(generateCaptchaImage(captchaText[0])));
        JButton refreshCaptchaButton = new JButton("🔄");
        refreshCaptchaButton.setPreferredSize(new Dimension(50, 30));
        refreshCaptchaButton.setBackground(new Color(255, 165, 0));
        refreshCaptchaButton.setForeground(Color.BLACK);

        JPanel captchaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        captchaPanel.setBackground(new Color(255, 248, 220));
        captchaPanel.add(captchaImage);
        captchaPanel.add(refreshCaptchaButton);

        gbc.gridx = 1;
        gbc.gridy = 2;
        loginFrame.add(captchaPanel, gbc);

        JTextField captchaField = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 3;
        loginFrame.add(captchaField, gbc);

        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(34, 139, 34)); // Forest Green
        loginButton.setForeground(Color.BLACK);
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        loginFrame.add(loginButton, gbc);

        JButton backButton = new JButton("Back");
        backButton.setBackground(new Color(255, 69, 0)); // Red-Orange
        backButton.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 4;
        loginFrame.add(backButton, gbc);

        backButton.addActionListener(e -> {
            loginFrame.dispose();
            main(null);
        });

        refreshCaptchaButton.addActionListener(e -> {
            captchaText[0] = generateCaptchaText();
            captchaImage.setIcon(new ImageIcon(generateCaptchaImage(captchaText[0])));
        });

        loginButton.addActionListener(e -> {
            String inputCaptcha = captchaField.getText();
            if (!inputCaptcha.equals(captchaText[0])) {
                JOptionPane.showMessageDialog(loginFrame, "Invalid CAPTCHA input! Please enter the above details.");
                return;
            }

            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (authenticateAdmin(username, password)) {
                JOptionPane.showMessageDialog(loginFrame, "Admin Login Successful!");
                AdminPage.main(new String[]{});
                loginFrame.dispose();

            } else {
                JOptionPane.showMessageDialog(loginFrame, "Admin Login Failed! Check your username or password.");
            }
        });

        loginFrame.setVisible(true);
    }

    private static boolean authenticateAdmin(String username, String password) {
        try (Connection conn = getConnection()) {
            String query = "SELECT * FROM admin WHERE username = ? AND password = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                stmt.setString(2, password);
                try (ResultSet rs = stmt.executeQuery()) {
                    return rs.next(); // Return true if a matching row is found
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static String generateCaptchaText() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder captcha = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            int index = (int) (Math.random() * chars.length());
            captcha.append(chars.charAt(index));
        }
        return captcha.toString();
    }

    private static BufferedImage generateCaptchaImage(String captchaText) {
        BufferedImage captchaImage = new BufferedImage(200, 50, BufferedImage.TYPE_INT_RGB);
        Graphics g = captchaImage.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 200, 50);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString(captchaText, 20, 35);
        g.dispose();
        return captchaImage;
    }
}
