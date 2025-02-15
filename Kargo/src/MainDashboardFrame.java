import javax.swing.*;
import javax.swing.border.*;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainDashboardFrame extends JFrame {
    private final Client currentClient;
    private final Map<Integer, Client> userDatabase;
    private final CardLayout cardLayout;
    private final JPanel contentPanel;
    private final CargoManager cargoManager;
    private final CityTree cityTree;
    private JLabel statusLabel;
    private List<Component> focusOrder;
    
    public MainDashboardFrame(Client client, Map<Integer, Client> userDatabase) {
        this.currentClient = client;
        this.userDatabase = userDatabase;
        
        // Initialize backend components
        CityNode central = new CityNode("ANKARA", 1);
        CityNode cityA = new CityNode("İSTANBUL", 2);
        CityNode cityB = new CityNode("BURSA", 3);
        CityNode cityC = new CityNode("İZMİR", 4);
        CityNode cityD = new CityNode("ANTALYA", 5);

        central.addChild(cityA);
        central.addChild(cityB);
        cityA.addChild(cityC);
        cityB.addChild(cityD);

        this.cityTree = new CityTree(central);
        this.cargoManager = new CargoManager(cityTree);
        
        // Setup frame
        setTitle("Cargo Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1000, 600));

        // Initialize focus order list
        focusOrder = new ArrayList<>();
        
        // Create main container with focus policy
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(UITheme.BACKGROUND_COLOR);

        // Create header with gradient
        JPanel headerPanel = new GradientHeaderPanel("Welcome, " + client.getName() + " " + client.getSurname());
        
        // Add status label to header
        statusLabel = new JLabel("");
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setFont(UITheme.SMALL_FONT);
        headerPanel.add(statusLabel, BorderLayout.EAST);

        // Create side navigation panel
        JPanel sideNavPanel = createSideNavPanel();

        // Create main content panel with CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(UITheme.BACKGROUND_COLOR);
        
        // Create panels and store focusable components
        AddCargoPanel addCargoPanel = new AddCargoPanel(cargoManager, cityTree, currentClient);
        CargoListPanel cargoListPanel = new CargoListPanel(currentClient);
        SearchCargoPanel searchCargoPanel = new SearchCargoPanel(currentClient, cargoManager);
        CityTreePanel cityTreePanel = new CityTreePanel(cityTree);
        
        contentPanel.add(createDashboardPanel(), "DASHBOARD");
        contentPanel.add(addCargoPanel, "ADD_CARGO");
        contentPanel.add(cargoListPanel, "CARGO_LIST");
        contentPanel.add(searchCargoPanel, "SEARCH_CARGO");
        contentPanel.add(cityTreePanel, "CITY_TREE");

        // Create content wrapper with shadow border
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBackground(UITheme.BACKGROUND_COLOR);
        contentWrapper.setBorder(BorderFactory.createCompoundBorder(
            new SoftBevelBorder(SoftBevelBorder.RAISED),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        contentWrapper.add(contentPanel, BorderLayout.CENTER);

        // Add components to main container
        mainContainer.add(headerPanel, BorderLayout.NORTH);
        mainContainer.add(sideNavPanel, BorderLayout.WEST);
        mainContainer.add(contentWrapper, BorderLayout.CENTER);

        // Set up focus traversal for the entire frame
        setFocusTraversalPolicy(new LayoutFocusTraversalPolicy() {
            @Override
            public Component getComponentAfter(Container container, Component component) {
                int idx = focusOrder.indexOf(component);
                if (idx < 0 || idx >= focusOrder.size() - 1) {
                    return focusOrder.get(0);
                }
                return focusOrder.get(idx + 1);
            }
            
            @Override
            public Component getComponentBefore(Container container, Component component) {
                int idx = focusOrder.indexOf(component);
                if (idx <= 0) {
                    return focusOrder.get(focusOrder.size() - 1);
                }
                return focusOrder.get(idx - 1);
            }
            
            @Override
            public Component getFirstComponent(Container container) {
                return focusOrder.isEmpty() ? null : focusOrder.get(0);
            }
            
            @Override
            public Component getLastComponent(Container container) {
                return focusOrder.isEmpty() ? null : focusOrder.get(focusOrder.size() - 1);
            }
            
            @Override
            public Component getDefaultComponent(Container container) {
                return getFirstComponent(container);
            }
        });
        setFocusCycleRoot(true);
        
        add(mainContainer);

        // Add keyboard navigation
        setupKeyboardShortcuts();
        setupInitialFocus();
    }

    private JPanel createSideNavPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setPreferredSize(new Dimension(220, 0));
        panel.setBackground(new Color(51, 51, 51));
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, UITheme.PRIMARY_COLOR));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);

        // Add navigation buttons
        addNavButton(panel, gbc, "Dashboard", "DASHBOARD", 'H', KeyEvent.VK_H);
        addNavButton(panel, gbc, "Add Cargo", "ADD_CARGO", 'A', KeyEvent.VK_A);
        addNavButton(panel, gbc, "View Shipments", "CARGO_LIST", 'V', KeyEvent.VK_V);
        addNavButton(panel, gbc, "Search Cargo", "SEARCH_CARGO", 'S', KeyEvent.VK_S);
        addNavButton(panel, gbc, "City Tree", "CITY_TREE", 'T', KeyEvent.VK_T);

        // Add logout at bottom
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.SOUTH;
        JButton logoutButton = createNavButton("Logout", 'L');
        logoutButton.addActionListener(_ -> logout());
        panel.add(logoutButton, gbc);

        return panel;
    }

    private JButton createNavButton(String text, char mnemonic) {
        JButton button = new JButton(text);
        button.setFont(UITheme.REGULAR_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(51, 51, 51));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setMnemonic(mnemonic);
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(UITheme.PRIMARY_COLOR);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(51, 51, 51));
            }
        });
        
        return button;
    }

    private void addNavButton(JPanel panel, GridBagConstraints gbc, String text, String cardName, 
            char mnemonic, int keyCode) {
        JButton button = createNavButton(text, mnemonic);
        button.addActionListener(_ -> switchCard(cardName));
        panel.add(button, gbc);
    }

    private void switchCard(String cardName) {
        cardLayout.show(contentPanel, cardName);
        showStatus("Switched to " + cardName.replace('_', ' ').toLowerCase());
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(UITheme.BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        // Create dashboard cards with animations
        addDashboardCard(panel, gbc, "Add New Cargo", "ADD_CARGO", 0, 0);
        addDashboardCard(panel, gbc, "View Shipments", "CARGO_LIST", 0, 1);
        addDashboardCard(panel, gbc, "Search Cargo", "SEARCH_CARGO", 1, 0);
        addDashboardCard(panel, gbc, "View City Tree", "CITY_TREE", 1, 1);

        return panel;
    }

    private void addDashboardCard(JPanel panel, GridBagConstraints gbc, String title, String cardName,
            int x, int y) {
        JPanel card = new DashboardCard(title, _ -> switchCard(cardName));
        gbc.gridx = x;
        gbc.gridy = y;
        panel.add(card, gbc);
    }

    private void setupKeyboardShortcuts() {
        JRootPane rootPane = getRootPane();
        InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = rootPane.getActionMap();

        // Add keyboard shortcuts (Alt + key)
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.ALT_DOWN_MASK), "dashboard");
        actionMap.put("dashboard", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchCard("DASHBOARD");
            }
        });

        // Add more shortcuts for other actions...

        // Add F6 to cycle between panels
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0), "cyclePanel");
        getRootPane().getActionMap().put("cyclePanel", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout layout = (CardLayout) contentPanel.getLayout();
                layout.next(contentPanel);
            }
        });
    }

    private void setupInitialFocus() {
        SwingUtilities.invokeLater(() -> {
            Component first = getFocusTraversalPolicy().getFirstComponent(this);
            if (first != null) {
                first.requestFocusInWindow();
            }
        });
    }

    private void showStatus(String message) {
        statusLabel.setText(message);
        Timer timer = new Timer(2000, _ -> statusLabel.setText(""));
        timer.setRepeats(false);
        timer.start();
    }

    private void logout() {
        int choice = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
            
        if (choice == JOptionPane.YES_OPTION) {
            LoginFrame loginFrame = new LoginFrame(userDatabase);
            loginFrame.setVisible(true);
            this.dispose();
        }
    }
}

