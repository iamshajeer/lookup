package com.github.axet.lookup;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.List;

import com.github.axet.lookup.common.FeatureSetDefault;
import com.github.axet.lookup.common.GPoint;
import com.github.axet.lookup.common.ImageBinaryFeature;
import com.github.axet.lookup.common.ImageBinaryZeroIntegral;
import com.github.axet.lookup.proc.FNCC;

public class FNCCTest {

    public static void main(String[] args) {
        BufferedImage image = Capture.load(OCRTest.class, "cyclopst1.png");
        BufferedImage template = Capture.load(OCRTest.class, "cyclopst3.png");

        ImageBinaryFeature bf = new ImageBinaryFeature(template, new FeatureSetDefault());

        List<GPoint> pp = FNCC.lookup(new ImageBinaryZeroIntegral(image), 21, 7, 21 + template.getWidth() - 1,
                7 + template.getHeight(), bf, 0.9f);

        for (Point p : pp) {
            System.out.println(p);
        }

        pp = FNCC.lookup(image, template, 0.9f);

        for (Point p : pp) {
          System.out.println(p);
        }
    }

}
