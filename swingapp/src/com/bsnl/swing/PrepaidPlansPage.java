package com.bsnl.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrepaidPlansPage {

    private JFrame frame;
    private String email;
    private String phoneNo;
    private int accId;

    public PrepaidPlansPage(int accId, String phoneNo, String email) {
        this.accId = accId;
        this.phoneNo = phoneNo;
        this.email = email;
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Available Prepaid Plans");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        frame.add(panel);
        JLabel headerLabel = new JLabel("Available Prepaid Plans");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(headerLabel, BorderLayout.NORTH);

        JPanel plansPanel = new JPanel();
        plansPanel.setLayout(new BoxLayout(plansPanel, BoxLayout.Y_AXIS));
        plansPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

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
            URL url = new URL("http://localhost:8081/adjservlets/PrepaidPlansServlet");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                String[] fields = inputLine.split("___");
                if (fields.length >= 12) {
                    JPanel planPanel = new JPanel();
                    planPanel.setLayout(new BoxLayout(planPanel, BoxLayout.Y_AXIS));
                    planPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                    planPanel.add(new JLabel("Plan ID: " + fields[0]));
                    planPanel.add(new JLabel("Plan Name: " + fields[1]));
                    planPanel.add(new JLabel("Validity: " + fields[2] + " days"));
                    planPanel.add(new JLabel("Cost: $" + fields[3]));
                    planPanel.add(new JLabel("Free Talktime: $" + fields[4]));
                    planPanel.add(new JLabel("Free Data: " + fields[5] + " GB"));
                    planPanel.add(new JLabel("Free Daily Data: " + fields[6] + " GB"));
                    planPanel.add(new JLabel("Free SMS: " + fields[7]));
                    planPanel.add(new JLabel("Free SMS per Day: " + fields[8]));
                    planPanel.add(new JLabel("Data Charges: $" + fields[9] + " per GB"));
                    planPanel.add(new JLabel("Call Charges on Net: $" + fields[10]));
                    planPanel.add(new JLabel("Call Charges off Net: $" + fields[11]));
                    planPanel.add(new JLabel("Category: " + fields[12]));

                    JButton subscribeButton = new JButton("Subscribe");
                    subscribeButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                            	String cityname = JOptionPane.showInputDialog(frame, "Enter the city name:");
                                URL url = new URL("http://localhost:8081/adjservlets/SubscribePrepaidServlet");
                                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                conn.setRequestMethod("POST");
                                conn.setDoOutput(true);

                                // Get current date
                                Date currentDate = new Date();
                                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                String currentDateString = dateFormat.format(currentDate);

                                // Compute next due date based on validity
                                String validityDaysString = fields[2]; // Assuming validity in days is in fields[2]
                                int validityDays = Integer.parseInt(validityDaysString);
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(currentDate);
                                cal.add(Calendar.DATE, validityDays);
                                Date nextDueDate = cal.getTime();
                                String nextDueDateString = dateFormat.format(nextDueDate);
                     
                                String maxdata, maxsms;

                                if (!fields[5].equals("0.0"))
                                    maxdata = fields[5];
                                else if (!fields[6].equals("0.0"))
                                    maxdata = fields[6];
                                else
                                    maxdata = "0";

                                System.out.println(maxdata);

                                if (!fields[7].equals("0"))
                                    maxsms = fields[7];
                                else if (!fields[8].equals("0"))
                                    maxsms = fields[8];
                                else
                                    maxsms = "0";

                                System.out.println(maxsms);
                                
                                // Construct jsonInputString
                                String jsonInputString = 
                                        "phoneNo=" + phoneNo +
                                        "&planId=" + fields[0] +
                                        "&maxDataUsage=" + maxdata +
                                        "&maxTalktime="+fields[4]+
                                        "&maxSms="+maxsms+
                                        "&totalTariff="+fields[3]+
                                        "&lastTransactionDate=" + currentDateString +
                                        "&nextDueDate=" + nextDueDateString+
                                        "&cityName="+cityname;

                                try (OutputStream os = conn.getOutputStream()) {
                                    byte[] input = jsonInputString.getBytes("utf-8");
                                    os.write(input, 0, input.length);
                                }

                                int responseCode = conn.getResponseCode();
                                if (responseCode == HttpURLConnection.HTTP_OK) {
                                    JOptionPane.showMessageDialog(frame, "Subscribed to " + fields[1]);
                                } else {
                                    JOptionPane.showMessageDialog(frame, "Subscription failed");
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                    planPanel.add(subscribeButton);

                    plansPanel.add(planPanel);
                    plansPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Space between plans
                }
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        JScrollPane scrollPane = new JScrollPane(plansPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }
}
