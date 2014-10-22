/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.OperationalCRM;

import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Stock;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Kamilul Ashraf
 */
@Stateless
public class ManageFeaturedProducts implements ManageFeaturedProductsLocal {

    @PersistenceContext
    EntityManager em;

    CountryOffice co;
    Stock stock;

    // Function: Add Featured Products to List
    @Override
    public void addStock(Plant plant, Long stockId) {
        co = (CountryOffice) plant;
        stock = (Stock) em.find(Stock.class, stockId);
        co.getFeaturedProducts().add(stock);
        em.merge(co);
        em.flush();
    }

    // Function: Delete Featured Products
    @Override
    public void deleteStock(Plant plant, Stock stock) {
        stock = (Stock) em.find(Stock.class, stock.getId());
        co = (CountryOffice) plant;
        co.getFeaturedProducts().remove(stock);
        em.merge(co);
        em.flush();
    }

    // Function: View List of Featured Products
    @Override
    public List<Stock> viewFeaturedProducts(Plant plant) {
        co = (CountryOffice) plant;
        return co.getFeaturedProducts();
    }

}
