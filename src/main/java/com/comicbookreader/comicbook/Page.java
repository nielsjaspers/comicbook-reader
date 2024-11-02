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

    /**
     * Creates a new {@link Page} instance with a specified number, path, and image.
     *
     * @param number the page number
     * @param path the file path of the image associated with the page
     * @param image the {@link BufferedImage} representation of the page
     */
    public Page(int number, String path, BufferedImage image) {
        this.number = number;
        this.path = path;
        this.image = image;
    }

    /**
     * Creates a new {@link Page} instance with only the specified image.
     *
     * @param image the {@link BufferedImage} representation of the page
     */
    public Page(BufferedImage image) {
        this.image = image;
    }

    /**
     * Creates a new empty {@link Page} instance.
     * <p>
     * This constructor initializes a {@link Page} object without setting any properties.
     * </p>
     */
    public Page(){}

    public int getCurrentPageNumber() {
        return number;
    }

    public String getPath() {
        return path;
    }
}

