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
    private Comicbook currentComic;
    private JLabel numberLabel;

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

        // Creates panels for organization
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel westPanel = new JPanel(new BorderLayout());
        JPanel eastPanel = new JPanel(new BorderLayout());
        JPanel centerPanel = new JPanel(new BorderLayout());

        // Title Label
        JLabel titleLabel = new JLabel("Comic Books");
        westPanel.add(titleLabel, BorderLayout.NORTH); // Add to west panel

        // Get the list of comics
        getComicList(comicList);
        JScrollPane scrollPane = new JScrollPane(displayList);
        scrollPane.setPreferredSize(new Dimension(200, frame.getHeight())); // Set width of JList
        westPanel.add(scrollPane, BorderLayout.CENTER);

        // Number Label
        numberLabel = new JLabel("Number of pages: 0");
        numberLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center the number label
        centerPanel.add(numberLabel, BorderLayout.NORTH); // Add number label to the center panel

        // Preview of the comic
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center the image
        centerPanel.add(imageLabel, BorderLayout.CENTER); // Add image label to the center panel

        // Buttons
        JButton selectButton = new JButton("Select Comic");
        selectButton.addActionListener(this);

        JButton invertButton = new JButton("Invert Comic");
        invertButton.addActionListener(this);

        JButton addButton = new JButton("Import Comic");
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

                    Comicbook comicbook = Comicbook.fromFilePath(selectedFile.toString(), pages);
                    addComic(comicbook);  // Voeg de nieuwe comic toe aan de bestaande lijst
                } catch (IOException ex) {
                    System.err.println("Fout bij het verplaatsen van bestand: " + ex.getMessage());
                }
            }
        });


        // Panel for the Import, Select, invert buttons
        westPanel.add(addButton, BorderLayout.SOUTH); // Add to the bottom panel
        centerPanel.add(selectButton, BorderLayout.SOUTH); // Centered in the bottom
        eastPanel.add(invertButton, BorderLayout.SOUTH); // East of the bottom

        // Add panels to the main frame
        mainPanel.add(westPanel, BorderLayout.WEST); // West side for JList
        mainPanel.add(eastPanel, BorderLayout.EAST); // East side for InvertButton
        mainPanel.add(centerPanel, BorderLayout.CENTER); // Center for image and number

        // Adds panel to frame
        frame.add(mainPanel);
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
            int numberOfPages = currentComic.getPages().size(); // Get the number of pages from the current comic
            numberLabel.setText("Number of pages: " + numberOfPages);
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