/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.Test.Purchasing;

import IslandFurniture.EJB.ITManagement.ManageOrganizationalHierarchyBeanRemote;
import IslandFurniture.EJB.Manufacturing.StockManagerRemote;
import IslandFurniture.EJB.Purchasing.SupplierManagerRemote;
import IslandFurniture.Entities.Country;
import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.Currency;
import IslandFurniture.Entities.ManufacturingFacility;
import IslandFurniture.Entities.ProcuredStock;
import IslandFurniture.Entities.ProcuredStockContractDetail;
import IslandFurniture.Entities.ProcuredStockSupplier;
import IslandFurniture.Entities.Stock;
import IslandFurniture.Entities.StockSupplied;
import IslandFurniture.Entities.Supplier;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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

public class SupplierManagerRemoteTest {
    
    SupplierManagerRemote supplierManagerRemote = lookupSupplierManagerRemote();
    
    static Long supplierId;
    static Long supplier2Id;
    static ProcuredStockSupplier supplier;
    static ProcuredStockSupplier supplier2; 
    static ManufacturingFacility mf;
    static Country country;
    static Stock stock;
    static ProcuredStockContractDetail pscd;
    static CountryOffice co;
    static Long pscdId;
    
    public SupplierManagerRemoteTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        ManageOrganizationalHierarchyBeanRemote manageOrganizationalHierarchyBeanRemote = lookupManageOrganizationalHierarchyBeanRemote();
        StockManagerRemote stockManagerRemote = lookupStockManagerRemote();
        country = manageOrganizationalHierarchyBeanRemote.findCountryByName("Singapore");
        mf = (ManufacturingFacility) manageOrganizationalHierarchyBeanRemote.findPlantByNameOnly("Tuas");
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
    public void test01AddSupplier() {
        String result = supplierManagerRemote.addSupplier("new supplier", country.getName(), "91234567", "a@test.com");
        assertNotNull(result);
        supplierId = Long.parseLong(result.substring(0, result.length()-2));
        result = supplierManagerRemote.addSupplier("new supplier", "Non existent", "91234567", "a@test.com");
        assertNotNull(result);
        result = supplierManagerRemote.addSupplier("new supplier2", country.getName(), "91234567", "b@test.com");
        assertNotNull(result);
        supplier2Id = Long.parseLong(result.substring(0, result.length()-2));
    }   
    
    @Test
    public void test02DisplaySupplierList() {
        List<ProcuredStockSupplier> supplierList = supplierManagerRemote.displaySupplierList();
        assertNotNull(supplierList);
    }

    @Test
    public void test03GetSupplier() {
        supplier = supplierManagerRemote.getSupplier(supplierId);
        assertNotNull(supplier);
        try{
            supplier = supplierManagerRemote.getSupplier(Long.parseLong("1"));
        }catch (Exception ex){
            fail("Exception Thrown");
        }
        try{
            supplier = supplierManagerRemote.getSupplier(null);
            fail("Exception Not Thrown");
        }catch (Exception ex){
            
        }
    }
    
    @Test
    public void test04EditSupplier() {
        try{
            supplierManagerRemote.editSupplier(supplierId, "new sup", country.getName(), "91234567", "a@test.com");
        }catch (Exception ex){
            fail("Exception Thrown");
        }
        try{
            supplierManagerRemote.editSupplier(supplier2Id, "new sup2", country.getName(), "91234567", "b@test.com");
        }catch (Exception ex){
            fail("Exception Thrown");
        }
        try{
            supplierManagerRemote.editSupplier(Long.parseLong("1"), "new sup2", country.getName(), "91234567", "b@test.com");
        }catch (Exception ex){
            fail("Exception Thrown");
        }
    }

    @Test
    public void test05DisplayProcurementContractDetails() {
        List<ProcuredStockContractDetail> pscdList = supplierManagerRemote.displayProcurementContractDetails(String.valueOf(supplierId));
        assertNotNull(pscdList);
        List<ProcuredStockContractDetail> result;
        try{
            result = supplierManagerRemote.displayProcurementContractDetails("1");
        }catch (Exception ex){
            fail("Exception Thrown");
        }
        result = supplierManagerRemote.displayProcurementContractDetails(String.valueOf(String.valueOf(supplier2Id)));
        assertNotNull(result);
        
    }
    
    @Test
    public void test06DisplayProcuredStock() {
        try{
            List<ProcuredStock> result = supplierManagerRemote.displayProcuredStock();
        }catch (Exception ex){
            fail("Exception Thrown");
        }
    }
    
