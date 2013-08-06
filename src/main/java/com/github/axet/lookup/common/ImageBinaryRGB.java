package com.github.axet.lookup.common;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

public class ImageBinaryRGB implements ImageBinary {

    public RGBImage image;
    public ImageBinaryChannel r;
    public ImageBinaryChannel g;
    public ImageBinaryChannel b;

    List<ImageBinaryChannel> list;

    public ImageBinaryRGB(BufferedImage img) {
        image = new RGBImage();
        r = new ImageBinaryChannel();
        r.integral = new IntegralImage();
        r.integral2 = new IntegralImage2();
        g = new ImageBinaryChannel();
        g.integral = new IntegralImage();
        g.integral2 = new IntegralImage2();
        b = new ImageBinaryChannel();
        b.integral = new IntegralImage();
        b.integral2 = new IntegralImage2();

        list = Arrays.asList(new ImageBinaryChannel[] { r, g, b });

        this.image.init(img);
        this.r.initBase(this.image.r);
        this.g.initBase(this.image.g);
        this.b.initBase(this.image.b);

        for (int x = 0; x < this.image.cx; x++) {
            for (int y = 0; y < this.image.cy; y++) {
                this.image.step(x, y);
                this.r.integral.step(x, y);
                this.r.integral2.step(x, y);
                this.g.integral.step(x, y);
                this.g.integral2.step(x, y);
                this.b.integral.step(x, y);
                this.b.integral2.step(x, y);
            }
        }

        r.zeroMean = new ImageZeroMean();
        r.zeroMean.init(r.integral);
        g.zeroMean = new ImageZeroMean();
        g.zeroMean.init(g.integral);
        b.zeroMean = new ImageZeroMean();
        b.zeroMean.init(b.integral);

        for (int x = 0; x < this.image.cx; x++) {
            for (int y = 0; y < this.image.cy; y++) {
                r.zeroMean.step(x, y);
                g.zeroMean.step(x, y);
                b.zeroMean.step(x, y);
            }
        }
    }

    public int getWidth() {
        return image.cx;
    }

    public int getHeight() {
        return image.cy;
    }

    public int size() {
        return image.cx * image.cy;
    }

    public BufferedImage getImage() {
        return image.buf;
    }

    @Override
    public List<ImageBinaryChannel> getChannels() {
        return list;
    }

}
