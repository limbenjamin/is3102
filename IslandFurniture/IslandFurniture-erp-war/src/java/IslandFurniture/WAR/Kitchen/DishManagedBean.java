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
public class DishManagedBean implements Serializable {
    @EJB
    private ManageUserAccountBeanLocal staffBean;
    @EJB
    private KitchenStockManagerLocal ksm;
    
    private List<Dish> dishList;
    private String username;
    private Staff staff;
    private Plant plant;
    private CountryOffice co;
    private Dish dish;

    public List<Dish> getDishList() {
        return dishList;
    }

    public void setDishList(List<Dish> dishList) {
        this.dishList = dishList;
    }

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

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }
    
    @PostConstruct
    public void init() {
        System.out.println("init:DishManagedBean");
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        staff = staffBean.getStaff(username);
        plant = staff.getPlant();

        if (plant instanceof CountryOffice) {
            this.co = (CountryOffice) plant;
            this.dishList = ksm.getDishList(co);
        } else {
            try {
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect(ec.getRequestContextPath());
            } catch (IOException ex) {

            }
        }
    }
    public String addDish() {
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        System.out.println("DishManagedBean.addDish()");
        String name = request.getParameter("addDishForm:name"); 
        String output = ksm.addDish(name, co);
        String id = output.split("#")[0];
        String msg = output.split("#")[1];
        if(msg.length() > 1) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, ""));              
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Dish Item \"" + name + "\" successfully created ", ""));             
        } 
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("dishID", Long.parseLong(id));
        return "recipe?faces-redirect=true";
    }
    public String editDish(ActionEvent event) throws IOException {
        System.out.println("DishManagedBean.editDish()");
        dish = (Dish) event.getComponent().getAttributes().get("toEdit");
        String msg = ksm.editDish(dish.getId(), dish.getName());
        if(msg != null) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, ""));
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Dish Item \"" + dish.getName() + "\" has been updated", ""));
        }
        return "dish";
    }
    public String deleteDish(ActionEvent event) {
        System.out.println("DishManagedBean.deleteDish()");
        dish = (Dish) event.getComponent().getAttributes().get("toDelete");
        String msg = ksm.deleteDish(dish.getId(), co);
        if(msg != null) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, ""));
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Dish Item has been successfully deleted", ""));
        }
        this.dishList = ksm.getDishList(co);
        return "dish";
    }
    public void recipeActionListener(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("dishID", event.getComponent().getAttributes().get("dishID"));
        FacesContext.getCurrentInstance().getExternalContext().redirect("recipe.xhtml");
        
    }
}
