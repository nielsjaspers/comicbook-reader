package com.comicbookreader;
import com.comicbookreader.comicbook.*;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {

        Path directory = Paths.get("imported_comics");

        // paths to data folders
        Path appDataDirectory = Paths.get("appdata");
        Path userDataDirectory = Paths.get("userdata");

        List<String> comicPath = DirectoryScanner.getComicFilePaths(directory, ".cbz", ".cbr", ".nhlcomic");
      
        File importedComicPath = new File("imported_comics");

        if (!importedComicPath.exists()) {
            importedComicPath.mkdir();
        }

        // Maakt data files aan en initialiseert een lege 'array'
        File appDataJson = new File("appdata/data.json");
        File userDataJson = new File("userdata/data.json");
        if (!Files.exists(appDataDirectory)) {
            Files.createDirectory(appDataDirectory);
            if (!appDataJson.exists()) {
                appDataJson.createNewFile();
                BufferedWriter writer = new BufferedWriter(new FileWriter(appDataJson));
                writer.write("[]");
                writer.close();
            }

        }
        if (!Files.exists(userDataDirectory)) {
            Files.createDirectory(userDataDirectory);
            if (!userDataJson.exists()) {
                userDataJson.createNewFile();
                BufferedWriter writer = new BufferedWriter(new FileWriter(userDataJson));
                writer.write("[]");
                writer.close();
            }
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