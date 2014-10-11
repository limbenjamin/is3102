/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.Manufacturing;

import IslandFurniture.Entities.BOMDetail;
import IslandFurniture.Entities.ExternalTransferOrder;
import IslandFurniture.Entities.ExternalTransferOrderDetail;
import IslandFurniture.Entities.FurnitureModel;
import IslandFurniture.Entities.ManufacturingFacility;
import IslandFurniture.Entities.Material;
import IslandFurniture.Enums.Month;
import IslandFurniture.Entities.MonthlyProductionPlan;
import IslandFurniture.Entities.MonthlyStockSupplyReq;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.ProcuredStockContractDetail;
import IslandFurniture.Entities.ProcuredStockSupplier;
import IslandFurniture.Enums.ProdOrderStatus;
import IslandFurniture.Entities.ProductionCapacity;
import IslandFurniture.Entities.ProductionOrder;
import IslandFurniture.Entities.ProcuredStockPurchaseOrder;
import IslandFurniture.Entities.ProcuredStockPurchaseOrderDetail;
import IslandFurniture.Entities.StockSupplied;
import IslandFurniture.Entities.WeeklyMRPRecord;
import IslandFurniture.Entities.WeeklyProductionPlan;
import IslandFurniture.Enums.TransferOrderStatus;
import IslandFurniture.Exceptions.ProductionPlanExceedsException;
import IslandFurniture.Exceptions.ProductionPlanNoCN;
import IslandFurniture.StaticClasses.Helper;
import static IslandFurniture.StaticClasses.Helper.getWeekNoFromDate;
import static IslandFurniture.StaticClasses.Helper.workingDaysInWeek;
import IslandFurniture.StaticClasses.QueryMethods;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import javax.ejb.Stateful;
import javax.ejb.StatefulTimeout;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author James This powerful Bean does production planning on a specific
 * manufacturing facility Note to James: Future today. WPP to mpp translation .
 * take care of boundary condition wpp boundary . take care of rounding off
 * effect
 */
@Stateful
@StatefulTimeout(unit = TimeUnit.MINUTES, value = 30)
public class ManageProductionPlanning implements ManageProductionPlanningLocal {

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
    public ProductionCapacity createOrUpdateCapacity(String fmName, String mancFacName, int daily_max_capacity) {

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
            return (pc);
        }

        ProductionCapacity pc = new ProductionCapacity();
        pc.setFurnitureModel(fm);
        pc.setManufacturingFacility(MF);
        pc.setQty(daily_max_capacity);
        em.persist(pc);

        System.err.println("Successfully Created Production Capacity: for " + mancFacName + "/" + fmName);
        return (pc);
    }

    @Override
    public void createOrUpdateCapacityExternal(String fmName, String mancFacName, int daily_max_capacity) {
        createOrUpdateCapacity(fmName, mancFacName, daily_max_capacity);
        return;
    }

    @Override
    //Plan Production Planning 6 months in advance that are relevant to MF
    public void CreateProductionPlanFromForecast() throws Exception {
        int m = Helper.addMonth(Helper.getCurrentMonth(), Helper.getCurrentYear(), 6, true);
        int y = Helper.addMonth(Helper.getCurrentMonth(), Helper.getCurrentYear(), 6, false);
        System.out.println("CreateProductionPlanFromForecast(): Planning Production Planning 6 months in advance until " + Helper.translateMonth(m).toString() + "/" + y);
        CreateProductionPlanFromForecast(m, y);
    }
