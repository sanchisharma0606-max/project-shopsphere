package com.shopsphere.desktop;

import com.shopsphere.desktop.view.LoginFrame;

import javax.swing.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main application launcher for the Java Swing Desktop Administration Suite.
 * Applies the OS native Pluggable Look and Feel and schedules GUI startup on the Swing Event Dispatch Thread (EDT).
 */
public class DesktopAdminApp {

    private static final Logger LOGGER = Logger.getLogger(DesktopAdminApp.class.getName());

    public static void main(String[] args) {
        // Pluggable Look and Feel configuration
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to apply system Look and Feel. Defaulting to Swing Metal/Nimbus.", e);
        }

        // Schedule UI creation on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
