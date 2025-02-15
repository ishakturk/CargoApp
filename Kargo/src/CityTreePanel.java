import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;

public class CityTreePanel extends JPanel {
    private final CityTree cityTree;
    private final JTree tree;
    private final JLabel statusLabel;
    private Timer statusTimer;
    
    public CityTreePanel(CityTree cityTree) {
        this.cityTree = cityTree;
        setLayout(new BorderLayout(10, 10));
        setBackground(UITheme.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header Panel with status
        JPanel headerPanel = new JPanel(new BorderLayout(10, 0));
        headerPanel.setBackground(UITheme.BACKGROUND_COLOR);
        
        JLabel titleLabel = new JLabel("City Distribution Network");
        titleLabel.setFont(UITheme.HEADER_FONT);
        titleLabel.setForeground(UITheme.PRIMARY_COLOR);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        statusLabel = new JLabel("");
        statusLabel.setFont(UITheme.SMALL_FONT);
        statusLabel.setForeground(UITheme.SECONDARY_COLOR);
        headerPanel.add(statusLabel, BorderLayout.EAST);
        
        // Create and style tree
        DefaultMutableTreeNode root = createTreeNode(cityTree.getRoot());
        tree = new JTree(root);
        UITheme.styleTree(tree);
        
        // Custom tree renderer with icons and colors
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer() {
            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value,
                    boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
                
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                String nodeText = node.toString();
                
                if (nodeText.contains("Active Shipments")) {
                    setForeground(UITheme.PRIMARY_COLOR);
                    setIcon(createCircleIcon(10, UITheme.PRIMARY_COLOR));
                } else {
                    setForeground(UITheme.TEXT_COLOR);
                    setIcon(createCircleIcon(8, UITheme.SECONDARY_COLOR));
                }
                
                if (sel) {
                    setBackgroundSelectionColor(UITheme.SECONDARY_COLOR);
                    setTextSelectionColor(Color.WHITE);
                }
                
                setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));
                return this;
            }
        };
        
        tree.setCellRenderer(renderer);
        tree.setRowHeight(30);
        tree.setShowsRootHandles(true);
        
        // Add tree to scroll pane with styling
        JScrollPane scrollPane = new JScrollPane(tree);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(UITheme.PRIMARY_COLOR));
        
        // Control panel with buttons
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        controlPanel.setBackground(UITheme.BACKGROUND_COLOR);
        
        JButton expandAllButton = new JButton("Expand All");
        JButton collapseAllButton = new JButton("Collapse All");
        JButton refreshButton = new JButton("Refresh Tree");
        
        expandAllButton.setMnemonic('E');
        collapseAllButton.setMnemonic('C');
        refreshButton.setMnemonic('R');
        
        expandAllButton.setToolTipText("Expand all nodes (Alt+E)");
        collapseAllButton.setToolTipText("Collapse all nodes (Alt+C)");
        refreshButton.setToolTipText("Refresh tree data (Alt+R)");
        
        UITheme.styleButton(expandAllButton);
        UITheme.styleButton(collapseAllButton);
        UITheme.styleButton(refreshButton);
        
        expandAllButton.setBackground(UITheme.SECONDARY_COLOR);
        collapseAllButton.setBackground(UITheme.SECONDARY_COLOR);
        
        expandAllButton.addActionListener(action -> expandAllNodes());
        collapseAllButton.addActionListener(action -> collapseAllNodes());
        refreshButton.addActionListener(action -> refreshTree());
        
        // Add keyboard shortcuts
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();
        
        // F5 to refresh
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), "refresh");
        actionMap.put("refresh", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshTree();
            }
        });
        
        controlPanel.add(expandAllButton);
        controlPanel.add(collapseAllButton);
        controlPanel.add(Box.createHorizontalStrut(20));
        controlPanel.add(refreshButton);
        
        // Add components
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
        
        // Initial expansion
        expandAllNodes();
    }

    @Override
    public void removeNotify() {
        super.removeNotify();
        if (statusTimer != null) {
            statusTimer.stop();
        }
    }
    
    private Icon createCircleIcon(int size, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(color);
                g2d.fillOval(x, y, size, size);
                g2d.dispose();
            }
            
            @Override
            public int getIconWidth() {
                return size;
            }
            
            @Override
            public int getIconHeight() {
                return size;
            }
        };
    }
    
    private DefaultMutableTreeNode createTreeNode(CityNode cityNode) {
        StringBuilder nodeText = new StringBuilder();
        nodeText.append(cityNode.getCityName())
                .append(" (ID: ")
                .append(cityNode.getCityId())
                .append(")");
        
        if (!cityNode.getCargos().isEmpty()) {
            nodeText.append(" - Active Shipments: ")
                   .append(cityNode.getCargos().size());
        }
        
        DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(nodeText.toString());
        
        for (CityNode child : cityNode.getChildren()) {
            treeNode.add(createTreeNode(child));
        }
        
        return treeNode;
    }
    
    private void expandAllNodes() {
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
        showStatus("Tree expanded");
    }
    
    private void collapseAllNodes() {
        for (int i = tree.getRowCount() - 1; i >= 0; i--) {
            tree.collapseRow(i);
        }
        tree.expandRow(0); // Keep root visible
        showStatus("Tree collapsed");
    }
    
    private void refreshTree() {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                showStatus("Refreshing tree data...");
                DefaultMutableTreeNode root = createTreeNode(cityTree.getRoot());
                ((DefaultTreeModel) tree.getModel()).setRoot(root);
                return null;
            }
            
            @Override
            protected void done() {
                expandAllNodes();
                showStatus("Tree updated");
            }
        };
        worker.execute();
    }
    
    private void showStatus(String message) {
        statusLabel.setText(message);
        if (statusTimer != null) {
            statusTimer.stop();
        }
        statusTimer = new Timer(2000, action -> statusLabel.setText(""));
        statusTimer.setRepeats(false);
        statusTimer.start();
    }
}