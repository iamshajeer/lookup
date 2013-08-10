package com.github.axet.lookup.common;

import java.awt.image.BufferedImage;

import com.github.axet.lookup.Lookup;

/**
 * mix RGB and Grey for memory saving
 * 
 * Scaled image is RGB and Full Scale is Gray
 * 
 * @author axet
 * 
 */
public class ImageBinaryGreyScaleRGB extends ImageBinaryScale {

    public ImageBinaryGreyScaleRGB(BufferedImage i) {
        image = new ImageBinaryGrey(i);
    }

    /**
     * 
     * @param i
     * @param scaleSize
     *            template scale size in pixels you wish. (ex: 5)
     */
    public ImageBinaryGreyScaleRGB(BufferedImage i, int scaleSize) {
        image = new ImageBinaryRGB(i);

        rescale(scaleSize);
    }

    public ImageBinaryGreyScaleRGB(BufferedImage i, double scale) {
        image = new ImageBinaryRGB(i);

        s = scale;

        rescale();
    }

    public void rescale() {
        scaleBuf = Lookup.scale(image.getImage(), s);
        scaleBin = new ImageBinaryRGB(scaleBuf);
    }
}
