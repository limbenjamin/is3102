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
import IslandFurniture.EJB.Exceptions.MssrLockedException;
import IslandFurniture.StaticClasses.Helper.Couple;
import static IslandFurniture.StaticClasses.Helper.SystemConstants.FORECAST_HORIZON;
import static IslandFurniture.StaticClasses.Helper.SystemConstants.FORECAST_LOCKOUT_MONTHS;
import IslandFurniture.StaticClasses.Helper.TimeMethods;
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
public class SalesForecastBean implements SalesForecastBeanLocal {

    @PersistenceContext(unitName = "IslandFurniture")
    private EntityManager em;

    @Override
    public List<MonthlyStockSupplyReq> generateSalesFigures(CountryOffice co, Month startMonth, int startYear, Month endMonth, int endYear) {
        Calendar start = TimeMethods.getCalFromMonthYear(startMonth, startYear);
        Calendar end = TimeMethods.getCalFromMonthYear(endMonth, endYear);

        MonthlyStockSupplyReq mssr;
        FurnitureTransaction fTrans;
        RetailItemTransaction riTrans;
        Calendar cal;

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
//            DateFormat dateYearFormat = new SimpleDateFormat("MMM yyyy");
//            System.out.println("--------");
//            System.out.println(dateYearFormat.format(start.getTime()));
//            System.out.println("--------");

            // Loops through each transaction and increment qtySold in relevant MSSR
            for (Transaction eachTrans : listOfTrans) {
                // Prints Tansaction to system log
//                System.out.println("Transaction Id: " + eachTrans.getId());
//                System.out.println("Store: " + eachTrans.getStore().getName());
//                System.out.println("Transaction Time: " + TimeMethods.getPlantDefaultTimeString(eachTrans.getStore(), eachTrans.getTransTime()));

                // Convert Calendar raw values to country office's offset value
                cal = TimeMethods.convertToPlantTime(co, eachTrans.getTransTime());

                if (eachTrans instanceof FurnitureTransaction) {
                    fTrans = (FurnitureTransaction) eachTrans;
                    em.refresh(fTrans);

                    for (FurnitureTransactionDetail eachDetail : fTrans.getFurnitureTransactionDetails()) {
//                        System.out.println(eachDetail.getFurnitureModel() + "|" + eachDetail.getQty());

                        mssr = this.addMonthlyStockSupplyReq(eachDetail.getFurnitureModel(), co, Month.getMonth(cal.get(Calendar.MONTH)), cal.get(Calendar.YEAR));
                        mssr.setQtySold(mssr.getQtySold() + eachDetail.getQty());
                    }

                } else if (eachTrans instanceof RetailItemTransaction) {
                    riTrans = (RetailItemTransaction) eachTrans;
                    em.refresh(riTrans);

                    for (RetailItemTransactionDetail eachDetail : riTrans.getRetailItemTransactionDetails()) {
//                        System.out.println(eachDetail.getRetailItem() + "|" + eachDetail.getQty());

                        mssr = this.addMonthlyStockSupplyReq(eachDetail.getRetailItem(), co, Month.getMonth(cal.get(Calendar.MONTH)), cal.get(Calendar.YEAR));
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
    public void saveMonthlyStockSupplyReq(List<MonthlyStockSupplyReq> mssrList) throws MssrLockedException {
        Calendar lockoutCutoff;

        for (MonthlyStockSupplyReq mssr : mssrList) {
            lockoutCutoff = TimeMethods.getPlantCurrTime(mssr.getCountryOffice());
            lockoutCutoff.add(Calendar.MONTH, FORECAST_LOCKOUT_MONTHS);

            if (TimeMethods.getCalFromMonthYear(mssr.getMonth(), mssr.getYear()).compareTo(lockoutCutoff) > 0) {
                mssr.setForecasted(true);
                mssr.setApproved(false);

                em.merge(mssr);
            } else {
                throw new MssrLockedException("Monthly Stock Supply Requirements for " + mssr.getMonth() + " " + mssr.getYear() + " falls within the lockout period and cannot be edited.");
            }
        }
    }

    @Override
    public List<Couple<Stock, List<MonthlyStockSupplyReq>>> retrieveMssrForCo(CountryOffice co, int year) {
        return this.retrieveMssrForCo(co, Month.JAN, year, Month.DEC, year);
    }

    @Override
    public List<Couple<Stock, Couple<List<MonthlyStockSupplyReq>, List<MonthlyStockSupplyReq>>>> retrievePairedMssrForCo(CountryOffice co, int mthsHist)
            throws IllegalArgumentException {
        if (mthsHist < 0) {
            throw new IllegalArgumentException("Number of History MSSR Months must be zero or positive");
        }

        Calendar lockedOutStart = TimeMethods.getPlantCurrTime(co);
        lockedOutStart.add(Calendar.MONTH, mthsHist * -1);

        Calendar lockedOutEnd = TimeMethods.getPlantCurrTime(co);
        lockedOutEnd.add(Calendar.MONTH, FORECAST_LOCKOUT_MONTHS);
        
        Calendar planningStart = TimeMethods.getPlantCurrTime(co);
        planningStart.add(Calendar.MONTH, FORECAST_LOCKOUT_MONTHS+1);

        Calendar planningEnd = TimeMethods.getPlantCurrTime(co);
        planningEnd.add(Calendar.MONTH, FORECAST_HORIZON);

        List<Couple<Stock, Couple<List<MonthlyStockSupplyReq>, List<MonthlyStockSupplyReq>>>> pairedMssrList = new ArrayList();

        for (StockSupplied ss : co.getSuppliedWithFrom()) {

            List<MonthlyStockSupplyReq> lockedMssrList;
            lockedMssrList = this.retrieveMssrForCoStock(co, ss.getStock(), Month.getMonth(lockedOutStart.get(Calendar.MONTH)), lockedOutStart.get(Calendar.YEAR), Month.getMonth(lockedOutEnd.get(Calendar.MONTH)), lockedOutEnd.get(Calendar.YEAR));

            List<MonthlyStockSupplyReq> unlockedMssrList;
            unlockedMssrList = this.retrieveMssrForCoStock(co, ss.getStock(), Month.getMonth(planningStart.get(Calendar.MONTH)), planningStart.get(Calendar.YEAR), Month.getMonth(planningEnd.get(Calendar.MONTH)), planningEnd.get(Calendar.YEAR));

            pairedMssrList.add(new Couple(ss.getStock(), new Couple(lockedMssrList, unlockedMssrList)));
        }

        return pairedMssrList;
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

    @Override
    public List<MonthlyStockSupplyReq> retrieveMssrForCoStock(CountryOffice co, Stock stock, Month startMonth, int startYear, Month endMonth, int endYear) {
        Query q = em.createNamedQuery("getMssrByCoStock");
        q.setParameter("countryOffice", co);
        q.setParameter("stock", stock);
        q.setParameter("startYr", startYear);
        q.setParameter("startMth", startMonth);
        q.setParameter("endYr", endYear);
        q.setParameter("endMth", endMonth);

        List<MonthlyStockSupplyReq> stockMssrList = (List<MonthlyStockSupplyReq>) q.getResultList();

        if (stockMssrList == null) {
            stockMssrList = new ArrayList();
        }

        Calendar start = TimeMethods.getCalFromMonthYear(startMonth, startYear);
        Calendar end = TimeMethods.getCalFromMonthYear(endMonth, endYear);

        while (start.compareTo(end) <= 0) {
            MonthlyStockSupplyReq mssr = new MonthlyStockSupplyReq();
            mssr.setYear(start.get(Calendar.YEAR));
            mssr.setMonth(Month.getMonth(start.get(Calendar.MONTH)));
            mssr.setCountryOffice(co);
            mssr.setStock(stock);

            if (!stockMssrList.contains(mssr)) {
                stockMssrList.add(mssr);
            }

            start.add(Calendar.MONTH, 1);
        }
        
        stockMssrList.sort(null);

        return stockMssrList;
    }

    private List<Transaction> getStoreTransactions(Store store, Month month, int year) {
        Calendar start = TimeMethods.convertToPlantTime(store, TimeMethods.getCalFromMonthYear(month, year));

        Calendar end = TimeMethods.getCalFromMonthYear(month, year);
        end.add(Calendar.MONTH, 1);
        end = TimeMethods.convertToPlantTime(store, end);

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

    private List<Couple<Stock, List<MonthlyStockSupplyReq>>> retrieveMssrForCo(CountryOffice co, Month startMonth, int startYear, Month endMonth, int endYear) {
        List<Couple<Stock, List<MonthlyStockSupplyReq>>> coupleList = new ArrayList();

        for (StockSupplied ss : co.getSuppliedWithFrom()) {
            List<MonthlyStockSupplyReq> stockMssrList = this.retrieveMssrForCoStock(co, ss.getStock(), startMonth, startYear, endMonth, endYear);

            coupleList.add(new Couple(ss.getStock(), stockMssrList));
        }

        return coupleList;
    }

}
