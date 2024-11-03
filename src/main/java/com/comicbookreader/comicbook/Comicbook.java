package com.comicbookreader.comicbook;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.awt.image.BufferedImage;
import java.io.File;
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
    public Comicbook comicbook;
    ObjectMapper mapper = new ObjectMapper();
    private static final String DATA_FILE_PATH = "appdata/data.json";


    public Comicbook(String name, List<Page> pages, boolean invert) {
        this.name = name;
        this.pages = new ArrayList<>(pages);
        if (invert) {
            this.pages = invertPages();
        }
    }


    public Comicbook(String name, List<Page> pages) {
        this.name = name;
        this.pages = new ArrayList<>(pages);
    }

    public Comicbook(String name, List<Page> pages, String path) {
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

    public boolean getIsRead() {
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
     * @param pages    the list of {@link Page} objects associated with the comic book
     * @param path     the original path where the comic book is located
     * @return a new {@link Comicbook} instance
     */
    public static Comicbook fromFilePath(String filePath, List<Page> pages, String path) {
        String name = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.lastIndexOf("."));
        System.out.println(name);
        return new Comicbook(name, pages, path);
    }

    public boolean getIsFavourite() {
        return favourite;
    }

    public String getPath() {
        return path;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPages(ArrayList<Page> pages) {
        this.pages = pages;
    }

    public void setProgression(int progression) {
        if (progression < pages.size()) {
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

    public void setPath(String path) {
        this.path = path;
    }
    /**
     * Retrieves the progression of a specified comic book.
     * <p>
     * This method loads the existing comic books from a JSON file and searches for the comic book
     * with the specified name. If found, it returns the progression (current page number) of that
     * comic book. If the comic book is not found or an error occurs during the loading process,
     * it returns -1.
     * </p>
     *
     * @param comicName the name of the comic book whose progression is to be retrieved.
     *                  This name is used to locate the corresponding comic book in the JSON file.
     *                  The comparison is case-sensitive and can match exact names or names that
     *                  start with the specified name followed by a dot.
     *
     * @return the current progression (page number) of the specified comic book, or -1 if the
     *         comic book is not found or if an error occurs during the file operations.
     *
     * @throws IOException if there is an error reading from the JSON file. This includes issues such as
     *                     file not found, read permissions, or JSON parsing errors.
     */
    public int getComicbookProgression(String comicName) {
        try {
            // Load existing comicbooks from the JSON file
            ArrayList<Comicbook> comicbooks = mapper.readValue(new File(DATA_FILE_PATH), new TypeReference<ArrayList<Comicbook>>() {
            });

            // Search for the specific comicbook by name
            for (Comicbook comic : comicbooks) {
                if (comic.getName().equals(comicName) || comic.getName().startsWith(comicName + ".")) {
                    // Return the progression of the found comicbook
                    return comic.getProgression();
                }
            }

            // Return -1 or another value to indicate that the comicbook was not found
            System.out.println("Comicbook not found: " + comicName);
            return -1; // or throw an exception if preferred

        } catch (IOException e) {
            System.err.println("Error reading comicbook data: " + e.getMessage());
            return -1; // or handle the error as preferred
        }
    }
    /**
     * Updates the progression of a specified comic book to the given page number.
     * <p>
     * This method loads the existing comic books from a JSON file and searches for the comic book
     * with the specified name. If found, it updates the progression of that comic book to the
     * specified page number. After updating, it saves the modified list of comic books back to the
     * JSON file.
     * </p>
     *
     * @param comicName the name of the comic book whose progression is to be updated.
     *                  This name is used to locate the corresponding comic book in the JSON file.
     *                  The comparison is case-sensitive and can match exact names or names that
     *                  start with the specified name followed by a dot.
     * @param pageNumber the page number to which the comic book's progression should be updated.
     *                   This value is expected to be a non-negative integer and should not exceed
     *                   the total number of pages in the comic book.
     *
     * @throws IOException if there is an error reading from the JSON file or writing the updated
     *                     comic book list back to the file. This includes issues such as file not
     *                     found, read/write permissions, or JSON parsing errors.
     *
     */
    public void setComicbookProgression(String comicName, int pageNumber) {
        try {
            // Load existing comicbooks
            ArrayList<Comicbook> comicbooks = mapper.readValue(new File(DATA_FILE_PATH), new TypeReference<ArrayList<Comicbook>>() {
            });

            boolean found = false; // Flag to check if the comic is found

            // Update the progression for the specific comicbook
            for (Comicbook comic : comicbooks) {
                String jsonComicName = comic.getName();
                if (jsonComicName.equals(comicName) || jsonComicName.startsWith(comicName + ".")) {
                    comic.setProgression(pageNumber);
                    found = true;
                    System.out.println("Updated progression for: " + jsonComicName);
                    break;
                }
            }
            // Save updated comicbook list back to JSON
            mapper.writeValue(new File(DATA_FILE_PATH), comicbooks);
            System.out.println("Comicbook progression saved successfully.");

            if (!found) {
                System.out.println("Comicbook not found for name: " + comicbook.getName());
            }

        } catch (IOException e) {
            System.err.println("Error saving progression: " + e.getMessage());
        }
    }
}

