/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.Kitchen;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.EJB.Kitchen.KitchenStockManagerLocal;
import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.Ingredient;
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
public class IngredientManagedBean implements Serializable  {
    @EJB
    private ManageUserAccountBeanLocal staffBean;
    @EJB
    private KitchenStockManagerLocal ksm;
    
    private List<Ingredient> ingredientList;
    private String username;
    private Staff staff;
    private Plant plant;
    private CountryOffice co;
    private Ingredient ingredient;

    public List<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public void setIngredientList(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
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

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }
    
    @PostConstruct
    public void init() {
        System.out.println("init:IngredientManagedBean");
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        staff = staffBean.getStaff(username);
        plant = staff.getPlant();

        if (plant instanceof CountryOffice) {
            this.co = (CountryOffice) plant;
            this.ingredientList = ksm.getIngredientList(co);
        } else {
            try {
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect(ec.getRequestContextPath());
            } catch (IOException ex) {

            }
        }
    }
    
    public String addIngredient() {
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        System.out.println("IngredientManagedBean.addIngredient()");
        String name = request.getParameter("addIngredientForm:name");
        String msg = ksm.addIngredient(name, co);
        if(msg != null) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, ""));
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully added Ingredient \"" + name + "\"", ""));
        }
        return "ingredient";
    }
    public String editIngredient(ActionEvent event) throws IOException {
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        System.out.println("IngredientManagedBean.editIngredient()");
        ingredient = (Ingredient) event.getComponent().getAttributes().get("toEdit");
        String msg = ksm.editIngredient(ingredient.getId(), ingredient.getName());
        if(msg != null) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, ""));
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Ingredient \"" + ingredient.getName() + "\" has been updated", ""));
        }
        return "ingredient";
    }
    public String deleteIngredient(ActionEvent event) {
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        System.out.println("IngredientManagedBean.deleteIngredient()");
        ingredient = (Ingredient) event.getComponent().getAttributes().get("toDelete");
        String msg = ksm.deleteIngredient(ingredient.getId());
        if(msg != null) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, ""));
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Ingredient has been successfully deleted", ""));
        }
        this.ingredientList = ksm.getIngredientList(co);
        return "ingredient";
    }
     
}
