package com.github.axet.lookup.common;

public class SArray {
    public int cx;
    public int cy;

    public int s[];

    public int s(int x, int y) {
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
        int a = s(x1 - 1, y1 - 1);
        int b = s(x2, y1 - 1);
        int c = s(x1 - 1, y2);
        int d = s(x2, y2);
        return a - b - c + d;
    }

    public void printDebug() {
        for (int y = 0; y < cy; y++) {
            for (int x = 0; x < cx; x++) {
                System.out.print(s(x, y));
                System.out.print("\t");
            }
            System.out.println("");
        }
    }
}
