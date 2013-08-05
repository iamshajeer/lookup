package com.github.axet.lookup.common;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class FeatureK {

    static class RectK implements Comparable<RectK> {
        public int x1;
        public int y1;
        public int x2;
        public int y2;

        public double scaleX;
        public double scaleY;

        public double k;

        public RectK(int x, int y) {
            x1 = x;
            y1 = y;
            x2 = x;
            y2 = y;
        }

        public boolean equal(RectK k) {
            return x1 == k.x1 && x2 == k.x2 && y1 == k.y1 && y2 == k.y2;
        }

        @Override
        public int compareTo(RectK arg0) {
            int r = 0;
            if (r == 0)
                r = new Integer(x1).compareTo(arg0.x1);
            if (r == 0)
                r = new Integer(y1).compareTo(arg0.y1);
            if (r == 0)
                r = new Integer(x2).compareTo(arg0.x2);
            if (r == 0)
                r = new Integer(y2).compareTo(arg0.y2);
            return r;
        }
    }

    public Feature f;
    public int x1;
    public int y1;
    public int x2;
    public int y2;
    public List<RectK> list;

    IntegralImage image;

    public FeatureK(Feature f, IntegralImage image) {
        this.image = image;
        this.f = f;

        Set<RectK> list = new TreeSet<RectK>();

        for (int x = 0; x < f.cx; x++) {
            for (int y = 0; y < f.cy; y++) {
                RectK k = rectNearFill(x, y);
                list.add(k);
            }
        }

        this.list = Arrays.asList(list.toArray(new RectK[] {}));

        for (RectK k : this.list) {
            double dx = image.cx / f.cx;
            double sx = k.scaleX * dx;
            double dy = image.cy / f.cy;
            double sy = k.scaleY * dy;
            
            ;
        }
    }

    RectK rectNearFill(int x, int y) {
        RectK k = near(x, y);
        k = fill(k);
        return k;
    }

    int limX(int i) {
        if (i < 0)
            return 0;

        if (i >= f.cx)
            return f.cx - 1;

        return i;
    }

    int limY(int i) {
        if (i < 0)
            return 0;

        if (i >= f.cy)
            return f.cy - 1;

        return i;
    }

    RectK near(int x, int y) {
        int m = Math.max(f.cx, f.cy);
        for (int r = 1; r < m; r++) {
            int xx;
            int xxm;
            int yy;
            int yym;

            // top - bottom
            xx = limX(x - r);
            yym = limY(y + r);
            for (yy = limY(y - r); yy <= yym; yy++) {
                if (test(xx, yy))
                    return new RectK(xx, yy);
            }
            // left - right
            yy = limY(y + r);
            xxm = limX(x + r);
            for (xx = limX(x - r); xx <= xxm; xx++) {
                if (test(xx, yy))
                    return new RectK(xx, yy);
            }
            // down - top
            xx = limX(x + r);
            yym = limY(y - r);
            for (yy = limY(y + r); yy >= yym; yy--) {
                if (test(xx, yy))
                    return new RectK(xx, yy);
            }
            // right - left
            yy = limY(y - r);
            xxm = limX(x - r);
            for (xx = limX(x + r); xx >= xxm; xx--) {
                if (test(xx, yy))
                    return new RectK(xx, yy);
            }
        }

        throw new RuntimeException("no 1 found");
    }

    boolean test(int x, int y) {
        if (f.s(x, y) == 1)
            return true;

        return false;
    }

    RectK fill(RectK k) {
        while (test(k.x1 - 1, k.y1)) {
            k.x1--;
        }

        while (test(k.x1, k.y1 - 1)) {
            k.y1--;
        }

        while (test(k.x2 + 1, k.y1)) {
            k.x2++;
        }

        while (test(k.x2, k.y1 + 1)) {
            k.y2++;
        }

        k.scaleX = (k.x2 - k.x1 + 1.0) / f.cx;
        k.scaleY = (k.y2 - k.y1 + 1.0) / f.cy;

        return k;
    }
}
