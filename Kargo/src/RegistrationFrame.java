import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.Map;

public class RegistrationFrame extends JFrame {
    private final JTextField nameField;
    private final JTextField surnameField;
    private final JTextField userIdField;
    private final Map<Integer, Client> userDatabase;
    
    public RegistrationFrame(Map<Integer, Client> userDatabase) {
        this.userDatabase = userDatabase;
        setTitle("Cargo Management System - Registration");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 450);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Main panel with background
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(UITheme.BACKGROUND_COLOR);
        
        // Gradient header panel
        mainPanel.add(new GradientHeaderPanel("New User Registration"), BorderLayout.NORTH);
        
        // Registration form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            new SoftBevelBorder(SoftBevelBorder.RAISED),
            BorderFactory.createEmptyBorder(25, 30, 25, 30)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        // Name Field with mnemonic
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel nameLabel = new JLabel("First Name:");
        nameLabel.setDisplayedMnemonic('F');
        UITheme.styleLabel(nameLabel);
        formPanel.add(nameLabel, gbc);
        
        gbc.gridx = 1;
        nameField = new JTextField(20);
        nameField.setToolTipText("Enter your first name");
        UITheme.styleField(nameField);
        nameLabel.setLabelFor(nameField);
        formPanel.add(nameField, gbc);
        
        // Surname Field with mnemonic
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel surnameLabel = new JLabel("Last Name:");
        surnameLabel.setDisplayedMnemonic('L');
        UITheme.styleLabel(surnameLabel);
        formPanel.add(surnameLabel, gbc);
        
        gbc.gridx = 1;
        surnameField = new JTextField(20);
        surnameField.setToolTipText("Enter your last name");
        UITheme.styleField(surnameField);
        surnameLabel.setLabelFor(surnameField);
        formPanel.add(surnameField, gbc);
        
        // User ID Field with mnemonic
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel userIdLabel = new JLabel("User ID:");
        userIdLabel.setDisplayedMnemonic('U');
        UITheme.styleLabel(userIdLabel);
        formPanel.add(userIdLabel, gbc);
        
        gbc.gridx = 1;
        userIdField = new JTextField(20);
        userIdField.setToolTipText("Enter a unique numeric ID for login");
        UITheme.styleField(userIdField);
        userIdLabel.setLabelFor(userIdField);
        formPanel.add(userIdField, gbc);
        
        // Help text
        gbc.gridx = 1; gbc.gridy = 3;
        JLabel helpLabel = new JLabel("Enter a numeric ID for login");
        helpLabel.setFont(UITheme.SMALL_FONT);
        helpLabel.setForeground(UITheme.SECONDARY_COLOR);
        formPanel.add(helpLabel, gbc);
        
        // Buttons Panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonsPanel.setBackground(Color.WHITE);
        
        JButton registerButton = new JButton("Register");
        JButton backButton = new JButton("Back to Login");
        
        // Add mnemonics
        registerButton.setMnemonic('R');
        backButton.setMnemonic('B');
        
        // Add tooltips
        registerButton.setToolTipText("Create your account (Alt+R)");
        backButton.setToolTipText("Return to login screen (Alt+B)");
        
        UITheme.styleButton(registerButton);
        UITheme.styleButton(backButton);
        backButton.setBackground(UITheme.SECONDARY_COLOR);
        
        registerButton.setPreferredSize(new Dimension(120, 35));
        backButton.setPreferredSize(new Dimension(120, 35));
        
        registerButton.addActionListener((_ignored) -> handleRegistration());
        backButton.addActionListener((_ignored) -> returnToLogin());
        
        // Add Enter key support
        nameField.addActionListener((_ignored) -> surnameField.requestFocus());
        surnameField.addActionListener((_ignored) -> userIdField.requestFocus());
        userIdField.addActionListener((_ignored) -> handleRegistration());
        
        buttonsPanel.add(registerButton);
        buttonsPanel.add(backButton);
        
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        formPanel.add(buttonsPanel, gbc);
        
        // Set up keyboard focus traversal
        setFocusTraversalPolicy(new LayoutFocusTraversalPolicy());
        nameField.setNextFocusableComponent(surnameField);
        surnameField.setNextFocusableComponent(userIdField);
        userIdField.setNextFocusableComponent(registerButton);
        registerButton.setNextFocusableComponent(backButton);
        backButton.setNextFocusableComponent(nameField);
        
        // Wrap form panel in a container with margins
        JPanel formContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        formContainer.setBackground(UITheme.BACKGROUND_COLOR);
        formContainer.add(formPanel);
        
        mainPanel.add(formContainer, BorderLayout.CENTER);
        add(mainPanel);
        
        // Set initial focus to nameField
        SwingUtilities.invokeLater(() -> nameField.requestFocusInWindow());
    }
    
    private void handleRegistration() {
        String name = nameField.getText().trim();
        String surname = surnameField.getText().trim();
        
        if (name.isEmpty() || surname.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please fill in all fields",
                "Registration Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            int userId = Integer.parseInt(userIdField.getText().trim());
            
            if (userDatabase.containsKey(userId)) {
                JOptionPane.showMessageDialog(this,
                    "This User ID already exists. Please choose another.",
                    "Registration Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Client newClient = new Client(userId, name, surname);
            userDatabase.put(userId, newClient);
            
            JOptionPane.showMessageDialog(this,
                "Registration successful!\nYour User ID: " + userId,
                "Registration Success",
                JOptionPane.INFORMATION_MESSAGE);
                
            returnToLogin();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Please enter a valid numeric ID",
                "Input Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void returnToLogin() {
        LoginFrame loginFrame = new LoginFrame(userDatabase);
        loginFrame.setVisible(true);
        this.dispose();
    }
}