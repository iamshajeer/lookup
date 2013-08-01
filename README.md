## Lookup

It is a nice, simple and friendly to use library which helps you to lookup objects on a screen. Also it has a OCR functionality.

        static public void main(String[] args) {
            OCR l = new OCR();
    
            // will go to com/github/axet/lookup/fonts folder and load all font
            // familys (here is only font_1 family in this library)
            l.loadFontsDirectory(OCR.class, new File("fonts"));
    
            // example how to load only one family
            // "com/github/axet/lookup/fonts/font_1"
            l.loadFont(OCR.class, new File("fonts", "font_1"));
    
            String str = "";
    
            // recognize using all familys set
            str = l.recognize(Lookup.load(new File("/Users/axet/Desktop/test3.png")));
    
            // recognize using only one family set
            str = l.recognize("font_1", Lookup.load(new File("/Users/axet/Desktop/test3.png")));
    
            // str = recognized string
            System.out.println(str);
        }

## Central Maven Repo

        <dependency>
          <groupId>com.github.axet</groupId>
          <artifactId>lookup</artifactId>
          <version>0.0.5</version>
        </dependency>
