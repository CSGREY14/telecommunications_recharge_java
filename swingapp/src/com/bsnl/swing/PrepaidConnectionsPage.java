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

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

public class PrepaidConnectionsPage {

    private JFrame frame;
    private String email;

    public PrepaidConnectionsPage(String phoneNo, String email) {
        this.email = email;
        initialize(phoneNo);
    }

    private void initialize(String phoneNo) {
        frame = new JFrame("Prepaid Connections");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        frame.add(panel);
        JLabel welcomeLabel = new JLabel("Welcome User");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(welcomeLabel, BorderLayout.NORTH);

        JPanel connectionsPanel = new JPanel();
        connectionsPanel.setLayout(new BoxLayout(connectionsPanel, BoxLayout.Y_AXIS));
        connectionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

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
            URL url = new URL("http://localhost:8081/adjservlets/PrepaidDetailsServlet?phoneNo=" + phoneNo);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                String[] fields = inputLine.split(",");
                if (fields.length >= 22) { 
                    JPanel connectionPanel = new JPanel();
                    connectionPanel.setLayout(new BoxLayout(connectionPanel, BoxLayout.Y_AXIS));
                    connectionPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                    connectionPanel.add(new JLabel("Plan ID: " + fields[0]));
                    connectionPanel.add(new JLabel("Plan Name: " + fields[1]));
                    connectionPanel.add(new JLabel("Validity: " + fields[2] + " days"));
                    connectionPanel.add(new JLabel("Cost: " + fields[3]));
                    connectionPanel.add(new JLabel("Free Talktime: " + fields[4]));
                    connectionPanel.add(new JLabel("Free Data: " + fields[5]));
                    connectionPanel.add(new JLabel("Free Daily Data: " + fields[6]));
                    connectionPanel.add(new JLabel("Free SMS: " + fields[7]));
                    connectionPanel.add(new JLabel("Free SMS per Day: " + fields[8]));
                    connectionPanel.add(new JLabel("Data Charges: " + fields[9]));
                    connectionPanel.add(new JLabel("On-net Call Charges: " + fields[10]));
                    connectionPanel.add(new JLabel("Off-net Call Charges: " + fields[11]));
                    connectionPanel.add(new JLabel("Category: " + fields[12]));
                    connectionPanel.add(new JLabel("Current Data Usage: " + fields[13]));
                    connectionPanel.add(new JLabel("Max Data Usage: " + fields[14]));
                    double remainingData = Double.parseDouble(fields[14]) - Double.parseDouble(fields[13]);
                    connectionPanel.add(new JLabel("Remaining Data: " + remainingData));
                    connectionPanel.add(new JLabel("Current Talktime: " + fields[15]));
                    connectionPanel.add(new JLabel("Max Talktime: " + fields[16]));
                    connectionPanel.add(new JLabel("Current SMS: " + fields[17]));
                    connectionPanel.add(new JLabel("Max SMS: " + fields[18]));
                    connectionPanel.add(new JLabel("Total Tariff: " + fields[19]));
                    connectionPanel.add(new JLabel("Last Transaction Date: " + fields[20]));
                    connectionPanel.add(new JLabel("Next Due Date: " + fields[21]));
                    connectionPanel.add(new JLabel("City Name: " + fields[22]));

                    // Add a pie chart for data usage
                    DefaultPieDataset dataset = new DefaultPieDataset();
                    dataset.setValue("Used", Double.parseDouble(fields[13]));
                    dataset.setValue("Remaining", remainingData);
                    JFreeChart chart = ChartFactory.createPieChart(
                            "Data Usage",
                            dataset,
                            true,
                            true,
                            false);
                    PiePlot plot = (PiePlot) chart.getPlot();
                    plot.setSectionPaint("Used", Color.RED);
                    plot.setSectionPaint("Remaining", Color.GREEN);
                    ChartPanel chartJPanel = new ChartPanel(chart);
                    chartJPanel.setPreferredSize(new Dimension(400, 300));
                    connectionPanel.add(chartJPanel);
                    
                 // Check if the next due date has passed
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date nextDueDate = sdf.parse(fields[21]);
                    Date currentDate = new Date();
                    if (nextDueDate.before(currentDate)) {
                        // Style the panel as expired
                        connectionPanel.setBackground(Color.RED);
                        JLabel expiredLabel = new JLabel("Expired");
                        expiredLabel.setFont(new Font("Arial", Font.BOLD, 14));
                        expiredLabel.setForeground(Color.WHITE);
                        connectionPanel.add(expiredLabel);
                        connectionPanel.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                    }

                    connectionsPanel.add(connectionPanel);
                    connectionsPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Space between connections
                }
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        JScrollPane scrollPane = new JScrollPane(connectionsPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }
}
