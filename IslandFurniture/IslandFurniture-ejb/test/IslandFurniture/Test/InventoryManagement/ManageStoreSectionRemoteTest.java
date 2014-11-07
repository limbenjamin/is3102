/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.Test.InventoryManagement;

import IslandFurniture.EJB.ITManagement.ManageOrganizationalHierarchyBeanRemote;
import IslandFurniture.EJB.InventoryManagement.ManageInventoryTransferRemote;
import IslandFurniture.EJB.InventoryManagement.ManageStoreSectionRemote;
import IslandFurniture.Entities.ManufacturingFacility;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Store;
import IslandFurniture.Entities.StoreSection;
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

public class ManageStoreSectionRemoteTest {
    
    ManageStoreSectionRemote manageStoreSectionRemote = lookupManageStoreSectionRemote();
    
    static Store store;
    static ManufacturingFacility mf;
    static StoreSection ss;
    static StoreSection ss2;
    
    public ManageStoreSectionRemoteTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        ManageOrganizationalHierarchyBeanRemote manageOrganizationalHierarchyBeanRemote = lookupManageOrganizationalHierarchyBeanRemote();
        store = (Store) manageOrganizationalHierarchyBeanRemote.findPlantByNameOnly("Alexandra");
        mf = (ManufacturingFacility) manageOrganizationalHierarchyBeanRemote.findPlantByNameOnly("Tuas");
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
    public void test01CreateStoreSection() {
        try{
            manageStoreSectionRemote.createStoreSection(store, 4, "new section", "desc");
        }catch(Exception e){
            fail("Exception Thrown");
        }
        try{
            manageStoreSectionRemote.createStoreSection(store, 3, "new section2", "desc2");
        }catch(Exception e){
            fail("Exception Thrown");
        }
        try{
            manageStoreSectionRemote.createStoreSection(mf, 3, "new section2", "desc2");
            fail("Exception Not Thrown");
        }catch(Exception e){
            
        }
    }

    @Test
    public void test02ViewStoreSectionList() {
        List<StoreSection> result = manageStoreSectionRemote.viewStoreSectionList(store);
        assertNotNull(result);
        ss = result.get(0);
        ss2 = result.get(1);
        try{
            result = manageStoreSectionRemote.viewStoreSectionList(mf);
        }catch(Exception e){
            fail("Exception Thrown");
        }
        try{
            result = manageStoreSectionRemote.viewStoreSectionList(null);
            fail("Exception Not Thrown");
        }catch(Exception e){
            
        }
    }
    
    @Test
    public void test03EditStoreSection() {
        try{
            manageStoreSectionRemote.editStoreSection(ss);
        }catch(Exception e){
            fail("Exception Thrown");
        }
        try{
            manageStoreSectionRemote.editStoreSection(ss2);
        }catch(Exception e){
            fail("Exception Thrown");
        }
        try{
            manageStoreSectionRemote.editStoreSection(null);
            fail("Exception Not Thrown");
        }catch(Exception e){
            
        }
    }

    @Test
    public void test04CheckIfNoStoreSectionWithSameNameAndLevel() {
        Boolean result = manageStoreSectionRemote.checkIfNoStoreSectionWithSameNameAndLevel(store, "new section", 4);
        assertFalse(result);
        result = manageStoreSectionRemote.checkIfNoStoreSectionWithSameNameAndLevel(store, "new section2", 4);
        assertTrue(result);
        try{
            result = manageStoreSectionRemote.checkIfNoStoreSectionWithSameNameAndLevel(mf, "new section2", 4);
        }catch(Exception e){
            fail("Exception Thrown");
        }
    }

    @Test
    public void test05CheckIfNoStorefrontInventoryInThisStoreSection() {
        Boolean result = manageStoreSectionRemote.checkIfNoStorefrontInventoryInThisStoreSection(ss2);
        assertTrue(result);
        result = manageStoreSectionRemote.checkIfNoStorefrontInventoryInThisStoreSection(ss);
        assertFalse(result);
        try{
            result = manageStoreSectionRemote.checkIfNoStorefrontInventoryInThisStoreSection(null);
            fail("Exception Not Thrown");
        }catch(Exception e){
            
        }
    }
    
    @Test
    public void test06DeleteStoreSection() {
        
        try{
            manageStoreSectionRemote.deleteStoreSection(ss);
            fail("Exception Not Thrown");
        }catch(Exception e){
        }
        try{
            manageStoreSectionRemote.deleteStoreSection(ss2);
            fail("Exception Not Thrown");
        }catch(Exception e){
        }
        try{
            manageStoreSectionRemote.deleteStoreSection(null);
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
    
    private static ManageStoreSectionRemote lookupManageStoreSectionRemote() {
        try {
            Context c = new InitialContext();
            return (ManageStoreSectionRemote) c.lookup("java:global/IslandFurniture/IslandFurniture-ejb/ManageStoreSection!IslandFurniture.EJB.InventoryManagement.ManageStoreSectionRemote");
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }
    
    
}
