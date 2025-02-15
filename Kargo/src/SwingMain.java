import javax.swing.*;

import java.util.HashMap;
import java.util.Map;

public class SwingMain {
    public static void main(String[] args) {
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create user database
        Map<Integer, Client> userDatabase = new HashMap<>();

        // Launch application
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame(userDatabase);
            loginFrame.setVisible(true);
        });
    }
}