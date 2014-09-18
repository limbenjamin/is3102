/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.StaticClasses.Helper;

import IslandFurniture.EJB.Entities.Country;
import IslandFurniture.EJB.Entities.FurnitureModel;
import IslandFurniture.EJB.Entities.ManufacturingFacility;
import IslandFurniture.EJB.Entities.Material;
import IslandFurniture.EJB.Entities.Month;
import IslandFurniture.EJB.Entities.MonthlyProductionPlan;
import IslandFurniture.EJB.Entities.MonthlyStockSupplyReq;
import IslandFurniture.EJB.Entities.Plant;
import IslandFurniture.EJB.Entities.RetailItem;
import IslandFurniture.EJB.Entities.Stock;
import IslandFurniture.EJB.Entities.StockSupplied;
import IslandFurniture.EJB.Entities.Store;
import IslandFurniture.EJB.Manufacturing.ManageProductionPlanning;
import java.util.ArrayList;
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

    public static MonthlyStockSupplyReq findMssrByMonth(EntityManager em, Month month, int year) {
        return null;
    }

    public static List<MonthlyStockSupplyReq> GetRelevantMSSRAtPT(EntityManager em, int m, int year, ManufacturingFacility MF, Stock s) {





        Query q = em.createNamedQuery("MonthlyStockSupplyReq.FindByCoStockAT");
        q.setParameter("y", year);
        try {
            q.setParameter("m", Helper.translateMonth(m).value);

        } catch (Exception ex) {
        }
        q.setParameter("co", MF.getCountryOffice());
        q.setParameter("stock", s);
        List<MonthlyStockSupplyReq> RelevantMSSR=(List<MonthlyStockSupplyReq>) q.getResultList();
        System.out.println("GetRelevantMSSRAtPT(): " + MF.getName() + " UNTIL " + m + "/" + year +" Returned:"+RelevantMSSR.size());
        return (RelevantMSSR);
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
            q.setParameter("m", Helper.translateMonth(Helper.addMonth(MPP.getMonth(), MPP.getYear(), 1, true)));
            q.setParameter("y", Helper.addMonth(MPP.getMonth(), MPP.getYear(), 1, false));
            q.setParameter("fm", MPP.getFurnitureModel());
            return (MonthlyProductionPlan) q.getResultList().get(0);
        } catch (Exception ex) {
            return (null);
        }

    }

    public static MonthlyProductionPlan getPrevMonthlyProductionPlan(EntityManager em, MonthlyProductionPlan MPP) {

        try {
            Query q = em.createNamedQuery("MonthlyProductionPlan.Find");
            q.setParameter("m", Helper.translateMonth(Helper.addMonth(MPP.getMonth(), MPP.getYear(), -1, true)));
            q.setParameter("y", Helper.addMonth(MPP.getMonth(), MPP.getYear(), -1, false));
            q.setParameter("fm", MPP.getFurnitureModel());

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

}
