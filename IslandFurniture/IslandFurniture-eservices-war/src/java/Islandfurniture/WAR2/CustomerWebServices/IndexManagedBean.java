/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Islandfurniture.WAR2.CustomerWebServices;

import IslandFurniture.EJB.ITManagement.ManageOrganizationalHierarchyBeanLocal;
import IslandFurniture.Entities.CountryOffice;
import java.io.IOException;
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@ManagedBean
@ViewScoped
public class IndexManagedBean implements Serializable {

    @EJB
    private ManageOrganizationalHierarchyBeanLocal manageOrganizationalHierarchyBean;
    private FacesContext ctx = FacesContext.getCurrentInstance(); 
    private List<CountryOffice> coList;
    private String selectedLocale;

    @PostConstruct
    public void init() {
        this.coList = manageOrganizationalHierarchyBean.displayCountryOffice();
         ctx.getViewRoot().setLocale(new Locale("zh", "CN"));
    }

    public void fowardToLocalizedPage(ActionEvent event) throws IOException {
        String coCode = event.getComponent().getAttributes().get("coCode").toString();
        if (coCode.equals("cn")) {
            ctx.getViewRoot().setLocale(new Locale("zh", "CN"));
            System.out.println("co code is " + coCode);
        }    
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        System.out.println("Redirecting to localised page: " + ec.getRequestContextPath() + "/" + coCode + "/home.xhtml");
        ec.redirect(ec.getRequestContextPath() + "/" + coCode + "/home.xhtml");
    }
    
    /**public void selectedLocaleValueChangeListener() {
        if(selectedLocale.equals("en_US")) ctx.getViewRoot().setLocale(new Locale("en", "US"));
        else if(selectedLocale.equals("en_SG")) ctx.getViewRoot().setLocale(new Locale("en", "SG"));
        else if(selectedLocale.equals("zh_CN")) ctx.getViewRoot().setLocale(new Locale("zh", "CN"));
        else if(selectedLocale.equals("ms_MY")) ctx.getViewRoot().setLocale(new Locale("ms", "MY"));
    }    

    public String getLocalizedCurrencyFormat() {
        return NumberFormat.getCurrencyInstance(ctx.getViewRoot().getLocale()).format(1000);     
    }**/    
    /**
     * Creates a new instance of IndexManagedBean
     */
    public IndexManagedBean() {
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

}
