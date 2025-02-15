package ui;

import model.Cargo;
import model.Client;
import service.CargoManager;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;

public class SearchCargoPanel extends JPanel {
    private final Client client;
    private final CargoManager cargoManager;
    private final JTextField searchField;
    private final JTextArea resultArea;
    private final JLabel statusLabel;
    private Timer fadeTimer;
    
    public SearchCargoPanel(Client client, CargoManager cargoManager) {
        this.client = client;
        this.cargoManager = cargoManager;
        setLayout(new BorderLayout(10, 10));
        setBackground(UITheme.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header Panel with status
        JPanel headerPanel = new JPanel(new BorderLayout(10, 0));
        headerPanel.setBackground(UITheme.BACKGROUND_COLOR);
        
        JLabel titleLabel = new JLabel("Search Cargo");
        titleLabel.setFont(UITheme.HEADER_FONT);
        titleLabel.setForeground(UITheme.PRIMARY_COLOR);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        statusLabel = new JLabel("");
        statusLabel.setFont(UITheme.SMALL_FONT);
        statusLabel.setForeground(UITheme.SECONDARY_COLOR);
        headerPanel.add(statusLabel, BorderLayout.EAST);
        
        // Search Panel with validation
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.PRIMARY_COLOR),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel searchLabel = new JLabel("Cargo ID:");
        searchLabel.setDisplayedMnemonic('I');
        UITheme.styleLabel(searchLabel);
        
        searchField = new JTextField(15);
        searchField.setToolTipText("Enter cargo ID to search");
        UITheme.styleField(searchField);
        searchLabel.setLabelFor(searchField);
        
        // Add input validation
        searchField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) {
                    e.consume();
                    showStatus("Please enter numbers only", true);
                }
            }
        });
        
        JButton searchButton = new JButton("Search");
        searchButton.setMnemonic('S');
        searchButton.setToolTipText("Search for cargo (Alt+S)");
        UITheme.styleButton(searchButton);
        searchButton.addActionListener(action -> performSearch());
        
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        
        // Results Panel with animation
        JPanel resultsPanel = new JPanel(new BorderLayout(5, 5));
        resultsPanel.setBackground(UITheme.BACKGROUND_COLOR);
        
        JLabel resultsLabel = new JLabel("Search Results");
        resultsLabel.setFont(UITheme.REGULAR_FONT);
        resultsPanel.add(resultsLabel, BorderLayout.NORTH);
        
        resultArea = new JTextArea(10, 40);
        resultArea.setFont(UITheme.REGULAR_FONT);
        resultArea.setEditable(false);
        resultArea.setMargin(new Insets(10, 10, 10, 10));
        resultArea.setBackground(Color.WHITE);
        resultArea.setBorder(null);
        
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(UITheme.PRIMARY_COLOR));
        resultsPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Layout
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBackground(UITheme.BACKGROUND_COLOR);
        contentPanel.add(searchPanel, BorderLayout.NORTH);
        contentPanel.add(resultsPanel, BorderLayout.CENTER);
        
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        
        // Add keyboard shortcuts
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();
        
        // Enter key in search field triggers search
        searchField.addActionListener(action -> performSearch());
        
        // Ctrl+F focuses search field
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK), "focusSearch");
        actionMap.put("focusSearch", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchField.requestFocusInWindow();
                searchField.selectAll();
            }
        });
        
        // Initialize fade timer for status messages
        fadeTimer = new Timer(2000, action -> clearStatus());
        fadeTimer.setRepeats(false);
        
        // Set initial focus
        SwingUtilities.invokeLater(() -> searchField.requestFocusInWindow());
    }
    
    private void performSearch() {
        try {
            String searchText = searchField.getText().trim();
            if (searchText.isEmpty()) {
                showStatus("Please enter a cargo ID", true);
                return;
            }
            
            int cargoId = Integer.parseInt(searchText);
            Cargo cargo = cargoManager.binarySearchById(client.getCargoHistory(), cargoId);
            
            if (cargo != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                StringBuilder result = new StringBuilder();
                result.append("Cargo Details:\n\n");
                result.append("ID: ").append(cargo.getId()).append("\n");
                result.append("Date: ").append(dateFormat.format(cargo.getDate())).append("\n");
                result.append("Status: ").append(cargo.getStatus()).append("\n");
                result.append("City ID: ").append(cargo.getCityId()).append("\n");
                result.append("Delivery Time: ").append(cargo.getDeliveryTime()).append(" days\n");
                
                resultArea.setText(result.toString());
                showStatus("Cargo found", false);
            } else {
                resultArea.setText("No cargo found with ID: " + cargoId);
                showStatus("No results found", true);
            }
        } catch (NumberFormatException e) {
            resultArea.setText("Please enter a valid numeric ID");
            showStatus("Invalid input", true);
        }
    }
    
    private void showStatus(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.setForeground(isError ? UITheme.ERROR_COLOR : UITheme.SUCCESS_COLOR);
        fadeTimer.restart();
    }
    
    private void clearStatus() {
        statusLabel.setText("");
    }
}