package com.comicbookreader;
import com.comicbookreader.comicbook.*;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {

        CBRParser cbrParser = new CBRParser();
        String cbrFilePath = "imported_comics/Origin of Galactus v1 001 (1996-02).cbr";

        cbrParser.extractPages(cbrFilePath);

        ArrayList<Page> pages2 = new CBZParser().extractPages(cbrFilePath);
        Comicbook comicbook3 = new Comicbook("Origin of Galactus", pages2);

        CBZParser cbzParser = new CBZParser();
        String cbzFilePath = "imported_comics/Deadpool Team-Up 002 (2024) (Digital) (Shan-Empire).cbz";
        String cbzFilePath1 = "imported_comics/pepper&carrot_1.nhlcomic";

        ArrayList<Page> pages = new CBZParser().extractPages(cbzFilePath);
        ArrayList<Page> pages1 = new CBZParser().extractPages(cbzFilePath1);
        Comicbook comicbook = new Comicbook("Deadpool", pages);
        Comicbook comicbook1 = new Comicbook("Deadpool1", pages);
        Comicbook comicbook2 = new Comicbook("Pepper & Carrot", pages1);


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