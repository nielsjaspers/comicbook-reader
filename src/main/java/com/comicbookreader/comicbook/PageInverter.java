package com.comicbookreader.comicbook;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class PageInverter {

    ArrayList<Page> pages = new ArrayList<>();
    ArrayList<Page> invertedPages = new ArrayList<>();

    public ArrayList<Page> PageInverter(ArrayList<Page> pages) {
        Collections.reverse(pages);
        return invertedPages;
    }

}
