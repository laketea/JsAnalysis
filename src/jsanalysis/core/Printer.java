package jsanalysis.core;

import java.util.HashMap;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.ast.FunctionCall;
import org.mozilla.javascript.ast.FunctionNode;
import org.mozilla.javascript.ast.Name;
import org.mozilla.javascript.ast.NodeVisitor;

//遍历以及发现function call的方法
public class Printer implements NodeVisitor {

    public HashMap map;
    public String curFuncName = "";
    public JsFile file;
    public HashMap<String,JsFunc> funcMap;

    public Printer(JsFile file, HashMap map,HashMap<String,JsFunc> funcmap) {
        this.map = map;
        this.file = file;
        this.funcMap = funcmap;
    }

    @Override
    public boolean visit(AstNode node) {
        String indent = "%1$Xs".replace("X", String.valueOf(node.depth() + 1));
        if (node instanceof FunctionNode && node.getParent() instanceof AstRoot) {
            curFuncName = ((FunctionNode) node).getFunctionName().getIdentifier();
        }

        if (!(node instanceof FunctionNode) && node.getParent() instanceof AstRoot) {
            curFuncName = "";
        }
        if (node instanceof FunctionCall) {
            AstNode fnode = ((FunctionCall) node).getTarget();
            if (!(fnode instanceof Name) || fnode == null) {
                return true;
            }
            String id = ((Name) fnode).getIdentifier();
            if (map.containsKey(id)) {//取消map中根据方法名 保存的 文件paht::
                String files = (String)map.get(id);
                for(String str:files.split(":")){
                    JsFunc func = funcMap.get(id+":"+str);//组合方法名以及文件名， 根据: 组合 获取各个方法 并加入 当前方法的 调用列表
                    if(func!=null&&file.getFunction(curFuncName)!=null){
                        file.getFunction(curFuncName).addFuncCall(func);
                    }
                    
                }
            }
            System.out.println("find call curfuncname:" + curFuncName + " , callfuncname:" + id);
        }
        return true;
    }
}
