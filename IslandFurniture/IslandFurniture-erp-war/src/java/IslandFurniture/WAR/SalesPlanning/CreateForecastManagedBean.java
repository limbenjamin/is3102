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
import IslandFurniture.EJB.Entities.StockSupplied;
import IslandFurniture.EJB.Exceptions.ForecastFailureException;
import IslandFurniture.EJB.SalesPlanning.SalesForecastBeanLocal;
import IslandFurniture.StaticClasses.Helper.Couple;
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
    private SalesForecastBeanLocal salesForecastBean;

    @EJB
    private ManageUserAccountBeanLocal staffBean;

    private Staff staff;
    private CountryOffice co;

    String panelActive = "0";
    String statusMessage = "";

    int numPoints;
    int plannedInv;

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

        for (Couple<Stock, Couple<List<MonthlyStockSupplyReq>, List<MonthlyStockSupplyReq>>> couple : this.mssrPairedList) {
            try {
                couple.getSecond().setSecond(salesForecastBean.retrieveNaiveForecast(co, couple.getFirst()));
                impacted = true;
            } catch (Exception ex) {
                statusMessage = "Failed to forecast: " + ex.getMessage();
            }
        }

        if (!impacted) {
            statusMessage = "Failed to forecast: There are no available months to forecast!";
        } else {
            statusMessage = "Naive forecast performed successfullly!";
        }
    }

    public void nPointForecast(AjaxBehaviorEvent event) {
        boolean impacted = false;
        boolean illegalArg = false;
        statusMessage = "";

        for (Couple<Stock, Couple<List<MonthlyStockSupplyReq>, List<MonthlyStockSupplyReq>>> couple : this.mssrPairedList) {
            try {
                couple.getSecond().setSecond(salesForecastBean.retrieveNPointForecast(co, couple.getFirst(), this.numPoints, this.plannedInv));
                impacted = true;
            } catch (ForecastFailureException ex) {
                if (!ex.getMessage().equals("NoMonths")) {
                    statusMessage += " " + ex.getMessage() + ",";
                }
            } catch (IllegalArgumentException ex) {
                illegalArg = true;
                statusMessage = ex.getMessage();
                break;
            }
        }

        if (!illegalArg) {
            if (!impacted) {
                statusMessage = "Failed to forecast: There are no available months to forecast!";
            } else {
                if (statusMessage.isEmpty()) {
                    statusMessage = numPoints + "-Point forecast performed successfullly!";
                } else {
                    statusMessage = "No historical data for:" + statusMessage.substring(0, statusMessage.length() - 1);
                }
            }
        }
    }

    public void saveForecast(AjaxBehaviorEvent event) {
        System.out.println("Ajax call");

        try {
            List<Couple<Stock, List<MonthlyStockSupplyReq>>> coupleList = new ArrayList();

            for (Couple<Stock, Couple<List<MonthlyStockSupplyReq>, List<MonthlyStockSupplyReq>>> mssrEntry : this.mssrPairedList) {
                coupleList.add(new Couple(mssrEntry.getFirst(), mssrEntry.getSecond().getSecond()));
            }

            salesForecastBean.saveMonthlyStockSupplyReq(coupleList);

            statusMessage = "Forecast saved successfully!";
        } catch (Exception ex) {
            statusMessage = "Error saving forecast: " + ex.getMessage();
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

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
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
