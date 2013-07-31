package com.github.axet.lookup;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.github.axet.lookup.common.ImageBinary;
import com.github.axet.lookup.common.RangeColor;
import com.github.axet.lookup.trans.CannyEdgeDetector;
import com.github.axet.lookup.trans.NCC;

public class Lookup {

    public static class NotFound extends RuntimeException {
        private static final long serialVersionUID = 5393563026702192412L;

        public NotFound() {
            super("NotFound");
        }
    }

    public BufferedImage convert(Image image) {
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null),
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = bufferedImage.createGraphics();
        g2.drawImage(image, null, null);
        return bufferedImage;
    }

    public BufferedImage crop(BufferedImage image, Rectangle r) {
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null),
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = bufferedImage.createGraphics();
        g2.drawImage(image, null, null);
        g2.setColor(new Color(0xff0000ff));
        g2.fillRect(r.x, r.y, r.width, r.height);
        return bufferedImage;
    }

    public BufferedImage filterResize(BufferedImage bi) {
        int cx = bi.getWidth() / 7;
        int cy = bi.getHeight() / 7;
        BufferedImage resizedImage = new BufferedImage(cx, cy, bi.getType());
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(bi, 0, 0, cx, cy, null);
        g.dispose();

        return resizedImage;
    }

    static public BufferedImage filterResizeDouble(BufferedImage bi) {
        int cx = bi.getWidth() * 2;
        int cy = bi.getHeight() * 2;
        BufferedImage resizedImage = new BufferedImage(cx, cy, bi.getType());
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(bi, 0, 0, cx, cy, null);
        g.dispose();
        return resizedImage;
    }

    static public BufferedImage filterResizeDoubleCanvas(BufferedImage bi) {
        int cx = bi.getWidth() * 4;
        int cy = bi.getHeight() * 4;
        BufferedImage resizedImage = new BufferedImage(cx, cy, bi.getType());
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(bi, cx / 4, cy / 4, cx / 2, cy / 2, null);
        g.dispose();

        return resizedImage;
    }

    static public BufferedImage filterShrinkDouble(BufferedImage bi) {
        int cx = bi.getWidth() / 2;
        int cy = bi.getHeight() / 2;
        BufferedImage resizedImage = new BufferedImage(cx, cy, bi.getType());
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(bi, 0, 0, cx, cy, null);
        g.dispose();

        return resizedImage;
    }

    static public BufferedImage filterRemoveCanvas(BufferedImage bi) {
        int x1 = bi.getWidth();
        int x2 = 0;

        int y1 = bi.getHeight();
        int y2 = 0;

        for (int x = 0; x < bi.getWidth(); x++) {
            for (int y = 0; y < bi.getHeight(); y++) {
                if ((bi.getRGB(x, y) & 0xffffff) != 0) {
                    if (x1 > x)
                        x1 = x;
                    if (x2 < x)
                        x2 = x;
                    if (y1 > y)
                        y1 = y;
                    if (y2 < y)
                        y2 = y;
                }
            }
        }

        int cx = x2 - x1;
        int cy = y2 - y1;

        BufferedImage dest = new BufferedImage(cx, cy, bi.getType());
        Graphics g = dest.getGraphics();
        g.drawImage(bi, 0, 0, (int) dest.getWidth(), (int) dest.getHeight(), x1, y1, x1 + dest.getWidth(),
                y1 + dest.getHeight(), null);
        g.dispose();

        return dest;
    }

    public BufferedImage filterBlur(BufferedImage bi) {
        BufferedImage buff = new BufferedImage(bi.getWidth(), bi.getHeight(), bi.getType());

        float n = 1f / 25f;
        Kernel kernel = new Kernel(5, 5, new float[] { n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n,
                n, n, n, n });

        ConvolveOp op = new ConvolveOp(kernel);
        op.filter(bi, buff);

        return buff;
    }

    static BufferedImage loadImageIcon(String path) {
        ImageIcon icon = new ImageIcon(path);
        BufferedImage bi = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.createGraphics();
        icon.paintIcon(null, g, 0, 0);
        g.dispose();
        return bi;
    }

    static boolean find(BufferedImage bi, int x, int y, BufferedImage icon, float m) {
        for (int yy = 0; yy < icon.getHeight(); yy++) {
            for (int xx = 0; xx < icon.getWidth(); xx++) {
                int rgb1 = icon.getRGB(xx, yy);
                int rgb2 = bi.getRGB(x + xx, y + yy);
                RangeColor r = new RangeColor(rgb1, m);
                if (!r.inRange(rgb2))
                    return false;
            }
        }

        return true;
    }

    static boolean findCount(BufferedImage bi, int x, int y, BufferedImage icon, float m) {
        int c = 1;
        int t = 1;
        for (int yy = 0; yy < icon.getHeight(); yy++) {
            for (int xx = 0; xx < icon.getWidth(); xx++) {
                int rgb1 = icon.getRGB(xx, yy);
                int rgb2 = bi.getRGB(x + xx, y + yy);
                if ((rgb1 & 0xffffff) > 0 || (rgb2 & 0xffffff) > 0) {
                    t++;
                    if (rgb1 != rgb2) {
                        c++;
                    }
                }
            }
        }

        return (c / (float) t) < m;
    }

    public static Point lookup(BufferedImage bi, BufferedImage icon) {
        return lookup(bi, icon, 0.10f);
    }

    public static Point lookup(ImageBinary bi, ImageBinary icon) {
        List<Point> list = NCC.lookup(bi, icon, 0.80f);
        if (list.size() != 1)
            throw new NotFound();
        return list.get(0);
    }

    public static Point lookup(BufferedImage bi, BufferedImage icon, float m) {
        for (int y = 0; y < bi.getHeight() - icon.getHeight(); y++) {
            for (int x = 0; x < bi.getWidth() - icon.getWidth(); x++) {
                if (find(bi, x, y, icon, m))
                    return new Point(x, y);
            }
        }

        return null;
    }

    static public BufferedImage createImageIcon(String path) {
        java.net.URL imgURL = Lookup.class.getResource(path);
        ImageIcon icon = new ImageIcon(imgURL);

        BufferedImage bi = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.createGraphics();
        icon.paintIcon(null, g, 0, 0);
        g.dispose();

        return bi;
    }

    static public Point lookupImage(BufferedImage bi, BufferedImage exit) {
        return lookupImage(bi, exit, 0.10f);
    }

    /**
     * lookup center of image
     * 
     * @param bi
     * @param exit
     * @param m
     * @return
     */
    static public Point lookupImage(BufferedImage bi, BufferedImage exit, float m) {
        Point pul = lookup(bi, exit, m);
        if (pul == null)
            throw new NotFound();

        int x = pul.x + exit.getWidth() / 2;
        int y = pul.y + exit.getHeight() / 2;
        return new Point(x, y);
    }

    static public Point lookupInsideRect(BufferedImage bi, BufferedImage i, Point pul, Point pdr) {
        return lookupInsideRect(bi, i, pul, pdr, 0.10f);
    }

    static public Point lookupInsideRect(ImageBinary bi, ImageBinary i, Point pul, Point pdr) {
        List<Point> list = NCC.lookup(bi, pul.x, pul.y, pdr.x, pdr.y, i, 0.80f);

        if (list.size() != 1)
            throw new NotFound();

        return list.get(0);
    }

    static public BufferedImage crop(BufferedImage src, Point pul, Point pdr) {
        BufferedImage dest = new BufferedImage(pdr.x - pul.x, pdr.y - pul.y, src.getType());
        Graphics g = dest.getGraphics();
        g.drawImage(src, 0, 0, (int) dest.getWidth(), (int) dest.getHeight(), pul.x, pul.y, pul.x + dest.getWidth(),
                pul.y + dest.getHeight(), null);
        g.dispose();

        return dest;
    }

    static public Point lookupInsideRect(BufferedImage bi, BufferedImage i, Point pul, Point pdr, float p) {
        bi = crop(bi, pul, pdr);
        Point p3 = lookupImage(bi, i, p);
        p3.x += pul.x;
        p3.y += pul.y;

        return p3;
    }

    static public BufferedImage filterSimply(BufferedImage bi) {
        BufferedImage buff = new BufferedImage(bi.getWidth(), bi.getHeight(), bi.getType());

        float n = 1f / 25f;
        Kernel kernel = new Kernel(5, 5, new float[] { n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n,
                n, n, n, n });

        ConvolveOp op = new ConvolveOp(kernel);
        op.filter(bi, buff);

        return buff;
    }

    static public void write(BufferedImage img, File f) {
        try {
            ImageIO.write(img, "PNG", f);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static public BufferedImage load(File f) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(f);
            return img;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static public BufferedImage load(InputStream f) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(f);
            return img;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static public BufferedImage toGray(BufferedImage bi) {
        BufferedImage out = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_BYTE_GRAY);

        ColorConvertOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
        op.filter(bi, out);

        return out;
    }

    static public BufferedImage edge(BufferedImage bi) {
        CannyEdgeDetector detector = new CannyEdgeDetector();

        detector.setLowThreshold(3f);
        detector.setHighThreshold(3f);
        detector.setGaussianKernelWidth(2);
        detector.setGaussianKernelRadius(1f);

        detector.setSourceImage(bi);
        detector.process();

        return detector.getEdgesImage();
    }

}
