/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.Kitchen;

import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.Ingredient;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author a0101774
 */
@Local
public interface KitchenStockManagerLocal {

    public String addIngredient(String ingredientName, CountryOffice co);

    public List<Ingredient> getIngredientList(CountryOffice co);

    public String editIngredient(Long ingredientID, String ingredientName);

    public String deleteIngredient(Long ingredientID);
    
}
