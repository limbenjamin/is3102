/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.Kitchen;

import IslandFurniture.Entities.MenuItem;
import IslandFurniture.Entities.MonthlyMenuItemSalesForecast;
import IslandFurniture.Entities.Store;
import IslandFurniture.Enums.Month;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Local
public interface FoodForecastBeanLocal {

    List<Integer> getYearsOfMmsf(Store store);

    List<MonthlyMenuItemSalesForecast> retrieveMmsfForStoreMi(Store store, MenuItem menuItem, Integer year);

    List<MonthlyMenuItemSalesForecast> retrieveMmsfForStoreMi(Store store, MenuItem menuItem, Month startMonth, int startYear, Month endMonth, int endYear);

    void updateMonthlyMenuItemSalesForecast(Store store, Month startMth, int startYr, Month endMth, int endYr);
    
}
