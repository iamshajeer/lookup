package com.github.axet.lookup.common;

import java.awt.image.BufferedImage;
import java.util.List;

public class ImageBinaryFeature extends ImageBinary {

    List<FeatureK> k;

    public ImageBinaryFeature(BufferedImage img, FeatureSet list) {
        super(img);

        k = list.k(image);
    }

}
