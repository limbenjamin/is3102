/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.SalesPlanning;

import IslandFurniture.Entities.Stock;
import IslandFurniture.Entities.StockSupplied;
import IslandFurniture.Entities.StockSuppliedPK;
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
    public String editPrice(StockSupplied ss, Double price) {
        StockSuppliedPK pk;
        StockSupplied stockSupplied;
        try {
            System.out.println("PriceManager.editPrice()");
            System.out.println("Editing for Country Office " + ss.getCountryOffice().getName() + ". Price to " + price);
            pk = new StockSuppliedPK(ss.getStock().getId(), ss.getCountryOffice().getId(), ss.getManufacturingFacility().getId());
            stockSupplied = em.find(StockSupplied.class, pk);
            stockSupplied.setPrice(price);
            return null;
        } catch(Exception ex) {
            System.err.println("Something went wrong here");
            return null; 
        }
    }
}