package com.comicbookreader.comicbook;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ComicBookLoader {
    private static ArrayList<Comicbook> comicList = new ArrayList<>();

    public static void startDirectoryScan(Path directory, String... extensions) {
        List<String> comicFiles = DirectoryScanner.getComicFilePaths(directory, extensions);
        System.out.println(comicFiles); // prints imported comics to console
        for (String comicFile : comicFiles) {
            routeToParser(comicFile);
        }
    }

    private static void routeToParser(String filePath) {
        try {
            ArrayList<Page> pages;
            CBZParser cbzParser = new CBZParser();
            CBRParser cbrParser = new CBRParser();

            if (filePath.endsWith(".cbr")) {
                pages = cbrParser.extractPages(filePath);
            } else if (filePath.endsWith(".cbz")) {
                pages = cbzParser.extractPages(filePath);
            } else if (filePath.endsWith(".nhlcomic")){
                pages = cbzParser.extractPages(filePath);
            } else{
                System.out.println("Unsupported file type: " + filePath);
                return;
            }

            Comicbook comicbook = Comicbook.fromFilePath(filePath, pages);
            comicList.add(comicbook);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static ArrayList<Comicbook> getComicList() {
        return comicList; // Return the list of loaded comics
    }
}