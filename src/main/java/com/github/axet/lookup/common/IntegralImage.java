package com.github.axet.lookup.common;

import java.awt.image.BufferedImage;

public class IntegralImage extends IntegralArray {

    public IntegralImage(BufferedImage buf) {
        super(buf);

        for (int x = 1; x < cx; x++) {
            for (int y = 1; y < cy; y++) {
                s[y * cx + x] = f(x, y) + s(x - 1, y) + s(x, y - 1) + s(x - 1, y - 1);
            }
        }
    }
}
