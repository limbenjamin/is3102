/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.Manufacturing;

import IslandFurniture.EJB.Entities.BOMDetail;
import IslandFurniture.EJB.Entities.FurnitureModel;
import IslandFurniture.EJB.Entities.ManufacturingCapacity;
import IslandFurniture.EJB.Entities.ManufacturingFacility;
import IslandFurniture.EJB.Entities.Material;
import IslandFurniture.EJB.Entities.Month;
import IslandFurniture.EJB.Entities.MonthlyProductionPlan;
import IslandFurniture.EJB.Entities.MonthlyStockSupplyReq;
import IslandFurniture.EJB.Entities.ProcurementContractDetail;
import IslandFurniture.EJB.Entities.ProductionCapacity;
import IslandFurniture.EJB.Entities.ProductionOrder;
import IslandFurniture.EJB.Entities.PurchaseOrder;
import IslandFurniture.EJB.Entities.StockSupplied;
import IslandFurniture.EJB.Entities.Supplier;
import IslandFurniture.EJB.Entities.WeeklyProductionPlan;
import IslandFurniture.EJB.Exceptions.ProductionPlanExceedsException;
import IslandFurniture.EJB.Exceptions.ProductionPlanNoCN;
import IslandFurniture.StaticClasses.Helper.Helper;
import IslandFurniture.StaticClasses.Helper.QueryMethods;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.ejb.Stateful;
import javax.ejb.StatefulTimeout;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author James This powerful Bean does production planning on a specific
 * manufacturing facility
 */
