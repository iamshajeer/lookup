package com.github.axet.lookup;

import java.awt.image.BufferedImage;
import java.util.List;

import com.github.axet.lookup.common.GPoint;
import com.github.axet.lookup.common.ImageBinaryGrey;
import com.github.axet.lookup.common.ImageBinaryRGB;
import com.github.axet.lookup.common.ImageBinaryRGBFeature;
import com.github.axet.lookup.proc.FNCC;

public class FNCCTest {

    public static void main(String[] args) {
        BufferedImage image = Capture.load(OCRTest.class, "cyclopst1.png");
        BufferedImage template = Capture.load(OCRTest.class, "cyclopst3.png");

        // rgb image lookup
        {
            List<GPoint> pp = FNCC
                    .lookupAll(new ImageBinaryRGB(image), new ImageBinaryRGBFeature(template, 5000), 0.9f);

            for (GPoint p : pp) {
                System.out.println(p);
            }
        }

        // grey image lookup
        {
            List<GPoint> pp = FNCC.lookupAll(new ImageBinaryGrey(image), new ImageBinaryRGBFeature(template, 5000),
                    0.9f);

            for (GPoint p : pp) {
                System.out.println(p);
            }
        }
    }

}
