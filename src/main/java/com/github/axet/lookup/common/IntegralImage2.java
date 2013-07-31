package com.github.axet.lookup.common;


public class IntegralImage2 extends SArray {

    static public double pow2(double x) {
        return x * x;
    }

    public IntegralImage2(SArray buf) {
        super(buf);

        for (int x = 0; x < cx; x++) {
            for (int y = 0; y < cy; y++) {
                s[i(x, y)] = pow2(buf.s(x, y)) + s(x - 1, y) + s(x, y - 1) - s(x - 1, y - 1);
            }
        }
    }

    /**
     * Standard deviation
     * 
     * @param i
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public double dev(IntegralImage i, int x1, int y1, int x2, int y2) {
        double diff = i.sigma(x1, y1, x2, y2);
        int area = (x2 - x1 + 1) * (y2 - y1 + 1);
        double sqdiff = sigma(x1, y1, x2, y2);
        return Math.sqrt((sqdiff - pow2(diff) / area) / (area - 1));
    }

    /**
     * Standard deviation
     * 
     * @param i
     * @return
     */
    public double dev(IntegralImage i) {
        return dev(i, 0, 0, cx - 1, cy - 1);
    }
}
