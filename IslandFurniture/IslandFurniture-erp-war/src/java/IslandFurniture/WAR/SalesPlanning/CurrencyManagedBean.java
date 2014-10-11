/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.SalesPlanning;

import IslandFurniture.EJB.ITManagement.ManageOrganizationalHierarchyBeanLocal;
import IslandFurniture.EJB.SalesPlanning.CurrencyManagerLocal;
import IslandFurniture.Entities.Country;
import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.Currency;
import IslandFurniture.Entities.ExchangeRate;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

/**
 *
 * @author a0101774
 */
@ManagedBean
@ViewScoped
public class CurrencyManagedBean implements Serializable {
    @EJB
    private CurrencyManagerLocal currencyManager;
    @EJB
    private ManageOrganizationalHierarchyBeanLocal mohBean;
    
    private String currencyParser;
    private List<Currency> currencyList;
    private List<ExchangeRate> exchangeRates;
    private Currency currency;
    private boolean toDisplay;
    private Long currencyID;
    private LineChartModel lineModel1;
    private LineChartModel lineModel2;
    private Double latestRate;

    public String getCurrencyParser() {
        return currencyParser;
    }

    public void setCurrencyParser(String currencyParser) {
        this.currencyParser = currencyParser;
    }

    public List<Currency> getCurrencyList() {
        return currencyList;
    }

    public void setCurrencyList(List<Currency> currencyList) {
        this.currencyList = currencyList;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public boolean isToDisplay() {
        return toDisplay;
    }

    public void setToDisplay(boolean toDisplay) {
        this.toDisplay = toDisplay;
    }

    public Long getCurrencyID() {
        return currencyID;
    }

    public void setCurrencyID(Long currencyID) {
        this.currencyID = currencyID;
    }

    public List<ExchangeRate> getExchangeRates() {
        return exchangeRates;
    }

    public void setExchangeRates(List<ExchangeRate> exchangeRates) {
        this.exchangeRates = exchangeRates;
    }

    public LineChartModel getLineModel1() {
        System.out.println("Retrieving lineModel");
        return lineModel1;
    } 
    public LineChartModel getLineModel2() {
        return lineModel2;
    }

    public Double getLatestRate() {
        return latestRate;
    }

    public void setLatestRate(Double latestRate) {
        this.latestRate = latestRate; 
    }
     
    @PostConstruct
    public void init() { 
        System.out.println("init:CurrencyManagedBean");
        this.currencyID = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("currencyID");
        currencyList = currencyManager.getAllCurrency(); 
        toDisplay = false;
        if(currencyID != null) {
            this.currency = currencyManager.getCurrency(currencyID);
            this.exchangeRates = currency.getExchangeRates();
            this.toDisplay = true;
            if(exchangeRates.size() > 0)
                this.latestRate = exchangeRates.get(exchangeRates.size()-1).getExchangeRate();
            else this.latestRate = null; 
            createLineModels();
        }
    }
    
    public String displayCurrencyDetails() {
        System.out.println("CurrencyManagedBean.displayCurrencyDetails()");
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        Long currencyID = Long.parseLong(request.getParameter("selectCurrencyForm:currencyID"));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("currencyID", currencyID);
        return "currency.xhtml";
    }
    public String updateExchangeRate() {
        System.out.println("CurrencyManagedBean.updateExchangeRate()");
        Double rate = currencyManager.updateExchangeRate(currency.getCurrencyCode());
        System.out.println("Exchange rate is " + rate);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("currencyID", currencyID);
        return "currency.xhtml"; 
    } 
    private void createLineModels() {
        lineModel1 = initLinearModel();
        lineModel1.setTitle("Linear Chart");
        lineModel1.setLegendPosition("e");
        Axis yAxis = lineModel1.getAxis(AxisType.Y);
        yAxis.setMin(0);
        yAxis.setMax(10);
         
        lineModel2 = initCategoryModel();
        lineModel2.setTitle("Category Chart");
        lineModel2.setLegendPosition("e");
        lineModel2.setShowPointLabels(true);
        lineModel2.getAxes().put(AxisType.X, new CategoryAxis("Years"));
        yAxis = lineModel2.getAxis(AxisType.Y);
        yAxis.setLabel("Births");
        yAxis.setMin(0);
        yAxis.setMax(200); 
    }
     
    private LineChartModel initLinearModel() {
        LineChartModel model = new LineChartModel();
 
        LineChartSeries series1 = new LineChartSeries();
        series1.setLabel("Series 1");
 
        series1.set(1, 2);
        series1.set(2, 1);
        series1.set(3, 3);
        series1.set(4, 6);
        series1.set(5, 8);
 
        LineChartSeries series2 = new LineChartSeries();
        series2.setLabel("Series 2");
 
        series2.set(1, 6);
        series2.set(2, 3);
        series2.set(3, 2);
        series2.set(4, 7);
        series2.set(5, 9);
 
        model.addSeries(series1);
        model.addSeries(series2);
         
        return model;
    }
     
    private LineChartModel initCategoryModel() {
        LineChartModel model = new LineChartModel();
 
        ChartSeries boys = new ChartSeries();
        boys.setLabel("Boys");
        boys.set("2004", 120);
        boys.set("2005", 100);
        boys.set("2006", 44);
        boys.set("2007", 150);
        boys.set("2008", 25);
 
        ChartSeries girls = new ChartSeries();
        girls.setLabel("Girls");
        girls.set("2004", 52);
        girls.set("2005", 60);
        girls.set("2006", 110);
        girls.set("2007", 90);
        girls.set("2008", 120);
 
        model.addSeries(boys);
        model.addSeries(girls);
         
        return model;
    }
}
