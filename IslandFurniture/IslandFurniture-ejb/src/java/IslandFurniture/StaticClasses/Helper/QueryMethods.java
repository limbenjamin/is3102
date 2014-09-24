/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.StaticClasses.Helper;

import IslandFurniture.EJB.Entities.BOMDetail;
import IslandFurniture.EJB.Entities.Country;
import IslandFurniture.EJB.Entities.FurnitureModel;
import IslandFurniture.EJB.Entities.ManufacturingFacility;
import IslandFurniture.EJB.Entities.Material;
import IslandFurniture.EJB.Entities.Month;
import IslandFurniture.EJB.Entities.MonthlyProductionPlan;
import IslandFurniture.EJB.Entities.MonthlyStockSupplyReq;
import IslandFurniture.EJB.Entities.MonthlyStockSupplyReqPK;
import IslandFurniture.EJB.Entities.Plant;
import IslandFurniture.EJB.Entities.ProcuredStock;
import IslandFurniture.EJB.Entities.ProcurementContractDetail;
import IslandFurniture.EJB.Entities.RetailItem;
import IslandFurniture.EJB.Entities.Stock;
import IslandFurniture.EJB.Entities.StockSupplied;
import IslandFurniture.EJB.Entities.Store;
import IslandFurniture.EJB.Entities.Supplier;
import static IslandFurniture.EJB.Manufacturing.ManageProductionPlanning.FORWARDLOCK;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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

    public static Supplier findSupplierByName(EntityManager em, String supplierName) {
        Query q = em.createNamedQuery("findSupplierByName");
        q.setParameter("name", supplierName);

        try {
            return (Supplier) q.getSingleResult();
        } catch (NoResultException nrex) {
            return null;
        }
    }

    public static ProcurementContractDetail findPCDByStockMFAndSupplier(EntityManager em, ProcuredStock stock, ManufacturingFacility mf, Supplier s) {
        Query q = em.createNamedQuery("getProcurementContractDetailByStockMFAndSupplier", ProcurementContractDetail.class);
        q.setParameter("stock", stock);
        q.setParameter("mf", mf);
        q.setParameter("supplier", s);
        
        try {
            return (ProcurementContractDetail) q.getSingleResult();
        } catch (NoResultException NRE) {
            return null;
        }
    }
    public static MonthlyStockSupplyReq findNextMssr(EntityManager em, MonthlyStockSupplyReq mssr, int monthsOffset) {
        Calendar cal = TimeMethods.getCalFromMonthYear(mssr.getMonth(), mssr.getYear());
        cal.add(Calendar.MONTH, monthsOffset);

        MonthlyStockSupplyReqPK mssrPK = new MonthlyStockSupplyReqPK(mssr.getStock().getId(), mssr.getCountryOffice().getId(), Month.getMonth(cal.get(Calendar.MONTH)), cal.get(Calendar.YEAR));

        return em.find(MonthlyStockSupplyReq.class, mssrPK);
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

    public static List<BOMDetail> getBOMDetailByMaterial(EntityManager em, Material m) {
        Query q = em.createNamedQuery("findBOMDetailByMaterial", BOMDetail.class);
        q.setParameter("m", m);

        return (List<BOMDetail>) q.getResultList();
    }

    public static List<MonthlyStockSupplyReq> GetRelevantMSSR(EntityManager em, ManufacturingFacility MF, int m, int year) {

        Query l = em.createNamedQuery("StockSupplied.FindByMf");
        l.setParameter("mf", MF);

        ArrayList<MonthlyStockSupplyReq> RelevantMSSR = new ArrayList<MonthlyStockSupplyReq>();

        for (StockSupplied ss : (List<StockSupplied>) l.getResultList()) {

            if (!(ss.getStock() instanceof FurnitureModel)) {
                continue;
            }

            Query q = em.createNamedQuery("MonthlyStockSupplyReq.FindByCoStockBefore");
            q.setParameter("y", year);
            try {
                q.setParameter("m", Helper.translateMonth(m).value);

            } catch (Exception ex) {
            }
            q.setParameter("nm", Helper.getCurrentMonth().value + FORWARDLOCK + 1); //2 months in advance
            q.setParameter("ny", Helper.getCurrentYear());
            q.setParameter("co", ss.getCountryOffice());
            q.setParameter("stock", ss.getStock());

            try {
                RelevantMSSR.addAll(q.getResultList());
            } catch (Exception err) {
            }

        }
        System.out.println("GetRelevantMSSR(): " + MF.getName() + " UNTIL " + m + "/" + year + " Returned:" + RelevantMSSR.size());
        return RelevantMSSR;
    }

    public static List<MonthlyStockSupplyReq> GetRelevantMSSRAtPT(EntityManager em, int m, int year, ManufacturingFacility MF, Stock s) {

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
        System.out.println("GetRelevantMSSRAtPT(): " + MF.getName() + " UNTIL " + m + "/" + year + " Returned:" + RelevantMSSR.size());
        return RelevantMSSR;
    }

    public static List<MonthlyStockSupplyReq> getMonthlyStockSupplyReqs(EntityManager em, MonthlyProductionPlan MPP, ManufacturingFacility MF) {
        try {

            return (GetRelevantMSSRAtPT(em, MPP.getMonth().value, MPP.getYear(), MF, MPP.getFurnitureModel()));

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
            throw new RuntimeException("This MSSR does not have a MPP yet");
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

}
