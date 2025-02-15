package ui;

import model.Client;

import javax.swing.*;
import javax.swing.border.*;

import java.awt.*;
import java.util.Map;

public class LoginFrame extends JFrame {
    private final JTextField userIdField;
    private final Map<Integer, Client> userDatabase;
    
    public LoginFrame(Map<Integer, Client> userDatabase) {
        this.userDatabase = userDatabase;
        setTitle("Cargo Management System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Main panel with background
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(UITheme.BACKGROUND_COLOR);
        
        // Gradient header panel
        mainPanel.add(new GradientHeaderPanel("Cargo Management System"), BorderLayout.NORTH);
        
        // Login panel
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(Color.WHITE);
        loginPanel.setBorder(BorderFactory.createCompoundBorder(
            new SoftBevelBorder(SoftBevelBorder.RAISED),
            BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // User ID Field
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel userLabel = new JLabel("User ID:");
        userLabel.setDisplayedMnemonic('U'); // Alt+U keyboard shortcut
        UITheme.styleLabel(userLabel);
        loginPanel.add(userLabel, gbc);
        
        gbc.gridx = 1;
        userIdField = new JTextField(15);
        userIdField.setToolTipText("Enter your numeric user ID");
        UITheme.styleField(userIdField);
        userLabel.setLabelFor(userIdField); // Connect label to field for keyboard navigation
        loginPanel.add(userIdField, gbc);
        
        // Buttons Panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonsPanel.setBackground(Color.WHITE);
        
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        
        // Add keyboard shortcuts
        loginButton.setMnemonic('L'); // Alt+L for login
        registerButton.setMnemonic('R'); // Alt+R for register
        
        // Add tooltips
        loginButton.setToolTipText("Log in with your user ID (Alt+L)");
        registerButton.setToolTipText("Create a new account (Alt+R)");
        
        UITheme.styleButton(loginButton);
        UITheme.styleButton(registerButton);
        registerButton.setBackground(UITheme.SECONDARY_COLOR);
        
        loginButton.setPreferredSize(new Dimension(120, 35));
        registerButton.setPreferredSize(new Dimension(120, 35));
        
        // Add Enter key support for login
        loginButton.addActionListener((_ignored) -> handleLogin());
        registerButton.addActionListener((_ignored) -> showRegistrationFrame());
        
        // Add Enter key support to userIdField
        userIdField.addActionListener((_ignored) -> handleLogin());
        
        buttonsPanel.add(loginButton);
        buttonsPanel.add(registerButton);
        
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 2;
        loginPanel.add(buttonsPanel, gbc);
        
        // Add keyboard focus traversal
        setFocusTraversalPolicy(new LayoutFocusTraversalPolicy());
        userIdField.setNextFocusableComponent(loginButton);
        loginButton.setNextFocusableComponent(registerButton);
        registerButton.setNextFocusableComponent(userIdField);
        
        // Wrap login panel in a container with margins
        JPanel loginContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        loginContainer.setBackground(UITheme.BACKGROUND_COLOR);
        loginContainer.add(loginPanel);
        
        mainPanel.add(loginContainer, BorderLayout.CENTER);
        add(mainPanel);
        
        // Set initial focus to userIdField
        SwingUtilities.invokeLater(() -> userIdField.requestFocusInWindow());
    }
    
    private void handleLogin() {
        try {
            int userId = Integer.parseInt(userIdField.getText().trim());
            if (userDatabase.containsKey(userId)) {
                Client client = userDatabase.get(userId);
                MainDashboardFrame dashboard = new MainDashboardFrame(client, userDatabase);
                dashboard.setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Invalid User ID", 
                    "Login Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a valid numeric ID", 
                "Input Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void showRegistrationFrame() {
        RegistrationFrame registrationFrame = new RegistrationFrame(userDatabase);
        registrationFrame.setVisible(true);
        this.dispose();
    }
}