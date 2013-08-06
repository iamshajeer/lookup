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

    public RectK(int x1, int y1, int x2, int y2, double sx, double sy) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.scaleX = sx;
        this.scaleY = sy;
    }

    public int getWidth() {
        return x2 - x1;
    }

    public int getHeight() {
        return y2 - y1;
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

    public RectK[] devide() {
        int w = getWidth();
        int h = getHeight();
        if (w > h) {
            w /= 2;
        } else {
            h /= 2;
        }

        return new RectK[] { new RectK(x1, y1, x1 + w, y1 + h, scaleX / (getWidth() / w), scaleY / (getHeight() / h)),
                new RectK(x1 + w, y1 + h, x1 + w, y1 + h, scaleX / (getWidth() / w), scaleY / (getHeight() / h)) };
    }
}
