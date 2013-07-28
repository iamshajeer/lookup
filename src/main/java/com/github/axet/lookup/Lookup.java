package com.github.axet.lookup;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import com.github.axet.lookup.fnnc.FNNC;

public class Lookup {

    public static class FontFamily extends ArrayList<FontSymbol> {
        private static final long serialVersionUID = 3279037448543102425L;

        public String name;

        public FontFamily(String name) {
            this.name = name;
        }
    }

    public static class FontSymbol {
        public FontFamily fontFamily;
        public String fontSymbol;
        public BufferedImage image;

        public FontSymbol(FontFamily ff, String fs, BufferedImage i) {
            this.fontFamily = ff;
            this.fontSymbol = fs;
            this.image = i;
        }
    }

    public static class FontSymbolLookup {
        public int x;
        public int y;
        public FontSymbol fs;

        public FontSymbolLookup(FontSymbol fs, int x, int y) {
            this.fs = fs;
            this.x = x;
            this.y = y;
        }
    }

    Map<String, FontFamily> fontFamily = new HashMap<String, FontFamily>();

    CannyEdgeDetector detector = new CannyEdgeDetector();

    public Lookup() {
        detector.setLowThreshold(3f);
        detector.setHighThreshold(3f);
        detector.setGaussianKernelWidth(2);
        detector.setGaussianKernelRadius(1f);

        load(getClass(), "fonts");
    }

    public void load(Class<?> c, String path) {
        String[] str = getResourceListing(c, path);

        for (String s : str)
            loadFont(c, path, s);
    }

    public void loadFont(Class<?> c, String path, String name) {
        String[] str = getResourceListing(c, path + "/" + name);

        for (String s : str) {
            String f = path + "/" + name + "/" + s;

            InputStream is = c.getResourceAsStream(f);

            String symbol = FilenameUtils.getBaseName(s);

            try {
                symbol = URLDecoder.decode(symbol, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }

            loadFontSymbol(name, symbol, is);
        }
    }

    BufferedImage prepareImage(BufferedImage b) {
        b = LookupMethods.toGray(b);

        b = LookupMethods.filterResizeDoubleCanvas(b);

        b = LookupMethods.edge(b);

        return b;
    }

    BufferedImage prepareImageCrop(BufferedImage b) {
        b = prepareImage(b);

        b = LookupMethods.filterRemoveCanvas(b);

        return b;
    }

    public void loadFontSymbol(String fontName, String fontSymbol, InputStream is) {
        BufferedImage b = LookupMethods.load(is);

        b = prepareImageCrop(b);

        FontFamily ff = fontFamily.get(fontName);
        if (ff == null) {
            ff = new FontFamily(fontName);
            fontFamily.put(fontName, ff);
        }

        FontSymbol f = new FontSymbol(ff, fontSymbol, b);

        ff.add(f);
    }

    // 1) under debugger, /Users/axet/source/mircle/play/target/classes/
    //
    // 2) app packed as one jar, mac osx wihtout debugger path -
    // /Users/axet/source/mircle/mircle/macosx/Mircle.app/Contents/Resources/Java/mircle.jar
    // case above 1) works prefectly
    //
    // 3) if it is a separate library packed with maven under debugger
    // /Users/axet/.m2/repository/com/github/axet/play/0.0.3/play-0.0.3.jar
    File getPath(Class<?> cls) {
        String path;

        CodeSource src = cls.getProtectionDomain().getCodeSource();

        if (src == null)
            return null;

        return new File(src.getLocation().getPath());
    }

    String getClassPath(Class c) {
        return new File(c.getCanonicalName().replace('.', '/')).getParent();
    }

    String getClassPath(Class c, String path) {
        return new File(c.getCanonicalName().replace('.', '/')).getParent() + "/" + path;
    }

    String[] getResourceListing(Class clazz, String path) {
        try {
            // clazz.getClassLoader().getResource(pp) may return system library
            // if path is common (Like "/com")
            File pp = getPath(clazz);
            if (pp.isDirectory()) {
                if (path.startsWith("/"))
                    pp = new File(pp, path);
                else
                    pp = new File(pp, getClassPath(clazz, path));

                return new File(pp.toURI()).list();
            }

            if (pp.isFile()) {
                String p;

                if (path.startsWith("/"))
                    p = StringUtils.stripStart(path, "/");
                else
                    p = getClassPath(clazz, path);

                JarFile jar = new JarFile(pp);
                Enumeration<JarEntry> entries = jar.entries();
                Set<String> result = new HashSet<String>();

                while (entries.hasMoreElements()) {
                    String name = entries.nextElement().getName();
                    if (name.startsWith(p)) {
                        String a = name.substring(p.length());
                        a = StringUtils.stripStart(a, "/");
                        a = a.trim();
                        if (!a.isEmpty())
                            result.add(a);
                    }
                }
                return result.toArray(new String[result.size()]);
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
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

        LookupMethods.write(bi, new File("/Users/axet/Desktop/1.png"));

        for (FontSymbol fs : list) {
            LookupMethods.write(fs.image, new File("/Users/axet/Desktop/2.png"));

            for (int y = 0; y < bi.getHeight() - fs.image.getHeight(); y++) {
                for (int x = 0; x < bi.getWidth() - fs.image.getWidth(); x++) {
                    if (LookupMethods.findCount(bi, x, y, fs.image, m))
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
        
        FNNC f = new FNNC(null, null);
        
        Lookup l = new Lookup();

        String str = l.recognize(LookupMethods.load(new File("/Users/axet/Desktop/test.png")));

        System.out.println(str);
    }
}
