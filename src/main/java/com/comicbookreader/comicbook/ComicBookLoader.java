package com.comicbookreader.comicbook;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ComicBookLoader {
    private static ArrayList<Comicbook> comicList = new ArrayList<>();

    public static void loadComics(List<String> comicPaths) {
        for (String comicPath : comicPaths) {
            routeToParser(comicPath);
        }
    }

    private static void routeToParser(String filePath) {
        try {
            ArrayList<Page> pages;
            if (filePath.endsWith(".cbr")) {
                pages = new CBRParser().extractPages(filePath);
            } else if (filePath.endsWith(".cbz") || filePath.endsWith(".nhlcomic")) {
                pages = new CBZParser().extractPages(filePath);
            } else {
                System.out.println("Unsupported file type: " + filePath);
                return;
            }
            Comicbook comicbook = Comicbook.fromFilePath(filePath, pages, filePath);
            comicList.add(comicbook);
        } catch (IOException e) {
            System.err.println("Error loading comic: " + e.getMessage());
        }
    }

    public static ArrayList<Comicbook> getComicList() {
        return comicList;
    }
}
