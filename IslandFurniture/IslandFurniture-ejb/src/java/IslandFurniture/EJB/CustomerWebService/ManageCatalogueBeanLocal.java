/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.CustomerWebService;

import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.FurnitureModel;
import IslandFurniture.Entities.RetailItem;
import IslandFurniture.Enums.FurnitureCategory;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Zee
 */
@Local
public interface ManageCatalogueBeanLocal {
    
    List<FurnitureModel> getAllFurniture();
    
    void updateStockSuppliedFurniturePrice(CountryOffice co);
    
    void updateStockSuppliedRetailItemPrice(CountryOffice co);

    // get a list of furniture sold in a store
    List<FurnitureModel> getStoreFurniture(CountryOffice co);

    // get a list of furniture of a particular category sold in a store
    List<FurnitureModel> getStoreFurnitureByCategory(CountryOffice co, FurnitureCategory category);

    // get a list of retail items sold in a store
    List<RetailItem> getStoreRetailItems(CountryOffice co);
    
    // get details of a furniture model
    FurnitureModel getFurnitureModel(Long id);
    
    RetailItem getRetailItem(Long id);
    
    List<FurnitureModel> getCountryFeaturedFurniture(CountryOffice co);
    
    List<RetailItem> getCountryFeaturedRetail(CountryOffice co);
    
}
