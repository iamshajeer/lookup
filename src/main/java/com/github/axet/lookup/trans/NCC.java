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
    public List<Point> lookup(BufferedImage i, BufferedImage t, float m) {
        ImageBinary imageBinary = new ImageBinary(i);
        ImageBinary templateBinary = new ImageBinary(t);

        return lookup(imageBinary, templateBinary, m);
    }

    public List<Point> lookup(ImageBinary image, ImageBinary template, float m) {
        List<Point> list = new ArrayList<Point>();

        for (int x = 0; x <= image.getWidth() - template.getWidth(); x++) {
            for (int y = 0; y <= image.getHeight() - template.getHeight(); y++) {
                if (gamma(image, template, x, y) > m) {
                    list.add(new Point(x, y));
                }
            }
        }

        return list;
    }

    float gamma(ImageBinary image, ImageBinary template, int x, int y) {
        ImageMultiply s = new ImageMultiply(image.zeroMean, x, y, template.zeroMean);
        IntegralImage ss = new IntegralImage(s);

        return (float) (ss.mean() / (image.dev(x, y, x + template.getWidth() - 1, y + template.getHeight() - 1) * template
                .dev()));
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

        image = Capture.load("/Users/axet/Desktop/4a.jpg");
        template = Capture.load("/Users/axet/Desktop/4c.jpg");

        NCC nnc = new NCC();
        List<Point> pp = nnc.lookup(image, template, 0.7f);

        for (Point p : pp) {
            System.out.println(p);
        }
    }
}
