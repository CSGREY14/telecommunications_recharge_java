package com.bsnl.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class login {

    public static void main(String[] args) {
        // Create the frame
        JFrame frame = new JFrame("Login");
        frame.setSize(350, 350);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        frame.add(panel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel userLabel = new JLabel("Enter your email");
        panel.add(userLabel, gbc);

        gbc.gridy++;
        JTextField userText = new JTextField(20);
        panel.add(userText, gbc);

        gbc.gridy++;
        JLabel passwordLabel = new JLabel("Enter your password");
        panel.add(passwordLabel, gbc);

        gbc.gridy++;
        JPasswordField passwordText = new JPasswordField(20);
        panel.add(passwordText, gbc);

        gbc.gridy++;
        JButton forgotPasswordButton = new JButton("Forgot password?");
        forgotPasswordButton.setContentAreaFilled(false);
        forgotPasswordButton.setBorderPainted(false);
        forgotPasswordButton.setForeground(Color.BLUE.darker());
        panel.add(forgotPasswordButton, gbc);

        gbc.gridy++;
        JButton loginButton = new JButton("Submit");
        panel.add(loginButton, gbc);

        gbc.gridy++;
        JPanel signupPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JLabel signupLabel = new JLabel("Don't have an account?");
        signupPanel.add(signupLabel);

        JButton signupButton = new JButton("Signup");
        signupButton.setContentAreaFilled(false);
        signupButton.setBorderPainted(false);
        signupButton.setForeground(Color.BLUE.darker());
        signupPanel.add(signupButton);

        panel.add(signupPanel, gbc);

        // Action listeners for buttons
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = userText.getText();
                String password = new String(passwordText.getPassword());

                try {
                    // Construct data
                    String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
                    data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

                    // Send data
                    URL url = new URL("http://localhost:8081/adjservlets/LoginServlet");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                    PrintWriter out = new PrintWriter(conn.getOutputStream());
                    out.print(data);
                    out.flush();

                    // Get the response
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String response = in.readLine(); // Assuming the response is a single line
                    in.close();

                    // Process the response
                    if (response.equals("Login successful!")) {
                        JOptionPane.showMessageDialog(frame, "Login successful!");
                        frame.dispose(); // Close the login frame
                        new HomePage(email); // Open the home page with user's email
                    } else {
                        JOptionPane.showMessageDialog(frame, response);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        forgotPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle forgot password logic here
                JOptionPane.showMessageDialog(frame, "Forgot password button clicked.");
            }
        });

        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle signup logic here
            	frame.dispose(); // Close the login frame
                new SignupPage(); // Open the home page with user's email
            }
        });

        // Set frame visibility
        frame.setVisible(true);
    }
}
