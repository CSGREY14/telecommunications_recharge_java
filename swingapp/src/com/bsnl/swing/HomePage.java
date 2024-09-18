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

public class HomePage {

    private JFrame frame;
    private String username;
    private String email;
    private String phoneNo;
    private int accId;

    public HomePage(String email) {
        fetchUserDetailsFromServlet(email);
        initialize();
    }

    private void fetchUserDetailsFromServlet(String email) {
        try {
            // Construct data to send to servlet
            String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");

            // Send data to servlet
            URL url = new URL("http://localhost:8081/adjservlets/UserDetailsServlet"); // Update URL with your servlet URL
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            PrintWriter out = new PrintWriter(conn.getOutputStream());
            out.print(data);
            out.flush();

            // Get response from servlet
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            // Process servlet response
            String[] userDetails = response.toString().split(",");
            this.username = userDetails[0];
            this.email = userDetails[1];
            this.phoneNo = userDetails[2];
            this.accId = Integer.parseInt(userDetails[3]);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initialize() {
        frame = new JFrame("User Dashboard");
        frame.setSize(900, 600); // Adjusted frame size for the carousel
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        frame.add(mainPanel);

        // Navigation bar
        JMenuBar menuBar = new JMenuBar();
        JMenu homeMenu = new JMenu("Home");
        JMenu viewConnectionsMenu = new JMenu("View Connections");
        JMenu addConnectionsMenu = new JMenu("Add Connections");
        JMenu viewGrievancesMenu = new JMenu("View My Grievances");
        JMenu fileGrievanceMenu = new JMenu("File a Grievance");
        JMenu logoutMenu = new JMenu("Logout");
        JMenuItem bd = new JMenuItem("Broadband");
        JMenuItem pp = new JMenuItem("Prepaid");
        JMenuItem abd = new JMenuItem("Broadband");
        JMenuItem app = new JMenuItem("Prepaid");

        menuBar.add(homeMenu);
        viewConnectionsMenu.add(bd);
        viewConnectionsMenu.add(pp);
        addConnectionsMenu.add(abd);
        addConnectionsMenu.add(app);        
        menuBar.add(viewConnectionsMenu);
        menuBar.add(addConnectionsMenu);
        menuBar.add(viewGrievancesMenu);
        menuBar.add(fileGrievanceMenu);
        menuBar.add(logoutMenu);

        mainPanel.add(menuBar, BorderLayout.NORTH);

        // Content panel for main dashboard
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Top panel for carousel
        JPanel carouselPanel = new JPanel();
        carouselPanel.setPreferredSize(new Dimension(900, 300)); // Set preferred size for carousel
        contentPanel.add(carouselPanel, BorderLayout.NORTH);

        // Initialize and add carousel to the carousel panel
        ImageCarousel imageCarousel = new ImageCarousel();
        carouselPanel.add(imageCarousel);

        // Center panel for user information
        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
        contentPanel.add(userInfoPanel, BorderLayout.CENTER);

        JLabel welcomeLabel = new JLabel("Welcome, " + username);
        JLabel emailLabel = new JLabel("Email: " + email);
        JLabel phoneLabel = new JLabel("Phone No: " + phoneNo);

        userInfoPanel.add(welcomeLabel);
        userInfoPanel.add(emailLabel);
        userInfoPanel.add(phoneLabel);


       
        // Broadband menu actions
        bd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new BroadbandConnectionsPage(accId, email);
            }
        });
        
        pp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new PrepaidConnectionsPage(phoneNo, email);
            }
        });
        
        abd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new BroadbandPlansPage(accId, phoneNo, email);
            }
        });
        
        app.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new PrepaidPlansPage(accId, phoneNo, email);
            }
        });
        
        viewGrievancesMenu.addMenuListener(new javax.swing.event.MenuListener() {
            @Override
            public void menuSelected(javax.swing.event.MenuEvent e) {
                frame.dispose();
                new BroadbandComplaintsPage(accId, email);
            }

            @Override
            public void menuDeselected(javax.swing.event.MenuEvent e) {
            }

            @Override
            public void menuCanceled(javax.swing.event.MenuEvent e) {
            }
        });
        
        fileGrievanceMenu.addMenuListener(new javax.swing.event.MenuListener() {
            @Override
            public void menuSelected(javax.swing.event.MenuEvent e) {
                frame.dispose();
                new FileGrievancePage(accId, email);
            }

            @Override
            public void menuDeselected(javax.swing.event.MenuEvent e) {
            }

            @Override
            public void menuCanceled(javax.swing.event.MenuEvent e) {
            }
        });
        
        logoutMenu.addMenuListener(new javax.swing.event.MenuListener() {
            @Override
            public void menuSelected(javax.swing.event.MenuEvent e) {
                frame.dispose();
             
            }

            @Override
            public void menuDeselected(javax.swing.event.MenuEvent e) {
            }

            @Override
            public void menuCanceled(javax.swing.event.MenuEvent e) {
            }
        });

        frame.setVisible(true);
    }

    // Inner class for ImageCarousel
    class ImageCarousel extends JPanel {

        private Timer timer;
        private int currentIndex = 0;

        private String[] imagePaths = {
                "C:\\Users\\Dell\\Downloads\\OTT249.jpg",
                "C:\\Users\\Dell\\Downloads\\STV2399WEB.jpg",
                "C:\\Users\\Dell\\Downloads\\STV997WEB.jpg"
                // Add more image paths as needed
        };

        public ImageCarousel() {
            setupTimer();
        }

        private void setupTimer() {
            int delay = 2000; // Set delay between image transitions (in milliseconds)
            ActionListener taskPerformer = new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    currentIndex = (currentIndex + 1) % imagePaths.length;
                    repaint();
                }
            };
            timer = new Timer(delay, taskPerformer);
            timer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (imagePaths.length > 0) {
                ImageIcon imageIcon = new ImageIcon(imagePaths[currentIndex]);
                Image image = imageIcon.getImage();
                int x = (getWidth() - image.getWidth(null)) / 2;
                int y = (getHeight() - image.getHeight(null)) / 2;
                g.drawImage(image, x, y, this);
            }
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(900, 305); // Set preferred size for carousel
        }
    }

}
