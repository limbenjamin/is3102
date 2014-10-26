/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Islandfurniture.WAR2.CustomerWebServices;

import IslandFurniture.EJB.CustomerWebService.ManageLocalizationBeanLocal;
import IslandFurniture.EJB.CustomerWebService.ManageShoppingListBeanLocal;
import IslandFurniture.EJB.OperationalCRM.ManageMarketingBeanLocal;
import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.Customer;
import IslandFurniture.Entities.ShoppingList;
import IslandFurniture.Entities.ShoppingListDetail;
import IslandFurniture.Entities.Stock;
import IslandFurniture.Entities.Store;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
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
public class ShoppingListManagedBean {

    private String emailAddress = null;
    private Customer customer;
    private List<ShoppingList> shoppingLists;
    private List<Store> localStores;
    private String coDir;
    private CountryOffice countryOffice;
    
    @EJB
    private ManageLocalizationBeanLocal manageLocalizationBean;    
    @EJB
    private ManageShoppingListBeanLocal mslbl;
    @EJB
    private ManageMarketingBeanLocal mmbl;    
    
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
            customer = mslbl.getCustomer(emailAddress);
            shoppingLists = customer.getShoppingLists();
            // update total price of each list
            for (ShoppingList list : shoppingLists) {
                mslbl.updateListTotalPrice(list.getId());
            }
            countryOffice = manageLocalizationBean.findCoByCode((String) httpReq.getAttribute("coCode"));
            localStores = countryOffice.getStores();
        }
    }
    
    public void newShoppingList() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();        
        Long storeId = Long.parseLong(request.getParameter("newShoppingList:storeId"));
        String name = request.getParameter("newShoppingList:listName");
        ShoppingList newList = mslbl.createShoppingList(emailAddress, storeId, name);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
        new FacesMessage(FacesMessage.SEVERITY_INFO, name + " has been sucessfully created", ""));
        ec.redirect(ec.getRequestContextPath() + coDir + "/member/shoppinglist.xhtml");
    }
    
    public void deleteShoppingList(ActionEvent event) throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
        mslbl.deleteShoppingList(new Long(ec.getRequestParameterMap().get("listid")), emailAddress);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Your shopping list has been sucessfully removed", ""));
        } catch(NumberFormatException ex) {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "cannot get list id", "")); 
        } finally {
            shoppingLists = customer.getShoppingLists();
            ec.redirect(ec.getRequestContextPath() + coDir + "/member/shoppinglist.xhtml");
        }
    }    
    
    public Double calculateSubTotal(ShoppingList list) {
        Double subtotal = 0.0;
        Iterator<ShoppingListDetail> iterator = list.getShoppingListDetails().iterator();
        while (iterator.hasNext()) {
            ShoppingListDetail current = iterator.next();
            Double discountedPrice = getDiscountedPrice(current.getFurnitureModel());
            subtotal = subtotal + discountedPrice * current.getQty();
        }        
        return subtotal;
    }  
    
    public Double getDiscountedPrice(Stock s) {
        Store st = new Store();
        st.setCountryOffice(countryOffice);
        return (Double)mmbl.getDiscountedPrice(s, st, new Customer()).get("D_PRICE");
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

    public List<ShoppingList> getShoppingLists() {
        return shoppingLists;
    }

    public void setShoppingLists(List<ShoppingList> shoppingLists) {
        this.shoppingLists = shoppingLists;
    }

    public String getCoDir() {
        return coDir;
    }

    public void setCoDir(String coDir) {
        this.coDir = coDir;
    }

    public ManageShoppingListBeanLocal getMslbl() {
        return mslbl;
    }

    public void setMslbl(ManageShoppingListBeanLocal mslbl) {
        this.mslbl = mslbl;
    }

    public List<Store> getLocalStores() {
        return localStores;
    }

    public void setLocalStores(List<Store> localStores) {
        this.localStores = localStores;
    }

    public CountryOffice getCountryOffice() {
        return countryOffice;
    }

    public void setCountryOffice(CountryOffice countryOffice) {
        this.countryOffice = countryOffice;
    }

    public ManageLocalizationBeanLocal getManageLocalizationBean() {
        return manageLocalizationBean;
    }

    public void setManageLocalizationBean(ManageLocalizationBeanLocal manageLocalizationBean) {
        this.manageLocalizationBean = manageLocalizationBean;
    }
    
}
