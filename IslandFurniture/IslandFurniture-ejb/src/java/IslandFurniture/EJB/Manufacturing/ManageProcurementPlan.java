/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.Manufacturing;

import IslandFurniture.EJB.Entities.ManufacturingFacility;
import IslandFurniture.EJB.Entities.MonthlyProcurementPlan;
import IslandFurniture.EJB.Entities.MonthlyStockSupplyReq;
import IslandFurniture.EJB.Entities.ProcuredStock;
import IslandFurniture.EJB.Entities.RetailItem;
import IslandFurniture.EJB.Entities.StockSupplied;
import java.util.Iterator;
import java.util.List;
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
    private MonthlyProcurementPlan mpp;
    private MonthlyStockSupplyReq mssr;
    private ProcuredStock procuredStock;
    private RetailItem retailItem;
    private ManufacturingFacility mf;
    private StockSupplied ss;
    
    @PersistenceContext(unitName = "IslandFurniture")
    private EntityManager em;
    
    @Override
    public void createMonthlyProcumentPlan(){
        Query query = em.createQuery("SELECT m FROM MonthlyStockSupplyReq m WHERE m.stock.id IN (SELECT p.id FROM ProcuredStock p)");
        mssrList = query.getResultList();
        Iterator<MonthlyStockSupplyReq> iterator = mssrList.iterator();
        while (iterator.hasNext()) {
            mpp = new MonthlyProcurementPlan();
            mssr = iterator.next();
            mpp.setMonth(mssr.getMonth());
            mpp.setYear(mssr.getYear());
            mpp.setQty(mssr.getQtyRequested());
            mpp.setLocked(false);
            procuredStock = (ProcuredStock) mssr.getStock();
            retailItem = (RetailItem) procuredStock;
            mpp.setRetailItem(retailItem);
            query = em.createQuery("Select ss.manufacturingFacility FROM StockSupplied ss WHERE ss.countryOffice.id=:co AND ss.stock.id=:stock");
            query.setParameter("co", mssr.getCountryOffice().getId());
            query.setParameter("stock", mssr.getStock().getId());
            mf = (ManufacturingFacility) query.getSingleResult(); 
            mpp.setManufacturingFacility(mf);
            em.persist(mpp);
	}
    }
    
    @Override
    public List<MonthlyProcurementPlan> viewMonthlyProcurementPlan(){
        Query query = em.createQuery("FROM MonthlyProcurementPlan m");
        return query.getResultList();
    }
    
}
