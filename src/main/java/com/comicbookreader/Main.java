package com.comicbookreader;
import com.comicbookreader.comicbook.*;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {

        Path directory = Paths.get("imported_comics");

        List<String> comicPath = DirectoryScanner.getComicFilePaths(directory, ".cbz", ".cbr", ".nhlcomic");

        CBRParser cbrParser = new CBRParser();
        CBZParser cbzParser = new CBZParser();

        String cbzFilePath = "imported_comics/Deadpool Team-Up 002 (2024) (Digital) (Shan-Empire).cbz";
        String cbzFilePath1 = "imported_comics/pepper&carrot_1.nhlcomic";
        String cbrFilePath = "imported_comics/Origin of Galactus v1 001 (1996-02).cbr";

        ArrayList<Page> pages = new CBZParser().extractPages(cbzFilePath);
        ArrayList<Page> pages1 = new CBZParser().extractPages(cbzFilePath1);
        ArrayList<Page> pages2 = new CBRParser().extractPages(cbrFilePath);

        Comicbook comicbook = new Comicbook("Deadpool", pages, false);
        Comicbook comicbook1 = new Comicbook("Deadpool1", pages, false);
        Comicbook comicbook2 = new Comicbook("Pepper & Carrot", pages1, true);
        Comicbook comicbook3 = new Comicbook("Origin of Galactus", pages2, false);



        ArrayList<Comicbook> comicbooks = new ArrayList<>();
        comicbooks.add(comicbook);
        comicbooks.add(comicbook1);
        comicbooks.add(comicbook2);
        comicbooks.add(comicbook3);

        System.out.println("Pages extracted: " + pages.size()); // Print number of pages
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