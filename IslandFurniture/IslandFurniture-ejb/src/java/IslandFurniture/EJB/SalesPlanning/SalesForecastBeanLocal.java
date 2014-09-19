/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.SalesPlanning;

import IslandFurniture.EJB.Entities.CountryOffice;
import IslandFurniture.EJB.Entities.Month;
import IslandFurniture.EJB.Entities.MonthlyStockSupplyReq;
import IslandFurniture.EJB.Entities.Stock;
import IslandFurniture.StaticClasses.Helper.Couple;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Local
public interface SalesForecastBeanLocal {

    public void saveMonthlyStockSupplyReq(List<MonthlyStockSupplyReq> mssrList);

    public List<MonthlyStockSupplyReq> generateSalesFigures(CountryOffice co, Month startMonth, int startYear, Month endMonth, int endYear);

    public List<MonthlyStockSupplyReq> retrieveMssrForCoStock(CountryOffice co, Stock stock, Month startMonth, int startYear, Month endMonth, int endYear);

    public List<Couple<Stock, List<MonthlyStockSupplyReq>>> retrieveMssrForCo(CountryOffice co, int year);

    public List<Couple<Stock, List<MonthlyStockSupplyReq>>> retrieveMssrForCo(CountryOffice co, Month startMonth, int startYear, Month endMonth, int endYear);

    public List<Integer> getYearsOfMssr(CountryOffice co);

    public Stock getStockById(long stockId);

}
