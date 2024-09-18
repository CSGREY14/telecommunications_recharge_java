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
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.*;

public class BroadbandPlansPage {

    private JFrame frame;
    private String email;
	private int accId;
	private String phoneNo;
    
    public BroadbandPlansPage(int accId,String phoneNo, String email) {
    	this.accId = accId;
    	this.phoneNo=phoneNo;
    	this.email = email;
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Available Broadband Plans");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        frame.add(panel);
        JLabel headerLabel = new JLabel("Available Broadband Plans");
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
            URL url = new URL("http://localhost:8081/adjservlets/BroadbandPlansServlet");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                String[] fields = inputLine.split("___");
                if (fields.length >= 9) {
                    JPanel planPanel = new JPanel();
                    planPanel.setLayout(new BoxLayout(planPanel, BoxLayout.Y_AXIS));
                    planPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                    planPanel.add(new JLabel("Plan ID: " + fields[0]));
                    planPanel.add(new JLabel("Plan Name: " + fields[1]));
                    planPanel.add(new JLabel("Category: " + fields[2]));
                    planPanel.add(new JLabel("Data and Speed: " + fields[3]));
                    planPanel.add(new JLabel("Validity: " + fields[4] + " days"));
                    planPanel.add(new JLabel("Price: " + fields[5]));
                    planPanel.add(new JLabel("Voice Calls: " + fields[6]));
                    planPanel.add(new JLabel("Static IP: " + fields[7]));
                    planPanel.add(new JLabel("Bundled OTT: " + fields[8]));

                    JButton subscribeButton = new JButton("Subscribe");
                    subscribeButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                URL url = new URL("http://localhost:8081/adjservlets/SubscribeServlet");
                                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                conn.setRequestMethod("POST");
                                conn.setDoOutput(true);
                             // Get current date
                                Date currentDate = new Date();
                                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                String currentDateString = dateFormat.format(currentDate);

                                // Compute next due date based on validity
                                String validityDaysString = fields[4]; // Assuming validity in days is in fields[4]
                                int validityDays = Integer.parseInt(validityDaysString);
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(currentDate);
                                cal.add(Calendar.DATE, validityDays);
                                Date nextDueDate = cal.getTime();
                                String nextDueDateString = dateFormat.format(nextDueDate);
                                double max_var;
                                //extract max data from data and speed
                             // Define a regex pattern to match the number after "upto" and before "GB"
                                /*String pattern = "upto\\s+(\\d+)\\s+GB";

                                // Compile the pattern into a regex object
                                Pattern regex = Pattern.compile(pattern);

                                // Create a matcher with the input string
                                Matcher matcher = regex.matcher(fields[3]);
                                if (matcher.find()) {
                                    // Extract the matched group
                                    String maxVarStr = matcher.group(1); // Group 1 is the first captured group (\d+)

                                    // Convert the matched string to an integer
                                    max_var = Integer.parseInt(maxVarStr);
                                 */
                                Pattern sizePattern = Pattern.compile("\\bupto\\s*(\\d+\\.?\\d*)\\s*(GB|TB)");
                                 Matcher matcher = sizePattern.matcher(fields[3]);

                                 if (matcher.find()) {
         
                                     if (matcher.group(2).equals("TB")) {
                                         max_var = Double.parseDouble(matcher.group(1)) * 1000;
                                     } else {
                                         max_var = Double.parseDouble(matcher.group(1));
                                     }   
                                } else {max_var=0;}
                                System.out.println(max_var);
                                // Construct jsonInputString
                                String jsonInputString = "accId=" + accId +
                                                        "&phoneNo=" + phoneNo +
                                                        "&planId=" + fields[0] +
                                                        "&maxDataUsage=" + max_var + 
                                                        "&lastTransactionDate=" + currentDateString +
                                                        "&nextDueDate=" + nextDueDateString;


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
