package com.demo.evotingportal;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.chart.plot.PlotOrientation;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ResultPage {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Election Results");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 700);
            frame.setLayout(new BorderLayout());

            frame.setLocationRelativeTo(null);
            frame.getContentPane().setBackground(new Color(211, 211, 211));

            JPanel contentPanel = new JPanel();
            contentPanel.setLayout(new GridBagLayout());
            contentPanel.setBackground(new Color(211, 211, 211));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.BOTH;

            JLabel winnerLabel = new JLabel("Election Results", SwingConstants.CENTER);
            winnerLabel.setFont(new Font("Serif", Font.BOLD, 30));
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            contentPanel.add(winnerLabel, gbc);

            JTextArea voteStatsArea = new JTextArea(5, 30);
            voteStatsArea.setEditable(false);
            voteStatsArea.setFont(new Font("Serif", Font.PLAIN, 18));
            voteStatsArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            gbc.gridy = 1;
            gbc.gridwidth = 2;
            contentPanel.add(new JScrollPane(voteStatsArea), gbc);

            DefaultPieDataset pieDataset = new DefaultPieDataset();
            DefaultCategoryDataset barDataset = new DefaultCategoryDataset();

            fetchResults(pieDataset, barDataset, voteStatsArea);

            JFreeChart pieChart = ChartFactory.createPieChart(
                    "Votes Distribution",
                    pieDataset,
                    true, true, false
            );

            PiePlot piePlot = (PiePlot) pieChart.getPlot();
            piePlot.setBackgroundPaint(Color.WHITE);
            piePlot.setSectionPaint("COMPUTER", new Color(255, 100, 100));
            piePlot.setSectionPaint("IT", new Color(100, 255, 100));
            piePlot.setSectionPaint("AI&DS", new Color(100, 100, 255));
            piePlot.setSectionPaint("EXTC", new Color(255, 255, 100));
            piePlot.setSectionPaint("CHEMICAL", new Color(255, 150, 150));

            ChartPanel pieChartPanel = new ChartPanel(pieChart);
            pieChartPanel.setPreferredSize(new Dimension(400, 300));
            pieChartPanel.setBorder(BorderFactory.createTitledBorder("Pie Chart"));
            gbc.gridy = 2;
            gbc.gridwidth = 1;
            gbc.weightx = 0.5;
            contentPanel.add(pieChartPanel, gbc);

            JFreeChart barChart = ChartFactory.createBarChart(
                    "Votes per Party",
                    "Parties",
                    "Votes",
                    barDataset,
                    PlotOrientation.VERTICAL,
                    true,
                    true,
                    false
            );

            ChartPanel barChartPanel = new ChartPanel(barChart);
            barChartPanel.setPreferredSize(new Dimension(400, 300));
            barChartPanel.setBorder(BorderFactory.createTitledBorder("Bar Chart"));
            gbc.gridx = 1;
            gbc.weightx = 0.5;
            contentPanel.add(barChartPanel, gbc);

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

            JButton downloadButton = new JButton("Download Results");
            downloadButton.setPreferredSize(new Dimension(430, 30));
            downloadButton.addActionListener(e -> downloadResults());
            buttonPanel.add(downloadButton);

            JButton exitButton = new JButton("Exit");
            exitButton.setPreferredSize(new Dimension(430, 30));
            exitButton.addActionListener(e -> System.exit(0));
            buttonPanel.add(exitButton);

            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.SOUTHEAST;
            contentPanel.add(buttonPanel, gbc);

            frame.add(contentPanel, BorderLayout.CENTER);
            frame.setVisible(true);
        });
    }
    private static void downloadResults() {
        String filePath = "election_results.csv";
        String url = "jdbc:mysql://localhost:3306/evoting_portal";
        String username = "root";
        String password = "Ashmit_06!";

        String query = "SELECT party_name, COUNT(*) AS vote_count FROM votes GROUP BY party_name";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery();
             BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

            writer.write("Party Name,Votes\n");
            while (rs.next()) {
                String partyName = rs.getString("party_name");
                int voteCount = rs.getInt("vote_count");
                writer.write(partyName + "," + voteCount + "\n");
            }

            JOptionPane.showMessageDialog(null, "Results downloaded as " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error writing to file.");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching results.");
        }
    }

    private static void fetchResults(DefaultPieDataset pieDataset, DefaultCategoryDataset barDataset, JTextArea voteStatsArea) {
        String url = "jdbc:mysql://localhost:3306/evoting_portal";
        String username = "root";
        String password = "Ashmit_06!";

        String query = "SELECT party_name, COUNT(*) AS vote_count FROM votes GROUP BY party_name";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            StringBuilder stats = new StringBuilder("Votes:\n");
            while (rs.next()) {
                String partyName = rs.getString("party_name");
                int voteCount = rs.getInt("vote_count");

                pieDataset.setValue(partyName, voteCount);
                barDataset.addValue(voteCount, "Votes", partyName);

                stats.append(partyName).append(": ").append(voteCount).append(" votes\n");
            }

            String[] parties = {"COMPUTER", "IT", "AI&DS", "EXTC", "CHEMICAL"};
            for (String party : parties) {
                if (!pieDataset.getKeys().contains(party)) {
                    pieDataset.setValue(party, 0);
                    barDataset.addValue(0, "Votes", party);
                }
            }

            voteStatsArea.setText(stats.toString());

        } catch (Exception e) {
            e.printStackTrace();
            voteStatsArea.setText("Error fetching results.");
        }
    }
}
