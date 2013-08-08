package com.github.axet.lookup.common;

import java.util.Comparator;

public class GFirstLeftRight implements Comparator<GPoint> {
    double qualityEqual;

    public GFirstLeftRight(double qualityEqual) {
        this.qualityEqual = 1 - qualityEqual;
    }

    @Override
    public int compare(GPoint arg0, GPoint arg1) {
        // quality first
        int r = LessCompare.compareBigFirst(arg0.g, arg1.g, qualityEqual);
        if (r == 0)
            r = LessCompare.compareSmallFirst(arg0.x, arg1.x);
        if (r == 0)
            r = LessCompare.compareSmallFirst(arg0.y, arg1.y);
        if (r == 0)
            r = LessCompare.compareBigFirst(arg0.g, arg1.g);

        return r;
    }
}