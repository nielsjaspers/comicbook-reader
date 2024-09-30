package com.comicbookreader.comicbook;

import java.util.List;

public class Series {
    public String name;
    public List<Comicbook> comicbookList;
    public int comicbookCount;
    public boolean isCompleted;
    public List<String> categories;

    public Series(String name, List<Comicbook> comicbookList) {}
}
