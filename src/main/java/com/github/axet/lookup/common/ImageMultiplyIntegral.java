package com.github.axet.lookup.common;

public class ImageMultiplyIntegral extends IntegralImage {

    public ImageMultiply s = new ImageMultiply();

    public ImageMultiplyIntegral(ImageZeroMean image, int xx, int yy, ImageZeroMean template) {
        // ImageMultiply s = new ImageMultiply(image.zeroMean, xx, yy,
        // template.zeroMean);
        // IntegralImage ss = new IntegralImage(s);

        // speed up

        s.init(image, xx, yy, template);
        init(s);

        for (int x = 0; x < template.cx; x++) {
            for (int y = 0; y < template.cy; y++) {
                step(x, y);
            }
        }
    }

    public void step(int x, int y) {
        s.step(x, y);

        super.step(x, y);
    }
}
