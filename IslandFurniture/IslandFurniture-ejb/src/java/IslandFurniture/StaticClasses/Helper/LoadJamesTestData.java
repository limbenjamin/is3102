/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.StaticClasses.Helper;

import IslandFurniture.EJB.Entities.FurnitureModel;
import IslandFurniture.EJB.Entities.ManufacturingFacility;
import IslandFurniture.EJB.Entities.Month;
import IslandFurniture.EJB.Entities.MonthlyStockSupplyReq;
import IslandFurniture.EJB.Entities.ProductionCapacity;
import IslandFurniture.EJB.Entities.StockSupplied;
import IslandFurniture.EJB.Entities.Store;
import IslandFurniture.EJB.Manufacturing.ManageProductionPlanningRemote;
import java.util.List;
import java.util.Random;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author James
 */
@Stateless
public class LoadJamesTestData implements LoadJamesTestDataRemote {

    @PersistenceContext(unitName = "IslandFurniture")
    private EntityManager em;

    @EJB
    private ManageProductionPlanningRemote mpp;

    public void persist(Object object) {
        em.persist(object);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NEVER)
    public Boolean loadProductionCapacityData() {
        try{
        Query q = em.createQuery("select MF from ManufacturingFacility MF");
        for (ManufacturingFacility MF : (List<ManufacturingFacility>) q.getResultList()) {
            Query L = em.createQuery("SELECT SS from StockSupplied SS where SS.manufacturingFacility=:mf");
            L.setParameter("mf", MF);

            for (StockSupplied SS : (List<StockSupplied>) L.getResultList()) {
                
                if (!(SS.getStock() instanceof FurnitureModel)) continue;

                Query M = em.createQuery("SELECT MAX(MSSR.qtyRequested) from MonthlyStockSupplyReq MSSR where MSSR.stock=:stk and MSSR.store=:store");
                M.setParameter("store", SS.getStore());
                M.setParameter("stk", SS.getStock());
                int max = (int) M.getSingleResult();
                Random r=new Random();
                int Capacity=(int)max*(10+r.nextInt(5));
                mpp.createOrUpdateCapacity(SS.getStock().getName(), MF.getName(),Capacity);
                System.out.println("loadProductionCapacityData(): MF="+MF.getName()+" PRODUCT="+SS.getStock().getName()+" MaxCapacity="+Capacity);

            }

          
        }
        return true;
        }catch(Exception ex){return false;}
    }
    
    
}
