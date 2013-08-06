package com.github.axet.lookup.proc;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.github.axet.lookup.common.FeatureK;
import com.github.axet.lookup.common.FeatureSet;
import com.github.axet.lookup.common.FeatureSetDefault;
import com.github.axet.lookup.common.GPoint;
import com.github.axet.lookup.common.ImageBinary;
import com.github.axet.lookup.common.ImageBinaryFeature;
import com.github.axet.lookup.common.ImageMultiplySum;
import com.github.axet.lookup.common.RectK;

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

    static double denominator(ImageBinary image, ImageBinary template, int xx, int yy) {
        double di = image.dev2n(xx, yy, xx + template.getWidth() - 1, yy + template.getHeight() - 1);
        double dt = template.dev2n();
        return Math.sqrt(di * dt);
    }

    static double numerator(ImageBinary image, ImageBinaryFeature template, int xx, int yy) {
        double n = 0;

        for (FeatureK f : template.k) {
            for (RectK k : f.list) {
                double ii = image.integral.sigma(xx + k.x1, yy + k.y1, xx + k.x2, yy + k.y2);
                double mt = k.k;
                n += ii * mt;
            }
        }

        // n /= ns;

        // multiply sum[f(x,y) * t'mean]
        ImageMultiplySum m = new ImageMultiplySum(image.gi, xx, yy, template.zeroMean);
        double q = m.sum;

        return n;
    }

    static public double gamma(ImageBinary image, ImageBinaryFeature template, int xx, int yy) {
        double d = denominator(image, template, xx, yy);

        if (d == 0)
            return -1;

        double n = numerator(image, template, xx, yy);

        return (n / d);
    }

}
