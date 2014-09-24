/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.Manufacturing;

import IslandFurniture.EJB.Entities.ManufacturingFacility;
import IslandFurniture.EJB.Entities.MonthlyProductionPlan;
import IslandFurniture.EJB.Entities.ProductionCapacity;
import IslandFurniture.EJB.Entities.WeeklyProductionPlan;
import IslandFurniture.EJB.Manufacturing.ManageProductionPlanningEJBBeanInterface;
import IslandFurniture.EJB.Manufacturing.ManageProductionPlanningRemote;
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
import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.event.TabChangeEvent;

@SessionScoped
@ManagedBean(name = "ProductionPlanning")
public class ViewProductionPlanning implements Serializable {

    @EJB
    private ManageProductionPlanningRemote mpp;

    @EJB
    ManageProductionPlanningEJBBeanInterface dpv;

    private String success_msg;
    private String currentDrill = "";

    private List<SelectItem> MF_LIST;
    private String MF;

    private String error_msg = "";

    private int activePanelIndex = 0;

    public int getActivePanelIndex() {
        return activePanelIndex;
    }

    public void setActivePanelIndex(int activePanelIndex) {
        this.activePanelIndex = activePanelIndex;
    }

    private IslandFurnitures.DataStructures.JDataTable<String> dt;

    private IslandFurnitures.DataStructures.JDataTable<String> capacity_dt;

    private IslandFurnitures.DataStructures.JDataTable<String> wpp;

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

    public String getCurrentDrill() {
        return currentDrill;
    }

    public void setCurrentDrill(String currentDrill) {
        this.currentDrill = currentDrill;
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
        pullcapacityTableFromBean();
        wpp = null;

    }

    public void updatePPTableToBean() {
        try {
            ArrayList<Object> o = dt.getStateChangedEntities();
            if (o.size() == 0) {
                return;
            }
            dpv.updateListOfEntities(o);
            MonthlyProductionPlan mpp = (MonthlyProductionPlan) o.get(0);
            success_msg += "Status: Update Planning Capacity Success new value=" + mpp.getQTY() + " FOR " + MF;
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
                success_msg += "Status: Successfully pulled planning table for " + MF;
                System.out.println("updateTable(): Production Table Reconstructed for " + MF + " Rows Count:" + dt.getRowCount());
            }
        } catch (Exception ex) {
            success_msg = "";
            error_msg = ex.getMessage();
        }
        return;

    }

    public void pullcapacityTableFromBean() {
        try {
            if (!MF.isEmpty()) {

                capacity_dt = (JDataTable<String>) dpv.getCapacityList(MF);
                Integer i = 0;
                success_msg += "Status: Successfully pulled capacity table for " + MF;
                System.out.println("updateTable(): Capacity Reconstructed for " + MF + " Rows Count:" + capacity_dt.getRowCount());
            }
        } catch (Exception ex) {
            success_msg = "";
            error_msg = ex.getMessage();
        }
        return;
    }

    public void updatePCTableToBean() {
        try {
            ArrayList<Object> o = capacity_dt.getStateChangedEntities();
            if (o.size() == 0) {
                return;
            }
            dpv.updateListOfEntities(o);
            ProductionCapacity pc = (ProductionCapacity) o.get(0);
            success_msg += "Status: Update Production Daily Capacity Success. new value=" + pc.getQty() + " FOR " + MF + ". Please re-do production planning !";
        } catch (Exception ex) {
            success_msg = "";
            error_msg = ex.getMessage();
        }

        pullcapacityTableFromBean();

    }

    public String automaticPlanning() {
        try {
            System.out.println("ViewProductionPlanning() User Triggered Production Planning Algorithm");
            mpp.CreateProductionPlanFromForecast();
            mpp.setMF(MF);
            pullPPTableFromBean();
            FacesContext.getCurrentInstance().getExternalContext().redirect(".");
        } catch (Exception ex) {
            success_msg = "";
        }
        return ".";
    }

    public void drillDownMonth(String Month) {
        currentDrill = Month;
        System.out.println("DrillDownMonth(): Drilling Down to:" + Month);
        wpp = (JDataTable<String>) dpv.getWeeklyPlans(Month, this.MF);
        activePanelIndex = 1;

    }

    public JDataTable<String> getWpp() {
        return wpp;
    }

    public void setWpp(JDataTable<String> wpp) {
        this.wpp = wpp;
    }

    public void updateWPPTableToBean() {
        try {
            ArrayList<Object> o = wpp.getStateChangedEntities();
            if (o.size() == 0) {
                return;
            }

            dpv.updateListOfEntities(o);
            WeeklyProductionPlan wpp = (WeeklyProductionPlan) o.get(0);
            success_msg += "<br/>Status: Update Planning Capacity Success new value=" + wpp.getQTY() + " FOR " + MF;
        } catch (Exception ex) {
            success_msg = "";
            error_msg = ex.getMessage();
        }
        drillDownMonth(currentDrill);

    }

    public void onTabChange(TabChangeEvent event) {

        if (event.getTab().getTitle().equals("Production Planning")) {
            pullPPTableFromBean();
        } else if (event.getTab().getTitle().equals("Capacity Management")) {
            pullcapacityTableFromBean();
        } else if (event.getTab().getTitle().equals("Weekly Production Planning")) {
            drillDownMonth(this.currentDrill);
        }
    }

    public void listenToCell(ActionEvent actionEvent) {
        try {
            CommandButton button = (CommandButton) actionEvent.getComponent();
            String ID = button.getAlt();
            String command = button.getLabel();

            switch (command) {
                case "COMMIT_WPP":
                    mpp.commitWPP(Integer.valueOf(ID));
                    drillDownMonth(this.currentDrill);
                    break;
                case "UNCOMMIT_WPP":
                    mpp.uncommitWPP(Integer.valueOf(ID));
                    drillDownMonth(this.currentDrill);
                    break;

            }
            success_msg += "Successfully Toggled Production Order";
            System.out.println("listenToCell(): " + command + " ON " + ID);
        } catch (Exception ex) {
            success_msg = "";
            error_msg = ex.getMessage();
        }
    }

}
