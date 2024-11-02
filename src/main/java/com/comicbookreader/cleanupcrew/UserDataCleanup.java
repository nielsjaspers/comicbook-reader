package com.comicbookreader.cleanupcrew;

import com.comicbookreader.comicbook.CBRParser;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class UserDataCleanup {
    private String userDataPath = "userdata";

    /**
     * Performs a cleanup of user-specific data.
     * <p>
     * This constructor initiates the cleanup of user data by:
     * <ul>
     *     <li>Calling {@link CBRParser#cleanup()} to clear any temporary data related to CBR parsing.</li>
     *     <li>Deleting the user data directory specified by {@code userDataPath}.</li>
     * </ul>
     * After cleanup, the user is prompted to restart the application. If the user confirms, the application exits with
     * a success status (0); if not, it exits with a failure status (-1).
     * </p>
     *
     * @throws IOException if an error occurs during directory deletion or file cleanup.
     */
    public UserDataCleanup() throws IOException {
        CBRParser cbrParser = new CBRParser();
        cbrParser.cleanup();

        FileUtils.deleteDirectory(new File(userDataPath));

        int result = JOptionPane.showConfirmDialog(
                null,
                "Cleanup complete, please restart to continue",
                "Cleanup Complete",
                JOptionPane.DEFAULT_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {
            System.exit(0);
        }
        else {
            System.exit(-1);
        }
    }

}
