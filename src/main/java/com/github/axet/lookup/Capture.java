package com.github.axet.lookup;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;

import com.github.axet.desktop.Desktop;
import com.github.axet.desktop.DesktopFolders;

public class Capture {

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

    static public BufferedImage load(String path, Rectangle rect) {
        BufferedImage src = load(path);

        BufferedImage dest = new BufferedImage((int) rect.getWidth(), (int) rect.getHeight(), src.getType());
        Graphics g = dest.getGraphics();
        g.drawImage(src, 0, 0, (int) rect.getWidth(), (int) rect.getHeight(), (int) rect.getX(), (int) rect.getY(),
                (int) rect.getX() + (int) rect.getWidth(), (int) rect.getY() + (int) rect.getHeight(), null);
        g.dispose();

        return dest;
    }

    static public void writeDesktop(BufferedImage img, Rectangle rr, String file) {
        BufferedImage i = new BufferedImage(rr.width, rr.height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = i.getGraphics();
        g.drawImage(img, 0, 0, rr.width, rr.height, rr.x, rr.y, rr.x + rr.width, rr.y + rr.height, null);
        g.dispose();

        writeDesktop(i, file);
    }

    static public void writeDesktop(BufferedImage img) {
        writeDesktop(img, Long.toString(System.currentTimeMillis()));
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

    static public BufferedImage load(String path) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(path));
            return img;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
