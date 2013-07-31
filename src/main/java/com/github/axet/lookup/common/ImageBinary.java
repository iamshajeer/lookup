package com.github.axet.lookup.common;

import java.awt.image.BufferedImage;

public class ImageBinary {

    public GrayImage gi;
    public IntegralImage image;
    public IntegralImage2 image2;
    public ImageZeroMean zeroMean;

    public ImageBinary(BufferedImage img) {
        gi = new GrayImage(img);
        image = new IntegralImage(gi);
        image2 = new IntegralImage2(gi);
        zeroMean = new ImageZeroMean(image);
    }

    public double dev() {
        return image2.dev(image);
    }

    public double dev(int x1, int y1, int x2, int y2) {
        return image2.dev(image, x1, y1, x2, y2);
    }

    public int getWidth() {
        return gi.cx;
    }

    public int getHeight() {
        return gi.cy;
    }

}
