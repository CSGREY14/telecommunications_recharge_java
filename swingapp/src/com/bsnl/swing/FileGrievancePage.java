package com.bsnl.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class FileGrievancePage {

    private JFrame frame;
    private int accId;
    private String email;

    public FileGrievancePage(int accId, String email) {
        this.accId = accId;
        this.email = email;
        initialize();
    }

    private void initialize() {
        frame = new JFrame("File a Grievance");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        frame.add(panel);

        JLabel label = new JLabel("Enter the details of your complaint:");
        JTextArea complaintTextArea = new JTextArea(10, 30);

        // Panel for the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JButton submitButton = new JButton("Submit");
        JButton backButton = new JButton("Back");

        buttonPanel.add(backButton);
        buttonPanel.add(submitButton);

        panel.add(label, BorderLayout.NORTH);
        panel.add(new JScrollPane(complaintTextArea), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String details = complaintTextArea.getText();
                if (!details.isEmpty()) {
                    fileComplaint(details);
                } else {
                    JOptionPane.showMessageDialog(frame, "Please enter complaint details.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new HomePage(email);  // Pass the email to the HomePage
            }
        });

        frame.setVisible(true);
    }

    private void fileComplaint(String details) {
        try {
            // Prepare data to send to servlet
            String data = URLEncoder.encode("accId", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(accId), "UTF-8");
            data += "&" + URLEncoder.encode("details", "UTF-8") + "=" + URLEncoder.encode(details, "UTF-8");

            // Send data to servlet
            URL url = new URL("http://localhost:8081/adjservlets/FileGrievanceServlet"); // Update URL with your servlet URL
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            PrintWriter out = new PrintWriter(conn.getOutputStream());
            out.print(data);
            out.flush();

            // Get response from servlet
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                JOptionPane.showMessageDialog(frame, "Complaint filed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
                new HomePage(email);
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to file complaint.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "An error occurred while filing the complaint.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
