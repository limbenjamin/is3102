/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.Test.Purchasing;

import IslandFurniture.EJB.ITManagement.ManageOrganizationalHierarchyBeanRemote;
import IslandFurniture.EJB.Manufacturing.StockManagerRemote;
import IslandFurniture.EJB.Purchasing.ManagePurchaseOrderRemote;
import IslandFurniture.EJB.Purchasing.SupplierManagerRemote;
import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.ManufacturingFacility;
import IslandFurniture.Entities.ProcuredStock;
import IslandFurniture.Entities.ProcuredStockPurchaseOrder;
import IslandFurniture.Entities.ProcuredStockPurchaseOrderDetail;
import IslandFurniture.Entities.ProcuredStockSupplier;
import IslandFurniture.Entities.Stock;
import IslandFurniture.Entities.Store;
import IslandFurniture.Entities.Supplier;
import IslandFurniture.Enums.PurchaseOrderStatus;
import IslandFurniture.Exceptions.DuplicateEntryException;
import IslandFurniture.Exceptions.InvalidStatusException;
import IslandFurniture.Exceptions.NoLotsException;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.jboss.weld.event.Status;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
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

public class ManagePurchaseOrderRemoteTest {

    ManagePurchaseOrderRemote managePurchaseOrder = lookupManagePurchaseOrderRemote();
    
    static ManufacturingFacility mf;
    static ManufacturingFacility mf2;
    static CountryOffice co;
    static Store store;
    static Stock stock;
    static Stock stock2;
    static Supplier supplier;
    
    static ProcuredStockPurchaseOrder po;
    static ProcuredStockPurchaseOrder po2;
    static ProcuredStockPurchaseOrderDetail pod;

    public ManagePurchaseOrderRemoteTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        ManageOrganizationalHierarchyBeanRemote manageOrganizationalHierarchyBeanRemote = lookupManageOrganizationalHierarchyBeanRemote();
        StockManagerRemote stockManagerRemote = lookupStockManagerRemote();
        SupplierManagerRemote supplierManagerRemote = lookupSupplierManagerRemote();
        
        mf = (ManufacturingFacility) manageOrganizationalHierarchyBeanRemote.getPlantById(Long.parseLong("412")); //Tuas MF
        mf2 = manageOrganizationalHierarchyBeanRemote.displayManufacturingFacility().get(1);
        co = manageOrganizationalHierarchyBeanRemote.displayCountryOffice().get(0);
        store = manageOrganizationalHierarchyBeanRemote.displayStore().get(0);
        stock = stockManagerRemote.getStock(Long.parseLong("432")); //flathead screw
        stock2 = stockManagerRemote.displayFurnitureList().get(1);
        supplier = supplierManagerRemote.displaySupplierList().get(0);
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
    public void test01CreateNewPurchaseOrder() throws Exception {
        System.out.println("createNewPurchaseOrder");
        Calendar calendar = Calendar.getInstance();
        po = managePurchaseOrder.createNewPurchaseOrder(PurchaseOrderStatus.PLANNED, (ProcuredStockSupplier) supplier, mf, store, calendar);
        assertNotNull(po);
        System.out.println("Case 2");
        try{
            ProcuredStockPurchaseOrder result = managePurchaseOrder.createNewPurchaseOrder(null, (ProcuredStockSupplier) supplier, mf, store, calendar);
            fail("Exception Not Thrown");
        }catch (InvalidStatusException ex){
        }
        System.out.println("Case 3");
        po2 = managePurchaseOrder.createNewPurchaseOrder(PurchaseOrderStatus.PAID, (ProcuredStockSupplier) supplier, mf, co, calendar);
        assertNotNull(po2);
    }
    
    @Test
    public void test02CreateNewPurchaseOrderDetail() throws Exception {
        System.out.println("createNewPurchaseOrderDetail");
        pod = managePurchaseOrder.createNewPurchaseOrderDetail(po.getId(), stock.getId(), 10);
        assertNotNull(pod);
        System.out.println("Case 2");
        try{
            ProcuredStockPurchaseOrderDetail result = managePurchaseOrder.createNewPurchaseOrderDetail(po.getId(), stock.getId(), 10);
            fail("Exception Not Thrown");
        }catch (DuplicateEntryException ex){
        }
        System.out.println("Case 3");
        try{
            ProcuredStockPurchaseOrderDetail result = managePurchaseOrder.createNewPurchaseOrderDetail(po.getId(), stock.getId(), 0);
            fail("Exception Not Thrown");
        }catch (NoLotsException ex){
        }
    }

