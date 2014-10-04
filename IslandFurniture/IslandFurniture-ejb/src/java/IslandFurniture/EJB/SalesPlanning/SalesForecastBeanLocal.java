/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.SalesPlanning;

import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Enums.Month;
import IslandFurniture.Entities.MonthlyStockSupplyReq;
import IslandFurniture.Entities.Stock;
import IslandFurniture.EJB.Exceptions.ForecastFailureException;
import IslandFurniture.EJB.Exceptions.InvalidInputException;
import IslandFurniture.EJB.Exceptions.InvalidMssrException;
import IslandFurniture.DataStructures.Couple;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Local
public interface SalesForecastBeanLocal {

    public void updateMonthlyStockSupplyReq(CountryOffice co, Month startMonth, int startYear, Month endMonth, int endYear);

    public List<MonthlyStockSupplyReq> retrieveNaiveForecast(CountryOffice co, Stock stock) throws ForecastFailureException, InvalidMssrException;
    
    public List<MonthlyStockSupplyReq> retrieveNPointForecast(CountryOffice co, Stock stock, int nPoint, int plannedInv) throws ForecastFailureException, InvalidInputException;

    public void saveMonthlyStockSupplyReq(List<Couple<Stock, List<MonthlyStockSupplyReq>>> stockMssrList) throws InvalidMssrException;
    
    public void reviewMonthlyStockSupplyReq(List<Couple<Stock, List<MonthlyStockSupplyReq>>> stockMssrList, boolean approved) throws InvalidMssrException;

    public List<MonthlyStockSupplyReq> retrieveMssrForCoStock(CountryOffice co, Stock stock, int year);
    
    public List<MonthlyStockSupplyReq> retrieveMssrForCoStock(CountryOffice co, Stock stock, Month startMonth, int startYear, Month endMonth, int endYear);
    
    public List<MonthlyStockSupplyReq> retrieveLockedMssrForCoStock(CountryOffice co, Stock stock, int mthsHist) throws IllegalArgumentException;
    
    public List<MonthlyStockSupplyReq> retrieveUnlockedMssrForCoStock(CountryOffice co, Stock stock);

    public List<Integer> getYearsOfMssr(CountryOffice co);

}
