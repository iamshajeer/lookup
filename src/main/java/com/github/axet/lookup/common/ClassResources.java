package com.github.axet.lookup.common;

import java.io.File;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.lang.StringUtils;

/**
 * it worth to be a separate project
 * 
 * c = ClassResources(YourClass.class, "fonts") c.names();
 * 
 * fonts is a resources directory:
 * 
 * com/example/project/YourClass.class
 * 
 * com/example/project/fonts
 * 
 * TODO make it separate project
 * 
 * @author axet
 * 
 */
public class ClassResources {

    Class<?> c;
    String path;

    public ClassResources(Class<?> c) {
        this.c = c;
    }

    public ClassResources(Class<?> c, String path) {
        this.c = c;
        this.path = path;
    }

    /**
     * 
     * @param path
     * @return
     */
    public List<String> names() {
        return getResourceListing(c, path);
    }

    /**
     * enter sub directory
     * 
     * @param path
     * @return
     */
    public ClassResources dir(String path) {
        return new ClassResources(c, this.path + "/" + path);
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
        CodeSource src = cls.getProtectionDomain().getCodeSource();

        if (src == null)
            return null;

        return new File(src.getLocation().getPath());
    }

    String getClassPath(Class<?> c) {
        return new File(c.getCanonicalName().replace('.', File.separatorChar)).getParent();
    }

    String getClassPath(Class<?> c, String path) {
        return new File(c.getCanonicalName().replace('.', File.separatorChar)).getParent() + File.separator + path;
    }

    List<String> getResourceListing(Class<?> clazz, String path) {
        try {
            // clazz.getClassLoader().getResource(pp) may return system library
            // if path is common (Like "/com")
            File pp = getPath(clazz);
            if (pp.isDirectory()) {
                if (path.startsWith(File.separator))
                    pp = new File(pp, path);
                else
                    pp = new File(pp, getClassPath(clazz, path));

                return Arrays.asList(new File(pp.toURI()).list());
            }

            if (pp.isFile()) {
                String p;

                if (path.startsWith(File.separator))
                    p = StringUtils.removeStart(path, File.separator);
                else
                    p = getClassPath(clazz, path);

                JarFile jar = new JarFile(pp);
                Enumeration<JarEntry> entries = jar.entries();
                Set<String> result = new HashSet<String>();

                while (entries.hasMoreElements()) {
                    String name = entries.nextElement().getName();
                    if (name.startsWith(p)) {
                        String a = name.substring(p.length());
                        a = StringUtils.stripStart(a, File.separator);
                        a = a.trim();
                        if (!a.isEmpty())
                            result.add(a);
                    }
                }
                return new ArrayList<String>(result);
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