// Custom dashboard card with animations
class DashboardCard extends JPanel {
    private Color backgroundColor = Color.WHITE;
    private final Timer animationTimer;
    private float animationProgress = 0.0f;
    private boolean isHovered = false;

    public DashboardCard(String title, ActionListener listener) {
        setLayout(new BorderLayout());
        setBackground(backgroundColor);
        setBorder(BorderFactory.createCompoundBorder(
            new SoftBevelBorder(SoftBevelBorder.RAISED),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(UITheme.HEADER_FONT);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.CENTER);

        setCursor(new Cursor(Cursor.HAND_CURSOR));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "click"));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                startAnimation();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                startAnimation();
            }
        });

        animationTimer = new Timer(20, _ -> updateAnimation());
    }

    private void startAnimation() {
        animationTimer.stop();
        animationTimer.start();
    }

    private void updateAnimation() {
        if (isHovered && animationProgress < 1.0f) {
            animationProgress += 0.1f;
        } else if (!isHovered && animationProgress > 0.0f) {
            animationProgress -= 0.1f;
        }

        if (animationProgress <= 0.0f || animationProgress >= 1.0f) {
            animationTimer.stop();
        }

        backgroundColor = interpolateColor(
            Color.WHITE,
            UITheme.BACKGROUND_COLOR,
            animationProgress
        );
        repaint();
    }

    private Color interpolateColor(Color c1, Color c2, float fraction) {
        fraction = Math.min(1.0f, Math.max(0.0f, fraction)); // Clamp fraction between 0 and 1
        int red = clamp((int) (c1.getRed() + fraction * (c2.getRed() - c1.getRed())));
        int green = clamp((int) (c1.getGreen() + fraction * (c2.getGreen() - c1.getGreen())));
        int blue = clamp((int) (c1.getBlue() + fraction * (c2.getBlue() - c1.getBlue())));
        return new Color(red, green, blue);
    }
    
    private int clamp(int value) {
        return Math.min(255, Math.max(0, value));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(backgroundColor);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }
}