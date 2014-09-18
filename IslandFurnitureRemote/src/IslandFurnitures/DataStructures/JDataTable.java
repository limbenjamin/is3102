/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurnitures.DataStructures;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author James Imitates C#.NET datatable behaviour First in the universe. HEHE
 * !
 */
public class JDataTable<T> implements Serializable {

    public JDataTable.Columns<T> columns = new JDataTable.Columns<T>();
    public String Title = "TABLE";
    public ArrayList<Row<T>> Internalrows;

    public static class Columns<T> implements Serializable {

        public ArrayList<T> ColumnsHeader = new ArrayList<T>();

        public Columns() {

        }

        public int getColumnCount() {
            return (ColumnsHeader.size());
        }
    }

    public static class Row<T> implements Serializable {

        public ArrayList<T> rowdata = new ArrayList<T>();
        public boolean Editable=false;

        public Row(String dt) {
            this.displaytype = dt;
        }

        public Row() {

        }
        public String rowheader;
        public String rowgroup;

        public String displaytype = "string";

        public int getColumnCount() {
            return (rowdata.size());
        }

        public <Any> Any getValue(int i) {
            if (i == -2) {
                return (Any) rowgroup;
            }
            if (i == -1) {
                return (Any) rowheader;
            }
            return (Any) rowdata.get(i);
        }

        public String getStrValue(int i) {
            if (i == -2) {
                return (String) rowgroup;
            }
            if (i == -1) {
                return (String) rowheader;
            }
            return (String) transform(rowdata.get(i).toString());
        }

        public String transform(String s) {
            try {
                switch (displaytype) {
                    case "string":
                        return s;
                    case "percentage.2dp":
                        
                        return String.valueOf(Math.round(Double.valueOf(s) * 10000) / 100.00) + "%";

                }
            } catch (Exception ex) {

            }
            return s;
        }

    }

//Constructor
    public JDataTable() {
        this.Internalrows = new ArrayList<Row<T>>();
    }

    public JDataTable.Row NewRow() {
        Row<T> r = new Row<T>();
        Internalrows.add(r);
        return (r);

    }

    public JDataTable.Row NewRow(String dt) {
        Row<T> r = new Row<T>(dt);

        Internalrows.add(r);
        return (r);

    }
    
        public JDataTable.Row NewRowDefered() {
        Row<T> r = new Row<T>();
        return (r);

    }

    public JDataTable.Row NewRowDefered(String dt) {
        Row<T> r = new Row<T>(dt);

        return (r);

    }

    public int getRowCount() {
        return Internalrows.size();
    }

    public int getColumnCount() {
        return columns.getColumnCount();
    }

    public Row getRow(int i) {
        return Internalrows.get(i);
    }

    public T getCell(int i, int j) {
        return (T) ((Row) Internalrows.get(i)).rowdata.get(j);
    }

    public String getCellStr(int i, int j) {
        return ((Row) Internalrows.get(i)).rowdata.get(j).toString();
    }

    public void setCell(int i, int j, Object Value) {
        ((Row) Internalrows.get(i)).rowdata.set(j, Value);
    }

    public void setCell(Object Value) {
        ArrayList<T> cr = getRow(Internalrows.size() - 1).rowdata;
        cr.add((T) Value);
    }

}
