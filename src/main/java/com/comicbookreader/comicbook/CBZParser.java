package com.comicbookreader.comicbook;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class CBZParser implements FileParser {
    ArrayList<Page> pages = new ArrayList<>();

    @Override
    public ArrayList<Page> extractPages(String path) {
        try(ZipInputStream zipIn = new ZipInputStream(new FileInputStream(path))) {
            ZipEntry entry;
            int i = 0;
            while ((entry = zipIn.getNextEntry()) != null){
                if (isImageFile(entry.getName())){
//                    System.out.println("Reading and displaying: " + entry.getName());
                    BufferedImage img = ImageIO.read(zipIn);
//                    System.out.println("img = " + img);
                    pages.add(new Page(i, entry.getName(), img));
                    i++;
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
        return lowerCaseFileName.endsWith(".jpg") || lowerCaseFileName.endsWith(".jpeg") || lowerCaseFileName.endsWith(".png");
    }
}