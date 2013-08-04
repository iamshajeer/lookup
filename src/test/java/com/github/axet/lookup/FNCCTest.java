package com.github.axet.lookup;

import java.awt.image.BufferedImage;

import com.github.axet.lookup.proc.FNCC;

/**
 * http://isas.uka.de/Material/AltePublikationen/briechle_spie2001.pdf
 * 
 * NOT WORKING (check NCC.java)
 * 
 * Fast Normalized cross correlation algorithm
 * 
 * 
 * @author axet
 * 
 */
public class FNCCTest {

    public static void main(String[] args) {
        BufferedImage image = new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB);
        image.getWritableTile(0, 0).setDataElements(0, 0, image.getWidth(), image.getHeight(), new int[] {

        0, 0, 0,

        0, 1, 0,

        0, 0, 0

        });

        BufferedImage template = new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB);
        image.getWritableTile(0, 0).setDataElements(0, 0, image.getWidth(), image.getHeight(), new int[] {

        0, 0, 0,

        0, 1, 0,

        0, 0, 0

        });

        FNCC fnnc = new FNCC();
    }
}
