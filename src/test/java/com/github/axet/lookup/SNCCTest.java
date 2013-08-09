package com.github.axet.lookup;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.List;

import com.github.axet.lookup.common.GFirst;
import com.github.axet.lookup.common.GPoint;
import com.github.axet.lookup.common.ImageBinaryGreyScale;

public class SNCCTest {

    public static void main(String[] args) {
        BufferedImage image = Capture.load(OCRTest.class, "desktop.png");
        BufferedImage templateSmall = Capture.load(OCRTest.class, "desktop_feature_small.png");
        BufferedImage templateBig = Capture.load(OCRTest.class, "desktop_feature_big.png");

        LookupScale s = new LookupScale(5, 0.75f, 0.8f);

        ImageBinaryGreyScale si = new ImageBinaryGreyScale(image);

        ImageBinaryGreyScale stBig = new ImageBinaryGreyScale(templateBig);
        ImageBinaryGreyScale stSmall = new ImageBinaryGreyScale(templateSmall);

        Long l;

        System.out.println("big");
        l = System.currentTimeMillis();
        {
            List<GPoint> pp = s.lookupAll(si, stBig);

            Collections.sort(pp, new GFirst());

            for (GPoint p : pp) {
                System.out.println(p);
            }
        }
        System.out.println(System.currentTimeMillis() - l);

        System.out.println("small");
        l = System.currentTimeMillis();
        {
            List<GPoint> pp = s.lookupAll(si, stSmall);

            Collections.sort(pp, new GFirst());

            for (GPoint p : pp) {
                System.out.println(p);
            }
        }
        System.out.println(System.currentTimeMillis() - l);

        System.out.println("big");
        l = System.currentTimeMillis();
        {
            List<GPoint> pp = s.lookupAll(si, stBig);

            Collections.sort(pp, new GFirst());

            for (GPoint p : pp) {
                System.out.println(p);
            }
        }
        System.out.println(System.currentTimeMillis() - l);

    }
}
