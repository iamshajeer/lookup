package com.github.axet.lookup.fnnc;

import java.awt.image.BufferedImage;

public class IntegralArray extends SArray {
    BufferedImage buf;

    public int cx;
    public int cy;

    public int s[];

    int f(int x, int y) {
        return buf.getRGB(x, y);
    }

    public IntegralArray(BufferedImage buf) {
        this.buf = buf;
        cx = buf.getWidth();
        cy = buf.getHeight();
        s = new int[cx * cy];
    }

}
