/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.Kitchen;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.EJB.Kitchen.KitchenStockManagerLocal;
import IslandFurniture.EJB.Kitchen.MenuManagerLocal;
import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.Dish;
import IslandFurniture.Entities.MenuItem;
import IslandFurniture.Entities.MenuItemDetail;
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
public class MenuItemDetailManagedBean implements Serializable {
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
    private Long menuItemID;
    private boolean alacarte;
    private MenuItem menuItem;
    private MenuItemDetail menuItemDetail;
    private List<MenuType> menuTypeList;
    private List<Dish> dishList;

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

    public Long getMenuItemID() {
        return menuItemID;
    }

    public void setMenuItemID(Long menuItemID) {
        this.menuItemID = menuItemID;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public List<MenuType> getMenuTypeList() {
        return menuTypeList;
    }

    public void setMenuTypeList(List<MenuType> menuTypeList) {
        this.menuTypeList = menuTypeList;
    }

    public boolean isAlacarte() {
        return alacarte;
    }

    public void setAlacarte(boolean alacarte) {
        this.alacarte = alacarte;
    }

    public List<Dish> getDishList() {
        return dishList;
    }

    public void setDishList(List<Dish> dishList) {
        this.dishList = dishList;
    }

    public MenuItemDetail getMenuItemDetail() {
        return menuItemDetail;
    }

    public void setMenuItemDetail(MenuItemDetail menuItemDetail) {
        this.menuItemDetail = menuItemDetail;
    }
    
    @PostConstruct
    public void init() {
        System.out.println("init:MenuItemDetailManagedBean");
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        staff = staffBean.getStaff(username);
        plant = staff.getPlant();

        if (plant instanceof CountryOffice) {
            this.menuItemID = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("menuItemID");
            this.menuItem = menuManager.getMenuItem(menuItemID);
            if(menuItem != null) {
                this.co = (CountryOffice) plant;
                this.menuTypeList = new ArrayList<MenuType>(EnumSet.allOf(MenuType.class));
                this.alacarte = menuItem.isAlaCarte();
                this.dishList = ksm.getDishList(co);
            }
        } else {
            try {
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect(ec.getRequestContextPath());
            } catch (IOException ex) {

            }
        }
    }
    public String editMenuItemAttribute(ActionEvent event) {
        System.out.println("MenuItemDetailManagedBean.editMenuItemAttribute");
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String menuType = request.getParameter("attributeForm:menuType");
        String price = request.getParameter("attributeForm:price"); 
        System.out.println("MenuType is " + menuType + ". Price is " + price + ". Ala carte is " + alacarte);
        String msg = menuManager.editMenuItemAttribute(menuItemID, MenuType.valueOf(menuType), Double.parseDouble(price), alacarte);
        if(msg != null) { 
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, ""));
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Menu Item has been successfully updated", ""));
        }
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("menuItemID", menuItemID);
        return "menuitemdetail";
    }
    public String editMenuItemDetail(ActionEvent event) throws IOException {
        System.out.println("MenuItemDetailManagedBean.editMenuItemDetail");
        menuItemDetail = (MenuItemDetail) event.getComponent().getAttributes().get("toEdit");
        String msg = menuManager.editMenuItemDetail(menuItemDetail.getId(), menuItemDetail.getQuantity());
        if(msg != null) { 
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, ""));
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Menu Item Detail has been successfully updated", ""));
        }
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("menuItemID", menuItemID);
        return "menuitemdetail";        
    }
    public String deleteMenuItemDetail() {
        System.out.println("MenuItemDetailManagedBean.deleteMenuItemDetail");
        String ID = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("menuItemDetailID");
        String msg = menuManager.deleteMenuItemDetail(Long.parseLong(ID));
        if(msg != null) { 
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, ""));
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Menu Item Detail has been successfully deleted", ""));
        }
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("menuItemID", menuItemID);
        return "menuitemdetail";    
    }
    public String addMenuItemDetail(ActionEvent event) {
        System.out.println("MenuItemDetailManagedBean.addMenuItemDetail");
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String dishID = request.getParameter("addNewMenuItemDetailForm:dishID");
        String dishQuantity = request.getParameter("addNewMenuItemDetailForm:dishQuantity");
        String msg = menuManager.addMenuItemDetail(menuItemID, Long.parseLong(dishID), Integer.parseInt(dishQuantity));
        if(msg != null) { 
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, ""));
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Menu Item Detail has been successfully added", ""));
        }
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("menuItemID", menuItemID);
        return "menuitemdetail"; 
    }
    
}
