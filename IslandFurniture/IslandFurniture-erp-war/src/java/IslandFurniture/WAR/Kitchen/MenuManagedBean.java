/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.Kitchen;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.EJB.Kitchen.KitchenStockManagerLocal;
import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.Menu;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author a0101774
 */
@ManagedBean
@ViewScoped
public class MenuManagedBean implements Serializable {
    @EJB
    private ManageUserAccountBeanLocal staffBean;
    @EJB
    private KitchenStockManagerLocal ksm;
    
    private String username;
    private Staff staff;
    private Plant plant;
    private CountryOffice co;
    private List<Menu> menuList;
    private Menu menu;

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

    public List<Menu> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<Menu> menuList) {
        this.menuList = menuList;
    }
    
    @PostConstruct
    public void init() {
        System.out.println("init:MenuManagedBean");
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        staff = staffBean.getStaff(username);
        plant = staff.getPlant();

        if (plant instanceof CountryOffice) {
            this.co = (CountryOffice) plant;
            this.menuList = ksm.getMenuList(co);
        } else {
            try {
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect(ec.getRequestContextPath());
            } catch (IOException ex) {

            }
        }
    }
    public String addMenu() {
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        System.out.println("MenuManagedBean.addMenu()");
        String name = request.getParameter("addMenuForm:name"); 
        String price = request.getParameter("addMenuForm:price"); 
        String output = ksm.addMenu(name, Double.parseDouble(price), co);
        String id = output.split("#")[0];
        String msg = output.split("#")[1];
        if(msg.length() > 1) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, ""));              
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Menu \"" + name + "\" successfully created ", ""));             
        } 
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("menuID", Long.parseLong(id));
        return "menuitem?faces-redirect=true";
    }
    public String editMenu(ActionEvent event) throws IOException {
        System.out.println("MenuManagedBean.editMenu()");
        menu = (Menu) event.getComponent().getAttributes().get("toEdit");
        String msg = ksm.editMenu(menu.getId(), menu.getName());
        if(msg != null) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, ""));
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Menu \"" + menu.getName() + "\" has been updated", ""));
        }
        return "menu";
    }
    public String deleteMenu(ActionEvent event) {
        System.out.println("MenuManagedBean.deleteMenu()");
        menu = (Menu) event.getComponent().getAttributes().get("toDelete");
        String msg = ksm.deleteDish(menu.getId(), co);
        if(msg != null) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, ""));
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Menu has been successfully deleted", ""));
        }
        this.menuList = ksm.getMenuList(co);
        return "menu";
    } 
    public void menuItemActionListener(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("menuID", event.getComponent().getAttributes().get("menuID"));
        FacesContext.getCurrentInstance().getExternalContext().redirect("menuitem.xhtml");
    }
    
}
