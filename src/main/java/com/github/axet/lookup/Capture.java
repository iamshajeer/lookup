package com.github.axet.lookup;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.commons.io.FileUtils;

import com.github.axet.desktop.Desktop;
import com.github.axet.desktop.DesktopFolders;

public class Capture {

    public static ColorModel getColorModel() {
        GraphicsDevice d = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        return d.getDefaultConfiguration().getColorModel();
    }

    public static GraphicsDevice getScreenDevice(int screenNumber) throws Exception {
        GraphicsDevice[] screens = getScreenDevices();
        if (screenNumber >= screens.length) {
            throw new Exception("CanvasFrame Error: Screen number " + screenNumber + " not found. " + "There are only "
                    + screens.length + " screens.");
        }
        return screens[screenNumber];// .getDefaultConfiguration();
    }

    public static GraphicsDevice[] getScreenDevices() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
    }

    //
    // capture
    //

    static public BufferedImage capture() {
        try {
            Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
            return capture(new Rectangle(size));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static public BufferedImage capture(Rectangle rec) {
        try {
            Robot robot = new Robot();
            BufferedImage img = robot.createScreenCapture(rec);
            return img;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //
    // crop
    //

    static public BufferedImage crop(BufferedImage src, Point pul, Point pdr) {
        BufferedImage dest = new BufferedImage(pdr.x - pul.x, pdr.y - pul.y, src.getType());
        Graphics g = dest.getGraphics();
        g.drawImage(src, 0, 0, (int) dest.getWidth(), (int) dest.getHeight(), pul.x, pul.y, pul.x + dest.getWidth(),
                pul.y + dest.getHeight(), null);
        g.dispose();

        return dest;
    }

    static public BufferedImage crop(BufferedImage image, Rectangle r) {
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null),
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = bufferedImage.createGraphics();
        g2.drawImage(image, null, null);
        g2.setColor(new Color(0xff0000ff));
        g2.fillRect(r.x, r.y, r.width, r.height);
        return bufferedImage;
    }

    //
    // load / save
    //

    static public void writeDesktop(BufferedImage img, Rectangle rr, String file) {
        BufferedImage i = new BufferedImage(rr.width, rr.height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = i.getGraphics();
        g.drawImage(img, 0, 0, rr.width, rr.height, rr.x, rr.y, rr.x + rr.width, rr.y + rr.height, null);
        g.dispose();

        writeDesktop(i, file);
    }

    static public void writeDesktop(BufferedImage img) {
        writeDesktop(img, Long.toString(System.currentTimeMillis()) + ".png");
    }

    static public void writeDesktop(BufferedImage img, String file) {
        DesktopFolders d = Desktop.getDesktopFolders();
        File f = FileUtils.getFile(d.getDesktop(), file);
        write(img, f);
    }

    static public void write(BufferedImage img, File f) {
        try {
            ImageIO.write(img, "PNG", f);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static public BufferedImage load(File path, Rectangle rect) {
        BufferedImage src = load(path);

        BufferedImage dest = new BufferedImage((int) rect.getWidth(), (int) rect.getHeight(), src.getType());
        Graphics g = dest.getGraphics();
        g.drawImage(src, 0, 0, (int) rect.getWidth(), (int) rect.getHeight(), (int) rect.getX(), (int) rect.getY(),
                (int) rect.getX() + (int) rect.getWidth(), (int) rect.getY() + (int) rect.getHeight(), null);
        g.dispose();

        return dest;
    }
    
    static public BufferedImage load(File path) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(path);
            return img;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static public BufferedImage load(Class<?> c, String path) {
        return load(c.getResourceAsStream(path));
    }
    
    static public BufferedImage load(InputStream path) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(path);
            return img;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static BufferedImage loadImageIcon(String path) {
        ImageIcon icon = new ImageIcon(path);
        BufferedImage bi = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.createGraphics();
        icon.paintIcon(null, g, 0, 0);
        g.dispose();
        return bi;
    }

}
