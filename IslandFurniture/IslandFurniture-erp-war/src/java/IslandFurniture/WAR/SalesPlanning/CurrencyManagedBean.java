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
        createLineModels();
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
 
        model.addSeries(series1);
         
        return model;
    }
}
