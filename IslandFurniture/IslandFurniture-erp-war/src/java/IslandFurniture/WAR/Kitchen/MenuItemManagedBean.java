/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.Kitchen;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.EJB.Kitchen.KitchenStockManagerLocal;
import IslandFurniture.EJB.Kitchen.MenuManagerLocal;
import IslandFurniture.EJB.SalesPlanning.CurrencyManagerLocal;
import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.MenuItem;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Staff;
import IslandFurniture.Enums.MenuType;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumSet;
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
 * @author a0101774
 */
@ManagedBean
@ViewScoped
public class MenuItemManagedBean implements Serializable {
    @EJB
    private CurrencyManagerLocal currencyManager;
    @EJB
    private MenuManagerLocal menuManager;
    @EJB
    private ManageUserAccountBeanLocal staffBean;
    @EJB
    private KitchenStockManagerLocal ksm;
    
    private String username;
    private Staff staff;
    private Plant plant;
    private CountryOffice co;
    private List<MenuItem> menuItemList;
    private MenuType menuType;
    private MenuItem menuItem;
    private boolean checkbox;
    private List<MenuType> menuTypeList;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    public CountryOffice getCo() {
        return co;
    }

    public void setCo(CountryOffice co) {
        this.co = co;
    }

    public List<MenuItem> getMenuItemList() {
        return menuItemList;
    }

    public void setMenuItemList(List<MenuItem> menuItemList) {
        this.menuItemList = menuItemList;
    }

    public MenuType getMenuType() {
        return menuType;
    }

    public void setMenuType(MenuType menuType) {
        this.menuType = menuType;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public boolean isCheckbox() {
        return checkbox;
    }

    public void setCheckbox(boolean checkbox) {
        this.checkbox = checkbox;
    }

    public List<MenuType> getMenuTypeList() {
        return menuTypeList;
    }

    public void setMenuTypeList(List<MenuType> menuTypeList) {
        this.menuTypeList = menuTypeList;
    }
     
    @PostConstruct 
    public void init() {
        System.out.println("init:MenuItemManagedBean");
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        staff = staffBean.getStaff(username);
        plant = staff.getPlant();

        if (plant instanceof CountryOffice) { 
            this.co = (CountryOffice) plant;
            this.menuItemList = menuManager.getMenuItemList(co);
            this.checkbox = false;
            this.menuTypeList = new ArrayList<MenuType>(EnumSet.allOf(MenuType.class));
        } else {
            try {
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect(ec.getRequestContextPath());
            } catch (IOException ex) {

            }
        }
    }
    public String addMenuItem() {
        System.out.println("MenuItemManagedBean.addMenuItem()");
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String name = request.getParameter("addMenuItemForm:name");
        String price = request.getParameter("addMenuItemForm:price"); 
        String menuType = request.getParameter("addMenuItemForm:menuType");
        String output = menuManager.addMenuItem(name, Double.parseDouble(price), co, checkbox, MenuType.valueOf(menuType));
        
        String id = output.split("#")[0];
        String msg = output.split("#")[1];
        if(msg.length() > 1) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, ""));              
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Menu Item \"" + name + "\" successfully created ", ""));             
        } 
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("menuItemID", Long.parseLong(id));
        return "menuitemdetail?faces-redirect=true";
    }
    public String editMenuItem(ActionEvent event) throws IOException {
        System.out.println("MenuItemManagedBean.editMenuItem()");
        menuItem = (MenuItem) event.getComponent().getAttributes().get("toEdit");
        String msg = menuManager.editMenuItem(menuItem.getId(), menuItem.getName(), menuItem.getPrice());
        if(msg != null) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, ""));
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Menu Item \"" + menuItem.getName() + "\" has been updated", ""));
        }
        return "menuitem";        
    }
    public String deleteMenuItem(ActionEvent event) {
        System.out.println("MenuItemManagedBean.deleteMenuItem()");
        menuItem = (MenuItem) event.getComponent().getAttributes().get("toDelete");
        String msg = menuManager.deleteMenuItem(menuItem.getId(), co);
        if(msg != null) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, ""));
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Menu Item has been successfully deleted", ""));
        }
        this.menuItemList = menuManager.getMenuItemList(co);
        return "menuitem";
    }
    public void menuItemDetailActionListener(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("menuItemID", event.getComponent().getAttributes().get("menuItemID"));
        FacesContext.getCurrentInstance().getExternalContext().redirect("menuitemdetail.xhtml");
    }
}
