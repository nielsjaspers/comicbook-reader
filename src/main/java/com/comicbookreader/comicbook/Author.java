package com.comicbookreader.comicbook;

import java.util.ArrayList;
import java.util.List;

public class Author {
    public String name;
    public List<Comicbook> comicbookList;

    public Author(String name, List<Comicbook> comicbookList) {
        this.name = name;
        this.comicbookList = comicbookList;
    }

    public String getName() {
        return name;
    }

    public List<Comicbook> getComicbookList() {
        return comicbookList;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setComicbookList(List<Comicbook> comicbookList) {
        this.comicbookList = comicbookList;
    }

    public void deleteAuthor() {
        name = null;
        comicbookList.remove(this);
    }
}
