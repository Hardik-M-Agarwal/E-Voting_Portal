package com.demo.evotingportal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class VotingPortal {

    private static String currentUsername;
    private static JLabel voteDisplayLabel;
    private static JButton[] buttons;
    private static Timer voteTimer;
    private static JLabel timerLabel;
    private static int timeLeft = 30;
    private static Timer feedbackDelayTimer;

    public static void launchVotingPortal(String username) {
        currentUsername = username;

        JFrame frame = new JFrame("E-Voting Portal");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLayout(new BorderLayout());

        frame.setLocationRelativeTo(null);

        String[] partyNames = {"COMPUTER", "IT", "AI&DS", "EXTC", "CHEMICAL"};
        String[] partySymbols = {"🌙", "🔥", "⭐", "🚀", "🌿"};

        JPanel partyPanel = new JPanel();
        partyPanel.setLayout(new GridLayout(5, 2, 10, 10));
        partyPanel.setBackground(Color.LIGHT_GRAY);

        buttons = new JButton[partyNames.length];

        for (int i = 0; i < partyNames.length; i++) {
            String name = partyNames[i];
            String symbol = partySymbols[i];

            JButton nameButton = new JButton(name);
            nameButton.setFont(new Font("Arial", Font.PLAIN, 20));
            nameButton.setPreferredSize(new Dimension(100, 60));
            buttons[i] = nameButton;

            JLabel emojiLabel = new JLabel(symbol, JLabel.CENTER);
            emojiLabel.setFont(new Font("Arial", Font.PLAIN, 40));

            partyPanel.add(nameButton);
            partyPanel.add(emojiLabel);

            nameButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    handleVote(nameButton, name);
                }
            });
        }

        voteDisplayLabel = new JLabel("Your vote will appear here.", JLabel.CENTER);
        voteDisplayLabel.setFont(new Font("Arial", Font.BOLD, 24));
        voteDisplayLabel.setPreferredSize(new Dimension(400, 120));
        voteDisplayLabel.setBackground(Color.WHITE);
        voteDisplayLabel.setOpaque(true);

        timerLabel = new JLabel("Time left: 30s", SwingConstants.RIGHT);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timerLabel.setForeground(Color.RED);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.GRAY);
        topPanel.add(timerLabel, BorderLayout.EAST);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(partyPanel, BorderLayout.CENTER);
        frame.add(voteDisplayLabel, BorderLayout.SOUTH);

        startTimer();

        frame.setVisible(true);
    }

    private static void startTimer() {
        voteTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeLeft--;
                timerLabel.setText("Time left: " + timeLeft + "s");
                if (timeLeft <= 0) {
                    disableAllButtons();
                    voteTimer.stop();
                    timerLabel.setText("Time's up!");
                }
            }
        });
        voteTimer.start();
    }

    private static void handleVote(JButton button, String name) {
        button.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));

        int confirmed = JOptionPane.showConfirmDialog(null, "Are you sure you want to vote for " + name + "?", "Confirm Vote", JOptionPane.YES_NO_OPTION);

        if (confirmed == JOptionPane.YES_OPTION) {
            voteDisplayLabel.setText("You voted for " + name);
            disableAllButtons();
            voteTimer.stop();
            
            insertVoteIntoDatabase(currentUsername, name);

            feedbackDelayTimer = new Timer(5000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    feedbackDelayTimer.stop();

                    JFrame votingFrame = (JFrame) SwingUtilities.getWindowAncestor(button);
                    votingFrame.dispose();

                    showFeedbackForm();
                }
            });
            feedbackDelayTimer.setRepeats(false);
            feedbackDelayTimer.start();
        }
    }
    
    private static void insertVoteIntoDatabase(String username, String partyName) {
        String url = "jdbc:mysql://localhost:3306/evoting_portal";
        String dbUser = "root";
        String dbPassword = "Ashmit_06!";

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword)) {
            String query = "INSERT INTO votes (voter_username, party_name) VALUES (?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, username);
                pstmt.setString(2, partyName);
                pstmt.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occurred while submitting the vote: " + ex.getMessage());
        }
    }

    public static void showFeedbackForm() {
        JPanel feedbackPanel = new JPanel();
        feedbackPanel.setLayout(new BoxLayout(feedbackPanel, BoxLayout.Y_AXIS));
        feedbackPanel.setBackground(Color.WHITE);

        JLabel ratingLabel = new JLabel("Rate us out of 5:");

        StarRating starRating = new StarRating(5, 30, Color.YELLOW, Color.GRAY);

        JLabel explanationLabel = new JLabel("Write a detailed explanation of your experience:");
        JTextArea explanationArea = new JTextArea(5, 30);
        JScrollPane scrollPane = new JScrollPane(explanationArea);
        explanationArea.setLineWrap(true);
        explanationArea.setWrapStyleWord(true);

        feedbackPanel.add(ratingLabel);
        feedbackPanel.add(starRating);
        feedbackPanel.add(explanationLabel);
        feedbackPanel.add(scrollPane);

        int result = JOptionPane.showConfirmDialog(null, feedbackPanel, "Feedback Form", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            int rating = starRating.getRating();
            String explanation = explanationArea.getText();

            insertFeedbackIntoDatabase(currentUsername, rating, explanation);

            JOptionPane.showMessageDialog(null, "Thank you for your feedback!");

            LoginInterface.main(new String[]{});
        }
    }

    // Inserts feedback into the database
    private static void insertFeedbackIntoDatabase(String username, int rating, String explanation) {
        String url = "jdbc:mysql://localhost:3306/evoting_portal";
        String dbUser = "root";
        String dbPassword = "Ashmit_06!";

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword)) {
            String query = "INSERT INTO feedback (voter_username, rating, feedback_text) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setInt(2, rating);
            pstmt.setString(3, explanation);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occurred while submitting feedback: " + ex.getMessage());
        }
    }

    private static void disableAllButtons() {
        for (JButton button : buttons) {
            button.setEnabled(false);
            button.setBorder(null);
        }
    }

    public static class StarRating extends JPanel {
        private int rating;
        private final int maxRating;
        private final int starSize;
        private final Color fillColor;
        private final Color emptyColor;

        public StarRating(int maxRating, int starSize, Color fillColor, Color emptyColor) {
            this.maxRating = maxRating;
            this.starSize = starSize;
            this.fillColor = fillColor;
            this.emptyColor = emptyColor;
            this.rating = 0;

            setLayout(new FlowLayout());
            for (int i = 1; i <= maxRating; i++) {
                JLabel starLabel = createStarLabel(i);
                add(starLabel);
            }

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int selectedRating = getStarAtPoint(e.getPoint());
                    if (selectedRating != -1) {
                        setRating(selectedRating);
                    }
                }
            });
        }

        private JLabel createStarLabel(int index) {
            JLabel starLabel = new JLabel("★");
            starLabel.setFont(new Font("Arial", Font.PLAIN, starSize));
            starLabel.setForeground(emptyColor);
            starLabel.setName(String.valueOf(index));
            return starLabel;
        }

        private int getStarAtPoint(Point point) {
            for (Component component : getComponents()) {
                if (component.getBounds().contains(point)) {
                    return Integer.parseInt(component.getName());
                }
            }
            return -1;
        }

        public void setRating(int rating) {
            this.rating = rating;
            for (int i = 0; i < maxRating; i++) {
                JLabel starLabel = (JLabel) getComponent(i);
                starLabel.setForeground(i < rating ? fillColor : emptyColor);
            }
        }

        public int getRating() {
            return rating;
        }
    }
}
