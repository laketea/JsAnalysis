/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ResultTabelPnl.java
 *
 * Created on 2013-3-14, 16:03:32
 */
package jsanalysis.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import jsanalysis.MainFrame;
import jsanalysis.common.Utils;
import jsanalysis.test.ImageUtils;

/**
 *
 * @author zhangwei <laketea@163.com>
 */
public class ResultTabelPnl extends javax.swing.JPanel {

    private String[] TITLES = new String[]{"序号", "方法名", "调用次数"};
    private SimpleTableModel model;
    private JTable table;
    private String result ="";

    /** Creates new form ResultTabelPnl */
    public ResultTabelPnl(List<String[]> lst, String path,String result) {
        initComponents();
        initTable(lst);
        this.result = result;
//        this.jblRootPath.setText(path);
//        this.jScrollPane1.setAutoscrolls();
    }
    
    public JTable getResultTable(){
        return table;
    }

    private void initTable(List<String[]> lst) {
        model = new SimpleTableModel(TITLES, lst);
        model.setCellEditabled(true);
        table = new JTable(model);
        table.setShowGrid(true);
//        table.s
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(false);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(0).setCellRenderer(new NormalRenderer(0));
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(1).setCellRenderer(new NormalRenderer(1));
        table.getColumnModel().getColumn(2).setPreferredWidth(500);
        table.getColumnModel().getColumn(2).setCellRenderer(new NormalRenderer(2));
        table.setRowHeight(22);
//         table.setRowMargin(20);
        table.setRowSelectionAllowed(true);
//        tabPnl.add(table,BorderLayout.CENTER);
        this.jScrollPane1.setViewportView(table);
    }

    class NormalRenderer extends DefaultTableCellRenderer {

        JPanel jp = null;
//        JLabel jlb = null;
        int column = 0;

        public NormalRenderer(int column) {
            this.jp = new JPanel();
//            this.jlb = new JLabel();
            this.column = column;
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

            JPanel jp = new JPanel();
            jp.setLayout(new BorderLayout());
            JLabel jlb = new JLabel();
            jp.add(jlb, "Center");
            jp.setBackground(UIManager.getColor("Table.focusCellBackground"));
            jlb.setText((String) value);
            jlb.setFont(null);
//            if(!((String)value).endsWith("()")){
            String str = (String) value;
            SimpleTableModel model = (SimpleTableModel) table.getModel();
            if (" ".equals((String) (model.getValueAt(row, 0)))) {
                jp.setBackground(new Color(206, 231, 255));
                jp.setBackground(new Color(206, 231, 255));
                jlb.setFont(new java.awt.Font("Lucida Grande", 1, 13));

            }
            return jp;
        }
    }

    private void showExportDialog(String type) {
        ExportPnl pnl = new ExportPnl(type, table,result);
        String title = "导出" + ("img".equals(type) ? "结果图" : "文本");
        Utils.showDialog(MainFrame.FRAME, 423, 243, title, pnl);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jbtImport = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane1.setAutoscrolls(true);

        jbtImport.setText("导出结果图");
        jbtImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtImportActionPerformed(evt);
            }
        });

        jButton1.setText("导出文本文件");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("关  闭");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jbtImport)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButton1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 451, Short.MAX_VALUE)
                .add(jButton2)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(8, 8, 8)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jbtImport)
                    .add(jButton1)
                    .add(jButton2))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 478, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jbtImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtImportActionPerformed
        // TODO add your handling code here:
        showExportDialog("img");
//        MainFrame.FRAME.showImgResultPnl();
    }//GEN-LAST:event_jbtImportActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        showExportDialog("txt");
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        Utils.exit(this);
    }//GEN-LAST:event_jButton2ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbtImport;
    // End of variables declaration//GEN-END:variables
}
