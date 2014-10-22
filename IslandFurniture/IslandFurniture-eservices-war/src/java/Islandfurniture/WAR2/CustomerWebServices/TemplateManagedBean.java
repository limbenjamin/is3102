/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Islandfurniture.WAR2.CustomerWebServices;

import IslandFurniture.EJB.CustomerWebService.ManageLocalizationBeanLocal;
import IslandFurniture.EJB.ITManagement.ManageOrganizationalHierarchyBeanLocal;
import IslandFurniture.Entities.CountryOffice;
import java.util.List;
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

    @EJB
    private ManageOrganizationalHierarchyBeanLocal manageOrganizationalHierarchyBean;
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
        
    }    
    
}
