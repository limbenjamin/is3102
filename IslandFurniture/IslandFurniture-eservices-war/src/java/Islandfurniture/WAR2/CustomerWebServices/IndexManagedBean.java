/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Islandfurniture.WAR2.CustomerWebServices;

import IslandFurniture.EJB.ITManagement.ManageOrganizationalHierarchyBeanRemote;
import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.Store;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@ManagedBean
@ViewScoped
public class IndexManagedBean implements Serializable {

    @EJB
    private ManageOrganizationalHierarchyBeanRemote manageOrganizationalHierarchyBean;
    private FacesContext ctx = FacesContext.getCurrentInstance(); 
    private List<CountryOffice> coList;
    private List<Store> storeList;
    private String selectedLocale;

    @PostConstruct
    public void init() {
        this.coList = manageOrganizationalHierarchyBean.displayCountryOffice();
        this.storeList = manageOrganizationalHierarchyBean.displayStore();
    }

    public void fowardToLocalizedPage(ActionEvent event) throws IOException {
        String coCode = event.getComponent().getAttributes().get("coCode").toString();
        /**if (coCode.equals("cn")) {
            ctx.getViewRoot().setLocale(new Locale("zh", "CN"));
            System.out.println("co code is " + coCode);
        }**/
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        System.out.println("Redirecting to localised page: " + ec.getRequestContextPath() + "/" + coCode + "/home.xhtml");
        ec.redirect(ec.getRequestContextPath() + "/" + coCode + "/home.xhtml");
    }

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

    public List<Store> getStoreList() {
        return storeList;
    }

    public void setStoreList(List<Store> storeList) {
        this.storeList = storeList;
    }

}
