package com.github.axet.lookup;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.List;

import com.github.axet.lookup.common.GPoint;
import com.github.axet.lookup.common.ImageBinary;
import com.github.axet.lookup.common.ImageBinaryFeature;
import com.github.axet.lookup.proc.FNCC;

public class FNCCTest {

    public static void main(String[] args) {
        BufferedImage image = Capture.load(OCRTest.class, "cyclopst1.png");
        BufferedImage template = Capture.load(OCRTest.class, "cyclopst3.png");

        {
            // lookup images using threshold == 300000 (bigger is faster)
            // and match quality 0.53f (high is more accurate match)
            List<GPoint> pp = FNCC.lookup(image, template, 300000, 0.54f);

            for (Point p : pp) {
                System.out.println(p);
            }
        }

        System.out.println();
        System.out.println("Manual mode");
        System.out.println();

        {
            ImageBinaryFeature bf = new ImageBinaryFeature(template, 300000);

            System.out.println("Features: " + bf.k.size());

            List<GPoint> pp = FNCC.lookup(new ImageBinary(image), bf, 0.54f);

            for (Point p : pp) {
                System.out.println(p);
            }
        }
    }

}
