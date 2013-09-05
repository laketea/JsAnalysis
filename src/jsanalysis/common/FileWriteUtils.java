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
     *1 ���ֽ�д�� FileOutputStream
     * 
     * @param count д��ѭ������
     * @param str д���ַ���
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
     *2 ���ֽڻ���д�� BufferedOutputStream
     * 
     * @param count д��ѭ������
     * @param str д���ַ���
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
     *3 ���ַ�д�� FileWriter
     * 
     * @param count д��ѭ������
     * @param str д���ַ���
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
     *4 ���ַ�����д�� BufferedWriter
     * 
     * @param count д��ѭ������
     * @param str д���ַ���
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
     *5 ���ַ�����д�� BufferedWriter and BufferedOutputStream
     * 
     * @param count д��ѭ������
     * @param str д���ַ���
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
     *6 ���ַ�����д�� BufferedWriter and FileWriter
     * 
     * @param count д��ѭ������
     * @param str д���ַ���
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
