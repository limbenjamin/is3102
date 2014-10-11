/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.Manufacturing;

import IslandFurniture.Enums.Month;
import IslandFurniture.Entities.MonthlyProductionPlan;
import IslandFurniture.Entities.WeeklyProductionPlan;
import IslandFurniture.EJB.Manufacturing.ManageProductionPlanningEJBBeanInterface;
import IslandFurniture.EJB.Manufacturing.ManageProductionPlanningLocal;
import IslandFurniture.StaticClasses.Helper;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import IslandFurnitures.DataStructures.JDataTable;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;
import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.event.TabChangeEvent;

@SessionScoped
@ManagedBean(name = "ProductionPlanning")
public class ViewProductionPlanning implements Serializable {

    private boolean debugmode = false;

    @EJB
    private ManageProductionPlanningLocal mpp;

    @EJB
    ManageProductionPlanningEJBBeanInterface dpv;

    private String success_msg;
    private String currentDrill = "";

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
        HttpSession session = Util.getSession();
        String username = (String) session.getAttribute("username");
        this.MF = dpv.getAuthorizedMF(username).keySet().iterator().next();
        try {
            mpp.setMF(this.MF);
            tooglePage("", "PP_TABLE");
        } catch (Exception ex) {
        }
    }

    public String getMF() {
        return MF;
    }

    public void setMF(String MF) {

        this.MF = MF;

    }

    public void pullPPTableFromBean() throws Exception {

        if (!MF.isEmpty()) {
            dt = (JDataTable<String>) dpv.getDemandPlanningTable(MF);
            Integer i = 0;

            System.out.println("updateTable(): Production Table Reconstructed for " + MF + " Rows Count:" + dt.getRowCount());
        }

        return;

    }

    public void pullcapacityTableFromBean() throws Exception {

        if (!MF.isEmpty()) {

            capacity_dt = (JDataTable<String>) dpv.getCapacityList(MF);
            Integer i = 0;

            System.out.println("updateTable(): Capacity Reconstructed for " + MF + " Rows Count:" + capacity_dt.getRowCount());
        }

        return;
    }

    public void pullMRPTableFromBean(String month) throws Exception {

        if (!MF.isEmpty()) {

            mrp = (JDataTable<String>) dpv.getWeeklyMRPTable(currentDrill, MF);
            Integer i = 0;
            System.out.println("updateTable(): MRP Reconstructed for " + MF + " " + month + " Rows Count:" + mrp.getRowCount());
        }

        return;
    }

    public void updatePCTableToBean() throws Exception {

        try {
            ArrayList<Object> o = capacity_dt.getStateChangedEntities();
            if (o.size() > 0) {
                dpv.updateListOfEntities(o);

            }
        } catch (Exception ex) {

            error_msg = ex.getMessage();
            throw (ex);
        }

        try {
            mpp.setMF(MF);
            mpp.CreateProductionPlanFromForecast();
            pullPPTableFromBean();

        } catch (Exception ex) {
            if (ex.getMessage() != null) {
                error_msg = ex.getMessage();
            } else {
                error_msg = "New Capacity Data does not work. Reason: Capacity does not fufill planned requirement";
            }
            throw new Exception(error_msg);
        }

        pullcapacityTableFromBean();

    }

    public void pullWeeklyProductionTable(String Month) throws Exception {
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

        try {
            ArrayList<Object> o = wpp.getStateChangedEntities();
            if (o.size() == 0) {
                return;
            }

            dpv.updateListOfEntities(o);
            WeeklyProductionPlan wpp = (WeeklyProductionPlan) o.get(0);

        } catch (Exception ex) {
            error_msg = ex.getMessage();
        }
        pullWeeklyProductionTable(currentDrill);
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
            tooglePage("", "PP_TABLE");
        } else if (event.getTab().getTitle().equals("Capacity Management")) {
            tooglePage("", "Capacity_Table");
        } else if (event.getTab().getTitle().equals("Weekly Production Planning")) {
            try {
                tooglePage("", "WPP_TABLE");
            } catch (Exception ex) {
            }
        } else if (event.getTab().getTitle().equals("Material Resource Planning")) {
            tooglePage("", "MRP_TABLE");
        }
    }

    public void tooglePage(String ID, String command) {
        try {
            success_msg = "";
            error_msg = "";
            tooglePageLoader(ID, command);

        } catch (Exception ex) {
            success_msg = "";

            if (ex.getMessage() == null) {
                error_msg = "Error: Not Successful ! Business Logic Error ? Perhaps not enough capacity ?";
            } else {
                error_msg = "ERROR: " + ex.getMessage();
                System.out.println("tooglePage(): Exception " + ex.getMessage());
            }

            //FacesContext.getCurrentInstance().addMessage("PP", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", error_msg));
        }

    }

    public void tooglePageLoader(String ID, String command) throws Exception {
        switch (command) {
            case "CommenceMonth":
                int monthNo = Integer.valueOf(Month.valueOf(ID.split("/")[0]).value);
                int yearNo = Integer.valueOf(ID.split("/")[1]);
                for (int i = 1; i <= Helper.getNumOfWeeks(monthNo, yearNo); i++) {
                    mpp.commitallWPP(i, monthNo, yearNo);
                }
                for (int i = 1; i <= Helper.getNumOfWeeks(monthNo, yearNo); i++) {
                    mpp.orderMaterials(i, monthNo, yearNo);
                }
                pullWeeklyProductionTable(this.currentDrill);
                success_msg = "Successfully Commenced Month";
                break;
            case "UncommenceMonth":
                monthNo = Integer.valueOf(Month.valueOf(ID.split("/")[0]).value);
                yearNo = Integer.valueOf(ID.split("/")[1]);
                for (int i = 1; i <= Helper.getNumOfWeeks(monthNo, yearNo); i++) {
                    mpp.unOrderMaterials(i, monthNo, yearNo);
                }
                for (int i = 1; i <= Helper.getNumOfWeeks(monthNo, yearNo); i++) {
                    mpp.uncommitallWPP(i, monthNo, yearNo);
                }

                pullWeeklyProductionTable(this.currentDrill);
                success_msg = "Successfully Uncommenced Month";
                break;

            case "Update_PC":
                System.out.println("ViewProductionPlanning(): Updating Capacity");
                updatePCTableToBean();
                pullcapacityTableFromBean();
                success_msg = "Capacity Table Updated";
                break;

            case "Auto_Plan":
                System.out.println("ViewProductionPlanning(): User Triggered Production Planning Algorithm");
                mpp.CreateProductionPlanFromForecast();
                mpp.setMF(MF);
                pullPPTableFromBean();
                success_msg = "AI Planning Successful !";

                break;
            case "Update_PP":
                ArrayList<Object> o = dt.getStateChangedEntities();
                if (o.size() == 0) {
                    throw new Exception("No Production Plan !");
                }
                dpv.updateListOfEntities(o);
                MonthlyProductionPlan mppz = (MonthlyProductionPlan) o.get(0);
                pullPPTableFromBean();
                success_msg += "Status: Update Planning Capacity Success new value=" + mppz.getQTY() + " FOR " + MF;

                break;
            case "Capacity_Table":
                pullcapacityTableFromBean();
                success_msg = "Capacity Table pulled";
                break;
            case "WPP_TABLE_SPECIFY":
                pullWeeklyProductionTable(ID);
                success_msg = "Successfully pulled weekly Production Planning";
                break;
            case "WPP_TABLE":
                pullWeeklyProductionTable(this.currentDrill);
                success_msg = "Successfully pulled weekly Production Planning";
                break;
            case "PP_TABLE":
                pullPPTableFromBean();
                success_msg = "Successfully pulled Production Planning";
                break;
            case "MRP_TABLE":
                pullMRPTableFromBean(this.currentDrill);
                success_msg = "Successfully pulled Material Planning !";
                break;
            case "COMMIT_WPP":

                mpp.commitWPP(Integer.valueOf(ID));
                pullWeeklyProductionTable(this.currentDrill);
                success_msg = "Commited Weekly Production Plan !";
                break;
            case "UNCOMMIT_WPP":
                mpp.uncommitWPP(Integer.valueOf(ID));
                pullWeeklyProductionTable(this.currentDrill);
                success_msg = "Uncommited Weekly Production Plan !";
                break;
            case "Commit_All_Material":
                int weekNo = Integer.valueOf(ID.split("_")[0]);
                monthNo = Integer.valueOf(ID.split("_")[1]);
                yearNo = Integer.valueOf(ID.split("_")[2]);
                mpp.commitallWPP(weekNo, monthNo, yearNo);
                pullWeeklyProductionTable(this.currentDrill);
                success_msg = "Commited all Materials For Week!";
                break;
            case "Uncommit_All_Material":
                weekNo = Integer.valueOf(ID.split("_")[0]);
                monthNo = Integer.valueOf(ID.split("_")[1]);
                yearNo = Integer.valueOf(ID.split("_")[2]);
                mpp.uncommitallWPP(weekNo, monthNo, yearNo);
                pullWeeklyProductionTable(this.currentDrill);
                success_msg = "Uncommited all Materials For Week!";
                break;
            case "COMMIT_WEEK_WPP":
                weekNo = Integer.valueOf(ID.split("_")[0]);
                monthNo = Month.valueOf(ID.split("_")[1]).value;
                yearNo = Integer.valueOf(ID.split("_")[2]);
                mpp.orderMaterials(weekNo, monthNo, yearNo);
                mpp.createPOForWeekMRP(weekNo, monthNo, yearNo);
                pullWeeklyProductionTable(this.currentDrill);
                success_msg = "Commited all Weekly Production Plan !";
                break;
            case "UNCOMMIT_WEEK_WPP":
                weekNo = Integer.valueOf(ID.split("_")[0]);
                monthNo = Month.valueOf(ID.split("_")[1]).value;
                yearNo = Integer.valueOf(ID.split("_")[2]);
                mpp.uncreatePOForWeekMRP(weekNo, monthNo, yearNo);
                mpp.unOrderMaterials(weekNo, monthNo, yearNo);
                pullWeeklyProductionTable(this.currentDrill);
                success_msg = "Uncommited all Weekly Production Plan !";

                break;

//            case "ORDER_MATERIAL":
//                yearNo = Integer.valueOf(ID.split("_")[0]);
//                monthNo = Integer.valueOf(ID.split("_")[1]);
//                weekNo = Integer.valueOf(ID.split("_")[2]);
//                mpp.createPOForWeekMRP(weekNo, monthNo, yearNo);
//                pullWeeklyProductionTable(this.currentDrill);
//                success_msg = "Ordered Material For Week !";
//                break;
//            case "UNORDER_MATERIAL":
//                yearNo = Integer.valueOf(ID.split("_")[0]);
//                monthNo = Integer.valueOf(ID.split("_")[1]);
//                weekNo = Integer.valueOf(ID.split("_")[2]);
//                mpp.uncreatePOForWeekMRP(weekNo, monthNo, yearNo);
//
//                pullWeeklyProductionTable(this.currentDrill);
//                success_msg = "Unordered Material For Week !";
//                break;
        }
        //FacesContext.getCurrentInstance().addMessage("PP", new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", success_msg));

    }

    public void listenToCell(ActionEvent actionEvent) throws IOException {
        CommandButton button = (CommandButton) actionEvent.getComponent();
        String ID = button.getAlt();
        String command = button.getLabel();
        System.out.println("listenToCell(): " + command + " ON " + ID);
        tooglePage(ID, command);

    }

}
