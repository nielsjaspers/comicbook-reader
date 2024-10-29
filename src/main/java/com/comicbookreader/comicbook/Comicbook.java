package com.comicbookreader.comicbook;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Comicbook {
    public String name;
    public ArrayList<Author> authorList;
    public ArrayList<Page> pages;
    public BufferedImage coverImage;
    public boolean read;
    public int progression;
    public String publisher;
    public Date publishedDate;
    public boolean favourite;
    public boolean bookmark;
    public int numberInSeries;


    public Comicbook(String name, List<Page> pages, boolean invert) {
        this.name = name;
        this.pages = new ArrayList<>(pages);
        if (invert) {
            this.pages = invertPages();
        }
    }


    public Comicbook(String name) {
        this.name = name;
        this.pages = new ArrayList<>();
    }

    public Comicbook() {
        this.pages = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public ArrayList<Page> getPages() {
        return pages;
    }

    public ArrayList<Page> invertPages() {
            Collections.reverse(pages);
        return pages;
    }
}

