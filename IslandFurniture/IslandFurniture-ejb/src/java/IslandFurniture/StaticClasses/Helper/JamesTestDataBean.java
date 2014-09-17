/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.StaticClasses.Helper;

import IslandFurniture.EJB.Entities.Country;
import IslandFurniture.EJB.Entities.FurnitureModel;
import IslandFurniture.EJB.Entities.ManufacturingFacility;
import IslandFurniture.EJB.Entities.Month;
import IslandFurniture.EJB.Entities.MonthlyStockSupplyReq;
import IslandFurniture.EJB.Entities.ProductionCapacity;
import IslandFurniture.EJB.Entities.Store;
import IslandFurniture.EJB.Manufacturing.ManageProductionPlanning;
import IslandFurniture.EJB.Exceptions.ProductionPlanNoCN;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author James
 */
@Stateless
public class JamesTestDataBean implements JamesTestDataBeanRemote {

    @PersistenceContext(unitName = "IslandFurniture")
    private EntityManager em;

    @EJB
    private ManageProductionPlanning mpp;

    @Override
    public void persist(Object object) {
        em.persist(object);
    }

    @Override
    public void deletedata() {
//        EntityTransaction trx = em.getTransaction();
//        trx.begin();
        em.createQuery("DELETE FROM Store").executeUpdate();
        em.createQuery("DELETE FROM Country").executeUpdate();
        em.createQuery("DELETE FROM ManufacturingFacility").executeUpdate();
        em.createQuery("DELETE FROM ManufacturingCapacity").executeUpdate();
        em.createQuery("DELETE FROM MonthlyStockSupplyReq").executeUpdate();
        em.createQuery("DELETE FROM FurnitureModel").executeUpdate();

        em.createQuery("DELETE FROM MonthlyProductionPlan").executeUpdate();
        em.createQuery("DELETE FROM ProductionCapacity").executeUpdate();
        em.flush();

    }

    @Override
    public void planall() {
        try {
            mpp.setCN("SINGAPORE");
            mpp.CreateProductionPlanFromForecast();
        } catch (Exception ex) {
            Logger.getLogger(JamesTestDataBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void createtestdata() {
        Country cn = null;
        ManufacturingFacility mf = null;
        FurnitureModel fm = null;
        ProductionCapacity pc = null;
        Store store = null;
        MonthlyStockSupplyReq MSSR = null;
        MonthlyStockSupplyReq MSSR2 = null;

        try {
            cn = new Country();
            cn.setName("SINGAPORE");
            cn.setPhoneCode("65");
            cn.setCode("SG");

            persist(cn);
        } catch (Exception err) {
        }

        try {
            mf = new ManufacturingFacility();
            mf.setCountry(cn);
            mf.setTimeZoneID("GMT 8");
            persist(mf);
        } catch (Exception err) {
        }
        try {
            fm = new FurnitureModel();
            fm.setName("FURNITURE");
            persist(fm);
        } catch (Exception err) {
        }
        try {
            pc = new ProductionCapacity();
            pc.setFurnitureModel(fm);
            pc.setQty(100);
            pc.setManufacturingFacility(mf);
            persist(pc);
        } catch (Exception err) {
        }

        try {
            store = new Store();
            store.setCountry(cn);
            store.setName("Sembawang");
            store.setTimeZoneID("GMT 8");
            persist(store);
        } catch (Exception err) {
        }

        try {
            MSSR = new MonthlyStockSupplyReq();
            MSSR.setQtyRequested(101); //why is it qty forecasted - sales ?
            MSSR.setStock(fm);
            MSSR.setMonth(Month.DEC);
            MSSR.setYear(2014);
            MSSR.setStore(store);
            persist(MSSR);

            MSSR2 = new MonthlyStockSupplyReq();
            MSSR2.setQtyRequested(199);
            MSSR2.setStock(fm);
            MSSR2.setMonth(Month.NOV);
            MSSR2.setYear(2014);
            MSSR2.setStore(store);
            persist(MSSR2);
        } catch (Exception err) {
        }

        List<MonthlyStockSupplyReq> mssrs = new ArrayList<MonthlyStockSupplyReq>();

        mssrs.add(MSSR);
        mssrs.add(MSSR2);
        em.flush();

        mpp.setCN(cn.getName());

        try {
            mpp.CreateProductionPlanFromForecast(mssrs);
        } catch (ProductionPlanNoCN ex) {
            Logger.getLogger(JamesTestDataBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(JamesTestDataBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("SUCCESS");

    }

}
