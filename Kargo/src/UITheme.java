import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;

public class UITheme {
    // Colors
    public static final Color PRIMARY_COLOR = new Color(70, 130, 180); // Steel Blue
    public static final Color SECONDARY_COLOR = new Color(135, 206, 235); // Sky Blue
    public static final Color BACKGROUND_COLOR = new Color(240, 248, 255); // Alice Blue
    public static final Color TEXT_COLOR = new Color(25, 25, 25);
    public static final Color SUCCESS_COLOR = new Color(46, 139, 87); // Sea Green
    public static final Color ERROR_COLOR = new Color(178, 34, 34); // Firebrick
    
    // Fonts
    public static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font REGULAR_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font SMALL_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    
    // Button styling
    public static void styleButton(JButton button) {
        button.setFont(REGULAR_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(PRIMARY_COLOR);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Enhanced hover effect with smooth animation
        Timer fadeTimer = new Timer(50, null);
        Color originalColor = button.getBackground();
        ActionListener[] storedActions = new ActionListener[1];
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                fadeTimer.stop();
                if (storedActions[0] != null) {
                    fadeTimer.removeActionListener(storedActions[0]);
                }
                
                final float[] fraction = {0.0f};
                storedActions[0] = _ -> {
                    fraction[0] += 0.2f;
                    if (fraction[0] >= 1.0f) {
                        fraction[0] = 1.0f;
                        fadeTimer.stop();
                    }
                    Color currentColor = interpolateColor(originalColor, SECONDARY_COLOR, fraction[0]);
                    button.setBackground(currentColor);
                };
                fadeTimer.addActionListener(storedActions[0]);
                fadeTimer.start();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                fadeTimer.stop();
                if (storedActions[0] != null) {
                    fadeTimer.removeActionListener(storedActions[0]);
                }
                
                final float[] fraction = {1.0f};
                storedActions[0] = _ -> {
                    fraction[0] -= 0.2f;
                    if (fraction[0] <= 0.0f) {
                        fraction[0] = 0.0f;
                        fadeTimer.stop();
                    }
                    Color currentColor = interpolateColor(originalColor, SECONDARY_COLOR, fraction[0]);
                    button.setBackground(currentColor);
                };
                fadeTimer.addActionListener(storedActions[0]);
                fadeTimer.start();
            }
        });
        
        // Add keyboard focus highlight
        button.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                button.setBorderPainted(true);
                button.setBorder(BorderFactory.createLineBorder(SECONDARY_COLOR, 2));
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                button.setBorderPainted(false);
            }
        });
    }
    
    private static Color interpolateColor(Color c1, Color c2, float fraction) {
        int red = (int) (c1.getRed() + fraction * (c2.getRed() - c1.getRed()));
        int green = (int) (c1.getGreen() + fraction * (c2.getGreen() - c1.getGreen()));
        int blue = (int) (c1.getBlue() + fraction * (c2.getBlue() - c1.getBlue()));
        return new Color(red, green, blue);
    }
    
    // Panel styling
    public static void stylePanel(JPanel panel) {
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }
    
    // Field styling
    public static void styleField(JTextField field) {
        field.setFont(REGULAR_FONT);
        field.setForeground(TEXT_COLOR);
        field.setBackground(Color.WHITE);
        Border defaultBorder = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        );
        Border focusBorder = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SECONDARY_COLOR, 2),
            BorderFactory.createEmptyBorder(4, 4, 4, 4)
        );
        field.setBorder(defaultBorder);
        
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(focusBorder);
                field.setBackground(new Color(240, 248, 255)); // Very light blue
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(defaultBorder);
                field.setBackground(Color.WHITE);
            }
        });
    }
    
    // Label styling
    public static void styleLabel(JLabel label) {
        label.setFont(REGULAR_FONT);
        label.setForeground(TEXT_COLOR);
    }
    
    // ComboBox styling
    public static void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setFont(REGULAR_FONT);
        comboBox.setForeground(TEXT_COLOR);
        comboBox.setBackground(Color.WHITE);
        Border lineBorder = BorderFactory.createLineBorder(PRIMARY_COLOR, 1);
        Border paddingBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        comboBox.setBorder(BorderFactory.createCompoundBorder(lineBorder, paddingBorder));
    }
    
    // Table styling
    public static void styleTable(JTable table) {
        table.setFont(REGULAR_FONT);
        table.setForeground(TEXT_COLOR);
        table.setBackground(Color.WHITE);
        table.setSelectionBackground(SECONDARY_COLOR);
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(new Color(230, 230, 230));
        table.setRowHeight(30);
        
        // Header styling
        JTableHeader header = table.getTableHeader();
        header.setFont(HEADER_FONT);
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, SECONDARY_COLOR));
    }
    
    // Tree styling
    public static void styleTree(JTree tree) {
        tree.setFont(REGULAR_FONT);
        tree.setBackground(Color.WHITE);
        tree.setRowHeight(30);
    }
}