    @Test
    public void test03GetPurchaseOrder() throws Exception {
        System.out.println("getPurchaseOrder");
        ProcuredStockPurchaseOrder result = managePurchaseOrder.getPurchaseOrder(po.getId());
        assertNotNull(result);
        System.out.println("Case 2");
        result = managePurchaseOrder.getPurchaseOrder(stock.getId());
        assertNull(result);
        System.out.println("Case 3");
        try{
            result = managePurchaseOrder.getPurchaseOrder(null);
            fail("Exception Not Thrown");
        }catch (Exception ex){
        }
    }


    @Test
    public void test04UpdatePurchaseOrder() throws Exception {
        System.out.println("updatePurchaseOrder");
        Calendar cal = Calendar.getInstance();
        try{
            managePurchaseOrder.updatePurchaseOrder(po.getId(), PurchaseOrderStatus.CONFIRMED, cal);
        }catch (Exception ex){
            ex.printStackTrace();
            fail("Exception Thrown");
        }
        System.out.println("Case 2");
        try{
            managePurchaseOrder.updatePurchaseOrder(po2.getId(), PurchaseOrderStatus.DELIVERED, cal);
        }catch (Exception ex){
            fail("Exception Thrown");
        }
        System.out.println("Case 3");
        try{
            managePurchaseOrder.updatePurchaseOrder(Long.parseLong("1"), null, cal);
            fail("Exception Not Thrown");
        }catch (Exception ex){
        }
     }
    
     @Test
     public void test05UpdatePurchaseOrderDetail() throws Exception {
        System.out.println("updatePurchaseOrderDetail");
        ProcuredStockPurchaseOrderDetail pod2 = new ProcuredStockPurchaseOrderDetail();
        pod2.setProcuredStock((ProcuredStock) stock);
        pod2.setPurchaseOrder(po);
        pod2.setNumberOfLots(20);
        try{
            managePurchaseOrder.updatePurchaseOrderDetail(pod2);
        }catch (Exception ex){
            fail("Exception Thrown");
        }
        System.out.println("Case 2");
        pod2.setNumberOfLots(0);
        try{
            managePurchaseOrder.updatePurchaseOrderDetail(pod2);
            fail("Exception Not Thrown");
        }catch (NoLotsException ex){
        }
        System.out.println("Case 3");
        pod2.setNumberOfLots(30);
        try{
            managePurchaseOrder.updatePurchaseOrderDetail(pod2);
        }catch (NoLotsException ex){
            fail("Exception Thrown");
        }
     }

     @Test
     public void test06ViewPurchaseOrderDetails() throws Exception {
        System.out.println("viewPurchaseOrderDetails");
        List<ProcuredStockPurchaseOrderDetail> result = managePurchaseOrder.viewPurchaseOrderDetails(po.getId());
        assertNotNull(result);
        System.out.println("Case 2");
        try{
            result = managePurchaseOrder.viewPurchaseOrderDetails(stock.getId());
            fail("Exception Not Thrown");
        }catch (Exception ex){
        }
        System.out.println("Case 3");
        try{
            result = managePurchaseOrder.viewPurchaseOrderDetails(null);
            fail("Exception Not Thrown");
        }catch (Exception ex){
        }
     }

     @Test
     public void test07ViewSupplierProcuredStocks() throws Exception {
        System.out.println("viewSupplierProcuredStocks");
        List<ProcuredStock> result = managePurchaseOrder.viewSupplierProcuredStocks(po.getId(), mf);
        assertNotNull(result);
        System.out.println("Case 2");
        result = managePurchaseOrder.viewSupplierProcuredStocks(po2.getId(), mf);
        assertNotNull(result);
        System.out.println("Case 3"); 
        try{
            result = managePurchaseOrder.viewSupplierProcuredStocks(null, mf);
            fail("Exception Not Thrown");
        }catch (Exception ex){
        }
     }

    @Test
     public void test08GetLotSize() throws Exception {
        System.out.println("getLotSize");
        int result = managePurchaseOrder.getLotSize((ProcuredStock) stock, mf);
        assertNotNull(result);
        System.out.println("Case 2");
        try{
            managePurchaseOrder.getLotSize((ProcuredStock) stock, mf2);
            fail("Exception Not Thrown");
        }catch (Exception ex){
        }
        System.out.println("Case 3");
        try{
            managePurchaseOrder.getLotSize(null, mf);
            fail("Exception Not Thrown");
        }catch (Exception ex){
        }
     }

     @Test
     public void test09DeletePurchaseOrderDetail() throws Exception {
        System.out.println("deletePurchaseOrderDetail");
        try{
            managePurchaseOrder.deletePurchaseOrderDetail(pod.getId());
        }catch (Exception ex){
            fail("Exception Thrown");
        }
        System.out.println("Case 2");
        try{
            managePurchaseOrder.deletePurchaseOrderDetail(Long.parseLong("1"));
            fail("Exception Not Thrown");
        }catch (Exception ex){
            
        }
        System.out.println("Case 3");
        try{
            managePurchaseOrder.deletePurchaseOrderDetail(null);
            fail("Exception Not Thrown");
        }catch (Exception ex){
            
        }
     }
     
