/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jsanalysis.core;

import java.util.ArrayList;
import java.util.List;
import jsanalysis.common.Utils;

/**
 *
 * @author zhangwei <laketea@163.com>
 */
public class JsFunc implements Entity {
    
    private JsFile parent;
    
    private String name;
    
    private String source;
    
    private String key;
    
    private List<JsFunc> funcCallLst = new ArrayList<JsFunc>();
    
    private List<JsFunc> beCallFuncLst= new ArrayList<JsFunc>();//被其他方法调用的集合
    
    private String levelPath = "";
    
    
    public JsFunc(JsFile parent,String name,String source){
        this.parent  = parent;
        this.name = name;
        this.source = Utils.decodeUnicode(source);
    }
    
    
    public void addFuncCall(JsFunc func){
        this.funcCallLst.add(func);
        func.addBeCallFunc(this);
    }
    
    public List<JsFunc> getFuncCallLst(){
        return funcCallLst;
    }
    
    public void addBeCallFunc(JsFunc func){
        this.beCallFuncLst.add(func);
    }
    
    public List<JsFunc> getBeCallFuncLst(){
        return beCallFuncLst;
    }
    
    public String getKey(){
        return name+":"+parent.getKeyName();
    }
    
    public JsFile getParent() {
        return parent;
    }

    public void setParent(JsFile parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = Utils.decodeUnicode(source);
    }
    
    public String toString(){
        return name+" ["+parent.getName()+"]";
    }

    public String getLevelPath() {
        return levelPath;
    }

    public void setLevelPath(String levelPath) {
        this.levelPath = levelPath;
    }

    public String getFileName() {
       return parent.getName();
    }
    
    public String getFuncName() {
       return name;
    }

    public String getPath() {
        return parent.getPath();
    }
    
    public String[] getResult(int i){
        return new String[]{" "+i," "+name+"()"," "+beCallFuncLst.size()+""};
    }
    
    
}
