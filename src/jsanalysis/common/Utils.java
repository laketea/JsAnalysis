/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jsanalysis.common;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 *
 * @author zhangwei <laketea@163.com>
 */
public class Utils {

    private static final String CONFIGNAME = "/config.properties";
    public static String FILETERSTRING = "";
    private static final String PATTERN_LINE_START = "^";
    private static final String PATTERN_LINE_END = "$";
    private static final char[] META_CHARACTERS = {'$', '^', '[', ']', '(', ')',
        '{', '}', '|', '+', '.', '\\'};
    public static final String FILEFILETERNAME = "fileFilter";

    static {
        FILETERSTRING = getParam(FILEFILETERNAME);
    }

    public static boolean notBank(String str) {
        return str == null || "".equals(str) ? false : true;
    }

    public static void showMesaage(JComponent jcp, String str) {
        JOptionPane.showMessageDialog(jcp, str);
    }

    // 复制文件
    public static void writeResultToFile(String str, String file) throws Exception {
        FileWriter fw = null;
        try {
            fw = new FileWriter(file);
            fw.write(str);// 无法实现追加可写，存在覆盖现象
            fw.close();
        } catch (IOException e1) {
            e1.printStackTrace();
            System.out.println("文件写入错误");
        }
    }

    public static boolean lstNull(List lst) {
        return lst == null || lst.isEmpty() ? true : false;
    }

    public static void setDefaultUIFont() {
        Font myFont = new Font("宋体", Font.PLAIN, 12);
        javax.swing.plaf.FontUIResource fontRes = new javax.swing.plaf.FontUIResource(
                myFont);
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, fontRes);
            }
        }
        if (System.getProperty("os.name").toLowerCase().startsWith("mac")) {//苹果系统换皮肤

            try {
                UIManager.setLookAndFeel("com.jtattoo.plaf.mcwin.McWinLookAndFeel");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    // 复制文件
    public void copyFile(File file, File destFile) throws Exception {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new BufferedInputStream(new FileInputStream(file), 16384);
            out = new BufferedOutputStream(new FileOutputStream(destFile), 16384);
            byte buffer[] = new byte[16384];
            while (in.read(buffer) > 0) {
                out.write(buffer);
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }

    }

    public static void writeToFile(String fileName, String fileContent) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
            out.write(fileContent);
            out.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block    
            e.printStackTrace();
        }
    }

    public void createNewFile(String fileName, String fileContent) {
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
                file = new File(fileName);//创建
            }
            FileWriter filewriter = new FileWriter(file);   //用来写入字符文件的便捷类。
            PrintWriter f = new PrintWriter(filewriter);    //向文本输出流打印对象的格式化表示形式。
            f.println(fileContent);
            filewriter.close();
        } catch (Exception ex) {
            System.out.println("出错了!");
        }
    }

    public static void saveStringToFile(String fileName, String towrite) {
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
                file = new File(fileName);
            }
            fos = new FileOutputStream(file);
            osw = new OutputStreamWriter(fos);
            osw.write(towrite);
            osw.flush();
        } catch (IOException iex) {
            iex.printStackTrace();
        } finally {
            try {
                if (null != osw) {
                    osw.close();
                }
                if (null != fos) {
                    fos.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static boolean match(String str) {
        boolean isMatch = false;
        String[] pt = FILETERSTRING.split(",");
        if (pt.length == 0 || !notBank(str)) {
            return isMatch;
        }
        for (String val : pt) {
            if (fileMatch(val, str)) {
                isMatch = true;
                break;
            }
        }
        return isMatch;
    }

    public static boolean fileMatch(String pattern, String str) {
        pattern = convertToRegexPattern(pattern);
        return Pattern.matches(pattern, str);
    }

    private static String convertToRegexPattern(String wildcardString) {
        String result = PATTERN_LINE_START;
        char[] chars = wildcardString.toCharArray();
        for (char ch : chars) {
            if (Arrays.binarySearch(META_CHARACTERS, ch) >= 0) {
                result += "\\" + ch;
                continue;
            }
            switch (ch) {
                case '*':
                    result += ".*";
                    break;
                case '?':
                    result += ".{0,1}";
                    break;
                default:
                    result += ch;
            }
        }
        result += PATTERN_LINE_END;
        return result;
    }

    private static Properties loadp() {
        Properties pro = new Properties();
        File file = new File(System.getProperty("user.dir") + CONFIGNAME);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileInputStream is = new FileInputStream(file);
            pro.load(is);
            is.close();
            is = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pro;
    }

    public static String getParam(String name) {
        Properties pro = loadp();
        String value = pro.getProperty(name);
        return value == null ? "" : value;
    }

    public static void saveParam(String name, String value) {
        FileOutputStream os = null;
        try {
            Properties prop = loadp();
            prop.setProperty(name, value);
            os = new FileOutputStream(new File(System.getProperty("user.dir") + CONFIGNAME));
            prop.store(os, "");
        } catch (Exception e) {
        } finally {
            if (os != null) {
                try {
                    os.close();
                    os = null;
                } catch (Exception e) {
                }
            }
        }
    }

    public static void p(String str) {
        System.out.println(str);
    }

    public static String decodeUnicode(String dataStr) {
        final StringBuffer buffer = new StringBuffer();
        String tempStr = "";
        String operStr = dataStr;

        if (operStr != null && operStr.indexOf("\\u") == -1) {
            return buffer.append(operStr).toString(); // 
        }
        if (operStr != null && !operStr.equals("") && !operStr.startsWith("\\u")) { // 
            tempStr = operStr.substring(0, operStr.indexOf("\\u")); //  
            operStr = operStr.substring(operStr.indexOf("\\u"), operStr.length());// operStr字符一定是以unicode编码字符打头的字符串
        }
        buffer.append(tempStr);
        while (operStr != null && !operStr.equals("") && operStr.startsWith("\\u")) { // 循环处理,处理对象一定是以unicode编码字符打头的字符串
            tempStr = operStr.substring(0, 6);
            operStr = operStr.substring(6, operStr.length());
            String charStr = "";
            charStr = tempStr.substring(2, tempStr.length());
            char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。
            buffer.append(new Character(letter).toString());
            if (operStr.indexOf("\\u") == -1) { //  
                buffer.append(operStr);
            } else { // 处理operStr使其打头字符为unicode字符
                tempStr = operStr.substring(0, operStr.indexOf("\\u"));
                operStr = operStr.substring(operStr.indexOf("\\u"), operStr.length());
                buffer.append(tempStr);
            }
        }
        return buffer.toString();
    }

    public static void showDialog(JFrame frame, int width, int height, String title, JComponent jcp) {
        JDialog dlg = new JDialog(new JFrame(), title, true);
        dlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dlg.setLayout(new BorderLayout());
        dlg.add(jcp, BorderLayout.CENTER);
        dlg.setSize(width, height);
        dlg.setLocationRelativeTo(jcp);
        dlg.setResizable(true);
        dlg.setVisible(true);
    }

  

    public static void exit(JComponent jcp) {
        SwingUtilities.getWindowAncestor(jcp).dispose();
    }

    public static void main(String[] args) {
//        Utils.p("\u83b7\u53d6_module\u5bf9\u8c61\u5931\u8d25");
        String s = "123456789";

        String t = s.substring(5, s.length());
        System.out.println(t);
    }
}
