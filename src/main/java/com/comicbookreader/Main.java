package com.comicbookreader;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        final int HEIGHT_FRAME = 400;
        final int WIDTH_FRAME = 400;

        JFrame frame = new JFrame("Comic Book Reader");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setSize(WIDTH_FRAME, HEIGHT_FRAME);

        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
