package com.comicbookreader.comicbook;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Comicbook {
    public String name;
    public ArrayList<Author> authorList;
    public ArrayList<Page> pages;
    @JsonIgnore
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

    public Comicbook(String name, List<Page>pages, String path) {
        this.name = name;
        this.pages = new ArrayList<>(pages);
        this.path = path;
    }

    public Comicbook() {
    }

    public Comicbook(List<Page> pages) {
        this.pages = new ArrayList<>(pages);
    }


    public ArrayList<Page> invertPages() {
        Collections.reverse(pages);
        return pages;
    }

    /**
     * Loads pages from a comic archive if the pages list is empty.
     * <p>
     * This method checks if the {@code pages} list is empty and, if so, determines the archive type
     * based on the file name extension. It then uses either a {@link CBRParser} or {@link CBZParser}
     * to extract pages from the specified path. If an error occurs during the extraction, it prints
     * an error message to the standard error output.
     * </p>
     */
    public void loadPages() {
        if (pages.isEmpty()) {
            try {
                this.pages = name.endsWith(".cbr")
                        ? new CBRParser().extractPages(this.path)
                        : new CBZParser().extractPages(this.path);
            } catch (IOException e) {
                System.err.println("Error reloading pages: " + e.getMessage());
            }
        }
    }

    // getters & setters
    public String getName() {
        return name;
    }

    public ArrayList<Page> getPages() {
        return pages;
    }

    public int getProgression() {
        return progression;
    }

    public boolean getIsRead(){
        return read;
    }

    /**
     * Creates a {@link Comicbook} instance from a specified file path.
     * <p>
     * This static factory method extracts the comic book name from the provided file path by
     * taking the substring between the last slash and the file extension. It then constructs
     * and returns a new {@link Comicbook} object using the extracted name, the provided list of
     * pages, and the original path.
     * </p>
     *
     * @param filePath the full file path of the comic book file
     * @param pages the list of {@link Page} objects associated with the comic book
     * @param path the original path where the comic book is located
     * @return a new {@link Comicbook} instance
     */
    public static Comicbook fromFilePath(String filePath, List<Page> pages, String path) {
        String name = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.lastIndexOf("."));
        System.out.println(name);
        return new Comicbook(name, pages, path);
    }

    public boolean getIsFavourite(){
        return favourite;
    }

    public String getPath() {
        return path;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setPages(ArrayList<Page> pages) {
        this.pages = pages;
    }

    public void setProgression(Page page ,int progression){
        int currentpage = page.getCurrentPageNumber();

        if(currentpage < pages.size()){
            this.progression = progression;
            return;
        }
        setIsRead(true);
    }

    public void setIsRead(boolean read) {
        this.read = read;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public void setPath(String path){
        this.path = path;
    }

}

