package com.github.axet.lookup.fnnc;

import java.awt.image.BufferedImage;

public class Integral extends IntegralArray {

    public Integral(BufferedImage buf) {
        super(buf);

        cx = buf.getWidth();
        cy = buf.getHeight();
        s = new int[cx * cy];

        for (int x = 1; x < cx; x++) {
            for (int y = 1; y < cy; y++) {
                s[y * cx + x] = f(x, y) + s(x - 1, y) + s(x, y - 1) + s(x - 1, y - 1);
            }
        }
    }
}
