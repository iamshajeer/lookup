package com.github.axet.lookup.trans;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.github.axet.lookup.common.GPoint;
import com.github.axet.lookup.common.ImageBinary;
import com.github.axet.lookup.common.ImageMultiply;
import com.github.axet.lookup.common.IntegralImage;

/**
 * 
 * http://www.fmwconcepts.com/imagemagick/similar/index.php
 * 
 * Normalized cross correlation algorithm
 * 
 * @author axet
 * 
 */
public class NCC {

    static public List<GPoint> lookup(BufferedImage i, BufferedImage t, float m) {
        ImageBinary imageBinary = new ImageBinary(i);
        ImageBinary templateBinary = new ImageBinary(t);

        return lookup(imageBinary, templateBinary, m);
    }

    static public List<GPoint> lookup(ImageBinary image, ImageBinary template, float m) {
        return lookup(image, 0, 0, image.getWidth() - 1, image.getHeight() - 1, template, m);
    }

    static public List<GPoint> lookup(ImageBinary image, int x1, int y1, int x2, int y2, ImageBinary template, float m) {
        List<GPoint> list = new ArrayList<GPoint>();

        for (int x = x1; x <= x2 - template.getWidth() + 1; x++) {
            for (int y = y1; y <= y2 - template.getHeight() + 1; y++) {
                double g = gamma(image, template, x, y);
                if (g > m) {
                    list.add(new GPoint(x, y, g));
                }
            }
        }

        return list;
    }

    static double numerator(ImageBinary image, ImageBinary template, int xx, int yy) {
        // ImageMultiply s = new ImageMultiply(image.zeroMean, xx, yy,
        // template.zeroMean);
        // IntegralImage ss = new IntegralImage(s);

        // speed up

        ImageMultiply s = new ImageMultiply();
        IntegralImage ss = new IntegralImage();

        s.init(image.zeroMean, xx, yy, template.zeroMean);
        ss.init(s);

        for (int x = 0; x < template.getWidth(); x++) {
            for (int y = 0; y < template.getHeight(); y++) {
                s.step(x, y);
                ss.step(x, y);
            }
        }

        return ss.mean();
    }

    static double denominator(ImageBinary image, ImageBinary template, int xx, int yy) {
        double id = image.dev(xx, yy, xx + template.getWidth() - 1, yy + template.getHeight() - 1);
        double td = template.dev();
        return id * td;
    }

    static public double gamma(ImageBinary image, ImageBinary template, int xx, int yy) {
        double d = denominator(image, template, xx, yy);

        if (d == 0)
            return -1;

        double n = numerator(image, template, xx, yy);

        return (n / d);
    }

}
