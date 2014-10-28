/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.Kitchen;

import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.Dish;
import IslandFurniture.Entities.Ingredient;
import IslandFurniture.Entities.IngredientInventory;
import IslandFurniture.Entities.IngredientInventoryPK;
import IslandFurniture.Entities.Recipe;
import IslandFurniture.Entities.RecipeDetail;
import IslandFurniture.Entities.Store;
import IslandFurniture.StaticClasses.QueryMethods;
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
        IngredientInventory inventory;
        List<Store> storeList;
        List<IngredientInventory> ingredientInventoryList;
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
                storeList = co.getStores();
                for(Store s : storeList) {
                    inventory = new IngredientInventory();
                    inventory.setIngredient(ing);
                    inventory.setStore(s);
                    inventory.setQty(0);
                    inventory.setThreshold(0);
                    em.persist(inventory);
                    
                    if(s.getIngredInvents() == null) 
                        s.setIngredInvents(new ArrayList<>());
                    s.getIngredInvents().add(inventory);
                }
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
        List<IngredientInventory> ingredientInvList;
        List<Store> storeList = new ArrayList<>();
        String returnMsg = "";
        try {
            System.out.println("KitchenStockManager.deleteIngredient()");
            dishList = getDishListByCountryOffice(em, co);
            ing = em.find(Ingredient.class, ingredientID);
            for(Dish d : dishList) {
                for(RecipeDetail rd : d.getRecipe().getRecipeDetails()) {
                    if(rd.getIngredient().equals(ing)) {
                        System.out.println("Invald deletion. Ingredient already used in a dish");
                        return "Invalid deletion because Ingredient \"" + ing.getName() + "\" already used in a dish";
                    }
                }
            }
            System.out.println("Ingredient not used in any dishes.");
            ingredientInvList = QueryMethods.findIngredientInventoryByIngredient(em, ing);
            System.out.println("1: IngredientInventory size " + ingredientInvList.size());
            for(IngredientInventory iInv : ingredientInvList) {
                if(iInv.getQty() != null && iInv.getQty() > 0) 
                    storeList.add(iInv.getStore());
            }
            if(storeList.size() > 1) {
                System.err.println("Invalid deletion. Ingredient has existing quantities in stores");
                returnMsg = "Invalid deletion because Ingredient \"" + ing.getName() + "\" has existing quantities in following Stores: <br />";
                for(Store s : storeList)
                    returnMsg += s.getName() + "<br />"; 
                return returnMsg;
            } else if(storeList.size() == 1) {
                System.err.println("Invalid deletion. Ingredient has existing quantities in stores");
                return "Invalid deletion because Ingredient \"" + ing.getName() + "\" has existing quantities in Store: <br />" + storeList.get(0).getName();
            } else {
                System.out.println("Ingredient is not in any ingredient inventory. Carry on removing all ingredient inventory now");
                for(IngredientInventory iInv : ingredientInvList)
                    em.remove(iInv);
            }
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
        Dish dish;
        try {
            System.out.println("KitchenStockManager.editDish()");
            dish = em.find(Dish.class, dishID);
            dish.setName(dishName);
            return null;
        } catch(Exception ex) {
            System.err.println("Something went wrong here"); 
            return "Unexpected error occured";
        }
    } 
    public String deleteDish(Long dishID, CountryOffice co) {
        Dish dish;
        try {
            System.out.println("KitchenStockManager.deleteDish()");
            dish = em.find(Dish.class, dishID);
            em.remove(dish);
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
