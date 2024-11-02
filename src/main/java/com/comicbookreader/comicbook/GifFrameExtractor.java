package com.comicbookreader.comicbook;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class GifFrameExtractor {

    /**
     * Extracts individual frames from a GIF image stream.
     * <p>
     * This method takes an {@link ImageInputStream} representing a GIF file and extracts all frames,
     * returning them as a list of {@link Page} objects. Each frame is assigned a unique filename based
     * on the provided base file name and its frame index, along with a corresponding page number.
     * </p>
     *
     * @param imageStream the input stream of the GIF image to extract frames from
     * @param baseFileName the base name used for naming the extracted frames
     * @param startingPageIndex the index from which to start numbering the extracted frames
     * @return an {@link ArrayList} of {@link Page} objects, each representing a frame of the GIF
     * @throws IOException if an error occurs while reading the frames from the image stream
     */
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
