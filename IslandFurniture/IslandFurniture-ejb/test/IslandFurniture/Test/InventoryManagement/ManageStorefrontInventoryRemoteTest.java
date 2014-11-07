/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.Test.InventoryManagement;

import IslandFurniture.EJB.ITManagement.ManageOrganizationalHierarchyBeanRemote;
import IslandFurniture.EJB.InventoryManagement.ManageInventoryMonitoringRemote;
import IslandFurniture.EJB.InventoryManagement.ManageStorefrontInventoryRemote;
import IslandFurniture.EJB.Manufacturing.StockManagerRemote;
import IslandFurniture.Entities.ManufacturingFacility;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Stock;
import IslandFurniture.Entities.Store;
import IslandFurniture.Entities.StoreSection;
import IslandFurniture.Entities.StorefrontInventory;
import static IslandFurniture.Test.InventoryManagement.ManageInventoryMonitoringRemoteTest.stock;
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

public class ManageStorefrontInventoryRemoteTest {
    
    ManageStorefrontInventoryRemote manageStorefrontInventoryRemote = lookupManageStorefrontInventoryRemote();
    
    static StorefrontInventory si;
    static Store store; 
    static ManufacturingFacility mf; 
    static StoreSection ss; 
    static Stock stock;
    
    public ManageStorefrontInventoryRemoteTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        ManageOrganizationalHierarchyBeanRemote manageOrganizationalHierarchyBeanRemote = lookupManageOrganizationalHierarchyBeanRemote();
        StockManagerRemote stockManagerRemote = lookupStockManagerRemote();
        store = manageOrganizationalHierarchyBeanRemote.displayStore().get(0);
        mf = manageOrganizationalHierarchyBeanRemote.displayManufacturingFacility().get(0);
        ss = store.getStoreSections().get(0);
        stock = stockManagerRemote.getStock(Long.parseLong("432"));
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
    public void test01CreateStorefrontInventory() {
        try{
            manageStorefrontInventoryRemote.createStorefrontInventory(store, stock.getId(), 10, 20, ss.getId());
        }catch(Exception e){
            fail("Exception Thrown");
        }
        try{
            manageStorefrontInventoryRemote.createStorefrontInventory(mf, stock.getId(), 10, 20, ss.getId());
            fail("Exception Not Thrown");
        }catch(Exception e){
            
        }
        try{
            manageStorefrontInventoryRemote.createStorefrontInventory(store, stock.getId(), 10, 20, ss.getId());
            fail("Exception Not Thrown");
        }catch(Exception e){
            
        }
    }

    @Test
    public void test02EditStorefrontInventory() {
        try{
            manageStorefrontInventoryRemote.editStorefrontInventory(si, ss.getId(), 10, 50);
            fail("Exception Not Thrown");
        }catch(Exception e){
            
        }
        try{
            manageStorefrontInventoryRemote.editStorefrontInventory(null, ss.getId(), 10, 50);
            fail("Exception Not Thrown");
        }catch(Exception e){
        }
    }

    @Test
    public void test03ViewStorefrontInventory() {
        List<StorefrontInventory> siList;
        try{
            siList = manageStorefrontInventoryRemote.viewStorefrontInventory(store);
        }catch(Exception e){
            fail("Exception Thrown");
        }
        try{
            siList = manageStorefrontInventoryRemote.viewStorefrontInventory(null);
            fail("Exception Not Thrown");
        }catch(Exception e){
            
        }
    }

    @Test
    public void test04GetStorefrontInventory() {
        StorefrontInventory result;
        si = manageStorefrontInventoryRemote.getStorefrontInventory(store, stock.getId());
        assertNotNull(si);
        try{
            result = manageStorefrontInventoryRemote.getStorefrontInventory(mf, stock.getId());
            
        }catch(Exception e){
            fail("Exception Thrown");
        }
        try{
            result = manageStorefrontInventoryRemote.getStorefrontInventory(null, stock.getId());
            fail("Exception Not Thrown");
        }catch(Exception e){
        }
    }

    @Test
    public void test05ReduceStockfrontInventoryFromTransaction() {
        try{
            manageStorefrontInventoryRemote.reduceStockfrontInventoryFromTransaction(store, stock, 1);
        }catch(Exception e){
            fail("Exception Thrown");
        }
        try{
            manageStorefrontInventoryRemote.reduceStockfrontInventoryFromTransaction(mf, stock, 1);
            fail("Exception Not Thrown");
        }catch(Exception e){
            
        }
        try{
            manageStorefrontInventoryRemote.reduceStockfrontInventoryFromTransaction(null, stock, 1);
            fail("Exception Not Thrown");
        }catch(Exception e){
            
        }
    }

    @Test
    public void test06EditStorefrontInventoryQty() {
        try{
            manageStorefrontInventoryRemote.editStorefrontInventoryQty(si, 5);
        }catch(Exception e){
            fail("Exception Thrown");
        }
        try{
            manageStorefrontInventoryRemote.editStorefrontInventoryQty(si, 0);
        }catch(Exception e){
            fail("Exception Thrown");
        }
        try{
            manageStorefrontInventoryRemote.editStorefrontInventoryQty(null, 5);
            fail("Exception Not Thrown");
        }catch(Exception e){
            
        }
    }
    
    @Test
    public void test07DeleteStorefrontInventory() {
        try{
            manageStorefrontInventoryRemote.deleteStorefrontInventory(si);
            
        }catch(Exception e){
            fail("Exception Thrown");
        }
        try{
            manageStorefrontInventoryRemote.deleteStorefrontInventory(null);
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
    
    private static StockManagerRemote lookupStockManagerRemote() {
        try {
            Context c = new InitialContext();
            return (StockManagerRemote) c.lookup("java:global/IslandFurniture/IslandFurniture-ejb/StockManager!IslandFurniture.EJB.Manufacturing.StockManagerRemote");
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }    
    
    private static ManageStorefrontInventoryRemote lookupManageStorefrontInventoryRemote() {
        try {
            Context c = new InitialContext();
            return (ManageStorefrontInventoryRemote) c.lookup("java:global/IslandFurniture/IslandFurniture-ejb/ManageStorefrontInventory!IslandFurniture.EJB.InventoryManagement.ManageStorefrontInventoryRemote");
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    } 
    
}
