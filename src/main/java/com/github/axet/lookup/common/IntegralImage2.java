package com.github.axet.lookup.common;

import java.awt.image.BufferedImage;

public class IntegralImage2 extends IntegralArray {

    static public int pow2(int x) {
        return x * x;
    }

    public IntegralImage2(BufferedImage buf) {
        super(buf);

        for (int x = 1; x < cx; x++) {
            for (int y = 1; y < cy; y++) {
                s[y * cx + x] = pow2(f(x, y)) + pow2(s(x - 1, y)) + pow2(s(x, y - 1)) + pow2(s(x - 1, y - 1));
            }
        }
    }
}
