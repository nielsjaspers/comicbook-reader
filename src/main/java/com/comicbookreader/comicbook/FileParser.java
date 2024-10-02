package com.comicbookreader.comicbook;

import java.io.IOException;
import java.util.List;

public interface FileParser {
    List<Page> extractPages(String path) throws IOException;
}
