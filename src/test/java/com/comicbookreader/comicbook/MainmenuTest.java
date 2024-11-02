package com.comicbookreader.comicbook;

import com.comicbookreader.Main;
import org.junit.jupiter.api.Test;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class MainmenuTest{


    @Test
    public void testImageDisplayedWhenComicSelected() {
        //Creates a comicList to give to the mainmenu
        ArrayList<Comicbook> comicList = new ArrayList<>();
        ArrayList<Page> samplePages = createSamplePages(2);  //Page(s) being created
        Comicbook sampleComic = new Comicbook("Sample Comic", samplePages, false); // creates a sampleComic

        //Create Mainmenu instance and add a sample comic with pages containing images
        comicList.add(sampleComic);
        Mainmenu mainMenu = new Mainmenu(comicList);

        // Step 2: Action - Simulate comic selection in the displayList
        mainMenu.getDisplayList().setSelectedIndex(0);  // Select the first (and only) comic
        System.out.println("Comic has been selected");

        // Step 3: Verification - Check if the imageLabel is set with an ImageIcon
        ImageIcon displayedIcon = (ImageIcon) mainMenu.getImageLabel().getIcon();
        assertNotNull(displayedIcon, "An image should be displayed when a comic is selected.");
    }

    //Creates samplePages for the comicbook
    private ArrayList<Page> createSamplePages(int amountOfPages) {
        ArrayList<Page> pages = new ArrayList<>();
        while (pages.size() < amountOfPages) {
            BufferedImage newImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB); // Mock image
            pages.add(new Page(newImage));
            System.out.println("Sample page(s) being added " + amountOfPages + " times");
        }
        return pages;
    }
}
