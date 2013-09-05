/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jsanalysis.common;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 *
 * @author zhangwei <laketea@163.com>
 */
public class FileWriteUtils {

    /**
     *1 按字节写入 FileOutputStream
     * 
     * @param count 写入循环次数
     * @param str 写入字符串
     */
    public void outputStreamTest(int count, String str) {
        File f = new File("f:test1.txt");
        OutputStream os = null;
        try {
            os = new FileOutputStream(f);
            for (int i = 0; i < count; i++) {
                os.write(str.getBytes());
            }
            os.flush();
            System.out.println("file's long:" + f.length());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *2 按字节缓冲写入 BufferedOutputStream
     * 
     * @param count 写入循环次数
     * @param str 写入字符串
     */
    public void bufferedOutputTest(int count, String str) {
        File f = new File("f:test2.txt");
        BufferedOutputStream bos = null;
        try {
            OutputStream os = new FileOutputStream(f);
            bos = new BufferedOutputStream(os);
            for (int i = 0; i < count; i++) {
                bos.write(str.getBytes());
            }
            bos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *3 按字符写入 FileWriter
     * 
     * @param count 写入循环次数
     * @param str 写入字符串
     */
    public void fileWriteTest(int count, String str) {
        File f = new File("f:test.txt");
        Writer writer = null;
        try {
            writer = new FileWriter(f);
            for (int i = 0; i < count; i++) {
                writer.write(str);
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *4 按字符缓冲写入 BufferedWriter
     * 
     * @param count 写入循环次数
     * @param str 写入字符串
     */
    public void bufferedWriteTest(int count, String str) {
        File f = new File("f:test3.txt");
        OutputStreamWriter writer = null;
        BufferedWriter bw = null;
        try {
            OutputStream os = new FileOutputStream(f);
            writer = new OutputStreamWriter(os);
            bw = new BufferedWriter(writer);
            for (int i = 0; i < count; i++) {
                bw.write(str);
            }
            bw.flush();
            if (f.exists()) {
                f.delete();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *5 按字符缓冲写入 BufferedWriter and BufferedOutputStream
     * 
     * @param count 写入循环次数
     * @param str 写入字符串
     */
    public void bufferedWriteAndBufferedOutputStreamTest(int count, String str) {
        File f = new File("f:test4.txt");
        BufferedOutputStream bos = null;
        OutputStreamWriter writer = null;
        BufferedWriter bw = null;
        try {
            OutputStream os = new FileOutputStream(f);
            bos = new BufferedOutputStream(os);
            writer = new OutputStreamWriter(bos);
            bw = new BufferedWriter(writer);
            for (int i = 0; i < count; i++) {
                bw.write(str);
            }
            bw.flush();
            if (f.exists()) {
                f.delete();
                System.out.println("delete---");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *6 按字符缓冲写入 BufferedWriter and FileWriter
     * 
     * @param count 写入循环次数
     * @param str 写入字符串
     */
    public static void bestWriteFile(String str, String fileName) throws IOException {

        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            File file = new File(fileName);
            if(!file.exists()){
                file.createNewFile();
                file = new File(fileName);
            }
            fw = new FileWriter(fileName);
            bw = new BufferedWriter(fw);
            bw.write(str);
            bw.flush();
        } finally {
            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
