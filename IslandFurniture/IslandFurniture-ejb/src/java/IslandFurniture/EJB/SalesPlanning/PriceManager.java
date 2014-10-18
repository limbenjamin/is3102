/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.SalesPlanning;

import IslandFurniture.Entities.FurnitureModel;
import IslandFurniture.Entities.RetailItem;
import IslandFurniture.Entities.Stock;
import IslandFurniture.Entities.StockSupplied;
import static IslandFurniture.StaticClasses.QueryMethods.getStockSuppliedByStock;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author a0101774 
 */
@Stateless
public class PriceManager implements PriceManagerLocal {

    @PersistenceContext
    EntityManager em;
    
    @Override
    public List<StockSupplied> findCountryOfficeWithStock(Long stockID) {
        List<StockSupplied> returnList;
        Stock s;
        try {
            System.out.println("PriceManager.findCountryOfficeWithStock() ");
            s = em.find(Stock.class, stockID); 
            returnList = getStockSuppliedByStock(em, s); 
            return returnList; 
        } catch(Exception ex) {
            System.err.println("Something went wrong here");
            return null;
        }
    }
    
    @Override
    public String editPrice(StockSupplied ss, Double price) {
        try {
            System.out.println("PriceManager.editPrice()");
            System.out.println("Editing for Country Office " + ss.getCountryOffice().getName() + ". Price to " + price);
            em.merge(ss);
            ss.setPrice(price);
            
            return null;
        } catch(Exception ex) {
            System.err.println("Something went wrong here");
            return null; 
        }
    }
    @Override
    public String editPoints(Long stockID, Long pointsWorth) {
        Stock s;
        FurnitureModel fm;
        RetailItem item;
        try {
            s = em.find(Stock.class, stockID);
            if(s instanceof FurnitureModel) {
                fm = (FurnitureModel) s;
                fm.setPointsWorth(pointsWorth);
            } else {
                item = (RetailItem) s;
                item.setPointsWorth(pointsWorth);
            }
            return null;
        } catch(Exception ex) {
            System.err.println("Something went wrong here");
            return null;  
        }
    }
}
