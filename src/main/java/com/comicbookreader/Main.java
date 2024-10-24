package com.comicbookreader;
import com.comicbookreader.comicbook.*;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {

        //new ComicbookreaderUI(pages);

        CBZParser cbzParser = new CBZParser();
        String cbzFilePath = "imported_comics/Deadpool Team-Up 002 (2024) (Digital) (Shan-Empire).cbz";

        ArrayList<Page> pages = new CBZParser().extractPages(cbzFilePath);

        Comicbook comicbook = new Comicbook("Deadpool", pages);
        Comicbook comicbook1 = new Comicbook("Deadpool1", pages);


        ArrayList<Comicbook> comicbooks = new ArrayList<>();
        comicbooks.add(comicbook);
        comicbooks.add(comicbook1);

        System.out.println("Pages extracted: " + pages.size()); // Print number of pages
        new Mainmenu(comicbooks);



//        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
//
//        JFrame frame = new JFrame("Comic Book Reader");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//
//
//        System.out.println("h = " + dim.height + "\nw = " + dim.width);
//
//        frame.setSize(dim.width, dim.height);
//
//        frame.setLocationRelativeTo(null);
//        frame.setVisible(true);
//
//        ArrayList<Page> pages = (ArrayList<Page>) cbzParser.extractPages(cbzFilePath);
//
//        for (Page page : pages) {
//            System.out.println("page.image = " + page.image);
//            if (page.image != null) {
//                JLabel label = new JLabel(new ImageIcon(page.image));
//                frame.getContentPane().removeAll();  // Remove previous image
//                frame.getContentPane().add(label, BorderLayout.CENTER);
//                frame.revalidate();  // Revalidate the frame to display changes
//                frame.repaint();
//            }
//        }
    }
}