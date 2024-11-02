package com.comicbookreader.comicbook;

import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class CBZParser implements FileParser {

    private ArrayList<Page> pages = new ArrayList<>();
    private GifFrameExtractor gifFrameExtractor = new GifFrameExtractor();

    /**
     * Extracts image pages from a specified zip archive.
     * <p>
     * This method reads images from a zip file at the given path. It processes each entry in the zip,
     * and if the entry is an image file, it adds it to the list of pages. If the entry is a GIF file,
     * it utilizes a {@link GifFrameExtractor} to extract individual frames from the GIF.
     * </p>
     *
     * @param path the file path to the zip archive containing image pages
     * @return an {@link ArrayList} of {@link Page} objects representing each extracted image page
     */
    @Override
    public ArrayList<Page> extractPages(String path) {
        try(ZipInputStream zipIn = new ZipInputStream(new FileInputStream(path))) {
            ZipEntry entry;
            int pageNumber = 0;

            while ((entry = zipIn.getNextEntry()) != null){
                String fileName = entry.getName();

                if (isImageFile(fileName)){
                    if (fileName.toLowerCase().endsWith(".gif")){
                        ImageInputStream imageStream = ImageIO.createImageInputStream(zipIn);
                        ArrayList<Page> gifPages = gifFrameExtractor.extractFrames(imageStream, fileName, pageNumber);
                        pages.addAll(gifPages);
                        pageNumber += gifPages.size();
                    }else {
                        BufferedImage img = ImageIO.read(zipIn);
                        if (img != null) {
                            pages.add(new Page(pageNumber++, fileName, img));
                        }
                    }
                }
                zipIn.closeEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pages;
    }

    /**
     * Checks if a given file name corresponds to a valid image file format.
     * <p>
     * This method evaluates the file name by checking its extension against common image formats:
     * JPG, JPEG, PNG, and GIF. The file name is converted to lowercase to ensure the check is case-insensitive.
     * </p>
     *
     * @param fileName the name of the file to check
     * @return {@code true} if the file name has a valid image extension; {@code false} otherwise
     */
    private static boolean isImageFile(@NotNull String fileName) {
        String lowerCaseFileName = fileName.toLowerCase();
        return lowerCaseFileName.endsWith(".jpg") ||
                lowerCaseFileName.endsWith(".jpeg") ||
                lowerCaseFileName.endsWith(".png") ||
                lowerCaseFileName.endsWith(".gif");
    }
}