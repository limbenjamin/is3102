/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.Test.ITManagement;

import IslandFurniture.EJB.ITManagement.ManageOrganizationalHierarchyBeanRemote;
import IslandFurniture.Entities.Country;
import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.GlobalHQ;
import IslandFurniture.Entities.ManufacturingFacility;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Store;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
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

public class ManageOrganizationalHierarchyBeanRemoteTest {
    
    ManageOrganizationalHierarchyBeanRemote manageOrganizationalHierarchyBeanRemote = lookupManageOrganizationalHierarchyBeanRemote();
    
    static List<Country> countryList;
    static Country country;
    static Country country2;
    static CountryOffice countryOffice;
    static CountryOffice countryOffice2;
    static Country singapore;
    
    public ManageOrganizationalHierarchyBeanRemoteTest(){
        
    }
    
    @BeforeClass
    public static void setUpClass() {
        ManageOrganizationalHierarchyBeanRemote staticManageOrganizationalHierarchyBeanRemote = lookupManageOrganizationalHierarchyBeanRemote();
        singapore = staticManageOrganizationalHierarchyBeanRemote.findCountryByName("Singapore");
        country = staticManageOrganizationalHierarchyBeanRemote.findCountryByName("Andorra");
        country2 = staticManageOrganizationalHierarchyBeanRemote.findCountryByName("Anguilla");
    }

