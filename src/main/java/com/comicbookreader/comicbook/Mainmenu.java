package com.comicbookreader.comicbook;

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


public class Mainmenu implements ActionListener {

    private JFrame frame;
    private JLabel imageLabel;
    private JList<String> displayList;
    private DefaultListModel<String> listModel;
    private ArrayList<Comicbook> comicList;
    private int scrollPaneWidth;
    private Comicbook currentComic;

    public Mainmenu() {
        Path comicDirectory = Path.of("imported_comics");
        ComicBookLoader.startDirectoryScan(comicDirectory, "cbr", "cbz", "nhlcomic");

        this.comicList = ComicBookLoader.getComicList(); // Retrieve the loaded comics
        initUI();
    }

    public void addComic(Comicbook comicbook) {
        comicList.add(comicbook);
        listModel.addElement(comicbook.getName());
    }

    public void initUI() {
        frame = new JFrame("Comic Book Reader - Main Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(dim.width, dim.height);

        scrollPaneWidth = frame.getSize().width / 8;

        frame.setLayout(new BorderLayout());

        //Gets the list of comics
        getComicList(comicList);
        JScrollPane scrollPane = new JScrollPane(displayList);
        scrollPane.setPreferredSize(new Dimension(scrollPaneWidth, frame.getHeight()));
        frame.add(scrollPane, BorderLayout.WEST);

        //shows the first page when clicked on a comic
        imageLabel = new JLabel();
        frame.add(imageLabel, BorderLayout.CENTER);

        JButton selectButton = new JButton("Select Comic");
        selectButton.addActionListener(this);

        JButton InvertButton = new JButton("Invert Comic");
        InvertButton.addActionListener(this);

        JButton addButton = new JButton("Import Comic");
        addButton.setPreferredSize(new Dimension(scrollPaneWidth, addButton.getPreferredSize().height));

        addButton.addActionListener(e -> {
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
                    System.out.println("Bestand verplaatst naar: " + targetPath);

                    ArrayList<Page> pages;
                    if (selectedFile.toString().endsWith(".cbr")) {
                        pages = new CBRParser().extractPages(targetPath.toString());
                    } else {
                        pages = new CBZParser().extractPages(targetPath.toString());
                    }

                    Comicbook comicbook = new Comicbook(selectedFile.getName(), pages, false);
                    addComic(comicbook);  // Voeg de nieuwe comic toe aan de bestaande lijst
                } catch (IOException ex) {
                    System.err.println("Fout bij het verplaatsen van bestand: " + ex.getMessage());
                }
            }
        });


        // Paneel met knoppen onder in het scherm -- Voor Import, Select & Invert
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(selectButton, BorderLayout.CENTER);
        bottomPanel.add(InvertButton, BorderLayout.EAST);
        bottomPanel.add(addButton, BorderLayout.WEST);

        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    public void getComicList(ArrayList<Comicbook> comicList) {
        //get the list of comics
        listModel = new DefaultListModel<>();
        for (Comicbook comic : comicList) {
            System.out.println(comic);
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
                        System.out.println("Comic selected: " + currentComic.getName());
                        Page firstPage = currentComic.getPages().getFirst();
                        displayPage(currentComic.getPages().get(0));
                    }
                }
            }
        });
    }

    private void displayPage(Page page) {
        if (page.image != null) {
            Image scaledImage = page.image.getScaledInstance(500, 700, Image.SCALE_SMOOTH);  // Adjust dimensions as needed
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
            displayPage(currentComic.getPages().get(0));
        }
        int selectedIndex = displayList.getSelectedIndex();
        if (selectedIndex != -1 && command.equals("Select Comic")){
            currentComic = comicList.get(selectedIndex);
            new ComicbookreaderUI(currentComic.getPages());

        } else System.out.println("No comic selected.");


    }
}