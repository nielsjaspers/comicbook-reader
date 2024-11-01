package com.comicbookreader.comicbook;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.awt.image.BufferedImage;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Page {
    public int number;
    public String path;
    @JsonIgnore
    public BufferedImage image;
    public List<Annotation> annotationsList;

    public Page(int number, String path, BufferedImage image) {
        this.number = number;
        this.path = path;
        this.image = image;
    }

    public Page(){}

    public int getCurrentPageNumber() {
        return number;
    }

    public String getPath() {
        return path;
    }
}

