/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.OperationalCRM;

import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Stock;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author KamilulAshraf
 */
@Local
public interface ManageFeaturedProductsLocal {

    // Function: Add Featured Products to List
    void addStock(Plant plant, Long stockId);

    // Function: Delete Featured Products
    void deleteStock(Plant plant, Stock stock);

    // Function: View List of Featured Products
    List<Stock> viewFeaturedProducts(Plant plant);
    
}
