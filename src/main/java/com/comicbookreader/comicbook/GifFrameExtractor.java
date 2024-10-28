package com.comicbookreader.comicbook;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class GifFrameExtractor {

    public ArrayList<Page> extractFrames(ImageInputStream imageStream, String baseFileName, int startingPageIndex) throws IOException {
        ArrayList<Page> gifPages = new ArrayList<>();
        Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("gif");

        if (readers.hasNext()) {
            ImageReader reader = readers.next();
            reader.setInput(imageStream, false);

            for (int frameIndex = 0; frameIndex < reader.getNumImages(true); frameIndex++) {
                BufferedImage frame = reader.read(frameIndex);
                if (frame != null) {
                    gifPages.add(new Page(startingPageIndex++, baseFileName + "_frame" + frameIndex, frame));
                }
            }
            reader.dispose();
        }
        return gifPages;
    }
}
