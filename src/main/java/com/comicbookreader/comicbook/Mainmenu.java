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
    private Comicbook currentComic;
    private JLabel numberLabel;
    private String appDataPath = "appdata/data.json";
    private File appDataFile = new File(appDataPath);
    private JButton startFromBeginningButton;


    public Mainmenu(ArrayList<Comicbook> comicList) {
        this.comicList = comicList; // Retrieve the loaded comics
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

        // Creates panels for organization
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel westPanel = new JPanel(new BorderLayout());
        JPanel eastPanel = new JPanel(new BorderLayout());
        JPanel centerPanel = new JPanel(new BorderLayout());

        // Title Label
        JLabel titleLabel = new JLabel("Comic Books");
        westPanel.add(titleLabel, BorderLayout.NORTH); // Add to west panel

        // Get the list of comics
        getComicList();
        int scrollPaneWidth = frame.getSize().width / 8;
        JScrollPane scrollPane = new JScrollPane(displayList);
        scrollPane.setPreferredSize(new Dimension(scrollPaneWidth, frame.getHeight())); // Set width of JList
        westPanel.add(scrollPane, BorderLayout.CENTER);

        // Number Label
        numberLabel = new JLabel("Number of pages: 0");
        numberLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center the number label
        centerPanel.add(numberLabel, BorderLayout.NORTH); // Add number label to the center panel

        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center the image
        centerPanel.add(imageLabel, BorderLayout.CENTER); // Add image label to the center panel

        //create Button panel for start from beginnning and continue comic
        JPanel buttonPanel = new JPanel(new FlowLayout());

        // Buttons
        startFromBeginningButton = new JButton("Start From Beginning");
        startFromBeginningButton.addActionListener(this);
        startFromBeginningButton.setEnabled(false);


        JButton selectButton = new JButton("Continue Comic");
        selectButton.addActionListener(this);

        JButton invertButton = new JButton("Invert Comic");
        invertButton.addActionListener(this);

        JButton addButton = new JButton("Import Comic");
        addButton.addActionListener(e -> importComic());

        buttonPanel.add(startFromBeginningButton);
        buttonPanel.add(selectButton);

        // Panel for the Import, Select, invert buttons
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);
        westPanel.add(addButton, BorderLayout.SOUTH); // Add to the bottom panel
        eastPanel.add(invertButton, BorderLayout.SOUTH); // East of the bottom

        // Add panels to the main frame
        mainPanel.add(westPanel, BorderLayout.WEST); // West side for JList
        mainPanel.add(eastPanel, BorderLayout.EAST); // East side for InvertButton
        mainPanel.add(centerPanel, BorderLayout.CENTER); // Center for image and number

        SettingsMenu settingsMenu = new SettingsMenu();
        frame.setJMenuBar(settingsMenu.getMenuBar());

        frame.add(mainPanel);
        frame.setVisible(true);

    }

    /**
     * Imports a comic book file selected by the user through a file chooser dialog.
     * <p>
     * This method opens a {@link JFileChooser} to allow the user to select a comic book file
     * with supported extensions (.cbr, .cbz, .nhlcomic). Upon selection, it moves the file to a
     * designated target directory ("imported_comics") and extracts its pages using the appropriate
     * parser based on the file extension. A new {@link Comicbook} object is created and added to
     * the comic list.
     * </p>
     */
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
                        updatestartFromBeginningButton(currentComic);
                        displayPage(firstPage);
                    }
                }
            }
        });
    }

    private void displayPage(Page page) {
        if (page.image != null) {
            int numberOfPages = currentComic.getPages().size(); // Get the number of pages from the current comic
            numberLabel.setText("Number of pages: " + numberOfPages);
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
        } else if (command.equals("Continue Comic")) {
            int selectedIndex = displayList.getSelectedIndex();
            if (selectedIndex != -1) {
                updatestartFromBeginningButton(currentComic);
                new ComicbookreaderUI(currentComic);
            } else {
                System.out.println("No comic selected.");
            }
        }
        if (command.equals("Start From Beginning")) {
                currentComic.setComicbookProgression(currentComic.getName(), 0);
                new ComicbookreaderUI(currentComic);
            }
        }
    private void updatestartFromBeginningButton(Comicbook currentComic) {
        if (currentComic != null && currentComic.getComicbookProgression(currentComic.getName()) >= 1) {
            // If currentPage is greater than 0, show the button, otherwise hide it
            startFromBeginningButton.setEnabled(true);
        } else startFromBeginningButton.setEnabled(false);
    }
    public JList<String> getDisplayList() {
        return displayList;
    }

    public JLabel getImageLabel() {
        return imageLabel;
    }
}
