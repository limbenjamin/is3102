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
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@ManagedBean
@ViewScoped
public class CreateMmsfManagedBean implements Serializable {

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

    String panelActive = "0";
    String successMessage = "";
    String errorMessage = "";

    int numPoints;

    private List<Couple<String, String>> mmsfLabels = new ArrayList();
    private List<Couple<MenuItem, Couple<List<MonthlyMenuItemSalesForecast>, List<MonthlyMenuItemSalesForecast>>>> mmsfPairedList;

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
                    errorMessage = "No historical data for:" + errorMessage.substring(0, errorMessage.length() - 1);
                }
            }
        } catch (InvalidInputException ex) {
            errorMessage = ex.getMessage();
        }
    }

    // Incomplete at the moment
    public void saveForecast(AjaxBehaviorEvent event) {
        try {
            List<Couple<MenuItem, List<MonthlyMenuItemSalesForecast>>> coupleList = new ArrayList();

            for (Couple<MenuItem, Couple<List<MonthlyMenuItemSalesForecast>, List<MonthlyMenuItemSalesForecast>>> mmsfEntry : this.mmsfPairedList) {
                coupleList.add(new Couple(mmsfEntry.getFirst(), mmsfEntry.getSecond().getSecond()));
            }

            foodForecastBean.saveMonthlyMenuItemSalesForecast(coupleList);

            manageNotificationsBean.createNewNotificationForPrivilegeFromPlant("Pending Restaurant Forecast", "New restaurant forecast awaiting your approval", "/kitchen/reviewmmsf.xhtml?store=" + store, "Review Forecast", managePrivilegesBean.getPrivilegeFromName("Review MMSF"), store.getCountryOffice());

            successMessage = "Forecast saved successfully!";
        } catch (InvalidMmsfException ex) {
            errorMessage = "Error saving forecast: " + ex.getMessage();
        }
    }

    /**
     * Creates a new instance of CreateMmsfManagedBean
     */
    public CreateMmsfManagedBean() {
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

}
