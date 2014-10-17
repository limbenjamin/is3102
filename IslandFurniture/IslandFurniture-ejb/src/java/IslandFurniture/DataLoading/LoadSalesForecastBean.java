/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.DataLoading;

import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Enums.Month;
import IslandFurniture.Entities.MonthlyStockSupplyReq;
import IslandFurniture.Enums.MssrStatus;
import IslandFurniture.Entities.StockSupplied;
import IslandFurniture.EJB.SalesPlanning.SalesForecastBeanLocal;
import IslandFurniture.StaticClasses.TimeMethods;
import static IslandFurniture.StaticClasses.SystemConstants.FORECAST_LOCKOUT_MONTHS;
import java.util.Calendar;
import java.util.Iterator;
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

                    // Update Quantity Forecasted and Actual Inventories
                    if (i < listOfMssr.size() - (FORECAST_LOCKOUT_MONTHS + 1)) {
                        do {
                            eachMssr.setQtyForecasted(eachMssr.getQtySold() + rand.nextInt(10)*10 - 50);
                        } while (eachMssr.getQtyForecasted() + eachMssr.getPlannedInventory() - eachMssr.getQtySold() < 0);

                        eachMssr.setActualInventory(eachMssr.getQtyForecasted() + eachMssr.getPlannedInventory() - eachMssr.getQtySold());
                    } else {
                        eachMssr.setQtyForecasted(500 + rand.nextInt(500));
                    }

                    // Update Variances
                    if (i >= FORECAST_LOCKOUT_MONTHS + 2) {
                        MonthlyStockSupplyReq oldMssr = listOfMssr.get(i - (FORECAST_LOCKOUT_MONTHS + 2));
                        if (oldMssr.isEndMthUpdated()) {
                            eachMssr.setVarianceOffset(oldMssr.getQtyForecasted() - oldMssr.getQtySold());
                            eachMssr.setVarianceUpdated(true);
                        }
                    }

                    // Update Quantity Requested
                    if (i >= 1) {
                        MonthlyStockSupplyReq oldMssr = listOfMssr.get(i - 1);
                        eachMssr.setQtyRequested(eachMssr.getQtyForecasted() + eachMssr.getPlannedInventory() - eachMssr.getVarianceOffset() - oldMssr.getPlannedInventory());
                    } else {
                        eachMssr.setQtyRequested(eachMssr.getQtyForecasted() + eachMssr.getPlannedInventory());
                    }

                    eachMssr.setStatus(MssrStatus.APPROVED);

                    System.out.println(eachMssr);
                }

                // Remove the quantity sold and actualInventory for demonstration
                Calendar mthOfDel = TimeMethods.getCalFromMonthYear(Month.MAR, 2014);
                listOfMssr = salesForecastBean.retrieveMssrForCoStock(eachCo, ss.getStock(), Month.getMonth(mthOfDel.get(Calendar.MONTH)), mthOfDel.get(Calendar.YEAR), Month.getMonth(lockedOutCutoff.get(Calendar.MONTH)), lockedOutCutoff.get(Calendar.YEAR));

                for (int i = 0; i < listOfMssr.size(); i++) {
                    MonthlyStockSupplyReq mssr = listOfMssr.get(i);
                    mssr.setQtySold(0);
                    mssr.setActualInventory(0);
                    mssr.setEndMthUpdated(false);
                }

                // Remove the varianceOffset for demonstration
                mthOfDel.add(Calendar.MONTH, FORECAST_LOCKOUT_MONTHS + 1);
                Calendar noVarEnd = TimeMethods.getCalFromMonthYear(Month.getMonth(lockedOutCutoff.get(Calendar.MONTH)), lockedOutCutoff.get(Calendar.YEAR));
                noVarEnd.add(Calendar.MONTH, 1);
                listOfMssr = salesForecastBean.retrieveMssrForCoStock(eachCo, ss.getStock(), Month.getMonth(mthOfDel.get(Calendar.MONTH)), mthOfDel.get(Calendar.YEAR), Month.getMonth(noVarEnd.get(Calendar.MONTH)), noVarEnd.get(Calendar.YEAR));
                listOfMssr.sort(null);

                MonthlyStockSupplyReq oldMssr = null;
                for (Iterator itr = listOfMssr.iterator(); itr.hasNext();) {
                    MonthlyStockSupplyReq mssr = (MonthlyStockSupplyReq) itr.next();
                    mssr.setVarianceOffset(0);
                    mssr.setVarianceUpdated(false);

                    // Reset Qty Requested
                    if (oldMssr != null) {
                        mssr.setQtyRequested(mssr.getQtyForecasted() + mssr.getPlannedInventory() - mssr.getVarianceOffset() - oldMssr.getPlannedInventory());
                    } else {
                        mssr.setQtyRequested(mssr.getQtyForecasted() + mssr.getPlannedInventory() - mssr.getVarianceOffset());
                    }

                    if (mssr.getStatus() != null && mssr.getStatus().equals(MssrStatus.NONE)) {
                        em.remove(mssr);
                    }

                    oldMssr = mssr;
                }
            }
        }
        return true;
    }

}
