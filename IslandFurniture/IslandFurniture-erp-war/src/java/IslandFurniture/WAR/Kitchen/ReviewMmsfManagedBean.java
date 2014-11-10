/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.Kitchen;

import IslandFurniture.DataStructures.Couple;
import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.EJB.ITManagement.ManageOrganizationalHierarchyBeanLocal;
import IslandFurniture.EJB.Kitchen.FoodForecastBeanLocal;
import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.MenuItem;
import IslandFurniture.Entities.MonthlyMenuItemSalesForecast;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Staff;
import IslandFurniture.Entities.Store;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
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
public class ReviewMmsfManagedBean implements Serializable {

    @EJB
    private ManageOrganizationalHierarchyBeanLocal manageOrganizationalHierarchyBean;

    @EJB
    private FoodForecastBeanLocal foodForecastBean;

    @EJB
    private ManageUserAccountBeanLocal staffBean;

    private Staff staff;
    private CountryOffice co;

    private List<Store> storeList;
    private String storeId;
    private Store store;

    private String panelActive = "0";
    private String errorMessage = "";
    private String successMessage = "";

    private List<Couple<String, String>> mmsfLabels = new ArrayList();
    private List<Couple<MenuItem, Couple<List<MonthlyMenuItemSalesForecast>, List<MonthlyMenuItemSalesForecast>>>> mmsfPairedList;

    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        this.staff = staffBean.getStaff((String) session.getAttribute("username"));
        this.storeId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("store");

        errorMessage = "";
        successMessage = "";

        Plant plant = staff.getPlant();

        if (plant instanceof CountryOffice) {
            this.co = (CountryOffice) plant;

            storeList = manageOrganizationalHierarchyBean.displayStore();
            for (Iterator itr = storeList.iterator(); itr.hasNext();) {
                Store eachStore = (Store) itr.next();
                if (!eachStore.getCountryOffice().equals(co)) {
                    itr.remove();
                }
            }

            if (storeId != null) {
                Plant plantEntered = null;
                try {
                    plantEntered = manageOrganizationalHierarchyBean.getPlantById(new Long(storeId));
                } catch (Exception ex) {
                }

                if (plantEntered != null && plantEntered instanceof Store) {
                    store = (Store) plantEntered;
                    this.loadMmsf();
                }
            }
        } else {
            try {
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect(ec.getRequestContextPath());
            } catch (IOException ex) {

            }
        }

    }

    public void loadMmsf() {
        System.out.println("Store is: " + store);

        this.mmsfLabels = MonthlyMenuItemSalesForecast.getLabels();
        this.mmsfPairedList = new ArrayList();

        for (MenuItem mi : co.getMenuItems()) {
            List<MonthlyMenuItemSalesForecast> lockedMmsfList = foodForecastBean.retrieveLockedMmsfForStoreMi(store, mi, 6);
            List<MonthlyMenuItemSalesForecast> unlockedMmsfList = foodForecastBean.retrieveUnlockedMmsfForStoreMi(store, mi);

            this.mmsfPairedList.add(new Couple(mi, new Couple(lockedMmsfList, unlockedMmsfList)));
        }

        // Expand all accordion panels by default
        for (int i = 1; i < this.mmsfPairedList.size(); i++) {
            this.panelActive += "," + i;
        }
    }

    public void reviewForecast(boolean approved) {
        try {
            List<Couple<MenuItem, List<MonthlyMenuItemSalesForecast>>> coupleList = new ArrayList();

            for (Couple<MenuItem, Couple<List<MonthlyMenuItemSalesForecast>, List<MonthlyMenuItemSalesForecast>>> mmsfEntry : this.mmsfPairedList) {
                coupleList.add(new Couple(mmsfEntry.getFirst(), mmsfEntry.getSecond().getSecond()));
            }

            foodForecastBean.reviewMonthlyMenuItemSalesForecast(coupleList, approved);

            if (approved) {
                successMessage = "Forecast approved successfully";
            } else {
                successMessage = "Forecast rejected successfully";
            }
        } catch (Exception ex) {
            errorMessage = "Error reviewing forecast: " + ex.getMessage();
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
     * Creates a new instance of ReviewMmsfManagedBean
     */
    public ReviewMmsfManagedBean() {
    }

    public CountryOffice getCo() {
        return co;
    }

    public void setCo(CountryOffice co) {
        this.co = co;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public List<Store> getStoreList() {
        return storeList;
    }

    public void setStoreList(List<Store> storeList) {
        this.storeList = storeList;
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
