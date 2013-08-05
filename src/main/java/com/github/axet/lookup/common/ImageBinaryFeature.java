package com.github.axet.lookup.common;

import java.awt.image.BufferedImage;
import java.util.List;

public class ImageBinaryFeature extends ImageBinary {

    public List<FeatureK> k;
    public IntegralImage zeroMeanIntegral;

    public ImageBinaryFeature(BufferedImage img, FeatureSet list) {
        super(img);

        zeroMeanIntegral = new IntegralImage(zeroMean);
        k = list.k(zeroMeanIntegral);
    }

}
