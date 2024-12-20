package com.comicbookreader;
import com.comicbookreader.comicbook.*;

import javax.swing.*;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        System.setProperty("apple.laf.useScreenMenuBar", "true");

        // Set up the application directories
        setupDataDirectories();

        // Perform directory scan or load comics from JSON
        Path comicDirectory = Paths.get("imported_comics");
        List<String> comicPaths = DirectoryScanner.getComicFilePaths(comicDirectory, "cbr", "cbz", "nhlcomic");

        // Load comics from paths and initialize the Mainmenu
        ComicBookLoader.loadComics(comicPaths);
        ArrayList<Comicbook> comicList = ComicBookLoader.getComicList();
        new Mainmenu(comicList);

        // Start native Swing GUI
        SwingUtilities.invokeLater(SettingsMenu::new);

        // Cleanup unzipped .cbr comic books on shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                new CBRParser().cleanup();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));
    }

    /**
     * Initializes application data directories and JSON files.
     * <p>
     * Creates the "appdata" and "userdata" directories if they do not already exist.
     * Additionally, initializes "data.json" files in each directory with default values:
     * an empty array for "appdata/data.json" and an empty JSON object for "userdata/data.json".
     * </p>
     *
     * @throws IOException if an I/O error occurs during directory or file creation.
     */
    private static void setupDataDirectories() throws IOException {
        Path importedComicsDirectory = Paths.get("imported_comics");
        Path appDataDirectory = Paths.get("appdata");
        Path userDataDirectory = Paths.get("userdata");

        Files.createDirectories(appDataDirectory);
        Files.createDirectories(userDataDirectory);

        File appDataJson = new File("appdata/data.json");
        File userDataJson = new File("userdata/data.json");

        if (!appDataJson.exists()) {
            Files.writeString(appDataJson.toPath(), "[]"); // Initialize as empty array
        }
        if (!userDataJson.exists()) {
            Files.writeString(userDataJson.toPath(), "{}"); // Initialize with empty JSON object
        }
        if (!Files.exists(importedComicsDirectory)) {
            Files.createDirectories(importedComicsDirectory);
        }
    }
}
