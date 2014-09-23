/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.StaticClasses.Helper;

import IslandFurniture.EJB.Entities.FurnitureModel;
import IslandFurniture.EJB.Entities.ManufacturingFacility;
import IslandFurniture.EJB.Entities.MonthlyStockSupplyReq;
import IslandFurniture.EJB.Entities.StockSupplied;
import IslandFurniture.EJB.Manufacturing.ManageProductionPlanningRemote;
import java.util.List;
import java.util.Random;
import javax.ejb.EJB;
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
        try {
            Query q = em.createQuery("select MF from ManufacturingFacility MF");
            for (ManufacturingFacility MF : (List<ManufacturingFacility>) q.getResultList()) {
                Query L = em.createQuery("SELECT SS from StockSupplied SS where SS.manufacturingFacility=:mf");
                L.setParameter("mf", MF);

                for (StockSupplied SS : (List<StockSupplied>) L.getResultList()) {

                    if (!(SS.getStock() instanceof FurnitureModel)) {
                        continue;
                    }

                    Query M = em.createQuery("SELECT MAX(MSSR.qtyRequested) from MonthlyStockSupplyReq MSSR where MSSR.stock=:stk and MSSR.countryOffice=:co");
                    M.setParameter("co", SS.getCountryOffice());
                    M.setParameter("stk", SS.getStock());
                    int max = (int) M.getSingleResult();
                    Random r = new Random();
                    int Capacity =  (int)(max*5) + r.nextInt((int)(max*3));
                    Capacity=Capacity/30;
                    mpp.createOrUpdateCapacity(SS.getStock().getName(), MF.getName(), Capacity);

                //Create a few fake MSSR
                    for (int i = 2; i <= 12; i++) {
                        if (Math.random() > 0.8) {
                            continue;
                        }

                        int i_month = Helper.addMonth(Helper.getCurrentMonth(), Helper.getCurrentYear(), i, true);
                        int i_year = Helper.addMonth(Helper.getCurrentMonth(), Helper.getCurrentYear(), i, false);

                        MonthlyStockSupplyReq MSSR=new MonthlyStockSupplyReq();
                        MSSR.setMonth(Helper.translateMonth(i_month));
                        MSSR.setYear(i_year);
                        MSSR.setStock(SS.getStock());
                        MSSR.setCountryOffice(SS.getCountryOffice());
                        MSSR.setQtyRequested(r.nextInt(max)+max/4); //Max of 1.25
                        MSSR.setApproved(true);
                        try{
                        em.persist(MSSR);
                        
                        }catch(Exception ex){}
                        
                    }

                    System.out.println("loadProductionCapacityData(): MF=" + MF.getName() + " PRODUCT=" + SS.getStock().getName() + " MaxCapacity=" + Capacity);

                }

            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

}
