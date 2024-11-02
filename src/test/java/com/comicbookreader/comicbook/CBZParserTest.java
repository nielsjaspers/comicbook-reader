package com.comicbookreader.comicbook;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class CBZParserTest {
    private CBZParser parser;
    private String validCbzPath;
    private String invalidCbzPath;

    @BeforeEach
    public void setUp() {
        parser = new CBZParser();
        validCbzPath = "imported_comics/Deadpool Team-Up 002 (2024) (Digital) (Shan-Empire).cbz";
        invalidCbzPath = "invalid/path/comic.cbz"; // Ongeldig pad
    }

    @Test
    public void testExtractPages_validCbzFile() {
        ArrayList<Page> pages = parser.extractPages(validCbzPath);

        // Gebruikte cbz zou 19 pagina's moeten bevatten
        assertEquals(19, pages.size());

        for (int i = 0; i < pages.size(); i++) {
            assertNotNull(pages.get(i).image); // Controleer of afbeeldingen zijn geladen
            assertEquals(i, pages.get(i).number); // Controleer het paginanummer
        }
    }

    @Test
    public void testExtractPages_invalidPath() {
        ArrayList<Page> pages = parser.extractPages(invalidCbzPath);

        assertTrue(pages.isEmpty()); // Controleer of er geen pagina's zijn gevonden
    }

    @Test
    public void testExtractPages_withGifFile() {
        // Hier zou je een test CBZ-bestand moeten hebben dat een GIF bevat.
        ArrayList<Page> pages = parser.extractPages("imported_comics/pepper&carrot_1.nhlcomic");

        // Controleer of de pagina's correct zijn verwerkt.
        assertTrue(!pages.isEmpty()); // We verwachten ten minste één pagina te hebben
        for (Page page : pages) {
            assertNotNull(page.image); // Controleer dat elke pagina een afbeelding heeft
        }
    }
}
