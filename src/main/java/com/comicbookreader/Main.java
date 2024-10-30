package com.comicbookreader;
import com.comicbookreader.comicbook.*;

import java.io.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        File importedComicPath = new File("imported_comics");

        if (!importedComicPath.exists()) {
            importedComicPath.mkdir();
        }

        // Initialiseert leeg Mainmenu -- Zorgt dat de app uberhaupt openblijft.
        ArrayList<Comicbook> comicbooks = new ArrayList<>();
        new Mainmenu(comicbooks);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                CBRParser cbrParser = new CBRParser();
                try {
                    cbrParser.cleanup();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });


    }
}