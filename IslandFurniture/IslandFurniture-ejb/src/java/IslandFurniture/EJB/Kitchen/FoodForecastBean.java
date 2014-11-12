/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.Kitchen;

import IslandFurniture.DataStructures.Couple;
import IslandFurniture.Entities.Dish;
import IslandFurniture.Entities.Ingredient;
import IslandFurniture.Entities.IngredientContractDetail;
import IslandFurniture.Entities.IngredientPurchaseOrder;
import IslandFurniture.Entities.IngredientPurchaseOrderDetail;
import IslandFurniture.Entities.IngredientSupplier;
import IslandFurniture.Entities.MenuItem;
import IslandFurniture.Entities.MenuItemDetail;
import IslandFurniture.Entities.MonthlyMenuItemSalesForecastPK;
import IslandFurniture.Entities.MonthlyMenuItemSalesForecast;
import IslandFurniture.Entities.RecipeDetail;
import IslandFurniture.Entities.RestaurantTransaction;
import IslandFurniture.Entities.RestaurantTransactionDetail;
import IslandFurniture.Entities.Store;
import IslandFurniture.Entities.WeeklyIngredientSupplyReq;
import IslandFurniture.Entities.WeeklyMenuItemSalesForecast;
import IslandFurniture.Enums.Month;
import IslandFurniture.Enums.MmsfStatus;
import IslandFurniture.Enums.PurchaseOrderStatus;
import IslandFurniture.Exceptions.ForecastFailureException;
import IslandFurniture.Exceptions.InvalidInputException;
import IslandFurniture.Exceptions.InvalidMmsfException;
import IslandFurniture.Exceptions.InvalidWmsfException;
import IslandFurniture.StaticClasses.Helper;
import IslandFurniture.StaticClasses.QueryMethods;
import static IslandFurniture.StaticClasses.SystemConstants.FOOD_FORECAST_LOCKOUT_MONTHS;
import static IslandFurniture.StaticClasses.SystemConstants.FORECAST_DEFAULT_WEIGHT;
import static IslandFurniture.StaticClasses.SystemConstants.FORECAST_HORIZON;
import IslandFurniture.StaticClasses.TimeMethods;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            throw new ForecastFailureException(mi.getName());
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
    public void saveWeeklyMenuItemSalesForecast(List<Couple<MenuItem, List<WeeklyMenuItemSalesForecast>>> miWmsfList) throws InvalidWmsfException {
        Calendar lockoutCutoff;

        for (Couple<MenuItem, List<WeeklyMenuItemSalesForecast>> wmsfList : miWmsfList) {
            int sum = 0;

            for (int i = 0; i < wmsfList.getSecond().size(); i++) {
                WeeklyMenuItemSalesForecast wmsf = wmsfList.getSecond().get(i);
                sum += wmsf.getQty();
                if (i == wmsfList.getSecond().size() - 1 && sum != wmsf.getMmsf().getQtyForecasted()) {
                    throw new InvalidWmsfException("The weekly quantities forecasted for " + wmsf.getMmsf().getMenuItem().getName() + " do not match up to the Monthly figure of " + wmsf.getMmsf().getQtyForecasted() + ". Please make necessary adjustments and try again.");
                }
            }

            for (WeeklyMenuItemSalesForecast wmsf : wmsfList.getSecond()) {
                lockoutCutoff = TimeMethods.getPlantCurrTime(wmsf.getMmsf().getStore());
                lockoutCutoff.add(Calendar.MONTH, FOOD_FORECAST_LOCKOUT_MONTHS);

                if (!wmsf.isLocked()) {
                    if (wmsf.getQty() < 0) {
                        throw new InvalidWmsfException("Invalid entry for " + wmsf.getMmsf().getMenuItem().getName() + ", Week " + wmsf.getWeekNo());
                    }

                    Calendar currMthYr = TimeMethods.getCalFromMonthYear(wmsf.getMmsf().getMonth(), wmsf.getMmsf().getYear());

                    if (currMthYr.compareTo(lockoutCutoff) > 0) {
                        em.merge(wmsf);
                    } else {
                        throw new InvalidWmsfException("This Weekly Menu Item Sales Forecast falls within the lockout period and cannot be edited.");
                    }
                } else {
                    throw new InvalidWmsfException("This Weekly Menu Item Sales Forecast has already been locked and cannot be edited");
                }
            }
        }
    }

    @Override
    public List<Couple<MenuItem, List<WeeklyMenuItemSalesForecast>>> resetWmsfList(List<Couple<MenuItem, List<WeeklyMenuItemSalesForecast>>> miWmsfList) {
        for (Couple<MenuItem, List<WeeklyMenuItemSalesForecast>> couple : miWmsfList) {
            if (couple.getSecond() != null) {
                int totalReq = couple.getSecond().get(0).getMmsf().getQtyForecasted();
                int numWeeks = couple.getSecond().size();
                for (WeeklyMenuItemSalesForecast wmsf : couple.getSecond()) {
                    wmsf.setQty(totalReq / numWeeks);
                }
                couple.getSecond().get(numWeeks - 1).setQty(totalReq - (numWeeks - 1) * (totalReq / numWeeks));
            }
        }

        return miWmsfList;
    }

    @Override
    public boolean isWmsfListEditable(List<Couple<MenuItem, List<WeeklyMenuItemSalesForecast>>> miWmsfList) {
        Calendar lockoutCutoff;

        for (Couple<MenuItem, List<WeeklyMenuItemSalesForecast>> wmsfList : miWmsfList) {
            for (WeeklyMenuItemSalesForecast wmsf : wmsfList.getSecond()) {
                lockoutCutoff = TimeMethods.getPlantCurrTime(wmsf.getMmsf().getStore());
                lockoutCutoff.add(Calendar.MONTH, FOOD_FORECAST_LOCKOUT_MONTHS);

                if (wmsf.isLocked()) {
                    return false;
                }

                Calendar currMthYr = TimeMethods.getCalFromMonthYear(wmsf.getMmsf().getMonth(), wmsf.getMmsf().getYear());
                if (currMthYr.compareTo(lockoutCutoff) < 0) {
                    return false;
                }
            }
        }

        return true;
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
                            this.createWeeklyForecast(mmsf);
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
    public void orderIngredients(List<Couple<MenuItem, List<WeeklyMenuItemSalesForecast>>> miWmsfList, Store store, Month month, int year, int numWeeks) {
        for (int i = 0; i < numWeeks; i++) {
            Map<Dish, Integer> dishMap = new HashMap();

            for (Couple<MenuItem, List<WeeklyMenuItemSalesForecast>> couple : miWmsfList) {
                if (couple.getSecond() != null && couple.getSecond().size() == numWeeks) {
                    for (MenuItemDetail miDetail : couple.getFirst().getMenuItemDetails()) {
                        couple.getSecond().get(i).setLocked(true);
                        em.merge(couple.getSecond().get(i));

                        if (dishMap.containsKey(miDetail.getDish())) {
                            dishMap.put(miDetail.getDish(), dishMap.get(miDetail.getDish()) + miDetail.getQuantity() * couple.getSecond().get(i).getQty());
                        } else {
                            dishMap.put(miDetail.getDish(), miDetail.getQuantity() * couple.getSecond().get(i).getQty());
                        }
                    }
                }
            }

            Map<Ingredient, Double> ingredMap = new HashMap();

            for (Dish eachDish : dishMap.keySet()) {
                System.out.println(eachDish.getName() + ": " + dishMap.get(eachDish));
                for (RecipeDetail rd : eachDish.getRecipe().getRecipeDetails()) {
                    if (ingredMap.containsKey(rd.getIngredient())) {
                        ingredMap.put(rd.getIngredient(), ingredMap.get(rd.getIngredient()) + rd.getQuantity() * dishMap.get(eachDish));
                    } else {
                        ingredMap.put(rd.getIngredient(), rd.getQuantity() * dishMap.get(eachDish));
                    }
                }
            }

            for (Ingredient ingred : ingredMap.keySet()) {
                System.out.println(ingred.getName() + ": " + ingredMap.get(ingred));
                IngredientContractDetail icd = QueryMethods.getICDByIngredAndCo(em, ingred, store.getCountryOffice());
                System.out.println(icd);

                if (icd != null) {
                    WeeklyIngredientSupplyReq wisr = new WeeklyIngredientSupplyReq();
                    wisr.setIngredient(ingred);
                    wisr.setStore(store);
                    wisr.setMonth(month);
                    wisr.setYear(year);
                    wisr.setWeek(i + 1);
                    wisr.setQty((long) Math.ceil(ingredMap.get(ingred)));
                    em.persist(wisr);

                    WeeklyIngredientSupplyReq pastWisr = QueryMethods.findOrMakePastWmsf(em, wisr, (int) Math.ceil(icd.getLeadTimeInDays() / 7.0));
                    if (pastWisr != null) {
                        pastWisr.setQtyToOrder(wisr.getQty());

                        IngredientPurchaseOrderDetail ipod = new IngredientPurchaseOrderDetail();
                        ipod.setIngredient(ingred);
                        ipod.setNumberOfLots((int) Math.ceil(wisr.getQty() / icd.getLotSize()));
                        ipod.setQuantity(ipod.getNumberOfLots() * icd.getLotSize());
                        ipod.setSubtotalPrice(icd.getLotPrice() * ipod.getNumberOfLots());
                        em.persist(ipod);

                        pastWisr.setIngredPoDetail(ipod);
                        em.merge(pastWisr);
                    }
                }
            }
        }
    }

    @Override
    public List<WeeklyMenuItemSalesForecast> retrieveWmsfForStoreMi(Store store, MenuItem menuItem, Integer year, Month month) {
        Query q = em.createNamedQuery("getWmsfByStoreMi");
        q.setParameter("store", store);
        q.setParameter("mi", menuItem);
        q.setParameter("year", year);
        q.setParameter("month", month);

        List<WeeklyMenuItemSalesForecast> wmsfList = (List<WeeklyMenuItemSalesForecast>) q.getResultList();
        wmsfList.sort(null);

        return wmsfList;
    }

    @Override
    public List<WeeklyIngredientSupplyReq> retrieveWisrForStoreIngredYrMth(Store store, Ingredient ingred, int year, Month month) {
        Query q = em.createNamedQuery("getWisrByStoreIngredYrMth");
        q.setParameter("store", store);
        q.setParameter("ingredient", ingred);
        q.setParameter("year", year);
        q.setParameter("month", month);

        List<WeeklyIngredientSupplyReq> wisrList = (List<WeeklyIngredientSupplyReq>) q.getResultList();
        int numWeeks = Helper.getNumOfWeeks(month.value, year);

        if (wisrList.size() < numWeeks) {
            int[] slots = {0, 0, 0, 0, 0};
            for (WeeklyIngredientSupplyReq wisr : wisrList) {
                slots[wisr.getWeek() - 1]++;
            }
            for (int i = 0; i < numWeeks; i++) {
                if (slots[i] == 0) {
                    WeeklyIngredientSupplyReq wisr = new WeeklyIngredientSupplyReq();
                    wisr.setIngredient(ingred);
                    wisr.setStore(store);
                    wisr.setMonth(month);
                    wisr.setYear(year);
                    wisr.setWeek(i + 1);
                    wisrList.add(wisr);
                }
            }
        }
        wisrList.sort(null);

        return wisrList;
    }

    @Override
    public void makeIngredPurchaseOrders(Store store, Month month, int year, int week) {
        Query q = em.createNamedQuery("getWisrByStoreYrMthWk");
        q.setParameter("store", store);
        q.setParameter("year", year);
        q.setParameter("month", month);
        q.setParameter("week", week);

        List<WeeklyIngredientSupplyReq> wisrList = (List<WeeklyIngredientSupplyReq>) q.getResultList();
        Map<IngredientSupplier, List<IngredientPurchaseOrderDetail>> supplierMap = new HashMap();
        IngredientSupplier supplier;
        List<IngredientPurchaseOrderDetail> ipodList;

        for (WeeklyIngredientSupplyReq wisr : wisrList) {
            supplier = QueryMethods.findIngredSupplierFromIngred(em, wisr.getIngredient());

            if (wisr.getIngredPoDetail() != null) {
                if (supplier != null) {
                    if (!supplierMap.containsKey(supplier) || (supplierMap.containsKey(supplier) && supplierMap.get(supplier) == null)) {
                        ipodList = new ArrayList();
                        ipodList.add(wisr.getIngredPoDetail());
                        supplierMap.put(supplier, ipodList);
                    } else {
                        supplierMap.get(supplier).add(wisr.getIngredPoDetail());
                    }
                } else {
                    System.err.println("Ingredient NOT ORDERED for " + wisr.getIngredient().getName() + " because there is no supplier for this ingredient!");
                }
            }
        }

        for (IngredientSupplier eachSup : supplierMap.keySet()) {
            IngredientPurchaseOrder ipo = new IngredientPurchaseOrder();
            ipo.setCurrency(eachSup.getCountry().getCurrency());
            ipo.setIngredPurchaseOrderDetails(supplierMap.get(eachSup));
            ipo.setIngredSupplier(eachSup);
            ipo.setOrderDate(Helper.getStartDateOfWeek(month.value, year, week));
            ipo.setStatus(PurchaseOrderStatus.PLANNED);
            ipo.setShipsTo(store);
            ipo.setStore(store);

            double totalPrice = 0.0;
            for (IngredientPurchaseOrderDetail ipod : ipo.getIngredPurchaseOrderDetails()) {
                totalPrice += ipod.getSubtotalPrice();
                ipod.setIngredPurchaseOrder(ipo);
            }
            ipo.setPrice(totalPrice);

            em.persist(ipo);
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

    private void createWeeklyForecast(MonthlyMenuItemSalesForecast mmsf) {
        int numWeeks = Helper.getNumOfWeeks(mmsf.getMonth().value, mmsf.getYear());
        int sum = 0;
        int weeklyNum = mmsf.getQtyForecasted() / numWeeks;

        mmsf.setWmsfList(new ArrayList());
        for (int i = 1; i <= numWeeks; i++) {
            WeeklyMenuItemSalesForecast wmsf = new WeeklyMenuItemSalesForecast();
            wmsf.setLocked(false);
            wmsf.setWeekNo(i);
            wmsf.setMmsf(mmsf);
            if (i < numWeeks) {
                sum += weeklyNum;
                wmsf.setQty(weeklyNum);
            } else {
                wmsf.setQty(mmsf.getQtyForecasted() - sum);
            }

            mmsf.getWmsfList().add(wmsf);

            em.persist(wmsf);
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
