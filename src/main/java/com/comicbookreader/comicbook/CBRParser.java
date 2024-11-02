package com.comicbookreader.comicbook;

import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.rarfile.FileHeader;

import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

public class CBRParser implements FileParser {
    private String destinationFolder = "imported_comics/unzipped_rar";

    public ArrayList<Page> pages = new ArrayList<>();

    /**
     * Extracts pages from a specified .rar archive.
     * <p>
     * This method extracts image files from the given .rar archive path, saves them in a designated
     * destination folder, and creates {@link Page} objects for each image found.
     * If the specified file path does not exist, a {@link FileNotFoundException} is thrown.
     * </p>
     * <p>
     * During extraction, each file is checked to ensure it is not a directory, then extracted to the destination.
     * If the extracted file is an image, it is loaded into memory and added to the list of pages.
     * </p>
     *
     * @param path the file path to the .rar archive containing comic pages
     * @return an {@link ArrayList} of {@link Page} objects representing each extracted image
     * @throws IOException if an I/O error occurs while extracting or reading the images
     */
    @Override
    public ArrayList<Page> extractPages(String path) throws IOException {
        if(!new File(path).exists()) {
            throw new FileNotFoundException(path);
        }

        File archive = new File(path);
        File destination = new File(destinationFolder + "/" + new File(path).getName().trim());

        int pagenumber = 0;

        if (!destination.exists()) {
            destination.mkdirs();
        }

        try (Archive rar = new Archive(archive)){
            FileHeader fileHeader;
            while((fileHeader = rar.nextFileHeader()) != null) {
                if(!fileHeader.isDirectory()) {
                    File extractedFile = new File(destination + File.separator + fileHeader.getFileName().trim());
                    try(FileOutputStream fos = new FileOutputStream(extractedFile)){
                        rar.extractFile(fileHeader, fos);
                        try {
                            BufferedImage image = ImageIO.read(extractedFile);
                            pages.add(new Page(pagenumber++, extractedFile.toString(), image));
                        } catch (IOException e) {
                            System.err.println("Error reading image " + extractedFile.getAbsolutePath());
                        }
                    } catch (RarException e) {
                        System.err.println("Error extracting file:" + e.getMessage());
                    }
                }
            }
        } catch (RarException e) {
            System.err.println("Error opening .rar archive:" + e.getMessage());
        }

        return pages;
    }

    /**
     * Cleans up the specified destination folder by deleting it and all its contents.
     * <p>
     * This method attempts to remove the directory specified by {@code destinationFolder}.
     * If the directory does not exist or cannot be deleted, an {@link IOException} is thrown.
     * </p>
     *
     * @throws IOException if an error occurs during the deletion of the directory.
     */
    public void cleanup() throws IOException {
        File destination = new File(destinationFolder);
        FileUtils.deleteDirectory(destination);
    }
}
