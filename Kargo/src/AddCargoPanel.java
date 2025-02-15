import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddCargoPanel extends JPanel {
    private final JTextField cargoIdField;
    private final JTextField dateField;
    private final JComboBox<Status> statusCombo;
    private final JComboBox<String> cityCombo;
    private final CargoManager cargoManager;
    private final Client currentClient;

    public AddCargoPanel(CargoManager cargoManager, CityTree cityTree, Client currentClient) {
        this.cargoManager = cargoManager;
        this.currentClient = currentClient;
        
        setLayout(new BorderLayout(10, 10));
        setBackground(UITheme.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header Panel with animation
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UITheme.BACKGROUND_COLOR);
        JLabel titleLabel = new JLabel("Add New Cargo");
        titleLabel.setFont(UITheme.HEADER_FONT);
        titleLabel.setForeground(UITheme.PRIMARY_COLOR);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            new SoftBevelBorder(SoftBevelBorder.RAISED),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        // Cargo ID with mnemonic
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel idLabel = new JLabel("Cargo ID:");
        idLabel.setDisplayedMnemonic('C');
        UITheme.styleLabel(idLabel);
        formPanel.add(idLabel, gbc);
        
        gbc.gridx = 1;
        cargoIdField = new JTextField(15);
        cargoIdField.setToolTipText("Enter a unique numeric cargo ID");
        UITheme.styleField(cargoIdField);
        idLabel.setLabelFor(cargoIdField);
        formPanel.add(cargoIdField, gbc);
        
        // Date with mnemonic
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel dateLabel = new JLabel("Date (yyyy-MM-dd):");
        dateLabel.setDisplayedMnemonic('D');
        UITheme.styleLabel(dateLabel);
        formPanel.add(dateLabel, gbc);
        
        gbc.gridx = 1;
        dateField = new JTextField(15);
        dateField.setToolTipText("Enter date in yyyy-MM-dd format");
        UITheme.styleField(dateField);
        dateLabel.setLabelFor(dateField);
        formPanel.add(dateField, gbc);
        
        // Status with mnemonic
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setDisplayedMnemonic('S');
        UITheme.styleLabel(statusLabel);
        formPanel.add(statusLabel, gbc);
        
        gbc.gridx = 1;
        statusCombo = new JComboBox<>(Status.values());
        statusCombo.setToolTipText("Select the current status of the cargo");
        UITheme.styleComboBox(statusCombo);
        statusLabel.setLabelFor(statusCombo);
        formPanel.add(statusCombo, gbc);
        
        // City with mnemonic
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel cityLabel = new JLabel("City:");
        cityLabel.setDisplayedMnemonic('T');
        UITheme.styleLabel(cityLabel);
        formPanel.add(cityLabel, gbc);
        
        gbc.gridx = 1;
        cityCombo = new JComboBox<>(new String[]{
            "ANKARA (1)", "İSTANBUL (2)", "BURSA (3)", "İZMİR (4)", "ANTALYA (5)"
        });
        cityCombo.setToolTipText("Select the destination city");
        UITheme.styleComboBox(cityCombo);
        cityLabel.setLabelFor(cityCombo);
        formPanel.add(cityCombo, gbc);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(UITheme.BACKGROUND_COLOR);
        
        JButton clearButton = new JButton("Clear");
        JButton submitButton = new JButton("Add Cargo");
        
        clearButton.setMnemonic('L');
        submitButton.setMnemonic('A');
        
        clearButton.setToolTipText("Clear all fields (Alt+L)");
        submitButton.setToolTipText("Submit cargo details (Alt+A)");
        
        UITheme.styleButton(clearButton);
        UITheme.styleButton(submitButton);
        clearButton.setBackground(UITheme.SECONDARY_COLOR);
        
        clearButton.addActionListener(e -> clearFields());
        submitButton.addActionListener(e -> handleAddCargo());
        
        // Add Enter key support
        cargoIdField.addActionListener(e -> dateField.requestFocus());
        dateField.addActionListener(e -> statusCombo.requestFocus());
        
        // Set up focus traversal using FocusTraversalPolicy
        setFocusTraversalPolicy(new FocusTraversalPolicy() {
            public Component getComponentAfter(Container container, Component component) {
                if (component == cargoIdField) return dateField;
                if (component == dateField) return statusCombo;
                if (component == statusCombo) return cityCombo;
                if (component == cityCombo) return submitButton;
                if (component == submitButton) return clearButton;
                if (component == clearButton) return cargoIdField;
                return cargoIdField;
            }

            public Component getComponentBefore(Container container, Component component) {
                if (component == cargoIdField) return clearButton;
                if (component == dateField) return cargoIdField;
                if (component == statusCombo) return dateField;
                if (component == cityCombo) return statusCombo;
                if (component == submitButton) return cityCombo;
                if (component == clearButton) return submitButton;
                return cargoIdField;
            }

            public Component getDefaultComponent(Container container) {
                return cargoIdField;
            }

            public Component getFirstComponent(Container container) {
                return cargoIdField;
            }

            public Component getLastComponent(Container container) {
                return clearButton;
            }
        });
        setFocusCycleRoot(true);

        buttonPanel.add(clearButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(submitButton);

        // Add all panels
        add(headerPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Set initial focus
        SwingUtilities.invokeLater(() -> cargoIdField.requestFocusInWindow());
    }
    
    private void handleAddCargo() {
        try {
            // Validate and parse input
            int cargoId = Integer.parseInt(cargoIdField.getText().trim());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(dateField.getText().trim());
            Status status = (Status) statusCombo.getSelectedItem();
            
            String citySelection = (String) cityCombo.getSelectedItem();
            int cityId = Integer.parseInt(citySelection.substring(
                citySelection.indexOf("(") + 1, 
                citySelection.indexOf(")")
            ));
            
            // Create and add cargo
            Cargo newCargo = new Cargo(cargoId, date, 0, status, currentClient, cityId);
            cargoManager.addCargo(newCargo, cityId, currentClient);
            
            // Show success message
            showSuccessDialog(newCargo.getDeliveryTime());
            
            // Clear fields
            clearFields();
            
        } catch (NumberFormatException e) {
            showErrorDialog("Please enter valid numeric values");
        } catch (ParseException e) {
            showErrorDialog("Please enter date in yyyy-MM-dd format");
        }
    }
    
    private void showSuccessDialog(int deliveryTime) {
        JOptionPane.showMessageDialog(this,
            "Cargo added successfully!\nEstimated delivery time: " + deliveryTime + " days",
            "Success",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this,
            message,
            "Input Error",
            JOptionPane.ERROR_MESSAGE
        );
    }
    
    private void clearFields() {
        cargoIdField.setText("");
        dateField.setText("");
        statusCombo.setSelectedIndex(0);
        cityCombo.setSelectedIndex(0);
    }
}