/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.SalesPlanning;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountInformationBean;
import IslandFurniture.EJB.Entities.CountryOffice;
import IslandFurniture.EJB.Entities.Month;
import IslandFurniture.EJB.Entities.MonthlyStockSupplyReq;
import IslandFurniture.EJB.Entities.Plant;
import IslandFurniture.EJB.Entities.Staff;
import IslandFurniture.EJB.Entities.Stock;
import IslandFurniture.EJB.SalesPlanning.SalesForecastBeanLocal;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
public class CreateForecastManagedBean {

    @EJB
    private SalesForecastBeanLocal salesForecastBean;

    @EJB
    private ManageUserAccountInformationBean staffBean;

    private Staff staff;
    private CountryOffice co;

    private int endYear;
    private List<Integer> endYears;

    private int endMonth;
    private List<Map.Entry<String, Integer>> endMonths;

    String panelActive = "0";

    private Map<Stock, List<MonthlyStockSupplyReq>> mssrMap;
    private List<Map.Entry<Stock, List<MonthlyStockSupplyReq>>> mssrList;
    
    

    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        this.staff = staffBean.getStaff((String) session.getAttribute("username"));

        Plant plant = staff.getPlant();

        if (plant instanceof CountryOffice) {
            this.co = (CountryOffice) plant;
            Calendar cal = Calendar.getInstance();

            // Because you can only plan for minimum 2 months ahead
            cal.add(Calendar.MONTH, 2);

            // Populate years
            endYears = new ArrayList();
            for (int i = cal.get(Calendar.YEAR); i <= cal.get(Calendar.YEAR) + 1; i++) {
                endYears.add(i);
            }
            
            this.loadPrevMssr();
        } else {
            try {
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect(ec.getRequestContextPath());
            } catch (IOException ex) {

            }
        }

    }

    public void updateMonths(AjaxBehaviorEvent event) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 2);
        
        endMonth = -1;

        endMonths = new ArrayList();

        if (endYear != cal.get(Calendar.YEAR)) {
            cal.set(2000, 0, 1);
        }

        for (int i = cal.get(Calendar.MONTH); i < 12; i++) {
            Map.Entry<String, Integer> item = new AbstractMap.SimpleEntry(cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.ENGLISH), i);
            endMonths.add(item);

            cal.add(Calendar.MONTH, 1);
        }
    }

    public void updateMssr(AjaxBehaviorEvent event) {
        System.err.println("UPDATE MSSR");
    }

    public void loadPrevMssr() {
        Calendar sixMthsBefore = Calendar.getInstance();
        sixMthsBefore.add(Calendar.MONTH, -6);

        Calendar prevMth = Calendar.getInstance();
        prevMth.add(Calendar.MONTH, -1);

        this.mssrMap = salesForecastBean.retrieveMssrForCo(this.co, Month.getMonth(sixMthsBefore.get(Calendar.MONTH)), sixMthsBefore.get(Calendar.YEAR), Month.getMonth(prevMth.get(Calendar.MONTH)), prevMth.get(Calendar.YEAR));

        this.mssrList = new ArrayList();

        if (this.mssrMap != null) {
            this.mssrList.addAll(this.mssrMap.entrySet());
        }
        
        for(int i=1;i<this.mssrList.size();i++){
            this.panelActive += "," + i;
        }
    }

    /**
     * Creates a new instance of CreateForecastManagedBean
     */
    public CreateForecastManagedBean() {
    }

    public int getEndYear() {
        return endYear;
    }

    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }

    public List<Integer> getEndYears() {
        return endYears;
    }

    public void setEndYears(List<Integer> endYears) {
        this.endYears = endYears;
    }

    public int getEndMonth() {
        return endMonth;
    }

    public void setEndMonth(int endMonth) {
        this.endMonth = endMonth;
    }

    public List<Map.Entry<String, Integer>> getEndMonths() {
        return endMonths;
    }

    public void setEndMonths(List<Map.Entry<String, Integer>> endMonths) {
        this.endMonths = endMonths;
    }

    public String getPanelActive() {
        return panelActive;
    }

    public void setPanelActive(String panelActive) {
        this.panelActive = panelActive;
    }

    public List<Map.Entry<Stock, List<MonthlyStockSupplyReq>>> getMssrList() {
        return mssrList;
    }

    public void setMssrList(List<Map.Entry<Stock, List<MonthlyStockSupplyReq>>> mssrList) {
        this.mssrList = mssrList;
    }

}
