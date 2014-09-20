/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.Manufacturing;

import IslandFurniture.EJB.Entities.MonthlyProductionPlan;
import IslandFurniture.EJB.Manufacturing.ManageProductionPlanningRemote;
import IslandFurniture.EJB.Manufacturing.ManageProductionPlanningEJBBeanInterface;
import IslandFurniture.WAR.HELPER.helper;
import IslandFurnitures.DataStructures.JDataTable;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

@SessionScoped
@ManagedBean(name = "ProductionPlanning")
public class ViewProductionPlanning implements Serializable {

    @EJB
    private ManageProductionPlanningRemote mpp;

    @EJB
    ManageProductionPlanningEJBBeanInterface dpv;

    private String success_msg;

    private List<SelectItem> MF_LIST;
    private String MF;

    private String error_msg = "";

    private IslandFurnitures.DataStructures.JDataTable<String> dt;

    private IslandFurnitures.DataStructures.JDataTable<String> capacity_dt;

    public String getSuccess_msg() {
        return success_msg;
    }

    public void setSuccess_msg(String success_msg) {
        this.success_msg = success_msg;
    }

    public String getError_msg() {
        return error_msg;
    }

    public JDataTable<String> getDt() {
        return dt;
    }

    public void setDt(JDataTable<String> dt) {
        this.dt = dt;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public JDataTable<String> getCapacity_dt() {
        return capacity_dt;
    }

    public void setCapacity_dt(JDataTable<String> capacity_dt) {
        this.capacity_dt = capacity_dt;
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

    @PostConstruct
    public void init() {
        System.out.println("ViewProductionPlanning() Init!");
        MF_LIST = helper.convertHMToDropdown(dpv.getAuthorizedMF("AUTH_CHECK_TO_IMPLEMENT_SOON"));
        success_msg = "";
        error_msg = "";
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
        success_msg = "Status: Switched TO" + MF;
        System.out.println("ViewProductionPlanning() MF Changed to " + MF);
        //Create the Datatable
        pullPPTableFromBean();

    }

    public void updatePPTableToBean() {
        try {
            ArrayList<Object> o = dt.getStateChangedEntities();
            dpv.updateListOfEntities(o);
            MonthlyProductionPlan mpp = (MonthlyProductionPlan) o.get(0);
            success_msg = "Status: Update Planning Capacity Success new value=" + mpp.getQTY() + " FOR " + MF;
        } catch (Exception ex) {
            success_msg = "";
            error_msg = ex.getMessage();
        }
        pullPPTableFromBean();
    }

    public void pullPPTableFromBean() {
        try {
            if (!MF.isEmpty()) {
                dt = (JDataTable<String>) dpv.getDemandPlanningTable(MF);
                Integer i = 0;
                success_msg = "Status: Successfully pulled planning table for " + MF;
                System.out.println("updateTable(): Production Table Reconstructed for " + MF + " Rows Count:" + dt.getRowCount());
            }
        } catch (Exception ex) {

        }
        return;

    }

    public void updateCapacityTableFromBean() {
        try {
            if (!MF.isEmpty()) {
                dt = (JDataTable<String>) dpv.getCapacityList(MF);

                success_msg = "Status: Successfully pulled Capacity table for " + MF;
                System.out.println("updateTable(): Capacity Table Reconstructed for " + MF + " Rows Count:" + dt.getRowCount());
            }
        } catch (Exception ex) {

        }
        return;
    }

    public String automaticPlanning() {
        try {
            System.out.println("ViewProductionPlanning() User Triggered Production Planning Algorithm");
            try {
                mpp.CreateProductionPlanFromForecast();
                mpp.setMF(MF);
                pullPPTableFromBean();
            } catch (Exception ex) {
                success_msg = "";
                error_msg = ex.getMessage();
            }

            FacesContext.getCurrentInstance().getExternalContext().redirect(".");
        } catch (IOException ex) {
            success_msg = "";
            error_msg = ex.getMessage();
        }
        return ".";
    }

}
