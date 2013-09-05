package jsanalysis.test;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
//import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
　　* A class illustrating running line number count on JTextPane. Nothing
　　is painted on the pane itself,
　　* but a separate JPanel handles painting the line numbers.
　　*
　　* @author Daniel Sj?blom
　　* Created on Mar 3, 2004
　　* Copyright (c) 2004
　　* @version 1.0
　　*/
public class LineNr extends JPanel {
    // for this simple experiment, we keep the pane + scrollpane as members.
    //JTextPane pane;

    JTextArea pane;
    JScrollPane scrollPane;

    public LineNr() {
        super();
        setMinimumSize(new Dimension(30, 30));
        setPreferredSize(new Dimension(30, 30));
        setMinimumSize(new Dimension(30, 30));
        //pane = new JTextPane() // we need to override paint so that the linenumbers stay in sync
        pane = new JTextArea() {

            public void paint(Graphics g) {
                super.paint(g);
                LineNr.this.repaint();
            }
        };
        scrollPane = new JScrollPane(pane);
    }

    public void paint(Graphics g) {
        super.paint(g);
        // We need to properly convert the points to match the viewport
        // Read docs for viewport
        int start = pane.viewToModel(scrollPane.getViewport().getViewPosition()); // starting pos in document
        int end =pane.viewToModel(new Point(scrollPane.getViewport().getViewPosition().x + pane.getWidth(), scrollPane.getViewport().getViewPosition().y + pane.getHeight()));
    // end pos in doc
    // translate offsets to lines
    Document doc = pane.getDocument();
        int startline = doc.getDefaultRootElement().getElementIndex(start) + 1;
        int endline = doc.getDefaultRootElement().getElementIndex(end) + 1;
        int fontHeight = g.getFontMetrics(pane.getFont()).getHeight();
        int fontDesc = g.getFontMetrics(pane.getFont()).getDescent();
        int starting_y = -1;



        try {

            starting_y = pane.modelToView(start).y - scrollPane.getViewport().getViewPosition().y + fontHeight - fontDesc;
        } catch (BadLocationException e1) {
            e1.printStackTrace();
        }
        for (int line = startline, y = starting_y;line<= endline;y += fontHeight, line++) 
        {
//            g.drawString(Integer.toString(line), 20, y); 
        }
    }
// test main


    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());
        final LineNr nr = new LineNr();
        frame.getContentPane().add(nr, BorderLayout.WEST);
        frame.getContentPane().add(nr.scrollPane, BorderLayout.CENTER);
        frame.pack();
        frame.setSize(new Dimension(400, 400));
        frame.show();
    }
}