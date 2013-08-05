package com.github.axet.lookup.proc;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.github.axet.lookup.common.FeatureSet;
import com.github.axet.lookup.common.FeatureSetDefault;
import com.github.axet.lookup.common.GPoint;
import com.github.axet.lookup.common.ImageBinary;
import com.github.axet.lookup.common.ImageBinaryFeature;
import com.github.axet.lookup.common.ImageMultiplyIntegral;

/**
 * http://isas.uka.de/Material/AltePublikationen/briechle_spie2001.pdf
 * 
 * NOT WORKING (check NCC.java)
 * 
 * Fast Normalized cross correlation algorithm
 * 
 * 
 * @author axet
 * 
 */
public class FNCC {

    static FeatureSet features = new FeatureSetDefault();

    static public List<GPoint> lookup(BufferedImage i, BufferedImage t, float m) {
        ImageBinary imageBinary = new ImageBinary(i);
        ImageBinaryFeature templateBinary = new ImageBinaryFeature(t, features);

        return lookup(imageBinary, templateBinary, m);
    }

    static public List<GPoint> lookup(ImageBinary image, ImageBinaryFeature template, float m) {
        return lookup(image, 0, 0, image.getWidth() - 1, image.getHeight() - 1, template, m);
    }

    static public List<GPoint> lookup(ImageBinary image, int x1, int y1, int x2, int y2, ImageBinaryFeature template,
            float m) {
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
        ImageMultiplyIntegral m = new ImageMultiplyIntegral(image.zeroMean, xx, yy, template.zeroMean);
        return m.mean();
    }

    static double denominator(ImageBinary image, ImageBinary template, int xx, int yy) {
        double id = image.dev(xx, yy, xx + template.getWidth() - 1, yy + template.getHeight() - 1);
        double td = template.dev();
        return id * td;
    }

    static double numerator(ImageBinary image, ImageBinaryFeature template, int xx, int yy) {
        ImageMultiplyIntegral m = new ImageMultiplyIntegral(image.zeroMean, xx, yy, template.zeroMean);
        return m.mean();
    }

    static public double gamma(ImageBinary image, ImageBinaryFeature template, int xx, int yy) {
        double d = denominator(image, template, xx, yy);

        if (d == 0)
            return -1;

        double n = numerator(image, template, xx, yy);

        return (n / d);
    }

}
