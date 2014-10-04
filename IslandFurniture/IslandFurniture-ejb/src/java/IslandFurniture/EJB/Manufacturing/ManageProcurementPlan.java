/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.Manufacturing;

import IslandFurniture.EJB.Entities.CountryOffice;
import IslandFurniture.EJB.Entities.ManufacturingFacility;
import IslandFurniture.EJB.Enums.Month;
import IslandFurniture.EJB.Entities.MonthlyProcurementPlan;
import IslandFurniture.EJB.Entities.MonthlyProcurementPlanPK;
import IslandFurniture.EJB.Entities.MonthlyStockSupplyReq;
import IslandFurniture.EJB.Entities.ProcuredStock;
import IslandFurniture.EJB.Entities.ProcurementContractDetail;
import IslandFurniture.EJB.Entities.PurchaseOrder;
import IslandFurniture.EJB.Entities.PurchaseOrderDetail;
import IslandFurniture.EJB.Enums.PurchaseOrderStatus;
import IslandFurniture.EJB.Entities.RetailItem;
import IslandFurniture.EJB.Entities.Stock;
import IslandFurniture.EJB.Entities.StockSupplied;
import IslandFurniture.EJB.Entities.Supplier;
import IslandFurniture.EJB.ITManagement.ManageOrganizationalHierarchyBeanLocal;
import IslandFurniture.StaticClasses.Helper.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Benjamin
 */
@Stateful
public class ManageProcurementPlan implements ManageProcurementPlanLocal {

    private List<MonthlyStockSupplyReq> mssrList;
    private MonthlyStockSupplyReq mssr;
    private CountryOffice co;
    private ManufacturingFacility mf;
    private MonthlyProcurementPlan mpp;
    private Month month;
    private ProcuredStock procuredStock;
    private RetailItem retailItem;
    private List<MonthlyProcurementPlan> mppList;
    private List<CountryOffice> coList;
    private List<StockSupplied> ssList;
    private List<Stock> stockList;
    private StockSupplied ss;
    private Stock stock;
    private Integer year;
    private int monthInt;
    private List<MonthlyStockSupplyReq> tempMssrList;
    
    @PersistenceContext(unitName = "IslandFurniture")
    private EntityManager em;
    
    @EJB
    private ManageOrganizationalHierarchyBeanLocal mohb;
    private PurchaseOrder purchaseOrder;
    private PurchaseOrderDetail purchaseOrderDetail;
    
    @Override
    public void createMonthlyProcumentPlan(ManufacturingFacility mf){
        Query query = em.createQuery("SELECT s FROM StockSupplied s WHERE s.manufacturingFacility=:mf AND s.stock.id IN (SELECT s.id FROM ProcuredStock s)");
        query.setParameter("mf", mf);
        ssList = query.getResultList();
        Iterator<StockSupplied> iterator = ssList.iterator();
        coList = new ArrayList();
        stockList = new ArrayList();
        while(iterator.hasNext()){
            ss = iterator.next();
            coList.add(ss.getCountryOffice());
            stockList.add(ss.getStock());
        }
        mssrList = new ArrayList();
        tempMssrList = new ArrayList();
        for (int i=0;i<coList.size();i++){
            query = em.createQuery("SELECT m FROM MonthlyStockSupplyReq m WHERE m.stock=:stock AND m.countryOffice=:co AND m.approved = 1");
            query.setParameter("stock", stockList.get(i));
            query.setParameter("co", coList.get(i));
            tempMssrList = query.getResultList();
            Iterator<MonthlyStockSupplyReq> iterator2 = tempMssrList.iterator();
            while(iterator2.hasNext()){
                mssr = iterator2.next();
                mssrList.add(mssr);
            }
        }
        Iterator<MonthlyStockSupplyReq> iterator2 = mssrList.iterator();
        //regenerate all unlocked MPPs
        while(iterator2.hasNext()){
            mssr = iterator2.next();
            month= mssr.getMonth();
            Integer year = mssr.getYear();
            try{
                mpp = em.find(MonthlyProcurementPlan.class, new MonthlyProcurementPlanPK(mf.getId(),mssr.getStock().getId(),month,year));
                if (mpp.isLocked().equals(Boolean.FALSE)){
                    mpp.setQty(0);
                    em.merge(mpp);
                }
            }catch(Exception e){
                
            }
        }
        iterator2 = mssrList.iterator();
        while(iterator2.hasNext()){
            mssr = iterator2.next();
            month= mssr.getMonth();
            Integer year = mssr.getYear();
            mpp = em.find(MonthlyProcurementPlan.class, new MonthlyProcurementPlanPK(mf.getId(),mssr.getStock().getId(),month,year));
            if (mpp == null){
                mpp = new MonthlyProcurementPlan();
                mpp.setMonth(mssr.getMonth());
                mpp.setYear(mssr.getYear());
                procuredStock = (ProcuredStock) mssr.getStock();
                retailItem = (RetailItem) procuredStock;
                mpp.setRetailItem(retailItem);
                mpp.setManufacturingFacility(mf);
                mpp.setLocked(Boolean.FALSE);
                mpp.setQty(mssr.getQtyRequested());
                em.persist(mpp);
            }
            //MPP already exists, created by another country which uses the same MF and orders the same stock
            else if(mpp.isLocked() == false){
                mpp.setQty(mpp.getQty()+mssr.getQtyRequested());
                em.merge(mpp);
            }
        }
    }
    
