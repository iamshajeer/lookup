## Lookup

It is a nice, simple and friendly to use library which helps you to lookup objects on a screen. Also it has a OCR functionality.
Using Lookup you can do Java OCR tricks like recognizing any infromation from your Robot application. Which can be
usefull for debuging or automating things.

## OCR functionality

    package com.github.axet.lookup;
    
    import java.io.File;
    
    import com.github.axet.lookup.common.ImageBinary;
    
    public class OCRTest {
    
        static public void main(String[] args) {
            OCR l = new OCR();
    
            // will go to com/github/axet/lookup/fonts folder and load all font
            // familys (here is only font_1 family in this library)
            l.loadFontsDirectory(OCRTest.class, new File("fonts"));
    
            // example how to load only one family
            // "com/github/axet/lookup/fonts/font_1"
            l.loadFont(OCRTest.class, new File("fonts", "font_1"));
    
            String str = "";
    
            // recognize using all familys set
            str = l.recognize(Capture.load(OCRTest.class, "test3.png"));
            System.out.println(str);
    
            // recognize using only one family set
            str = l.recognize("font_1", Capture.load(OCRTest.class, "test3.png"));
            System.out.println(str);
    
            // recognize using only one family set and rectangle
            ImageBinary i = new ImageBinary(Capture.load(OCRTest.class, "full.png"));
            str = l.recognize(i, 1285, 654, 1343, 677, l.getSymbols("font_1"));
            System.out.println(str);
        }
    }

        
## Lookup methods

// cleaning up

## Central Maven Repo

        <dependency>
          <groupId>com.github.axet</groupId>
          <artifactId>lookup</artifactId>
          <version>0.0.8</version>
        </dependency>
