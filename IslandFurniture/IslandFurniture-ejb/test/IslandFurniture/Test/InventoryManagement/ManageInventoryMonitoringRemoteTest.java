/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.Test.InventoryManagement;

import IslandFurniture.EJB.ITManagement.ManageOrganizationalHierarchyBeanRemote;
import IslandFurniture.EJB.InventoryManagement.ManageInventoryMonitoringRemote;
import IslandFurniture.EJB.InventoryManagement.ManageInventoryTransferRemote;
import IslandFurniture.EJB.Manufacturing.StockManagerRemote;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Stock;
import IslandFurniture.Entities.StockUnit;
import IslandFurniture.Entities.StorageBin;
import IslandFurniture.Entities.Store;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 *
 * @author Benjamin
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class ManageInventoryMonitoringRemoteTest {
    
    ManageInventoryMonitoringRemote manageInventoryMonitoringRemote = lookupManageInventoryMonitoringRemote();
    
    static Store store;
    static Store store2;
    static Stock stock;
    static StockUnit stockUnit;
    static StorageBin storageBin;
    
    public ManageInventoryMonitoringRemoteTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        ManageOrganizationalHierarchyBeanRemote manageOrganizationalHierarchyBeanRemote = lookupManageOrganizationalHierarchyBeanRemote();
        store = (Store) manageOrganizationalHierarchyBeanRemote.findPlantByNameOnly("Alexandra");
        storageBin = store.getStorageAreas().get(0).getStorageBins().get(0);
        ManageInventoryTransferRemote manageInventoryTransferRemote = lookupManageInventoryTransferRemote();
        StockManagerRemote stockManagerRemote = lookupStockManagerRemote();
        stock = stockManagerRemote.getStock(Long.parseLong("432"));
        manageInventoryTransferRemote.createStockUnit(stock, "1", Long.parseLong("10"), storageBin);
        stockUnit =  manageInventoryTransferRemote.viewStockUnitByStorageBin(store, storageBin).get(0);
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void test01EditStockUnitQuantity() {
        try{
            manageInventoryMonitoringRemote.editStockUnitQuantity(stockUnit.getId(), Long.parseLong("50"));
        }catch(Exception e){
            fail("Exception Thrown");
        }
        try{
            manageInventoryMonitoringRemote.editStockUnitQuantity(stockUnit.getId(), Long.parseLong("0"));
        }catch(Exception e){
            fail("Exception Thrown");
        }
        try{
            manageInventoryMonitoringRemote.editStockUnitQuantity(Long.parseLong("1"), Long.parseLong("50"));
            fail("Exception Not Thrown");
        }catch(Exception e){
            
        }
    }

    @Test
    public void test02ViewStockUnit() {
        List<StockUnit> result = manageInventoryMonitoringRemote.viewStockUnit(store);
        assertNotNull(result);
        try{
            result = manageInventoryMonitoringRemote.viewStockUnit(null);
            fail("Exception Not Thrown");
        }catch(Exception e){
            
        }
        try{
            result = manageInventoryMonitoringRemote.viewStockUnit(store2);
            fail("Exception Not Thrown");
        }catch(Exception e){
            
        }
    }

    @Test
    public void test03ViewStockUnitAll() {
        List<StockUnit> result = manageInventoryMonitoringRemote.viewStockUnitAll(store);
        assertNotNull(result);
        try{
            result = manageInventoryMonitoringRemote.viewStockUnitAll(null);
            fail("Exception Not Thrown");
        }catch(Exception e){
            
        }
        try{
            result = manageInventoryMonitoringRemote.viewStockUnitAll(store2);
            fail("Exception Not Thrown");
        }catch(Exception e){
            
        }
    }

    @Test
    public void test04ViewStockUnitbyStock() {
        List<StockUnit> result = manageInventoryMonitoringRemote.viewStockUnitbyStock(stock);
        assertNotNull(result);
        try{
            result = manageInventoryMonitoringRemote.viewStockUnitbyStock(null);
            fail("Exception Not Thrown");
        }catch(Exception e){
            
        }
    }

    @Test
    public void test05ViewStockUnitsInStorageBin() {
        List<StockUnit> result = manageInventoryMonitoringRemote.viewStockUnitsInStorageBin(storageBin);
        assertNotNull(result);
        try{
            result = manageInventoryMonitoringRemote.viewStockUnitsInStorageBin(null);
            fail("Exception Not Thrown");
        }catch(Exception e){
            
        }
    }

    private static ManageOrganizationalHierarchyBeanRemote lookupManageOrganizationalHierarchyBeanRemote() {
        try {
            Context c = new InitialContext();
            return (ManageOrganizationalHierarchyBeanRemote) c.lookup("java:global/IslandFurniture/IslandFurniture-ejb/ManageOrganizationalHierarchyBean!IslandFurniture.EJB.ITManagement.ManageOrganizationalHierarchyBeanRemote");
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }
    
    private static ManageInventoryTransferRemote lookupManageInventoryTransferRemote() {
        try {
            Context c = new InitialContext();
            return (ManageInventoryTransferRemote) c.lookup("java:global/IslandFurniture/IslandFurniture-ejb/ManageInventoryTransfer!IslandFurniture.EJB.InventoryManagement.ManageInventoryTransferRemote");
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }
    
    private static ManageInventoryMonitoringRemote lookupManageInventoryMonitoringRemote() {
        try {
            Context c = new InitialContext();
            return (ManageInventoryMonitoringRemote) c.lookup("java:global/IslandFurniture/IslandFurniture-ejb/ManageInventoryMonitoring!IslandFurniture.EJB.InventoryManagement.ManageInventoryMonitoringRemote");
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }
    
    private static StockManagerRemote lookupStockManagerRemote() {
        try {
            Context c = new InitialContext();
            return (StockManagerRemote) c.lookup("java:global/IslandFurniture/IslandFurniture-ejb/StockManager!IslandFurniture.EJB.Manufacturing.StockManagerRemote");
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }
    
}
