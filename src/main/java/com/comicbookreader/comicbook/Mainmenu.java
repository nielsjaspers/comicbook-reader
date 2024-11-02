package com.comicbookreader.comicbook;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class Mainmenu implements ActionListener {

    private JFrame frame;
    private JLabel imageLabel;
    private JList<String> displayList;
    private DefaultListModel<String> listModel;
    private ArrayList<Comicbook> comicList;
    private int scrollPaneWidth;
    private Comicbook currentComic;

    private String appDataPath = "appdata/data.json";
    private File appDataFile = new File(appDataPath);

    public Mainmenu(ArrayList<Comicbook> comicList) {
        this.comicList = comicList; // Use the loaded comics directly
        initUI();
    }

    public void addComic(Comicbook comicbook) {
        comicList.add(comicbook);
        listModel.addElement(comicbook.getName()); // Update display list

        // Save new comic to JSON file
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<Comicbook> tempComicList = new ArrayList<>(comicList);
            mapper.writeValue(appDataFile, tempComicList);
            System.out.println("Comic added to JSON: " + comicbook.getName());
        } catch (IOException e) {
            System.err.println("Error saving to JSON: " + e.getMessage());
        }
    }

    public void initUI() {
        frame = new JFrame("Comic Book Reader - Main Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(dim.width, dim.height);

        scrollPaneWidth = frame.getSize().width / 8;
        frame.setLayout(new BorderLayout());

        // Display comics in list
        getComicList();
        JScrollPane scrollPane = new JScrollPane(displayList);
        scrollPane.setPreferredSize(new Dimension(scrollPaneWidth, frame.getHeight()));
        frame.add(scrollPane, BorderLayout.WEST);

        imageLabel = new JLabel();
        frame.add(imageLabel, BorderLayout.CENTER);

        JButton selectButton = new JButton("Select Comic");
        selectButton.addActionListener(this);

        JButton invertButton = new JButton("Invert Comic");
        invertButton.addActionListener(this);

        JButton addButton = new JButton("Import Comic");
        addButton.setPreferredSize(new Dimension(scrollPaneWidth, addButton.getPreferredSize().height));

        addButton.addActionListener(e -> importComic());

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(selectButton, BorderLayout.CENTER);
        bottomPanel.add(invertButton, BorderLayout.EAST);
        bottomPanel.add(addButton, BorderLayout.WEST);

        frame.add(bottomPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private void importComic() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Comic Book Files", "cbr", "cbz", "nhlcomic"));

        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            Path targetDirectory = Path.of("imported_comics");

            try {
                Path targetPath = targetDirectory.resolve(selectedFile.getName());
                Files.move(selectedFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("File moved to: " + targetPath);

                ArrayList<Page> pages;
                if (selectedFile.toString().endsWith(".cbr")) {
                    pages = new CBRParser().extractPages(targetPath.toString());
                } else {
                    pages = new CBZParser().extractPages(targetPath.toString());
                }

                Comicbook comicbook = Comicbook.fromFilePath(targetPath.toString(), pages, targetPath.toString());

                comicbook.setPath(targetPath.toString());
                addComic(comicbook);

            } catch (IOException e) {
                System.err.println("Error moving file: " + e.getMessage());
            }
        }
    }

    public void getComicList() {
        listModel = new DefaultListModel<>();
        for (Comicbook comic : comicList) {
            listModel.addElement(comic.getName());
        }
        displayList = new JList<>(listModel);
        displayList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        displayList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedIndex = displayList.getSelectedIndex();
                    if (selectedIndex != -1) {
                        currentComic = comicList.get(selectedIndex);
                        Page firstPage = currentComic.getPages().getFirst();
                        displayPage(firstPage);
                    }
                }
            }
        });
    }

    private void displayPage(Page page) {
        if (page.image != null) {
            Image scaledImage = page.image.getScaledInstance(500, 700, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledImage));
        } else {
            imageLabel.setText("No image available for this page.");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (command.equals("Invert Comic") && currentComic != null) {
            currentComic.invertPages();
            displayPage(currentComic.getPages().getFirst());
        } else if (command.equals("Select Comic")) {
            int selectedIndex = displayList.getSelectedIndex();
            if (selectedIndex != -1) {
                currentComic = comicList.get(selectedIndex);
                new ComicbookreaderUI(currentComic.getPages());
            } else {
                System.out.println("No comic selected.");
            }
        }
    }
    public JList<String> getDisplayList() {
        return displayList;
    }

    public JLabel getImageLabel() {
        return imageLabel;
    }
}
