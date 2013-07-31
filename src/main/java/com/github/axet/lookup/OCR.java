package com.github.axet.lookup;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;

import com.github.axet.lookup.common.ClassResources;
import com.github.axet.lookup.common.FontFamily;
import com.github.axet.lookup.common.FontSymbol;
import com.github.axet.lookup.common.FontSymbolLookup;
import com.github.axet.lookup.common.ImageBinary;
import com.github.axet.lookup.trans.CannyEdgeDetector;
import com.github.axet.lookup.trans.NCC;

public class OCR {

    static class BiggerFirst implements Comparator<FontSymbolLookup> {

        @Override
        public int compare(FontSymbolLookup arg0, FontSymbolLookup arg1) {
            return new Integer(arg1.size()).compareTo(new Integer(arg0.size()));
        }

    }

    class Left2Right implements Comparator<FontSymbolLookup> {

        public int compare(int o1, int o2, int val) {
            if (Math.abs(o1 - o2) < val)
                return 0;

            return compare(o1, o2);
        }

        // desc algorithm (high comes at first [0])
        public int compare(int o1, int o2) {
            return new Integer(o1).compareTo(new Integer(o2));
        }

        @Override
        public int compare(FontSymbolLookup arg0, FontSymbolLookup arg1) {
            int r = 0;

            if (r == 0)
                r = compare(arg0.y, arg1.y, rowHeight);

            if (r == 0)
                r = compare(arg0.x, arg1.x);

            if (r == 0)
                r = compare(arg0.y, arg1.y);

            return r;
        }
    }

    Map<String, FontFamily> fontFamily = new HashMap<String, FontFamily>();

    CannyEdgeDetector detector = new CannyEdgeDetector();

    // 1.0f == exact match, -1.0f - completely different images
    float threshold = 0.80f;
    // row height in the pixels
    int rowHeight = 10;

    public OCR() {
        detector.setLowThreshold(3f);
        detector.setHighThreshold(3f);
        detector.setGaussianKernelWidth(2);
        detector.setGaussianKernelRadius(1f);

        load(getClass(), "fonts");
    }

    public void load(Class<?> c, String path) {
        ClassResources e = new ClassResources(c, path);

        List<String> str = e.names();

        for (String s : str)
            loadFont(c, path, s);
    }

    public void loadFont(Class<?> c, String path, String name) {
        ClassResources e = new ClassResources(c, path);
        e = e.dir(name);

        List<String> str = e.names();

        for (String s : str) {
            String f = path + "/" + name + "/" + s;

            InputStream is = c.getResourceAsStream(f);

            String symbol = FilenameUtils.getBaseName(s);

            try {
                symbol = URLDecoder.decode(symbol, "UTF-8");
            } catch (UnsupportedEncodingException ee) {
                throw new RuntimeException(ee);
            }

            loadFontSymbol(name, symbol, is);
        }
    }

    BufferedImage prepareImage(BufferedImage b) {
        b = Lookup.toGray(b);

        b = Lookup.filterResizeDoubleCanvas(b);

        b = Lookup.edge(b);

        return b;
    }

    BufferedImage prepareImageCrop(BufferedImage b) {
        b = prepareImage(b);

        b = Lookup.filterRemoveCanvas(b);

        return b;
    }

    public void loadFontSymbol(String fontName, String fontSymbol, InputStream is) {
        BufferedImage b = Lookup.load(is);

        FontFamily ff = fontFamily.get(fontName);
        if (ff == null) {
            ff = new FontFamily(fontName);
            fontFamily.put(fontName, ff);
        }

        FontSymbol f = new FontSymbol(ff, fontSymbol, b);

        ff.add(f);
    }

    List<FontSymbol> getSymbols() {
        List<FontSymbol> list = new ArrayList<FontSymbol>();

        for (FontFamily f : fontFamily.values()) {
            list.addAll(f);
        }

        return list;
    }

    List<FontSymbol> getSymbols(String fontFamily) {
        return this.fontFamily.get(fontFamily);
    }

    String recognize(BufferedImage bi) {
        ImageBinary i = new ImageBinary(bi);

        List<FontSymbol> list = getSymbols();

        List<FontSymbolLookup> all = findAll(list, i);

        // bigger first.

        Collections.sort(all, new BiggerFirst());

        // big images eat small ones

        List<FontSymbolLookup> copy = new ArrayList<FontSymbolLookup>(all);

        for (int k = 0; k < copy.size(); k++) {
            FontSymbolLookup kk = copy.get(k);
            for (int j = k + 1; j < copy.size(); j++) {
                FontSymbolLookup jj = copy.get(j);
                if (kk.cross(jj))
                    all.remove(jj);
            }
        }

        // sort top/bottom/left/right

        Collections.sort(all, new Left2Right());

        // calculate rows

        String str = "";
        {
            int x = 0;
            for (FontSymbolLookup s : all) {
                if (s.x < x)
                    str += "\n";
                x = s.x;
                str += s.fs.fontSymbol;
            }
        }

        return str;
    }

    List<FontSymbolLookup> findAll(List<FontSymbol> list, ImageBinary bi) {
        List<FontSymbolLookup> l = new ArrayList<FontSymbolLookup>();

        for (FontSymbol fs : list) {
            List<Point> ll = NCC.lookup(bi, fs.image, threshold);
            for (Point p : ll)
                l.add(new FontSymbolLookup(fs, p.x, p.y));
        }

        return l;
    }

    /**
     * 
     * @param fontSet
     *            use font in the specified folder only
     * @param bi
     * @return
     */
    String recognize(String fontSet, BufferedImage bi) {
        return "";
    }

    static public void main(String[] args) {
        OCR l = new OCR();

        String str = l.recognize(Lookup.load(new File("/Users/axet/Desktop/4099.png")));

        System.out.println(str);
    }
}
