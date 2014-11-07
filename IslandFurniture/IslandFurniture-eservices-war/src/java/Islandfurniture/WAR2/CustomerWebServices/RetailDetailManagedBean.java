/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Islandfurniture.WAR2.CustomerWebServices;

import IslandFurniture.EJB.CustomerWebService.ManageCatalogueBeanLocal;
import IslandFurniture.EJB.CustomerWebService.ManageLocalizationBeanLocal;
import IslandFurniture.EJB.CustomerWebService.ManageMemberAuthenticationBeanLocal;
import IslandFurniture.EJB.CustomerWebService.ManageShoppingListBeanLocal;
import IslandFurniture.EJB.InventoryManagement.ManageStorefrontInventoryLocal;
import IslandFurniture.EJB.OperationalCRM.ManageMarketingBeanLocal;
import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.Customer;
import IslandFurniture.Entities.RetailItem;
import IslandFurniture.Entities.ShoppingList;
import IslandFurniture.Entities.Stock;
import IslandFurniture.Entities.Store;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Zee
 */
@ManagedBean
@ViewScoped
public class RetailDetailManagedBean {

    private Long id = null;
    private Stock stock = null;
    private RetailItem retail;
    private List<Store> localStores = null;
    private String emailAddress;
    private Customer customer;
    private boolean loggedIn = false;    
    private String coDir;
    private CountryOffice co;
    private Double discountedPrice;    
    
    @EJB
    private ManageLocalizationBeanLocal manageLocalizationBean;    
    @EJB
    private ManageCatalogueBeanLocal mcbl;
    @EJB
    private ManageMemberAuthenticationBeanLocal mmab;    
    @EJB
    private ManageShoppingListBeanLocal mslbl;    
    @EJB
    private ManageMarketingBeanLocal mmbl;
    @EJB
    private ManageStorefrontInventoryLocal inventoryBean;
    
    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        HttpServletRequest httpReq = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        co = manageLocalizationBean.findCoByCode((String) httpReq.getAttribute("coCode"));
        coDir = (String) httpReq.getAttribute("coCode");
        try{
            id = new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
            if (id != null) {
                System.out.println("id is " + id);
                session.setAttribute("id", id);
            }
            else {
                id = (Long) session.getAttribute("id");
            }
        }catch (Exception e){
            
        }
        localStores = co.getStores();
        loggedIn = checkLoggedIn();        
        if (loggedIn)
            customer = mmab.getCustomer(emailAddress);   
        
        retail = mcbl.getRetailItem(id);
        if (retail != null) {
            discountedPrice = getDiscountedPrice(retail);
            System.out.println("Got retail item " + retail.getName());            
        }
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
    
    public Double getDiscountedPrice(Stock s) {
            Store st = new Store();
            st.setCountryOffice(co);
            if (customer != null)
                return (Double)mmbl.getDiscountedPrice(s, st, customer).get("D_PRICE");
            else
                return (Double)mmbl.getDiscountedPrice(s, st, new Customer()).get("D_PRICE");
    }
    
    public String getStockAvailability (Store store) {
        return inventoryBean.viewStorefrontInventoryStockLevelPerPlant(store, retail);
    }
    
    public void redirectPage() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/" + coDir + "/home.xhtml");        
    }    

    public RetailItem getRetail() {
        return retail;
    }

    public void setRetail(RetailItem retail) {
        this.retail = retail;
    }

    public List<Store> getLocalStores() {
        return localStores;
    }

    public void setLocalStores(List<Store> localStores) {
        this.localStores = localStores;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public Double getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(Double discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public CountryOffice getCo() {
        return co;
    }

    public void setCo(CountryOffice co) {
        this.co = co;
    }
}
