package com.github.axet.lookup.trans;

import java.awt.image.BufferedImage;

import com.github.axet.lookup.common.GrayImage;
import com.github.axet.lookup.common.IntegralImage;
import com.github.axet.lookup.common.IntegralImage2;

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
public class FNCC {
    IntegralImage imageIntegral;
    IntegralImage2 imageIntegral2;

    IntegralImage templateIntegral;
    IntegralImage2 templateIntegral2;

    int Nx;
    int Ny;

    int Mx;
    int My;

    public FNCC(BufferedImage image, BufferedImage template) {
        imageIntegral = new IntegralImage(new GrayImage(image));
        imageIntegral2 = new IntegralImage2(new GrayImage(image));

        templateIntegral = new IntegralImage(new GrayImage(template));
        templateIntegral2 = new IntegralImage2(new GrayImage(template));
    }

    public FNCC(IntegralImage image, IntegralImage template) {
        Nx = template.cx;
        Ny = template.cy;

        Mx = image.cx;
        My = image.cy;

        int cx = Mx + Nx - 1;
        int cy = My + Ny - 1;

        // s = new int[cx * cy];

        for (int u = 0; u < cx - Nx; u++) {
            for (int v = 0; v < cy - Ny; v++) {
                gamma(u, v);
            }
        }
    }

    public double f2(int u, int v) {
        double f7 = imageIntegral.s(u + Nx - 1, v + Ny - 1) - imageIntegral.s(u - 1, v + Ny - 1)
                - imageIntegral.s(u + Nx - 1, v - 1) + imageIntegral.s(u - 1, v - 1);

        return (1 / (Nx * Ny)) * f7;
    }

    public double t2() {
        double t7 = templateIntegral.sigma(0, 0, templateIntegral.cx, templateIntegral.cy);

        return (1 / (Nx * Ny)) * t7;
    }

    public double f10(int u, int v) {
        return imageIntegral2.sigma(0, 0, imageIntegral2.cx, imageIntegral2.cy) - (1 / (Nx * Ny))
                * IntegralImage2.pow2(imageIntegral.sigma(0, 0, imageIntegral.cx, imageIntegral.cy));
    }

    public float numerator(int u, int v) {
        return 0;
    }

    public float denominator(int u, int v) {
        return 0;// return Math.sqrt( f10(u, v) * Integral.pow2( - t2()) );
    }

    public float gamma(int u, int v) {
        return numerator(u, v) / denominator(u, v);
    }

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

        FNCC fnnc = new FNCC(image, template);
    }
}
