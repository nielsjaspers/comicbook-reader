package com.comicbookreader.comicbook;

import java.io.IOException;

public interface FileParser {
    void extractPages(String path) throws IOException;
}
