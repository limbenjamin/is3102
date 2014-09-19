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

    public static class Cell implements Serializable {

        private String EditCmd;
        public String Value = null;
        private String displaytype="String";

        public Cell(String s) {
            this.Value = s;

        }

        public Cell(String s, String displaytype) {
            this.Value = s;
            this.displaytype = displaytype;

        }

        public Cell(String s, String displaytype, String EditCmd) {
            this.Value = s;
            this.EditCmd = EditCmd;
            this.displaytype = displaytype;

        }

        public String getEditCmd() {
            return EditCmd;
        }

        public void setEditCmd(String EditCmd) {
            this.EditCmd = EditCmd;
        }

        public String getValue() {

            return transform(Value);
        }

        public void setValue(String Value) {
        }

        private String transform(String sx) {
            if (displaytype.equals("String")) {

                return (sx);
            } else if (displaytype.equals("percentage.2dp")) {

                return String.valueOf((Math.round(Double.valueOf(sx.toString()) * 10000) / 100.00)) + "%";
            }
            return (sx);
        }
    }

    public static class Row<V> implements Serializable {

        public ArrayList<Cell> rowdata = new ArrayList<Cell>();
        public boolean Editable = false;

        public Row(String dt) {
            this.displaytype = dt;
        }

        public Row() {

        }

        public Cell newCell(String s) {
            Cell c = new Cell(s, this.displaytype);
            rowdata.add(c);
            return (c);
        }

        public Cell newCell(String s, String identify) {
            Cell c = new Cell(s, this.displaytype, identify);
            rowdata.add(c);
            return (c);
        }

        public String rowheader;
        public String rowgroup;
        private String ColorClass;

        public void setColorClass(String ColorClass) {
            this.ColorClass = ColorClass;
        }

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
            return (Any) rowdata.get(i).getValue();
        }

        public Cell getCell(int i) {
            if (i == -2) {
                Cell r_cell = new Cell(this.rowgroup);

                return r_cell;

            }
            if (i == -1) {
                Cell r_cell = new Cell(this.rowheader);

                return r_cell;

            }
            return rowdata.get(i);
        }

        public String getColorClass() {
            return ColorClass;
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

   

}