//Factory Perspective

    //Public method. Plan all requirements
    @Override
    public void CreateProductionPlanFromForecast(int m, int year) throws Exception {

        List<MonthlyStockSupplyReq> list = QueryMethods.getRelevantMSSR(em, this.MF, m, year);
        if (list.size() == 0) {
            throw new Exception("CreateProductionPlanFromForecast(): Warning No MSSR !");
        }

        CreateProductionPlanFromForecast(list);
        System.out.println("CreateProductionPlanFromForecast(): Planning Production until Year:" + year + " and Month: " + Helper.translateMonth(m).toString());

    }

    // Public method , pass a list of forecast to see if it is feasible.
    public void CreateProductionPlanFromForecast(List<MonthlyStockSupplyReq> MSSRL) throws ProductionPlanExceedsException, ProductionPlanNoCN, Exception {
        if (MF == null) {
            throw new ProductionPlanNoCN();
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

        for (Object o : QueryMethods.getRelevantMSSR(em, this.MF, m, year)) {
            MonthlyStockSupplyReq mssr = (MonthlyStockSupplyReq) o;
            ProductionCapacity thismfc = MF.findProductionCapacity((FurnitureModel) mssr.getStock());
            if (thismfc == null) {
                System.err.println("WARNING: CAPACITY DATA DOES NOT EXIST FOR :" + mssr.getStock() + " AUTOMATICALLY CREATING !");
                thismfc = createOrUpdateCapacity(mssr.getStock().getName(), this.MF.getName(), 0);
            }

            double maxCapacity = thismfc.getCapacity(mssr.getMonth(), mssr.getYear());
            reqCapacity += mssr.getQtyRequested() / maxCapacity;
            sum += mssr.getQtyRequested();
        }

        return (reqCapacity);
    }

    private void balanceProductionTill() throws Exception {
        MonthlyProductionPlan latest = (MonthlyProductionPlan) em.createQuery("select mpp from MonthlyProductionPlan mpp where mpp.manufacturingFacility.name='" + MF.getName() + "' order by mpp.year*12 + mpp.month DESC").getResultList().get(0);
        balanceProductionTill(latest.getYear(), latest.getMonth().value);
    }

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

        for (WeeklyProductionPlan wpp : mpp.getWeeklyProductionPlans()) {
            em.remove(wpp);
        }
        Integer sum_produce = 0;

        mpp.getWeeklyProductionPlans().clear();

        for (int i = 1; i <= maxWeekNumber; i++) {

//            WeeklyProductionPlan wp = addWeeklyPlan(mpp);
//            if (i == maxWeekNumber) {
//                Double produce = 0.0;
//                Double Nextproduce = 0.0;
//                if (QueryMethods.getNextMonthlyProductionPlan(em, mpp) != null) {
//
//                    Nextproduce = ((QueryMethods.getNextMonthlyProductionPlan(em, mpp).getQTY() + 0.0) / QueryMethods.getNextMonthlyProductionPlan(em, mpp).getNumWorkDays()) * (workingDaysInWeek + 0.0);
//                }
//                Double normalProduce = mpp.getQTY() * ((workingDaysInWeek + 0.0) / mpp.getNumWorkDays());
//                Double w1 = (Helper.getBoundaryWeekDays(mpp.getMonth(), mpp.getYear()) / 7.0);
//                Double w2 = 1 - w1;
//                produce = normalProduce * w1 + w2 * Nextproduce;
//                wp.setQTY(produce.intValue());
//
//                System.out.println("Boundary Case: Planned Year:" + mpp.getYear() + " month:" + mpp.getMonth() + " Week: " + i + " Split Product=" + wp.getQTY());
//                System.out.println("Current Month produce=" + normalProduce + " Next Month PRoduce= " + Nextproduce);
//                System.out.println("First weight = " + w1);
//                System.out.println("Second weight = " + w2);
//
//            } else {
//                Double produce = mpp.getQTY() * ((workingDaysInWeek + 0.0) / mpp.getNumWorkDays());
//                wp.setQTY(produce.intValue());
//                System.out.println("Planned Year:" + mpp.getYear() + " month:" + mpp.getMonth() + " Week: " + i + " Split Product=" + wp.getQTY());
//            }
            WeeklyProductionPlan wp = addWeeklyPlan(mpp);
            Double produce = 0.0;
            if (i < maxWeekNumber) {
                produce = mpp.getQTY() * ((workingDaysInWeek + 0.0) / mpp.getNumWorkDays());
            } else {
                produce = mpp.getQTY() - sum_produce.doubleValue();
            }
            sum_produce += produce.intValue();

            wp.setQTY(produce.intValue());
            System.out.println("Planned Year:" + mpp.getYear() + " month:" + mpp.getMonth() + " Week: " + i + " Split Product=" + wp.getQTY());
            mpp.getWeeklyProductionPlans().add(wp);
        }

    }

    @Override
    public void commitWPP(Integer wppID) throws Exception {
        Query q = em.createQuery("SELECT wpp from WeeklyProductionPlan wpp where wpp.id=:id");
        q.setParameter("id", wppID);
        WeeklyProductionPlan wpp = (WeeklyProductionPlan) q.getResultList().get(0);
        
        if (wpp.getQTY()<=0) return;

        ProductionOrder po = new ProductionOrder();
        po.setFurnitureModel(wpp.getMonthlyProductionPlan().getFurnitureModel());
        po.setStatus(ProdOrderStatus.PLANNED);
        po.setQty(wpp.getQTY());
        //Fixes for Missing PO
        Calendar ca = Calendar.getInstance();
        ca.set(wpp.getMonthlyProductionPlan().getYear(), wpp.getMonthlyProductionPlan().getMonth().value, 1);
        ca.set(Calendar.WEEK_OF_MONTH, wpp.getWeekNo());
        po.setProdOrderDate(ca);
        po.setMf(MF);
        wpp.setProductionOrder(po);
         persist(po);

        HashMap<Plant, Long> orders = QueryMethods.traceWPPToPlant(em, wpp);

        for (Plant p : orders.keySet()) {

            Query ll = em.createQuery("Select eto from ExternalTransferOrder eto where eto.remark=:r");
            ll.setParameter("r", "WPP:" + wpp.getId()+" Plant:"+p.getName());

            ExternalTransferOrder eto = null;
            if (ll.getResultList().size() == 0) {
                eto = new ExternalTransferOrder();
                eto.setFulfillingPlant(this.MF);
                eto.setRequestingPlant(p);
                eto.setStatus(TransferOrderStatus.REQUESTED);
                eto.setRemark("WPP:" + wpp.getId()+" Plant:"+p.getName());
                persist(eto);
            } else {
                eto = (ExternalTransferOrder) ll.getResultList().get(0);
            }
            ExternalTransferOrderDetail etod = new ExternalTransferOrderDetail();
            etod.setExtTransOrder(eto);
            etod.setQty(orders.get(p).intValue());
            etod.setStock(wpp.getMonthlyProductionPlan().getFurnitureModel());
            persist(etod);
        }

        System.out.println("commitWPP(): " + wppID);

    }

    @Override
    public void uncommitWPP(Integer wppID) throws Exception {
        Query q = em.createQuery("SELECT wpp from WeeklyProductionPlan wpp where wpp.id=:id");
        q.setParameter("id", wppID);
        WeeklyProductionPlan wpp = (WeeklyProductionPlan) q.getResultList().get(0);

        ProductionOrder po = wpp.getProductionOrder();

        wpp.setProductionOrder(null);
        
            HashMap<Plant, Long> orders = QueryMethods.traceWPPToPlant(em, wpp);

        for (Plant p : orders.keySet()) {

            Query ll = em.createQuery("Select eto from ExternalTransferOrder eto where eto.remark=:r");
            ll.setParameter("r", "WPP:" + wpp.getId()+" Plant:"+p.getName());

            ExternalTransferOrder eto = null;
            if (ll.getResultList().size() == 0) {

            } else {
                eto = (ExternalTransferOrder) ll.getResultList().get(0);
            }
            
            
            for (ExternalTransferOrderDetail etod: eto.getExtTransOrderDetails())
            {
                em.remove(etod);
            }
            
            em.remove(eto);
            
        }
        

        if (po == null) {
            return;
        }
        em.remove(po);
        em.merge(wpp);
        System.out.println("uncommitWPP(): " + wppID);
    }

    @Override
    public int getLotSize(Material m) {
        List<ProcuredStockContractDetail> pcs = MF.getSuppliedBy();
        for (ProcuredStockContractDetail pc : pcs) {
            if (pc.getProcuredStock().equals(m)) {
                return pc.getLotSize();
            }
        }

        return 1;

    }

    @Override
    public ProcuredStockSupplier getSupplierSize(Material m) {
        List<ProcuredStockContractDetail> pcs = MF.getSuppliedBy();
        for (ProcuredStockContractDetail pc : pcs) {
            if (pc.getProcuredStock().equals(m)) {
                return pc.getProcurementContract().getSupplier();
            }
        }

        return null;

    }

    @Override
    public HashMap<Material, Long> getMaterialsNeededForCommited(int WeekNo, int Year, int Month) {
        Query q = em.createQuery("SELECT wpp from WeeklyProductionPlan wpp where wpp.WeekNo=:wno and wpp.productionOrder!=null and wpp.monthlyProductionPlan.month+wpp.monthlyProductionPlan.year*12=:m+:y*12 and wpp.monthlyProductionPlan.manufacturingFacility=:mf");
        q.setParameter("mf", this.MF);
        q.setParameter("y", Year);
        q.setParameter("m", Month);
        q.setParameter("wno", WeekNo);

        HashMap<Material, Long> material_map = new HashMap<Material, Long>();

        for (WeeklyProductionPlan wpp : (List<WeeklyProductionPlan>) q.getResultList()) {

            if (wpp.getQTY() <= 0) {
                continue;
            }

            Query l = em.createQuery("select bomd from BOMDetail bomd where bomd.bom=:bom order by bomd.material.name DESC");
            l.setParameter("bom", wpp.getMonthlyProductionPlan().getFurnitureModel().getBom());
            for (BOMDetail bomd : (List<BOMDetail>) l.getResultList()) {
                if (material_map.get(bomd.getMaterial()) == null) {
                    material_map.put(bomd.getMaterial(), 0L);
                }
                System.out.println("getMaterialsNeeded(): WPPID: " + wpp.getId() + " Furniture " + bomd.getMaterial() + " " + wpp.getMonthlyProductionPlan().getFurnitureModel().getName()
                        + " required quantity=" + bomd.getQuantity() * wpp.getQTY()
                );
                material_map.put(bomd.getMaterial(), material_map.get(bomd.getMaterial()) + bomd.getQuantity() * wpp.getQTY());

            }

        }

        return material_map;

    }

    @Override
    public void orderMaterials(int weekNo, int monthNo, int YearNo) throws Exception {

        HashMap<Material, Long> hm = getMaterialsNeededForCommited(weekNo, YearNo, monthNo);

        for (Material m : (Set<Material>) hm.keySet()) {
            
            if (hm.get(m).intValue()==0) continue;
            WeeklyMRPRecord first = null;

            //Check if a wmrp already exists and if so use the current one then.
            Query q = em.createNamedQuery("weeklyMRPRecord.findwMRPatMFM");
            q.setParameter("mf", MF);
            q.setParameter("y", YearNo);
            q.setParameter("m", monthNo);
            q.setParameter("w", weekNo);
            q.setParameter("ma", m);

            WeeklyMRPRecord wMRP = null;
            if (q.getResultList().size() > 0) {

                WeeklyMRPRecord old = (WeeklyMRPRecord) q.getResultList().get(0);
                wMRP = old;
                System.out.println("orderMaterials(): Using existing wMRP records for " + weekNo + " " + monthNo + " " + YearNo);
            } else {
                wMRP = new WeeklyMRPRecord();
                wMRP.setMaterial(m);
                wMRP.setMonth(Helper.translateMonth(monthNo));
                wMRP.setWeek(weekNo);
                wMRP.setYear(YearNo);
                wMRP.setManufacturingFacility(MF);
            }

            wMRP.setQtyReq(hm.get(m).intValue());
            wMRP.setLotSize(getLotSize(m));
            wMRP.setLeadTime(getLeadTime(m));

            if (q.getResultList().size() > 0) {
                em.merge(wMRP);
                System.err.println("orderMaterials(): merged !");
            } else {
                em.persist(wMRP);
                System.err.println("orderMaterials(): Persisted !");
            }

            //Calculate the order date base on lead time.
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.WEEK_OF_MONTH, weekNo);
            cal.set(Calendar.YEAR, YearNo);
            cal.set(Calendar.MONTH, monthNo);
            Calendar order_date = ((Calendar) cal.clone());
            order_date.add(Calendar.DATE, -getLeadTime(m));

            if (order_date.getTime().before(Calendar.getInstance().getTime())) {
                throw (new Exception("Material " + m.getName() + " cannot be ordered in time !"));
            }

            //get first WMRP 
            Calendar cal_first = Calendar.getInstance();

            Query jk = em.createQuery("select wmrp from WeeklyMRPRecord wmrp where wmrp.manufacturingFacility=:mf and wmrp.material=:ma order by wmrp.month*4+wmrp.year*52+wmrp.week asc");
            jk.setParameter("mf", wMRP.getManufacturingFacility());
            jk.setParameter("ma", wMRP.getMaterial());

            if (!(jk.getResultList().size() == 0)) {
                WeeklyMRPRecord firstRecord = (WeeklyMRPRecord) jk.getResultList().get(0);
                cal_first.set(Calendar.WEEK_OF_MONTH, firstRecord.getWeek());
                cal_first.set(Calendar.YEAR, firstRecord.getYear());
                cal_first.set(Calendar.MONTH, firstRecord.getMonth().value);

                if (firstRecord.equals(wMRP)) {
                    first = wMRP;
                } else {
                    first = firstRecord;
                }
            }

            Calendar calendarPointer = Calendar.getInstance();
            calendarPointer.set(Calendar.WEEK_OF_MONTH, order_date.get(Calendar.WEEK_OF_MONTH));
            calendarPointer.set(Calendar.YEAR, order_date.get(Calendar.YEAR));
            calendarPointer.set(Calendar.MONTH, order_date.get(Calendar.MONTH));
            calendarPointer.setFirstDayOfWeek(Calendar.MONDAY);

            Calendar begining_of_month = Calendar.getInstance();
            begining_of_month.set(YearNo, monthNo, 1);

            //today
            if (cal_first.before(calendarPointer)) {
                calendarPointer = cal_first;
            }

            if (begining_of_month.before(cal_first)) {
                calendarPointer = begining_of_month;
            }

            int targetpt = wMRP.getYear() * 1000 + wMRP.getMonth().value * 10 + Helper.getNumOfWeeks(wMRP.getMonth().value, wMRP.getYear()); //make sure the whole month wmrp is generated
            int i_w = calendarPointer.get(Calendar.WEEK_OF_MONTH);
            int i_m = calendarPointer.get(Calendar.MONTH);
            int i_y = calendarPointer.get(Calendar.YEAR);

            int currentpt = i_m * 10 + i_w + i_y * 1000;

            //Fill up the gap before the first one or before the order...
            while (currentpt <= targetpt) {

                Query z = em.createNamedQuery("weeklyMRPRecord.findwMRPatMFM");
                z.setParameter("mf", wMRP.getManufacturingFacility());
                z.setParameter("y", i_y);
                z.setParameter("m", i_m);
                z.setParameter("w", i_w);
                z.setParameter("ma", wMRP.getMaterial());
                WeeklyMRPRecord dummy_wMRP = null;
                if (z.getResultList().size() > 0) {
                    dummy_wMRP = (WeeklyMRPRecord) z.getResultList().get(0);

                } else {
                    dummy_wMRP = new WeeklyMRPRecord();
                    dummy_wMRP.setMaterial(wMRP.getMaterial());
                    dummy_wMRP.setMonth(Helper.translateMonth(i_m));
                    dummy_wMRP.setYear(i_y);
                    dummy_wMRP.setWeek(i_w);
                    dummy_wMRP.setManufacturingFacility(wMRP.getManufacturingFacility());
                    dummy_wMRP.setQtyReq(0);
                    dummy_wMRP.setLeadTime(getLeadTime(m));
                    dummy_wMRP.setLotSize(getLotSize(m));
                    em.persist(dummy_wMRP);
                    System.out.println("orderMaterials(): Created Blank WeeklyMRP For: Week:" + dummy_wMRP.getWeek() + "Month:" + dummy_wMRP.getMonth().value + "year:" + dummy_wMRP.getYear() + " for material" + m.getName());

                }

                if (first == null) {
                    first = dummy_wMRP;
                }

                int temp_i_w = Helper.addoneWeek(i_m, i_y, i_w, 1, Calendar.WEEK_OF_MONTH);
                int temp_i_m = Helper.addoneWeek(i_m, i_y, i_w, 1, Calendar.MONTH);
                int temp_i_y = Helper.addoneWeek(i_m, i_y, i_w, 1, Calendar.YEAR);

                i_w = temp_i_w;
                i_m = temp_i_m;
                i_y = temp_i_y;
                currentpt = i_m * 10 + i_w + i_y * 1000;
            }
            //Recalculate the whole chain
            cascadeWMRP(first);

            System.out.println("orderMaterials(): Success For: Week:" + weekNo + "Month:" + monthNo + "year:" + YearNo);
        }
    }

    private void calculatePO(WeeklyMRPRecord wMRP) {
        while (wMRP != null) {
            wMRP.setPlannedOrder(QueryMethods.getOrderedatwMRP(em, wMRP));
            em.merge(wMRP);
            wMRP = QueryMethods.getPrevWMRP(em, wMRP);

        }

    }

    //this is a powerful function to cascade wMRP
    private void cascadeWMRP(WeeklyMRPRecord wMRP) throws Exception {

        Boolean begin = true;
        WeeklyMRPRecord current = null;
        WeeklyMRPRecord last = null;
        //Cascade value to next few wMRP and order correctly if needed
        while (current != null || begin) {
            if (!begin) {
                current = QueryMethods.getNextWMRP(em, current);

                if (current == null) {
                    break;
                }

            } else {
                current = wMRP;
                begin = false;
            }
            last = current;

            WeeklyMRPRecord prev = QueryMethods.getPrevWMRP(em, current);

            if (prev == null) {
                prev = current;
                prev.setOnHand(0);
            } //this is the first WMRP record
//
            current.setLotSize(prev.getLotSize()); //cascade these
            current.setLeadTime(prev.getLeadTime()); //cascade these
            //Calculate the order date base on lead time.
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.WEEK_OF_MONTH, current.getWeek());
            cal.set(Calendar.YEAR, current.getYear());
            cal.set(Calendar.MONTH, current.getMonth().value);
            cal.set(Calendar.DAY_OF_WEEK, 1);
            cal.setFirstDayOfWeek(Calendar.MONDAY);

            Double orderday = Math.ceil(getLeadTime(current.getMaterial()) / 7.0) * 7.0;
            Calendar order_date = Helper.addWeek(current.getMonth().value, current.getYear(), current.getWeek(), -orderday.intValue());

            try {
                current.setOrderYear(order_date.get(Calendar.YEAR));
                current.setOrderMonth(Helper.translateMonth(order_date.get(Calendar.MONTH)));
                current.setOrderWeek(getWeekNoFromDate(order_date));
            } catch (Exception ex) {
            }

            Integer Required = Math.max(0, current.getQtyReq() - prev.getOnHand());
            Double orderLot = Math.ceil(Required.doubleValue() / getLotSize(current.getMaterial()));
            Double orderamt = (orderLot * getLotSize(current.getMaterial()));

            current.setOrderLot(orderLot.intValue());
            current.setOrderAMT(orderamt.intValue());
            current.setOnHand(prev.getOnHand() + current.getOrderAMT() - current.getQtyReq());
            if (current.getPurchaseOrderDetail() != null) {
                current.getPurchaseOrderDetail().setQuantity(orderamt.intValue());
            }

            if (em.contains(current)) {
                System.out.println(current);
                em.merge(current);
            }

            System.out.println("CascadeWMRP(): Current=" + wMRP.getMaterial().getName() + " " + current.getWeek() + "/" + current.getMonth().value + " RECEIPT=" + current.getOrderAMT() + "USED=" + current.getQtyReq() + " ONHAND=" + current.getOnHand());

        }

        calculatePO(last);
        System.out.println("CascadeWMRP(): Done");
    }

    @Override
    public void unOrderMaterials(int weekNo, int monthNo, int YearNo) throws Exception {

        Query qq = em.createNamedQuery("weeklyMRPRecord.findwMRPatMF");
        qq.setParameter("mf", this.MF);
        qq.setParameter("m", Helper.translateMonth(monthNo));
        qq.setParameter("y", YearNo);
        qq.setParameter("w", weekNo);

        for (WeeklyMRPRecord wMRP : (List< WeeklyMRPRecord>) qq.getResultList()) {
            wMRP.setQtyReq(0);
            cascadeWMRP(wMRP);
            System.out.println("unOrderMaterials() Success for " + wMRP.getMaterial().getName());

        }
    }

    @Override
    public int getLeadTime(Material m) {
        List<ProcuredStockContractDetail> pcs = MF.getSuppliedBy();
        for (ProcuredStockContractDetail pc : pcs) {
            if (pc.getProcuredStock().equals(m)) {
                return pc.getLeadTimeInDays();
            }
        }

        return 1;
    }

    @Override //mass commit
    public void commitallWPP(int weekNo, int monthNo, int yearNo) throws Exception {
        Query q = em.createNamedQuery("WeeklyProductionPlan.getForMFatWK");
        q.setParameter("MF", this.MF);
        q.setParameter("wk", weekNo);
        q.setParameter("y", yearNo);
        q.setParameter("m", monthNo);

        for (WeeklyProductionPlan wpp : (List<WeeklyProductionPlan>) q.getResultList()) {

            if (wpp.getProductionOrder() == null) {
                commitWPP(wpp.getId().intValue());
            }
        }
    }

    @Override //mass commit
    public void uncommitallWPP(int weekNo, int monthNo, int yearNo) throws Exception {
        Query q = em.createNamedQuery("WeeklyProductionPlan.getForMFatWK");
        q.setParameter("MF", this.MF);
        q.setParameter("wk", weekNo);
        q.setParameter("y", yearNo);
        q.setParameter("m", monthNo);

        for (WeeklyProductionPlan wpp : (List<WeeklyProductionPlan>) q.getResultList()) {
            uncommitWPP(wpp.getId().intValue());
        }
    }

    @Override
    public void createPOForWeekMRP(int weekNo, int monthNo, int yearNo) throws Exception {

        Query l = em.createNamedQuery("weeklyMRPRecord.findwMRPatMFMnospecmat");
        l.setParameter("mf", this.MF);
        l.setParameter("w", weekNo);
        l.setParameter("m", monthNo);
        l.setParameter("y", yearNo);

        ProcuredStockPurchaseOrder po = null;
        String lastdate = "";

        for (WeeklyMRPRecord wmrp : (List<WeeklyMRPRecord>) l.getResultList()) {
            if (wmrp.getPurchaseOrderDetail() == null) {
                ProcuredStockPurchaseOrderDetail pod = new ProcuredStockPurchaseOrderDetail();
                pod.setProcuredStock(wmrp.getMaterial());
                pod.setQuantity(wmrp.getOrderAMT());
                em.persist(pod);
                wmrp.setPurchaseOrderDetail(pod);
                System.out.println("createPOForWeekMRP(): Created POD for" + wmrp);
            }
        }
    }

    @Override
    public void uncreatePOForWeekMRP(int weekNo, int monthNo, int yearNo) throws Exception {

        Query l = em.createNamedQuery("weeklyMRPRecord.findwMRPatMFMnospecmat");
        l.setParameter("mf", this.MF);
        l.setParameter("w", weekNo);
        l.setParameter("m", monthNo);
        l.setParameter("y", yearNo);

        ProcuredStockPurchaseOrder po = null;
        String lastdate = "";

        for (WeeklyMRPRecord wmrp : (List<WeeklyMRPRecord>) l.getResultList()) {
            if (wmrp.getPurchaseOrderDetail() != null) {

                if (wmrp.getPurchaseOrderDetail().getPurchaseOrder()
                        != null) {
                    throw new Exception("This Week MRP has been ordered and cant be uncreated !");
                }
                em.remove(wmrp.getPurchaseOrderDetail());
                wmrp.setPurchaseOrderDetail(null);
                System.out.println("uncreatePOForWeekMRP(): Created POD for" + wmrp);
            }
        }
    }

}
