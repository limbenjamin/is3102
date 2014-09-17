/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.StaticClasses.Helper;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author James Imitates C#.NET datatable behaviour First in the universe. HEHE
 * !
 */
public class DataTable<T> {

    public DataTable.Columns <T> columns;
    public String Title;
    LinkedList<Row> rows;

    public static class Columns<T> {

        public ArrayList<T> ColumnsHeader = new ArrayList<T>();

        public Columns() {

        }

        public int getColumnCount() {
            return (ColumnsHeader.size());
        }
    }

    public static class Row<T>{

        public ArrayList<T> rowdata = new ArrayList<T>();

        public Row() {

        }
        public Object rowheader;

        public int getColumnCount() {
            return (rowdata.size());
        }
    }

//Constructor
    public DataTable() {
        columns = new DataTable.Columns<T>();
        ArrayList<Row> rows = new ArrayList<Row>();
    }

    public DataTable.Row NewRow() {
        Row r = new Row();
        rows.add(r);
        return (r);

    }

    public int getRowCount() {
        return rows.size();
    }

    public int getColumnCount() {
        return columns.getColumnCount();
    }

    public Row getRow(int i) {
        return rows.get(i);
    }

    public T getCell(int i, int j) {
        return (T)((Row) rows.get(i)).rowdata.get(j);
    }

    public String getCellStr(int i, int j) {
        return ((Row) rows.get(i)).rowdata.get(j).toString();
    }

    public Object setCell(int i, int j, Object Value) {
        return ((Row) rows.get(i)).rowdata.set(j, Value);
    }

}
