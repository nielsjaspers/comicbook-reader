package com.comicbookreader.comicbook;

import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.rarfile.FileHeader;

import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class CBRParser implements FileParser {
    private final String DESTINATION_FOLDER = "imported_comics/unzipped_rar";

    public ArrayList<Page> pages = new ArrayList<>();
    private BufferedImage image;

    @Override
    public ArrayList<Page> extractPages(String path) throws IOException {
        File archive = new File(path);
        File destination = new File(DESTINATION_FOLDER);

        int pagenumber = 0;

        if (!destination.exists()) {
            destination.mkdirs();
        }

        try (Archive rar = new Archive(archive)){
            FileHeader fileHeader;
            while((fileHeader = rar.nextFileHeader()) != null) {
                if(!fileHeader.isDirectory()) {
                    File extractedFile = new File(DESTINATION_FOLDER + File.separator + fileHeader.getFileName().trim());
                    try(FileOutputStream fos = new FileOutputStream(extractedFile)){
                        rar.extractFile(fileHeader, fos);
                        try {
                            image = ImageIO.read(extractedFile);
                            pages.add(new Page(pagenumber++, extractedFile.getAbsolutePath(), image));
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

        for (Page page : pages) {
            System.out.println("page.path = " + page.path);
        }
        return pages;
    }

    public void cleanup() throws IOException {
        File destination = new File(DESTINATION_FOLDER);
        FileUtils.deleteDirectory(destination);
    }
}
