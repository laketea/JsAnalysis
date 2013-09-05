/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jsanalysis.ui;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;

import java.io.InputStream;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import jsanalysis.core.JsFile;
import jsanalysis.core.JsFunc;

/**
 *
 * @author yfcd
 */
public class TreeIconCellRenderer extends DefaultTreeCellRenderer {

    
    private final ImageIcon ROOT_IMG = new ImageIcon(getClass().getResource("/jsanalysis/resource/folder-3.png"));
    private final ImageIcon CATALOG_IMG = new ImageIcon(getClass().getResource("/jsanalysis/resource/folder-3.png"));
    private final ImageIcon CATALOG_OPEN_IMG = new ImageIcon(getClass().getResource("/jsanalysis/resource/folder-3.png"));
    private final ImageIcon FUNC_IMG = new ImageIcon(getClass().getResource("/jsanalysis/resource/func.png"));
    private final ImageIcon METHOD_IMG = new ImageIcon(getClass().getResource("/jsanalysis/resource/method.png"));
    private final ImageIcon CALL_METHOD_IMG = new ImageIcon(getClass().getResource("/jsanalysis/resource/becall.png"));
    private final ImageIcon JS_IMG = new ImageIcon(getClass().getResource("/jsanalysis/resource/jsfile-2.png"));

    public TreeIconCellRenderer() {
        super();
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value,
            boolean sel,
            boolean expanded,
            boolean leaf, int row,
            boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        Object obj = node.getUserObject();
        
        if(obj instanceof String ||obj instanceof JsFile ){
            if(expanded){
            setIcon(CATALOG_OPEN_IMG);
            }else{
               setIcon(CATALOG_IMG); 
            }
        
        }
        
        if(node.isRoot()){
             setIcon(ROOT_IMG);
        }
        
        if(obj instanceof JsFile){
               setIcon(JS_IMG); 
        
        }
        
        if(obj instanceof JsFunc){
                DefaultMutableTreeNode pnode = (DefaultMutableTreeNode)node.getParent();
               if(pnode.getUserObject() instanceof JsFile){
                   setIcon(METHOD_IMG); 
               }else{
                   setIcon(CALL_METHOD_IMG); 
               }
        
        }
      
        setText(node.toString());
        return this;
    }

    public ImageIcon createImage(Class cls, String name) {
        try {
            InputStream in = cls.getResourceAsStream(name);
            byte[] buffer = new byte[in.available()];
            in.read(buffer);
            return new ImageIcon(buffer);
        } catch (Exception e) {
            System.out.println(name);
            e.printStackTrace();
            return new ImageIcon();
        }
    }

    
}
