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

    // 打印输出
    private void printJsFile() {
        if (funcNameFileMap == null) {
            return;
        }
        String str = sameFuncNamePrint();
        str += "\n\n\n\n/****************JS文件分析****************/\n";
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
        String str = "/****************同名方法分析开始****************/\n";
        Set<String> set = funcNameFileMap.keySet();
        for (Iterator its = set.iterator(); its.hasNext();) {
            String key = (String) its.next();
            String value = (String) (funcNameFileMap.get(key));
            if (value.split(":").length > 1) {
                str += " 重复方法:" + key + "()" + " =" + value + ";\n";
            }
        }
        str += "/****************同名方法分析结束****************/\n";
        return str;
    }

    private void p(String s) {
        System.out.println(s);
    }

    // 分析遍历重组所有方法 跟调用方法之间的关系
    private void analy() {

        if (jsFileMap == null || jsFileMap.isEmpty()) {
            return;
        }
        for (JsFile jsFile : jsFileMap.values()) {//遍历所有的 jsFile
            if (jsFile.getFuncMap().isEmpty()) {
                continue;
            }
            for (JsFunc func : jsFile.getFuncMap().values()) {//遍历当前jsFile的 所有方法
                String key = func.getName() + ":" + jsFile.getKeyName();
                if (jsFileFuncCallMap.containsKey(key)) {//根据当前方法+fileKe,查询是否map中存在当前文件方法 有调用的方法
                    String beCallFuncNames = jsFileFuncCallMap.get(key);
                    for (String beCallFuncName : beCallFuncNames.split(",")) {//遍历当前方法 调用了的方法，split
                        if (funcNameFileMap.containsKey(beCallFuncName)) {
                            String files = funcNameFileMap.get(beCallFuncName);
                            for(String filePath:files.split(":")){//根据被调用方法，分析可能存在的file
                                String beCallFuncKey =beCallFuncName+":"+filePath;
                                if(jsFuncMap.containsKey(beCallFuncKey)){//根据被调用方法的name 以及 file  取出jsFunc，并加入当前func的调用方法中
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

    // 遍历所有文件的第一级的方法
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

    // 获取需要遍历的所有文件
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
