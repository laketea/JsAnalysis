/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * TreePnl.java
 *
 * Created on 2013-3-12, 14:10:16
 */
package jsanalysis.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import jsanalysis.ui.MainPnl;
import jsanalysis.ui.TreeIconCellRenderer;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import jsanalysis.common.Utils;
import jsanalysis.core.Entity;
import jsanalysis.core.JsAnalysis;
import jsanalysis.core.JsFile;
import jsanalysis.core.JsFunc;

/**
 *
 * @author zhangwei <laketea@163.com>
 */
public class TreePnl extends javax.swing.JPanel implements ActionListener {

    private static String fColumn = "      ";//第一行宽度
    private static String sColumn = "                                           ";//第二行宽带
    private String path = "";
    private JTree tree;
    private DefaultMutableTreeNode root;
    private DefaultTreeModel model;
    private Integer i = 0;
    private JPopupMenu popup;
    HashMap<String, JsFile> map;
    MainPnl main;

    public TreePnl(MainPnl frame) {
        initComponents();
        main = frame;
        root = new DefaultMutableTreeNode("全部");
        model = new DefaultTreeModel(root);
        tree = new JTree(model);
        jScrollPane1.setViewportView(tree);
        createMenum();
        tree.expandRow(0);
        tree.setSelectionRow(0);
        tree.setRootVisible(false);
        tree.setShowsRootHandles(true);
        tree.setCellRenderer(new TreeIconCellRenderer());
        tree.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent event) {
                if (event.getButton() == 3 && (tree.getSelectionCount() > 0)) {
                    TreePath selPath = tree.getPathForLocation(event.getX(), event.getY());
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) selPath.getLastPathComponent();
                    if ((node.getUserObject() instanceof String)) {
                        popup.show(event.getComponent(), event.getX(), event.getY());
                    }
                }
            }
        });
        tree.addTreeSelectionListener(new TreeSelectionListener() {

            public void valueChanged(TreeSelectionEvent event) {
                TreePath path = tree.getSelectionPath();
                if (path != null) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) (path.getLastPathComponent());
                    if (node.getUserObject() instanceof Entity) {
                        main.setRightComponent((Entity) (node.getUserObject()));
                    } else {
                        main.setRightComponent();
                    }
                }
            }
        });
    }

    public boolean isSelectPath() {
        return Utils.notBank(path);
    }

    public String getSelectPath() {
        return path;
    }

    public HashMap<String, JsFile> getJsFileMap() {
        return map;
    }

    public DefaultMutableTreeNode getSelectNode() {
        DefaultMutableTreeNode node = null;
        TreePath path = tree.getSelectionPath();
        if(path==null){
            return node;
        }
        node = (DefaultMutableTreeNode) (path.getLastPathComponent());
        if (!(node.getUserObject() instanceof String)) {
            node = null;
        }
        return node;
    }

    public List<String[]> getTableResult() {

        List<String[]> lst = new ArrayList<String[]>();
        if (map == null || map.isEmpty()) {
            return lst;
        }
        int i = 0;
        for (JsFile jsFile : map.values()) {
            i++;
            lst.add(jsFile.getResult());
            if (!(jsFile.getFuncMap().isEmpty())) {
                for (JsFunc func : jsFile.getFuncMap().values()) {
                    lst.add(func.getResult(i));
                    i++;
                }
            }
        }
        return lst;
    }

    public String getTxtResult() {
        StringBuffer buffer = new StringBuffer();
        List<String[]> lst = getTableResult();
        if (Utils.lstNull(lst)) {
            return buffer.toString();
        }
        buffer.append("/**********************************************************************/\n");
        buffer.append("=======  " + "JS方法分析结果" + "  \n");
        buffer.append("=======  " + "根目录:" + path + "  \n");
        buffer.append("/**********************************************************************/\n\n");

        for (String[] arr : lst) {
            if (" ".equals(arr[0])) {
                buffer.append("\n");
            }
            
            buffer.append(parserColumn(arr[0], 0));
            buffer.append(" ".equals(arr[0])?arr[2].replace("path", "."):(parserColumn(arr[1], 1)+arr[2]));
//            buffer.append(arr[2].replace("path", "."));
            buffer.append("\n");
        }
        return buffer.toString();
    }

    public String parserColumn(String str, int column) {
        String curColumn = column == 0 ? fColumn : sColumn;
         str = str + curColumn.substring(str.length(), curColumn.length());
        return column==0?str:str.replace(" ", "-");
    }

    //选择目录
    private void selectJsFilePath() {
        path = selectDirectoryDialog();
        if (!Utils.notBank(path) || !(new File(path).exists())) {
            Utils.showMesaage(this, "无效的目录，请重新选择!");
            return;
        }
        jtfPath.setText(path);
        flushTreeNode();
    }

    //刷新tree的结点
    public void flushTreeNode() {
        if (!Utils.notBank(path)) {
            return;
        }
        JsAnalysis analysis = new JsAnalysis(path);
        analysis.run();
        map = analysis.getJsFileMap();
        root.removeAllChildren();
        File directory = new File(path);
        i = 0;
        visitFile(root, directory);
        tree.updateUI();
        tree.expandRow(0);
        tree.setSelectionRow(0);
    }

    //遍历目录
    public void visitFile(DefaultMutableTreeNode parent, File file) {
        if (Utils.match(file.getName())) {
            return;
        }
        if (file.isDirectory()) {//如果是目录
            DefaultMutableTreeNode node = addCatalogNode(parent, file);
            File[] fileArr = file.listFiles();
            for (File cfile : fileArr) {
                if (Utils.match(cfile.getName())) {
                    continue;
                }
                if (cfile.isDirectory()) {
                    visitFile(node, cfile);
                } else {
                    if (cfile.getName().endsWith(".js")) {
                        addJsFileNode(node, cfile);
                    }
                }
            }
        } else {
            addJsFileNode(parent, file);
        }
    }

    //添加目录结点
    private DefaultMutableTreeNode addCatalogNode(DefaultMutableTreeNode pnode, File file) {
        DefaultMutableTreeNode cnode = new DefaultMutableTreeNode(file.getName());
        pnode.add(cnode);
        return cnode;
    }

    //添加jsfile结点
    private DefaultMutableTreeNode addJsFileNode(DefaultMutableTreeNode pnode, File file) {
        JsFile jsFile = map.get(file.getAbsolutePath());
        if (jsFile == null) {
            return pnode;
        }
        DefaultMutableTreeNode cnode = new DefaultMutableTreeNode(jsFile);
        pnode.add(cnode);
        HashMap<String, JsFunc> map = jsFile.getFuncMap();
        if (!(map.isEmpty())) {
            for (JsFunc jsFunc : map.values()) {
                jsFunc.setLevelPath(jsFunc.getKey());
                addJsFuncNode(cnode, jsFunc);
            }
        }
        return cnode;
    }

    //添加func结点
    private void addJsFuncNode(DefaultMutableTreeNode pnode, JsFunc func) {
        DefaultMutableTreeNode funcnode = new DefaultMutableTreeNode(func);
        pnode.add(funcnode);
        List<JsFunc> funcLst = func.getFuncCallLst();
        if (!Utils.lstNull(funcLst)) {
            for (JsFunc childFunc : funcLst) {
                if (func.getLevelPath().indexOf(childFunc.getKey()) > -1) {
                    System.out.println("发现死循环!");
                } else {
                    childFunc.setLevelPath(func.getLevelPath() + "," + childFunc.getKey());
                    addJsFuncNode(funcnode, childFunc);
                }
            }
        }
    }

    //弹出文件目录选择框
    private String selectDirectoryDialog() {
        String path = "";
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        chooser.setDialogType(JFileChooser.OPEN_DIALOG);
        int retval = chooser.showOpenDialog(this);//显示“打开文件”对话框
        if (retval == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            String fileName = chooser.getName(file);
            path = file.getAbsolutePath();
        }
        return path;

    }

    private JPopupMenu createMenum(String[] names) {

        JPopupMenu tmp1 = new JPopupMenu();
        for (int i = 0; i < names.length; i++) {
            //新增下划线
            if ("-".equals(names[i])) {
                tmp1.addSeparator();
                continue;
            }
            JMenuItem tmp = new JMenuItem(names[i]);
            tmp.setActionCommand(names[i]);
            tmp.addActionListener(this);
            tmp1.add(tmp);
        }
        return tmp1;

    }

    public void actionPerformed(ActionEvent ae) {
        TreePath path = tree.getSelectionPath();
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) (path.getLastPathComponent());
        if ("合并JS文件".equals(ae.getActionCommand())) {
            main.showFileMerDialog(node);
        } else if ("查看分析结果".equals(ae.getActionCommand())) {
            main.showResultTablePnl();
        } else if ("导出图形结果".equals(ae.getActionCommand())) {
            main.showImgResultPnl();
        } else if ("导出文本结果".equals(ae.getActionCommand())) {
            main.showTxtResultPnl();
        }
    }

