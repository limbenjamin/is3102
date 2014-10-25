/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.CustomerWebService;

import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.FurnitureModel;
import IslandFurniture.Entities.RetailItem;
import IslandFurniture.Entities.Stock;
import IslandFurniture.Entities.StockSupplied;
import IslandFurniture.Enums.FurnitureCategory;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Zee
 */
@Stateless
public class ManageCatalogueBean implements ManageCatalogueBeanLocal {
    
    @PersistenceContext
    EntityManager em;    
    
    // get all furniture
    @Override
    public List<FurnitureModel> getAllFurniture() {
        Query q = em.createNamedQuery("getAllFurnitureModels");
        return (List<FurnitureModel>) q.getResultList();
    }   
    
    // get a list of furniture sold in a store
    @Override
    public List<FurnitureModel> getStoreFurniture(CountryOffice co) {
        List<StockSupplied> stockSupplied = co.getSuppliedWithFrom();
        List<FurnitureModel> furnitureList = new ArrayList();

        Iterator<StockSupplied> iterator = stockSupplied.iterator();
        while (iterator.hasNext()) {
            Stock stock = iterator.next().getStock();
            if (stock instanceof FurnitureModel) {
                FurnitureModel f=(FurnitureModel)stock;
                furnitureList.add((FurnitureModel)stock);
            }
        }
        return furnitureList;
    }    
    
    // get a list of furniture of a particular category sold in a store
    @Override
    public List<FurnitureModel> getStoreFurnitureByCategory(CountryOffice co, FurnitureCategory category) {
        List<StockSupplied> stockSupplied = co.getSuppliedWithFrom();
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
    public List<RetailItem> getStoreRetailItems(CountryOffice co) {
        List<StockSupplied> stockSupplied = co.getSuppliedWithFrom();
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
    
    @Override
    public FurnitureModel getFurnitureModel(Long id) {
        return (FurnitureModel) em.find(FurnitureModel.class, id);
    }
    
}
