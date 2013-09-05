/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jsanalysis.ui;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import jsanalysis.common.Utils;


/**
 *
 * @author 
 * @version 
 */
public class SimpleTableModel extends AbstractTableModel {
    
    private String[] columns;//表头数组
    
    private List valueObjs = new ArrayList();//表格值列表,为Object[]对象
    
    private boolean cellEditabled = true;//列是否可以修改   
    
    private int editcol = 0;
    private boolean flag = false;
    
    
    /** Creates a new instance of SimpleTableModel */
    public SimpleTableModel(String[] columns,List valueObjs) {
        this.columns = columns;
        this.valueObjs = valueObjs;
    }
    
    /**
     * 获取总行数
     * @return 总行数
     */
    public int getRowCount() {
        return getValueObjs().size();
    }
    
    /**
     * 获取总列数
     * @return 总列数
     */
    public int getColumnCount() {
        return columns.length;
    }
    
    /**
     * 得到指定列的列名
     * @param column 指定列
     * @return 指定列的列名
     */
    public String getColumnName(int column) {
        return columns[column];
    }
    
    /**
     * 指定单元格是否可以编辑
     * @param row 行序号
     * @param col 列序号
     * @return true or false
     */
    public boolean isCellEditable(int r, int c) {
        if(flag && c==editcol){
            return true;
        }else{
            return cellEditabled;
        }
        
            
    }
    
    /**
     * 设置单元格是否可以编辑
     * @param b true or false
     */
    public void setCellEditable(boolean b, int editcol) {        
        cellEditabled = b;
        this.editcol = editcol;
        if(editcol>=0){
            this.flag = true;
        }
    }
    
    /**
     * 获得指定单元格的值
     * @param row 行序号
     * @param col 列序号
     * @return 指定单元格的值
     *
     */
    public Class getColumnClass(int c) {
        return String.class;
    }

    public Object getValueAt(int row, int col) {
        if(getValueObjs().size() <= row){
            return null;
        }
        return ((Object[])getValueObjs().get(row))[col];
    }
    
    /**
     * 设置指定单元格的值
     * @param row 行序号
     * @param col 列序号
     */
    public void setValueAt(Object value, int row, int col) {
        if(getValueObjs().size() <= row){
            return;
        }
        ((Object[])getValueObjs().get(row))[col] = value;
        fireTableCellUpdated(row,col);
    }
    
    /**
     * 获得指定值的行序号
     * @parma value 指定值
     * @return 行序号
     */
    public int findRow(Object value){
        for(int i = 0;i < valueObjs.size();i++){
            if(((Object[])valueObjs.get(i))[0].equals(((Object[])value)[0])){
                return i;
            }
        }
        return -1;
        //return getValueObjs().indexOf(value);
    }
    
    /**
     * 在最后增加一行
     * @parma value 指定值
     */
    public void appendRow(Object value){
        getValueObjs().add(value);
        fireTableDataChanged();
    }
    
    /**
     * 在指定行插入一下记录
     * @param row
     * @param value
     */
    public void appendRow(int row,Object value){
        getValueObjs().add(row,value);
        fireTableDataChanged();    	
    }
    
    /**
     * 删除指定行
     * @param row 行序号
     */
    public void removeRow(int row){
        getValueObjs().remove(row);
        fireTableDataChanged();
    }
    
    /**
     * 清空整个表格
     */
    public void clean(){
        valueObjs = new ArrayList();
        fireTableDataChanged();
    }
    
    /**
     * 取出所有数据
     */
    public List getValueObjs() {
        return valueObjs;
    }

    public void setCellEditabled(boolean cellEditabled) {
        this.cellEditabled = cellEditabled;
    }
    
    /**
     * 重新加载表格数据
     * @param values 新的数据
     */
    public void reloadTable(List values){
        valueObjs.removeAll(valueObjs);
        valueObjs.addAll(values);
        fireTableDataChanged();
    }
    
    /**
     * 指定列的值排序并重新加载数据
     */
    public void sortColumnData(final int col,final boolean isAscend){
        //排序
        Collections.sort(valueObjs, new Comparator() {
            public int compare(Object o1, Object o2) {
                if(o1 == null || o2== null )
                    return 0;
                
                Object[] obj1 = (Object[])o1;
                Object[] obj2 = (Object[])o2;
//    			if(StringUtils.nullOrBlank((String)obj1[col])||StringUtils.nullOrBlank((String)obj2[col]))
//    				return 0;
                String val1 = !Utils.notBank((String)obj1[col])? "" : (String)obj1[col];
                String val2 = !Utils.notBank((String)obj2[col])? "" : (String)obj2[col];
                int cc = ((String)obj1[col]).compareTo((String)obj2[col]);
                if(isAscend)//升序
                    return (cc < 0 ? -1 : cc > 0 ? 1 : 0);
                else//降序
                    return (cc < 0 ? 1 : cc > 0 ? -1 : 0);
            }
        });
        //重新加载数据
        fireTableDataChanged();
    }
    
}