package com.comicbookreader.comicbook;

import com.comicbookreader.Main;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class ComicbookreaderUI implements ActionListener {
    private JFrame frame;
    private JLabel imageLabel;
    private JLabel pageNumberLabel;
    private ArrayList<Page> pages;
    private Comicbook comicbook;
    private int currentPage = 0;
    public Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    private ObjectMapper mapper = new ObjectMapper();


    public ComicbookreaderUI(Comicbook comicbook) {
        this.currentPage = comicbook.getComicbookProgression(comicbook.getName());
        this.comicbook = comicbook;
        this.pages = comicbook.getPages();
        initUI();
    }

    public void initUI() {
        frame = new JFrame("Comic Book Reader");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setSize(dim.width, dim.height);

        frame.setLayout(new BorderLayout());

        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.Y_AXIS));

        imageLabel = new JLabel();
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imagePanel.add(imageLabel);

        frame.add(imagePanel, BorderLayout.CENTER);

        JPanel navigationPanel = new JPanel(new FlowLayout());
        JButton previousButton = new JButton("< Previous");
        previousButton.addActionListener(this);
        JButton nextButton = new JButton("Next >");
        nextButton.addActionListener(this);

        JPanel mainMenuPanel = new JPanel(new FlowLayout());
        JButton mainMenuButton = new JButton("Main Menu");
        mainMenuButton.addActionListener(this);

        pageNumberLabel = new JLabel("Current page: " + (currentPage + 1) + " / " + pages.size());

        navigationPanel.add(previousButton);
        navigationPanel.add(pageNumberLabel);
        navigationPanel.add(nextButton);
        mainMenuPanel.add(mainMenuButton);
        frame.add(navigationPanel, BorderLayout.SOUTH);
        frame.add(mainMenuPanel, BorderLayout.NORTH);
        frame.setVisible(true);
        updatePageDisplay();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (command.equals("Next >") && currentPage < (pages.size() - 1)) {
            currentPage++;
            updatePageDisplay();
            System.out.println("The page is going up and = " + currentPage);


        } else if (command.equals("< Previous") && currentPage > 0) {
            currentPage--;
            updatePageDisplay();
            System.out.println("The page is going down and = " + currentPage);
        } else if (command.equals("Main Menu")) {
            comicbook.setProgression(currentPage);
            System.out.println("Progression for " + comicbook.getName() + " Last read page: " + currentPage);
            comicbook.setComicbookProgression(comicbook.getName(), currentPage);
            frame.dispose();
        }
    }


    private void updatePageDisplay() {
        if (pages.isEmpty()) {
            imageLabel.setIcon(null);
            pageNumberLabel.setText("No pages available");
            return;
        }
        BufferedImage img = pages.get(currentPage).image;
        if (img != null) {
            imageLabel.setIcon(new ImageIcon(scaleImages(img)));
            pageNumberLabel.setText("Current page: " + (currentPage + 1) + " / " + pages.size());
        } else {
            imageLabel.setIcon(null);
            pageNumberLabel.setText("Image not found for this page");
        }
    }


    private Image scaleImages(BufferedImage img) {
        int imgWidth = img.getWidth();
        int imgHeight = img.getHeight();

        int frameWidth = dim.width - 100;
        int frameHeight = dim.height - 150;

        double imgAspectRatio = (double) imgWidth / imgHeight;
        double frameAspectRatio = (double) frameWidth / frameHeight;

        int newWidth, newHeight;

        if (frameAspectRatio > imgAspectRatio) {
            newHeight = frameHeight;
            newWidth = (int) (newHeight * imgAspectRatio);
        } else {
            newWidth = frameWidth;
            newHeight = (int) (newWidth / imgAspectRatio);
        }
        Image scaledImage = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        return scaledImage;
    }
}

