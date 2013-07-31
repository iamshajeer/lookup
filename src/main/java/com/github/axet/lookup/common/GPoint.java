package com.github.axet.lookup.common;

import java.awt.Point;

public class GPoint extends Point {
    public double g;

    public GPoint(int x, int y, double g) {
        super(x, y);
        this.g = g;
    }
}
