package com.github.axet.lookup.common;

/**
 * Haar Like feature
 * 
 * @author axet
 * 
 */
public class Feature extends SArray {

    public Feature(int cx, int cy, double[] i) {
        this.cx = cx;
        this.cy = cy;
        this.s = i;
    }
}
