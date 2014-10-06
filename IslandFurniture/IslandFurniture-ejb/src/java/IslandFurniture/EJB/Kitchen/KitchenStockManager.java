/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.Kitchen;

import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.Dish;
import IslandFurniture.Entities.Ingredient;
import IslandFurniture.Entities.Recipe;
import IslandFurniture.Entities.RecipeDetail;
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
                msg = "Ingredient \"" + ingredientName + "\" already exists";     
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
//            for(int i=0; i<dishList.size(); i++) {
//                for(int j=0; j<dishList.get(i).getRecipe().size(); j++) {
//                    if(dishList.get(i).getRecipe().get(j).equals(ing)) {
//                        System.out.println("Invalid delete. Ingredient exists in a dish");
//                        return "Invalid deletion because Ingredient \"" + ing.getName() + "\" used for dish preparation";
//                    }
//                }
//            }
            ing = em.find(Ingredient.class, ingredientID);
            em.remove(ing);
            return null;
        } catch(Exception ex) {
            System.err.println("Something went wrong here"); 
            return "Unexpected error occured";
        }
    }
    public Dish getDish(Long dishID) {
        Dish dish;
        try {
            dish = em.find(Dish.class, dishID);
            return dish;
        } catch(Exception ex) {
            System.err.println("Something went wrong here");
            return null;  
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
        Recipe recipe;
        List<RecipeDetail> recipeDetails;
        try {
            System.out.println("KitchenStockManager.addDish()");
            dish = getDishByCountryOfficeAndName(em, co, dishName);
            if(dish != null) {
                System.out.println("Ingredient " + dishName + " already exists. Directing to Recipe");
                msg = "" + dish.getId() + "#Ingredient " + dishName + " already exists. Directing to Recipe";     
            } else {
                dish = new Dish();
                dish.setName(dishName);
                recipe = new Recipe();
                recipeDetails = new ArrayList<RecipeDetail>();
                
                recipe.setRecipeDetails(recipeDetails);
                dish.setRecipe(recipe);
                dish.setCo(co);
                em.persist(dish);
                msg = "" + dish.getId() + "#0";
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
    public String addToRecipe(Long dishID, Long ingredientID, Double ingredientQuantity) {
        Dish dish;
        Ingredient ingredient;
        Recipe recipe;
        RecipeDetail recipeDetail;
        String msg = null;
        try {
            System.out.println("KitchenStockManager.addToRecipe()");
            ingredient = em.find(Ingredient.class, ingredientID);
            dish = em.find(Dish.class, dishID);
            recipe = dish.getRecipe();
            for(int i=0; i<recipe.getRecipeDetails().size(); i++) {
                if(recipe.getRecipeDetails().get(i).getIngredient().equals(ingredient)) {
                System.out.println("RecipeDetail already exists");
                return "RecipeDetail already exists ";
                }
            }
            recipeDetail = new RecipeDetail();
            recipeDetail.setIngredient(ingredient);
            recipeDetail.setQuantity(ingredientQuantity);
            recipeDetail.setRecipe(recipe);
            recipe.getRecipeDetails().add(recipeDetail);
            System.out.println("Successfully added new Recipe Detail");
            em.flush();
            return msg;
        } catch(Exception ex) {
            System.err.println("Something went wrong here"); 
            return "Unexpected error occured";
        }
    }
    public String editRecipeDetail(Long recipeDetailID, Double quantity) {
        RecipeDetail recipeDetail;
        try {
            System.out.println("KitchenStockManager.editRecipeDetail()");
            recipeDetail = em.find(RecipeDetail.class, recipeDetailID);
            recipeDetail.setQuantity(quantity);
            em.persist(recipeDetail);
            return null;
        } catch(Exception ex) {
            System.err.println("Something went wrong here"); 
            return "Unexpected error occured";
        }
    }
    public String deleteRecipeDetail(Long recipeDetailID) {
        RecipeDetail recipeDetail;
        Recipe recipe;
        try {
            System.out.println("KitchenStockManager.deleteRecipeDetail()");
            recipeDetail = em.find(RecipeDetail.class, recipeDetailID);
            recipe = recipeDetail.getRecipe();
            recipe.getRecipeDetails().remove(recipeDetail);
            em.remove(recipeDetail);
            em.persist(recipe);
            return null;
        } catch(Exception ex) {
            System.err.println("Something went wrong here"); 
            return "Unexpected error occured";
        }        
    }
}
