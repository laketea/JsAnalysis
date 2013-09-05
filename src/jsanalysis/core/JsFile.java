package jsanalysis.core;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import jsanalysis.common.Utils;

public class JsFile implements Entity {

    private String name;
    private File file;
    private String keyName;
    private String source = "";
    
    private HashMap<String,JsFunc> funcMap = new HashMap<String,JsFunc>();

    public JsFile(String name) {
        this.name = name;
    }

    public JsFile(File file) {
        this.file = file;
        name = file.getName();
        keyName = name;
    }

    public void setKeyName(int i) {
        keyName = file.getAbsolutePath();
    }

    public String getKeyName() {
        return file.getAbsolutePath();
    }
    
    public void addFunction(String name,JsFunc func){
        this.funcMap.put(name, func);
    }
    
    public JsFunc getFunction(String name){
        return funcMap.get(name);
    }
    
    public HashMap<String,JsFunc> getFuncMap(){
        return funcMap;
    }
    
    public String toString(){
        return name;
    }
    
    public String[] getResult(){
        return new String[]{" "," "+file.getName()," ["+file.getAbsolutePath()+"]"};
    }
    
    
    
    
    
    

//    public void addFunction(String funcname) {
//        funcList.add(funcname);
////		funcCall.put(funcname, new ArrayList());
//    }
//
//    public void addFunctionCall(String funcName, String callName, String call) {
//        HashMap<String, String> tmLst = new HashMap<String, String>();
//        if (funcCall.containsKey(funcName)) {
//            tmLst = (HashMap<String, String>) (funcCall.get(funcName));
//        }
//        tmLst.put(callName, call);
//        funcCall.put(funcName, tmLst);
//    }
//
//    public HashMap<String, String> getCallLst(String funcName) {
//        return funcCall.get(funcName);
//    }
//
//    public List getFuncList() {
//        return this.funcList;
//    }

    public String getName() {
        return name;
    }
//
//    public void print() {
//        p("ÎÄ¼þÃû:" + file.getAbsolutePath() + "");
//        for (Iterator it = getFuncList().iterator(); it.hasNext();) {
//            String name = (String) it.next();
//            p("    +" + name + "()");
//            if (getCallLst(name) != null) {
//                Set<String> set = getCallLst(name).keySet();
//                for (Iterator its = set.iterator(); its.hasNext();) {
//                    String key = (String) its.next();
//                    p("            ---" + key + "()  //" + getCallLst(name).get(key));
//
//                }
//
//            }
//        }
//
//
//    }

    public String getSource() {
        return source;
    }

    private void p(String s) {
        System.out.println(s);
        setSource(getSource() + s + "\n");
    }

    public static void main(String[] args) {
        String s = "asdfsdf";
        String[] strings = s.split(",");
        System.out.println(strings[0]);
    }

    public void setSource(String source) {
        this.source = Utils.decodeUnicode(source);
    }

    public String getFileName() {
        return name;
    }
    
    public String getFuncName() {
        return null;
    }

    public String getPath() {
        return file.getAbsolutePath();
    }
    
}
