package com.github.axet.lookup.common;

public class RectK implements Comparable<RectK> {
    public int x1;
    public int y1;
    public int x2;
    public int y2;

    public double scaleX;
    public double scaleY;

    public double k;

    public RectK(int x, int y) {
        x1 = x;
        y1 = y;
        x2 = x;
        y2 = y;
    }

    public int size() {
        return (x2 - x1 + 1) * (y2 - y1 + 1);
    }

    public boolean equal(RectK k) {
        return x1 == k.x1 && x2 == k.x2 && y1 == k.y1 && y2 == k.y2;
    }

    @Override
    public int compareTo(RectK arg0) {
        int r = 0;
        if (r == 0)
            r = new Integer(x1).compareTo(arg0.x1);
        if (r == 0)
            r = new Integer(y1).compareTo(arg0.y1);
        if (r == 0)
            r = new Integer(x2).compareTo(arg0.x2);
        if (r == 0)
            r = new Integer(y2).compareTo(arg0.y2);
        return r;
    }
}
