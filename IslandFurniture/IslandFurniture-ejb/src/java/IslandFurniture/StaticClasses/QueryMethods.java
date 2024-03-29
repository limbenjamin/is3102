/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.StaticClasses;

import static IslandFurniture.EJB.Manufacturing.ManageProductionPlanning.FORWARDLOCK;
import IslandFurniture.Entities.BOMDetail;
import IslandFurniture.Entities.Country;
import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.Currency;
import IslandFurniture.Entities.Dish;
import IslandFurniture.Entities.FurnitureModel;
import IslandFurniture.Entities.Ingredient;
import IslandFurniture.Entities.IngredientContractDetail;
import IslandFurniture.Entities.IngredientInventory;
import IslandFurniture.Entities.IngredientSupplier;
import IslandFurniture.Entities.ManufacturingFacility;
import IslandFurniture.Entities.Material;
import IslandFurniture.Entities.MembershipTier;
import IslandFurniture.Entities.MenuItem;
import IslandFurniture.Entities.MonthlyMenuItemSalesForecast;
import IslandFurniture.Entities.MonthlyMenuItemSalesForecastPK;
import IslandFurniture.Entities.MonthlyProductionPlan;
import IslandFurniture.Entities.MonthlyStockSupplyReq;
import IslandFurniture.Entities.MonthlyStockSupplyReqPK;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.ProcuredStock;
import IslandFurniture.Entities.ProcuredStockContract;
import IslandFurniture.Entities.ProcuredStockContractDetail;
import IslandFurniture.Entities.ProcuredStockSupplier;
import IslandFurniture.Entities.ProductionOrder;
import IslandFurniture.Entities.RetailItem;
import IslandFurniture.Entities.Stock;
import IslandFurniture.Entities.StockSupplied;
import IslandFurniture.Entities.StorageArea;
import IslandFurniture.Entities.StorageBin;
import IslandFurniture.Entities.Store;
import IslandFurniture.Entities.WeeklyIngredientSupplyReq;
import IslandFurniture.Entities.WeeklyMRPRecord;
import IslandFurniture.Entities.WeeklyProductionPlan;
import IslandFurniture.Enums.MenuType;
import IslandFurniture.Enums.Month;
import IslandFurniture.Enums.MssrStatus;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

