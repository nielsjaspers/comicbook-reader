package com.comicbookreader.comicbook;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class Mainmenu implements ActionListener {

    private JFrame frame;
    private JLabel imageLabel;
    private Comicbook comicbook = new Comicbook();
    private JList<String> displayList;
    private DefaultListModel<String> listModel;
    private ArrayList<Comicbook> comicList;
    private ArrayList<Page> pages;

    public Mainmenu(ArrayList<Comicbook> comicList) {
        this.comicList = comicList;
        initUI();
    }

    public void initUI() {
        frame = new JFrame("Comic Book Reader - Main Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(dim.width, dim.height);

        frame.setLayout(new BorderLayout());

        //Gets the list of comics
        getComicList();
        JScrollPane scrollPane = new JScrollPane(displayList);
        scrollPane.setPreferredSize(new Dimension(frame.getWidth() / 8, frame.getHeight()));
        frame.add(scrollPane, BorderLayout.WEST);

        //shows the first page when clicked on a comic
        imageLabel = new JLabel();
        frame.add(imageLabel, BorderLayout.CENTER);

        JButton selectButton = new JButton("Select Comic");
        selectButton.addActionListener(this);
        frame.add(selectButton, BorderLayout.SOUTH);

        frame.setVisible(true);
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
                            Comicbook selectedComicbook = comicList.get(selectedIndex);
                            System.out.println("Comic selected: " + selectedComicbook.getName());
                            Page firstPage = selectedComicbook.getPages().get(0);

                            if (firstPage.image != null) {
                                Image scaledImage = firstPage.image.getScaledInstance(500, 700, Image.SCALE_SMOOTH);  // Adjust dimensions as needed
                                imageLabel.setIcon(new ImageIcon(scaledImage));
                            } else {
                                imageLabel.setText("No image available for this page.");
                            }
                        }
                    }
                }
            });
        }

    @Override
    public void actionPerformed(ActionEvent e) {

        int selectedIndex = displayList.getSelectedIndex();
        if (selectedIndex != -1) {
            Comicbook tempcomic = comicList.get(selectedIndex);

            new ComicbookreaderUI(tempcomic.getPages());

        } else System.out.println("No comic selected.");
    }
}