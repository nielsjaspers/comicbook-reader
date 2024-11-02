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

    public void cleanup() throws IOException {
        File destination = new File(destinationFolder);
        FileUtils.deleteDirectory(destination);
    }
}
