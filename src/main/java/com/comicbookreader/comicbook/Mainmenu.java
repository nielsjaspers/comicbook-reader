package com.comicbookreader.comicbook;
import com.comicbookreader.comicbook.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import java.util.ArrayList;


public class Mainmenu implements ActionListener {
    private JFrame frame;
    private JLabel imageLabel;
    private JLabel pageNumberLabel;
    public Comicbook comicbook = new Comicbook();
    private JList<String> displayList;
    private DefaultListModel<String> listModel;
    private ArrayList<Comicbook> comicList;
    public Page pages = new Page();

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

        listModel = new DefaultListModel<>();
        for (Comicbook comic : comicList) {
            listModel.addElement(comic.getName());
        }

        displayList = new JList<>(listModel);
        displayList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(displayList);
        frame.add(scrollPane, BorderLayout.WEST);

        JButton selectButton = new JButton("Select Comic");
        selectButton.addActionListener(this);
        frame.add(selectButton, BorderLayout.SOUTH);

        frame.setVisible(true);


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