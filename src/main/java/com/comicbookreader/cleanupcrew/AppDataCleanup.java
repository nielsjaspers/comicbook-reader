package com.comicbookreader.cleanupcrew;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

import com.comicbookreader.comicbook.CBRParser;

import javax.swing.*;

public class AppDataCleanup {
    private String appDataPath = "appdata";

    /**
     * Performs a cleanup of application and user data.
     * <p>
     * This constructor initializes a cleanup of application data by:
     * <ul>
     *     <li>Invoking the {@link CBRParser#cleanup()} method to clear any temporary CBR parsing data.</li>
     *     <li>Deleting the application data directory specified by {@code appDataPath}.</li>
     *     <li>Creating an instance of {@link UserDataCleanup} to handle user-specific data cleanup.</li>
     * </ul>
     * After cleanup, prompts the user to restart the application. If the user confirms, the application exits with
     * a success status (0); otherwise, it exits with a failure status (-1).
     * </p>
     *
     * @throws IOException if an error occurs during directory deletion or file cleanup.
     */
    public AppDataCleanup() throws IOException {
        CBRParser cbrParser = new CBRParser();
        cbrParser.cleanup();

        FileUtils.deleteDirectory(new File(appDataPath));

        int result = JOptionPane.showConfirmDialog(
                null,
                "Cleanup complete, please restart to continue",
                "Cleanup Complete",
                JOptionPane.DEFAULT_OPTION
        );

        new UserDataCleanup();
        if (result == JOptionPane.OK_OPTION) {
            System.exit(0);
        }
        else {
            System.exit(-1);
        }
    }
}
