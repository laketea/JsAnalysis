package jsanalysis.core;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import jsanalysis.common.Utils;
import org.mozilla.javascript.CompilerEnvirons;
import org.mozilla.javascript.Parser;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.ast.FunctionCall;
import org.mozilla.javascript.ast.FunctionNode;
import org.mozilla.javascript.ast.Name;
import org.mozilla.javascript.ast.NodeVisitor;

public class JsAnalysis {

    private String path;
    private String resultFile = "";
    private HashMap<String, String> funcNameFileMap;
    HashMap<String, JsFile> jsFileMap;
    HashMap<String, JsFunc> jsFuncMap;
    HashMap<String, String> jsFileFuncCallMap;

    public JsAnalysis(String path) {
        this.path = path;
    }

    // run everyone know
    public void run() {
        analyEveryFileFunction();
        analy();
    }

    // ��ӡ���
    private void printJsFile() {
        if (funcNameFileMap == null) {
            return;
        }
        String str = sameFuncNamePrint();
        str += "\n\n\n\n/****************JS�ļ�����****************/\n";
        for (JsFile file : jsFileMap.values()) {
//			file.print();
            str += file.getSource();
        }
        try {
            Utils.writeResultToFile(str, resultFile);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String sameFuncNamePrint() {
        String str = "/****************ͬ������������ʼ****************/\n";
        Set<String> set = funcNameFileMap.keySet();
        for (Iterator its = set.iterator(); its.hasNext();) {
            String key = (String) its.next();
            String value = (String) (funcNameFileMap.get(key));
            if (value.split(":").length > 1) {
                str += " �ظ�����:" + key + "()" + " =" + value + ";\n";
            }
        }
        str += "/****************ͬ��������������****************/\n";
        return str;
    }

    private void p(String s) {
        System.out.println(s);
    }

    // ���������������з��� �����÷���֮��Ĺ�ϵ
    private void analy() {

        if (jsFileMap == null || jsFileMap.isEmpty()) {
            return;
        }
        for (JsFile jsFile : jsFileMap.values()) {//�������е� jsFile
            if (jsFile.getFuncMap().isEmpty()) {
                continue;
            }
            for (JsFunc func : jsFile.getFuncMap().values()) {//������ǰjsFile�� ���з���
                String key = func.getName() + ":" + jsFile.getKeyName();
                if (jsFileFuncCallMap.containsKey(key)) {//���ݵ�ǰ����+fileKe,��ѯ�Ƿ�map�д��ڵ�ǰ�ļ����� �е��õķ���
                    String beCallFuncNames = jsFileFuncCallMap.get(key);
                    for (String beCallFuncName : beCallFuncNames.split(",")) {//������ǰ���� �����˵ķ�����split
                        if (funcNameFileMap.containsKey(beCallFuncName)) {
                            String files = funcNameFileMap.get(beCallFuncName);
                            for(String filePath:files.split(":")){//���ݱ����÷������������ܴ��ڵ�file
                                String beCallFuncKey =beCallFuncName+":"+filePath;
                                if(jsFuncMap.containsKey(beCallFuncKey)){//���ݱ����÷�����name �Լ� file  ȡ��jsFunc�������뵱ǰfunc�ĵ��÷�����
                                    func.addFuncCall(jsFuncMap.get(beCallFuncKey));
                                }
                            }
                        }
                    }
                }
            }
        }

//        Reader reader = null;
//        int i = 0;
//        for (File file : getAnalysisFileList()) {
//
//            try {
//                reader = new FileReader(file);
//                JsFile jsFile = (JsFile) (jsFileMap.get(file.getAbsolutePath()));
//                AstNode node = new Parser().parse(reader, file.getAbsolutePath(), 1);
//                node.visit(new Printer(jsFile, funcNameFileMap, jsFuncMap));
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                try {
//                    reader.close();
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//        }
    }

    // ���������ļ��ĵ�һ���ķ���
    private void analyEveryFileFunction() {

        Reader reader = null;
        jsFileMap = new HashMap<String, JsFile>();
        jsFuncMap = new HashMap<String, JsFunc>();
        funcNameFileMap = new HashMap();
        jsFileFuncCallMap = new HashMap<String, String>();
        int i = 0;
        for (File file : getAnalysisFileList()) {
            if (file.getName().indexOf("DS_Store") > 0) {
                continue;
            }
            try {
                reader = new FileReader(file);
                JsFile jsFile = new JsFile(file);
                jsFileMap.put(jsFile.getKeyName(), jsFile);
                CompilerEnvirons env = new CompilerEnvirons();
      env.setRecordingLocalJsDocComments(true);
      env.setAllowSharpComments(true);
      env.setRecordingComments(true);
                AstNode node = new Parser().parse(reader, file.getAbsolutePath(), 1);
                node.visit(new FunctionNodeVisitor(jsFile, funcNameFileMap, jsFuncMap, jsFileFuncCallMap));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    reader.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }

    // ��ȡ��Ҫ�����������ļ�
    public List<File> getAnalysisFileList() {
        File directory = new File(path);
        if (!directory.exists()) {
        }
        List lst = new ArrayList();
        visitFile(directory, lst);
        return lst;
    }

    public List visitFile(File file, List lst) {
        if (Utils.match(file.getName())) {
            return lst;
        }
        if (file.isDirectory()) {
            File[] fileArr = file.listFiles();
            for (File cfile : fileArr) {
                if (Utils.match(cfile.getName())) {
                    continue;
                }
                if (cfile.isDirectory()) {
                    visitFile(cfile, lst);
                } else {
                    if (cfile.getName().endsWith(".js")) {
                        lst.add(cfile);
                    }
                }
            }
        } else {
            lst.add(file);
        }
        return lst;

    }

    public HashMap<String, JsFile> getJsFileMap() {
        return jsFileMap;
    }

}
