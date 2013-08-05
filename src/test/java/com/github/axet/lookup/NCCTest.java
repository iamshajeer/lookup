package com.github.axet.lookup;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.List;

import com.github.axet.lookup.common.GPoint;
import com.github.axet.lookup.proc.NCC;

public class NCCTest {

    public static void main(String[] args) {
        BufferedImage image = Capture.load(OCRTest.class, "cyclopst1.png");
        BufferedImage template = Capture.load(OCRTest.class, "cyclopst3.png");

        List<GPoint> pp = NCC.lookup(image, template, 0.9f);

        for (Point p : pp) {
            System.out.println(p);
        }
    }
}
