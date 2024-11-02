package com.comicbookreader.comicbook;

import com.comicbookreader.cleanupcrew.AppDataCleanup;
import com.comicbookreader.cleanupcrew.UserDataCleanup;

import javax.swing.*;
import java.io.IOException;


public class SettingsMenu {
    private JMenuBar menuBar;

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

    private void showSettingsDialog(){
        JOptionPane.showMessageDialog(null, "Test Message");
    }
}