    @Override
    public void lockMpp(ManufacturingFacility mf, Month month, Integer year){
        Query query = em.createQuery("SELECT m FROM MonthlyProcurementPlan m WHERE m.manufacturingFacility=:mf AND m.year=:year AND m.month=:month");
        query.setParameter("mf", mf);
        query.setParameter("year", year);
        query.setParameter("month", month);
        mppList = query.getResultList();
        Iterator<MonthlyProcurementPlan> iterator = mppList.iterator();
        while(iterator.hasNext()){
            MonthlyProcurementPlan mpp = iterator.next();
            mpp.setLocked(Boolean.TRUE);
            em.merge(mpp);
        }
    }
    
    @Override
    public boolean checkMppLocked(ManufacturingFacility mf, Month month, Integer year){
        Query query = em.createQuery("SELECT m FROM MonthlyProcurementPlan m WHERE m.manufacturingFacility=:mf AND m.year=:year AND m.month=:month");
        query.setParameter("mf", mf);
        query.setParameter("year", year);
        query.setParameter("month", month);
        try{
            mppList = query.getResultList();
            Iterator<MonthlyProcurementPlan> iterator = mppList.iterator();
            MonthlyProcurementPlan mpp = iterator.next();
            if (mpp.isLocked()){
                return true;
            }
        }catch(Exception e){
            return false;
        }
        return false;
    }
    
