/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.CWS;

import IslandFurniture.Entities.FurnitureModel;
import IslandFurniture.Entities.RetailItem;
import IslandFurniture.Entities.Stock;
import IslandFurniture.Entities.StockSupplied;
import IslandFurniture.Entities.Store;
import IslandFurniture.Enums.FurnitureCategory;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Zee
 */
@Stateless
public class ManageCatalogueBean implements ManageCatalogueBeanLocal {
    
    @PersistenceContext
    EntityManager em;    
    
    // get a list of furniture sold in a store
    @Override
    public List<FurnitureModel> getStoreFurniture(Store store) {
        List<StockSupplied> stockSupplied = store.getCountryOffice().getSuppliedWithFrom();
        List<FurnitureModel> furnitureList = new ArrayList();

        Iterator<StockSupplied> iterator = stockSupplied.iterator();
        while (iterator.hasNext()) {
            Stock stock = iterator.next().getStock();
            if (stock instanceof FurnitureModel) {
                furnitureList.add((FurnitureModel)stock);
            }
        }
        return furnitureList;
    }    
    
    // get a list of furniture of a particular category sold in a store
    @Override
    public List<FurnitureModel> getStoreFurnitureByCategory(Store store, FurnitureCategory category) {
        List<StockSupplied> stockSupplied = store.getCountryOffice().getSuppliedWithFrom();
        List<FurnitureModel> furnitureList = new ArrayList();

        Iterator<StockSupplied> iterator = stockSupplied.iterator();
        while (iterator.hasNext()) {
            Stock stock = iterator.next().getStock();
            if (stock instanceof FurnitureModel) {
                FurnitureModel furniture = (FurnitureModel)stock;
                if (furniture.getCategory().equals(category))
                furnitureList.add(furniture);
            }
        }
        return furnitureList;
    }    
    
    // get a list of retail items sold in a store
    @Override
    public List<RetailItem> getStoreRetailItems(Store store) {
        List<StockSupplied> stockSupplied = store.getCountryOffice().getSuppliedWithFrom();
        List<RetailItem> retailItemList = new ArrayList();

        Iterator<StockSupplied> iterator = stockSupplied.iterator();
        while (iterator.hasNext()) {
            Stock stock = iterator.next().getStock();
            if (stock instanceof RetailItem) {
                retailItemList.add((RetailItem)stock);
            }
        }
        return retailItemList;
    }      
    
}
