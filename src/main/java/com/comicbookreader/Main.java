package com.comicbookreader;
import com.comicbookreader.comicbook.*;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        ArrayList<Comicbook> comicbooks = new ArrayList<>();
        Path directory = Paths.get("imported_comics");

        File importedComicPath = new File("imported_comics");
        if (!importedComicPath.exists()) {
            importedComicPath.mkdir();
        }

        // Initialiseert leeg Mainmenu -- Zorgt dat de app uberhaupt openblijft.

        new Mainmenu();

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