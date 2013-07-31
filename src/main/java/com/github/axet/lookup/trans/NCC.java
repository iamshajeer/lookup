package com.github.axet.lookup.trans;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.github.axet.lookup.Capture;
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
    static public List<Point> lookup(BufferedImage i, BufferedImage t, float m) {
        ImageBinary imageBinary = new ImageBinary(i);
        ImageBinary templateBinary = new ImageBinary(t);

        return lookup(imageBinary, templateBinary, m);
    }

    static public List<Point> lookup(ImageBinary image, ImageBinary template, float m) {
        return lookup(image, 0, 0, image.getWidth() - 1, image.getHeight() - 1, template, m);
    }

    static public List<Point> lookup(ImageBinary image, int x1, int y1, int x2, int y2, ImageBinary template, float m) {
        List<Point> list = new ArrayList<Point>();

        for (int x = x1; x <= x2 - template.getWidth() + 1; x++) {
            for (int y = y1; y <= y2 - template.getHeight() + 1; y++) {
                double g = gamma(image, template, x, y);
                if (g > m) {
                    list.add(new Point(x, y));
                }
            }
        }

        return list;
    }

    static public double gamma(ImageBinary image, ImageBinary template, int xx, int yy) {
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

        double n = ss.mean();
        double id = image.dev(xx, yy, xx + template.getWidth() - 1, yy + template.getHeight() - 1);
        double td = template.dev();
        double d = id * td;

        if (d == 0)
            return -1;

        return (n / d);
    }

    public static void main(String[] args) {
        BufferedImage image = new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB);
        image.getWritableTile(0, 0).setDataElements(0, 0, image.getWidth(), image.getHeight(), new int[] {

        1, 0, 0,

        0, 1, 2,

        0, 0, 255

        });

        BufferedImage template = new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB);
        template.getWritableTile(0, 0).setDataElements(0, 0, template.getWidth(), template.getHeight(), new int[] {

        0, 0, 0,

        0, 255, 0,

        0, 0, 0

        });

        image = Capture.load("/Users/axet/Desktop/cyclopst1.png");
        template = Capture.load("/Users/axet/Desktop/cyclopst3.png");

        List<Point> pp = NCC.lookup(image, template, 0.9f);

        for (Point p : pp) {
            System.out.println(p);
        }
    }
}
