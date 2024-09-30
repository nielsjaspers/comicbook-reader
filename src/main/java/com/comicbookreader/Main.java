package com.comicbookreader;
import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        final int HEIGHT_FRAME = 400;
        final int WIDTH_FRAME = 400;

        JFrame frame = new JFrame("Comic Book Reader");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        System.out.println("h = " + dim.height + "\nw = " + dim.width);

        frame.setSize(dim.width, dim.height);

        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
