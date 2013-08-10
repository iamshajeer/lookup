package com.github.axet.lookup;

import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.List;

import com.github.axet.lookup.common.GFirst;
import com.github.axet.lookup.common.GPoint;
import com.github.axet.lookup.common.ImageBinaryGreyScaleRGB;
import com.github.axet.lookup.common.ImageBinaryScale;

public class SNCCTest {

    public static void main(String[] args) {
        BufferedImage image = Capture.load(OCRTest.class, "desktop.png");
        BufferedImage template = Capture.load(OCRTest.class, "desktop_feature_big.png");

        LookupScale s = new LookupScale(4, 0.50f, 0.8f);
        s.s = 0.2;

        ImageBinaryScale si = new ImageBinaryGreyScaleRGB(image);

        ImageBinaryScale st = new ImageBinaryGreyScaleRGB(template);

        Long l;

        for (int i = 0; i < 2; i++) {
            l = System.currentTimeMillis();
            {
                List<GPoint> pp = s.lookupAll(si, st);

                Collections.sort(pp, new GFirst());

                for (GPoint p : pp) {
                    System.out.println(p);
                }
            }
            System.out.println(System.currentTimeMillis() - l);
        }
    }
}
