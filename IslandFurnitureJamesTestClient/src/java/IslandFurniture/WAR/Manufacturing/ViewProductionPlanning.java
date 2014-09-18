/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.Manufacturing;

import IslandFurniture.EJB.Manufacturing.MaterialResourcePlanningView;
import IslandFurniture.EJB.Manufacturing.ManageProductionPlanningRemote;
import IslandFurniture.WAR.HELPER.helper;
import IslandFurnitures.DataStructures.JDataTable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

@SessionScoped
@ManagedBean(name = "DemandPlanning")
public class ViewProductionPlanning implements Serializable {

    @EJB
    private ManageProductionPlanningRemote mpp;

    @EJB
    MaterialResourcePlanningView dpv;

    private List<SelectItem> MF_LIST;
    private String MF;
    private List<ColumnModel> dates_columns = new ArrayList<ColumnModel>();

    private IslandFurnitures.DataStructures.JDataTable<String> dt;
    private ArrayList<JDataTable.Row<String>> dtRows = new ArrayList<JDataTable.Row<String>>();

    @PostConstruct
    public void init() {
        System.out.println("ViewProductionPlanning() Init!");
        MF_LIST = helper.convertHMToDropdown(dpv.getAuthorizedMF("AUTH_CHECK_TO_IMPLEMENT_SOON"));
    }

    public List<SelectItem> getMF_LIST() {
        return MF_LIST;
    }

    public String getMF() {
        return MF;
    }

    public void setMF(String MF) {

        this.MF = MF;

    }

    public void mfListerner() {

        if (this.MF == "") {
            return;
        }

        try {
            mpp.setMF(this.MF);
        } catch (Exception ex) {
        }

        dt = dpv.getDemandPlanningTable(MF);
        //Create the Datatable
        updateTable();

        System.out.println("ViewProductionPlanning() MF Changed to " + MF);

    }

    public void updateTable() {
        dates_columns.clear();
        dates_columns.add(new ColumnModel("Furniture", -2));
        dates_columns.add(new ColumnModel("Data Type", -1));
        Integer i = 0;
        for (String s : dt.columns.ColumnsHeader) {
            dates_columns.add(new ColumnModel(s, i));
            i++;
        }

        dtRows.clear();
        dtRows = dt.Internalrows;

        System.out.println("updateColumns() Table Column Updated ! " + MF);
    }

    public void automaticPlanning() {
        System.out.println("ViewProductionPlanning() User Triggered Production Planning Algorithm");
        try {
            mpp.CreateProductionPlanFromForecast();
        } catch (Exception ex) {
        }
    }

    public List<ColumnModel> getDates_columns() {
        return dates_columns;
    }

    public ArrayList<JDataTable.Row<String>> getDtRows() {
        return dtRows;
    }

    static public class ColumnModel implements Serializable {

        private String header;
        private int index;

        public ColumnModel(String header, int ind) {
            this.header = header;
            this.index = ind;
        }

        public String getHeader() {
            return header;
        }

        public int getIndex() {
            return index;
        }
    }

}
