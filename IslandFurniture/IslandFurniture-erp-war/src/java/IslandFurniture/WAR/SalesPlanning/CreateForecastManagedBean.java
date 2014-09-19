/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.SalesPlanning;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.EJB.Entities.CountryOffice;
import IslandFurniture.EJB.Entities.Month;
import IslandFurniture.EJB.Entities.MonthlyStockSupplyReq;
import IslandFurniture.EJB.Entities.Plant;
import IslandFurniture.EJB.Entities.Staff;
import IslandFurniture.EJB.Entities.Stock;
import IslandFurniture.EJB.SalesPlanning.SalesForecastBeanLocal;
import IslandFurniture.StaticClasses.Helper.Couple;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.io.IOException;
import java.io.Serializable;
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
public class CreateForecastManagedBean implements Serializable {

    @EJB
    private SalesForecastBeanLocal salesForecastBean;

    @EJB
    private ManageUserAccountBeanLocal staffBean;

    private Staff staff;
    private CountryOffice co;

    private int endYear;
    private List<Integer> endYears;

    private int endMonth;
    private List<Map.Entry<String, Integer>> endMonths;

    String panelActive = "0";
    String statusMessage = "";

    private List<Couple<String, String>> mssrLabels = new ArrayList();
    private List<Couple<Stock, Couple<List<MonthlyStockSupplyReq>, List<MonthlyStockSupplyReq>>>> mssrPairedList;

    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        this.staff = staffBean.getStaff((String) session.getAttribute("username"));
        
        Plant plant = staff.getPlant();

        if (plant instanceof CountryOffice) {
            // Populate MSSR labels and formatted labels
            this.mssrLabels = MonthlyStockSupplyReq.getLabels();
            
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
        Calendar start = Calendar.getInstance();
        start.set(Calendar.DAY_OF_MONTH, 1);
        start.add(Calendar.MONTH, 2);

        Calendar end = Calendar.getInstance();
        end.set(endYear, endMonth, 2);

        for (Couple<Stock, Couple<List<MonthlyStockSupplyReq>, List<MonthlyStockSupplyReq>>> eachPair : this.mssrPairedList) {
            eachPair.getSecond().getSecond().clear();
        }

        while (start.compareTo(end) <= 0) {
            for (Couple<Stock, Couple<List<MonthlyStockSupplyReq>, List<MonthlyStockSupplyReq>>> eachPair : this.mssrPairedList) {
                MonthlyStockSupplyReq mssr = new MonthlyStockSupplyReq();
                mssr.setCountryOffice(co);
                mssr.setStock(eachPair.getFirst());
                mssr.setMonth(Month.getMonth(start.get(Calendar.MONTH)));
                mssr.setYear(start.get(Calendar.YEAR));

                eachPair.getSecond().getSecond().add(mssr);
            }

            start.add(Calendar.MONTH, 1);
        }
    }

    public void loadPrevMssr() {
        Calendar sixMthsBefore = Calendar.getInstance();
        sixMthsBefore.add(Calendar.MONTH, -6);

        Calendar thisMth = Calendar.getInstance();

        List<Couple<Stock, List<MonthlyStockSupplyReq>>> stockMssrList = salesForecastBean.retrieveMssrForCo(this.co, Month.getMonth(sixMthsBefore.get(Calendar.MONTH)), sixMthsBefore.get(Calendar.YEAR), Month.getMonth(thisMth.get(Calendar.MONTH)), thisMth.get(Calendar.YEAR));

        if (stockMssrList != null) {
            this.mssrPairedList = new ArrayList();

            for (Couple<Stock, List<MonthlyStockSupplyReq>> stockMssr : stockMssrList) {
                Couple<List<MonthlyStockSupplyReq>, List<MonthlyStockSupplyReq>> pair;
                pair = new Couple(stockMssr.getSecond(), new ArrayList());
                this.mssrPairedList.add(new Couple(stockMssr.getFirst(), pair));
            }
        }

        for (int i = 1; i < this.mssrPairedList.size(); i++) {
            this.panelActive += "," + i;
        }
    }

    public void saveForecast(AjaxBehaviorEvent event) {
        System.out.println("Ajax call");
        
        try {
            for (Couple<Stock, Couple<List<MonthlyStockSupplyReq>, List<MonthlyStockSupplyReq>>> mssrEntry : this.mssrPairedList) {
                System.out.println(mssrEntry.getSecond().getSecond().get(0).getQtyForecasted());

                salesForecastBean.saveMonthlyStockSupplyReq(mssrEntry.getSecond().getSecond());
            }

            statusMessage = "Saved Successfully!";
        } catch (Exception ex) {
            statusMessage = "Error saving forecast";
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
