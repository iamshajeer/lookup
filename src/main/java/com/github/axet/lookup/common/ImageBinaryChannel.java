package com.github.axet.lookup.common;

public class ImageBinaryChannel {
    public SArray gi;
    public IntegralImage integral;
    public IntegralImage2 integral2;
    public ImageZeroMean zeroMean;

    public ImageBinaryChannel() {
    }

    public ImageBinaryChannel(SArray img) {
        gi = img;
        integral = new IntegralImage();
        integral2 = new IntegralImage2();

        this.integral.initBase(gi);
        this.integral2.initBase(gi);

        for (int x = 0; x < this.gi.cx; x++) {
            for (int y = 0; y < this.gi.cy; y++) {
                this.integral.step(x, y);
                this.integral2.step(x, y);
            }
        }

        zeroMean = new ImageZeroMean(integral);
    }

    public void initBase(SArray img) {
        gi = img;
        integral.initBase(img);
        integral2.initBase(img);
    }

    public double dev2n() {
        return integral2.dev2n(integral);
    }

    public double dev2() {
        return integral2.dev2(integral);
    }

    public double dev() {
        return integral2.dev(integral);
    }

    public double dev2n(int x1, int y1, int x2, int y2) {
        return integral2.dev2n(integral, x1, y1, x2, y2);
    }

    public double dev2(int x1, int y1, int x2, int y2) {
        return integral2.dev2(integral, x1, y1, x2, y2);
    }

    public double dev(int x1, int y1, int x2, int y2) {
        return integral2.dev(integral, x1, y1, x2, y2);
    }

    public int getWidth() {
        return gi.cx;
    }

    public int getHeight() {
        return gi.cy;
    }

    public int size() {
        return gi.cx * gi.cy;
    }

}
