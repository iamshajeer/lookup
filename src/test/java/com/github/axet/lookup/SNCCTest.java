package com.github.axet.lookup;

import java.awt.image.BufferedImage;
import java.util.List;

import com.github.axet.lookup.common.GPoint;
import com.github.axet.lookup.common.ImageBinaryGreyScale;

public class SNCCTest {

    public static void main(String[] args) {
        BufferedImage image = Capture.load(OCRTest.class, "desktop.png");
        BufferedImage templateSmall = Capture.load(OCRTest.class, "desktop_feature_small.png");
        BufferedImage templateBig = Capture.load(OCRTest.class, "desktop_feature_big.png");

        LookupScale s = new LookupScale(5);

        ImageBinaryGreyScale si = new ImageBinaryGreyScale(image);

        System.out.println("big");
        {
            ImageBinaryGreyScale st = new ImageBinaryGreyScale(templateBig);
            List<GPoint> pp = s.lookupAll(si, st, 0.6f, 0.7f);

            for (GPoint p : pp) {
                System.out.println(p);
            }
        }

        System.out.println("small");
        {
            ImageBinaryGreyScale st = new ImageBinaryGreyScale(templateSmall);
            List<GPoint> pp = s.lookupAll(si, st, 0.6f, 0.8f);

            for (GPoint p : pp) {
                System.out.println(p);
            }
        }
    }
}
