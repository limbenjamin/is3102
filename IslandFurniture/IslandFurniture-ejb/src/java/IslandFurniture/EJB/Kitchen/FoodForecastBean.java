/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.Kitchen;

import IslandFurniture.DataStructures.Couple;
import IslandFurniture.Entities.MenuItem;
import IslandFurniture.Entities.MonthlyMenuItemSalesForecastPK;
import IslandFurniture.Entities.MonthlyMenuItemSalesForecast;
import IslandFurniture.Entities.RestaurantTransaction;
import IslandFurniture.Entities.RestaurantTransactionDetail;
import IslandFurniture.Entities.Store;
import IslandFurniture.Enums.Month;
import IslandFurniture.Enums.MmsfStatus;
import IslandFurniture.Exceptions.ForecastFailureException;
import IslandFurniture.Exceptions.InvalidInputException;
import IslandFurniture.Exceptions.InvalidMmsfException;
import IslandFurniture.StaticClasses.QueryMethods;
import static IslandFurniture.StaticClasses.SystemConstants.FOOD_FORECAST_LOCKOUT_MONTHS;
import static IslandFurniture.StaticClasses.SystemConstants.FORECAST_DEFAULT_WEIGHT;
import static IslandFurniture.StaticClasses.SystemConstants.FORECAST_HORIZON;
import IslandFurniture.StaticClasses.TimeMethods;
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
    public List<MonthlyMenuItemSalesForecast> retrieveNaiveForecast(Store store, MenuItem mi) throws ForecastFailureException, InvalidMmsfException {
        boolean impacted = false;
        double growthRate = this.calcGrowthRate(store, mi);

        List<MonthlyMenuItemSalesForecast> unlockedMmsfList = this.retrieveUnlockedMmsfForStoreMi(store, mi);

        for (int i = 0; i < unlockedMmsfList.size(); i++) {
            MonthlyMenuItemSalesForecast mmsf = unlockedMmsfList.get(i);
            if (mmsf.getStatus() != MmsfStatus.APPROVED && mmsf.getStatus() != MmsfStatus.PENDING) {
                MonthlyMenuItemSalesForecast oldMmsf = QueryMethods.findNextMmsf(em, mmsf, -12);
                MonthlyMenuItemSalesForecast prevMmsf;

                if (oldMmsf == null) {
                    throw new InvalidMmsfException("Note: Previous forecasted values do not exist for some Stock!");
                }

                if (i <= 0) {
                    prevMmsf = QueryMethods.findNextMmsf(em, mmsf, -1);
                } else {
                    prevMmsf = unlockedMmsfList.get(i - 1);
                }

                em.detach(mmsf);

                if (oldMmsf.isEndMthUpdated()) {
                    mmsf.setQtyForecasted((int) (oldMmsf.getQtySold() * growthRate));
                } else {
                    mmsf.setQtyForecasted((int) (oldMmsf.getQtyForecasted() * growthRate));
                }

                impacted = true;
            }
        }

        if (!impacted) {
            throw new ForecastFailureException("There are no available months of forecast!");
        }

        return unlockedMmsfList;
    }

    @Override
    public List<MonthlyMenuItemSalesForecast> retrieveNPointForecast(Store store, MenuItem mi, int nPoint)
            throws ForecastFailureException, InvalidInputException {
        if (nPoint <= 0) {
            throw new InvalidInputException("Please enter a valid N-Point.");
        }
        boolean impacted = false;

        List<MonthlyMenuItemSalesForecast> unlockedMmsfList = this.retrieveUnlockedMmsfForStoreMi(store, mi);
        List<MonthlyMenuItemSalesForecast> lockedMmsfList = this.retrieveLockedMmsfForStoreMi(store, mi, nPoint);

        for (int i = 0; i < unlockedMmsfList.size(); i++) {
            MonthlyMenuItemSalesForecast mmsf = unlockedMmsfList.get(i);
            MonthlyMenuItemSalesForecast prevMmsf;
            int sum = 0;
            int count = 0;

            for (int j = i; j < lockedMmsfList.size(); j++) {
                if (lockedMmsfList.get(j).getStatus() != null && lockedMmsfList.get(j).getStatus() != MmsfStatus.NONE) {
                    count++;

                    if (lockedMmsfList.get(j).isEndMthUpdated()) {
                        sum += lockedMmsfList.get(j).getQtySold();
                    } else {
                        sum += lockedMmsfList.get(j).getQtyForecasted();
                    }
                }
            }

            for (int j = Math.max(i - nPoint, 0); j < i; j++) {
                count++;

                if (unlockedMmsfList.get(j).isEndMthUpdated()) {
                    sum += unlockedMmsfList.get(j).getQtySold();
                } else {
                    sum += unlockedMmsfList.get(j).getQtyForecasted();
                }
            }

            if (i <= 0) {
                prevMmsf = lockedMmsfList.get(lockedMmsfList.size() - 1);
            } else {
                prevMmsf = unlockedMmsfList.get(i - 1);
            }

            if (unlockedMmsfList == null || prevMmsf == null) {
                throw new ForecastFailureException(mmsf.getMenuItem().getName());
            }

            if (mmsf.getStatus() != MmsfStatus.APPROVED && mmsf.getStatus() != MmsfStatus.PENDING && count > 0) {
                em.detach(mmsf);
                mmsf.setQtyForecasted(sum / count);
                impacted = true;
            }
        }

        if (!impacted) {
            throw new ForecastFailureException("NoMonths");
        }

        return unlockedMmsfList;
    }

    @Override
    @TransactionAttribute(REQUIRED)
    public void saveMonthlyMenuItemSalesForecast(List<Couple<MenuItem, List<MonthlyMenuItemSalesForecast>>> miMmsfList) throws InvalidMmsfException {
        Calendar lockoutCutoff;
        boolean impacted = false;

        for (Couple<MenuItem, List<MonthlyMenuItemSalesForecast>> mmsfList : miMmsfList) {
            for (MonthlyMenuItemSalesForecast mmsf : mmsfList.getSecond()) {
                lockoutCutoff = TimeMethods.getPlantCurrTime(mmsf.getStore());
                lockoutCutoff.add(Calendar.MONTH, FOOD_FORECAST_LOCKOUT_MONTHS);

                if (mmsf.getStatus() != MmsfStatus.APPROVED && mmsf.getStatus() != MmsfStatus.PENDING) {

                    if (mmsf.getQtyForecasted() < 0) {
                        throw new InvalidMmsfException("Invalid entry for " + mmsf.getMenuItem().getName() + ", " + mmsf.getMonth() + " " + mmsf.getYear());
                    }

                    Calendar currMthYr = TimeMethods.getCalFromMonthYear(mmsf.getMonth(), mmsf.getYear());

                    if (currMthYr.compareTo(lockoutCutoff) > 0) {
                        mmsf.setStatus(MmsfStatus.PENDING);

                        em.merge(mmsf);
                        impacted = true;
                    } else {
                        throw new InvalidMmsfException("Monthly Menu Item Sales Forecast for " + mmsf.getMonth() + " " + mmsf.getYear() + " falls within the lockout period and cannot be edited.");
                    }
                }
            }
        }
        if (!impacted) {
            throw new InvalidMmsfException("All forecast entries are either pending approval or have already been approved.");
        }
    }
    
    @Override
    @TransactionAttribute(REQUIRED)
    public void reviewMonthlyMenuItemSalesForecast(List<Couple<MenuItem, List<MonthlyMenuItemSalesForecast>>> miMmsfList, boolean approved) throws InvalidMmsfException {
        Calendar lockoutCutoff;
        boolean impacted = false;

        for (Couple<MenuItem, List<MonthlyMenuItemSalesForecast>> mmsfList : miMmsfList) {
            for (MonthlyMenuItemSalesForecast mmsf : mmsfList.getSecond()) {
                lockoutCutoff = TimeMethods.getPlantCurrTime(mmsf.getStore());
                lockoutCutoff.add(Calendar.MONTH, FOOD_FORECAST_LOCKOUT_MONTHS);

                if (TimeMethods.getCalFromMonthYear(mmsf.getMonth(), mmsf.getYear()).compareTo(lockoutCutoff) > 0) {
                    if (mmsf.getStatus() == MmsfStatus.PENDING) {
                        if (approved == true) {
                            mmsf.setStatus(MmsfStatus.APPROVED);
                        } else {
                            mmsf.setStatus(MmsfStatus.REJECTED);
                        }
                        em.merge(mmsf);
                        impacted = true;
                    }
                } else {
                    throw new InvalidMmsfException("Monthly Menu Item Sales Forecast for " + mmsf.getMonth() + " " + mmsf.getYear() + " falls within the lockout period and cannot be edited.");
                }
            }
        }

        if (!impacted) {
            throw new InvalidMmsfException("There are no pending forecasts to approve or reject!");
        }
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
    public List<MonthlyMenuItemSalesForecast> retrieveLockedMmsfForStoreMi(Store store, MenuItem mi, int mthsHist) throws IllegalArgumentException {
        if (mthsHist < 0) {
            throw new IllegalArgumentException("Number of History MMSF Months must be zero or positive");
        }

        Calendar lockedOutStart = TimeMethods.getPlantCurrTime(store);
        lockedOutStart.add(Calendar.MONTH, (mthsHist - 1 - FOOD_FORECAST_LOCKOUT_MONTHS) * -1);

        Calendar lockedOutEnd = TimeMethods.getPlantCurrTime(store);
        lockedOutEnd.add(Calendar.MONTH, FOOD_FORECAST_LOCKOUT_MONTHS);

        List<MonthlyMenuItemSalesForecast> lockedMmsfList = this.retrieveMmsfForStoreMi(store, mi, Month.getMonth(lockedOutStart.get(Calendar.MONTH)), lockedOutStart.get(Calendar.YEAR), Month.getMonth(lockedOutEnd.get(Calendar.MONTH)), lockedOutEnd.get(Calendar.YEAR));

        return lockedMmsfList;
    }

    @Override
    public List<MonthlyMenuItemSalesForecast> retrieveUnlockedMmsfForStoreMi(Store store, MenuItem mi) {

        Calendar planningStart = TimeMethods.getPlantCurrTime(store);
        planningStart.add(Calendar.MONTH, FOOD_FORECAST_LOCKOUT_MONTHS + 1);

        Calendar planningEnd = TimeMethods.getPlantCurrTime(store);
        planningEnd.add(Calendar.MONTH, FORECAST_HORIZON);

        List<MonthlyMenuItemSalesForecast> unlockedMmsfList = this.retrieveMmsfForStoreMi(store, mi, Month.getMonth(planningStart.get(Calendar.MONTH)), planningStart.get(Calendar.YEAR), Month.getMonth(planningEnd.get(Calendar.MONTH)), planningEnd.get(Calendar.YEAR));

        return unlockedMmsfList;
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

        for (RestaurantTransaction eachTrans : listOfTrans) {
            // Convert Calendar raw values to country office's offset value
            cal = TimeMethods.convertToPlantTime(store, eachTrans.getTransTime());

            // Add items from transaction to the relevant MMSF
            em.refresh(eachTrans);

            for (RestaurantTransactionDetail eachDetail : eachTrans.getRestaurantTransactionDetails()) {
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

    private double calcGrowthRate(Store store, MenuItem mi) {
        double growthRate = FORECAST_DEFAULT_WEIGHT;

        // Calculate Growth Trend Weights
        Calendar endMth = TimeMethods.getPlantCurrTime(store);
        endMth.add(Calendar.MONTH, -1);

        Calendar startMth = TimeMethods.getPlantCurrTime(store);
        startMth.add(Calendar.MONTH, -12);
        List<MonthlyMenuItemSalesForecast> oneYrBackMmsfList = this.retrieveMmsfForStoreMi(store, mi, Month.getMonth(startMth.get(Calendar.MONTH)), startMth.get(Calendar.YEAR), Month.getMonth(endMth.get(Calendar.MONTH)), endMth.get(Calendar.YEAR));

        endMth.add(Calendar.YEAR, -1);
        startMth.add(Calendar.YEAR, -1);
        List<MonthlyMenuItemSalesForecast> twoYrBackMmsfList = this.retrieveMmsfForStoreMi(store, mi, Month.getMonth(startMth.get(Calendar.MONTH)), startMth.get(Calendar.YEAR), Month.getMonth(endMth.get(Calendar.MONTH)), endMth.get(Calendar.YEAR));

        int oneYrSum = 0, twoYrSum = 0;

        for (int i = 0; i < oneYrBackMmsfList.size(); i++) {
            if (oneYrBackMmsfList.get(i).isEndMthUpdated() && twoYrBackMmsfList.get(i).isEndMthUpdated()) {
                oneYrSum += oneYrBackMmsfList.get(i).getQtySold();
                twoYrSum += twoYrBackMmsfList.get(i).getQtySold();
            }
        }

        if (twoYrSum > 0) {
            growthRate = ((double) oneYrSum) / twoYrSum;
        }

        return growthRate;
    }
}
