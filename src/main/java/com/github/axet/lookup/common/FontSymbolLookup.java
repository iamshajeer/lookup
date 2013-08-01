package com.github.axet.lookup.common;

import java.awt.Rectangle;

public class FontSymbolLookup {
    public int x;
    public int y;
    public FontSymbol fs;
    public double g;

    public FontSymbolLookup(FontSymbol fs, int x, int y, double g) {
        this.fs = fs;
        this.x = x;
        this.y = y;
        this.g = g;
    }

    public int size() {
        return fs.image.getHeight() * fs.image.getWidth();
    }

    public boolean cross(FontSymbolLookup f) {
        Rectangle r = new Rectangle(x, y, fs.image.getWidth(), fs.image.getHeight());
        Rectangle r2 = new Rectangle(f.x, f.y, f.fs.image.getWidth(), f.fs.image.getHeight());

        return r.intersects(r2);
    }

    public boolean yCross(FontSymbolLookup f) {
        int y1 = y;
        int y2 = y1 + fs.image.getHeight();

        int yy1 = f.y;
        int yy2 = yy1 + f.fs.image.getHeight();

        if (y1 >= yy1 && y1 <= yy2)
            return true;

        if (y2 >= yy1 && y2 <= yy2)
            return true;

        return false;
    }

    public int getWidth() {
        return fs.image.getWidth();
    }

    public int getHeight() {
        return fs.image.getHeight();
    }
}
