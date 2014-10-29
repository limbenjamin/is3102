/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.OperationalCRM;

import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.FurnitureModel;
import IslandFurniture.Entities.RetailItem;
import IslandFurniture.Entities.Stock;
import java.util.Iterator;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Stateless
public class ProductInfoBean implements ProductInfoBeanLocal {

    @PersistenceContext(unitName = "IslandFurniture")
    private EntityManager em;

    @Override
    public List<Stock> searchProductByName(String stockName) {
        Query q = em.createQuery("SELECT a FROM Stock a WHERE a.name LIKE :name");
        q.setParameter("name", "%" + stockName + "%");

        List<Stock> results = (List<Stock>) q.getResultList();
        Iterator itr = results.listIterator();
        while(itr.hasNext()){
            Stock stock = (Stock) itr.next();
            if(!(stock instanceof FurnitureModel || stock instanceof RetailItem)){
                itr.remove();
            }
        }
        return results;
    }

    @Override
    public Stock findProductById(long stockId, CountryOffice co) {
        Stock stock = em.find(Stock.class, stockId);
        if (stock == null || !(stock instanceof FurnitureModel || stock instanceof RetailItem)) {
            return null;
        } else {
            return stock;
        }
    }

}
