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
    
    private String[] columns;//��ͷ����
    
    private List valueObjs = new ArrayList();//���ֵ�б�,ΪObject[]����
    
    private boolean cellEditabled = true;//���Ƿ�����޸�   
    
    private int editcol = 0;
    private boolean flag = false;
    
    
    /** Creates a new instance of SimpleTableModel */
    public SimpleTableModel(String[] columns,List valueObjs) {
        this.columns = columns;
        this.valueObjs = valueObjs;
    }
    
    /**
     * ��ȡ������
     * @return ������
     */
    public int getRowCount() {
        return getValueObjs().size();
    }
    
    /**
     * ��ȡ������
     * @return ������
     */
    public int getColumnCount() {
        return columns.length;
    }
    
    /**
     * �õ�ָ���е�����
     * @param column ָ����
     * @return ָ���е�����
     */
    public String getColumnName(int column) {
        return columns[column];
    }
    
    /**
     * ָ����Ԫ���Ƿ���Ա༭
     * @param row �����
     * @param col �����
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
     * ���õ�Ԫ���Ƿ���Ա༭
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
     * ���ָ����Ԫ���ֵ
     * @param row �����
     * @param col �����
     * @return ָ����Ԫ���ֵ
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
     * ����ָ����Ԫ���ֵ
     * @param row �����
     * @param col �����
     */
    public void setValueAt(Object value, int row, int col) {
        if(getValueObjs().size() <= row){
            return;
        }
        ((Object[])getValueObjs().get(row))[col] = value;
        fireTableCellUpdated(row,col);
    }
    
    /**
     * ���ָ��ֵ�������
     * @parma value ָ��ֵ
     * @return �����
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
     * ���������һ��
     * @parma value ָ��ֵ
     */
    public void appendRow(Object value){
        getValueObjs().add(value);
        fireTableDataChanged();
    }
    
    /**
     * ��ָ���в���һ�¼�¼
     * @param row
     * @param value
     */
    public void appendRow(int row,Object value){
        getValueObjs().add(row,value);
        fireTableDataChanged();    	
    }
    
    /**
     * ɾ��ָ����
     * @param row �����
     */
    public void removeRow(int row){
        getValueObjs().remove(row);
        fireTableDataChanged();
    }
    
    /**
     * ����������
     */
    public void clean(){
        valueObjs = new ArrayList();
        fireTableDataChanged();
    }
    
    /**
     * ȡ����������
     */
    public List getValueObjs() {
        return valueObjs;
    }

    public void setCellEditabled(boolean cellEditabled) {
        this.cellEditabled = cellEditabled;
    }
    
    /**
     * ���¼��ر������
     * @param values �µ�����
     */
    public void reloadTable(List values){
        valueObjs.removeAll(valueObjs);
        valueObjs.addAll(values);
        fireTableDataChanged();
    }
    
    /**
     * ָ���е�ֵ�������¼�������
     */
    public void sortColumnData(final int col,final boolean isAscend){
        //����
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
                if(isAscend)//����
                    return (cc < 0 ? -1 : cc > 0 ? 1 : 0);
                else//����
                    return (cc < 0 ? 1 : cc > 0 ? -1 : 0);
            }
        });
        //���¼�������
        fireTableDataChanged();
    }
    
}