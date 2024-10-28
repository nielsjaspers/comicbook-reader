package com.comicbookreader;
import com.comicbookreader.comicbook.CBRParser;
import com.comicbookreader.comicbook.Comicbook;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        JFrame frame = new JFrame("Comic Book Reader");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        System.out.println("h = " + dim.height + "\nw = " + dim.width);

        frame.setSize(dim.width, dim.height);

        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        CBRParser parser = new CBRParser();
        parser.extractPages("imported_comics/Origin of Galactus v1 001 (1996-02).cbr");
    }
}
