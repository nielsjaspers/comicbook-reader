package com.comicbookreader.comicbook;

import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.rarfile.FileHeader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;

public class CBRParser implements FileParser {
    private final String DESTINATION_FOLDER = "imported_comics/unzipped_rar";

    public ArrayList<ZipEntry> pages;

    @Override
    public void extractPages(String path) throws IOException {
        File archive = new File(path);
        File destination = new File(DESTINATION_FOLDER);

        if (!destination.exists()) {
            destination.mkdirs();
        }

        try (Archive rar = new Archive(archive)){
            FileHeader fileHeader;
            while((fileHeader = rar.nextFileHeader()) != null) {
                if(!fileHeader.isDirectory()) {
                    File extractedFile = new File(DESTINATION_FOLDER + File.separator + fileHeader.getFileNameString().trim());
                    try(FileOutputStream fos = new FileOutputStream(extractedFile)){
                        rar.extractFile(fileHeader, fos);
                    } catch (RarException e) {
                        System.err.println("Error extracting file:" + e.getMessage());
                    }
                }
            }
        } catch (RarException e) {
            System.err.println("Error opening .rar archive:" + e.getMessage());
        }
    }

}
