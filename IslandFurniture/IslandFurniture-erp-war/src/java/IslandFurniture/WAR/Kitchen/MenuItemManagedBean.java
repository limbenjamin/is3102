/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.Kitchen;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.EJB.Kitchen.KitchenStockManagerLocal;
import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.Dish;
import IslandFurniture.Entities.Menu;
import IslandFurniture.Entities.MenuItem;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Staff;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
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
    private ManageUserAccountBeanLocal staffBean;
    @EJB
    private KitchenStockManagerLocal ksm;
    
    private String username;
    private Staff staff;
    private Plant plant;
    private CountryOffice co;
    private Long menuID;
    private Menu menu;
    private List<Dish> dishList;
    private MenuItem menuItem;

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

    public Long getMenuID() {
        return menuID;
    }

    public void setMenuID(Long menuID) {
        this.menuID = menuID;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public List<Dish> getDishList() {
        return dishList;
    }

    public void setDishList(List<Dish> dishList) {
        this.dishList = dishList;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }
    
    @PostConstruct
    public void init() {
        System.out.println("init:MenuItemManagedBean");
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        staff = staffBean.getStaff(username); 
        plant = staff.getPlant();
        
        try {
            if(plant instanceof CountryOffice) {
                this.menuID = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("menuID");
                if(menuID != null) {
                    this.co = (CountryOffice) plant;
                    this.menu = ksm.getMenu(menuID);
                    this.dishList = ksm.getDishList(co);
                } else {
                    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                    ec.redirect("menu.xhtml"); 
                }
            } else {
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect(ec.getRequestContextPath());
            }
        } catch(IOException ex) {
            
        }  
    } 
    public void addToMenuDetail(ActionEvent event) {
        System.out.println("MenuItemManagedBean.addToMenuDetail()");
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String menuID = request.getParameter("addToMenuDetailForm:menuID");
        String dishID = request.getParameter("addToMenuDetailForm:dishID");
        String dishQuantity = request.getParameter("addToMenuDetailForm:dishQuantity");
        String msg = ksm.addToMenu(Long.parseLong(menuID), Long.parseLong(dishID), Integer.parseInt(dishQuantity));
        if(msg != null) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, ""));   
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Dish successfully added to Menu Item Detail", ""));        
        } 
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("menuID", menu.getId());
    }
    public String editMenuDetail(ActionEvent event) throws IOException {
        System.out.println("MenuItemManagedBean.editMenuDetail()");
        menuItem = (MenuItem) event.getComponent().getAttributes().get("toEdit");
        String msg = ksm.editMenuDetail(menuItem.getId(), menuItem.getQuantity());
        if(msg != null) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, ""));   
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Menu Item Detail has been updated", ""));        
        } 
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("menuID", menu.getId());
        return "menuitem";
    }
    public String deleteMenuDetail() {
        System.out.println("MenuItemManagedBean.deleteMenuDetail()");
        String ID = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("menuItemID");
        String msg = ksm.deleteMenuDetail(Long.parseLong(ID));       
        if(msg != null) { 
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, ""));
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Menu Item Detail has been successfully deleted", ""));
        }
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("menuID", menu.getId());
        return "menuitem";
    }
}
