package com.comicbookreader.comicbook;

import com.comicbookreader.comicbook.Comicbook;
import com.comicbookreader.comicbook.DirectoryScanner;
import com.comicbookreader.user.Userdata;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DirectoryScannerTest {

    private static final String TEST_DIRECTORY = "testDirectory";
    private static final String TEST_USERDATA_FILE = "userdata/data.json";
    private static final String TEST_APPDATA_FILE = "appdata/data.json";
    private static ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() throws IOException {
        // Setup a temporary directory for tests
        Files.createDirectories(Paths.get(TEST_DIRECTORY));
        // Create userdata.json for testing
        Userdata userdata = new Userdata();
        userdata.setHadInitialScan(false);
        mapper.writeValue(new File(TEST_USERDATA_FILE), userdata);
    }

    @AfterEach
    void tearDown() throws IOException {
        // Cleanup test directory and files
        Files.walk(Paths.get(TEST_DIRECTORY))
                .sorted((a, b) -> b.compareTo(a)) // Reverse order to delete files first
                .map(Path::toFile)
                .forEach(File::delete);
        new File(TEST_USERDATA_FILE).delete();
        new File(TEST_APPDATA_FILE).delete();
    }

    @Test
    void testGetComicFilePaths_withValidFiles() throws IOException {
        // Prepare test files
        Files.createFile(Paths.get(TEST_DIRECTORY, "testComic.cbz"));
        Files.createFile(Paths.get(TEST_DIRECTORY, "testComic.cbr"));

        List<String> comicFiles = DirectoryScanner.getComicFilePaths(Paths.get(TEST_DIRECTORY), ".cbz", ".cbr");

        assertEquals(2, comicFiles.size());
        assertTrue(comicFiles.contains(TEST_DIRECTORY + "/testComic.cbz"));
        assertTrue(comicFiles.contains(TEST_DIRECTORY + "/testComic.cbr"));
    }

    @Test
    void testGetComicFilePaths_withEmptyDirectory() throws IOException {
        List<String> comicFiles = DirectoryScanner.getComicFilePaths(Paths.get(TEST_DIRECTORY), ".cbz", ".cbr");
        assertTrue(comicFiles.isEmpty());
    }

    @Test
    void testGetComicFilePaths_withInvalidExtensions() throws IOException {
        // Prepare test files
        Files.createFile(Paths.get(TEST_DIRECTORY, "testComic.txt"));

        List<String> comicFiles = DirectoryScanner.getComicFilePaths(Paths.get(TEST_DIRECTORY), ".pdf");

        assertTrue(comicFiles.isEmpty());
    }

    @Test
    void testInitialScanUpdatesUserdata() throws IOException {
        // Prepare test files
        Files.createFile(Paths.get(TEST_DIRECTORY, "testComic.cbz"));

        DirectoryScanner.getComicFilePaths(Paths.get(TEST_DIRECTORY), ".cbz");

        // Load the updated userdata
        Userdata userdata = mapper.readValue(new File(TEST_USERDATA_FILE), Userdata.class);
        assertTrue(userdata.getHadInitialScan());
    }

    @Test
    void testGetComicFilePaths_withExistingAppData() throws IOException {
        // Prepare test files
        Files.createFile(Paths.get(TEST_DIRECTORY, "testComic.cbz"));

        // Simulate existing app data
        mapper.writeValue(new File(TEST_APPDATA_FILE), List.of(new Comicbook("testComic.cbz", List.of(), TEST_DIRECTORY + "/testComic.cbz")));

        Userdata userdata = new Userdata();
        userdata.setHadInitialScan(true);
        mapper.writeValue(new File(TEST_USERDATA_FILE), userdata);

        List<String> comicFiles = DirectoryScanner.getComicFilePaths(Paths.get(TEST_DIRECTORY), ".cbz");

        assertEquals(1, comicFiles.size());
        assertEquals(TEST_DIRECTORY + "/testComic.cbz", comicFiles.get(0));
    }

    @Test
    void testGetComicFilePaths_withNonExistentUserdata() {
        // Verwijder het userdata bestand
        new File(TEST_USERDATA_FILE).delete();

        IOException exception = assertThrows(IOException.class, () -> {
            DirectoryScanner.getComicFilePaths(Paths.get(TEST_DIRECTORY), ".cbz");
        });

        // Controleer of de foutboodschap relevant is
        String expectedMessage = "Cannot read userdata"; // Pas dit aan naar wat de werkelijke foutmelding is
        assertTrue(exception.getMessage().contains(expectedMessage),
                "De foutboodschap komt niet overeen met de verwachte boodschap.");
    }

}
