package com.github.axet.lookup;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.axet.lookup.Lookup.NotFound;
import com.github.axet.lookup.common.GFirst;
import com.github.axet.lookup.common.GPoint;
import com.github.axet.lookup.common.ImageBinaryGreyScale;
import com.github.axet.lookup.proc.NCC;

public class LookupScale {

    // minimum scale used
    double s = 0;

    // in pixels
    int defaultScaleSize;

    public LookupScale(int scaleSize) {
        this.defaultScaleSize = scaleSize;
    }

    public GPoint lookup(BufferedImage i, BufferedImage t, float m, float mm) {
        List<GPoint> list = lookupAll(i, t, m, mm);

        if (list.size() == 0)
            throw new NotFound();

        Collections.sort(list, new GFirst());

        return list.get(0);
    }

    public List<GPoint> lookupAll(BufferedImage i, BufferedImage t, float m, float mm) {
        ImageBinaryGreyScale templateBinary;
        if (s == 0) {
            templateBinary = new ImageBinaryGreyScale(t, defaultScaleSize);
            s = templateBinary.s;
        } else {
            templateBinary = new ImageBinaryGreyScale(t, s);
        }

        ImageBinaryGreyScale imageBinary = new ImageBinaryGreyScale(i, s);

        return lookupAll(imageBinary, templateBinary, m, mm);
    }

    public GPoint lookup(ImageBinaryGreyScale image, ImageBinaryGreyScale template, float m, float mm) {
        List<GPoint> list = lookupAll(image, template, m, mm);

        if (list.size() == 0)
            throw new NotFound();

        Collections.sort(list, new GFirst());

        return list.get(0);
    }

    public List<GPoint> lookupAll(ImageBinaryGreyScale image, ImageBinaryGreyScale template, float m, float mm) {
        scale(image, template);

        return lookupAll(image, 0, 0, image.scaleBin.getWidth() - 1, image.scaleBin.getHeight() - 1, template, m, mm);
    }

    public Point lookup(ImageBinaryGreyScale image, int x1, int y1, int x2, int y2, ImageBinaryGreyScale template,
            float m, float mm) {
        scale(image, template);

        List<GPoint> list = lookupAll(image, 0, 0, image.scaleBin.getWidth() - 1, image.scaleBin.getHeight() - 1,
                template, m, mm);

        if (list.size() == 0)
            throw new NotFound();

        Collections.sort(list, new GFirst());

        return list.get(0);
    }

    public List<GPoint> lookupAll(ImageBinaryGreyScale image, int x1, int y1, int x2, int y2,
            ImageBinaryGreyScale template, float m, float mm) {
        scale(image, template);

        List<GPoint> list = NCC.lookupAll(image.scaleBin, x1, y1, x2, y2, template.scaleBin, m);

        int mx = (int) (1 / s) + 1;
        int my = (int) (1 / s) + 1;

        List<GPoint> result = new ArrayList<GPoint>();

        for (GPoint p : list) {
            Point p1 = p;

            p1.x = (int) (p1.x / s - mx);
            p1.y = (int) (p1.y / s - mx);

            Point p2 = new Point(p1);
            p2.x = template.image.getWidth() - 1 + p2.x + 2 * mx;
            p2.y = template.image.getHeight() - 1 + p2.y + 2 * my;

            List<GPoint> list2 = NCC.lookupAll(image.image, p1.x, p1.y, p2.x, p2.y, template.image, mm);

            result.addAll(list2);
        }

        for (GPoint p : result) {
            p.x += template.image.getWidth() / 2;
            p.y += template.image.getHeight() / 2;
        }

        return result;
    }

    void scale(ImageBinaryGreyScale image, ImageBinaryGreyScale template) {
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
