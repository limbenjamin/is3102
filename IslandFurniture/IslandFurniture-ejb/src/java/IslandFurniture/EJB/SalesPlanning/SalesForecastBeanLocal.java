/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.SalesPlanning;

import IslandFurniture.EJB.Entities.Month;
import IslandFurniture.EJB.Entities.MonthlyStockSupplyReq;
import IslandFurniture.EJB.Entities.Stock;
import IslandFurniture.EJB.Entities.Store;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Local
public interface SalesForecastBeanLocal {

    public List<MonthlyStockSupplyReq> generateSalesFigures(Store store, Month startMonth, int startYear, Month endMonth, int endYear);

    public List<MonthlyStockSupplyReq> retrieveMssrForStoreStock(Store store, Stock stock, Month startMonth, int startYear, Month endMonth, int endYear);
    
}
