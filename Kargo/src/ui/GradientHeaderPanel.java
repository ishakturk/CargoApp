package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GradientHeaderPanel extends JPanel {
    private final String title;
    private final Font titleFont;
    private Timer pulseTimer;
    private float pulseAlpha = 0.0f;
    private boolean pulsing = false;
    
    public GradientHeaderPanel(String title) {
        this.title = title;
        this.titleFont = new Font("Segoe UI", Font.BOLD, 24);
        setPreferredSize(new Dimension(getWidth(), 80));
        
        // Create subtle pulse animation with proper cleanup
        pulseTimer = new Timer(50, action -> {
            if (pulsing) {
                pulseAlpha += 0.1f;
                if (pulseAlpha >= 1.0f) {
                    pulseAlpha = 0.0f;
                }
                repaint();
            }
        });
        
        // Add mouse interaction
        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                startPulse();
            }
            public void mouseExited(MouseEvent e) {
                stopPulse();
            }
        });
    }
    
    @Override
    public void removeNotify() {
        super.removeNotify();
        if (pulseTimer != null && pulseTimer.isRunning()) {
            pulseTimer.stop();
        }
    }
    
    private void startPulse() {
        pulsing = true;
        if (!pulseTimer.isRunning()) {
            pulseTimer.start();
        }
    }
    
    private void stopPulse() {
        pulsing = false;
        pulseTimer.stop();
        pulseAlpha = 0.0f;
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Create gradient background
        GradientPaint gradient = new GradientPaint(
            0, 0, UITheme.PRIMARY_COLOR,
            0, getHeight(), UITheme.SECONDARY_COLOR
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        // Add animated pattern
        g2d.setColor(new Color(255, 255, 255, 30));
        int offset = (int)(pulseAlpha * 20);
        for (int i = -20; i < getWidth() + 20; i += 20) {
            g2d.drawLine(i + offset, 0, i + offset + 10, getHeight());
        }
        
        // Draw title text with shadow
        g2d.setFont(titleFont);
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(title);
        int textHeight = fm.getHeight();
        int x = (getWidth() - textWidth) / 2;
        int y = (getHeight() + textHeight / 2) / 2;
        
        // Draw shadow
        g2d.setColor(new Color(0, 0, 0, 50));
        g2d.drawString(title, x + 2, y + 2);
        
        // Draw main text
        g2d.setColor(Color.WHITE);
        g2d.drawString(title, x, y);
    }
}