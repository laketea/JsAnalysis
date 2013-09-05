/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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

import java.util.SortedSet;
import jsanalysis.common.Utils;
import jsanalysis.core.FunctionNodeVisitor;
import jsanalysis.core.JsAnalysis;
import jsanalysis.core.JsFile;
import jsanalysis.core.JsFunc;
import org.mozilla.javascript.CompilerEnvirons;
import org.mozilla.javascript.Parser;
import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.ast.Comment;
import org.mozilla.javascript.ast.FunctionCall;
import org.mozilla.javascript.ast.FunctionNode;
import org.mozilla.javascript.ast.Name;
import org.mozilla.javascript.ast.NodeVisitor;

public class AnyComponent {

    private String path;
    private String resultFile = "";
    private HashMap<String, String> funcNameFileMap;
    HashMap<String, JsFile> jsFileMap;
    HashMap<String, JsFunc> jsFuncMap;
    HashMap<String, String> jsFileFuncCallMap;

    public AnyComponent(String path) {
        this.path = path;
    }

    // run everyone know
    public void run() {
        analyEveryFileFunction();
    }

    private void p(String s) {
        System.out.println(s);
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
                AstRoot astRoot = new Parser(env).parse(reader, file.getAbsolutePath(), 1);
                
                SortedSet<Comment> comment = astRoot.getComments();
                if (comment == null) {
                    for(Iterator<Comment> iter = comment.iterator(); iter.hasNext();) {
                        Comment cmt = (Comment)iter.next();
                        System.out.println("*******************");
                        System.out.println(cmt.toSource());
                        if(cmt.getCommentType()==Token.CommentType.JSDOC){
                            cmt.visit(new FunctionNodeVisitor(jsFile, funcNameFileMap, jsFuncMap, jsFileFuncCallMap));
                        
                        }
                    }
                }

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
    
    public void analyCptInfo(){
        
    
    
    
    
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

    public static void main(String[] args) {

    }
}