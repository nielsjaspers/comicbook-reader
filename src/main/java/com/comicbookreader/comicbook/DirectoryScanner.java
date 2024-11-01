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

    public static List<String> getComicFilePaths(Path directory, String... extensions) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String userDataPath = "userdata/data.json";
        File userDataFile = new File(userDataPath);
        String appDataPath = "appdata/data.json";
        File appDataFile = new File(appDataPath);

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
