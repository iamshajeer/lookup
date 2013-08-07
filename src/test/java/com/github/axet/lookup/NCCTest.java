package com.github.axet.lookup;

import java.awt.image.BufferedImage;
import java.util.List;

import com.github.axet.lookup.common.GPoint;
import com.github.axet.lookup.common.ImageBinaryGrey;
import com.github.axet.lookup.common.ImageBinaryRGB;
import com.github.axet.lookup.proc.NCC;

public class NCCTest {

    public static void main(String[] args) {
        BufferedImage image = Capture.load(OCRTest.class, "cyclopst1.png");
        BufferedImage template = Capture.load(OCRTest.class, "cyclopst3.png");

        // rgb image lookup
        {
            List<GPoint> pp = NCC.lookupAll(new ImageBinaryRGB(image), new ImageBinaryRGB(template), 0.9f);

            for (GPoint p : pp) {
                System.out.println(p);
            }
        }
        
        // grey image lookup
        {
            List<GPoint> pp = NCC.lookupAll(new ImageBinaryGrey(image), new ImageBinaryGrey(template), 0.9f);

            for (GPoint p : pp) {
                System.out.println(p);
            }
        }
    }
}
