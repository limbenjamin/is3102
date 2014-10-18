/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.SalesPlanning;

import IslandFurniture.Entities.StockSupplied;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author a0101774
 */
@Local
public interface PriceManagerLocal {

    public List<StockSupplied> findCountryOfficeWithStock(Long stockID);

    public String editPrice(StockSupplied ss, Double price);

    public String editPoints(Long stockID, Long pointsWorth);
    
}
