package com.github.axet.lookup.common;

import java.awt.image.BufferedImage;

public abstract class ImageBinaryScale {

    public ImageBinary image;
    public BufferedImage scaleBuf;
    public ImageBinary scaleBin;

    public double s = 0;

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

    abstract public void rescale();
}
