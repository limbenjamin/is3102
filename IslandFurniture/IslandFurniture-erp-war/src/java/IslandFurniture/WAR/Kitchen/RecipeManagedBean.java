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
import IslandFurniture.Entities.Ingredient;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.RecipeDetail;
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
public class RecipeManagedBean implements Serializable {
    @EJB
    private ManageUserAccountBeanLocal staffBean;
    @EJB
    private KitchenStockManagerLocal ksm;
    
    private String username;
    private Staff staff;
    private Plant plant;
    private CountryOffice co;
    private Dish dish;
    private Long dishID;
    private List<Ingredient> ingredientList;
    private RecipeDetail recipeDetail;

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

    public Long getDishID() {
        return dishID;
    }

    public void setDishID(Long dishID) {
        this.dishID = dishID;
    }

    public List<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public void setIngredientList(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    public RecipeDetail getRecipeDetail() {
        return recipeDetail;
    }

    public void setRecipeDetail(RecipeDetail recipeDetail) {
        this.recipeDetail = recipeDetail;
    }

    @PostConstruct
    public void init() {
        System.out.println("init:RecipeManagedBean");
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        staff = staffBean.getStaff(username); 
        plant = staff.getPlant();
        
        this.dishID = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("dishID");
        try {
            if(dishID == null) {
                    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                    ec.redirect("dish.xhtml"); 
            }
            if(!(plant instanceof CountryOffice)) {
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect(ec.getRequestContextPath());
            } else {
                this.co = (CountryOffice) plant;
                this.ingredientList = ksm.getIngredientList(co);
                this.dish = ksm.getDish(dishID);
                
            }
        } catch(IOException ex) {
            
        }
    }
    public String addToRecipe(ActionEvent event) {
        System.out.println("RecipeManagedBean.addToRecipe()");
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String dishID = request.getParameter("addToRecipeForm:dishID");
        String ingredientID = request.getParameter("addToRecipeForm:ingredientID");
        String ingredientQuantity = request.getParameter("addToRecipeForm:ingredientQuantity");
        String msg = ksm.addToRecipe(Long.parseLong(dishID), Long.parseLong(ingredientID), Double.parseDouble(ingredientQuantity));
        if(msg != null) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, ""));   
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Ingredient successfully added to Recipe", ""));        
        } 
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("dishID", dish.getId());
        return "recipe";
    }
    public void addIngredient(AjaxBehaviorEvent event) {
        System.out.println("RecipeManagedBean.addToRecipe()");
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String name = request.getParameter("addNewIngredientForm:ingredientName");
        String msg = ksm.addIngredient(name, co);
        this.ingredientList = ksm.getIngredientList(co);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("dishID", dish.getId());
    }
    public String editRecipeDetail(ActionEvent event) throws IOException {
        System.out.println("RecipeManagedBean.editRecipeDetail()");
        recipeDetail = (RecipeDetail) event.getComponent().getAttributes().get("toEdit");
        String msg = ksm.editRecipeDetail(recipeDetail.getId(), recipeDetail.getQuantity());
        if(msg != null) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, ""));   
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Ingredient Quantity has been updated", ""));        
        } 
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("dishID", dish.getId());
        return "recipe";
    }
    public String deleteRecipeDetail() {
        System.out.println("RecipeManagedBean.deleteRecipeDetail()");
        String ID = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("recipeDetailID");
        String msg = ksm.deleteRecipeDetail(Long.parseLong(ID));       
        if(msg != null) { 
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, ""));
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Recipe Detail has been successfully deleted", ""));
        }
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("dishID", dish.getId());
        return "recipe";
    }
    
}
