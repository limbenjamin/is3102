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
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

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
    private CountryOffice co;
    private List<String> currencyList;

    public String getCurrencyParser() {
        return currencyParser;
    }

    public void setCurrencyParser(String currencyParser) {
        this.currencyParser = currencyParser;
    }

    public CountryOffice getCo() {
        return co;
    }

    public void setCo(CountryOffice co) {
        this.co = co;
    }
     
    @PostConstruct
    public void init() { 
        System.out.println("init:CurrencyManagedBean");
        currencyList = currencyManager.getAllCurrency();
        
    }
    public void addCurrency() {
        System.out.println("CurrencyManagedBean.addCurrency()");
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String countryID = request.getParameter("addCurrencyForm:countryID");
        String currencyCode = request.getParameter("addCurrencyForm:currencyCode");
    }
}
