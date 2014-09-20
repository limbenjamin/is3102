/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurnitures.DataStructures;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author James super data structure !
 */
@SuppressWarnings("unchecked")
public class JDataTable<T> implements Serializable {

    public ArrayList<String> columns = new ArrayList<String>();
    public String Title = "TABLE";
    public List<Row> Internalrows;

    public List<Row> getInternalrows() {
        return Internalrows;
    }

    public void setInternalrows(ArrayList<Row> Internalrows) {
        this.Internalrows = Internalrows;
    }




    public static class Cell implements Serializable {

        public String Value = null;
        private String displaytype = "String";
        private Serializable binded_entity = null;
        private String propertyname;
        private Boolean isEditable = false;
        private Boolean stateChanged = false;
        private int Index = 0;

        @Override
        public boolean equals(Object object) {

            if (!(object instanceof Cell)) {

                return object.equals(this.Value);

            }

            return (this.Value.equals(((Cell) object).getValue()));

        }

        public Object getBinded_entity() {
            return binded_entity;
        }

        public int getIndex() {
            return Index;
        }

        public void setIndex(int Index) {
            this.Index = Index;
        }

        public Boolean isBinded() {
            return (binded_entity != null);
        }

        public String getPropertyname() {
            return propertyname;
        }

        public void setPropertyname(String propertyname) {
            this.propertyname = propertyname;
        }

        public Cell(String s) {
            this.Value = s;

        }

        public Cell(String s, String displaytype) {
            this.Value = s;
            this.displaytype = displaytype;

        }

        public Cell setBinded_entity(Serializable binded_entity) {
            this.binded_entity = binded_entity;
            return (this);
        }

        public Object getValue() {
            if (!this.isBinded()) {
                return transform(Value.toString());
            }
            try {
                Method m = binded_entity.getClass().getDeclaredMethod("get" + this.propertyname);
                return m.invoke(binded_entity).toString();
            } catch (NoSuchMethodException ex) {

            } catch (SecurityException ex) {
                System.err.println(ex.getMessage());
            } catch (IllegalAccessException ex) {
                System.err.println(ex.getMessage());
            } catch (IllegalArgumentException ex) {
                System.err.println(ex.getMessage());
            } catch (InvocationTargetException ex) {
                System.err.println(ex.getMessage());
            }
            System.out.println("JDataTable() Binding Failed to get property[" + propertyname + "] !");
            return "[ERROR]";
        }

        public void setValue(Object Value) {
            if (!isEditable) {
                return;
            }
            if (!this.isBinded()) {
                return;
            }

            if (Value.equals(this.Value)) {
                return;
            }

            Method[] methods = binded_entity.getClass().getDeclaredMethods();
            for (Method m : methods) {
                try {
                    if (m.getName().equals("set" + this.propertyname)) {
                        //Conversion
                        //if (m.getParameterTypes()[1].getComponentType().getClass().is)

                        m.invoke(binded_entity, ObjectConverter.convert(Value, m.getParameterTypes()[0]));
                        stateChanged = true;
                        System.out.println("JDataTable" + binded_entity.getClass().toString() + "[" + propertyname + "] updated to " + Value);
                        return;
                    }
                    continue;

                } catch (IllegalAccessException ex) {
                    System.err.println(ex.getMessage());
                } catch (IllegalArgumentException ex) {
                    System.err.println(ex.getMessage());
                } catch (InvocationTargetException ex) {
                    System.err.println(ex.getMessage());
                }
            }

            System.out.println("JDataTable() Binding Failed to set property[" + propertyname + "] !");
        }

        private String transform(String sx) {
            try {
                if (displaytype.equals("String")) {

                    return (sx);
                } else if (displaytype.equals("percentage.2dp")) {

                    return String.valueOf((Math.round(Double.valueOf(sx.toString()) * 10000) / 100.00)) + "%";
                }
            } catch (Exception ex) {

            }
            return (sx);
        }

        public Boolean getIsEditable() {
            return isEditable;
        }

        public void setIsEditable(Boolean isEditable) {
            this.isEditable = isEditable;
        }
    }

    public static class Row implements Serializable {

        public ArrayList<Cell> rowdata = new ArrayList<Cell>();
        public boolean Editable = false;
        private Serializable binded_entity;

        public Row(String dt) {
            this.displaytype = dt;
        }

        public Row() {

        }

        public Row(Serializable binded) {
            binded_entity = binded;
        }

        public Cell newCell(String s) {
            Cell c = new Cell(s, this.displaytype);
            rowdata.add(c);
            return (c);
        }

        public Serializable getBinded_entity() {
            return binded_entity;
        }

        public Cell newBindedCell(String s, String propertyName) {
            Cell c = new Cell(s, this.displaytype);
            c.setBinded_entity(binded_entity);
            c.setPropertyname(propertyName);
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
        this.Internalrows = new ArrayList<Row>();
    }

    public JDataTable.Row newRow() {
        Row r = new Row();
        Internalrows.add(r);
        return (r);

    }

    public ArrayList<String> getColumns() {
        return columns;
    }

    public void setColumns(ArrayList<String> columns) {
        this.columns = columns;
    }
    
    

    public JDataTable.Row newBindedRow(Serializable entity) {
        Row r = new Row(entity);

        Internalrows.add(r);
        return (r);

    }

    public JDataTable.Row newRow(String dt) {
        Row r = new Row(dt);

        Internalrows.add(r);
        return (r);

    }

    public JDataTable.Row NewRowDefered() {
        Row r = new Row();
        return (r);

    }

    public JDataTable.Row NewRowDefered(String dt) {
        Row r = new Row(dt);

        return (r);

    }

    public int getRowCount() {
        return Internalrows.size();
    }

    public Row getRow(int i) {
        return Internalrows.get(i);
    }

    public ArrayList<Object> getStateChangedEntities() {
        ArrayList<Object> ro = new ArrayList<Object>();
        for (Row r : this.Internalrows) {
            for (Cell c : r.rowdata) {
                if (c.stateChanged) {
                    ro.add(c.binded_entity);
                }
            }
        }
        return (ro);
    }

}
