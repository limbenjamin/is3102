/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.DataLoading;

import IslandFurniture.Entities.FurnitureModel;
import IslandFurniture.Entities.ManufacturingFacility;
import IslandFurniture.Enums.Month;
import IslandFurniture.Entities.MonthlyStockSupplyReq;
import IslandFurniture.Entities.StockSupplied;
import IslandFurniture.EJB.Manufacturing.ManageProductionPlanningLocal;
import IslandFurniture.Enums.MssrStatus;
import IslandFurniture.StaticClasses.Helper;
import java.util.List;
import java.util.Random;
import javax.ejb.EJB;
import javax.ejb.Stateless;
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
    private ManageProductionPlanningLocal mpp;

    public void persist(Object object) {
        em.persist(object);
    }

    @Override
    public Boolean loadProductionCapacityData() {

        Query J = em.createQuery("select mssr from MonthlyStockSupplyReq mssr order by mssr.month+mssr.year*12 desc");
        Month start_m = ((MonthlyStockSupplyReq) J.getResultList().get(0)).getMonth();
        int start_y = ((MonthlyStockSupplyReq) J.getResultList().get(0)).getYear();
        
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
                int Capacity = (int) (max * 5) + r.nextInt((int) (max * 3));
                Capacity = Capacity / 30;
                try {
                    mpp.createOrUpdateCapacityExternal(SS.getStock().getName(), MF.getName(), Capacity);
                } catch (Exception ex) {
                    System.err.println("loadProductionCapacityData()" + ex.getMessage());
                }

                //Create a few fake MSSR
                for (int i = 1; i <= 6; i++) {
                    if (Math.random() > 0.8) {
                        continue;
                    }

                    int i_month = Helper.addMonth(start_m, start_y, i, true);
                    int i_year = Helper.addMonth(start_m, start_y, i, false);

                    MonthlyStockSupplyReq MSSR = new MonthlyStockSupplyReq();
                    try {
                        MSSR.setMonth(Helper.translateMonth(i_month));
                    } catch (Exception ex) {
                    }
                    MSSR.setYear(i_year);
                    MSSR.setStock(SS.getStock());
                    MSSR.setCountryOffice(SS.getCountryOffice());
                    int qtydemanded = r.nextInt(max) + max / 4;
                    MSSR.setQtyRequested(qtydemanded); //Max of 1.25
                    MSSR.setStatus(MssrStatus.APPROVED);

                    try {
                        em.persist(MSSR);
                        em.flush();
                        System.out.println("loadProductionCapacityData():  Simulating MSSR forward looking " + MF.getName() + " PRODUCT=" + SS.getStock().getName() + " MONTH=" + i_month + " YEAR=" + i_year + " QTY DEMANDED=" + qtydemanded);

                    } catch (Exception ex) {
                        System.err.println("loadProductionCapacityData()" + ex.getMessage());
                    }

                }

                System.out.println("loadProductionCapacityData(): MF=" + MF.getName() + " PRODUCT=" + SS.getStock().getName() + " MaxCapacity=" + Capacity);

            }

        }

        return true;

    }

}
