/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.Kitchen;

import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.Dish;
import IslandFurniture.Entities.Ingredient;
import static IslandFurniture.StaticClasses.QueryMethods.findIngredientByName;
import static IslandFurniture.StaticClasses.QueryMethods.getDishByCountryOfficeAndName;
import static IslandFurniture.StaticClasses.QueryMethods.getDishListByCountryOffice;
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
            System.out.println("KitchenStockManager.editIngredient()");
            ing = em.find(Ingredient.class, ingredientID);
            ing.setName(ingredientName);
            return null;
        } catch(Exception ex) {
            System.err.println("Something went wrong here"); 
            return "Unexpected error occured";
        }
    }
    public String deleteIngredient(Long ingredientID, CountryOffice co) {
        Ingredient ing;
        List<Dish> dishList;
        try {
            System.out.println("KitchenStockManager.deleteIngredient()");
            dishList = getDishListByCountryOffice(em, co);
            ing = em.find(Ingredient.class, ingredientID);
            for(int i=0; i<dishList.size(); i++) {
                if(dishList.get(i).getIngredients().contains(ing)) {
                    System.out.println("Invalid delete. Ingredient exists in a dish");
                    return "Invalid deletion because Ingredient \"" + ing.getName() + "\" used for dish preparation";
                }
            }
            ing = em.find(Ingredient.class, ingredientID);
            em.remove(ing);
            return null;
        } catch(Exception ex) {
            System.err.println("Something went wrong here"); 
            return "Unexpected error occured";
        }
    }
    public List<Dish> getDishList(CountryOffice co) {
        List<Dish> dishList;
        try {
            dishList = getDishListByCountryOffice(em, co);
            return dishList;
        } catch(Exception ex) {
            System.err.println("Something went wrong here");
            return null;
        }
    }
    public String addDish(String dishName, CountryOffice co) {
        Dish dish;
        String msg = null;
        List<Ingredient> ingredientList;
        try {
            System.out.println("KitchenStockManager.addDish()");
            dish = getDishByCountryOfficeAndName(em, co, dishName);
            if(dish != null) {
                System.out.println("Ingredient " + dishName + " already exists");
                msg = "Ingredient " + dishName + " already exists";     
            } else {
                ingredientList = new ArrayList<Ingredient>();
                dish = new Dish();
                dish.setName(dishName);
                dish.setCo(co);
                dish.setIngredients(ingredientList);
                em.persist(dish);
            } 
            return msg;
        } catch(Exception ex) {
            System.err.println("Something went wrong here"); 
            return "Unexpected error occured";
        }
    }
    public String editDish(Long dishID, String dishName) {
        Dish Dish;
        try {
            System.out.println("KitchenStockManager.editDish()");
            Dish = em.find(Dish.class, dishID);
            Dish.setName(dishName);
            return null;
        } catch(Exception ex) {
            System.err.println("Something went wrong here"); 
            return "Unexpected error occured";
        }
    } 
    public String deleteDish(Long dishID, CountryOffice co) {
        Dish Dish;
        try {
            System.out.println("KitchenStockManager.deleteDish()");
            Dish = em.find(Dish.class, dishID);
            em.remove(Dish);
            return null;
        } catch(Exception ex) {
            System.err.println("Something went wrong here"); 
            return "Unexpected error occured";
        }
    }
}
