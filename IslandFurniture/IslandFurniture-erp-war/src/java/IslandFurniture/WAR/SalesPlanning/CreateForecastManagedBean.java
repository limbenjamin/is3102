/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.SalesPlanning;

import IslandFurniture.EJB.CommonInfrastructure.ManageNotificationsBeanLocal;
import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.MonthlyStockSupplyReq;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Staff;
import IslandFurniture.Entities.Stock;
import IslandFurniture.Entities.StockSupplied;
import IslandFurniture.Exceptions.ForecastFailureException;
import IslandFurniture.Exceptions.InvalidInputException;
import IslandFurniture.Exceptions.InvalidMssrException;
import IslandFurniture.EJB.ITManagement.ManagePrivilegesBeanLocal;
import IslandFurniture.EJB.SalesPlanning.SalesForecastBeanLocal;
import IslandFurniture.DataStructures.Couple;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
@ManagedBean(name = "createForecastManagedBean")
@ViewScoped
public class CreateForecastManagedBean implements Serializable {

    @EJB
    private ManagePrivilegesBeanLocal managePrivilegesBean;

    @EJB
    private ManageNotificationsBeanLocal manageNotificationsBean;

    @EJB
    private SalesForecastBeanLocal salesForecastBean;

    @EJB
    private ManageUserAccountBeanLocal staffBean;

    private Staff staff;
    private CountryOffice co;

    String panelActive = "0";
    String errorMessage = "";
    String successMessage = "";

    int numPoints;
    int plannedInv;

    private List<Couple<String, String>> mssrLabels = new ArrayList();
    private List<Couple<Stock, Couple<List<MonthlyStockSupplyReq>, List<MonthlyStockSupplyReq>>>> mssrPairedList;

    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        this.staff = staffBean.getStaff((String) session.getAttribute("username"));

        errorMessage = "";
        successMessage = "";

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
        this.mssrPairedList = new ArrayList();

        for (StockSupplied ss : co.getSuppliedWithFrom()) {
            List<MonthlyStockSupplyReq> lockedMssr = salesForecastBean.retrieveLockedMssrForCoStock(co, ss.getStock(), 6);
            List<MonthlyStockSupplyReq> unlockedMssr = salesForecastBean.retrieveUnlockedMssrForCoStock(co, ss.getStock());

            this.mssrPairedList.add(new Couple(ss.getStock(), new Couple(lockedMssr, unlockedMssr)));
        }

        // Expand all accordion panels by default
        for (int i = 1; i < this.mssrPairedList.size(); i++) {
            this.panelActive += "," + i;
        }
    }

    public void naiveForecast(AjaxBehaviorEvent event) {
        boolean impacted = false;
        boolean noPrevious = false;
        errorMessage = "";
        successMessage = "";

        for (Couple<Stock, Couple<List<MonthlyStockSupplyReq>, List<MonthlyStockSupplyReq>>> couple : this.mssrPairedList) {
            try {
                couple.getSecond().setSecond(salesForecastBean.retrieveNaiveForecast(co, couple.getFirst()));
                impacted = true;
            } catch (ForecastFailureException ex) {

            } catch (InvalidMssrException ex) {
                noPrevious = true;
                errorMessage = ex.getMessage();
            }
        }

        if (!impacted) {
            errorMessage = "Failed to forecast: There are no available months to forecast!";
        } else {
            if (!noPrevious) {
                successMessage = "Naive forecast performed successfullly!";
            }
        }
    }

    public void nPointForecast(AjaxBehaviorEvent event) {
        try {
            boolean impacted = false;
            errorMessage = "";
            successMessage = "";

            for (Couple<Stock, Couple<List<MonthlyStockSupplyReq>, List<MonthlyStockSupplyReq>>> couple : this.mssrPairedList) {
                try {
                    couple.getSecond().setSecond(salesForecastBean.retrieveNPointForecast(co, couple.getFirst(), this.numPoints, this.plannedInv));
                    impacted = true;
                } catch (ForecastFailureException ex) {
                    errorMessage += " " + ex.getMessage() + ",";
                }
            }

            if (!impacted) {
                errorMessage = "Failed to forecast: There are no available months to forecast!";
            } else {
                if (errorMessage.isEmpty()) {
                    successMessage = numPoints + "-Point forecast performed successfullly!";
                } else {
                    errorMessage = "Forecast not performed for:" + errorMessage.substring(0, errorMessage.length() - 1);
                    errorMessage += "\n\nThis could be due to forecasts already made or there are no historical sales for reference.";
                }
            }
        } catch (InvalidInputException ex) {
            errorMessage = ex.getMessage();
        }
    }

    public void saveForecast(AjaxBehaviorEvent event) {
        try {
            List<Couple<Stock, List<MonthlyStockSupplyReq>>> coupleList = new ArrayList();
            errorMessage = "";
            successMessage = "";

            for (Couple<Stock, Couple<List<MonthlyStockSupplyReq>, List<MonthlyStockSupplyReq>>> mssrEntry : this.mssrPairedList) {
                coupleList.add(new Couple(mssrEntry.getFirst(), mssrEntry.getSecond().getSecond()));
            }

            salesForecastBean.saveMonthlyStockSupplyReq(coupleList);

            manageNotificationsBean.createNewNotificationForPrivilegeFromPlant("Pending Requirements Forecast", "New requirements forecast awaiting your approval", "/salesplanning/reviewforecast.xhtml", "Review Forecast", managePrivilegesBean.getPrivilegeFromName("Review Forecast"), co);

            successMessage = "Forecast saved successfully!";
        } catch (InvalidMssrException ex) {
            errorMessage = "Error saving forecast: " + ex.getMessage();
        }
    }

    /**
     * Creates a new instance of CreateForecastManagedBean
     */
    public CreateForecastManagedBean() {
    }

    public String getPanelActive() {
        return panelActive;
    }

    public void setPanelActive(String panelActive) {
        this.panelActive = panelActive;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }

    public int getNumPoints() {
        return numPoints;
    }

    public void setNumPoints(int numPoints) {
        this.numPoints = numPoints;
    }

    public int getPlannedInv() {
        return plannedInv;
    }

    public void setPlannedInv(int plannedInv) {
        this.plannedInv = plannedInv;
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
