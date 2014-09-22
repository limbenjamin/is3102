/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.SalesPlanning;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.EJB.Entities.CountryOffice;
import IslandFurniture.EJB.Entities.MonthlyStockSupplyReq;
import IslandFurniture.EJB.Entities.Plant;
import IslandFurniture.EJB.Entities.Staff;
import IslandFurniture.EJB.Entities.Stock;
import IslandFurniture.EJB.SalesPlanning.SalesForecastBeanLocal;
import IslandFurniture.StaticClasses.Helper.Couple;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@ManagedBean(name = "reviewForecastManagedBean")
@ViewScoped
public class ReviewForecast {

    @EJB
    private SalesForecastBeanLocal salesForecastBean;

    @EJB
    private ManageUserAccountBeanLocal staffBean;

    private Staff staff;
    private CountryOffice co;

    String panelActive = "0";
    String statusMessage = "";

    Future<Boolean> asyncResult;

    private List<Couple<String, String>> mssrLabels = new ArrayList();
    private List<Couple<Stock, Couple<List<MonthlyStockSupplyReq>, List<MonthlyStockSupplyReq>>>> mssrPairedList;

    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        this.staff = staffBean.getStaff((String) session.getAttribute("username"));

        Plant plant = staff.getPlant();

        if (plant instanceof CountryOffice) {
            this.co = (CountryOffice) plant;

            this.loadMssr();
        } else {
            try {
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect(ec.getRequestContextPath());
            } catch (IOException ex) {

            }
        }

    }

    public void loadMssr() {
        this.mssrLabels = MonthlyStockSupplyReq.getLabels();
        this.mssrPairedList = salesForecastBean.retrievePairedMssrForCo(co, 6);

        // Expand all accordion panels by default
        for (int i = 1; i < this.mssrPairedList.size(); i++) {
            this.panelActive += "," + i;
        }
    }

    public void reviewForecast(boolean approved) {
        try {
            List<Couple<Stock, List<MonthlyStockSupplyReq>>> coupleList = new ArrayList();

            for (Couple<Stock, Couple<List<MonthlyStockSupplyReq>, List<MonthlyStockSupplyReq>>> mssrEntry : this.mssrPairedList) {
                coupleList.add(new Couple(mssrEntry.getFirst(), mssrEntry.getSecond().getSecond()));
            }

            salesForecastBean.reviewMonthlyStockSupplyReq(coupleList, approved);

            if (approved) {
                statusMessage = "Forecast approved.";
            } else {
                statusMessage = "Forecast rejected.";
            }
        } catch (Exception ex) {
            statusMessage = "Error reviewing forecast: " + ex.getMessage();
        }
    }

    public void approveForecast(AjaxBehaviorEvent event) {
        System.out.println("Approve Forecast");

        reviewForecast(true);
    }

    public void rejectForecast(AjaxBehaviorEvent event) {
        System.out.println("Reject Forecast");

        reviewForecast(false);
    }

    /**
     * Creates a new instance of CreateForecastManagedBean
     */
    public ReviewForecast() {
    }

    public String getPanelActive() {
        return panelActive;
    }

    public void setPanelActive(String panelActive) {
        this.panelActive = panelActive;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public List<Couple<Stock, Couple<List<MonthlyStockSupplyReq>, List<MonthlyStockSupplyReq>>>> getMssrPairedList() {
        return mssrPairedList;
    }

    public void setMssrPairedList(List<Couple<Stock, Couple<List<MonthlyStockSupplyReq>, List<MonthlyStockSupplyReq>>>> mssrPairedList) {
        this.mssrPairedList = mssrPairedList;
    }

    public List<Couple<String, String>> getMssrLabels() {
        return mssrLabels;
    }

    public void setMssrLabels(List<Couple<String, String>> mssrLabels) {
        this.mssrLabels = mssrLabels;
    }

}