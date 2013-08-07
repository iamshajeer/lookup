package com.github.axet.lookup;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import com.github.axet.lookup.common.GPoint;
import com.github.axet.lookup.common.ImageBinaryGrey;
import com.github.axet.lookup.common.ImageBinaryGreyFeature;
import com.github.axet.lookup.proc.FNCC;

public class FNCCTest {

    public static void main(String[] args) {
        BufferedImage image = Capture.load(OCRTest.class, "desktop.png");
        BufferedImage template = Capture.load(OCRTest.class, "desktop_problems.png");
        BufferedImage template2 = Capture.load(OCRTest.class, "desktop_problems.png");

        {
            // lookup images using threshold == 300000 (bigger is faster)
            // and match quality 0.53f (high is more accurate match)
            // List<GPoint> pp = FNCC.lookupAll(image, template, 300000, 0.54f);
            //
            // for (Point p : pp) {
            // System.out.println(p);
            // }
        }

        System.out.println();
        System.out.println("Manual mode");
        System.out.println();

        {
            ImageBinaryGreyFeature bf = new ImageBinaryGreyFeature(template, 50000);
            ImageBinaryGreyFeature bf2 = new ImageBinaryGreyFeature(template2, 50000);
            ImageBinaryGrey ib = new ImageBinaryGrey(image);

            long l;

            l = System.currentTimeMillis();
            Point p1 = Lookup.lookup(image, template, 0.20f);
            System.out.println(System.currentTimeMillis() - l);

            l = System.currentTimeMillis();
            List<GPoint> pp = FNCC.lookupAll(ib, bf, 0.20f);
            System.out.println(System.currentTimeMillis() - l);

            l = System.currentTimeMillis();
            List<GPoint> pp2 = FNCC.lookupAll(ib, bf2, 0.20f);
            System.out.println(System.currentTimeMillis() - l);

            System.out.println(p1);

            for (GPoint p : pp)
                System.out.println(p);

            for (GPoint p : pp2)
                System.out.println(p);
        }
    }

}
