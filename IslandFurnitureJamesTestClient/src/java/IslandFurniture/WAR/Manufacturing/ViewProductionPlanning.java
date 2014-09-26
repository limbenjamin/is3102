/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.Manufacturing;

import IslandFurniture.EJB.Entities.Month;
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
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
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

    private IslandFurnitures.DataStructures.JDataTable<String> mrp;

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
        if (mrp != null) {
            mrp.Internalrows.clear();
            mrp.columns.clear();
        }
        wpp = null;

    }

    public void updatePPTableToBean() {
        cleanMsg();
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
        cleanMsg();
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
        cleanMsg();
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

    public void pullMRPTableFromBean(String month) {
        cleanMsg();
        try {
            if (!MF.isEmpty()) {

                mrp = (JDataTable<String>) dpv.getWeeklyMRPTable(currentDrill, MF);
                Integer i = 0;
                success_msg += "Status: Successfully pulled MRP table for " + MF + " " + month;
                System.out.println("updateTable(): MRP Reconstructed for " + MF + " " + month + " Rows Count:" + capacity_dt.getRowCount());
            }
        } catch (Exception ex) {
            success_msg = "";
            error_msg = ex.getMessage();
        }
        return;
    }

    public void updatePCTableToBean() {
        cleanMsg();
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

    public void cleanMsg() {
        success_msg = "";
        error_msg = "";
    }

    public void automaticPlanning() {
        cleanMsg();

        try {
            System.out.println("ViewProductionPlanning() User Triggered Production Planning Algorithm");
            mpp.CreateProductionPlanFromForecast();
            mpp.setMF(MF);
            pullPPTableFromBean();
            FacesContext.getCurrentInstance().getExternalContext().redirect(".");
        } catch (Exception ex) {
            success_msg = "";
            error_msg = "Unable to fufill capacity planning . Might not have solution to planning?";
        }
        return;
    }

    public void drillDownMonth(String Month) throws Exception {
        currentDrill = Month;
        System.out.println("DrillDownMonth(): Drilling Down to:" + Month);
        wpp = (JDataTable<String>) dpv.getWeeklyPlans(Month, this.MF);
        activePanelIndex = 1;
        pullMRPTableFromBean(this.currentDrill);

    }

    public JDataTable<String> getWpp() {
        return wpp;
    }

    public void setWpp(JDataTable<String> wpp) {
        this.wpp = wpp;
    }

    public void updateWPPTableToBean() throws Exception {
        cleanMsg();
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
        pullMRPTableFromBean(currentDrill);

    }

    public JDataTable<String> getMrp() {
        return mrp;
    }

    public void setMrp(JDataTable<String> mrp) {
        this.mrp = mrp;
    }

    public void onTabChange(TabChangeEvent event) {

        if (event.getTab().getTitle().equals("Production Planning")) {
            pullPPTableFromBean();
        } else if (event.getTab().getTitle().equals("Capacity Management")) {
            pullcapacityTableFromBean();
        } else if (event.getTab().getTitle().equals("Weekly Production Planning")) {
            try {
                drillDownMonth(this.currentDrill);
            } catch (Exception ex) {
            }
        } else if (event.getTab().getTitle().equals("Material Resource Planning")) {
            pullMRPTableFromBean(this.currentDrill);

        }
    }

    public void listenToCell(ActionEvent actionEvent) throws IOException {
        cleanMsg();
        try {
            CommandButton button = (CommandButton) actionEvent.getComponent();
            String ID = button.getAlt();
            String command = button.getLabel();
            System.out.println("listenToCell(): " + command + " ON " + ID);

            switch (command) {
                case "COMMIT_WPP":
                    mpp.commitWPP(Integer.valueOf(ID));
                    drillDownMonth(this.currentDrill);
                    break;
                case "UNCOMMIT_WPP":
                    mpp.uncommitWPP(Integer.valueOf(ID));
                    drillDownMonth(this.currentDrill);
                    break;
                case "COMMIT_WEEK_WPP":
                    int weekNo = Integer.valueOf(ID.split("_")[0]);
                    int monthNo = Month.valueOf(ID.split("_")[1]).value;
                    int yearNo = Integer.valueOf(ID.split("_")[2]);
                    mpp.orderMaterials(weekNo, monthNo, yearNo);
                    drillDownMonth(this.currentDrill);
                    break;

                case "UNCOMMIT_WEEK_WPP":
                    weekNo = Integer.valueOf(ID.split("_")[0]);
                    monthNo = Month.valueOf(ID.split("_")[1]).value;
                    yearNo = Integer.valueOf(ID.split("_")[2]);
                    mpp.unOrderMaterials(weekNo, monthNo, yearNo);
                    drillDownMonth(this.currentDrill);
                    break;
                case "Commit_All_Material":
                    weekNo = Integer.valueOf(ID.split("_")[0]);
                    monthNo = Integer.valueOf(ID.split("_")[1]);
                    yearNo = Integer.valueOf(ID.split("_")[2]);
                    mpp.commitallWPP(weekNo, monthNo, yearNo);
                    drillDownMonth(this.currentDrill);
                    break;
                case "Uncommit_All_Material":
                    weekNo = Integer.valueOf(ID.split("_")[0]);
                    monthNo = Integer.valueOf(ID.split("_")[1]);
                    yearNo = Integer.valueOf(ID.split("_")[2]);
                    mpp.uncommitallWPP(weekNo, monthNo, yearNo);
                    drillDownMonth(this.currentDrill);
                    break;
                case "ORDER_MATERIAL":
                    yearNo = Integer.valueOf(ID.split("_")[0]);
                    monthNo = Integer.valueOf(ID.split("_")[1]);
                    weekNo = Integer.valueOf(ID.split("_")[2]);
                    mpp.createPOForWeekMRP(weekNo, monthNo, yearNo);
                    drillDownMonth(this.currentDrill);
                    break;
                case "UNORDER_MATERIAL":
                    yearNo = Integer.valueOf(ID.split("_")[0]);
                    monthNo = Integer.valueOf(ID.split("_")[1]);
                    weekNo = Integer.valueOf(ID.split("_")[2]);
                    mpp.uncreatePOForWeekMRP(weekNo, monthNo, yearNo);
                    drillDownMonth(this.currentDrill);
                    break;

            }
            success_msg += "Successfully Toggled Production Order";

        } catch (Exception ex) {
            success_msg = "";
            error_msg = ex.getMessage();
        }
    }

}
