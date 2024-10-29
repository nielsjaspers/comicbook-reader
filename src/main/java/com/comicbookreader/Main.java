package com.comicbookreader;
import com.comicbookreader.comicbook.*;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {

        CBZParser cbzParser = new CBZParser();
        String cbzFilePath = "imported_comics/Deadpool Team-Up 002 (2024) (Digital) (Shan-Empire).cbz";
        String cbzFilePath1 = "imported_comics/pepper&carrot_1.nhlcomic";

        ArrayList<Page> pages = new CBZParser().extractPages(cbzFilePath);
        ArrayList<Page> pages1 = new CBZParser().extractPages(cbzFilePath1);
        Comicbook comicbook = new Comicbook("Deadpool", pages, false);
        Comicbook comicbook1 = new Comicbook("Deadpool1", pages, false);
        Comicbook comicbook2 = new Comicbook("Pepper & Carrot", pages1, true);


        ArrayList<Comicbook> comicbooks = new ArrayList<>();
        comicbooks.add(comicbook);
        comicbooks.add(comicbook1);
        comicbooks.add(comicbook2);

        System.out.println("Pages extracted: " + pages.size()); // Print number of pages
        new Mainmenu(comicbooks);

    }
}