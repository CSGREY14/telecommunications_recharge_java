package com.bsnl.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BroadbandComplaintsPage {

    private JFrame frame;
    private String email;

    public BroadbandComplaintsPage(int accId, String email) {
        this.email = email;
        initialize(accId);
    }

    private void initialize(int accId) {
        frame = new JFrame("Broadband Complaints");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        frame.add(panel);
        JLabel welcomeLabel = new JLabel("Welcome User");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(welcomeLabel, BorderLayout.NORTH);

        JPanel complaintsPanel = new JPanel();
        complaintsPanel.setLayout(new BoxLayout(complaintsPanel, BoxLayout.Y_AXIS));
        complaintsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add a "Back" button to navigate to the HomePage
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new HomePage(email);  // Pass the email to the HomePage
            }
        });
        panel.add(backButton, BorderLayout.SOUTH);

        try {
            URL url = new URL("http://localhost:8081/adjservlets/ComplaintsServlet?accId=" + accId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                String[] fields = inputLine.split("___");
                if (fields.length >= 5) {
                    JPanel complaintPanel = new JPanel();
                    complaintPanel.setLayout(new BoxLayout(complaintPanel, BoxLayout.Y_AXIS));
                    complaintPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                    complaintPanel.add(new JLabel("Complaint ID: " + fields[0]));
                    complaintPanel.add(new JLabel("Account ID: " + fields[1]));
                    complaintPanel.add(new JLabel("Details: " + fields[2]));
                    complaintPanel.add(new JLabel("Filed Date: " + fields[3]));
                    complaintPanel.add(new JLabel("Status: " + fields[4]));

                    // Style the panel based on the status
                    String status = fields[4].trim();
                    if ("Initiated".equalsIgnoreCase(status)) {
                        complaintPanel.setBackground(Color.RED);
                    } else if ("Under Investigation".equalsIgnoreCase(status)) {
                        complaintPanel.setBackground(Color.YELLOW);
                    } else if ("Closed".equalsIgnoreCase(status)) {
                        complaintPanel.setBackground(Color.GREEN);
                    }

                    complaintsPanel.add(complaintPanel);
                    complaintsPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Space between complaints
                }
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        JScrollPane scrollPane = new JScrollPane(complaintsPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }
}
