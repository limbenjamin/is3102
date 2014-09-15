/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.StaticClasses.Helper;

import IslandFurniture.EJB.Entities.Month;
import IslandFurniture.EJB.Entities.MonthlyStockSupplyReq;
import IslandFurniture.EJB.Entities.StockSupplied;
import IslandFurniture.EJB.Entities.Store;
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
        try {
            Calendar curr;
            Random rand = new Random(1);
            List<Store> stores = (List<Store>) em.createNamedQuery("getAllStores").getResultList();

            for (Store eachStore : stores) {
                curr = Calendar.getInstance(TimeZone.getTimeZone(eachStore.getCountry().getTimeZoneID()));
                curr.add(Calendar.MONTH, -1);
                Month prevMth = Month.getMonth(curr.get(Calendar.MONTH));

                System.out.println(eachStore);
                salesForecastBean.generateSalesFigures(eachStore, Month.JAN, 2013, prevMth, curr.get(Calendar.YEAR));

                for (StockSupplied ss : eachStore.getSuppliedWithFrom()) {
                    List<MonthlyStockSupplyReq> listOfMssr = salesForecastBean.retrieveMssrForStoreStock(eachStore, ss.getStock(), Month.JAN, 2013, prevMth, curr.get(Calendar.YEAR));
                    listOfMssr.sort(null);

                    int prevExcessInv = 0;
                    int prevIdealInv = 0;

                    for (MonthlyStockSupplyReq eachMssr: listOfMssr) {
                        eachMssr.setIdealInventory(100);
                        eachMssr.setQtyForecasted(eachMssr.getQtySold() + rand.nextInt(200) - 100);

                        eachMssr.setBeginInventory(prevExcessInv + prevIdealInv);
                        eachMssr.setQtyRequested(eachMssr.getQtyForecasted() + eachMssr.getIdealInventory() - eachMssr.getBeginInventory());
                        
                        prevExcessInv = eachMssr.getQtyForecasted() - eachMssr.getQtySold();
                        prevIdealInv = eachMssr.getIdealInventory();

                        System.out.println(eachMssr);
                    }
                }
            }

            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());

            return false; // Stub - incomplete function
        }
    }

}
