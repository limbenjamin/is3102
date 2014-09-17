/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.StaticClasses.Helper;

import IslandFurniture.EJB.Entities.CountryOffice;
import IslandFurniture.EJB.Entities.Month;
import IslandFurniture.EJB.Entities.MonthlyStockSupplyReq;
import IslandFurniture.EJB.Entities.StockSupplied;
import IslandFurniture.EJB.SalesPlanning.SalesForecastBeanLocal;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Stateless
public class LoadSalesForecastBean implements LoadSalesForecastBeanRemote {

    @PersistenceContext(unitName = "IslandFurniture")
    private EntityManager em;
    @EJB
    private SalesForecastBeanLocal salesForecastBean;

    @Override
    public boolean loadSampleData() {
//        try {
            Calendar curr;
            Random rand = new Random(1);
            List<CountryOffice> countryOffices = (List<CountryOffice>) em.createNamedQuery("getAllCountryOffices").getResultList();

            for (CountryOffice eachCo : countryOffices) {
                curr = Calendar.getInstance(TimeZone.getTimeZone(eachCo.getTimeZoneID()));
                curr.add(Calendar.MONTH, -1);
                Month prevMth = Month.getMonth(curr.get(Calendar.MONTH));

                System.out.println(eachCo);
                salesForecastBean.generateSalesFigures(eachCo, Month.JAN, 2013, prevMth, curr.get(Calendar.YEAR));

                for (StockSupplied ss : eachCo.getSuppliedWithFrom()) {
                    List<MonthlyStockSupplyReq> listOfMssr = salesForecastBean.retrieveMssrForCoStock(eachCo, ss.getStock(), Month.JAN, 2013, prevMth, curr.get(Calendar.YEAR));
                    if (listOfMssr != null) {
                        listOfMssr.sort(null);

                        for (int i = 0; i < listOfMssr.size(); i++) {
                            MonthlyStockSupplyReq eachMssr = listOfMssr.get(i);

                            eachMssr.setQtyForecasted(eachMssr.getQtySold() + rand.nextInt(200) - 100);
                            eachMssr.setPlannedInventory(rand.nextInt(6) * 50);

                            eachMssr.setActualInventory(eachMssr.getQtyForecasted() + eachMssr.getPlannedInventory() - eachMssr.getQtySold());

                            if (i >= 3) {
                                MonthlyStockSupplyReq oldMssr = listOfMssr.get(i - 3);
                                eachMssr.setVarianceOffset(oldMssr.getQtyForecasted() - oldMssr.getQtySold());
                            }

                            if (i >= 1) {
                                MonthlyStockSupplyReq oldMssr = listOfMssr.get(i - 1);
                                eachMssr.setQtyRequested(eachMssr.getQtyForecasted() + eachMssr.getPlannedInventory() - oldMssr.getPlannedInventory());
                            }

                            System.out.println(eachMssr);
                        }
                    }
                }
            }

            return true;
//        } catch (Exception ex) {
//            System.out.println(ex.getMessage());
//
//            return false; // Stub - incomplete function
//        }
    }

}
