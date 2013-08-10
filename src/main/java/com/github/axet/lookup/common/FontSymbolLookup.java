package com.github.axet.lookup.common;

import java.awt.Rectangle;

import org.apache.commons.lang.math.IntRange;

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
        IntRange r1 = new IntRange(y, y + fs.image.getHeight());

        IntRange r2 = new IntRange(f.y, f.y + f.fs.image.getHeight());

        return r1.overlapsRange(r2);
    }

    public int getWidth() {
        return fs.image.getWidth();
    }

    public int getHeight() {
        return fs.image.getHeight();
    }
}
