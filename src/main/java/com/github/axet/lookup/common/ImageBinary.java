package com.github.axet.lookup.common;

import java.awt.image.BufferedImage;

public class ImageBinary {

    public GrayImage gi;
    public IntegralImage image;
    public IntegralImage2 image2;
    public ImageZeroMean zeroMean;

    public ImageBinary(BufferedImage img) {
        // gi = new GrayImage(img);
        // image = new IntegralImage(gi);
        // image2 = new IntegralImage2(gi);

        // speedup

        gi = new GrayImage();
        image = new IntegralImage();
        image2 = new IntegralImage2();

        this.gi.init(img);
        this.image.init(gi);
        this.image2.init(gi);

        for (int x = 0; x < this.gi.cx; x++) {
            for (int y = 0; y < this.gi.cy; y++) {
                this.gi.step(x, y);
                this.image.step(x, y);
                this.image2.step(x, y);
            }
        }

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

    public int size() {
        return gi.cx * gi.cy;
    }

    public BufferedImage getImage() {
        return gi.buf;
    }

}
