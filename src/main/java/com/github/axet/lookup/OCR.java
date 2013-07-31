package com.github.axet.lookup;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;

import com.github.axet.lookup.common.ClassResources;
import com.github.axet.lookup.common.FontFamily;
import com.github.axet.lookup.common.FontSymbol;
import com.github.axet.lookup.common.FontSymbolLookup;
import com.github.axet.lookup.trans.CannyEdgeDetector;

public class OCR {
    Map<String, FontFamily> fontFamily = new HashMap<String, FontFamily>();

    CannyEdgeDetector detector = new CannyEdgeDetector();

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

        b = prepareImageCrop(b);

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
        bi = prepareImage(bi);

        List<FontSymbol> list = getSymbols();

        // sort list bigger images goes first so we can assume bigger images
        // have priority over small one. and if big image cross small one delete
        // small

        List<FontSymbolLookup> all = findAll(list, bi);

        return "";
    }

    List<FontSymbolLookup> findAll(List<FontSymbol> list, BufferedImage bi) {
        List<FontSymbolLookup> l = new ArrayList<FontSymbolLookup>();

        float m = 0.20f;

        Lookup.write(bi, new File("/Users/axet/Desktop/1.png"));

        for (FontSymbol fs : list) {
            Lookup.write(fs.image.gi.buf, new File("/Users/axet/Desktop/2.png"));

            for (int y = 0; y < bi.getHeight() - fs.image.getHeight(); y++) {
                for (int x = 0; x < bi.getWidth() - fs.image.getWidth(); x++) {
                    if (Lookup.findCount(bi, x, y, fs.image.gi.buf, m))
                        l.add(new FontSymbolLookup(fs, x, y));
                }
            }
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

        String str = l.recognize(Lookup.load(new File("/Users/axet/Desktop/test.png")));

        System.out.println(str);
    }
}
