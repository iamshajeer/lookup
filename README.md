## Lookup

It is a nice, simple and friendly to use library which helps you to lookup objects on a screen. Also it has a OCR functionality.

		static public void main(String[] args) {
		    OCR l = new OCR();

		    String str = l.recognize(Lookup.load(new File("/file_to_recognize.png")));
				// str - recognized string

		    System.out.println(str);
		}
