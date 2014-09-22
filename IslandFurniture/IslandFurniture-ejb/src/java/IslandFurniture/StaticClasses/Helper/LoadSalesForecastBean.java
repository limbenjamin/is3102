/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.StaticClasses.Helper;

import IslandFurniture.EJB.Entities.CountryOffice;
import IslandFurniture.EJB.Entities.Month;
import IslandFurniture.EJB.Entities.MonthlyStockSupplyReq;
import IslandFurniture.EJB.Entities.MssrStatus;
import IslandFurniture.EJB.Entities.StockSupplied;
import IslandFurniture.EJB.SalesPlanning.SalesForecastBeanLocal;
import static IslandFurniture.StaticClasses.Helper.SystemConstants.FORECAST_LOCKOUT_MONTHS;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
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
        Calendar prevMth;
        Calendar lockedOutCutoff;

        Random rand = new Random(1);
        List<CountryOffice> countryOffices = (List<CountryOffice>) em.createNamedQuery("getAllCountryOffices").getResultList();

        for (CountryOffice eachCo : countryOffices) {
            prevMth = TimeMethods.getPlantCurrTime(eachCo);
            prevMth.add(Calendar.MONTH, -1);
            
            lockedOutCutoff = TimeMethods.getPlantCurrTime(eachCo);
            lockedOutCutoff.add(Calendar.MONTH, FORECAST_LOCKOUT_MONTHS);

            System.out.println(eachCo);
            salesForecastBean.updateMonthlyStockSupplyReq(eachCo, Month.JAN, 2013, Month.getMonth(prevMth.get(Calendar.MONTH)), prevMth.get(Calendar.YEAR));

            for (StockSupplied ss : eachCo.getSuppliedWithFrom()) {
                List<MonthlyStockSupplyReq> listOfMssr = salesForecastBean.retrieveMssrForCoStock(eachCo, ss.getStock(), Month.JAN, 2013, Month.getMonth(lockedOutCutoff.get(Calendar.MONTH)), lockedOutCutoff.get(Calendar.YEAR));
                listOfMssr.sort(null);

                for (int i = 0; i < listOfMssr.size(); i++) {
                    MonthlyStockSupplyReq eachMssr = listOfMssr.get(i);

                    eachMssr.setPlannedInventory(rand.nextInt(6) * 50);

                    if (i < listOfMssr.size() - FORECAST_LOCKOUT_MONTHS) {
                        do {
                            eachMssr.setQtyForecasted(eachMssr.getQtySold() + rand.nextInt(200) - 100);
                        } while (eachMssr.getQtyForecasted() + eachMssr.getPlannedInventory() - eachMssr.getQtySold() < 0);
                        
                        eachMssr.setActualInventory(eachMssr.getQtyForecasted() + eachMssr.getPlannedInventory() - eachMssr.getQtySold());
                    } else {
                        eachMssr.setQtyForecasted(500 + rand.nextInt(500));
                    }

                    if (i >= FORECAST_LOCKOUT_MONTHS + 1) {
                        MonthlyStockSupplyReq oldMssr = listOfMssr.get(i - (FORECAST_LOCKOUT_MONTHS + 1));
                        eachMssr.setVarianceOffset(oldMssr.getQtyForecasted() - oldMssr.getQtySold());
                        eachMssr.setVarianceUpdated(true);
                    }

                    if (i >= 1) {
                        MonthlyStockSupplyReq oldMssr = listOfMssr.get(i - 1);
                        eachMssr.setQtyRequested(eachMssr.getQtyForecasted() + eachMssr.getPlannedInventory() - eachMssr.getVarianceOffset() - oldMssr.getPlannedInventory());
                    } else {
                        eachMssr.setQtyRequested(eachMssr.getQtyForecasted() + eachMssr.getPlannedInventory());
                    }

                    eachMssr.setApproved(true);
                    eachMssr.setStatus(MssrStatus.APPROVED);

                    System.out.println(eachMssr);
                }
            }
        }
        return true;
    }

}
