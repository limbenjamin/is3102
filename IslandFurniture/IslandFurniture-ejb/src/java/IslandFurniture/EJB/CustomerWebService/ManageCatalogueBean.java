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
            StockSupplied stock = iterator.next();
            if (stock.getStock() instanceof FurnitureModel) {
                FurnitureModel furniture = (FurnitureModel)stock.getStock();
                furniture.setPrice(stock.getPrice());
                em.merge(furniture);
                furnitureList.add(furniture);
            }
        } 
        return furnitureList;
    }
    
    @Override
    public List<FurnitureModel> getCountryFeaturedFurniture(CountryOffice co) {
        List<Stock> featuredProducts = co.getFeaturedProducts();
        List<FurnitureModel> furnitureList = new ArrayList<>();
        
        Iterator<Stock> iterator = featuredProducts.iterator();
        while (iterator.hasNext()) {
            Stock stock = iterator.next();
            if (stock instanceof FurnitureModel) {
                Query q = em.createQuery("SELECT s.price FROM StockSupplied s WHERE s.countryOffice=:co AND s.stock=:stock");
                q.setParameter("stock", stock);
                q.setParameter("co", co);
                FurnitureModel furniture = (FurnitureModel)stock;
                furniture.setPrice((Double)q.getSingleResult());
                em.merge(furniture);
                furnitureList.add(furniture);
            }
        } 
        return furnitureList; 
    }
    
    @Override
    public List<RetailItem> getCountryFeaturedRetail(CountryOffice co) {
        List<Stock> featuredProducts = co.getFeaturedProducts();
        List<RetailItem> retailList = new ArrayList<>();
        Iterator<Stock> iterator = featuredProducts.iterator();
        while (iterator.hasNext()) {
            Stock stock = iterator.next();
            if (stock instanceof RetailItem) {
                Query q = em.createQuery("SELECT s.price FROM StockSupplied s WHERE s.countryOffice=:co AND s.stock=:stock");
                q.setParameter("stock", stock);
                q.setParameter("co", co);
                RetailItem retail = (RetailItem)stock;
                retail.setPrice((Double)q.getSingleResult());
                em.merge(retail);
                retailList.add(retail);
            }
        } 
        return retailList;         
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
