package com.comicbookreader.comicbook;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ComicBookLoader {
    private static ArrayList<Comicbook> comicList = new ArrayList<>();

    /**
     * Loads comic books from a list of specified file paths.
     * <p>
     * This static method iterates over the provided list of comic paths and routes each path
     * to the appropriate parser using the {@link #routeToParser(String)} method. This enables
     * the loading of comic books based on their file types.
     * </p>
     *
     * @param comicPaths a list of file paths to comic book files to be loaded
     */
    public static void loadComics(@NotNull List<String> comicPaths) {
        for (String comicPath : comicPaths) {
            routeToParser(comicPath);
        }
    }

    /**
     * Routes the provided file path to the appropriate parser based on its file extension.
     * <p>
     * This method checks the file extension of the specified file path to determine whether it is
     * a CBR or CBZ/NHL comic file. It then uses the corresponding parser to extract the pages
     * from the comic file. If the file type is unsupported, it prints an error message.
     * Finally, it creates a {@link Comicbook} instance and adds it to the comic list.
     * </p>
     *
     * @param filePath the path of the comic book file to be parsed
     */
    private static void routeToParser(@NotNull String filePath) {
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
