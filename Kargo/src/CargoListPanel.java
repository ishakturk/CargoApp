import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.Stack;

public class CargoListPanel extends JPanel {
    private final Client client;
    private final JTable cargoTable;
    private final DefaultTableModel tableModel;
    private final JLabel statusLabel;
    private Timer visibilityTimer;

    public CargoListPanel(Client client) {
        this.client = client;
        setLayout(new BorderLayout(10, 10));
        setBackground(UITheme.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header panel with status
        JPanel headerPanel = new JPanel(new BorderLayout(10, 0));
        headerPanel.setBackground(UITheme.BACKGROUND_COLOR);
        
        JLabel titleLabel = new JLabel("Recent Shipments");
        titleLabel.setFont(UITheme.HEADER_FONT);
        titleLabel.setForeground(UITheme.PRIMARY_COLOR);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        statusLabel = new JLabel("Loading...");
        statusLabel.setFont(UITheme.SMALL_FONT);
        statusLabel.setForeground(UITheme.SECONDARY_COLOR);
        statusLabel.setVisible(false);
        headerPanel.add(statusLabel, BorderLayout.EAST);

        // Create table with custom renderer
        String[] columns = {"Cargo ID", "Date", "Status", "City ID", "Delivery Time"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
            return false;
            }
            
            @Override
            public Class<?> getColumnClass(int column) {
            if (column == 0 || column == 3 || column == 4) {
                return Integer.class;
            }
            return super.getColumnClass(column);
            }
        };

        cargoTable = new JTable(tableModel);
        UITheme.styleTable(cargoTable);

        // Set column header color
        JTableHeader header = cargoTable.getTableHeader();
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            label.setForeground(Color.BLACK);
            return label;
            }
        });
        
        // Custom renderers for different columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.CENTER);
                if (!isSelected) {
                    setForeground(UITheme.TEXT_COLOR);
                    setBackground(Color.WHITE);
                }
                return c;
            }
        };
        
        // Status column renderer
        DefaultTableCellRenderer statusRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.CENTER);
                
                if (!isSelected) {
                    Status status = (Status) value;
                    setBackground(Color.WHITE);
                    if (status == Status.DELIVERED) {
                        setForeground(UITheme.SUCCESS_COLOR);
                    } else if (status == Status.ONDELIVERY) {
                        setForeground(UITheme.PRIMARY_COLOR);
                    } else {
                        setForeground(UITheme.ERROR_COLOR);
                    }
                }
                return c;
            }
        };

        // Apply the renderers to columns
        for (int i = 0; i < cargoTable.getColumnCount(); i++) {
            TableColumn column = cargoTable.getColumnModel().getColumn(i);
            if (i == 2) { // Status column
                column.setCellRenderer(statusRenderer);
            } else {
                column.setCellRenderer(centerRenderer);
            }
        }

        // Set column widths
        TableColumnModel columnModel = cargoTable.getColumnModel();
        int[] columnWidths = {80, 100, 100, 80, 100};
        for (int i = 0; i < columnWidths.length; i++) {
            columnModel.getColumn(i).setPreferredWidth(columnWidths[i]);
        }

        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(cargoTable);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(UITheme.PRIMARY_COLOR));

        // Button panel with keyboard shortcuts
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(UITheme.BACKGROUND_COLOR);
        
        JButton refreshButton = new JButton("Refresh List");
        refreshButton.setMnemonic('R');
        refreshButton.setToolTipText("Refresh cargo list (Alt+R)");
        UITheme.styleButton(refreshButton);
        
        refreshButton.addActionListener(_ -> refreshCargoList());
        
        // Add keyboard shortcut for F5 to refresh
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), "refresh");
        actionMap.put("refresh", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshCargoList();
            }
        });

        buttonPanel.add(refreshButton);

        // Add components
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Initial load
        refreshCargoList();
    }

    @Override
    public void removeNotify() {
        super.removeNotify();
        if (visibilityTimer != null) {
            visibilityTimer.stop();
        }
    }

    public void refreshCargoList() {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                statusLabel.setText("Refreshing data...");
                statusLabel.setVisible(true);
                
                // Clear existing rows
                tableModel.setRowCount(0);

                // Get last 5 cargos using Stack
                LinkedList<Cargo> cargoHistory = client.getCargoHistory();
                Stack<Cargo> stack = new Stack<>();
                
                int startIndex = Math.max(0, cargoHistory.size() - 5);
                for (int i = startIndex; i < cargoHistory.size(); i++) {
                    stack.push(cargoHistory.get(i));
                }

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                // Add rows from stack
                while (!stack.isEmpty()) {
                    Cargo cargo = stack.pop();
                    Object[] row = {
                        cargo.getId(),
                        dateFormat.format(cargo.getDate()),
                        cargo.getStatus(),
                        cargo.getCityId(),
                        cargo.getDeliveryTime()
                    };
                    tableModel.addRow(row);
                }
                return null;
            }

            @Override
            protected void done() {
                statusLabel.setText("Data updated");
                if (visibilityTimer != null) {
                    visibilityTimer.stop();
                }
                visibilityTimer = new Timer(2000, _ -> statusLabel.setVisible(false));
                visibilityTimer.setRepeats(false);
                visibilityTimer.start();
            }
        };
        worker.execute();
    }
}