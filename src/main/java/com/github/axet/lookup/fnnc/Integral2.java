package com.github.axet.lookup.fnnc;

import java.awt.image.BufferedImage;

public class Integral2 extends IntegralArray {

    static public int pow2(int x) {
        return x * x;
    }

    public Integral2(BufferedImage buf) {
        super(buf);

        cx = buf.getWidth();
        cy = buf.getHeight();
        s = new int[cx * cy];

        for (int x = 1; x < cx; x++) {
            for (int y = 1; y < cy; y++) {
                s[y * cx + x] = pow2(f(x, y)) + pow2(s(x - 1, y)) + pow2(s(x, y - 1)) + pow2(s(x - 1, y - 1));
            }
        }
    }
}
