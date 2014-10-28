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
import IslandFurniture.Entities.FurnitureModel;
import IslandFurniture.Entities.ShoppingList;
import IslandFurniture.Entities.Stock;
import IslandFurniture.Entities.Store;
import IslandFurniture.Exceptions.DuplicateEntryException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Zee
 */
@ManagedBean
@ViewScoped
public class ProductDetailManagedBean {

    private Long id = null;
    private Stock stock = null;
    private FurnitureModel furniture = null;
    private List<Store> localStores = null;
    private String emailAddress;
    private Customer customer;
    private List<ShoppingList> shoppingLists;
    private boolean loggedIn = false;
    private String coDir;
    private CountryOffice co;
    private Double discountedPrice;

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    
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
        
        furniture = mcbl.getFurnitureModel(id);
        discountedPrice = getDiscountedPrice(furniture);
        localStores = co.getStores();
        loggedIn = checkLoggedIn();
        
        if (loggedIn) {
            customer = mmab.getCustomer(emailAddress);
            shoppingLists = new ArrayList<>();
            List<ShoppingList> allLists = customer.getShoppingLists();
            // show only lists specific to the site's country in the site
            for (ShoppingList list : allLists) {
                if (list.getStore().getCountryOffice().equals(co)) {
                    shoppingLists.add(list);
                }
            }
        }
        
        System.out.println("Got furniture model " + furniture.getName());
    }    
    
    public boolean checkLoggedIn() {
        HttpSession session = Util.getSession();
        emailAddress = (String) session.getAttribute("emailAddress");  
        if (emailAddress == null)
            return false;
        else 
            return true;
    }
    
    public Double getDiscountedPrice(Stock s) {
        Store st = new Store();
        st.setCountryOffice(co);
        return (Double)mmbl.getDiscountedPrice(s, st, new Customer()).get("D_PRICE");
    }    
    
    public void addItemToShoppingList() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();        
        Long listId = Long.parseLong(request.getParameter("addItemToList:listId"));
        Integer quantity = Integer.parseInt(request.getParameter("addItemToList:quantity"));
        try {
            mslbl.createShoppingListDetail(listId, id, quantity, discountedPrice);
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
            new FacesMessage(FacesMessage.SEVERITY_INFO, furniture.getName() + " has been sucessfully added", ""));
            
        } catch (DuplicateEntryException ex) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, ex.getMessage(), "this item was already added previously"));
        } finally {
            mslbl.updateListTotalPrice(listId);
            ec.redirect(ec.getRequestContextPath() + "/" + coDir + "/member/shoppinglistdetail.xhtml?id=" + listId);
        }
    }
    
    public String getStockAvailability (Store store) {
        return inventoryBean.viewStorefrontInventoryStockLevelPerPlant(store, furniture);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CountryOffice getCo() {
        return co;
    }

    public void setCo(CountryOffice co) {
        this.co = co;
    }

    public FurnitureModel getFurniture() {
        return furniture;
    }

    public void setFurniture(FurnitureModel furniture) {
        this.furniture = furniture;
    }

    public ManageLocalizationBeanLocal getManageLocalizationBean() {
        return manageLocalizationBean;
    }

    public void setManageLocalizationBean(ManageLocalizationBeanLocal manageLocalizationBean) {
        this.manageLocalizationBean = manageLocalizationBean;
    }

    public ManageCatalogueBeanLocal getMcbl() {
        return mcbl;
    }

    public void setMcbl(ManageCatalogueBeanLocal mcbl) {
        this.mcbl = mcbl;
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

    public List<ShoppingList> getShoppingLists() {
        return shoppingLists;
    }

    public void setShoppingLists(List<ShoppingList> shoppingLists) {
        this.shoppingLists = shoppingLists;
    }

    public ManageMemberAuthenticationBeanLocal getMmab() {
        return mmab;
    }

    public void setMmab(ManageMemberAuthenticationBeanLocal mmab) {
        this.mmab = mmab;
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

    public Double getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(Double discountedPrice) {
        this.discountedPrice = discountedPrice;
    }
    
}