    @AfterClass
    public static void tearDownClass() {
        ManageOrganizationalHierarchyBeanRemote staticManageOrganizationalHierarchyBeanRemote = lookupManageOrganizationalHierarchyBeanRemote();
        staticManageOrganizationalHierarchyBeanRemote.deleteCountryOffice(countryOffice.getId());
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
    
    
    @Test
    public void test01GetCountries() {
        System.out.println("getCountries");
        countryList = manageOrganizationalHierarchyBeanRemote.getCountries();
        assertNotNull(countryList);
    }
    
    @Test
    public void test02FindCountryByName() {
        System.out.println("findCountryByName");
        Country result = manageOrganizationalHierarchyBeanRemote.findCountryByName("Andorra");
        assertNotNull(result);
        System.out.println("Case 2");
        assertEquals(result,country);
        System.out.println("Case 3");
        result = manageOrganizationalHierarchyBeanRemote.findCountryByName("Non Existent");
        assertNull(result);
    }
    
    @Test
    public void test03AddCountryOffice() {
        System.out.println("addCountryOffice");
        countryOffice = manageOrganizationalHierarchyBeanRemote.addCountryOffice("New CO", country, "GMT");
        assertNotNull(countryOffice);
        System.out.println("Case 2");
        CountryOffice result = manageOrganizationalHierarchyBeanRemote.addCountryOffice("New CO", country, "GMT");
        assertNull(result);
        System.out.println("Case 3");
        result = manageOrganizationalHierarchyBeanRemote.addCountryOffice("Singapore", singapore, "GMT");
        assertNull(result);
    }
    
    @Test
    public void test04EditCountryOffice() {
        System.out.println("editCountryOffice");
        try{
            manageOrganizationalHierarchyBeanRemote.editCountryOffice(countryOffice.getId(), "New name", singapore, "GMT");
        }catch (Exception ex){
            fail("Exception Thrown");
        }
        System.out.println("Case 2");
        try{
            manageOrganizationalHierarchyBeanRemote.editCountryOffice(countryOffice.getId(), "New name", country, "EST");
        }catch (Exception ex){
            
        }
        System.out.println("Case 3");
        try{
            manageOrganizationalHierarchyBeanRemote.editCountryOffice(Long.parseLong("1"), "New name", singapore, "GMT");
        }catch (Exception ex){
            fail("Exception Thrown");
        }
        
        
    }
    
    @Test
    public void test05DisplayCountryOffice() {
        System.out.println("displayCountryOffice");
        List<CountryOffice> coList = manageOrganizationalHierarchyBeanRemote.displayCountryOffice();
        assertNotNull(coList);
    }
    
    @Test
    public void test06GetCountryOffices() {
        System.out.println("getCountryOffices");
        List<CountryOffice> coList = manageOrganizationalHierarchyBeanRemote.getCountryOffices();
        assertNotNull(coList);
    }

    @Test
    public void test07FindCountryOfficeByName() {
        System.out.println("findCountryOfficeByName");
        CountryOffice co = manageOrganizationalHierarchyBeanRemote.findCountryOfficeByName("New name");
        assertNotNull(co);
        System.out.println("Case 2");
        countryOffice2 = manageOrganizationalHierarchyBeanRemote.findCountryOfficeByName("Singapore");
        assertNotNull(countryOffice2);
        System.out.println("Case 3");
        co = manageOrganizationalHierarchyBeanRemote.findCountryOfficeByName("Non Existent");
        assertNull(co);
        
    }

    /*
    @Test
    public void testAddManufacturingFacility() {
        System.out.println("addManufacturingFacility");
        String mfName = "";
        String tz = "";
        CountryOffice co = null;
        ManageOrganizationalHierarchyBeanRemote instance = new ManageOrganizationalHierarchyBeanRemoteImpl();
        ManufacturingFacility expResult = null;
        ManufacturingFacility result = instance.addManufacturingFacility(mfName, tz, co);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testAddStore() {
        System.out.println("addStore");
        String storeName = "";
        String tz = "";
        CountryOffice co = null;
        ManageOrganizationalHierarchyBeanRemote instance = new ManageOrganizationalHierarchyBeanRemoteImpl();
        Store expResult = null;
        Store result = instance.addStore(storeName, tz, co);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testDeleteCountryOffice() {
        System.out.println("deleteCountryOffice");
        Long coId = null;
        ManageOrganizationalHierarchyBeanRemote instance = new ManageOrganizationalHierarchyBeanRemoteImpl();
        instance.deleteCountryOffice(coId);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testDeleteManufacturingFacility() {
        System.out.println("deleteManufacturingFacility");
        Long mfId = null;
        ManageOrganizationalHierarchyBeanRemote instance = new ManageOrganizationalHierarchyBeanRemoteImpl();
        instance.deleteManufacturingFacility(mfId);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testDeleteStore() {
        System.out.println("deleteStore");
        Long storeId = null;
        ManageOrganizationalHierarchyBeanRemote instance = new ManageOrganizationalHierarchyBeanRemoteImpl();
        instance.deleteStore(storeId);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testDisplayCountryOffice() {
        System.out.println("displayCountryOffice");
        ManageOrganizationalHierarchyBeanRemote instance = new ManageOrganizationalHierarchyBeanRemoteImpl();
        List<CountryOffice> expResult = null;
        List<CountryOffice> result = instance.displayCountryOffice();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testDisplayManufacturingFacility() {
        System.out.println("displayManufacturingFacility");
        ManageOrganizationalHierarchyBeanRemote instance = new ManageOrganizationalHierarchyBeanRemoteImpl();
        List<ManufacturingFacility> expResult = null;
        List<ManufacturingFacility> result = instance.displayManufacturingFacility();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testDisplayStore() {
        System.out.println("displayStore");
        ManageOrganizationalHierarchyBeanRemote instance = new ManageOrganizationalHierarchyBeanRemoteImpl();
        List<Store> expResult = null;
        List<Store> result = instance.displayStore();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testEditManufacturingFacility() {
        System.out.println("editManufacturingFacility");
        Long mfId = null;
        String name = "";
        String tz = "";
        CountryOffice co = null;
        ManageOrganizationalHierarchyBeanRemote instance = new ManageOrganizationalHierarchyBeanRemoteImpl();
        instance.editManufacturingFacility(mfId, name, tz, co);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testEditStore() {
        System.out.println("editStore");
        Long storeId = null;
        String name = "";
        String tz = "";
        CountryOffice co = null;
        ManageOrganizationalHierarchyBeanRemote instance = new ManageOrganizationalHierarchyBeanRemoteImpl();
        instance.editStore(storeId, name, tz, co);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    

    @Test
    public void testFindPlantByName() {
        System.out.println("findPlantByName");
        Country country = null;
        String plantName = "";
        ManageOrganizationalHierarchyBeanRemote instance = new ManageOrganizationalHierarchyBeanRemoteImpl();
        Plant expResult = null;
        Plant result = instance.findPlantByName(country, plantName);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    

    @Test
    public void testDisplayPlant() {
        System.out.println("displayPlant");
        ManageOrganizationalHierarchyBeanRemote instance = new ManageOrganizationalHierarchyBeanRemoteImpl();
        List<Plant> expResult = null;
        List<Plant> result = instance.displayPlant();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testAddGlobalHQ() {
        System.out.println("addGlobalHQ");
        String name = "";
        Country country = null;
        String tz = "";
        ManageOrganizationalHierarchyBeanRemote instance = new ManageOrganizationalHierarchyBeanRemoteImpl();
        GlobalHQ expResult = null;
        GlobalHQ result = instance.addGlobalHQ(name, country, tz);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    

    

    @Test
    public void testFindPlantByNameOnly() {
        System.out.println("findPlantByNameOnly");
        String plantName = "";
        ManageOrganizationalHierarchyBeanRemote instance = new ManageOrganizationalHierarchyBeanRemoteImpl();
        Plant expResult = null;
        Plant result = instance.findPlantByNameOnly(plantName);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetPlantById() {
        System.out.println("getPlantById");
        Long plantId = null;
        ManageOrganizationalHierarchyBeanRemote instance = new ManageOrganizationalHierarchyBeanRemoteImpl();
        Plant expResult = null;
        Plant result = instance.getPlantById(plantId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testDisplayPlantbyInstanceOf() {
        System.out.println("displayPlantbyInstanceOf");
        Plant plant = null;
        ManageOrganizationalHierarchyBeanRemote instance = new ManageOrganizationalHierarchyBeanRemoteImpl();
        List<Plant> expResult = null;
        List<Plant> result = instance.displayPlantbyInstanceOf(plant);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    */
    
    private static ManageOrganizationalHierarchyBeanRemote lookupManageOrganizationalHierarchyBeanRemote() {
        try {
            Context c = new InitialContext();
            return (ManageOrganizationalHierarchyBeanRemote) c.lookup("java:global/IslandFurniture/IslandFurniture-ejb/ManageOrganizationalHierarchyBean!IslandFurniture.EJB.ITManagement.ManageOrganizationalHierarchyBeanRemote");
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }
    
}
