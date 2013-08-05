package com.github.axet.lookup.proc;

import com.github.axet.lookup.common.FeatureSet;
import com.github.axet.lookup.common.FeatureSetDefault;
import com.github.axet.lookup.common.ImageBinary;
import com.github.axet.lookup.common.ImageBinaryFeature;
import com.github.axet.lookup.common.ImageMultiplyIntegral;

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
    
    FeatureSet features = new FeatureSetDefault();
    
    public FNCC() {
    }

    static double numerator(ImageBinary image, ImageBinaryFeature template, int xx, int yy) {
        ImageMultiplyIntegral m = new ImageMultiplyIntegral(image.zeroMean, xx, yy, template.zeroMean);
        return m.mean();
    }

    static public double gamma(ImageBinary image, ImageBinaryFeature template, int xx, int yy) {
        double d = denominator(image, template, xx, yy);

        if (d == 0)
            return -1;

        double n = numerator(image, template, xx, yy);

        return (n / d);
    }

}
