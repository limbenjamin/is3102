/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.SalesPlanning;

import IslandFurniture.EJB.Exceptions.ForecastFailureException;
import IslandFurniture.EJB.Entities.CountryOffice;
import IslandFurniture.EJB.Entities.FurnitureTransaction;
import IslandFurniture.EJB.Entities.FurnitureTransactionDetail;
import IslandFurniture.EJB.Entities.Month;
import IslandFurniture.EJB.Entities.MonthlyStockSupplyReq;
import IslandFurniture.EJB.Entities.MonthlyStockSupplyReqPK;
import IslandFurniture.EJB.Entities.MssrStatus;
import IslandFurniture.EJB.Entities.RetailItemTransaction;
import IslandFurniture.EJB.Entities.RetailItemTransactionDetail;
import IslandFurniture.EJB.Entities.Stock;
import IslandFurniture.EJB.Entities.StockSupplied;
import IslandFurniture.EJB.Entities.Store;
import IslandFurniture.EJB.Entities.Transaction;
import IslandFurniture.EJB.Exceptions.InvalidMssrException;
import IslandFurniture.StaticClasses.Helper.Couple;
import IslandFurniture.StaticClasses.Helper.QueryMethods;
import static IslandFurniture.StaticClasses.Helper.SystemConstants.FORECAST_HORIZON;
import static IslandFurniture.StaticClasses.Helper.SystemConstants.FORECAST_LOCKOUT_MONTHS;
import IslandFurniture.StaticClasses.Helper.TimeMethods;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.REQUIRED;
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

    public void generateSalesFigures(CountryOffice co, Month month, int year) {
        MonthlyStockSupplyReq mssr;
        FurnitureTransaction fTrans;
        RetailItemTransaction riTrans;
        Calendar cal;

        // Reset all Quantities sold to zero
        for (StockSupplied eachStockSupplied : co.getSuppliedWithFrom()) {
            mssr = this.addMonthlyStockSupplyReq(eachStockSupplied.getStock(), co, month, year);

            mssr.setQtySold(0);
        }

        // Grab list of transactions in given store in a month
        List<Transaction> listOfTrans = new ArrayList();
        for (Store eachStore : co.getStores()) {
            listOfTrans.addAll(this.getStoreTransactions(eachStore, month, year));
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

            // Add items from transaction to the relevant MSSR
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
    }

    @Override
    public void updateMonthlyStockSupplyReq(CountryOffice co, Month startMonth, int startYear, Month endMonth, int endYear) {
        Calendar start = TimeMethods.getCalFromMonthYear(startMonth, startYear);
        Calendar end = TimeMethods.getCalFromMonthYear(endMonth, endYear);
        Calendar prevMth = TimeMethods.getPlantCurrTime(co);
        prevMth.add(Calendar.MONTH, -1);

        Calendar varCal;

        MonthlyStockSupplyReq mssr;

        while (start.compareTo(end) <= 0 && start.compareTo(prevMth) < 0) {

            // Get Sales figures in this month
            this.generateSalesFigures(co, Month.getMonth(start.get(Calendar.MONTH)), start.get(Calendar.YEAR));

            // Set Inventory and Variance Offset for this month
            for (StockSupplied eachStockSupplied : co.getSuppliedWithFrom()) {
                mssr = this.addMonthlyStockSupplyReq(eachStockSupplied.getStock(), co, Month.getMonth(start.get(Calendar.MONTH)), start.get(Calendar.YEAR));
                mssr.setEndMthUpdated(true);

                // Get inventory levels at Country Office - Stub
                // Account for variance offset
                varCal = TimeMethods.getCalFromMonthYear(Month.getMonth(start.get(Calendar.MONTH)), start.get(Calendar.YEAR));
                varCal.add(Calendar.MONTH, FORECAST_LOCKOUT_MONTHS + 2);
                MonthlyStockSupplyReq futureMssr = this.addMonthlyStockSupplyReq(eachStockSupplied.getStock(), co, Month.getMonth(varCal.get(Calendar.MONTH)), varCal.get(Calendar.YEAR));

                futureMssr.setVarianceOffset(mssr.getQtyForecasted() - mssr.getQtySold());
                futureMssr.setVarianceUpdated(true);
            }

            // Move on to next month
            start.add(Calendar.MONTH, 1);
        }
    }

    @Override
    public List<MonthlyStockSupplyReq> retrieveNaiveForecast(CountryOffice co, Stock stock) throws ForecastFailureException {
        boolean impacted = false;

        List<MonthlyStockSupplyReq> unlockedMssrList;
        unlockedMssrList = this.retrieveUnlockedMssrForCoStock(co, stock);

        for (int i = 0; i < unlockedMssrList.size(); i++) {
            MonthlyStockSupplyReq mssr = unlockedMssrList.get(i);
            MonthlyStockSupplyReq oldMssr = QueryMethods.findNextMssr(em, mssr, -12);
            MonthlyStockSupplyReq prevMssr;

            if (i <= 0) {
                prevMssr = QueryMethods.findNextMssr(em, mssr, -1);
            } else {
                prevMssr = unlockedMssrList.get(i - 1);
            }

            if (oldMssr == null || prevMssr == null) {
                throw new ForecastFailureException("Previous forecasted values do not exist!");
            }

            if (mssr.getStatus() != MssrStatus.APPROVED) {
                em.detach(mssr);
                mssr.setQtyForecasted(oldMssr.getQtyForecasted());
                mssr.setPlannedInventory(oldMssr.getPlannedInventory());
                mssr.setQtyRequested(mssr.getQtyForecasted() + mssr.getPlannedInventory() - prevMssr.getPlannedInventory() - mssr.getVarianceOffset());
                impacted = true;
            }
        }

        if (!impacted) {
            throw new ForecastFailureException("There are no available months of forecast!");
        }

        return unlockedMssrList;
    }

    @Override
    public List<MonthlyStockSupplyReq> retrieveNPointForecast(CountryOffice co, Stock stock, int nPoint, int plannedInv)
            throws ForecastFailureException, IllegalArgumentException {
        if (nPoint <= 0 || plannedInv <= 0) {
            throw new IllegalArgumentException("Please enter a valid N-Point and Planned Inventory count.");
        }
        boolean impacted = false;

        List<MonthlyStockSupplyReq> unlockedMssrList = this.retrieveUnlockedMssrForCoStock(co, stock);
        List<MonthlyStockSupplyReq> lockedMssrList = this.retrieveLockedMssrForCoStock(co, stock, nPoint);

        System.out.println(lockedMssrList.size());

        for (int i = 0; i < unlockedMssrList.size(); i++) {
            MonthlyStockSupplyReq mssr = unlockedMssrList.get(i);
            MonthlyStockSupplyReq prevMssr;
            int sum = 0;
            int count = 0;

            for (int j = i; j < lockedMssrList.size(); j++) {
                if (lockedMssrList.get(j).getStatus() != null && lockedMssrList.get(j).getStatus() != MssrStatus.NONE) {
                    count++;

                    if (lockedMssrList.get(j).isEndMthUpdated()) {
                        sum += lockedMssrList.get(j).getQtySold();
                    } else {
                        sum += lockedMssrList.get(j).getQtyForecasted();
                    }
                }
            }

            for (int j = Math.max(i - nPoint, 0); j < i; j++) {
                count++;

                if (unlockedMssrList.get(j).isEndMthUpdated()) {
                    sum += unlockedMssrList.get(j).getQtySold();
                } else {
                    sum += unlockedMssrList.get(j).getQtyForecasted();
                }
            }

            if (i <= 0) {
                prevMssr = lockedMssrList.get(lockedMssrList.size() - 1);
            } else {
                prevMssr = unlockedMssrList.get(i - 1);
            }

            if (unlockedMssrList == null || prevMssr == null) {
                throw new ForecastFailureException(mssr.getStock().getName());
            }

            if (mssr.getStatus() != MssrStatus.APPROVED && count > 0) {
                em.detach(mssr);
                mssr.setQtyForecasted(sum / count);
                mssr.setPlannedInventory(plannedInv);
                mssr.setQtyRequested(mssr.getQtyForecasted() + mssr.getPlannedInventory() - prevMssr.getPlannedInventory() - mssr.getVarianceOffset());
                impacted = true;
            }
        }

        if (!impacted) {
            throw new ForecastFailureException("NoMonths");
        }

        return unlockedMssrList;
    }

    @Override
    @TransactionAttribute(REQUIRED)
    public void saveMonthlyStockSupplyReq(List<Couple<Stock, List<MonthlyStockSupplyReq>>> stockMssrList) throws InvalidMssrException {
        Calendar lockoutCutoff;
        boolean impacted = false;

        for (Couple<Stock, List<MonthlyStockSupplyReq>> mssrList : stockMssrList) {
            for (MonthlyStockSupplyReq mssr : mssrList.getSecond()) {
                lockoutCutoff = TimeMethods.getPlantCurrTime(mssr.getCountryOffice());
                lockoutCutoff.add(Calendar.MONTH, FORECAST_LOCKOUT_MONTHS);

                if (mssr.getStatus() != MssrStatus.APPROVED && mssr.getStatus() != MssrStatus.PENDING) {

                    if (mssr.getQtyForecasted() < 0 || mssr.getPlannedInventory() < 0 || mssr.getQtyRequested() < 0) {
                        throw new InvalidMssrException("Invalid entry for " + mssr.getStock().getName() + ", " + mssr.getMonth() + " " + mssr.getYear());
                    }

                    if (TimeMethods.getCalFromMonthYear(mssr.getMonth(), mssr.getYear()).compareTo(lockoutCutoff) > 0) {
                        mssr.setStatus(MssrStatus.PENDING);
                        mssr.setApproved(false);

                        em.merge(mssr);
                        impacted = true;
                    } else {
                        throw new InvalidMssrException("Monthly Stock Supply Requirements for " + mssr.getMonth() + " " + mssr.getYear() + " falls within the lockout period and cannot be edited.");
                    }
                }
            }
        }
        if (!impacted) {
            throw new InvalidMssrException("All forecast entries are either pending approval or have already been approved.");
        }
    }

    @Override
    @TransactionAttribute(REQUIRED)
    public void reviewMonthlyStockSupplyReq(List<Couple<Stock, List<MonthlyStockSupplyReq>>> stockMssrList, boolean approved) throws InvalidMssrException {
        Calendar lockoutCutoff;
        boolean impacted = false;

        for (Couple<Stock, List<MonthlyStockSupplyReq>> mssrList : stockMssrList) {
            for (MonthlyStockSupplyReq mssr : mssrList.getSecond()) {
                lockoutCutoff = TimeMethods.getPlantCurrTime(mssr.getCountryOffice());
                lockoutCutoff.add(Calendar.MONTH, FORECAST_LOCKOUT_MONTHS);

//                if (mssr.getStatus() != MssrStatus.PENDING) {
//                    throw new InvalidMssrException("Entry for " + mssr.getStock().getName() + ", " + mssr.getMonth() + " " + mssr.getYear() + " has not been submitted for approval yet.");
//                }
                if (TimeMethods.getCalFromMonthYear(mssr.getMonth(), mssr.getYear()).compareTo(lockoutCutoff) > 0) {
                    if (mssr.getStatus() == MssrStatus.PENDING) {
                        if (approved == true) {
                            mssr.setStatus(MssrStatus.APPROVED);
                            mssr.setApproved(true);
                        } else {
                            mssr.setStatus(MssrStatus.REJECTED);
                        }
                        em.merge(mssr);
                        impacted = true;
                    }
                } else {
                    throw new InvalidMssrException("Monthly Stock Supply Requirements for " + mssr.getMonth() + " " + mssr.getYear() + " falls within the lockout period and cannot be edited.");
                }
            }
        }

        if (!impacted) {
            throw new InvalidMssrException("There are no pending forecasts to approve or reject!");
        }
    }

    @Override
    public List<MonthlyStockSupplyReq> retrieveMssrForCoStock(CountryOffice co, Stock stock, int year) {
        return this.retrieveMssrForCoStock(co, stock, Month.JAN, year, Month.DEC, year);
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

    @Override
    public List<MonthlyStockSupplyReq> retrieveLockedMssrForCoStock(CountryOffice co, Stock stock, int mthsHist)
            throws IllegalArgumentException {
        if (mthsHist < 0) {
            throw new IllegalArgumentException("Number of History MSSR Months must be zero or positive");
        }

        Calendar lockedOutStart = TimeMethods.getPlantCurrTime(co);
        lockedOutStart.add(Calendar.MONTH, (mthsHist - 1 - FORECAST_LOCKOUT_MONTHS) * -1);

        Calendar lockedOutEnd = TimeMethods.getPlantCurrTime(co);
        lockedOutEnd.add(Calendar.MONTH, FORECAST_LOCKOUT_MONTHS);

        return this.retrieveMssrForCoStock(co, stock, Month.getMonth(lockedOutStart.get(Calendar.MONTH)), lockedOutStart.get(Calendar.YEAR), Month.getMonth(lockedOutEnd.get(Calendar.MONTH)), lockedOutEnd.get(Calendar.YEAR));
    }

    @Override
    public List<MonthlyStockSupplyReq> retrieveUnlockedMssrForCoStock(CountryOffice co, Stock stock) {

        Calendar planningStart = TimeMethods.getPlantCurrTime(co);
        planningStart.add(Calendar.MONTH, FORECAST_LOCKOUT_MONTHS + 1);

        Calendar planningEnd = TimeMethods.getPlantCurrTime(co);
        planningEnd.add(Calendar.MONTH, FORECAST_HORIZON);

        return this.retrieveMssrForCoStock(co, stock, Month.getMonth(planningStart.get(Calendar.MONTH)), planningStart.get(Calendar.YEAR), Month.getMonth(planningEnd.get(Calendar.MONTH)), planningEnd.get(Calendar.YEAR));
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
            mssr.setStatus(MssrStatus.NONE);

            em.persist(mssr);

            co.getMonthlyStockSupplyReqs().add(mssr);
            stock.getMonthlyStockSupplyReqs().add(mssr);
        }

        return mssr;
    }

}
