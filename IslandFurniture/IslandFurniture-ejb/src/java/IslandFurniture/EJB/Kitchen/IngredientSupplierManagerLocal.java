/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.Kitchen;

import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.IngredientSupplier;
import IslandFurniture.Entities.Store;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author a0101774
 */
@Local
public interface IngredientSupplierManagerLocal {

    public List<IngredientSupplier> displaySupplierList(CountryOffice co);

    public IngredientSupplier getSupplier(Long id);

    public String addSupplier(String supplierName, String countryName, String phoneNo, String email, CountryOffice co);

    public String deleteSupplier(Long id);

    public String editSupplier(Long id, String name, String countryName, String phoneNumber, String email);

    public String addIngredientContractDetail(Long supplierID, Long ingredientID, Integer size, Integer leadTime, Double lotPrice, CountryOffice co);

    public String editIngredientContractDetail(Long id, Integer size, Integer leadTime, Double lotPrice);

    public String deleteIngredientContractDetail(Long id, Long supplierID);
    
}
