package com.comicbookreader.cleanupcrew;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

import com.comicbookreader.comicbook.CBRParser;

import javax.swing.*;

public class AppDataCleanup {
    private String appDataPath = "appdata";

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
