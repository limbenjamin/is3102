/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.Kitchen;

import IslandFurniture.DataStructures.Couple;
import IslandFurniture.Entities.Ingredient;
import IslandFurniture.Entities.MenuItem;
import IslandFurniture.Entities.MonthlyMenuItemSalesForecast;
import IslandFurniture.Entities.Store;
import IslandFurniture.Entities.WeeklyIngredientSupplyReq;
import IslandFurniture.Entities.WeeklyMenuItemSalesForecast;
import IslandFurniture.Enums.Month;
import IslandFurniture.Exceptions.ForecastFailureException;
import IslandFurniture.Exceptions.InvalidInputException;
import IslandFurniture.Exceptions.InvalidMmsfException;
import IslandFurniture.Exceptions.InvalidWmsfException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Local
public interface FoodForecastBeanLocal {

    List<Integer> getYearsOfMmsf(Store store);
    
    List<MonthlyMenuItemSalesForecast> retrieveNaiveForecast(Store store, MenuItem mi) throws ForecastFailureException, InvalidMmsfException;
    
    List<MonthlyMenuItemSalesForecast> retrieveNPointForecast(Store store, MenuItem mi, int nPoint) throws ForecastFailureException, InvalidInputException;
    
    void saveMonthlyMenuItemSalesForecast(List<Couple<MenuItem, List<MonthlyMenuItemSalesForecast>>> miMmsfList) throws InvalidMmsfException;
    
    void saveWeeklyMenuItemSalesForecast(List<Couple<MenuItem, List<WeeklyMenuItemSalesForecast>>> miWmsfList) throws InvalidWmsfException;
    
    boolean isWmsfListEditable(List<Couple<MenuItem, List<WeeklyMenuItemSalesForecast>>> miWmsfList);
    
    void orderIngredients(List<Couple<MenuItem, List<WeeklyMenuItemSalesForecast>>> miWmsfList, Store store, Month month, int year, int numWeeks);
    
    List<Couple<MenuItem, List<WeeklyMenuItemSalesForecast>>> resetWmsfList(List<Couple<MenuItem, List<WeeklyMenuItemSalesForecast>>> miWmsfList);
    
    void reviewMonthlyMenuItemSalesForecast(List<Couple<MenuItem, List<MonthlyMenuItemSalesForecast>>> miMmsfList, boolean approved) throws InvalidMmsfException;
    
    List<WeeklyMenuItemSalesForecast> retrieveWmsfForStoreMi(Store store, MenuItem menuItem, Integer year, Month month);
    
    List<WeeklyIngredientSupplyReq> retrieveWisrForStoreIngredYrMth(Store store, Ingredient ingred, int year, Month month);

    List<MonthlyMenuItemSalesForecast> retrieveMmsfForStoreMi(Store store, MenuItem menuItem, Integer year);

    List<MonthlyMenuItemSalesForecast> retrieveMmsfForStoreMi(Store store, MenuItem menuItem, Month startMonth, int startYear, Month endMonth, int endYear);

    List<MonthlyMenuItemSalesForecast> retrieveUnlockedMmsfForStoreMi(Store store, MenuItem mi);

    List<MonthlyMenuItemSalesForecast> retrieveLockedMmsfForStoreMi(Store store, MenuItem mi, int mthsHist) throws IllegalArgumentException;

    void updateMonthlyMenuItemSalesForecast(Store store, Month startMth, int startYr, Month endMth, int endYr);

    void makeIngredPurchaseOrders(Store store, Month month, int year, int week);

}
