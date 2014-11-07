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
    static GlobalHQ hq;
    static ManufacturingFacility mf;
    static Store store;
    
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
    
    @Test
    public void test08AddGlobalHQ() {
        System.out.println("addGlobalHQ");
        hq = manageOrganizationalHierarchyBeanRemote.addGlobalHQ("New HQ", country, "GMT");
        assertNotNull(hq);
        System.out.println("Case 2");
        GlobalHQ result = manageOrganizationalHierarchyBeanRemote.addGlobalHQ("New HQ", country, "GMT");
        assertNull(result);
        System.out.println("Case 3");
        result = manageOrganizationalHierarchyBeanRemote.addGlobalHQ("Global HQ", singapore, "GMT");
        assertNull(result);
    }

    @Test
    public void test09AddManufacturingFacility() {
        System.out.println("addManufacturingFacility");
        mf = manageOrganizationalHierarchyBeanRemote.addManufacturingFacility("New MF", "GMT", countryOffice);
        assertNotNull(mf);
        System.out.println("Case 2");
        ManufacturingFacility result;
        try{
            result = manageOrganizationalHierarchyBeanRemote.addManufacturingFacility("Tuas", "GMT", countryOffice2);
            fail("Exception Not Thrown");
        }catch (Exception ex){
            
        }
        System.out.println("Case 3");
        try{
            result = manageOrganizationalHierarchyBeanRemote.addManufacturingFacility("New MF", "GMT", countryOffice);
            fail("Exception Not Thrown");
        }catch (Exception ex){
            
        }
    }
    
    @Test
    public void test10EditManufacturingFacility() {
        System.out.println("editManufacturingFacility");
        try{
            manageOrganizationalHierarchyBeanRemote.editManufacturingFacility(mf.getId(), "New name2", "GMT", countryOffice);
        }catch (Exception ex){
            fail("Exception Thrown");
        }
        System.out.println("Case 2");
        try{
            manageOrganizationalHierarchyBeanRemote.editManufacturingFacility(mf.getId(), "New name2", "EST", countryOffice);
        }catch (Exception ex){
            fail("Exception Thrown");
        }
        System.out.println("Case 3");
        try{
            manageOrganizationalHierarchyBeanRemote.editManufacturingFacility(null, "New name2", "EST", countryOffice);
            fail("Exception Not Thrown");
        }catch (Exception ex){
            
        }
    }
    
    @Test
    public void test11DisplayManufacturingFacility() {
        System.out.println("displayManufacturingFacility");
        List<ManufacturingFacility> mfList = manageOrganizationalHierarchyBeanRemote.displayManufacturingFacility();
        assertNotNull(mfList);
    }
    

    @Test
    public void test12AddStore() {
        System.out.println("addStore");
        store = manageOrganizationalHierarchyBeanRemote.addStore("New Store", "GMT", countryOffice);
        assertNotNull(store);
        Store result;
        System.out.println("Case 2");
        try{
            result = manageOrganizationalHierarchyBeanRemote.addStore("New Store", "GMT", countryOffice);
            fail("Exception Not Thrown");
        }catch (Exception ex){
            
        }
        
    }
    
    @Test
    public void test13EditStore() {
        System.out.println("editStore");
        try{
            manageOrganizationalHierarchyBeanRemote.editStore(store.getId(), "New name3", "GMT", countryOffice);
        }catch (Exception ex){
            fail("Exception Thrown");
        }
        System.out.println("Case 2");
        try{
            manageOrganizationalHierarchyBeanRemote.editStore(store.getId(), "New name3", "EST", countryOffice);
        }catch (Exception ex){
            fail("Exception Thrown");
        }
        System.out.println("Case 3");
        try{
            manageOrganizationalHierarchyBeanRemote.editStore(null, "New name3", "EST", countryOffice);
            fail("Exception Not Thrown");
        }catch (Exception ex){
            
        }
    }
    
    @Test
    public void test14DisplayStore() {
        System.out.println("displayStore");
        List<Store> storeList = manageOrganizationalHierarchyBeanRemote.displayStore();
        assertNotNull(storeList);
    }
    
    @Test
    public void test15FindPlantByName() {
        System.out.println("findPlantByName");
        Plant result = manageOrganizationalHierarchyBeanRemote.findPlantByName(singapore, "Alexandra");
        assertNotNull(result);
        System.out.println("Case 2");
        result = manageOrganizationalHierarchyBeanRemote.findPlantByName(country, "New name3");
        assertNotNull(result);
        System.out.println("Case 3");
        result = manageOrganizationalHierarchyBeanRemote.findPlantByName(country, "Non existent");
        assertNull(result);
    }
    
    @Test
    public void test16GetPlantById() {
        System.out.println("getPlantById");
        Plant result = manageOrganizationalHierarchyBeanRemote.getPlantById(store.getId());
        assertNotNull(result);
        System.out.println("Case 2");
        result = manageOrganizationalHierarchyBeanRemote.getPlantById(mf.getId());
        assertNotNull(result);
        System.out.println("Case 3");
        result = manageOrganizationalHierarchyBeanRemote.getPlantById(Long.parseLong("1"));
        assertNull(result);
        
    }
    
    @Test
    public void test17DeleteStore() {
        System.out.println("deleteStore");
        try{
            manageOrganizationalHierarchyBeanRemote.deleteStore(store.getId());
        }catch (Exception ex){
            fail("Exception Thrown");
        }
        System.out.println("Case 2");
        try{
            manageOrganizationalHierarchyBeanRemote.deleteStore(store.getId());
            fail("Exception Not Thrown");
        }catch (Exception ex){
            
        }
        System.out.println("Case 3");
        try{
            manageOrganizationalHierarchyBeanRemote.deleteStore(null);
            fail("Exception Not Thrown");
        }catch (Exception ex){
            
        }
    }
    
    @Test
    public void test18DeleteManufacturingFacility() {
        System.out.println("deleteManufacturingFacility");
        try{
            manageOrganizationalHierarchyBeanRemote.deleteManufacturingFacility(mf.getId());
        }catch (Exception ex){
            fail("Exception Thrown");
        }
        System.out.println("Case 2");
        try{
            manageOrganizationalHierarchyBeanRemote.deleteManufacturingFacility(mf.getId());
            fail("Exception Not Thrown");
        }catch (Exception ex){
        }
        System.out.println("Case 3");
        try{
            manageOrganizationalHierarchyBeanRemote.deleteManufacturingFacility(null);
            fail("Exception Not Thrown");
        }catch (Exception ex){
            
        }
    }

    @Test
    public void test19DeleteCountryOffice() {
        System.out.println("deleteCountryOffice");
        try{
            manageOrganizationalHierarchyBeanRemote.deleteCountryOffice(countryOffice.getId());
        }catch (Exception ex){
            fail("Exception Thrown");
        }
        System.out.println("Case 2");
        try{
            manageOrganizationalHierarchyBeanRemote.deleteCountryOffice(countryOffice2.getId());
             fail("Exception Not Thrown");
        }catch (Exception ex){
           
        }
        System.out.println("Case 3");
        try{
            manageOrganizationalHierarchyBeanRemote.deleteCountryOffice(null);
            fail("Exception Not Thrown");
        }catch (Exception ex){
            
        }
    }

    @Test
    public void test20DisplayCountryOffice() {
        System.out.println("displayCountryOffice");
        List<CountryOffice> coList =  manageOrganizationalHierarchyBeanRemote.displayCountryOffice();
        assertNotNull(coList);
    }
    

    @Test
    public void test21DisplayPlant() {
        System.out.println("displayPlant");
        List<Plant> plantList = manageOrganizationalHierarchyBeanRemote.displayPlant();
        assertNotNull(plantList);
    }
    
    @Test
    public void test22DeleteGlobalHQ() {
        System.out.println("deleteGlobalHQ");
        try{
            manageOrganizationalHierarchyBeanRemote.deleteGlobalHQ(hq.getId());
            
        }catch (Exception ex){
            fail("Exception Thrown");
        }
        System.out.println("Case 3");
        try{
            manageOrganizationalHierarchyBeanRemote.deleteGlobalHQ(null);
            fail("Exception Not Thrown");
        }catch (Exception ex){
            
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
    
}
