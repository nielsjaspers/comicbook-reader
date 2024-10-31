package com.comicbookreader.comicbook;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class DirectoryScanner {

    public static List<String> getComicFilePaths(Path directory, String... extensions) {
        CBZParser cbzParser = new CBZParser();
        CBRParser cbrParser = new CBRParser();
        List<String> comicFiles = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(directory, 1)) {  // Use walk with depth 1 to avoid subdirectories
            paths
                    .filter(Files::isRegularFile)  // Only include regular files, ignore directories
                    .filter(path -> {
                        // Check if file has one of the desired extensions
                        String fileName = path.getFileName().toString().toLowerCase();
                        for (String extension : extensions) {
                            if (fileName.endsWith(extension)) {
                                return true;
                            }
                        }
                        return false;
                    })
                    .map(Path::toString)  // Convert to String representation
                    .forEach(comicFiles::add);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return comicFiles;
    }
}
