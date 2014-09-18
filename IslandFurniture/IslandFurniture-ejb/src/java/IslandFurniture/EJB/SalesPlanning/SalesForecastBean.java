/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.SalesPlanning;

import IslandFurniture.EJB.Entities.CountryOffice;
import IslandFurniture.EJB.Entities.FurnitureTransaction;
import IslandFurniture.EJB.Entities.FurnitureTransactionDetail;
import IslandFurniture.EJB.Entities.Month;
import IslandFurniture.EJB.Entities.MonthlyStockSupplyReq;
import IslandFurniture.EJB.Entities.MonthlyStockSupplyReqPK;
import IslandFurniture.EJB.Entities.RetailItemTransaction;
import IslandFurniture.EJB.Entities.RetailItemTransactionDetail;
import IslandFurniture.EJB.Entities.Stock;
import IslandFurniture.EJB.Entities.StockSupplied;
import IslandFurniture.EJB.Entities.Store;
import IslandFurniture.EJB.Entities.Transaction;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
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
public class SalesForecastBean implements SalesForecastBeanLocal {

    @PersistenceContext(unitName = "IslandFurniture")
    private EntityManager em;
    private TimeZone TimeZone;

    private List<Transaction> getStoreTransactions(Store store, Month month, int year) {
        TimeZone tz = TimeZone.getTimeZone(store.getTimeZoneID());
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();

        start.set(year, month.value, 1, 0, 0, 0);
        start.set(Calendar.MILLISECOND, 0);
        start.add(Calendar.MILLISECOND, tz.getRawOffset() * -1);

        end.set(year, month.value, 1, 0, 0, 0);
        end.add(Calendar.MONTH, 1);
        end.set(Calendar.MILLISECOND, 0);
        end.add(Calendar.MILLISECOND, -1);
        end.add(Calendar.MILLISECOND, tz.getRawOffset() * -1);

        Query q = em.createNamedQuery("getStoreTransactions");
        q.setParameter("store", store);
        q.setParameter("startDate", start, TemporalType.TIMESTAMP);
        q.setParameter("endDate", end, TemporalType.TIMESTAMP);

        return (List<Transaction>) q.getResultList();
    }

    private MonthlyStockSupplyReq addMonthlyStockSupplyReq(Stock stock, CountryOffice co, Month month, int year) {
        MonthlyStockSupplyReqPK mssrPK = new MonthlyStockSupplyReqPK(stock.getId(), co.getId(), month, year);

        MonthlyStockSupplyReq mssr = em.find(MonthlyStockSupplyReq.class, mssrPK);

        if (mssr == null) {
            mssr = new MonthlyStockSupplyReq();
            mssr.setStock(stock);
            mssr.setCountryOffice(co);
            mssr.setMonth(month);
            mssr.setYear(year);

            em.persist(mssr);

            co.getMonthlyStockSupplyReqs().add(mssr);
            stock.getMonthlyStockSupplyReqs().add(mssr);
        }

        return mssr;
    }

