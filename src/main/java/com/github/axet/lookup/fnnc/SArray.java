package com.github.axet.lookup.fnnc;

public class SArray {
    public int cx;
    public int cy;

    public int s[];

    int s(int x, int y) {
        return s[y * cx + cx];
    }

    public int sigma(int x1, int y1, int x2, int y2){
        return s(x2, y2) - s(x1, y1);
    }

}
