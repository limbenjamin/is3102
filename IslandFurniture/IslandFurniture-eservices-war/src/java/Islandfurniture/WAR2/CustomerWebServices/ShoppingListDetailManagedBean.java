/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Islandfurniture.WAR2.CustomerWebServices;

import IslandFurniture.EJB.CustomerWebService.ManageLocalizationBeanLocal;
import IslandFurniture.EJB.CustomerWebService.ManageMemberAuthenticationBeanLocal;
import IslandFurniture.EJB.CustomerWebService.ManageShoppingListBeanLocal;
import IslandFurniture.EJB.InventoryManagement.ManageStorefrontInventoryLocal;
import IslandFurniture.EJB.OperationalCRM.ManageMarketingBeanLocal;
import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.Customer;
import IslandFurniture.Entities.FurnitureModel;
import IslandFurniture.Entities.ShoppingList;
import IslandFurniture.Entities.ShoppingListDetail;
import IslandFurniture.Entities.Stock;
import IslandFurniture.Entities.Store;
import IslandFurniture.Entities.StorefrontInventory;
import IslandFurniture.StaticClasses.SendEmailByPost;
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
    private Double subtotal;
    
    @EJB
    private ManageLocalizationBeanLocal manageLocalizationBean;
    @EJB
    private ManageMemberAuthenticationBeanLocal mmab;    
    @EJB
    private ManageShoppingListBeanLocal mslbl;
    @EJB
    private ManageMarketingBeanLocal mmbl;
    @EJB
    private ManageStorefrontInventoryLocal inventoryBean;
    
    @PostConstruct
    public void init(){
        HttpSession session = Util.getSession();
        emailAddress = (String) session.getAttribute("emailAddress");
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest httpReq = (HttpServletRequest) ec.getRequest();
        countryOffice = manageLocalizationBean.findCoByCode((String) httpReq.getAttribute("coCode"));
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
            customer = mmab.getCustomer(emailAddress);
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
                if (shoppingList.getStore().getCountryOffice().equals(countryOffice)) {
                    // update total price of the list
                    mslbl.updateListTotalPrice(listId, customer);
                    shoppingListDetails = mslbl.getShoppingListDetails(listId);
                }
                else {
                    shoppingList = new ShoppingList();
                }
            } catch (Exception e){
            
            }
        }
    }
    
    public void editListStore() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        mslbl.updateShoppingList(listId, shoppingList.getStore(), shoppingList.getName());
        shoppingList = mslbl.getShoppingList(listId);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Your list has been sucessfully updated", ""));
        ec.redirect(ec.getRequestContextPath() + coDir + "/member/shoppinglistdetail.xhtml?id=" + listId);
    }
    
    public void editDetail(ActionEvent event) throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ShoppingListDetail detail = (ShoppingListDetail) event.getComponent().getAttributes().get("detailid");
        mslbl.updateShoppingListDetail(detail);
        mslbl.updateListTotalPrice(listId, customer);
        shoppingListDetails = mslbl.getShoppingListDetails(listId);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Update Successful!", ""));
        ec.redirect(ec.getRequestContextPath() + coDir + "/member/shoppinglistdetail.xhtml?id=" + listId);
    }
    
    public void deleteDetail(ActionEvent event) throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        Long detailId = Long.parseLong(ec.getRequestParameterMap().get("detailid"));
        ShoppingListDetail detail = mslbl.getShoppingListDetail(detailId);
        String furnitureName = detail.getFurnitureModel().getName();
        mslbl.deleteShoppingListDetail(detailId);
        mslbl.updateListTotalPrice(listId, customer);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, furnitureName + " has been sucessfully removed", ""));
        shoppingListDetails = mslbl.getShoppingListDetails(listId);
        ec.redirect(ec.getRequestContextPath() + coDir + "/member/shoppinglistdetail.xhtml?id=" + listId);
    }
    
    public String getLocation(Long stockId) {
        StorefrontInventory inventory = inventoryBean.getStorefrontInventory(shoppingList.getStore(), stockId);
        return inventory.getLocationInStore().getName();
    }
    
    public String getStockAvailability (FurnitureModel furniture) {
        return inventoryBean.viewStorefrontInventoryStockLevelPerPlant(shoppingList.getStore(), furniture);
    }
    
    public Double calculateSubTotal() {
        subtotal = 0.0;
        Iterator<ShoppingListDetail> iterator = shoppingListDetails.iterator();
        while (iterator.hasNext()) {
            ShoppingListDetail current = iterator.next();
            Double discountedPrice = getDiscountedPrice(current.getFurnitureModel());
            subtotal = subtotal + discountedPrice * current.getQty();
        }
        return subtotal;
    }
    
    public void shareList() throws Exception {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) ec.getRequest();
        String recipientEmail = request.getParameter("shareShoppingList:recipientEmail");
        SendEmailByPost.sendEmail("customerservice", recipientEmail, "Share Shopping List", customer.getName()+ 
                " has requested to share the shopping list with you. Click here to accept: https://localhost/cws/sg/member/shoppinglistinvite.xhtml?id=" + shoppingList.getIdHash());
        ec.redirect(ec.getRequestContextPath() + coDir + "/member/shoppinglistdetail.xhtml?id=" + listId);
    }
    
    public Double getDiscountedPrice(Stock s) {
        return (Double)mmbl.getDiscountedPrice(s, shoppingList.getStore(), customer).get("D_PRICE");
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

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }
    
}
