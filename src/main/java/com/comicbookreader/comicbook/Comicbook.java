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
    public String path;


    public Comicbook(String name, List<Page> pages, boolean invert) {
        this.name = name;
        this.pages = new ArrayList<>(pages);
        if (invert) {
            this.pages = invertPages();
        }
    }


    public Comicbook(String name, List<Page>pages) {
        this.name = name;
        this.pages = new ArrayList<>(pages);
    }


    public Comicbook(List<Page> pages) {
        this.pages = new ArrayList<>(pages);
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
    public static Comicbook fromFilePath(String filePath, List<Page> pages) {
        String name = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.lastIndexOf("."));
        System.out.println(name);
        return new Comicbook(name, pages);
    }
}

