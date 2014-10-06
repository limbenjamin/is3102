/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.Kitchen;

import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.Ingredient;
import static IslandFurniture.StaticClasses.QueryMethods.findIngredientByName;
import static IslandFurniture.StaticClasses.QueryMethods.getIngredientByCountryOfficeAndName;
import static IslandFurniture.StaticClasses.QueryMethods.getIngredientListByCountryOffice;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author a0101774
 */
@Stateful
public class KitchenStockManager implements KitchenStockManagerLocal {

    @PersistenceContext
    EntityManager em;
    
    public List<Ingredient> getIngredientList(CountryOffice co) {
        List<Ingredient> ingredientList;
        try {
            ingredientList = getIngredientListByCountryOffice(em, co);
            return ingredientList;
        } catch(Exception ex) {
            System.err.println("Something went wrong here");
            return null;
        }
    }
    public String addIngredient(String ingredientName, CountryOffice co) {
        Ingredient ing;
        String msg = null;
        List<Ingredient> ingredientList;
        try {
            System.out.println("KitchenStockManager.addIngredient()");
            ing = getIngredientByCountryOfficeAndName(em, co, ingredientName);
            if(ing != null) {
                System.out.println("Ingredient " + ingredientName + " already exists");
                msg = "Ingredient " + ingredientName + " already exists";     
            } else {
                ing = new Ingredient();
                ing.setName(ingredientName);
                ing.setCountryOffice(co);
                em.persist(ing); 
            }
            return msg;
        } catch(Exception ex) {
            System.err.println("Something went wrong here"); 
            return "Unexpected error occured";
        }
    }
    public String editIngredient(Long ingredientID, String ingredientName) {
        Ingredient ing;
        try {
            ing = em.find(Ingredient.class, ingredientID);
            ing.setName(ingredientName);
            return null;
        } catch(Exception ex) {
            System.err.println("Something went wrong here"); 
            return "Unexpected error occured";
        }
    }
    public String deleteIngredient(Long ingredientID) {
        Ingredient ing;
        try {
            ing = em.find(Ingredient.class, ingredientID);
            em.remove(ing);
            return null;
        } catch(Exception ex) {
            System.err.println("Something went wrong here"); 
            return "Unexpected error occured";
        }
    }
}
