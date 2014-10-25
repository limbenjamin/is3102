/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.Kitchen;

import IslandFurniture.Entities.Ingredient;
import IslandFurniture.Entities.IngredientInventory;
import IslandFurniture.Entities.Plant;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author KamilulAshraf
 */
@Local
public interface ManageIngredientInventoryLocal {

    //  Function: To add Ingredient Inventory
    void createIngredientInventory(Plant plant, Long ingredientId, Integer qty, Integer threshold);

    //  Function: To delete Ingredient Inventory
    void deleteIngredientInventory(IngredientInventory ingredientInventory);

    //  Function: To edit Ingredient Inventory
    void editIngredientInventory(IngredientInventory ingredientInventoryUpdated);

    //  Function: To edit Ingredient Inventory quantity
    void editIngredientInventoryQty(IngredientInventory si, int qty);

    //  Function: To get Ingredient Inventory Entity
    IngredientInventory getIngredientInventory(Plant plant, Long ingredientId);

    //  Function: To display list of IngredientInventory
    List<IngredientInventory> viewIngredientInventory(Plant plant);

    //  Function: To display list of IngredientInventory
    List<Ingredient> viewIngredient(Plant plant);

    //  Function: To get Ingredient Entity
    Ingredient getIngredient(Long ingredientId);
    
}
