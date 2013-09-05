package jsanalysis.core;

import java.util.HashMap;
import java.util.List;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.ast.FunctionCall;
import org.mozilla.javascript.ast.FunctionNode;
import org.mozilla.javascript.ast.Name;
import org.mozilla.javascript.ast.NodeVisitor;

//遍历方法vvisitor
public class FunctionNodeVisitor implements NodeVisitor {

    private String fileKeyName;
    private JsFile jsFile;
    private HashMap map;
    private List lst;
    private HashMap funcMap;
    private HashMap jsFileFuncCallMap;
    private String curFuncName = "";

    public FunctionNodeVisitor(JsFile jsFile, HashMap map, HashMap funcMap, HashMap jsFileFuncCallMap) {
        this.fileKeyName = jsFile.getKeyName();
        this.jsFile = jsFile;
        this.map = map;
        this.funcMap = funcMap;
        this.jsFileFuncCallMap = jsFileFuncCallMap;
    }

    @Override
    public boolean visit(AstNode node) {
        String indent = "%1$Xs".replace("X", String.valueOf(node.depth() + 1));
        if (node instanceof AstRoot) {
            jsFile.setSource(node.toSource());
        }

        //其他结点则把当前方法名清空
        if (!(node instanceof FunctionNode) && node.getParent() instanceof AstRoot) {
            curFuncName = "";
        }



        //如果是一级方法
        if (node instanceof FunctionNode && node.getParent() instanceof AstRoot) {

            //当前方法名
            curFuncName = ((FunctionNode) node).getFunctionName().getIdentifier();

            String functionName = ((FunctionNode) node).getFunctionName().getIdentifier();

            if (map.containsKey(functionName)) {
                String string = (String) map.get(functionName);
                map.put(functionName, string + ":" + fileKeyName);
            } else {
                map.put(functionName, fileKeyName);
            }
            JsFunc func = new JsFunc(jsFile, functionName, node.toSource());
            jsFile.addFunction(functionName, func);
            funcMap.put(functionName + ":" + fileKeyName, func);
            System.out.println("发现方法：" + functionName + "-" + fileKeyName);
        }


        //如果是方法调用
        if (node instanceof FunctionCall) {
            AstNode fnode = ((FunctionCall) node).getTarget();
            if (!(fnode instanceof Name) || fnode == null) {
                return true;
            }
            String id = ((Name) fnode).getIdentifier();
            String key = curFuncName + ":" + jsFile.getKeyName();
            if (jsFileFuncCallMap.containsKey(key)) {//一个方法里面可能调用多个方法
                jsFileFuncCallMap.put(key, jsFileFuncCallMap.get(key) + ",id");
            } else {
                jsFileFuncCallMap.put(key, id);
            }
//            if (map.containsKey(id)) {//取消map中根据方法名 保存的 文件paht::
//                String files = (String)map.get(id);
//                for(String str:files.split(":")){
//                    JsFunc func = funcMap.get(id+":"+str);//组合方法名以及文件名， 根据: 组合 获取各个方法 并加入 当前方法的 调用列表
//                    if(func!=null&&file.getFunction(curFuncName)!=null){
//                        file.getFunction(curFuncName).addFuncCall(func);
//                    }
//                    
//                }
//            }
            System.out.println("find call curfuncname:" + curFuncName + " , callfuncname:" + id);
        }
        if (node instanceof org.mozilla.javascript.ast.FunctionNode && node.getParent() instanceof AstRoot) {
        }




        return true;
    }
}
