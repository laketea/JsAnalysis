/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jsanalysis.test;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author zhangwei <laketea@163.com>
 */
public class ImageUtils {

    public void exportImg(JComponent jcp,String path) throws IOException {


        BufferedImage img = ScreenImage.createImage(jcp);

        System.out.println("export img path:"+path);
        int w = img.getWidth();
        int h = img.getHeight();

        BufferedImage tag = new BufferedImage((int) w, (int) h,
                BufferedImage.TYPE_INT_RGB);

        tag.getGraphics().drawImage(img.getScaledInstance(w, h, Image.SCALE_SMOOTH), 0, 0, null);

        FileOutputStream out = new FileOutputStream(path);
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
        encoder.encode(tag);
        out.close();



    }

    public static void main(String[] args) throws IOException {

        JPanel pnl = new JPanel();
        pnl.setBackground(new java.awt.Color(255, 255, 255));
        pnl.setLayout(new java.awt.BorderLayout());
//        pnl.setPreferredSize(new Dimension(3000, 200));
        pnl.add(new JLabel("test"));
//        (new ImageUtils()).test(pnl);
    }
}
