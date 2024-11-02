package com.comicbookreader.comicbook;

import com.comicbookreader.cleanupcrew.AppDataCleanup;
import com.comicbookreader.cleanupcrew.UserDataCleanup;

import javax.swing.*;
import java.io.IOException;


public class SettingsMenu {
    private JMenuBar menuBar;

    /**
     * Constructs a new SettingsMenu instance.
     * <p>
     * This constructor initializes the settings menu bar, creating a menu with options
     * for managing application and user data. It adds two menu items: "Remove Appdata"
     * and "Remove Userdata".
     * <br>
     * Each menu item is associated with an action listener that
     * prompts the user to confirm the deletion of the respective data when clicked.
     * If an IOException occurs during this process, it is wrapped in a RuntimeException.
     * </p>
     */
    public SettingsMenu(){
        menuBar = new JMenuBar();

        JMenu settingsMenu = new JMenu("Settings");
        JMenuItem appdataSettings = new JMenuItem("Remove Appdata");
        JMenuItem userSettings = new JMenuItem("Remove Userdata");

        appdataSettings.addActionListener(e -> {
            try {
                showAppdataSettingsDialog();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        userSettings.addActionListener(e -> {
            try {
                showUserDataSettingsDialog();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        settingsMenu.add(appdataSettings);
        settingsMenu.add(userSettings); // not implemented
        menuBar.add(settingsMenu);
    }

    public JMenuBar getMenuBar() {
        return menuBar;
    }

    /**
     * Displays a confirmation dialog to the user regarding the deletion of application data.
     * <p>
     * This method prompts the user with a warning message asking if they wish to delete
     * the application data. If the user confirms the action, the {@link AppDataCleanup}
     * process is initiated to handle the deletion. Note that this operation does not
     * affect any saved comic books.
     * </p>
     *
     * @throws IOException if an error occurs during the cleanup process
     */
    private void showAppdataSettingsDialog() throws IOException {
        int result = JOptionPane.showConfirmDialog(
                null,
                "Do you want to delete appdata?\n(This will not delete saved comic books)",
                "Warning!",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if(result == JOptionPane.OK_OPTION){
            new AppDataCleanup();
        }
    }

    /**
     * Displays a confirmation dialog to the user regarding the deletion of user data.
     * <p>
     * This method prompts the user with a warning message asking if they wish to delete
     * the user data. If the user confirms the action, the {@link UserDataCleanup}
     * process is initiated to handle the deletion. Note that this operation does not
     * affect any saved comic books.
     * </p>
     *
     * @throws IOException if an error occurs during the cleanup process
     */
    private void showUserDataSettingsDialog() throws IOException {
        int result = JOptionPane.showConfirmDialog(
                null,
                "Do you want to delete userdata?\n(This will not delete saved comic books)",
                "Warning!",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if(result == JOptionPane.OK_OPTION){
            new UserDataCleanup();
        }
    }
}
