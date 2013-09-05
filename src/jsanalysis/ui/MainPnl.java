/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MainPnl.java
 *
 * Created on 2013-3-12, 17:39:36
 */
package jsanalysis.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import jsanalysis.ui.ContentPnl;
import jsanalysis.MainFrame;
import jsanalysis.ui.TreePnl;
import jsanalysis.common.Utils;
import jsanalysis.core.Entity;
import jsanalysis.core.JsFile;
import jsanalysis.core.JsFunc;
import jsanalysis.test.ImageUtils;

/**
 *
 * @author zhangwei <laketea@163.com>
 */
public class MainPnl extends javax.swing.JPanel {

    private MainFrame frame;
    private TreePnl treePnl;

    /** Creates new form MainPnl */
    public MainPnl(MainFrame frame) {
        initComponents();
        this.frame = frame;
        treePnl = new TreePnl(this);
        this.jSplitPane1.setLeftComponent(treePnl);
//        jSplitPane1.setRightComponent(new JPanel());
        jSplitPane1.setDividerLocation(350);
    }

    public void setRightComponent(Entity entity) {
        this.jplContent.removeAll();
        this.jplContent.add(new ContentPnl(entity), BorderLayout.CENTER);
        jplContent.updateUI();
    }

    public void setRightComponent() {
//        this.jSplitPane1.setRightComponent(new JPanel());
        this.jplContent.removeAll();
    }

    public void setFileFilterDialog() {
        FileFilterPnl filterPnl = new FileFilterPnl();
        Frame frame = null;
        JDialog dlg = new JDialog(frame, "设置", true);
        dlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dlg.setLayout(new BorderLayout());
        dlg.add(filterPnl, BorderLayout.CENTER);
        dlg.setSize(477, 334);
        dlg.setLocationRelativeTo(null);
        dlg.setResizable(true);
        dlg.setVisible(true);
        if (filterPnl.isOk() && treePnl.isSelectPath()) {
            if (JOptionPane.showConfirmDialog(this, "已修改过滤规则，是否要重新分析数据？", "", JOptionPane.YES_NO_OPTION) == 0) {
                treePnl.flushTreeNode();
            }
        }
        return;
    }

    public void showFileMerDialog(DefaultMutableTreeNode node) {
        node = node == null ? treePnl.getSelectNode() : node;
        if (node == null) {
            return;
        }
        MergerJsFilePnl pnl = new MergerJsFilePnl(node);
        Frame frame = null;
        JDialog dlg = new JDialog(frame, "设置", true);
        dlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dlg.setLayout(new BorderLayout());
        dlg.add(pnl, BorderLayout.CENTER);
        dlg.setSize(423, 243);
        dlg.setLocationRelativeTo(null);
        dlg.setResizable(true);
        dlg.setVisible(true);
    }

    public void showResultTablePnl() {
        List lst = treePnl.getTableResult();
        String result = treePnl.getTxtResult();
        if (Utils.lstNull(lst)) {
            return;
        }
        ResultTabelPnl pnl = new ResultTabelPnl(lst, treePnl.getSelectPath(), result);
        Frame frame = null;
        JDialog dlg = new JDialog(frame, "分析结果" + "[" + treePnl.getSelectPath() + "]", true);
        dlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dlg.setLayout(new BorderLayout());
        dlg.add(pnl, BorderLayout.CENTER);
        dlg.setSize(780, 555);
        dlg.setLocationRelativeTo(null);
        dlg.setResizable(true);
        dlg.setVisible(true);
    }

    ;
    
    public void showTxtResultPnl() {
        String txt = treePnl.getTxtResult();
        ExportPnl pnl = new ExportPnl("txt", null, txt);
        Utils.showDialog(MainFrame.FRAME, 423, 243, "导出文本结果", pnl);
    }

    public void showImgResultPnl() {
        ResultTabelPnl rpnl = new ResultTabelPnl(treePnl.getTableResult(), treePnl.getSelectPath(), treePnl.getTxtResult());
        ExportPnl pnl = new ExportPnl("img", rpnl.getResultTable(), "");
        Utils.showDialog(MainFrame.FRAME, 423, 243, "导出图形结果", pnl);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jplContent = new javax.swing.JPanel();

        jplContent.setLayout(new java.awt.BorderLayout());
        jSplitPane1.setRightComponent(jplContent);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jSplitPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 799, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jSplitPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 564, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JPanel jplContent;
    // End of variables declaration//GEN-END:variables
}
