package com.shopsphere.desktop.view;

import com.shopsphere.model.User;
import com.shopsphere.service.UserService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Optional;

/**
 * Swing View: Login window for Desktop Administrators.
 */
public class LoginFrame extends JFrame {

    private final JTextField emailField;
    private final JPasswordField passwordField;
    private final JButton btnLogin;
    private final JButton btnDemoFill;
    private final JLabel lblStatus;
    private final UserService userService;

    public LoginFrame() {
        this.userService = new UserService();

        setTitle("ShopSphere Desktop Management - Admin Sign In");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(480, 360);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main Layout
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(25, 30, 25, 30));
        mainPanel.setBackground(new Color(248, 250, 252));

        // Header
        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 4, 4));
        headerPanel.setOpaque(false);
        JLabel titleLabel = new JLabel("ShopSphere Desktop Admin", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(15, 23, 42));

        JLabel subLabel = new JLabel("Enterprise Management & Inventory Suite", SwingConstants.CENTER);
        subLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subLabel.setForeground(new Color(100, 116, 139));

        headerPanel.add(titleLabel);
        headerPanel.add(subLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Center Form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);

        // Email
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        formPanel.add(new JLabel("Administrator Email:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.7;
        emailField = new JTextField("admin@shopsphere.com", 20);
        formPanel.add(emailField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.7;
        passwordField = new JPasswordField("Password@123", 20);
        formPanel.add(passwordField, gbc);

        // Status Label
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        lblStatus = new JLabel(" ", SwingConstants.CENTER);
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblStatus.setForeground(new Color(239, 68, 68));
        formPanel.add(lblStatus, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // South Button Bar
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        buttonPanel.setOpaque(false);

        btnLogin = new JButton("Sign In to Desktop");
        btnLogin.setBackground(new Color(79, 70, 229));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLogin.setFocusPainted(false);

        btnDemoFill = new JButton("Use Demo Admin");
        btnDemoFill.setBackground(new Color(241, 245, 249));
        btnDemoFill.setFocusPainted(false);

        buttonPanel.add(btnLogin);
        buttonPanel.add(btnDemoFill);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Event Listeners
        btnLogin.addActionListener(e -> executeLogin());
        btnDemoFill.addActionListener(e -> {
            emailField.setText("admin@shopsphere.com");
            passwordField.setText("Password@123");
            lblStatus.setText("Demo credentials filled.");
            lblStatus.setForeground(new Color(16, 185, 129));
        });

        getRootPane().setDefaultButton(btnLogin);
    }

    private void executeLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            lblStatus.setText("Email and password are required.");
            lblStatus.setForeground(new Color(239, 68, 68));
            return;
        }

        btnLogin.setEnabled(false);
        lblStatus.setText("Verifying credentials against database...");
        lblStatus.setForeground(new Color(79, 70, 229));

        // Execute authentication on background thread via SwingWorker
        new SwingWorker<Optional<User>, Void>() {
            @Override
            protected Optional<User> doInBackground() throws Exception {
                return userService.authenticate(email, password);
            }

            @Override
            protected void done() {
                btnLogin.setEnabled(true);
                try {
                    Optional<User> userOpt = get();
                    if (userOpt.isPresent()) {
                        User user = userOpt.get();
                        if (user.isAdmin()) {
                            dispose();
                            AdminDashboardFrame dashboard = new AdminDashboardFrame(user);
                            dashboard.setVisible(true);
                        } else {
                            lblStatus.setText("Access denied: User does not possess ADMIN role.");
                            lblStatus.setForeground(new Color(239, 68, 68));
                        }
                    } else {
                        lblStatus.setText("Invalid credentials or user account is suspended.");
                        lblStatus.setForeground(new Color(239, 68, 68));
                    }
                } catch (Exception ex) {
                    lblStatus.setText("Database error: " + ex.getCause().getMessage());
                    lblStatus.setForeground(new Color(239, 68, 68));
                }
            }
        }.execute();
    }
}
