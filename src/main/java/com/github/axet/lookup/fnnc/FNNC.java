package com.github.axet.lookup.fnnc;

import java.awt.image.BufferedImage;

/**
 * http://isas.uka.de/Material/AltePublikationen/briechle_spie2001.pdf
 * 
 * @author axet
 * 
 */
public class FNNC {

    Integral imageIntegral;
    Integral2 imageIntegral2;

    Integral templateIntegral;

    int Nx;
    int Ny;

    int Mx;
    int My;

    public FNNC(BufferedImage image, BufferedImage template) {
        imageIntegral = new Integral(image);
        imageIntegral2 = new Integral2(image);

        templateIntegral = new Integral(template);

        Nx = template.getWidth();
        Ny = template.getHeight();

        Mx = image.getWidth();
        My = image.getHeight();

        int cx = Mx + Nx - 1;
        int cy = My + Ny - 1;

        // s = new int[cx * cy];

        for (int u = 0; u < cx - Nx; u++) {
            for (int v = 0; v < cy - Ny; v++) {
                gamma(u, v);
            }
        }
    }

    public float f2(int u, int v) {
        float f7 = imageIntegral.s(u + Nx - 1, v + Ny - 1) - imageIntegral.s(u - 1, v + Ny - 1)
                - imageIntegral.s(u + Nx - 1, v - 1) + imageIntegral.s(u - 1, v - 1);

        return (1 / (Nx * Ny)) * f7;
    }

    public float t2() {
        float t7 = templateIntegral.sigma(0, 0, templateIntegral.cx, templateIntegral.cy);

        return (1 / (Nx * Ny)) * t7;
    }

    public float f10(int u, int v) {
        return imageIntegral2.sigma(0, 0, imageIntegral2.cx, imageIntegral2.cy) - (1 / (Nx * Ny))
                * Integral2.pow2(imageIntegral.sigma(0, 0, imageIntegral.cx, imageIntegral.cy));
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
}
