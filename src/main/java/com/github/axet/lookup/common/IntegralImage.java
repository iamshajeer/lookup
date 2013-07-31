package com.github.axet.lookup.common;

public class IntegralImage extends SArray {

    public IntegralImage(SArray buf) {
        super(buf);

        for (int x = 0; x < cx; x++) {
            for (int y = 0; y < cy; y++) {
                s[i(x, y)] = buf.s(x, y) + s(x - 1, y) + s(x, y - 1) - s(x - 1, y - 1);
            }
        }
    }
}
