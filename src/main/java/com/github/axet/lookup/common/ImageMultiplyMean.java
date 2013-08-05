package com.github.axet.lookup.common;

public class ImageMultiplyMean {

    public ImageMultiply s = new ImageMultiply();

    public double mean = 0;

    public ImageMultiplyMean(ImageZeroMean image, int xx, int yy, ImageZeroMean template) {
        // ImageMultiply s = new ImageMultiply(image.zeroMean, xx, yy,
        // template.zeroMean);
        // IntegralImage ss = new IntegralImage(s);

        // speed up

        s.init(image, xx, yy, template);

        for (int x = 0; x < template.cx; x++) {
            for (int y = 0; y < template.cy; y++) {
                s.step(x, y);

                mean += s.s(x, y);
            }
        }

        mean /= s.s.length;
    }
}
