/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.Manufacturing;

import IslandFurniture.EJB.Entities.CountryOffice;
import IslandFurniture.EJB.Entities.ManufacturingFacility;
import IslandFurniture.EJB.Entities.Month;
import IslandFurniture.EJB.Entities.MonthlyProcurementPlan;
import IslandFurniture.EJB.Entities.MonthlyProcurementPlanPK;
import IslandFurniture.EJB.Entities.MonthlyStockSupplyReq;
import IslandFurniture.EJB.Entities.ProcuredStock;
import IslandFurniture.EJB.Entities.PurchaseOrder;
import IslandFurniture.EJB.Entities.PurchaseOrderDetail;
import IslandFurniture.EJB.Entities.PurchaseOrderStatus;
import IslandFurniture.EJB.Entities.RetailItem;
import IslandFurniture.EJB.Entities.StockSupplied;
import IslandFurniture.EJB.ITManagement.ManageOrganizationalHierarchyBeanLocal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
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
    private Integer year;
    private int monthInt;
    
    @PersistenceContext(unitName = "IslandFurniture")
    private EntityManager em;
    
    @EJB
    private ManageOrganizationalHierarchyBeanLocal mohb;
    private PurchaseOrder purchaseOrder;
    private PurchaseOrderDetail purchaseOrderDetail;
    
    @Override
    public void createMonthlyProcumentPlan(ManufacturingFacility mf){
        Query query = em.createQuery("SELECT m FROM MonthlyStockSupplyReq m WHERE m.stock.id IN (SELECT p.id FROM ProcuredStock p) AND m.approved = 1");
        mssrList = query.getResultList();
        Iterator<MonthlyStockSupplyReq> iterator2 = mssrList.iterator();
        //regenerate all unlocked MPPs
        while(iterator2.hasNext()){
            mssr = iterator2.next();
            month= mssr.getMonth();
            Integer year = mssr.getYear();
            mpp = em.find(MonthlyProcurementPlan.class, new MonthlyProcurementPlanPK(mf.getId(),mssr.getStock().getId(),month,year));
            if (mpp.isLocked().equals(Boolean.FALSE)){
                mpp.setQty(0);
                em.persist(mpp);
            }
        }
        Iterator<MonthlyStockSupplyReq> iterator = mssrList.iterator();
        while(iterator.hasNext()){
            mssr = iterator.next();
            //query = em.createQuery("Select ss.manufacturingFacility FROM StockSupplied ss WHERE ss.countryOffice.id=:co AND ss.stock.id=:stock");
            //query.setParameter("co", mssr.getCountryOffice().getId());
            //query.setParameter("stock", mssr.getStock().getId());
            //mf = (ManufacturingFacility) query.getSingleResult();
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
            else{
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
        Query query = em.createQuery("SELECT s FROM StockSupplied s WHERE s.manufacturingFacility=:mf");
        query.setParameter("mf", mf);
        List<StockSupplied> ssList = query.getResultList();
        coList = new ArrayList<CountryOffice>();
        Iterator<StockSupplied> iterator3 = ssList.iterator();
        while(iterator3.hasNext()){
            StockSupplied ss = iterator3.next();
            coList.add(ss.getCountryOffice());
        }
        Iterator<CountryOffice> iterator = coList.iterator();
        while(iterator.hasNext()){
            co = iterator.next();
            Calendar cal = new GregorianCalendar(year, monthInt, 1);
            int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            //day = date of first monday in month
            int day = cal.get(Calendar.DAY_OF_WEEK);
            while (day != 1) {
                cal.add(Calendar.DATE, 1);
                day = cal.get(Calendar.DAY_OF_WEEK);
            }
            for (int j=0;j<22;j+=7){ //Add purchase order for week 1 to week 3
                purchaseOrder = new PurchaseOrder();
                purchaseOrder.setShipsTo(co);
                cal.add(Calendar.DATE, j);
                purchaseOrder.setOrderDate(cal);
                purchaseOrder.setStatus(PurchaseOrderStatus.PLANNED);
                //Adding purchase order detail
                query = em.createQuery("SELECT m FROM MonthlyStockSupplyReq m WHERE m.stock.id IN (SELECT p.id FROM ProcuredStock p) AND m.year=:year AND m.month=:month AND m.countryOffice=:co");
                query.setParameter("year", year);
                query.setParameter("month", month);
                query.setParameter("co", co);
                mssrList = query.getResultList();
                Iterator<MonthlyStockSupplyReq> iterator2 = mssrList.iterator();
                while(iterator2.hasNext()){
                    mssr = iterator2.next();
                    purchaseOrderDetail = new PurchaseOrderDetail();
                    procuredStock = (ProcuredStock) mssr.getStock();
                    retailItem = (RetailItem) procuredStock;
                    purchaseOrderDetail.setProcuredStock(procuredStock);
                    int qty = mssr.getQtyRequested()/daysInMonth*7;
                    purchaseOrderDetail.setQuantity(qty);
                    purchaseOrderDetail.setPurchaseOrder(purchaseOrder);
                    em.persist(purchaseOrderDetail);
                }
                em.persist(purchaseOrder);
            }   
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
