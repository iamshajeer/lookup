package com.github.axet.lookup.common;

public class ImageZeroMean extends SArray {

    public ImageZeroMean(SArray s1) {
        SArray image = s1.base;

        cx = s1.cx;
        cy = s1.cy;

        s = new double[cx * cy];

        double m = s1.mean();

        for (int x = 0; x < cx; x++) {
            for (int y = 0; y < cy; y++) {
                s(x, y, image.s(x, y) - m);
            }
        }
    }
}
