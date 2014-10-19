/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Islandfurniture.WAR2.CustomerWebServices;

import IslandFurniture.EJB.ITManagement.ManageOrganizationalHierarchyBeanLocal;
import IslandFurniture.Entities.CountryOffice;
import javax.faces.event.ActionEvent;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
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

    private List<CountryOffice> coList;

    @PostConstruct
    public void init() {
        this.coList = manageOrganizationalHierarchyBean.displayCountryOffice();
    }

    public void fowardToLocalizedPage(ActionEvent event) throws IOException {
        String coCode = event.getComponent().getAttributes().get("coCode").toString();
        
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        System.out.println("Redirecting to localised page: " + ec.getRequestContextPath() + "/" + coCode + "/home.xhtml");
        ec.redirect(ec.getRequestContextPath() + "/" + coCode + "/home.xhtml");
    }

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

}
