package com.github.axet.lookup.common;

import java.awt.image.BufferedImage;

import com.github.axet.lookup.Lookup;

public class ImageBinaryGreyScale {

    public ImageBinaryGrey image;
    public BufferedImage scaleBuf;
    public ImageBinaryGrey scaleBin;

    public double s = 0;

    public ImageBinaryGreyScale(BufferedImage i) {
        image = new ImageBinaryGrey(i);
    }

    /**
     * 
     * @param i
     * @param scaleSize
     *            template scale size in pixels you wish. (ex: 5)
     */
    public ImageBinaryGreyScale(BufferedImage i, int scaleSize) {
        image = new ImageBinaryGrey(i);

        rescale(scaleSize);
    }

    public ImageBinaryGreyScale(BufferedImage i, double scale) {
        image = new ImageBinaryGrey(i);

        s = scale;

        rescale();
    }

    public void rescale(int s) {
        rescale(project(s));
    }

    public double project(int s) {
        double m = (double) Math.min(image.getWidth(), image.getHeight());
        double q = m / s;

        q = Math.ceil(q);

        q = 1 / q;

        return q;
    }

    public void rescale(double s) {
        this.s = s;

        rescale();
    }

    public void rescale() {
        scaleBuf = Lookup.scale(image.getImage(), s);
        scaleBin = new ImageBinaryGrey(scaleBuf);
    }
}