//    
//    /**
//     * 加上右键功能
//     */
//    private void setPopupMenu() {
//        popup = new JPopupMenu();
//        JMenuItem reitem = null;
//        popup.add(reitem = new JMenuItem("刷新表单树 ctrl+R"));
//        reitem.addActionListener(new ActionListener() {
//
//            public void actionPerformed(ActionEvent event) {
//                initTree(catalogid);
//            }
//        });
//        popup.setBorder(new BevelBorder(BevelBorder.RAISED));
//    }
    private void createMenum() {
        String[] names = {"合并JS文件", "-", "查看分析结果", "导出文本结果"};
        popup = createMenum(names);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jtfPath = new javax.swing.JTextField();
        jbtSelect = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();

        jtfPath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfPathActionPerformed(evt);
            }
        });

        jbtSelect.setText("选择");
        jbtSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtSelectActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(11, 11, 11)
                .add(jtfPath, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 243, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jbtSelect)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jtfPath, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jbtSelect))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jtfPathActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfPathActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfPathActionPerformed

    private void jbtSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtSelectActionPerformed
        // TODO add your handling code here:
        selectJsFilePath();
    }//GEN-LAST:event_jbtSelectActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbtSelect;
    private javax.swing.JTextField jtfPath;
    // End of variables declaration//GEN-END:variables
}
