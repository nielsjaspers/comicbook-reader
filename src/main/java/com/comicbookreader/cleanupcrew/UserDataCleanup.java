package com.comicbookreader.cleanupcrew;

import com.comicbookreader.comicbook.CBRParser;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class UserDataCleanup {
    private String userDataPath = "userdata";

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
