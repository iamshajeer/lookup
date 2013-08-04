package com.github.axet.lookup.proc;

import com.github.axet.lookup.common.ImageBinary;

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
public class FNCC extends NCC {

    // FNCC needs only new numerator function
    static double numerator(ImageBinary image, ImageBinary template, int xx, int yy) {
        return 0;
    }

    static public double gamma(ImageBinary image, ImageBinary template, int xx, int yy) {
        double d = denominator(image, template, xx, yy);

        if (d == 0)
            return -1;

        double n = numerator(image, template, xx, yy);

        return (n / d);
    }

}
