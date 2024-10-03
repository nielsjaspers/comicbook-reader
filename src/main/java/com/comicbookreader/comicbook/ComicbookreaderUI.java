package com.comicbookreader.comicbook;

import com.comicbookreader.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ComicbookreaderUI implements ActionListener {
    private JFrame frame;
    private JLabel imageLabel;
    private JLabel pageNumberLabel;
    private ArrayList<Page> pages;
    private int currentPage = 0;

    public ComicbookreaderUI(ArrayList<Page> pages) {
       this.pages = pages;
       initUI();
    }

    public void initUI(){
        frame = new JFrame("Comic Book Reader");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(dim.width, dim.height);

        frame.setLayout(new BorderLayout());

        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(imageLabel, BorderLayout.CENTER);

        JPanel navigationPanel = new JPanel(new FlowLayout());
        JButton previousButton = new JButton("< Previous");
        previousButton.addActionListener(this);
        JButton nextButton = new JButton ("Next >");
        nextButton.addActionListener(this);

        pageNumberLabel = new JLabel("Current page: " + (currentPage + 1));

        navigationPanel.add(previousButton);
        navigationPanel.add(pageNumberLabel);
        navigationPanel.add(nextButton);

        frame.add(navigationPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
        updatePageDisplay();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (command.equals("Next >") && currentPage < (pages.size() - 1)) {
            currentPage ++;
            updatePageDisplay();
            System.out.println("The page is going up and = " + currentPage);


        } else if (command.equals("< Previous") && currentPage > 0) {
            currentPage --;
            updatePageDisplay();
            System.out.println("The page is going down and = " + currentPage);
        }

    }

    private void updatePageDisplay(){
        BufferedImage img = pages.get(currentPage).image;
        Dimension frameSize = frame.getSize();
        Image scaledImage = img.getScaledInstance(frameSize.width, frameSize.height, Image.SCALE_SMOOTH);

        if (img != null){
            imageLabel.setIcon(new ImageIcon(scaledImage));
            pageNumberLabel.setText("Current page: " + (currentPage + 1));
        }

    }
}
