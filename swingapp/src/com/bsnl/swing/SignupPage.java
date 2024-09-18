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

public class SignupPage {

    private JFrame frame;
    private JTextField accIdField;
    private JTextField categoryField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField emailField;
    private JTextField phoneField;

    public SignupPage() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Signup");
        frame.setSize(400, 350);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10)); // 7 rows, 2 columns
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel accIdLabel = new JLabel("Account ID:");
        accIdField = new JTextField();
        JLabel categoryLabel = new JLabel("Account Category:");
        categoryField = new JTextField();
        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField();
        JLabel phoneLabel = new JLabel("Phone Number:");
        phoneField = new JTextField();

        panel.add(accIdLabel);
        panel.add(accIdField);
        panel.add(categoryLabel);
        panel.add(categoryField);
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(phoneLabel);
        panel.add(phoneField);

        JButton signupButton = new JButton("Signup");
        panel.add(new JLabel()); // Empty label for spacing
        panel.add(signupButton);

        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String accId = accIdField.getText();
                String category = categoryField.getText();
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String email = emailField.getText();
                String phone = phoneField.getText();

                if (accId.isEmpty() || category.isEmpty() || username.isEmpty() || password.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    // Send data to servlet
                    String data = URLEncoder.encode("acc_id", "UTF-8") + "=" + URLEncoder.encode(accId, "UTF-8");
                    data += "&" + URLEncoder.encode("acc_category", "UTF-8") + "=" + URLEncoder.encode(category, "UTF-8");
                    data += "&" + URLEncoder.encode("uname", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
                    data += "&" + URLEncoder.encode("pword", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                    data += "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
                    data += "&" + URLEncoder.encode("phone_no", "UTF-8") + "=" + URLEncoder.encode(phone, "UTF-8");

                    URL url = new URL("http://localhost:8081/adjservlets/RegisterServlet");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                    PrintWriter out = new PrintWriter(conn.getOutputStream());
                    out.print(data);
                    out.flush();

                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String response = in.readLine();
                    in.close();

                    if (response.equals("success")) {
                        JOptionPane.showMessageDialog(frame, "Signup successful.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        frame.dispose(); // Close signup window after success
                        
                        // Optionally, navigate to another page or perform other actions
                    } else {
                        JOptionPane.showMessageDialog(frame, "Signup failed.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "An error occurred.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        frame.add(panel);
        frame.setVisible(true);
    }

}