/**
 * This java class contains static methods to implement all the various named
 * queries that are reusable
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
public class QueryMethods {

    public static Plant findPlantByName(EntityManager em, Country country, String plantName) {
        Query q = em.createNamedQuery("findPlantByName");
        q.setParameter("country", country);
        q.setParameter("name", plantName);

        try {
            return (Plant) q.getSingleResult();
        } catch (NoResultException nrex) {
            return null;
        }
    }

    public static Country findCountryByName(EntityManager em, String countryName) {
        Query q = em.createNamedQuery("findCountryByName");
        q.setParameter("name", countryName);

        try {
            return (Country) q.getSingleResult();
        } catch (NoResultException nrex) {
            return null;
        }
    }

    public static List<Country> getAllCountryWithOperations(EntityManager em) {
        Query q = em.createNamedQuery("getAllCountryWithOperations");

        try {
            return (List<Country>) q.getResultList();
        } catch (NoResultException nrex) {
            return null;
        }
    }

    public static Currency findCurrencyByName(EntityManager em, String name) {
        Query q = em.createNamedQuery("findCurrencyByName");
        q.setParameter("name", name);

        try {
            return (Currency) q.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public static Currency findCurrencyByCode(EntityManager em, String code) {
        Query q = em.createNamedQuery("findCurrencyByCode");
        q.setParameter("code", code);

        try {
            return (Currency) q.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public static FurnitureModel findFurnitureByName(EntityManager em, String furnitureName) {
        Query q = em.createNamedQuery("findFurnitureByName");
        q.setParameter("name", furnitureName);

        try {
            return (FurnitureModel) q.getSingleResult();
        } catch (NoResultException nrex) {
            return null;
        }
    }

    public static RetailItem findRetailItemByName(EntityManager em, String retailItemName) {
        Query q = em.createNamedQuery("findRetailItemByName");
        q.setParameter("name", retailItemName);

        try {
            return (RetailItem) q.getSingleResult();
        } catch (NoResultException nrex) {
            return null;
        }
    }

    public static Material findMaterialByName(EntityManager em, String materialName) {
        Query q = em.createNamedQuery("findMaterialByName");
        q.setParameter("name", materialName);

        try {
            return (Material) q.getSingleResult();
        } catch (NoResultException nrex) {
            return null;
        }
    }

    public static Ingredient findIngredientByNameAndCo(EntityManager em, String ingredientName, CountryOffice co) {
        Query q = em.createNamedQuery("findIngredientByNameAndCo");
        q.setParameter("name", ingredientName);
        q.setParameter("countryOffice", co);

        try {
            return (Ingredient) q.getSingleResult();
        } catch (NoResultException nrex) {
            return null;
        }
    }

    public static List<IngredientInventory> findIngredientInventoryByIngredient(EntityManager em, Ingredient ingredient) {
        Query q = em.createNamedQuery("findIngredientInventoryByIngredient");
        q.setParameter("ingredient", ingredient);

        try {
            return (List<IngredientInventory>) q.getResultList();
        } catch (NoResultException nrex) {
            return null;
        }
    }

    public static List<Ingredient> getIngredientListByCountryOffice(EntityManager em, CountryOffice countryOffice) {
        Query q = em.createNamedQuery("getIngredientListByCountryOffice");
        q.setParameter("countryOffice", countryOffice);

        try {
            return (List<Ingredient>) q.getResultList();
        } catch (NoResultException nrex) {
            return null;
        }
    }

    public static List<IngredientSupplier> getIngredSuppliersByCo(EntityManager em, CountryOffice countryOffice) {
        Query q = em.createNamedQuery("getIngredSuppliersByCo");
        q.setParameter("co", countryOffice);

        try {
            return (List<IngredientSupplier>) q.getResultList();
        } catch (NoResultException nrex) {
            return null;
        }
    }

    public static Ingredient getIngredientByCountryOfficeAndName(EntityManager em, CountryOffice countryOffice, String name) {
        Query q = em.createNamedQuery("getIngredientByCountryOfficeAndName");
        q.setParameter("countryOffice", countryOffice);
        q.setParameter("name", name);

        try {
            return (Ingredient) q.getSingleResult();
        } catch (NoResultException nrex) {
            return null;
        }
    }

    public static Dish findDishByName(EntityManager em, String dishName) {
        Query q = em.createNamedQuery("findDishByName");
        q.setParameter("name", dishName);

        try {
            return (Dish) q.getSingleResult();
        } catch (NoResultException nrex) {
            return null;
        }
    }

    public static List<Dish> getDishListByCountryOffice(EntityManager em, CountryOffice countryOffice) {
        Query q = em.createNamedQuery("getDishListByCountryOffice");
        q.setParameter("countryOffice", countryOffice);

        try {
            return (List<Dish>) q.getResultList();
        } catch (NoResultException nrex) {
            return null;
        }
    }

    public static Dish getDishByCountryOfficeAndName(EntityManager em, CountryOffice countryOffice, String name) {
        Query q = em.createNamedQuery("getDishByCountryOfficeAndName");
        q.setParameter("countryOffice", countryOffice);
        q.setParameter("name", name);

        try {
            return (Dish) q.getSingleResult();
        } catch (NoResultException nrex) {
            return null;
        }
    }

    public static MenuItem findMenuItemByName(EntityManager em, String name) {
        Query q = em.createNamedQuery("findMenuItemByName");
        q.setParameter("name", name);

        try {
            return (MenuItem) q.getSingleResult();
        } catch (NoResultException nrex) {
            return null;
        }
    }

    public static List<MenuItem> getMenuItemListByCountryOffice(EntityManager em, CountryOffice countryOffice) {
        Query q = em.createNamedQuery("getMenuItemListByCountryOffice");
        q.setParameter("countryOffice", countryOffice);

        try {
            return (List<MenuItem>) q.getResultList();
        } catch (NoResultException nrex) {
            return null;
        }
    }

    public static MenuItem getMenuItemByCountryOfficeAndName(EntityManager em, CountryOffice countryOffice, String name) {
        Query q = em.createNamedQuery("getMenuItemByCountryOfficeAndName");
        q.setParameter("countryOffice", countryOffice);
        q.setParameter("name", name);

        try {
            return (MenuItem) q.getSingleResult();
        } catch (NoResultException nrex) {
            return null;
        }
    }

    public static List<MenuItem> getMenuItemListByCountryOfficeAndMenuType(EntityManager em, CountryOffice countryOffice, MenuType menuType) {
        Query q = em.createNamedQuery("getMenuItemListByCountryOfficeAndMenuType");
        q.setParameter("countryOffice", countryOffice);
        q.setParameter("menuType", menuType);

        try {
            return (List<MenuItem>) q.getResultList();
        } catch (NoResultException nrex) {
            return null;
        }
    }

    public static ProcuredStockSupplier findSupplierByName(EntityManager em, String supplierName) {
        Query q = em.createNamedQuery("findSupplierByName");
        q.setParameter("name", supplierName);

        try {
            return (ProcuredStockSupplier) q.getSingleResult();
        } catch (NoResultException nrex) {
            return null;
        }
    }

    public static IngredientSupplier findIngredSupplierByNameAndCo(EntityManager em, String supplierName, CountryOffice co) {
        Query q = em.createNamedQuery("findIngredSupplierByNameAndCo");
        q.setParameter("name", supplierName);
        q.setParameter("co", co);

        try {
            return (IngredientSupplier) q.getSingleResult();
        } catch (NoResultException nrex) {
            return null;
        }
    }

    public static StorageArea findStorageAreaByName(EntityManager em, String storageAreaName, Plant plant) {
        Query q = em.createNamedQuery("findStorageAreaByName");
        q.setParameter("name", storageAreaName);
        q.setParameter("plant", plant);

        try {
            return (StorageArea) q.getSingleResult();
        } catch (NoResultException nrex) {
            return null;
        }
    }

    public static StorageBin findStorageBinByName(EntityManager em, String storageBinName, StorageArea sa) {
        Query q = em.createNamedQuery("findStorageBinByName");
        q.setParameter("name", storageBinName);
        q.setParameter("sa", sa);

        try {
            return (StorageBin) q.getSingleResult();
        } catch (NoResultException nrex) {
            return null;
        }
    }

    public static List<ProcuredStockContractDetail> getProcurementContractDetailByStockAndMF(EntityManager em, ProcuredStock stock, ManufacturingFacility mf) {
        Query q = em.createNamedQuery("getProcurementContractDetailByStockAndMF", ProcuredStockContractDetail.class);
        q.setParameter("stock", stock);
        q.setParameter("mf", mf);

        try {
            return (List<ProcuredStockContractDetail>) q.getResultList();
        } catch (NoResultException NRE) {
            return null;
        }
    }

    public static List<ProcuredStockContractDetail> findPCDByStockAndMF(EntityManager em, ProcuredStock stock, ManufacturingFacility mf) {
        Query q = em.createNamedQuery("getProcurementContractDetailByStockAndMF", ProcuredStockContractDetail.class);
        q.setParameter("stock", stock);
        q.setParameter("mf", mf);

        try {
            return (List<ProcuredStockContractDetail>) q.getResultList();
        } catch (NoResultException NRE) {
            return null;
        }
    }

    public static List<ProcuredStockContractDetail> findPCDByStock(EntityManager em, ProcuredStock stock) {
        Query q = em.createNamedQuery("getProcurementContractDetailByStock", ProcuredStockContractDetail.class);
        q.setParameter("stock", stock);

        try {
            return (List<ProcuredStockContractDetail>) q.getResultList();
        } catch (NoResultException NRE) {
            return null;
        }
    }

    public static IngredientContractDetail getICDByIngredAndCo(EntityManager em, Ingredient ingredient, CountryOffice co) {
        Query q = em.createNamedQuery("getICDByIngredAndCo", IngredientContractDetail.class);
        q.setParameter("ingredient", ingredient);
        q.setParameter("co", co);

        try {
            return (IngredientContractDetail) q.getSingleResult();
        } catch (NoResultException NRE) {
            return null;
        }
    }

    public static IngredientSupplier findIngredSupplierFromIngred(EntityManager em, Ingredient ingred) {
        Query q = em.createNamedQuery("findIngredSupplierFromIngred");
        q.setParameter("ingredient", ingred);

        for(IngredientSupplier supplier: (List<IngredientSupplier>) q.getResultList()){
            System.out.println(supplier.getName());
        }
        
        try {
            return (IngredientSupplier) q.getSingleResult();
        } catch (NoResultException nrex) {
            return null;
        }
    }

    public static WeeklyIngredientSupplyReq findOrMakePastWmsf(EntityManager em, WeeklyIngredientSupplyReq wisr, int weeksOffset) {
        int year = wisr.getYear();
        int month = wisr.getMonth().value;
        int week = wisr.getWeek();

        int weekOff = weeksOffset;
        while (weekOff > 0) {
            weekOff--;
            week--;
            if (week <= 0) {
                month--;
                week = Helper.getNumOfWeeks(month, year);
                if (month < 0) {
                    month = 11;
                    year--;
                }
            }
        }

        Query q = em.createNamedQuery("findWisrByStoreYrMthWk");
        q.setParameter("ingredient", wisr.getIngredient());
        q.setParameter("store", wisr.getStore());
        q.setParameter("year", year);
        q.setParameter("month", Month.getMonth(month));
        q.setParameter("week", week);

        try {
            return (WeeklyIngredientSupplyReq) q.getSingleResult();
        } catch (NoResultException nrex) {
            WeeklyIngredientSupplyReq pastWisr = new WeeklyIngredientSupplyReq();
            pastWisr.setIngredient(wisr.getIngredient());
            pastWisr.setStore(wisr.getStore());
            pastWisr.setYear(year);
            pastWisr.setMonth(Month.getMonth(month));
            pastWisr.setWeek(week);

            return pastWisr;
        } catch (NonUniqueResultException nurex) {
            return null;
        }
    }

    public static MonthlyStockSupplyReq findNextMssr(EntityManager em, MonthlyStockSupplyReq mssr, int monthsOffset) {
        Calendar cal = TimeMethods.getCalFromMonthYear(mssr.getMonth(), mssr.getYear());
        cal.add(Calendar.MONTH, monthsOffset);

        MonthlyStockSupplyReqPK mssrPK = new MonthlyStockSupplyReqPK(mssr.getStock().getId(), mssr.getCountryOffice().getId(), Month.getMonth(cal.get(Calendar.MONTH)), cal.get(Calendar.YEAR));

        return em.find(MonthlyStockSupplyReq.class, mssrPK);
    }

    public static MonthlyMenuItemSalesForecast findNextMmsf(EntityManager em, MonthlyMenuItemSalesForecast mmsf, int monthsOffset) {
        Calendar cal = TimeMethods.getCalFromMonthYear(mmsf.getMonth(), mmsf.getYear());
        cal.add(Calendar.MONTH, monthsOffset);

        MonthlyMenuItemSalesForecastPK mmsfPK = new MonthlyMenuItemSalesForecastPK(mmsf.getMenuItem().getId(), mmsf.getStore().getId(), Month.getMonth(cal.get(Calendar.MONTH)), cal.get(Calendar.YEAR));

        return em.find(MonthlyMenuItemSalesForecast.class, mmsfPK);
    }

    public static List<StockSupplied> getStockSuppliedToStore(EntityManager em, Store store) {
        Query q = em.createNamedQuery("getStockSuppliedToStore");
        q.setParameter("store", store);

        return (List<StockSupplied>) q.getResultList();
    }

    public static List<StockSupplied> getStockSuppliedByMF(EntityManager em, ManufacturingFacility mf) {
        Query q = em.createNamedQuery("getStockSuppliedByMF");
        q.setParameter("mf", mf);

        return (List<StockSupplied>) q.getResultList();
    }

    public static List<StockSupplied> getStockSuppliedByStock(EntityManager em, Stock s) {
        Query q = em.createNamedQuery("findByStock", StockSupplied.class);
        q.setParameter("s", s);

        return (List<StockSupplied>) q.getResultList();
    }

    public static List<BOMDetail> getBomDetailByMaterial(EntityManager em, Material m) {
        Query q = em.createNamedQuery("findBOMDetailByMaterial", BOMDetail.class);
        q.setParameter("m", m);

        return (List<BOMDetail>) q.getResultList();
    }

    public static MembershipTier findMembershipTierByTitle(EntityManager em, String title) {
        try {
            Query q = em.createNamedQuery("findMembershipTierByTitle");
            q.setParameter("title", title);

            return (MembershipTier) q.getSingleResult();
        } catch (NoResultException nrex) {
            return null;
        }
    }

    public static List<MonthlyStockSupplyReq> getRelevantMSSR(EntityManager em, ManufacturingFacility MF, int m, int year) {

        Query l = em.createNamedQuery("StockSupplied.FindByMf");
        l.setParameter("mf", MF);

        ArrayList<MonthlyStockSupplyReq> RelevantMSSR = new ArrayList<MonthlyStockSupplyReq>();

        for (StockSupplied ss : (List<StockSupplied>) l.getResultList()) {

            if (!(ss.getStock() instanceof FurnitureModel)) {
                continue;
            }

            Query q = em.createNamedQuery("MonthlyStockSupplyReq.FindByCoStockBefore");
            q.setParameter("y", year);
            q.setParameter("status", MssrStatus.APPROVED);
            try {
                q.setParameter("m", Helper.translateMonth(m).value);

            } catch (Exception ex) {
            }
            q.setParameter("nm", Helper.getCurrentMonth().value + FORWARDLOCK); //2 months in advance
            try {
                q.setParameter("ny", Helper.getCurrentYear());
            } catch (Exception ex) {
            }
            q.setParameter("co", ss.getCountryOffice());
            q.setParameter("stock", ss.getStock());

            try {
                RelevantMSSR.addAll(q.getResultList());
            } catch (Exception err) {
            }

        }
        //System.out.println("GetRelevantMSSR(): " + MF.getName() + " UNTIL " + m + "/" + year + " Returned:" + RelevantMSSR.size());
        return RelevantMSSR;
    }

    public static List<MonthlyStockSupplyReq> getRelevantMssrAtPT(EntityManager em, int m, int year, ManufacturingFacility MF, Stock s) {

        Query l = em.createNamedQuery("StockSupplied.FindByMfAndS");
        l.setParameter("mf", MF);
        l.setParameter("s", s);

        ArrayList<MonthlyStockSupplyReq> RelevantMSSR = new ArrayList<MonthlyStockSupplyReq>();

        for (StockSupplied ss : (List<StockSupplied>) l.getResultList()) {

            if (!(ss.getStock() instanceof FurnitureModel)) {
                continue;
            }
            Query q = em.createNamedQuery("MonthlyStockSupplyReq.FindByCoStockAT");
            q.setParameter("y", year);
            q.setParameter("status", MssrStatus.APPROVED);
            try {
                q.setParameter("m", Helper.translateMonth(m).value);

            } catch (Exception ex) {
            }
            q.setParameter("co", ss.getCountryOffice());
            q.setParameter("stock", ss.getStock());

            try {
                RelevantMSSR.addAll(q.getResultList());
            } catch (Exception err) {
            }

        }
        //System.out.println("GetRelevantMSSRAtPT(): " + MF.getName() + " UNTIL " + m + "/" + year + " Returned:" + RelevantMSSR.size());
        return RelevantMSSR;
    }

    public static List<MonthlyStockSupplyReq> getMonthlyStockSupplyReqs(EntityManager em, MonthlyProductionPlan MPP, ManufacturingFacility MF) {
        try {

            return (getRelevantMssrAtPT(em, MPP.getMonth().value, MPP.getYear(), MF, MPP.getFurnitureModel()));

        } catch (Exception er) {
            return null;
        }
    }

    public static MonthlyProductionPlan getNextMonthlyProductionPlan(EntityManager em, MonthlyProductionPlan MPP) {
        try {
            Query q = em.createNamedQuery("MonthlyProductionPlan.Find");
            q.setParameter("m", Helper.translateMonth(Helper.addMonth(MPP.getMonth(), MPP.getYear(), 1, true)).value);
            q.setParameter("y", Helper.addMonth(MPP.getMonth(), MPP.getYear(), 1, false));
            q.setParameter("fm", MPP.getFurnitureModel());
            q.setParameter("mf", MPP.getManufacturingFacility());
            return (MonthlyProductionPlan) q.getResultList().get(0);
        } catch (Exception ex) {
            return (null);
        }

    }

    public static MonthlyProductionPlan getPrevMonthlyProductionPlan(EntityManager em, MonthlyProductionPlan MPP) {

        try {
            Query q = em.createNamedQuery("MonthlyProductionPlan.Find");
            q.setParameter("m", Helper.translateMonth(Helper.addMonth(MPP.getMonth(), MPP.getYear(), -1, true)).value);
            q.setParameter("y", Helper.addMonth(MPP.getMonth(), MPP.getYear(), -1, false));
            q.setParameter("fm", MPP.getFurnitureModel());
            q.setParameter("mf", MPP.getManufacturingFacility());
            return (MonthlyProductionPlan) q.getResultList().get(0);
        } catch (Exception ex) {

            return (null);
        }
    }

    // Extra Methods
    public static long getTotalDemand(EntityManager em, MonthlyProductionPlan MPP, ManufacturingFacility MF) {
        long total = 0;
        try {
            for (MonthlyStockSupplyReq mssr : QueryMethods.getMonthlyStockSupplyReqs(em, MPP, MF)) {
                total += mssr.getQtyRequested();
            }
        } catch (Exception ex) {
        }

        return (total);
    }

    public MonthlyProductionPlan getMonthlyProductionPlan(EntityManager em, MonthlyStockSupplyReq MSSR) throws Exception {
        try {
            Query q = em.createNamedQuery("MonthlyProductionPlan.Find");
            q.setParameter("m", MSSR.getMonth().value);
            q.setParameter("y", MSSR.getYear());
            q.setParameter("fm", MSSR.getStock());
            q.setParameter("mf", MSSR);

            return (MonthlyProductionPlan) q.getResultList().get(0);
        } catch (Exception ex) {
            throw new Exception("This MSSR does not have a MPP yet");
        }
    }

    public static double getCurrentFreeCapacity(EntityManager em, ManufacturingFacility MF, Month m, int year) {

        Query q = em.createNamedQuery("MonthlyProductionPlan.FindAllInPeriod");
        q.setParameter("m", m);
        q.setParameter("y", year);
        q.setParameter("mf", MF);
        double cCap = 0;
        for (Object o : q.getResultList()) {
            MonthlyProductionPlan mpp = (MonthlyProductionPlan) o;
            if (mpp.isLocked()) {
                return 0; //out of bound
            }
            cCap += mpp.getQTY() / (0.0 + MF.findProductionCapacity(mpp.getFurnitureModel()).getCapacity(m, year));
        }

        return 1 - cCap;

    }

    public static WeeklyMRPRecord getPrevWMRP(EntityManager em, WeeklyMRPRecord WMRP) {
        try {
            Query k = em.createQuery("select wmrp from WeeklyMRPRecord wmrp where wmrp.manufacturingFacility=:mf and wmrp.year*1000+wmrp.month*10+wmrp.week<:y*1000+:m*10+:w and wmrp.material=:ma order by wmrp.year*1000+wmrp.month*10+wmrp.week DESC");

            k.setParameter("mf", WMRP.getManufacturingFacility());

            k.setParameter("m", WMRP.getMonth().value);
            k.setParameter("y", WMRP.getYear());
            k.setParameter("w", WMRP.getWeek());
            k.setParameter("ma", WMRP.getMaterial());
//            System.out.println(
//                    "current m=" + WMRP.getMonth() + " current w=" + WMRP.getWeek() + " current y= " + WMRP.getYear()
//                    + "prev m=" + Helper.translateMonth(Helper.addoneWeek(WMRP.getMonth().value, WMRP.getYear(), WMRP.getWeek(), -1, Calendar.MONTH))
//                    + " w=" + Helper.addoneWeek(WMRP.getMonth().value, WMRP.getYear(), WMRP.getWeek(), -1, Calendar.WEEK_OF_MONTH)
//                    + " y=" + Helper.addoneWeek(WMRP.getMonth().value, WMRP.getYear(), WMRP.getWeek(), -1, Calendar.YEAR)
//            );
            return (WeeklyMRPRecord) k.getResultList().get(0);

        } catch (Exception ex) {
            System.out.println("getPrevMRP(): ERROR !" + ex.getMessage());
            return null;
        }

    }

    public static WeeklyMRPRecord getNextWMRP(EntityManager em, WeeklyMRPRecord WMRP) {
        try {
            Query k = em.createQuery("select wmrp from WeeklyMRPRecord wmrp where wmrp.manufacturingFacility=:mf and wmrp.year*1000+wmrp.month*10+wmrp.week>:y*1000+:m*10+:w and wmrp.material=:ma order by wmrp.year*1000+wmrp.month*10+wmrp.week ASC");

            k.setParameter("mf", WMRP.getManufacturingFacility());

            k.setParameter("m", WMRP.getMonth().value);
            k.setParameter("y", WMRP.getYear());
            k.setParameter("w", WMRP.getWeek());
            k.setParameter("ma", WMRP.getMaterial());

//            System.out.println(
//                    "current m=" + WMRP.getMonth() + " current w=" + WMRP.getWeek() + " current y= " + WMRP.getYear()
//                    + " next m=" + Helper.translateMonth(Helper.addoneWeek(WMRP.getMonth().value, WMRP.getYear(), WMRP.getWeek(), 1, Calendar.MONTH))
//                    + " w=" + Helper.addoneWeek(WMRP.getMonth().value, WMRP.getYear(), WMRP.getWeek(), 1, Calendar.WEEK_OF_MONTH)
//                    + " y=" + Helper.addoneWeek(WMRP.getMonth().value, WMRP.getYear(), WMRP.getWeek(), 1, Calendar.YEAR)
//            );
            return (WeeklyMRPRecord) k.getResultList().get(0);

        } catch (Exception ex) {
            System.out.println("getNextMRP(): ERROR !" + ex.getMessage());
            return null;
        }

    }

    //Calculate the amount ordered at this date of the WMRP
    public static Integer getOrderedatwMRP(EntityManager em, WeeklyMRPRecord WMRP) {
        try {
            Query k = em.createNamedQuery("weeklyMRPRecord.findwMRPOrderedatMFM");
            k.setParameter("mf", WMRP.getManufacturingFacility());
            k.setParameter("m", WMRP.getMonth());
            k.setParameter("y", WMRP.getYear());
            k.setParameter("w", WMRP.getWeek());
            k.setParameter("ma", WMRP.getMaterial());
            int sum = 0;
            for (WeeklyMRPRecord wmrp : (List<WeeklyMRPRecord>) k.getResultList()) {
                sum += wmrp.getOrderAMT();
            }

            return sum;

        } catch (Exception ex) {
            System.out.println("getOrderedatwMRP(): ERROR !" + ex.getMessage());
            return 0;
        }
    }

    public static boolean isOrderedMaterial(EntityManager em, ManufacturingFacility mff, Month requestedMonth, int requestedYear, int requestedWeek) {

        Query qq = em.createNamedQuery("weeklyMRPRecord.findwMRPatMF");
        qq.setParameter("mf", mff);
        qq.setParameter("m", requestedMonth);
        qq.setParameter("y", requestedYear);
        qq.setParameter("w", requestedWeek);

        int qty = 0;
        for (WeeklyMRPRecord wppz : (List<WeeklyMRPRecord>) qq.getResultList()) {
            if (wppz.getQtyReq() > 0) {
                return true;
            }
        }

        return false;

    }

    public static boolean isOrderedMaterial(EntityManager em, ManufacturingFacility mff, Month requestedMonth, int requestedYear) {

        for (int i = 1; i <= Helper.getNumOfWeeks(requestedMonth.value, requestedYear); i++) {
            Query qq = em.createNamedQuery("weeklyMRPRecord.findwMRPatMF");
            qq.setParameter("mf", mff);
            qq.setParameter("m", requestedMonth);
            qq.setParameter("y", requestedYear);
            qq.setParameter("w", i);

            int qty = 0;
            for (WeeklyMRPRecord wppz : (List<WeeklyMRPRecord>) qq.getResultList()) {
                if (wppz.getQtyReq() > 0) {
                    return true;
                }
            }

        }
        return false;
    }

    public static boolean isMaterialWeekLocked(EntityManager em, ManufacturingFacility mf, Month requestedMonth, int requestedYear, int requestedWeek) {

        Query q = em.createQuery("select wmrp from WeeklyMRPRecord wmrp where wmrp.week=:w and wmrp.month=:m and wmrp.year=:y and wmrp.manufacturingFacility=:mf and wmrp.purchaseOrderDetail IS NOT NULL");
        q.setParameter("mf", mf);
        q.setParameter("w", requestedWeek);
        q.setParameter("m", requestedMonth);
        q.setParameter("y", requestedYear);
        return (q.getResultList().size() > 0);
    }

    public static boolean isMaterialWeekPermanentLocked(EntityManager em, ManufacturingFacility mf, Month requestedMonth, int requestedYear, int requestedWeek) {
        Query q = em.createQuery("select wmrp from WeeklyMRPRecord wmrp where wmrp.week=:w and wmrp.month=:m and wmrp.year=:y and wmrp.manufacturingFacility=:mf and wmrp.purchaseOrderDetail.purchaseOrder IS NOT NULL");

        q.setParameter("mf", mf);
        q.setParameter("w", requestedWeek);
        q.setParameter("m", requestedMonth);
        q.setParameter("y", requestedYear);

        for (WeeklyMRPRecord wmrp : (List<WeeklyMRPRecord>) q.getResultList()) {

            if (wmrp.getPurchaseOrderDetail() != null) {
                if (wmrp.getPurchaseOrderDetail().getPurchaseOrder() != null) {
                    return true;
                }
            }
        }

        return (false);
    }

    public static ProcuredStockSupplier getSupplierByMfAndM(EntityManager em, ManufacturingFacility mf, Material mat) {

        Query q = em.createNamedQuery("ProcuredStockContract.getSupplierForMFAndMaterial");
        q.setParameter("mf", mf);
        q.setParameter("ma", mat);
        ProcuredStockContract pc = (ProcuredStockContract) q.getResultList().get(0);
        return (pc.getSupplier());

    }

    public static HashMap<Plant, Long> tracePOToPlant(EntityManager em, ProductionOrder PO) {
//Cannot confirm no bug but test it out bah . GIT ISSUE #2

        HashMap<Plant, Long> returnObj = new HashMap<Plant, Long>();

        Query q = em.createQuery("SELECt wpp from WeeklyProductionPlan wpp where wpp.productionOrder=:PO");
        q.setParameter("PO", PO);

        WeeklyProductionPlan wpp = (WeeklyProductionPlan) q.getResultList().get(0);

        return traceWPPToPlant(em, wpp);

    }

    public static HashMap<Plant, Long> traceWPPToPlant(EntityManager em, WeeklyProductionPlan wpp) {
//Cannot confirm no bug but test it out bah . GIT ISSUE #2

        HashMap<Plant, Long> returnObj = new HashMap<Plant, Long>();

        long month_demand = QueryMethods.getTotalDemand(em, wpp.getMonthlyProductionPlan(), wpp.getMonthlyProductionPlan().getManufacturingFacility());

        List<MonthlyStockSupplyReq> MSSR_List = getRelevantMssrAtPT(em, wpp.getMonthlyProductionPlan().getMonth().value, wpp.getMonthlyProductionPlan().getYear(), wpp.getMonthlyProductionPlan().getManufacturingFacility(), wpp.getMonthlyProductionPlan().getFurnitureModel());

        HashMap<Plant, Long> roundingAdjustment = new HashMap<Plant, Long>();

        //last week procedure
        if (Helper.getNumOfWeeks(wpp.getMonthlyProductionPlan().getMonth().value, wpp.getMonthlyProductionPlan().getYear()) == wpp.getWeekNo()) {
            for (int i = 1; i < wpp.getWeekNo(); i++) {
                for (int j = 0; j < wpp.getMonthlyProductionPlan().getWeeklyProductionPlans().size(); j++) {
                    if (wpp.getMonthlyProductionPlan().getWeeklyProductionPlans().get(j).getWeekNo() == i) {
                        //action here
                        HashMap<Plant, Long> temp = traceWPPToPlant(em, wpp.getMonthlyProductionPlan().getWeeklyProductionPlans().get(j));

                        for (Plant p : temp.keySet()) {
                            if (roundingAdjustment.get(p) == null) {
                                roundingAdjustment.put(p, 0L);
                            }
                            roundingAdjustment.put(p, roundingAdjustment.get(p) + temp.get(p));
                        }

                    }

                }
            }

        }

        int distr = 0;
        int total_req = 0;

        ArrayList<MonthlyStockSupplyReq> mssr_l2 = MSSR_List.stream().filter(mssr -> mssr.getStock().equals(wpp.getMonthlyProductionPlan().getFurnitureModel())).collect(Collectors.toCollection(ArrayList::new));

        for (MonthlyStockSupplyReq mssr : mssr_l2) {
            if (roundingAdjustment.get(mssr.getCountryOffice()) != null) {
                Double add = ((mssr.getQtyRequested() + 0.0) / month_demand) * wpp.getMonthlyProductionPlan().getQTY() - roundingAdjustment.get(mssr.getCountryOffice());
                returnObj.put(mssr.getCountryOffice(), add.longValue());
                distr += ((mssr.getQtyRequested() + 0.0) / month_demand) * wpp.getMonthlyProductionPlan().getQTY() - roundingAdjustment.get(mssr.getCountryOffice());
            } else {
                Double weight = (mssr.getQtyRequested() + 0.0) / month_demand;
                weight *= wpp.getQTY();
                returnObj.put(mssr.getCountryOffice(), weight.longValue());
                distr += weight;
            }
        }

        //add undistributed qty to remaining
        if (Helper.getNumOfWeeks(wpp.getMonthlyProductionPlan().getMonth().value, wpp.getMonthlyProductionPlan().getYear()) != wpp.getWeekNo()) {
            if (wpp.getQTY() - distr > 0) {
                for (MonthlyStockSupplyReq mssr : MSSR_List) {
                    if (mssr.getQtyRequested() > wpp.getQTY() - distr) {
                        returnObj.put(mssr.getCountryOffice(), returnObj.get(mssr.getCountryOffice()) + wpp.getQTY() - distr);
                        break;
                    }
                }
            }
        }

//        //adjustment for manual edit
//        if (wpp.getQTY() != distr) {
//
//            for (Plant p : returnObj.keySet()) {
//                Double adjusted = returnObj.get(p) * (wpp.getQTY() + 0.0) / (total_req + 0.0);
//                returnObj.put(p, adjusted.longValue());
//            }
//        }
//
        return returnObj;

    }

}
