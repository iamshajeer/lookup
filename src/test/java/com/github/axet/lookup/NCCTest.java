package com.github.axet.lookup;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import com.github.axet.lookup.common.GPoint;
import com.github.axet.lookup.trans.NCC;

/**
 * 
 * http://www.fmwconcepts.com/imagemagick/similar/index.php
 * 
 * Normalized cross correlation algorithm
 * 
 * @author axet
 * 
 */
public class NCCTest {

    public static void main(String[] args) {
        BufferedImage image = new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB);
        image.getWritableTile(0, 0).setDataElements(0, 0, image.getWidth(), image.getHeight(), new int[] {

        1, 0, 0,

        0, 1, 2,

        0, 0, 255

        });

        BufferedImage template = new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB);
        template.getWritableTile(0, 0).setDataElements(0, 0, template.getWidth(), template.getHeight(), new int[] {

        0, 0, 0,

        0, 255, 0,

        0, 0, 0

        });

        image = Capture.load(OCRTest.class, "cyclopst1.png");
        template = Capture.load(OCRTest.class, "cyclopst3.png");

        List<GPoint> pp = NCC.lookup(image, template, 0.9f);

        for (Point p : pp) {
            System.out.println(p);
        }
    }
}
