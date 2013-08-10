package com.github.axet.lookup;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.axet.lookup.Lookup.NotFound;
import com.github.axet.lookup.common.GFirst;
import com.github.axet.lookup.common.GFirstLeftRight;
import com.github.axet.lookup.common.GPoint;
import com.github.axet.lookup.common.ImageBinary;
import com.github.axet.lookup.common.ImageBinaryGreyScale;
import com.github.axet.lookup.common.ImageBinaryGreyScaleRGB;
import com.github.axet.lookup.common.ImageBinaryScale;
import com.github.axet.lookup.proc.NCC;

public class LookupScale {

    // minimum scale used
    public double s = 0;

    // in pixels
    int defaultScaleSize;
    float m;
    float mm;

    /**
     * 
     * @param scaleSize
     *            ex:5
     * @param m
     *            ex:0.70f
     * @param mm
     *            ex:0.90f - for big templates, and 0.95f for small templates
     */
    public LookupScale(int scaleSize, float m, float mm) {
        this.defaultScaleSize = scaleSize;
        this.m = m;
        this.mm = mm;
    }

    public GPoint lookup(BufferedImage i, BufferedImage t) {
        List<GPoint> list = lookupAll(i, t);

        if (list.size() == 0)
            throw new NotFound();

        Collections.sort(list, new GFirst());

        return list.get(0);
    }

    public List<GPoint> lookupAll(BufferedImage i, BufferedImage t) {
        ImageBinaryScale templateBinary;
        if (s == 0) {
            templateBinary = new ImageBinaryGreyScaleRGB(t, defaultScaleSize);
            s = templateBinary.s;
        } else {
            templateBinary = new ImageBinaryGreyScaleRGB(t, s);
        }

        ImageBinaryScale imageBinary = new ImageBinaryGreyScaleRGB(i, s);

        return lookupAll(imageBinary, templateBinary);
    }

    public GPoint lookup(ImageBinaryScale image, ImageBinaryScale template) {
        List<GPoint> list = lookupAll(image, template);

        if (list.size() == 0)
            throw new NotFound();

        Collections.sort(list, new GFirst());

        return list.get(0);
    }

    public List<GPoint> lookupAll(ImageBinaryScale image, ImageBinaryScale template) {
        scale(image, template);

        return lookupAll(image, 0, 0, image.image.getWidth() - 1, image.image.getHeight() - 1, template);
    }

    public GPoint lookup(ImageBinaryScale image, int x1, int y1, int x2, int y2, ImageBinaryScale template) {
        scale(image, template);

        List<GPoint> list = lookupAll(image, x1, y1, x2, y2, template);

        if (list.size() == 0)
            throw new NotFound();

        Collections.sort(list, new GFirst());

        return list.get(0);
    }

    public List<GPoint> lookupAll(ImageBinaryScale image, int x1, int y1, int x2, int y2, ImageBinaryScale template) {
        scale(image, template);

        int sx1 = (int) (x1 * s);
        int sy1 = (int) (y1 * s);
        int sx2 = (int) (x2 * s);
        int sy2 = (int) (y2 * s);

        if (sy2 >= image.scaleBin.getHeight())
            sy2 = image.scaleBin.getHeight() - 1;
        if (sx2 >= image.scaleBin.getWidth())
            sx2 = image.scaleBin.getWidth() - 1;

        List<GPoint> list = NCC.lookupAll(image.scaleBin, sx1, sy1, sx2, sy2, template.scaleBin, m);

        int mx = (int) (1 / s) + 1;
        int my = (int) (1 / s) + 1;

        List<GPoint> result = new ArrayList<GPoint>();

        for (GPoint p : list) {
            Point p1 = new Point(p);

            p1.x = (int) (p1.x / s - mx);
            p1.y = (int) (p1.y / s - mx);

            Point p2 = new Point(p1);
            p2.x = template.image.getWidth() - 1 + p2.x + 2 * mx;
            p2.y = template.image.getHeight() - 1 + p2.y + 2 * my;

            if (p2.x >= image.image.getWidth())
                p2.x = image.image.getWidth() - 1;
            if (p2.y >= image.image.getHeight())
                p2.y = image.image.getHeight() - 1;

            List<GPoint> list2 = NCC.lookupAll(image.image, p1.x, p1.y, p2.x, p2.y, template.image, mm);

            result.addAll(list2);
        }

        // delete duplicates
        Collections.sort(result, new GFirstLeftRight(template.image));
        for (int k = 0; k < result.size(); k++) {
            GPoint kk = result.get(k);
            for (int j = k + 1; j < result.size(); j++) {
                GPoint jj = result.get(j);
                if (cross(template.image, kk, jj)) {
                    result.remove(jj);
                    j--;
                }
            }
        }

        for (GPoint p : result) {
            p.x += template.image.getWidth() / 2;
            p.y += template.image.getHeight() / 2;
        }

        return result;
    }

    boolean cross(ImageBinary image, GPoint i1, GPoint i2) {
        Rectangle r1 = new Rectangle(i1.x, i1.y, image.getWidth(), image.getHeight());
        Rectangle r2 = new Rectangle(i2.x, i2.y, image.getWidth(), image.getHeight());
        return r1.intersects(r2);
    }

    void scale(ImageBinaryScale image, ImageBinaryScale template) {
        if (s == 0) {
            s = template.s;
        }

        if (s == 0) {
            template.rescale(defaultScaleSize);
            s = template.s;
        }

        if (s != template.s) {
            double ss = template.project(defaultScaleSize);

            if (ss > s)
                s = ss;

            template.rescale(s);
        }

        if (s != image.s) {
            image.rescale(s);
        }
    }
}
