/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Islandfurniture.WAR2.CustomerWebServices;

import IslandFurniture.EJB.CustomerWebService.ManageLocalizationBeanLocal;
import IslandFurniture.EJB.ITManagement.ManageOrganizationalHierarchyBeanLocal;
import IslandFurniture.Entities.CountryOffice;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.inject.Named;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Zee
 */
@ManagedBean
@ViewScoped
public class TemplateManagedBean {

    private FacesContext ctx = FacesContext.getCurrentInstance(); 
    private List<CountryOffice> coList;
    private String selectedLocale;
    private CountryOffice co;
    
    @EJB
    private ManageLocalizationBeanLocal manageLocalizationBean;
    
    @PostConstruct
    public void init() {
        HttpServletRequest httpReq = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        co = manageLocalizationBean.findCoByCode((String) httpReq.getAttribute("coCode"));
        setCountryLocale((String) httpReq.getAttribute("coCode"));         
    } 
    
    public void setCountryLocale(String coCode) {
        if (coCode.equals("cn")) {
            ctx.getViewRoot().setLocale(new Locale("zh", "CN"));
        }    
        else if (coCode.equals("my")) {
            ctx.getViewRoot().setLocale(new Locale("ms", "MY"));
        }
        else
            ctx.getViewRoot().setLocale(new Locale("en", "SG"));
    }
    
    public String getLocalizedCurrencyFormat() {
        return NumberFormat.getCurrencyInstance(ctx.getViewRoot().getLocale()).format(1000);     
    }     

    public List<CountryOffice> getCoList() {
        return coList;
    }

    public void setCoList(List<CountryOffice> coList) {
        this.coList = coList;
    }

    public String getSelectedLocale() {
        return selectedLocale;
    }

    public void setSelectedLocale(String selectedLocale) {
        this.selectedLocale = selectedLocale;
    }

    public CountryOffice getCo() {
        return co;
    }

    public void setCo(CountryOffice co) {
        this.co = co;
    }
    
}
