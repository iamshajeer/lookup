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
}
