package com.comicbookreader.comicbook;

import java.util.ArrayList;
import java.util.Collections;

public class PageInverter {

    ArrayList<Page> pages = new ArrayList<>();

    /**
     * Inverts the order of the pages in the provided list.
     * <p>
     * This method takes an {@link ArrayList} of {@link Page} objects, reverses their order,
     * and returns the inverted list. The original list is modified in place.
     * </p>
     *
     * @param pages the list of pages to be inverted
     * @return the inverted list of pages
     */
    public ArrayList<Page> PageInverter(ArrayList<Page> pages) {
        Collections.reverse(pages);
        return pages;
    }

}