     @Test
     public void test10DeletePurchaseOrder() throws Exception {
        System.out.println("deletePurchaseOrder");
        try{
            managePurchaseOrder.deletePurchaseOrder(po.getId());
        }catch (Exception ex){
            fail("Exception Thrown");
        }
        System.out.println("Case 2");
        try{
            managePurchaseOrder.deletePurchaseOrder(po2.getId());
        }catch (Exception ex){
            fail("Exception Thrown");
        }
        System.out.println("Case 3");
        try{
            managePurchaseOrder.deletePurchaseOrder(null);
            fail("Exception Not Thrown");
        }catch (Exception ex){
        }
     }
     
     @Test
     public void test11ViewContractedSuppliers() throws Exception {
        System.out.println("viewContractedSuppliers");
        List<ProcuredStockSupplier> result = managePurchaseOrder.viewContractedSuppliers(mf);
        assertNotNull(result);
        System.out.println("Case 2");
        result = managePurchaseOrder.viewContractedSuppliers(mf2);
        assertNotNull(result);
        System.out.println("Case 3");
        result = managePurchaseOrder.viewContractedSuppliers(null);
        assertEquals(0,result.size());
     }
     
     @Test
     public void test12ViewPlannedPurchaseOrders() throws Exception {
        System.out.println("viewPlannedPurchaseOrders");
        List<ProcuredStockPurchaseOrder> result ;
        try{
           result = managePurchaseOrder.viewPlannedPurchaseOrders(store);
           fail("Exception Not Thrown");
           }catch (Exception ex){
           }
        System.out.println("Case 2");
        result = managePurchaseOrder.viewPlannedPurchaseOrders(mf);
        assertNotNull(result);
        System.out.println("Case 3");
        result = managePurchaseOrder.viewPlannedPurchaseOrders(null);
        assertEquals(0,result.size());
     }

     @Test
     public void test13ViewConfirmedPurchaseOrders() throws Exception {
        System.out.println("viewConfirmedPurchaseOrders");
        List<ProcuredStockPurchaseOrder> result ;
        try{
           result = managePurchaseOrder.viewConfirmedPurchaseOrders(store);
           fail("Exception Not Thrown");
           }catch (Exception ex){
           }
        System.out.println("Case 2");
        result = managePurchaseOrder.viewConfirmedPurchaseOrders(mf);
        assertNotNull(result);
        System.out.println("Case 3");
        result = managePurchaseOrder.viewConfirmedPurchaseOrders(null);
        assertEquals(0,result.size());
     }

     @Test
     public void test14ViewDeliveredPurchaseOrders() throws Exception {
        System.out.println("viewDeliveredPurchaseOrders");
        List<ProcuredStockPurchaseOrder> result ;
        try{
           result = managePurchaseOrder.viewDeliveredPurchaseOrders(store);
           fail("Exception Not Thrown");
           }catch (Exception ex){
           }
        System.out.println("Case 2");
        result = managePurchaseOrder.viewDeliveredPurchaseOrders(mf);
        assertNotNull(result);
        System.out.println("Case 3");
        result = managePurchaseOrder.viewDeliveredPurchaseOrders(null);
        assertEquals(0,result.size());
     }

     @Test
     public void test15ViewPaidPurchaseOrders() throws Exception {
        System.out.println("viewPaidPurchaseOrders");
        List<ProcuredStockPurchaseOrder> result ;
        try{
           result = managePurchaseOrder.viewPaidPurchaseOrders(store);
           fail("Exception Not Thrown");
           }catch (Exception ex){
           }
        System.out.println("Case 2");
        result = managePurchaseOrder.viewPaidPurchaseOrders(mf);
        assertNotNull(result);
        System.out.println("Case 3");
        result = managePurchaseOrder.viewPaidPurchaseOrders(null);
        assertEquals(0,result.size());
     }
     
     
    private ManagePurchaseOrderRemote lookupManagePurchaseOrderRemote() {
        try {
            Context c = new InitialContext();
            return (ManagePurchaseOrderRemote) c.lookup("java:global/IslandFurniture/IslandFurniture-ejb/ManagePurchaseOrder!IslandFurniture.EJB.Purchasing.ManagePurchaseOrderRemote");
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
    
    private static SupplierManagerRemote lookupSupplierManagerRemote() {
        try {
            Context c = new InitialContext();
            return (SupplierManagerRemote) c.lookup("java:global/IslandFurniture/IslandFurniture-ejb/SupplierManager!IslandFurniture.EJB.Purchasing.SupplierManagerRemote");
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }

}
