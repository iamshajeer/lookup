package com.github.axet.lookup.common;

import java.awt.image.BufferedImage;

public class ImageBinaryZeroIntegral extends ImageBinary {

    public IntegralImage zeroMeanIntegral;

    public ImageBinaryZeroIntegral(BufferedImage img) {
        super(img);

        zeroMeanIntegral = new IntegralImage(zeroMean);
    }

}