    @Test
    public void test07DisplayManufacturingFacility() {
        try{
            List<ManufacturingFacility> result = supplierManagerRemote.displayManufacturingFacility();
        }catch (Exception ex){
            fail("Exception Thrown");
        }        
    }
    
    @Test
    public void test08GetAllStockSupplied() {
        try{
            List<StockSupplied> result = supplierManagerRemote.getAllStockSupplied();
        }catch (Exception ex){
            fail("Exception Thrown");
        }  
    }
     
    @Test
    public void test09GetListOfCountryOffice() {
        try{
            List<CountryOffice> result = supplierManagerRemote.getListOfCountryOffice();
        }catch (Exception ex){
            fail("Exception Thrown");
        }
    } 
    
    @Test
    public void test10GetListOfMF() {
        try{
            List<ManufacturingFacility> result = supplierManagerRemote.getListOfMF();
        }catch (Exception ex){
            fail("Exception Thrown");
        }
    }
    
    @Test
    public void test11AddStockSupplyRequest() {
        String result;
        try{
            result = supplierManagerRemote.addStockSupplyRequest(stock.getId(), mf.getId(), country.getId());
            fail("Exception Not Thrown");
        }catch(Exception e){
            
        }
        try{
            result = supplierManagerRemote.addStockSupplyRequest(stock.getId(), Long.parseLong("1"), country.getId());
            
        }catch(Exception e){
            fail("Exception Thrown");
        }
        try{
            result = supplierManagerRemote.addStockSupplyRequest(Long.parseLong("1"), mf.getId(), country.getId());
            fail("Exception Not Thrown");
        }catch(Exception e){
            
        }
    }
    
    @Test
    public void test12DeleteStockSupplyRequest() {
        String result = supplierManagerRemote.deleteStockSupplyRequest(stock.getId(), mf.getId(), country.getId());
        assertNotNull(result);
        result = supplierManagerRemote.deleteStockSupplyRequest(stock.getId(), mf.getId(), country.getId());
        assertNotNull(result);
        result = supplierManagerRemote.deleteStockSupplyRequest(Long.parseLong("1"), mf.getId(), country.getId());
        assertNotNull(result);        
    }

    @Test
    public void test13GetListOfStock() {
        try{
            List<Stock> result = supplierManagerRemote.getListOfStock();
        }catch(Exception e){
            fail("Exception Thrown");
        }
    }
    
    @Test
    public void test14GetPriceForStock() {
        try{
            Double result = supplierManagerRemote.getPriceForStock(null, stock);
            fail("Exception Not Thrown");
        }catch(Exception e){
            
        }
        try{
            Double result = supplierManagerRemote.getPriceForStock(co, null);
            fail("Exception Not Thrown");
        }catch(Exception e){
            
        }
    }
    
    @Test
    public void test15DeleteProcurementContractDetail() {
        String result = supplierManagerRemote.deleteProcurementContractDetail(pscdId, supplierId);
        assertNotNull(result);
        result = supplierManagerRemote.deleteProcurementContractDetail(Long.parseLong("1"), supplierId);
        assertNotNull(result);
        try{
            result = supplierManagerRemote.deleteProcurementContractDetail(pscd.getId(), Long.parseLong("1"));
            fail("Exception Not Thrown");
        }catch(Exception e){
            
        }
    }

    @Test
    public void test16DeleteSupplier() {
        String result = supplierManagerRemote.deleteSupplier(supplierId);
        assertNull(result);
        result = supplierManagerRemote.deleteSupplier(supplier2Id);
        assertNull(result);
        result = supplierManagerRemote.deleteSupplier(supplier2Id);
        assertNotNull(result);
    }

    @Test
    public void test17CheckForValidPCD() {
        List<Stock> result;
        try{
            result = supplierManagerRemote.checkForValidPCD(stock.getId(), mf.getId());
        }catch(Exception e){
            fail("Exception Thrown");
        }
        try{
            result = supplierManagerRemote.checkForValidPCD(Long.parseLong("1"), mf.getId());
        }catch(Exception e){
            fail("Exception Thrown");
        }
        try{
            result = supplierManagerRemote.checkForValidPCD(stock.getId(), Long.parseLong("1"));
        }catch(Exception e){
            fail("Exception Thrown");
        }
    }

    private SupplierManagerRemote lookupSupplierManagerRemote() {
        try {
            Context c = new InitialContext();
            return (SupplierManagerRemote) c.lookup("java:global/IslandFurniture/IslandFurniture-ejb/SupplierManager!IslandFurniture.EJB.Purchasing.SupplierManagerRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
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
}
