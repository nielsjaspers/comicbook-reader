package com.comicbookreader.comicbook;

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

    private static boolean isImageFile(String fileName) {
        String lowerCaseFileName = fileName.toLowerCase();
        return lowerCaseFileName.endsWith(".jpg") ||
                lowerCaseFileName.endsWith(".jpeg") ||
                lowerCaseFileName.endsWith(".png") ||
                lowerCaseFileName.endsWith(".gif");
    }
}