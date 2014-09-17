/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.StaticClasses.Helper;

import IslandFurniture.EJB.Entities.FurnitureModel;
import IslandFurniture.EJB.Entities.Month;
import IslandFurniture.EJB.Entities.MonthlyStockSupplyReq;
import IslandFurniture.EJB.Entities.ProductionCapacity;
import IslandFurniture.EJB.Entities.Store;
import IslandFurniture.EJB.Manufacturing.ManageProductionPlanningRemote;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
    public Boolean loadMSSRS() {
            try{
        FurnitureModel fm = Helper.getFirstObjectFromQuery("SELECT FM from FurnitureModel FM where FM.name='Round Table'",em);
        FurnitureModel fm2 = Helper.getFirstObjectFromQuery("SELECT FM from FurnitureModel FM where FM.name='Swivel Chair'",em);
        Store store = Helper.getFirstObjectFromQuery("SELECT s from Store s where s.name='Alexandra'",em);

        //Add Daily Production Capacity
        try {

            mpp.createOrUpdateCapacity("Round Table", "Tuas", 4);
            mpp.createOrUpdateCapacity("Swivel Chair", "Tuas", 7);
        } catch (Exception ex) {
        }

        try {
            MonthlyStockSupplyReq MSSR = new MonthlyStockSupplyReq();
            MSSR.setQtyRequested(101);
            MSSR.setStock(fm);
            MSSR.setMonth(Month.DEC);
            MSSR.setYear(2014);
            MSSR.setStore(store);
            persist(MSSR);
        } catch (Exception err) {
  
        }

        try {
            MonthlyStockSupplyReq MSSR2 = new MonthlyStockSupplyReq();
            MSSR2.setQtyRequested(199);
            MSSR2.setStock(fm);
            MSSR2.setMonth(Month.NOV);
            MSSR2.setYear(2014);
            MSSR2.setStore(store);
            persist(MSSR2);
        } catch (Exception err) {

        }

        try {
            MonthlyStockSupplyReq MSSR3 = new MonthlyStockSupplyReq();
            MSSR3.setQtyRequested(101);
            MSSR3.setStock(fm2);
            MSSR3.setMonth(Month.DEC);
            MSSR3.setYear(2014);
            MSSR3.setStore(store);
            persist(MSSR3);
        } catch (Exception err) {
        }

        return (true);
            }catch(Exception ex){
            return (false);}
    }

}
