package com.github.axet.lookup.fnnc;

public class SArray {
    public int cx;
    public int cy;

    public int s[];

    int s(int x, int y) {
        if (x < 0)
            return 0;
        if (y < 0)
            return 0;
        return s[y * cx + x];
    }

    /**
     * Math Sigma (sum of the blocks)
     * 
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public int sigma(int x1, int y1, int x2, int y2) {
        int a = x1 > 0 && y1 > 0 ? s(x1 - 1, y1 - 1) : 0;
        int b = y1 > 0 ? s(x2, y1 - 1) : 0;
        int c = x1 > 0 ? s(x1 - 1, y2) : 0;
        int d = s(x2, y2);
        return a - b - c + d;
    }
}
