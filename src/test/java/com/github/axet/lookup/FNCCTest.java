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

        ImageBinaryFeature bf = new ImageBinaryFeature(template, 30000);

        List<GPoint> pp;

        Point p1 = new Point(21, 7);

        // pp = FNCC.lookup(new ImageBinary(image), p1.x, p1.y, p1.x +
        // template.getWidth() - 1,
        // p1.y + template.getHeight(), bf, 0.9f);
        //
        // for (Point p : pp) {
        // System.out.println(p);
        // }

        pp = FNCC.lookup(image, template, 0.01f);

        for (Point p : pp) {
            System.out.println(p);
        }
    }

}