@Stateful
@StatefulTimeout(unit = TimeUnit.MINUTES, value = 30)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ManageProductionPlanning implements ManageProductionPlanningRemote, ManageProductionPlanningLocal {

    public static final int FORWARDLOCK = 1; //This determine how many months in advance production planning is locked

    @PersistenceContext(unitName = "IslandFurniture")
    private EntityManager em;
    private ManufacturingFacility MF = null;

    private void persist(Object object) {
        em.persist(object);
    }

    @Override
    public void setMF(String MF_NAME) throws Exception {
        ManufacturingFacility mf = null;
        try {
            Query q = em.createQuery("select MF from ManufacturingFacility MF where MF.name=:MF_NAME");
            q.setParameter("MF_NAME", MF_NAME);
            mf = (ManufacturingFacility) q.getResultList().get(0);
            this.MF = mf;
        } catch (Exception ex) {
            throw new Exception("ManageProductionPlanning(): No such manufacturing facility = " + MF_NAME);

        }

        System.out.println("ManageProductionPlanning(): Plant set to " + MF_NAME);

    }

    @Override
    public void createCapacityIFDoNotExist() {
        System.out.println("createCapacityIFDoNotExist() Iterating through to fill up missing capacity !");
        Query l = em.createNamedQuery("StockSupplied.FindByMf");
        l.setParameter("mf", MF);
        for (StockSupplied ss : (List<StockSupplied>) l.getResultList()) {

            if (ss.getStock() instanceof FurnitureModel) {
                Query q = em.createNamedQuery("ProductionCapacity.findPC");
                q.setParameter("MFNAME", MF);
                q.setParameter("FMNAME", ss.getStock());

                if (q.getResultList().size() == 0) {
                    createOrUpdateCapacity(ss.getStock().getName(), MF.getName(), 0);
                }

            }
        }
    }

    @Override
    public void createOrUpdateCapacity(String fmName, String mancFacName, int daily_max_capacity) {

        FurnitureModel fm = Helper.getFirstObjectFromQuery("SELECT FM from FurnitureModel FM where FM.name='" + fmName + "'", em);
        ManufacturingFacility MF = Helper.getFirstObjectFromQuery("SELECT MF from ManufacturingFacility MF where MF.name='" + mancFacName + "'", em);
        Query q = em.createNamedQuery("ProductionCapacity.findPC");
        q.setParameter("MFNAME", MF);
        q.setParameter("FMNAME", fm);

        if (q.getResultList().size() > 0) {
            ProductionCapacity pc = (ProductionCapacity) q.getResultList().get(0);
            pc.setQty(daily_max_capacity);
            em.persist(pc);
            System.err.println("Successfully Updated Production Capacity: for " + mancFacName + "/" + fmName);
            return;
        }

        ProductionCapacity pc = new ProductionCapacity();
        pc.setFurnitureModel(fm);
        pc.setManufacturingFacility(MF);
        pc.setQty(daily_max_capacity);
        em.persist(pc);

        System.err.println("Successfully Created Production Capacity: for " + mancFacName + "/" + fmName);

    }

    @Override
    //Plan Production Planning 6 months in advance that are relevant to MF
    public void CreateProductionPlanFromForecast() throws Exception {
        int m = Helper.addMonth(Helper.getCurrentMonth(), Helper.getCurrentYear(), 6, true);
        int y = Helper.addMonth(Helper.getCurrentMonth(), Helper.getCurrentYear(), 6, false);
        try {

            System.out.println("CreateProductionPlanFromForecast(): Planning Production Planning 6 months in advance until " + Helper.translateMonth(m).toString() + "/" + y);
        } catch (Exception ex) {
        }
        CreateProductionPlanFromForecast(m, y);
    }
//Factory Perspective

    //Public method. Plan all requirements
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @Override
    public void CreateProductionPlanFromForecast(int m, int year) throws Exception {

        CreateProductionPlanFromForecast(QueryMethods.GetRelevantMSSR(em, this.MF, m, year));
        System.out.println("CreateProductionPlanFromForecast(): Planning Production until Year:" + year + " and Month: " + Helper.translateMonth(m).toString());

    }

    // Public method , pass a list of forecast to see if it is feasible.
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void CreateProductionPlanFromForecast(List<MonthlyStockSupplyReq> MSSRL) throws ProductionPlanExceedsException, ProductionPlanNoCN, Exception {
        if (MF == null) {
            throw new ProductionPlanNoCN();
        }

        if (MSSRL.size() == 0) {
            throw new Exception("NO Monthly Stock Supply to plan anything ! Try creating some stock supply requirement !");
        }

        //Start from last date to earlist date
        Comparator<MonthlyStockSupplyReq> byMY
                = new Comparator<MonthlyStockSupplyReq>() {
                    @Override
                    public int compare(MonthlyStockSupplyReq e1, MonthlyStockSupplyReq e2) {
                        return (Integer.compare((int) (e2.getYear() * 12 + e2.getMonth().value), (int) (e1.getYear() * 12 + e1.getMonth().value)));
                    }
                };

        Stream<MonthlyStockSupplyReq> sortedMSSRL = MSSRL.stream().sorted(byMY);

        boolean firstrow = true;

        for (Object o : sortedMSSRL.toArray()) {

            MonthlyStockSupplyReq mssr = (MonthlyStockSupplyReq) o;
            if (firstrow) {
                firstrow = false;

            }
            CreateProductionPlanFromForecast(mssr);
        }

        balanceProductionTill();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    private void CreateProductionPlanFromForecast(MonthlyStockSupplyReq MSSR) throws ProductionPlanExceedsException, Exception {

        int month = MSSR.getMonth().value;
        int Year = MSSR.getYear();
        FurnitureModel furnitureModel = (FurnitureModel) MSSR.getStock();

        MonthlyProductionPlan mpp = null;

        int c_year = Helper.getCurrentYear();
        int c_month = Helper.getCurrentMonth().value;
        long monthGap = (Year - c_year) * 12 + (month - c_month);

        long monthGap_semiannual = Math.max(monthGap, 6);
        //Loop to ensure get all MPP before are created up to the month

        for (int i = 0; i <= monthGap_semiannual; i++) {

            int i_month = Helper.addMonth(Helper.translateMonth(c_month), c_year, i, true);
            int i_year = Helper.addMonth(Helper.translateMonth(c_month), c_year, i, false);

            Query q = em.createQuery("select mpp from MonthlyProductionPlan mpp where mpp.manufacturingFacility=:mf and mpp.furnitureModel=:fm and mpp.year*12+mpp.month=:y*12+:i_m");
            q.setParameter("i_m", Helper.translateMonth(i_month));
            q.setParameter("y", i_year);
            q.setParameter("fm", MSSR.getStock());
            q.setParameter("mf", this.MF);

            if (q.getResultList().size() == 0) {
                Month eMonth = Helper.translateMonth(i_month);
                mpp = new MonthlyProductionPlan();
                mpp.setMonth(eMonth);
                mpp.setYear((int) i_year);
                mpp.setFurnitureModel((FurnitureModel) furnitureModel);
                mpp.setManufacturingFacility(MF);
                mpp.setQTY(0); //Brand New
                System.err.println("CreateProductionPlanFromForecast(): Created production planning for " + mpp.getFurnitureModel().getName() + " period: " + i_year + "/" + Helper.translateMonth(i_month).toString());

            } else {
                mpp = (MonthlyProductionPlan) q.getResultList().get(0);
            }

            if (i <= FORWARDLOCK) {
                mpp.setLocked(true); //prevent planning for this month
            }

            em.persist(mpp);
            em.flush();

        }

    } //End of iteration.

    @Override
    public double getAvailCapacity(int year, int m) throws Exception {
        return (year - Helper.getCurrentYear()) * 12 + (m - Helper.getCurrentMonth().value - FORWARDLOCK);
    }

    @Override
    public double getReqCapacity(int year, int m) throws Exception {
        //Calculate Required Capacity First
        double reqCapacity = 0;
        double sum = 0;

        for (Object o : QueryMethods.GetRelevantMSSR(em, this.MF, m, year)) {
            MonthlyStockSupplyReq mssr = (MonthlyStockSupplyReq) o;
            ProductionCapacity thismfc = MF.findProductionCapacity((FurnitureModel) mssr.getStock());
            if (thismfc == null) {
                System.err.println("WARNING: CAPACITY DATA DOES NOT EXIST FOR :" + mssr.getStock() + " AUTOMATICALLY CREATING !");
                createOrUpdateCapacity(mssr.getStock().getName(), this.MF.getName(), 0);
            }

            double maxCapacity = thismfc.getCapacity(mssr.getMonth(), mssr.getYear());
            reqCapacity += mssr.getQtyRequested() / maxCapacity;
            sum += mssr.getQtyRequested();
        }

        return (reqCapacity);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    private void balanceProductionTill() throws Exception {
        MonthlyProductionPlan latest = (MonthlyProductionPlan) em.createQuery("select mpp from MonthlyProductionPlan mpp where mpp.manufacturingFacility.name='" + MF.getName() + "' order by mpp.year*12 + mpp.month DESC").getResultList().get(0);
        balanceProductionTill(latest.getYear(), latest.getMonth().value);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    private void balanceProductionTill(int year, int m) throws Exception {

        Query q = em.createQuery("select mpp from MonthlyProductionPlan mpp where mpp.manufacturingFacility=:mf and mpp.year*12+mpp.month=:year*12+:i_m order by mpp.furnitureModel.name");
        q.setParameter("mf", this.MF);
        q.setParameter("i_m", Helper.translateMonth(m));
        q.setParameter("year", year);

        double reqCapacity = getReqCapacity(year, m);
        double AvaCapacity = getAvailCapacity(year, m);

        if (reqCapacity > AvaCapacity) {
            throw new RuntimeException("Insufficient Capacity to fufill current requirement till " + m + "/" + year);
        }

        //Set to zero whole chain of related capacity . else available capacity reading will be wrong
        for (MonthlyProductionPlan planTillMonthCursor : (List<MonthlyProductionPlan>) q.getResultList()) {
            while (QueryMethods.getPrevMonthlyProductionPlan(em, planTillMonthCursor) != null) {

                if (planTillMonthCursor.isLocked()) {
                    break;
                }
                planTillMonthCursor.setQTY(0);
                planTillMonthCursor = QueryMethods.getPrevMonthlyProductionPlan(em, planTillMonthCursor);
            }
        }

        for (MonthlyProductionPlan planTillMonthCursor : (List<MonthlyProductionPlan>) q.getResultList()) {

            int deficit = 0;
            String endmonth = planTillMonthCursor.getMonth().toString();
            while (QueryMethods.getPrevMonthlyProductionPlan(em, planTillMonthCursor) != null) {

                if (planTillMonthCursor.isLocked()) {
                    break;
                }
                int avaCapacity = planTillMonthCursor.getManufacturingFacility().findProductionCapacity(planTillMonthCursor.getFurnitureModel()).getCapacity(planTillMonthCursor.getMonth(), planTillMonthCursor.getYear());
                avaCapacity = (int) (avaCapacity * QueryMethods.getCurrentFreeCapacity(em, planTillMonthCursor.getManufacturingFacility(), planTillMonthCursor.getMonth(), planTillMonthCursor.getYear()));

                int requirement = (int) (QueryMethods.getTotalDemand(em, planTillMonthCursor, MF));
                int fufill = Math.min(requirement + deficit, avaCapacity);
                planTillMonthCursor.setQTY(fufill);
                deficit += requirement - fufill;

                System.out.println("balanceProductionTill(): Planned production for " + planTillMonthCursor.getFurnitureModel().getName() + " Period @ Year:" + planTillMonthCursor.getYear() + " month:" + planTillMonthCursor.getMonth().toString() + " PRODUCE=" + fufill + " Resultant Plant Free Capacity @" + QueryMethods.getCurrentFreeCapacity(em, planTillMonthCursor.getManufacturingFacility(), planTillMonthCursor.getMonth(), planTillMonthCursor.getYear()));

                planWeekMPP(planTillMonthCursor);
                em.persist(planTillMonthCursor);

                planTillMonthCursor = QueryMethods.getPrevMonthlyProductionPlan(em, planTillMonthCursor);
            }

            if (deficit > 0) {
                throw new RuntimeException("balanceProductionTill()Unknown Error: Unable to fufill requirements! Deficit by " + deficit + " For month till " + endmonth);
            }
        }

        System.out.println("balanceProductionTill(): Successful Production Planning !");

    }

    //Add a weeklyProduction Plan
    private WeeklyProductionPlan addWeeklyPlan(MonthlyProductionPlan mpp) {

        WeeklyProductionPlan wpp = null;
        try {

            wpp = new WeeklyProductionPlan();
            wpp.setMonthlyProductionPlan(mpp);
            wpp.setWeekNo(mpp.getWeeklyProductionPlans().size() + 1);

            em.persist(wpp);
        } catch (Exception ex) {
        }

        return wpp;

    }

    @Override
    public void planWeekMPP(String MF_NAME, String furniture_model_name, int m, long year) throws Exception {
        Query q = em.createNamedQuery("MonthlyProductionPlan.Find");
        Query k = em.createQuery("select mf from ManufacturingFacility mf where mf.name=:name");
        k.setParameter("name", MF_NAME);

        Query L = em.createQuery("select fm from FurnitureModel fm where fm.name=:name");
        L.setParameter("name", furniture_model_name);

        q.setParameter("mf", (ManufacturingFacility) k.getResultList().get(0));
        q.setParameter("m", Helper.translateMonth(m).value);
        q.setParameter("y", year);
        q.setParameter("fm", (FurnitureModel) L.getResultList().get(0));

        planWeekMPP((MonthlyProductionPlan) q.getResultList().get(0));

    }

    //This process plans MPP and split it evenly across weeks
    private void planWeekMPP(MonthlyProductionPlan mpp) throws Exception {

        int maxWeekNumber = Helper.getNumOfWeeks(mpp.getMonth().value, mpp.getYear());
        int maxDay = Helper.getNumWorkDays(mpp.getMonth(), mpp.getYear());
        int daysInLastWeek = Helper.getNumOfDaysInWeek(mpp.getMonth().value, mpp.getYear(), maxWeekNumber);

        int rPerWeek = (int) Math.floor((mpp.getQTY().doubleValue() / maxDay) * (maxDay - daysInLastWeek) / (maxWeekNumber - 1));

        for (WeeklyProductionPlan wpp : mpp.getWeeklyProductionPlans()) {
            em.remove(wpp);
        }

        mpp.getWeeklyProductionPlans().clear();

        for (int i = 1; i <= maxWeekNumber; i++) {

            WeeklyProductionPlan wp = addWeeklyPlan(mpp);
            if (i == maxWeekNumber) {
                wp.setQTY(mpp.getQTY() - rPerWeek * (maxWeekNumber - 1));
            } else {
                wp.setQTY(rPerWeek);
            }
            System.out.println("Planned Year:" + mpp.getYear() + " month:" + mpp.getMonth() + " Week: " + i + " Split Product=" + wp.getQTY());
            mpp.getWeeklyProductionPlans().add(wp);
        }

    }

    @Override
    public void commitWPP(Integer wppID) throws Exception {
        Query q = em.createQuery("SELECT wpp from WeeklyProductionPlan wpp where wpp.id=:id");
        q.setParameter("id", wppID);
        WeeklyProductionPlan wpp = (WeeklyProductionPlan) q.getResultList().get(0);

        ProductionOrder po = new ProductionOrder();
        po.setFurnitureModel(wpp.getMonthlyProductionPlan().getFurnitureModel());
        wpp.setProductionOrder(po);
        persist(po);

    }

    @Override
    public void uncommitWPP(Integer wppID) throws Exception {
        Query q = em.createQuery("SELECT wpp from WeeklyProductionPlan wpp where wpp.id=:id");
        q.setParameter("id", wppID);
        WeeklyProductionPlan wpp = (WeeklyProductionPlan) q.getResultList().get(0);

        ProductionOrder po = wpp.getProductionOrder();

        wpp.setProductionOrder(null);

        em.remove(po);

    }

    @Override
    public int getLotSize(Material m) {
        List<ProcurementContractDetail> pcs = MF.getSuppliedBy();
        for (ProcurementContractDetail pc : pcs) {
            if (pc.getProcuredStock().equals(m)) {
                return pc.getLotSize();
            }
        }

        return 1;

    }

    @Override
    public Supplier getSupplierSize(Material m) {
        List<ProcurementContractDetail> pcs = MF.getSuppliedBy();
        for (ProcurementContractDetail pc : pcs) {
            if (pc.getProcuredStock().equals(m)) {
                return pc.getProcurementContract().getSupplier();
            }
        }

        return null;

    }

    @Override
    public HashMap<Material, Long> getMaterialsNeeded(int WeekNo, int Year, int Month) {
        Query q = em.createQuery("SELECT wpp from WeeklyProductionPlan wpp where wpp.WeekNo=:wno and wpp.monthlyProductionPlan.month+wpp.monthlyProductionPlan.year*12=:m+:y*12 and wpp.monthlyProductionPlan.manufacturingFacility=:mf");
        q.setParameter("mf", this.MF);
        q.setParameter("y", Year);
        q.setParameter("m", Month);
        q.setParameter("wno", WeekNo);

        HashMap<Material, Long> material_map = new HashMap<Material, Long>();

        for (WeeklyProductionPlan wpp : (List<WeeklyProductionPlan>) q.getResultList()) {
            Query l = em.createQuery("select bomd from BOMDetail bomd where bomd.bom=:bom order by bomd.material.name ASC");
            l.setParameter("bom", wpp.getMonthlyProductionPlan().getFurnitureModel().getBom());
            for (BOMDetail bomd : (List<BOMDetail>) l.getResultList()) {
                if (material_map.get(bomd.getMaterial()) == null) {
                    material_map.put(bomd.getMaterial(), 0L);
                }
                System.out.println("getMaterialsNeeded(): WPPID: " + wpp.getId() + " Furniture " + bomd.getMaterial()+" "+ wpp.getMonthlyProductionPlan().getFurnitureModel().getName()
                        + " required quantity=" + bomd.getQuantity() * wpp.getQTY()
                );
                material_map.put(bomd.getMaterial(), material_map.get(bomd.getMaterial()) + bomd.getQuantity() * wpp.getQTY());

            }

        }

        return material_map;

    }

}
