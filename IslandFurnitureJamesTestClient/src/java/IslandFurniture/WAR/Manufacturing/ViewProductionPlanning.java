/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.Manufacturing;


import IslandFurniture.EJB.Manufacturing.MaterialResourcePlanningView;
import IslandFurniture.EJB.Manufacturing.ManageProductionPlanningRemote;
import IslandFurniture.WAR.HELPER.helper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.primefaces.component.datatable.DataTable;

@SessionScoped
@ManagedBean(name = "DemandPlanning")
public class ViewProductionPlanning implements Serializable {

    @EJB
    private ManageProductionPlanningRemote mpp;

    @EJB
    MaterialResourcePlanningView dpv;

    private List<SelectItem> MF_LIST;
    private String MF;
    private List<ColumnModel> dates_columns=new ArrayList<ColumnModel> ();

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
        try {
            mpp.setMF(this.MF);
        } catch (Exception ex) {
        }
        //Create the Datatable
        dates_columns.clear();
        dates_columns.add(new ColumnModel("A", "a"));
        System.out.println("ViewProductionPlanning() MF Changed to " + MF);

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

    static public class ColumnModel implements Serializable {

        private String header;
        private String property;

        public ColumnModel(String header, String property) {
            this.header = header;
            this.property = property;
        }

        public String getHeader() {
            return header;
        }

        public String getProperty() {
            return property;
        }
    }

}
