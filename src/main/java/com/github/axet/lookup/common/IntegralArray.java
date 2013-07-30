package com.github.axet.lookup.common;

import java.awt.image.BufferedImage;

import com.github.axet.lookup.Capture;

public class IntegralArray extends SArray {
    BufferedImage buf;

    int f(int x, int y) {
        return buf.getRGB(x, y);
    }

    public IntegralArray(BufferedImage buf) {
        this.buf = buf;

        cx = buf.getWidth();
        cy = buf.getHeight();

        s = new int[cx * cy];
    }

    public void writeDebug() {
        BufferedImage image = new BufferedImage(cx, cy, BufferedImage.TYPE_INT_ARGB);
        image.getWritableTile(0, 0).setDataElements(0, 0, image.getWidth(), image.getHeight(), s);
        Capture.writeDesktop(image);
    }

}
