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
import IslandFurniture.EJB.Entities.RetailItem;
import IslandFurniture.EJB.ITManagement.ManageOrganizationalHierarchyBeanLocal;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
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
    
    @PersistenceContext(unitName = "IslandFurniture")
    private EntityManager em;
    
    @EJB
    private ManageOrganizationalHierarchyBeanLocal mohb;
    
    @Override
    public void createMonthlyProcumentPlan(){
        Query query = em.createQuery("SELECT m FROM MonthlyStockSupplyReq m WHERE m.stock.id IN (SELECT p.id FROM ProcuredStock p)");
        mssrList = query.getResultList();
        Iterator<MonthlyStockSupplyReq> iterator = mssrList.iterator();
        while(iterator.hasNext()){
            mssr = iterator.next();
            query = em.createQuery("Select ss.manufacturingFacility FROM StockSupplied ss WHERE ss.countryOffice.id=:co AND ss.stock.id=:stock");
            query.setParameter("co", mssr.getCountryOffice().getId());
            query.setParameter("stock", mssr.getStock().getId());
            mf = (ManufacturingFacility) query.getSingleResult();
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
    public List<MonthlyProcurementPlan> viewMonthlyProcurementPlan(){
        Query query = em.createQuery("FROM MonthlyProcurementPlan m");
        return query.getResultList();
    }
    
}
