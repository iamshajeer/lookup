package com.github.axet.lookup.common;

import java.awt.image.BufferedImage;
import java.util.List;

public interface ImageBinaryFeature {

    public List<ImageBinaryChannelFeature> getFeatureChannels();

    public int getWidth();

    public int getHeight();

    public int size();

    public BufferedImage getImage();

}
