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
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Staff;
import IslandFurniture.Entities.Store;
import IslandFurniture.Entities.WeeklyMenuItemSalesForecast;
import IslandFurniture.Enums.Month;
import IslandFurniture.Exceptions.InvalidWmsfException;
import IslandFurniture.StaticClasses.Helper;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
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
public class IngredPlannerWeekManagedBean implements Serializable {

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

    private String successMessage = "";
    private String errorMessage = "";

    private Month month;
    private Integer year;

    private List<Couple<MenuItem, List<WeeklyMenuItemSalesForecast>>> wmsfPairedList;
    private boolean editable = true;
    private List<WeeklyMenuItemSalesForecast> notNullWmsfList;

    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        this.staff = staffBean.getStaff((String) session.getAttribute("username"));
        String monthParam = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("month");
        String yearParam = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("year");

        successMessage = "";
        errorMessage = "";

        Plant plant = staff.getPlant();

        if (plant instanceof Store && yearParam != null && monthParam != null) {
            this.store = (Store) plant;

            try {
                this.month = Month.valueOf(monthParam);
                this.year = Integer.valueOf(yearParam);

                this.loadWmsf();
            } catch (Exception ex) {
                try {
                    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                    ec.redirect(ec.getRequestContextPath());
                } catch (IOException IOex) {

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

    public void loadWmsf() {
        wmsfPairedList = new ArrayList();

        for (MenuItem mi : this.store.getCountryOffice().getMenuItems()) {
            List<WeeklyMenuItemSalesForecast> wmsfList = foodForecastBean.retrieveWmsfForStoreMi(store, mi, year, month.value);
            wmsfPairedList.add(new Couple(mi, wmsfList));
            if (wmsfList != null && !wmsfList.isEmpty()) {
                this.notNullWmsfList = wmsfList;
            }
        }

        this.editable = foodForecastBean.isWmsfListEditable(wmsfPairedList);
    }

    public void updateWmsf(AjaxBehaviorEvent event) {
        try {
            foodForecastBean.saveWeeklyMenuItemSalesForecast(wmsfPairedList);
            errorMessage = "";
            successMessage = "Changes saved successfully";
        } catch (InvalidWmsfException ex) {
            successMessage = "";
            errorMessage = ex.getMessage();
        }
    }

    public void resetWmsf(AjaxBehaviorEvent event) {
        foodForecastBean.resetWmsfList(wmsfPairedList);
        errorMessage = "";
        successMessage = "Default optimal figures loaded successfully";
    }

    public String datePeriod(int weekNo) {
        String formatted = "";
        Calendar start = Helper.getStartDateOfWeek(month.value, year, weekNo);
        formatted += start.get(Calendar.DAY_OF_MONTH) + "/" + (start.get(Calendar.MONTH) + 1) + "/" + start.get(Calendar.YEAR);
        formatted += " - ";
        start.add(Calendar.DAY_OF_MONTH, 7);
        formatted += start.get(Calendar.DAY_OF_MONTH) + "/" + (start.get(Calendar.MONTH) + 1) + "/" + start.get(Calendar.YEAR);

        return formatted;
    }

    public void orderIngred() {
        try {
            foodForecastBean.saveWeeklyMenuItemSalesForecast(wmsfPairedList);
            foodForecastBean.orderIngredients(this.wmsfPairedList, this.store, this.month, this.year, this.notNullWmsfList.size());
            errorMessage = "";
            successMessage = "Ingredients ordered successfully";
        } catch (InvalidWmsfException ex) {
            successMessage = "";
            errorMessage = ex.getMessage();
        }
    }

    /**
     * Creates a new instance of IngredPlannerWeekManagedBean
     */
    public IngredPlannerWeekManagedBean() {
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

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public List<Couple<MenuItem, List<WeeklyMenuItemSalesForecast>>> getWmsfPairedList() {
        return wmsfPairedList;
    }

    public void setWmsfPairedList(List<Couple<MenuItem, List<WeeklyMenuItemSalesForecast>>> wmsfPairedList) {
        this.wmsfPairedList = wmsfPairedList;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public List<WeeklyMenuItemSalesForecast> getNotNullWmsfList() {
        return notNullWmsfList;
    }

    public void setNotNullWmsfList(List<WeeklyMenuItemSalesForecast> notNullWmsfList) {
        this.notNullWmsfList = notNullWmsfList;
    }

}
