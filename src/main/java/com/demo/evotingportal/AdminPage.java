    package com.demo.evotingportal;

    import javax.swing.JTable;
    import javax.swing.table.DefaultTableModel;
    import javax.swing.*;
    import javax.swing.border.LineBorder;
    import java.awt.*;
    import java.awt.event.ActionEvent;
    import java.awt.event.ActionListener;
    import java.sql.Connection;
    import java.sql.DriverManager;
    import java.sql.PreparedStatement;
    import java.sql.ResultSet;
    import java.sql.SQLException;
    import java.io.BufferedWriter;
    import java.io.FileWriter;
    import java.io.IOException;


    public class AdminPage {

        private static JLabel voterCountLabel = new JLabel("Voters Count: 0");
        private static JLabel votesLabelComputer = new JLabel("COMPUTER Votes (🌙): 0");
        private static JLabel votesLabelIT = new JLabel("IT Votes (🔥): 0");
        private static JLabel votesLabelAIDS = new JLabel("AI&DS Votes (⭐): 0");
        private static JLabel votesLabelEXTC = new JLabel("EXTC Votes (🚀): 0");
        private static JLabel votesLabelChemical = new JLabel("CHEMICAL Votes (🌿): 0");


        private static boolean isElectionTerminated = false;
        public static void main(String[] args) {
            // Create the main frame
            JFrame frame = new JFrame("Election Control Panel");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400);
            frame.setLayout(new GridBagLayout());
            
         


            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);  // Spacing between components
            gbc.fill = GridBagConstraints.HORIZONTAL;

            frame.getContentPane().setBackground(new Color(211, 211, 211));

            JButton terminateElectionButton = createStyledButton("Terminate Election");

            JButton holdElectionButton = createStyledButton("Hold Election");
            holdElectionButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            holdElection();
        }
    });
            JButton resumeElectionButton = createStyledButton("Resume Election");
            resumeElectionButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            resumeElection();
        }
    });
            JButton showVotersButton = createStyledButton("Show Voters");
            showVotersButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            showVoters();
        }
    });

            JButton logoutButton = createStyledLogoutButton("Logout");

            JButton downloadVotersButton = createStyledButton("Download Voters List");
            downloadVotersButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    downloadVotersList();
                }
            });

            voterCountLabel = createStyledLabel(voterCountLabel);
            votesLabelComputer = createStyledLabel(votesLabelComputer);
            votesLabelIT = createStyledLabel(votesLabelIT);
            votesLabelAIDS = createStyledLabel(votesLabelAIDS);
            votesLabelEXTC = createStyledLabel(votesLabelEXTC);
            votesLabelChemical = createStyledLabel(votesLabelChemical);

            terminateElectionButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    terminateElection();
                    JOptionPane.showMessageDialog(frame, "Election terminated. All votes and feedback have been deleted.");
                    resetVoteCounts();
                }
            });

            logoutButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    LoginInterface.main(new String[]{});
                    frame.dispose();
                }
            });

            holdElectionButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Logic will be added later
                }
            });

            resumeElectionButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Logic will be added later
                }
            });

            showVotersButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Logic will be added later
                }
            });


            gbc.gridy = 3;
            gbc.gridx = 1;
            frame.add(downloadVotersButton, gbc);

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            voterCountLabel.setFont(new Font("Arial", Font.BOLD, 16));
            frame.add(voterCountLabel, gbc);

            gbc.gridy = 1;
            gbc.gridwidth = 1;
            gbc.gridx = 0;
            frame.add(terminateElectionButton, gbc);

            gbc.gridx = 1;
            frame.add(downloadVotersButton, gbc);

            gbc.gridy = 2;
            gbc.gridx = 0;
            frame.add(holdElectionButton, gbc);

            gbc.gridx = 1;
            frame.add(resumeElectionButton, gbc);

            gbc.gridy = 3;
            gbc.gridx = 0;
            frame.add(showVotersButton, gbc);

            gbc.gridy = 3;
            gbc.gridx = 1;
            gbc.anchor = GridBagConstraints.EAST;
            frame.add(logoutButton, gbc);


            gbc.gridy = 4;
            gbc.gridx = 0;
            frame.add(votesLabelComputer, gbc);

            gbc.gridx = 1;
            frame.add(votesLabelIT, gbc);

            gbc.gridy = 5;
            gbc.gridx = 0;
            frame.add(votesLabelAIDS, gbc);

            gbc.gridx = 1;
            frame.add(votesLabelEXTC, gbc);

            gbc.gridy = 6;
            gbc.gridx = 0;
            frame.add(votesLabelChemical, gbc);

            updateLiveVotingCounts();

            frame.setLocationRelativeTo(null);

            frame.setVisible(true);
        }



        private static void updateLiveVotingCounts() {

            String url = "jdbc:mysql://localhost:3306/evoting_portal";
            String username = "root";
            String password = "Ashmit_06!";

            String query = "SELECT COUNT(*) AS total_voters, " +
                           "SUM(CASE WHEN party_name = 'COMPUTER' THEN 1 ELSE 0 END) AS computer_votes, " +
                           "SUM(CASE WHEN party_name = 'IT' THEN 1 ELSE 0 END) AS it_votes, " +
                           "SUM(CASE WHEN party_name = 'AI&DS' THEN 1 ELSE 0 END) AS aids_votes, " +
                           "SUM(CASE WHEN party_name = 'EXTC' THEN 1 ELSE 0 END) AS extc_votes, " +
                           "SUM(CASE WHEN party_name = 'CHEMICAL' THEN 1 ELSE 0 END) AS chemical_votes " +
                           "FROM votes";

            try (Connection conn = DriverManager.getConnection(url, username, password);
                 PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    int totalVoters = rs.getInt("total_voters");
                    int computerVotes = rs.getInt("computer_votes");
                    int itVotes = rs.getInt("it_votes");
                    int aidsVotes = rs.getInt("aids_votes");
                    int extcVotes = rs.getInt("extc_votes");
                    int chemicalVotes = rs.getInt("chemical_votes");

                    // Update labels with the live voting data
                    voterCountLabel.setText("Voters Count: " + totalVoters);
                    votesLabelComputer.setText("COMPUTER Votes (🌙): " + computerVotes);
                    votesLabelIT.setText("IT Votes (🔥): " + itVotes);
                    votesLabelAIDS.setText("AI&DS Votes (⭐): " + aidsVotes);
                    votesLabelEXTC.setText("EXTC Votes (🚀): " + extcVotes);
                    votesLabelChemical.setText("CHEMICAL Votes (🌿): " + chemicalVotes);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        private static void terminateElection() {
            String url = "jdbc:mysql://localhost:3306/evoting_portal";
            String username = "root";
            String password = "Ashmit_06!";

            String deleteVotesQuery = "DELETE FROM votes";
            String deleteFeedbackQuery = "DELETE FROM feedback";

            try (Connection conn = DriverManager.getConnection(url, username, password);
                 PreparedStatement stmtVotes = conn.prepareStatement(deleteVotesQuery);
                 PreparedStatement stmtFeedback = conn.prepareStatement(deleteFeedbackQuery)) {

                stmtVotes.executeUpdate();
                stmtFeedback.executeUpdate();

                isElectionTerminated = true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        private static void downloadVotersList() {
            String url = "jdbc:mysql://localhost:3306/evoting_portal";
            String username = "root";
            String password = "Ashmit_06!";

            String query = "SELECT username, (SELECT COUNT(*) FROM votes WHERE voter_username = username) > 0 AS has_voted FROM voter";

            try (Connection conn = DriverManager.getConnection(url, username, password);
                 PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                StringBuilder csvData = new StringBuilder("Voter Username,Has Voted\n");

                while (rs.next()) {
                    String voterUsername = rs.getString("username");
                    String hasVoted = rs.getBoolean("has_voted") ? "Yes" : "No";

                    csvData.append(voterUsername).append(",").append(hasVoted).append("\n");
                }

                // Write CSV data to file
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("voters_list.csv"))) {
                    writer.write(csvData.toString());
                    JOptionPane.showMessageDialog(null, "Voters list has been downloaded as voters_list.csv", "Download Successful", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error saving file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error fetching voters list: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private static boolean hasVotes() {
            String url = "jdbc:mysql://localhost:3306/evoting_portal";
            String username = "root";
            String password = "Ashmit_06!";

            String query = "SELECT COUNT(*) AS total_votes FROM votes";

            try (Connection conn = DriverManager.getConnection(url, username, password);
                 PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt("total_votes") > 0;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }

        private static void resetVoteCounts() {
            voterCountLabel.setText("Voters Count: 0");
            votesLabelComputer.setText("COMPUTER Votes (🌙): 0");
            votesLabelIT.setText("IT Votes (🔥): 0");
            votesLabelAIDS.setText("AI&DS Votes (⭐): 0");
            votesLabelEXTC.setText("EXTC Votes (🚀): 0");
            votesLabelChemical.setText("CHEMICAL Votes (🌿): 0");
        }

        private static JButton createStyledButton(String text) {
            JButton button = new JButton(text);
            button.setFont(new Font("Arial", Font.PLAIN, 14));
            button.setBackground(new Color(70, 130, 180));
            button.setForeground(Color.BLACK);
            button.setBorder(new LineBorder(Color.BLACK, 2, true));
            button.setPreferredSize(new Dimension(150, 40));
            return button;
        }

        private static JButton createStyledLogoutButton(String text) {
            JButton button = new JButton(text);
            button.setFont(new Font("Arial", Font.PLAIN, 14));
            button.setBackground(Color.RED);
            button.setForeground(Color.BLACK);
            button.setBorder(new LineBorder(Color.BLACK, 2, true));
            button.setPreferredSize(new Dimension(150, 40));
            return button;
        }

        private static JLabel createStyledLabel(JLabel label) {
            label.setFont(new Font("Arial", Font.PLAIN, 14));
            label.setForeground(Color.BLACK);
            return label;
        }

    private static void showVoters() {
        JFrame votersFrame = new JFrame("Voters List");
        votersFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        votersFrame.setSize(300, 300);
        votersFrame.setLayout(new BorderLayout());

        JPanel searchPanel = new JPanel();
        JTextField searchField = new JTextField(15);
        JButton searchButton = new JButton("Search");

        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        votersFrame.add(searchPanel, BorderLayout.NORTH);

        String[] columnNames = {"Voter Username", "Has Voted"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable votersTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(votersTable);
        votersFrame.add(scrollPane, BorderLayout.CENTER);

        fetchAndDisplayVoters(tableModel, "");

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchText = searchField.getText();
                fetchAndDisplayVoters(tableModel, searchText);
            }
        });

        votersFrame.setLocationRelativeTo(null);
        votersFrame.setVisible(true);
    }

    private static void fetchAndDisplayVoters(DefaultTableModel tableModel, String searchText) {
        tableModel.setRowCount(0);

        String url = "jdbc:mysql://localhost:3306/evoting_portal";
        String username = "root";
        String password = "Ashmit_06!";

        String query = "SELECT username, (SELECT COUNT(*) FROM votes WHERE voter_username = username) > 0 AS has_voted FROM voter WHERE username LIKE ?";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "%" + searchText + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String voterUsername = rs.getString("username");
                String hasVoted = rs.getBoolean("has_voted") ? "Yes" : "No";

                tableModel.addRow(new Object[]{voterUsername, hasVoted});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void holdElection() {
        String url = "jdbc:mysql://localhost:3306/evoting_portal";
        String username = "root";
        String password = "Ashmit_06!";

        String query = "UPDATE election_status SET is_active = FALSE WHERE id = 1";
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Elections are now on hold.", "Election Status", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Failed to hold the election. Please check the status.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void resumeElection() {
        String url = "jdbc:mysql://localhost:3306/evoting_portal";
        String username = "root";
        String password = "Ashmit_06!";

        String query = "UPDATE election_status SET is_active = TRUE WHERE id = 1";
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Elections have been resumed.", "Election Status", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Failed to resume the election. Please check the status.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    }
