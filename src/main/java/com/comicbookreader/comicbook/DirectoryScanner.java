package com.comicbookreader.comicbook;

import com.comicbookreader.user.Userdata;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class DirectoryScanner {

    /**
     * Retrieves a list of comic book file paths from the specified directory or user data.
     * <p>
     * This method first checks for the existence of a user data file and throws an exception
     * if it cannot be read. It then loads user data to determine whether an initial scan of the
     * directory has been performed. If the application data file exists and the initial scan has
     * been completed, it retrieves comic book paths from the app data file. Otherwise, it performs
     * a directory scan to collect comic file paths based on the specified extensions and updates
     * the user data accordingly.
     * </p>
     *
     * @param directory the directory to scan for comic book files
     * @param extensions the allowed file extensions for comic books (e.g., .cbr, .cbz)
     * @return a list of strings representing the paths to the comic book files
     * @throws IOException if an error occurs while reading files or performing scans
     */
    public static List<String> getComicFilePaths(Path directory, String... extensions) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String userDataPath = "userdata/data.json";
        File userDataFile = new File(userDataPath);
        String appDataPath = "appdata/data.json";
        File appDataFile = new File(appDataPath);

        // Controleer of het userdata bestand bestaat
        if (!userDataFile.exists()) {
            throw new IOException("Cannot read userdata: " + userDataPath);
        }

        // Load userdata to check initial scan status
        Userdata userdata = mapper.readValue(userDataFile, Userdata.class);

        List<String> comicFiles = new ArrayList<>();
        if (appDataFile.exists() && userdata.getHadInitialScan()) {
            // Step 2: Load from appdata/data.json
            List<Comicbook> comicList = mapper.readValue(appDataFile, new TypeReference<>() {});
            for (Comicbook comic : comicList) {
                comicFiles.add(comic.getPath()); // Use paths stored in JSON
            }
        } else {
            // Step 1: Perform directory scan, set hadInitialScan to true
            comicFiles = performInitialDirectoryScan(directory, extensions, appDataFile, userdata, userDataFile);
        }

        return comicFiles;
    }

    /**
     * Performs an initial scan of the specified directory to find comic book files.
     * <p>
     * This method walks through the given directory and filters for regular files with
     * specified extensions. For each valid comic book file found, it attempts to extract
     * its pages using either a {@link CBRParser} or a {@link CBZParser}. The results are stored
     * in a list of {@link Comicbook} objects, which are then saved to the application data file.
     * The user data is updated to reflect that the initial scan has been completed.
     * </p>
     *
     * @param directory the directory to scan for comic book files
     * @param extensions the allowed file extensions for comic books (e.g., .cbr, .cbz)
     * @param appDataFile the file where the list of found comic books will be saved
     * @param userdata the user data object to be updated
     * @param userDataFile the file where user data will be saved
     * @return a list of strings representing the paths to the found comic book files
     * @throws IOException if an error occurs while reading files or writing to the data files
     */
    private static List<String> performInitialDirectoryScan(Path directory, String[] extensions,
                                                            File appDataFile, Userdata userdata,
                                                            File userDataFile) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<String> comicFiles = new ArrayList<>();
        List<Comicbook> comicList = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(directory, 1)) {
            paths.filter(Files::isRegularFile).forEach(path -> {
                String fileName = path.getFileName().toString();
                for (String extension : extensions) {
                    if (fileName.endsWith(extension)) {
                        ArrayList<Page> pages;
                        try {
                            pages = fileName.endsWith(".cbr")
                                    ? new CBRParser().extractPages(path.toString())
                                    : new CBZParser().extractPages(path.toString());
                        } catch (IOException e) {
                            System.err.println("Error extracting pages: " + e.getMessage());
                            continue;
                        }
                        Comicbook comicbook = new Comicbook(fileName, pages, path.toString());
                        comicList.add(comicbook);
                        comicFiles.add(path.toString());
                    }
                }
            });
        }

        // Save to appDataFile and update userdata
        mapper.writeValue(appDataFile, comicList);
        userdata.setHadInitialScan(true);
        mapper.writeValue(userDataFile, userdata);

        return comicFiles;
    }

}
