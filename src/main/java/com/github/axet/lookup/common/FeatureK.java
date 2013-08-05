package com.github.axet.lookup.common;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class FeatureK {
    public Feature f;
    public List<RectK> list;

    IntegralImage template;

    public FeatureK(Feature f, IntegralImage template) {
        this.template = template;
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
            double dx = template.cx / (double) f.cx;
            double dy = template.cy / (double) f.cy;

            int w = k.x2 - k.x1 + 1;
            int h = k.y2 - k.y1 + 1;

            k.x1 *= dx;
            k.y1 *= dy;
            k.x2 = (int) (k.x1 + w * dx - 1);
            k.y2 = (int) (k.y1 + h * dy - 1);

            k.k = template.sigma(k.x1, k.y1, k.x2, k.y2);
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
        if (test(x, y))
            return new RectK(x, y);

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
        if (x >= f.cx)
            return false;
        if (y >= f.cy)
            return false;

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

        while (test(k.x2, k.y2 + 1)) {
            k.y2++;
        }

        k.scaleX = (k.x2 - k.x1 + 1.0) / f.cx;
        k.scaleY = (k.y2 - k.y1 + 1.0) / f.cy;

        return k;
    }
}
