package jsanalysis.ui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;



import javax.swing.JTextArea;
import javax.swing.border.AbstractBorder;

public class LineNumberBorder extends AbstractBorder {

    public LineNumberBorder() {
    }

    /*Insets 对象是容器边界的表示形式。
    它指定容器必须在其各个边缘留出的空间。
     */
    //此方法在实例化时自动调用
    //此方法关系到边框是否占用组件的空间
    public Insets getBorderInsets(Component c) {
        return getBorderInsets(c, new Insets(0, 0, 0, 0));
    }

    public Insets getBorderInsets(Component c, Insets insets) {
        if (c instanceof JTextArea) {
            int width = lineNumberWidth((JTextArea) c);
            insets.left = width;
        }
        return insets;

    }

    public boolean isBorderOpaque() {
        return false;
    }
    //边框的绘制方法
    //此方法必须实现

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        //获得当前剪贴区域的边界矩形。
        java.awt.Rectangle clip = g.getClipBounds();
        FontMetrics fm = g.getFontMetrics();
        int fontHeight = fm.getHeight();

        //starting location at the "top" of the page...
        // y is the starting baseline for the font...
        int ybaseline = y + fm.getAscent();

        // now determine if it is the "top" of the page...or somewhere else
        int startingLineNumber = (clip.y / fontHeight) + 1;

        if (startingLineNumber != 1) {
            ybaseline = y + startingLineNumber * fontHeight
                    - (fontHeight - fm.getAscent());
        }

        int yend = ybaseline + height;
        if (yend > (y + height)) {
            yend = y + height;
        }

        JTextArea jta = (JTextArea) c;
        int lineWidth = lineNumberWidth(jta);

        int lnxStart = x + lineWidth;

//        g.setColor(Color.BLACK);


        // loop until out of the "visible" region...
        int length = ("" + Math.max(jta.getRows(), jta.getLineCount() + 1)).length();
        //绘制行号
        while (ybaseline < yend) {
            String label = padLabel(startingLineNumber, length, true);

            g.drawString(label, 40- fm.stringWidth(label), ybaseline);
            ybaseline += fontHeight;
            startingLineNumber++;
        }
    }

    //寻找适合的数字宽度
    private int lineNumberWidth(JTextArea jta) {
        int lineCount = Math.max(jta.getRows(), jta.getLineCount());
        return jta.getFontMetrics(new Font("黑体", Font.PLAIN, 18)).stringWidth(lineCount + " ");
    }

    private static String padLabel(int lineNumber, int length, boolean addSpace) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(lineNumber);
        for (int count = (length - buffer.length()); count > 0; count--) {
            buffer.insert(0, ' ');
        }
        if (addSpace) {
            buffer.append(' ');
        }
        return buffer.toString();
    }
}
