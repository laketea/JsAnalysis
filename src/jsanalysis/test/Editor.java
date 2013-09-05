package jsanalysis.test;

import java.awt.*;
import java.awt.event.*;
import java.lang.StringBuffer;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

class MyEditorKit extends DefaultEditorKit {

    public MyEditorKit() {
        super();
    }

    public ViewFactory getViewFactory() {
        return new MyViewFactory();
    }
}

class MyViewFactory implements ViewFactory {

    public MyViewFactory() {
    }

    public View create(Element element) {
        return new MyEditorView(element);
    }
}

class MyEditorView extends PlainView {

    public MyEditorView(Element element) {
        super(element);
    }

    protected int drawUnselectedText(Graphics g, int x, int y, int p0, int p1)
            throws BadLocationException {
        Document doc = getDocument();
        Segment segment = new Segment(), token = new Segment();
        int index = 0, count = p1 - p0;
        char c = '\u0000';

        doc.getText(p0, count, segment);
        for (int i = 0; i < count; i++) {
            if (Character.isLetter(c = segment.array[segment.offset + i])) {
                index = i;
                while (++i < count && Character.isLetter(segment.array[segment.offset + i]));
                doc.getText(p0 + index, (i--) - index, token);
                if (KeyWord.isKeyWord(token)) {
                    g.setFont(KEYWORDFONT);
                    g.setColor(KEYWORDCOLOR);
                } else {
                    g.setFont(TEXTFONT);
                    g.setColor(TEXTCOLOR);
                }
                x = Utilities.drawTabbedText(token, x, y, g, this, p0 + index);
                continue;
            } else if (c == '/') {
                index = i;
                if (++i < count && segment.array[segment.offset + i] == '/') {
                    doc.getText(p0 + index, count - index, token);
                    g.setFont(COMMENTFONT);
                    g.setColor(COMMENTCOLOR);
                    x = Utilities.drawTabbedText(token, x, y, g, this, p0 + index);
                    break;
                }
                doc.getText(p0 + index, 1, token);
                g.setFont(TEXTFONT);
                g.setColor(TEXTCOLOR);
                x = Utilities.drawTabbedText(token, x, y, g, this, p0 + index);
                i--;
                continue;
            } else if (c == '\'' || c == '\"') {
                index = i;
                char ch = '\u0000';
                while (++i < count) {
                    if ((ch = segment.array[segment.offset + i]) == '\\') {
                        i++;
                        continue;
                    } else if (ch == c) {
                        break;
                    }
                }
                if (i >= count) {
                    i = count - 1;
                }
                doc.getText(p0 + index, i - index + 1, token);
                g.setFont(STRINGFONT);
                g.setColor(STRINGCOLOR);
                x = Utilities.drawTabbedText(token, x, y, g, this, p0 + index);
                continue;
            } else {
                index = i;
                while (++i < count && !Character.isLetter((c = segment.array[segment.offset + i])) && c != '/' && c != '\'' && c != '\"');
                doc.getText(p0 + index, (i--) - index, token);
                g.setFont(TEXTFONT);
                g.setColor(TEXTCOLOR);
                x = Utilities.drawTabbedText(token, x, y, g, this, p0 + index);
            }
        }
        return x;
    }

    protected int drawSelectedText(Graphics g, int x, int y, int p0, int p1)
            throws BadLocationException {
        g.setFont(TEXTFONT);
        g.setColor(TEXTCOLOR);
        return super.drawSelectedText(g, x, y, p0, p1);
    }
    public static Font TEXTFONT = new Font("DialogInput", Font.PLAIN, 11);
    public static Color TEXTCOLOR = Color.black;
    public static Font KEYWORDFONT = new Font(TEXTFONT.getFontName(), Font.BOLD, TEXTFONT.getSize());
    public static Color KEYWORDCOLOR = new Color(0, 0, 128);
    public static Font COMMENTFONT = TEXTFONT;
    public static Color COMMENTCOLOR = new Color(192, 192, 192);
    public static Font STRINGFONT = TEXTFONT;
    public static Color STRINGCOLOR = new Color(255, 0, 0);
}

class KeyWord {

    public KeyWord() {
    }

    public static boolean isKeyWord(Segment seg) {
        boolean isKey = false;
        for (int i = 0; !isKey && i < KEYWORDS.length; i++) {
            if (seg.count == KEYWORDS[i].length()) {
                isKey = true;
                for (int j = 0; isKey && j < seg.count; j++) {
                    if (seg.array[seg.offset + j] != KEYWORDS[i].charAt(j)) {
                        isKey = false;
                    }
                }

            }
        }
        return isKey;
    }
    public static final String[] KEYWORDS = {
        "abstract",
        "boolean", "break", "byte",
        "case", "catch", "char", "class", "const", "continue",
        "default", "do", "double",
        "else", "extends",
        "final", "finally", "float", "for",
        "goto",
        "if", "implements", "import", "instanceof", "int", "interface",
        "long",
        "native", "new",
        "package",
        "private", "protected", "public",
        "return",
        "short", "static", "strictfp", "super", "switch", "synchronized",
        "this", "throw", "throws", "transient", "try",
        "void", "volatile",
        "while",
        "true", "false"
    };
}

public class Editor extends JFrame {

    Container c;
    JEditorPane editor = new JEditorPane();
    MyEditorKit kit = new MyEditorKit();

    public Editor() {
        super("Editor");
        initFrame();
        setSize(640, 480);
    }

    private void initFrame() {
        c = getContentPane();
        c.setLayout(new BorderLayout());
        editor.setFont(new Font("DialogInput", Font.PLAIN, 11));
        editor.setEditorKitForContentType("text/java", kit);
        editor.setContentType("text/java");

        editor.addCaretListener(new CaretListener() {

            public void caretUpdate(CaretEvent e) {
            }
        });
        c.add(new JScrollPane(editor), BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        Editor App = new Editor();
        App.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        App.show();
    }
}