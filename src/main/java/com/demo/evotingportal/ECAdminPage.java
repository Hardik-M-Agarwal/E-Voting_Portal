 package com.demo.evotingportal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ECAdminPage {

    private static boolean isElectionTerminated = false;

    public static void main(String[] args) {
        JFrame frame = new JFrame("EC Admin Page");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridBagLayout());
frame.getContentPane().setBackground(new Color(211, 211, 211));

        JButton declareResultButton = new JButton("Declare Result");
        JButton logoutButton = new JButton("Logout");

        declareResultButton.setPreferredSize(new Dimension(150, 50));
        logoutButton.setPreferredSize(new Dimension(150, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        frame.add(declareResultButton, gbc);

        gbc.gridy = 1;

        frame.add(logoutButton, gbc);

        declareResultButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isElectionTerminated) {
                    updateLiveVotingCounts();
                    ResultPage.main(new String[]{});
                } else {
                    JOptionPane.showMessageDialog(frame, "Election is terminated. No results available.");
                }
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginInterface.main(new String[]{});
                frame.dispose();
            }
        });

        frame.setLocationRelativeTo(null);

        frame.setVisible(true);
    }

    private static void updateLiveVotingCounts() {
        System.out.println("Live voting counts updated.");
    }
}
