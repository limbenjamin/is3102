/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Islandfurniture.WAR2.CustomerWebServices;

import IslandFurniture.EJB.CustomerWebService.ManageCatalogueBeanLocal;
import IslandFurniture.EJB.CustomerWebService.ManageLocalizationBeanLocal;
import IslandFurniture.EJB.CustomerWebService.ManageMemberAuthenticationBeanLocal;
import IslandFurniture.EJB.OperationalCRM.ManageMarketingBeanLocal;
import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.Customer;
import IslandFurniture.Entities.FurnitureModel;
import IslandFurniture.Entities.RetailItem;
import IslandFurniture.Entities.Stock;
import IslandFurniture.Entities.Store;
import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Zee
 */
@ManagedBean
@ViewScoped
public class RetailManagedBean {
    
    private CountryOffice co;
    private Customer customer;
    private String emailAddress;
    private  List<RetailItem> retailList;
    
    @EJB
    private ManageLocalizationBeanLocal manageLocalizationBean;
    @EJB
    private ManageCatalogueBeanLocal mcbl;
    @EJB
    private ManageMarketingBeanLocal mmbl;
    @EJB
    private ManageMemberAuthenticationBeanLocal mmab;    
    
    @PostConstruct
    public void init() {
        HttpServletRequest httpReq = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        co = manageLocalizationBean.findCoByCode((String) httpReq.getAttribute("coCode"));
        
        retailList = mcbl.getStoreRetailItems(co);
        
        boolean loggedIn = checkLoggedIn();
        if (loggedIn)
            customer = mmab.getCustomer(emailAddress);
        
        System.out.println("loaded " + co.getName() + " retail items");
    }
    
    public boolean checkLoggedIn() {
        HttpSession session = Util.getSession();  
        if (session == null)
            return false;
        else {
            emailAddress = (String) session.getAttribute("emailAddress");            
            if (emailAddress == null)
                return false;
            else 
                return true;
        }
    }
    
    public void displayRetailDetails(ActionEvent event) throws IOException {
      System.out.println("displayProductDetails()");
      // get country office code
      HttpServletRequest httpReq = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();  
      String coCode = (String) httpReq.getAttribute("coCode");
      
      String retailId = event.getComponent().getAttributes().get("retailId").toString();
      ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
      
      ec.redirect(ec.getRequestContextPath() + "/" + coCode + "/retaildetail.xhtml?id=" + retailId);
    }    
    
    public Double getDiscountedPrice(Stock s) {
        Store st = new Store();
        st.setCountryOffice(co);
        if (customer != null)
            return (Double)mmbl.getDiscountedPrice(s, st, customer).get("D_PRICE");
        else
            return (Double)mmbl.getDiscountedPrice(s, st, new Customer()).get("D_PRICE");
    }    

    public CountryOffice getCo() {
        return co;
    }

    public void setCo(CountryOffice co) {
        this.co = co;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public List<RetailItem> getRetailList() {
        return retailList;
    }

    public void setRetailList(List<RetailItem> retailList) {
        this.retailList = retailList;
    }
    
}
