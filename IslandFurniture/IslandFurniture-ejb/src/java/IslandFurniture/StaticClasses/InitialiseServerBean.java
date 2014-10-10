/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.StaticClasses;

import IslandFurniture.DataLoading.LoadJamesTestDataRemote;
import IslandFurniture.DataLoading.LoadOrgEntitiesBeanRemote;
import IslandFurniture.DataLoading.LoadPrivilegeBeanRemote;
import IslandFurniture.DataLoading.LoadSalesForecastBeanRemote;
import IslandFurniture.DataLoading.LoadStaffDataBeanRemote;
import IslandFurniture.DataLoading.LoadStocksBeanRemote;
import IslandFurniture.DataLoading.LoadStorageDataBeanRemote;
import IslandFurniture.DataLoading.LoadSupplierBeanRemote;
import IslandFurniture.DataLoading.LoadTransactionBeanRemote;
import IslandFurniture.DataLoading.MapPrivilegeDataBeanRemote;
import IslandFurniture.DataLoading.MapStaffDataBeanRemote;
import IslandFurniture.Entities.Country;
import IslandFurniture.EJB.ITManagement.ManageOrganizationalHierarchyBean;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * This is a bean that will be automatically run at deployment. Use this to
 * perform any initialisation activities.
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Startup
@Singleton
public class InitialiseServerBean implements InitialiseServerBeanLocal {
//
//    @EJB
//    private static LoadStorageDataBeanRemote loadStorageDataBean;
//
//    @EJB
//    private static LoadSupplierBeanRemote loadSupplierBean;
//
//    @EJB
//    private static LoadStocksBeanRemote loadStocksBean;
//    @EJB
//    private static LoadSalesForecastBeanRemote loadSalesForecastBean;
//
//    @EJB
//    private static LoadTransactionBeanRemote loadTransactionBean;
//
//    @EJB
//    private static LoadOrgEntitiesBeanRemote loadOrgEntitiesBean;
//
//    @EJB
//    private static LoadStaffDataBeanRemote loadStaffDataBean;
//
//    @EJB
//    private static MapStaffDataBeanRemote mapStaffDataBean;
//
//    @EJB
//    private static LoadJamesTestDataRemote loadJamesTestData;
//
//    @EJB
//    private static LoadPrivilegeBeanRemote loadPrivilegeBean;
//
//    @EJB
//    private static MapPrivilegeDataBeanRemote mapPrivilegeDataBean;

    Country country;
    @PersistenceContext(unitName = "IslandFurniture")
    private EntityManager em;
    private boolean updateMssrStarted;

//    public void loaddata() {
//        System.out.println("initialiseServer(): Server Started");
//        Query q = em.createQuery("SELECT c from Country");
//        if (q.getResultList().size() == 0) {
//            System.out.println("initialiseServer(): DataLoading Started");
//            try {
//                loadOrgEntitiesBean.loadSampleData();
//            } catch (Exception ex) {
//            }
//            try {
//                loadStocksBean.loadSampleData(0);
//            } catch (Exception ex) {
//            }
//            try {
//                loadSupplierBean.loadSampleData();
//            } catch (Exception ex) {
//            }
//            try {
//                loadStorageDataBean.loadSampleData();
//            } catch (Exception ex) {
//            }
//            try {
//                loadTransactionBean.loadSampleData();
//            } catch (Exception ex) {
//            }
//            try {
//                loadStaffDataBean.loadSampleData();
//            } catch (Exception ex) {
//            }
//            try {
//                mapStaffDataBean.loadSampleData();
//            } catch (Exception ex) {
//            }
//            try {
//                loadPrivilegeBean.loadSampleData();
//            } catch (Exception ex) {
//            }
//            try {
//                mapPrivilegeDataBean.loadSampleData();
//            } catch (Exception ex) {
//            }
//            try {
//                loadSalesForecastBean.loadSampleData();
//            } catch (Exception ex) {
//            }
//
//        }
//    }

    @PostConstruct
    public void initialiseServer() {
//        loaddata();
        this.updateMssrStarted = false;
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    public boolean isUpdateMssrStarted() {
        return updateMssrStarted;
    }

    public void setUpdateMssrStarted(boolean updateMssrStarted) {
        this.updateMssrStarted = updateMssrStarted;
    }

    // Extra Methods
    @Override
    public void startMssrTimer() {
        this.updateMssrStarted = true;
    }

    public void persist(Object object) {
        em.persist(object);
    }

}
