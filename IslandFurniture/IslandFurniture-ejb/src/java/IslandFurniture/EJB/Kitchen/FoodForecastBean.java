/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.Kitchen;

import IslandFurniture.Entities.MenuItem;
import IslandFurniture.Entities.MonthlyMenuItemSalesForecast;
import IslandFurniture.Entities.MonthlyMenuItemSalesForecastPK;
import IslandFurniture.Entities.RestaurantTransaction;
import IslandFurniture.Entities.RestaurantTransactionDetail;
import IslandFurniture.Entities.Store;
import IslandFurniture.Enums.MmsfStatus;
import IslandFurniture.Enums.Month;
import IslandFurniture.StaticClasses.TimeMethods;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

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

    @Override
    public void updateMonthlyMenuItemSalesForecast(Store store, Month startMth, int startYr, Month endMth, int endYr) {
        Calendar start = TimeMethods.getCalFromMonthYear(startMth, startYr);
        Calendar end = TimeMethods.getCalFromMonthYear(endMth, endYr);
        Calendar prevMth = TimeMethods.getPlantCurrTime(store);
        prevMth.add(Calendar.MONTH, -1);

        MonthlyMenuItemSalesForecast mmsf;

        while (start.compareTo(end) <= 0 && start.compareTo(prevMth) < 0) {

            // Get Sales figures in this month
            this.generateSalesFigures(store, Month.getMonth(start.get(Calendar.MONTH)), start.get(Calendar.YEAR));

            // Set Inventory and Variance Offset for this month
            for (MenuItem eachMi : store.getCountryOffice().getMenuItems()) {
                mmsf = this.addMonthlyMenuItemSalesForecast(eachMi, store, Month.getMonth(start.get(Calendar.MONTH)), start.get(Calendar.YEAR));
                mmsf.setEndMthUpdated(true);
            }

            // Move on to next month
            start.add(Calendar.MONTH, 1);
        }
    }

    public void generateSalesFigures(Store store, Month month, int year) {
        MonthlyMenuItemSalesForecast mmsf;
        Calendar cal;

        // Reset all Quantities sold to zero
        for (MenuItem mi : store.getCountryOffice().getMenuItems()) {
            mmsf = this.addMonthlyMenuItemSalesForecast(mi, store, month, year);

            mmsf.setQtySold(0);
        }

        // Grab list of transactions in given store in a month
        List<RestaurantTransaction> listOfTrans = this.getStoreRestTrans(store, month, year);

        // Display all transactions grabbed in the month
//            DateFormat dateYearFormat = new SimpleDateFormat("MMM yyyy");
//            System.out.println("--------");
//            System.out.println(dateYearFormat.format(start.getTime()));
//            System.out.println("--------");
        // Loops through each transaction and increment qtySold in relevant MSSR
        for (RestaurantTransaction eachTrans : listOfTrans) {
                // Prints Tansaction to system log
//                System.out.println("Transaction Id: " + eachTrans.getId());
//                System.out.println("Store: " + eachTrans.getStore().getName());
//                System.out.println("Transaction Time: " + TimeMethods.getPlantDefaultTimeString(eachTrans.getStore(), eachTrans.getTransTime()));

            // Convert Calendar raw values to country office's offset value
            cal = TimeMethods.convertToPlantTime(store, eachTrans.getTransTime());

            // Add items from transaction to the relevant MMSF
            em.refresh(eachTrans);

            for (RestaurantTransactionDetail eachDetail : eachTrans.getRestaurantTransactionDetails()) {
//                        System.out.println(eachDetail.getFurnitureModel() + "|" + eachDetail.getQty());

                mmsf = this.addMonthlyMenuItemSalesForecast(eachDetail.getMenuItem(), store, Month.getMonth(cal.get(Calendar.MONTH)), cal.get(Calendar.YEAR));
                mmsf.setQtySold(mmsf.getQtySold() + eachDetail.getQty());
            }
        }
    }

    private MonthlyMenuItemSalesForecast addMonthlyMenuItemSalesForecast(MenuItem mi, Store store, Month month, int year) {
        MonthlyMenuItemSalesForecastPK mmsfPK = new MonthlyMenuItemSalesForecastPK(mi.getId(), store.getId(), month, year);

        MonthlyMenuItemSalesForecast mmsf = em.find(MonthlyMenuItemSalesForecast.class, mmsfPK);

        if (mmsf == null) {
            mmsf = new MonthlyMenuItemSalesForecast();
            mmsf.setMenuItem(mi);
            mmsf.setStore(store);
            mmsf.setMonth(month);
            mmsf.setYear(year);
            mmsf.setStatus(MmsfStatus.NONE);

            em.persist(mmsf);

            store.getMonthlyMenuItemSalesForecasts().add(mmsf);
        }

        return mmsf;
    }

    private List<RestaurantTransaction> getStoreRestTrans(Store store, Month month, int year) {
        Calendar start = TimeMethods.convertToPlantTime(store, TimeMethods.getCalFromMonthYear(month, year));

        Calendar end = TimeMethods.getCalFromMonthYear(month, year);
        end.add(Calendar.MONTH, 1);
        end = TimeMethods.convertToPlantTime(store, end);

        Query q = em.createNamedQuery("getStoreRestTrans");
        q.setParameter("store", store);
        q.setParameter("startDate", start, TemporalType.TIMESTAMP);
        q.setParameter("endDate", end, TemporalType.TIMESTAMP);

        return (List<RestaurantTransaction>) q.getResultList();
    }
}
