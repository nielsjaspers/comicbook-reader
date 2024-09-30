package com.comicbookreader;
import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        JFrame frame = new JFrame("Comic Book Reader");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        System.out.println("h = " + dim.height + "\nw = " + dim.width);

        frame.setSize(dim.width, dim.height);

        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