    @Override
    public List<MonthlyStockSupplyReq> generateSalesFigures(CountryOffice co, Month startMonth, int startYear, Month endMonth, int endYear) {
        Calendar start = Calendar.getInstance();
        start.set(startYear, startMonth.value, 1);

        Calendar end = Calendar.getInstance();
        end.set(endYear, endMonth.value, 1);

        MonthlyStockSupplyReq mssr;
        FurnitureTransaction fTrans;
        RetailItemTransaction riTrans;
        Calendar cal;
        TimeZone tz = TimeZone.getTimeZone(co.getTimeZoneID());

        while (start.compareTo(end) <= 0) {
            for (StockSupplied eachStockSupplied : co.getSuppliedWithFrom()) {
                mssr = this.addMonthlyStockSupplyReq(eachStockSupplied.getStock(), co, Month.getMonth(start.get(Calendar.MONTH)), start.get(Calendar.YEAR));

                // Reset all qtySold to prevent duplicate counting
                mssr.setQtySold(0);
            }

            // Grab list of transactions in given store in a month
            List<Transaction> listOfTrans = new ArrayList();
            for (Store eachStore : co.getStores()) {
                listOfTrans.addAll(this.getStoreTransactions(eachStore, Month.getMonth(start.get(Calendar.MONTH)), start.get(Calendar.YEAR)));
            }

            // Display all transactions grabbed in the month
            DateFormat dateYearFormat = new SimpleDateFormat("MMM yyyy");
            System.out.println("--------");
            System.out.println(dateYearFormat.format(start.getTime()));
            System.out.println("--------");

            // Loops through each transaction and increment qtySold in relevant MSSR
            for (Transaction eachTrans : listOfTrans) {
                System.out.println("Transaction Id: " + eachTrans.getId());
                System.out.println("Store: " + eachTrans.getStore().getName());
                DateFormat timeFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
                timeFormat.setTimeZone(TimeZone.getTimeZone(eachTrans.getStore().getTimeZoneID()));
                System.out.println("Transaction Date: " + timeFormat.format(eachTrans.getTransTime().getTime()) + ", " + eachTrans.getStore().getTimeZoneID() + " time");

                // Convert Calendar raw values to country office's offset value
                cal = eachTrans.getTransTime();
                cal.add(Calendar.MILLISECOND, tz.getRawOffset());

                if (eachTrans instanceof FurnitureTransaction) {
                    fTrans = (FurnitureTransaction) eachTrans;
                    em.refresh(fTrans);

                    for (FurnitureTransactionDetail eachDetail : fTrans.getFurnitureTransactionDetails()) {

                        mssr = this.addMonthlyStockSupplyReq(eachDetail.getFurnitureModel(), co, Month.getMonth(cal.get(Calendar.MONTH)), cal.get(Calendar.YEAR));

                        System.out.println(eachDetail.getFurnitureModel() + "|" + eachDetail.getQty());

                        mssr.setQtySold(mssr.getQtySold() + eachDetail.getQty());

                    }

                } else if (eachTrans instanceof RetailItemTransaction) {
                    riTrans = (RetailItemTransaction) eachTrans;
                    em.refresh(riTrans);

                    for (RetailItemTransactionDetail eachDetail : riTrans.getRetailItemTransactionDetails()) {
                        mssr = this.addMonthlyStockSupplyReq(eachDetail.getRetailItem(), co, Month.getMonth(cal.get(Calendar.MONTH)), cal.get(Calendar.YEAR));

                        System.out.println(eachDetail.getRetailItem() + "|" + eachDetail.getQty());

                        mssr.setQtySold(mssr.getQtySold() + eachDetail.getQty());
                    }
                }
            }

            // Move on to next month
            start.add(Calendar.MONTH, 1);
        }

        return null;
    }

    @Override
    public List<MonthlyStockSupplyReq> retrieveMssrForCoStock(CountryOffice co, Stock stock, Month startMonth, int startYear, Month endMonth, int endYear) {
        Query q = em.createNamedQuery("getMssrByCoStock");
        q.setParameter("countryOffice", co);
        q.setParameter("stock", stock);
        q.setParameter("startYr", startYear);
        q.setParameter("startMth", startMonth);
        q.setParameter("endYr", endYear);
        q.setParameter("endMth", endMonth);

        return (List<MonthlyStockSupplyReq>) q.getResultList();
    }

    @Override
    public Map<Stock, List<MonthlyStockSupplyReq>> retrieveMssrForCo(CountryOffice co, int year) {
        return this.retrieveMssrForCo(co, Month.JAN, year, Month.DEC, year);
    }

    @Override
    public Map<Stock, List<MonthlyStockSupplyReq>> retrieveMssrForCo(CountryOffice co, Month startMonth, int startYear, Month endMonth, int endYear) {
        Map<Stock, List<MonthlyStockSupplyReq>> mssrMap = new HashMap();

        for (StockSupplied ss : co.getSuppliedWithFrom()) {
            List<MonthlyStockSupplyReq> stockMssrList = this.retrieveMssrForCoStock(co, ss.getStock(), startMonth, startYear, endMonth, endYear);

            if (stockMssrList != null) {
                Calendar start = Calendar.getInstance();
                start.set(startYear, startMonth.value, 1);

                Calendar end = Calendar.getInstance();
                end.set(endYear, endMonth.value, 1);

                while (start.compareTo(end) <= 0) {
                    MonthlyStockSupplyReq mssr = new MonthlyStockSupplyReq();
                    mssr.setYear(start.get(Calendar.YEAR));
                    mssr.setMonth(Month.getMonth(start.get(Calendar.MONTH)));
                    mssr.setCountryOffice(co);
                    mssr.setStock(ss.getStock());

                    if (!stockMssrList.contains(mssr)) {
                        stockMssrList.add(mssr);
                    }

                    start.add(Calendar.MONTH, 1);
                }

                stockMssrList.sort(null);
            }

            mssrMap.put(ss.getStock(), stockMssrList);
        }

        return mssrMap;
    }

    @Override
    public List<Integer> getYearsOfMssr(CountryOffice co) {
        Query q = em.createNamedQuery("getMssrByCO");
        q.setParameter("countryOffice", co);

        List<Integer> yearsOfMssr = new ArrayList();
        for (MonthlyStockSupplyReq mssr : (List<MonthlyStockSupplyReq>) q.getResultList()) {
            if (!yearsOfMssr.contains(mssr.getYear())) {
                yearsOfMssr.add(mssr.getYear());
            }
        }
        yearsOfMssr.sort(null);

        return yearsOfMssr;
    }

}
