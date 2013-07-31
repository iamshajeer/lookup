package com.github.axet.lookup.common;

public class ImageMultiply extends SArray {

    public ImageMultiply(ImageZeroMean s1, ImageZeroMean s2) {
        this(s1, 0, 0, s2);
    }

    public ImageMultiply(ImageZeroMean image, int xx, int yy, ImageZeroMean template) {
        cx = template.cx;
        cy = template.cy;

        s = new double[cx * cy];

        for (int x = 0; x < cx; x++) {
            for (int y = 0; y < cy; y++) {
                s[i(x, y)] = (image.s(xx + x, yy + y) * template.s(x, y));
            }
        }
    }
}
