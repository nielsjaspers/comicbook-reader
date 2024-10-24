package com.comicbookreader.comicbook;

import java.awt.image.BufferedImage;
import java.util.List;

public class Page {
    public int number;
    public String path;
    public BufferedImage image;
    public List<Annotation> annotationsList;

    public Page(int number, String path, BufferedImage image) {
        this.number = number;
        this.path = path;
        this.image = image;
    }
    public Page() {}
}
