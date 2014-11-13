/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.Kitchen;

import IslandFurniture.DataStructures.Couple;
import IslandFurniture.EJB.CommonInfrastructure.ManageNotificationsBeanLocal;
import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.EJB.ITManagement.ManagePrivilegesBeanLocal;
import IslandFurniture.EJB.Kitchen.FoodForecastBeanLocal;
import IslandFurniture.Entities.MenuItem;
import IslandFurniture.Entities.MonthlyMenuItemSalesForecast;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Staff;
import IslandFurniture.Entities.Store;
import IslandFurniture.Enums.Month;
import IslandFurniture.Exceptions.ForecastFailureException;
import IslandFurniture.Exceptions.InvalidInputException;
import IslandFurniture.Exceptions.InvalidMmsfException;
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
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@ManagedBean
@ViewScoped
public class IngredPlannerManagedBean implements Serializable {

    @EJB
    private ManagePrivilegesBeanLocal managePrivilegesBean;

    @EJB
    private ManageNotificationsBeanLocal manageNotificationsBean;

    @EJB
    private FoodForecastBeanLocal foodForecastBean;

    @EJB
    private ManageUserAccountBeanLocal staffBean;

    private Staff staff;
    private Store store;

    private String panelActive = "0";
    private String successMessage = "";
    private String errorMessage = "";

    int numPoints;

    private List<Couple<String, String>> mmsfLabels = new ArrayList();
    private List<Couple<MenuItem, Couple<List<MonthlyMenuItemSalesForecast>, List<MonthlyMenuItemSalesForecast>>>> mmsfPairedList;

    private Month wmsfMonth;
    private int wmsfYear;

    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        this.staff = staffBean.getStaff((String) session.getAttribute("username"));

        successMessage = "";
        errorMessage = "";

        Plant plant = staff.getPlant();

        if (plant instanceof Store) {
            this.store = (Store) plant;

            this.loadMmsf();
        } else {
            try {
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect(ec.getRequestContextPath());
            } catch (IOException ex) {

            }
        }
    }

    public void loadMmsf() {
        this.mmsfLabels = MonthlyMenuItemSalesForecast.getLabels();
        this.mmsfPairedList = new ArrayList();

        for (MenuItem mi : store.getCountryOffice().getMenuItems()) {
            List<MonthlyMenuItemSalesForecast> lockedMssr = foodForecastBean.retrieveLockedMmsfForStoreMi(store, mi, 6);
            List<MonthlyMenuItemSalesForecast> unlockedMssr = foodForecastBean.retrieveUnlockedMmsfForStoreMi(store, mi);

            this.mmsfPairedList.add(new Couple(mi, new Couple(lockedMssr, unlockedMssr)));
        }

        // Expand all accordion panels by default
        for (int i = 1; i < this.mmsfPairedList.size(); i++) {
            this.panelActive += "," + i;
        }
    }

    public void naiveForecast(AjaxBehaviorEvent event) {
        boolean impacted = false;
        boolean noPrevious = false;

        for (Couple<MenuItem, Couple<List<MonthlyMenuItemSalesForecast>, List<MonthlyMenuItemSalesForecast>>> couple : this.mmsfPairedList) {
            try {
                couple.getSecond().setSecond(foodForecastBean.retrieveNaiveForecast(store, couple.getFirst()));
                impacted = true;
            } catch (ForecastFailureException ex) {

            } catch (InvalidMmsfException ex) {
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

            for (Couple<MenuItem, Couple<List<MonthlyMenuItemSalesForecast>, List<MonthlyMenuItemSalesForecast>>> couple : this.mmsfPairedList) {
                try {
                    couple.getSecond().setSecond(foodForecastBean.retrieveNPointForecast(store, couple.getFirst(), this.numPoints));
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
            List<Couple<MenuItem, List<MonthlyMenuItemSalesForecast>>> coupleList = new ArrayList();

            for (Couple<MenuItem, Couple<List<MonthlyMenuItemSalesForecast>, List<MonthlyMenuItemSalesForecast>>> mmsfEntry : this.mmsfPairedList) {
                coupleList.add(new Couple(mmsfEntry.getFirst(), mmsfEntry.getSecond().getSecond()));
            }

            foodForecastBean.saveMonthlyMenuItemSalesForecast(coupleList);

            manageNotificationsBean.createNewNotificationForPrivilegeFromPlant("Pending Restaurant Forecast", "New restaurant forecast awaiting your approval", "/kitchen/reviewmmsf.xhtml?store=" + store.getId(), "Review Forecast", managePrivilegesBean.getPrivilegeFromName("Review MMSF"), store.getCountryOffice());

            successMessage = "Forecast saved successfully!";
        } catch (InvalidMmsfException ex) {
            errorMessage = "Error saving forecast: " + ex.getMessage();
        }
    }

    public void fetchWmsf(ActionEvent event) {
        this.wmsfMonth = Month.valueOf(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("month"));
        this.wmsfYear = Integer.valueOf(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("year"));
        System.out.println(wmsfMonth + " | " + this.wmsfYear);
        try {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect("ingredplannerweek.xhtml?month=" + this.wmsfMonth + "&year=" + this.wmsfYear);
        } catch (IOException ex) {

        }
    }

    /**
     * Creates a new instance of CreateMmsfManagedBean
     */
    public IngredPlannerManagedBean() {
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public String getPanelActive() {
        return panelActive;
    }

    public void setPanelActive(String panelActive) {
        this.panelActive = panelActive;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getNumPoints() {
        return numPoints;
    }

    public void setNumPoints(int numPoints) {
        this.numPoints = numPoints;
    }

    public List<Couple<String, String>> getMmsfLabels() {
        return mmsfLabels;
    }

    public void setMmsfLabels(List<Couple<String, String>> mmsfLabels) {
        this.mmsfLabels = mmsfLabels;
    }

    public List<Couple<MenuItem, Couple<List<MonthlyMenuItemSalesForecast>, List<MonthlyMenuItemSalesForecast>>>> getMmsfPairedList() {
        return mmsfPairedList;
    }

    public void setMmsfPairedList(List<Couple<MenuItem, Couple<List<MonthlyMenuItemSalesForecast>, List<MonthlyMenuItemSalesForecast>>>> mmsfPairedList) {
        this.mmsfPairedList = mmsfPairedList;
    }

    public Month getWmsfMonth() {
        return wmsfMonth;
    }

    public void setWmsfMonth(Month wmsfMonth) {
        this.wmsfMonth = wmsfMonth;
    }

    public int getWmsfYear() {
        return wmsfYear;
    }

    public void setWmsfYear(int wmsfYear) {
        this.wmsfYear = wmsfYear;
    }

}
