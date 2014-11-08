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
import IslandFurniture.StaticClasses.TimeMethods;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Stateless
public class FoodForecastBean implements FoodForecastBeanLocal {
    @PersistenceContext(unitName = "IslandFurniture")
    private EntityManager em;

    @Override
    public List<Integer> getYearsOfMmsf(Store store) {
        Query q = em.createNamedQuery("getMmsfByStore");
        q.setParameter("store", store);

        List<Integer> yearsOfMmsf = new ArrayList();
        for (MonthlyMenuItemSalesForecast mmsf : (List<MonthlyMenuItemSalesForecast>) q.getResultList()) {
            if (!yearsOfMmsf.contains(mmsf.getYear())) {
                yearsOfMmsf.add(mmsf.getYear());
            }
        }
        yearsOfMmsf.sort(null);

        return yearsOfMmsf;
    }

    @Override
    public List<MonthlyMenuItemSalesForecast> retrieveMmsfForStoreMi(Store store, MenuItem menuItem, Integer year) {
        return retrieveMmsfForStoreMi(store, menuItem, Month.getMonth(0), year, Month.getMonth(11), year);
    }

    @Override
    public List<MonthlyMenuItemSalesForecast> retrieveMmsfForStoreMi(Store store, MenuItem menuItem, Month startMonth, int startYear, Month endMonth, int endYear) {
        Query q = em.createNamedQuery("getMmsfByStoreMi");
        q.setParameter("store", store);
        q.setParameter("menuItem", menuItem);
        q.setParameter("startYr", startYear);
        q.setParameter("startMth", startMonth);
        q.setParameter("endYr", endYear);
        q.setParameter("endMth", endMonth);

        List<MonthlyMenuItemSalesForecast> miMmsfList = (List<MonthlyMenuItemSalesForecast>) q.getResultList();

        if (miMmsfList == null) {
            miMmsfList = new ArrayList();
        }

        Calendar start = TimeMethods.getCalFromMonthYear(startMonth, startYear);
        Calendar end = TimeMethods.getCalFromMonthYear(endMonth, endYear);

        // Adds new instances of null status MSSR to fill any gaps
        while (start.compareTo(end) <= 0) {
            MonthlyMenuItemSalesForecast mmsf = new MonthlyMenuItemSalesForecast();
            mmsf.setYear(start.get(Calendar.YEAR));
            mmsf.setMonth(Month.getMonth(start.get(Calendar.MONTH)));
            mmsf.setStore(store);
            mmsf.setMenuItem(menuItem);

            if (!miMmsfList.contains(mmsf)) {
                miMmsfList.add(mmsf);
            }

            start.add(Calendar.MONTH, 1);
        }

        miMmsfList.sort(null);

        return miMmsfList;
    }
    
    
}
