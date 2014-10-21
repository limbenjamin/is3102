/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Islandfurniture.WAR2.CustomerWebServices;

import IslandFurniture.EJB.CustomerWebService.ManageLocalizationBeanLocal;
import IslandFurniture.EJB.CustomerWebService.ManageShoppingListBeanLocal;
import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.Customer;
import IslandFurniture.Entities.ShoppingList;
import IslandFurniture.Entities.ShoppingListDetail;
import IslandFurniture.Entities.Store;
import java.io.IOException;
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
public class ShoppingListDetailManagedBean {
    
    private Long listId;
    private Customer customer;    
    private ShoppingList shoppingList;    
    private String emailAddress = null;
    private List<ShoppingListDetail> shoppingListDetails;
    private String coDir;
    private CountryOffice countryOffice;    
    private List<Store> localStores;
    
    @EJB
    private ManageLocalizationBeanLocal manageLocalizationBean;    
    @EJB
    private ManageShoppingListBeanLocal mslbl;
    
    @PostConstruct
    public void init(){
        HttpSession session = Util.getSession();
        emailAddress = (String) session.getAttribute("emailAddress");
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest httpReq = (HttpServletRequest) ec.getRequest();
        coDir = (String) httpReq.getAttribute("coCode");
        if(coDir !=null && !coDir.isEmpty()){
            coDir = "/"+ coDir;
        }
        if (emailAddress == null) {
            try {
                ec.redirect(ec.getRequestContextPath() + "/login.xhtml");
            } catch (IOException ex) {
                
            }
        }   
        else {
            // get list id from url
            try {
                listId = new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
                if (listId != null) {
                    System.out.println("list id is " + listId);
                    session.setAttribute("id", listId);
                }
                else {
                    listId = (Long) session.getAttribute("id");
                }
                shoppingList = mslbl.getShoppingList(listId);
                shoppingListDetails = mslbl.getShoppingListDetails(listId);
            } catch (Exception e){
            
            }
        }
    }    

    public Long getListId() {
        return listId;
    }

    public void setListId(Long listId) {
        this.listId = listId;
    }

    public ShoppingList getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(ShoppingList shoppingList) {
        this.shoppingList = shoppingList;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public List<ShoppingListDetail> getShoppingListDetails() {
        return shoppingListDetails;
    }

    public void setShoppingListDetails(List<ShoppingListDetail> shoppingListDetails) {
        this.shoppingListDetails = shoppingListDetails;
    }

    public String getCoDir() {
        return coDir;
    }

    public void setCoDir(String coDir) {
        this.coDir = coDir;
    }

    public CountryOffice getCountryOffice() {
        return countryOffice;
    }

    public void setCountryOffice(CountryOffice countryOffice) {
        this.countryOffice = countryOffice;
    }

    public List<Store> getLocalStores() {
        return localStores;
    }

    public void setLocalStores(List<Store> localStores) {
        this.localStores = localStores;
    }

    public ManageLocalizationBeanLocal getManageLocalizationBean() {
        return manageLocalizationBean;
    }

    public void setManageLocalizationBean(ManageLocalizationBeanLocal manageLocalizationBean) {
        this.manageLocalizationBean = manageLocalizationBean;
    }

    public ManageShoppingListBeanLocal getMslbl() {
        return mslbl;
    }

    public void setMslbl(ManageShoppingListBeanLocal mslbl) {
        this.mslbl = mslbl;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
}
