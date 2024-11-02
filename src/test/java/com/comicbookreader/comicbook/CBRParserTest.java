package com.comicbookreader.comicbook;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class CBRParserTest {
    private CBRParser parser;
    private String testRarPath;
    private String invalidRarPath;

    @Before
    public void setUp() {
        parser = new CBRParser();
        testRarPath = "imported_comics/Origin of Galactus v1 001 (1996-02).cbr";
        invalidRarPath = "/invalid/path/comic.rar"; // Path that does not exist
    }

    @After
    public void tearDown() throws IOException {
        parser.cleanup();
    }


    @Test
    public void testExtractPages_validRarFile() throws IOException {
        ArrayList<Page> pages = parser.extractPages(testRarPath);

        // Used rar has 36 pages
        assertEquals(36, pages.size());
        for (int i = 0; i < pages.size(); i++) {
            assertNotNull(pages.get(i).image); // Check that images are loaded
            assertEquals(i, pages.get(i).number); // Check the page number
        }
    }

    @Test(expected = FileNotFoundException.class)
    public void testExtractPages_invalidPath() throws IOException {
        parser.extractPages(invalidRarPath); // This should throw FileNotFoundException
    }

    @Test
    public void testCleanup_removesExtractedFiles() throws IOException {
        parser.extractPages(testRarPath); // Extract pages first
        File destinationFolder = new File("imported_comics/unzipped_rar");

        assertTrue(destinationFolder.exists()); // Ensure folder exists before cleanup

        parser.cleanup();

        assertFalse(destinationFolder.exists()); // Ensure folder is deleted after cleanup
    }

}
