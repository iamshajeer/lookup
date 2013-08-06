package com.github.axet.lookup.common;

import java.util.Comparator;

public class GFirst implements Comparator<GPoint> {
    @Override
    public int compare(GPoint arg0, GPoint arg1) {
        return new Double(arg1.g).compareTo(new Double(arg0.g));
    }
}