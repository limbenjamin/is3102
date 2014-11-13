/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Islandfurniture.WAR2.CustomerWebServices;

import IslandFurniture.EJB.CustomerWebService.ManageLocalizationBeanLocal;
import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Enums.FurnitureSubcategory;
import java.io.IOException;
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Zee
 */
@ManagedBean
@ViewScoped
public class TemplateManagedBean implements Serializable {

    private FacesContext ctx = FacesContext.getCurrentInstance();
    private List<CountryOffice> coList;
    private String selectedLocale;
    private CountryOffice co;
    private List<FurnitureSubcategory> subcategoryList;

    @EJB
    private ManageLocalizationBeanLocal manageLocalizationBean;

    @PostConstruct
    public void init() {
        HttpServletRequest httpReq = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();

        co = manageLocalizationBean.findCoByCode((String) httpReq.getAttribute("coCode"));
        if (co != null) {
            setCountryLocale((String) httpReq.getAttribute("coCode"));
            // get furniture subcategory enum list
            subcategoryList = new ArrayList<>(EnumSet.allOf(FurnitureSubcategory.class));
        } else {
            try{
                ec.redirect(ec.getRequestContextPath());
            } catch (IOException ex) {
                
            }
        }
    }

    public void setCountryLocale(String coCode) {
        selectedLocale = coCode;
        if (coCode.equals("cn")) {
            ctx.getViewRoot().setLocale(new Locale("zh", "CN"));
        } else if (coCode.equals("my")) {
            ctx.getViewRoot().setLocale(new Locale("ms", "MY"));
        } else {
            ctx.getViewRoot().setLocale(new Locale("en", "SG"));
        }
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

    public List<FurnitureSubcategory> getSubcategoryList() {
        return subcategoryList;
    }

    public void setSubcategoryList(List<FurnitureSubcategory> subcategoryList) {
        this.subcategoryList = subcategoryList;
    }

}