    @Override
    public void createPurchaseOrder(ManufacturingFacility mf, Month month, Integer year){
        Query query = em.createQuery("SELECT m FROM MonthlyProcurementPlan m WHERE m.manufacturingFacility=:mf AND m.month=:month AND m.year=:year");
        query.setParameter("mf", mf);
        query.setParameter("month", month);
        query.setParameter("year", year);
        mppList = query.getResultList();
        List<Supplier> uniqueSupplier = new ArrayList(); 
        List<ProcurementContractDetail> pcdList = new ArrayList();
        Iterator<MonthlyProcurementPlan> iterator = mppList.iterator();
        while(iterator.hasNext()){
            mpp = iterator.next();
            query = em.createQuery("SELECT s FROM ProcurementContractDetail s WHERE s.supplierFor=:mf AND s.procuredStock=:stock");
            query.setParameter("mf", mpp.getManufacturingFacility());
            ProcuredStock ps = mpp.getRetailItem();
            Stock s = (Stock) ps;
            query.setParameter("stock", s);
            ProcurementContractDetail pcd = (ProcurementContractDetail) query.getSingleResult();
            pcdList.add(pcd);
            if (!uniqueSupplier.contains(pcd.getProcurementContract().getSupplier())){
                uniqueSupplier.add(pcd.getProcurementContract().getSupplier());
            }
        }
        Iterator<Supplier> iterator2 = uniqueSupplier.iterator();
        while(iterator2.hasNext()){
            Supplier s = iterator2.next();
            Calendar cal = new GregorianCalendar(year,month.value, 1);
            cal.setTimeZone(TimeZone.getTimeZone("UTC"));
            //day = date of first monday in month
            int day = cal.get(Calendar.DAY_OF_WEEK);
            while (day != 3) {
               cal.add(Calendar.DATE, 1);
                day = cal.get(Calendar.DAY_OF_WEEK);
            }
            int maxWeekNumber = Helper.getNumOfWeeks( month.value,year);
            int maxDay = Helper.getNumWorkDays(month, year);
            int daysInLastWeek = Helper.getBoundaryWeekDays(month, year);
            for (int j=0;j< maxWeekNumber-1;j+=1){ //Add purchase order for week 1 to max week-1
                purchaseOrder = new PurchaseOrder();
                purchaseOrder.setManufacturingFacility(mf);
                purchaseOrder.setShipsTo(mf);
                purchaseOrder.setOrderDate(TimeMethods.convertToPlantTime(mf, cal));
                purchaseOrder.setStatus(PurchaseOrderStatus.PLANNED);
                purchaseOrder.setSupplier(s);
                em.persist(purchaseOrder);
                Iterator<ProcurementContractDetail> iterator3 = pcdList.iterator();
                while(iterator3.hasNext()){
                    ProcurementContractDetail pcd = iterator3.next();
                    if(pcd.getProcurementContract().getSupplier() == s){
                        ProcuredStock ps = pcd.getProcuredStock();
                        RetailItem ri = (RetailItem) ps;
                        mpp = em.find(MonthlyProcurementPlan.class, new MonthlyProcurementPlanPK(mf.getId(),ri.getId(),month,year));
                        mpp.getPurchaseOrderList().add(purchaseOrder);
                        purchaseOrder.setMonthlyProcurementPlan(mpp);
                        em.merge(mpp);
                        int qty = mpp.getQty()/maxDay*7;
                        purchaseOrderDetail = new PurchaseOrderDetail();
                        purchaseOrderDetail.setQuantity(qty);
                        purchaseOrderDetail.setPurchaseOrder(purchaseOrder);
                        purchaseOrderDetail.setProcuredStock(ps);
                        em.persist(purchaseOrderDetail);
                        purchaseOrder.getPurchaseOrderDetails().add(purchaseOrderDetail);
                        em.merge(purchaseOrder);
                    }
                }
                em.flush();
                cal.add(Calendar.DATE, 7);
            }
            //last week
            purchaseOrder = new PurchaseOrder();
            purchaseOrder.setManufacturingFacility(mf);
            purchaseOrder.setShipsTo(mf);
            purchaseOrder.setOrderDate(TimeMethods.convertToPlantTime(mf, cal));
            purchaseOrder.setStatus(PurchaseOrderStatus.PLANNED);
            purchaseOrder.setSupplier(s);
            em.persist(purchaseOrder);
            Iterator<ProcurementContractDetail> iterator3 = pcdList.iterator();
            while(iterator3.hasNext()){
                ProcurementContractDetail pcd = iterator3.next();
                if(pcd.getProcurementContract().getSupplier() == s){
                    ProcuredStock ps = pcd.getProcuredStock();
                    RetailItem ri = (RetailItem) ps;
                    mpp = em.find(MonthlyProcurementPlan.class, new MonthlyProcurementPlanPK(mf.getId(),ri.getId(),month,year));
                    Month newmonth = null;
                    try{
                        int i_month = Helper.addMonth(Helper.translateMonth(mpp.getMonth().value), mpp.getYear(), 1, true);
                        newmonth = Helper.translateMonth(i_month);
                    }catch(Exception e){
                        System.err.print(e);
                    }
                    MonthlyProcurementPlan mpp2 = em.find(MonthlyProcurementPlan.class, new MonthlyProcurementPlanPK(mf.getId(),ri.getId(),newmonth,year));
                    int maxDay2 = Helper.getNumWorkDays(mpp2.getMonth(), mpp2.getYear());
                    mpp.getPurchaseOrderList().add(purchaseOrder);
                    purchaseOrder.setMonthlyProcurementPlan(mpp);
                    em.merge(mpp);
                    int qty = mpp.getQty()/maxDay*daysInLastWeek;
                    int qty2 = mpp2.getQty()/maxDay2*(7-daysInLastWeek);
                    purchaseOrderDetail = new PurchaseOrderDetail();
                    purchaseOrderDetail.setQuantity(qty+qty2);
                    purchaseOrderDetail.setPurchaseOrder(purchaseOrder);
                    purchaseOrderDetail.setProcuredStock(ps);
                    em.persist(purchaseOrderDetail);
                    purchaseOrder.getPurchaseOrderDetails().add(purchaseOrderDetail);
                    em.merge(purchaseOrder);
                }
            }
            em.flush();
        }
    }
    
    
    @Override
    public List<MonthlyProcurementPlan> viewMonthlyProcurementPlan(){
        Query query = em.createQuery("FROM MonthlyProcurementPlan m");
        return query.getResultList();
    }
    
    @Override
    public List<PurchaseOrder> viewPurchaseOrder(){
        Query query = em.createQuery("SELECT p FROM PuchaseOrder p");
        return query.getResultList();
    }
    
    @Override
    public List<PurchaseOrderDetail> viewPurchaseOrderDetail(){
        Query query = em.createQuery("SELECT p FROM PuchaseOrderDetail p");
        return query.getResultList();
    }
    
    @Override
    public List<RetailItem> viewRetailItems(){
        Query query = em.createQuery("SELECT r FROM RetailItem r");
        return query.getResultList();
    }
    
}
