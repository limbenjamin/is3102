/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.Purchasing;

import IslandFurniture.Entities.ManufacturingFacility;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.ProcuredStockPurchaseOrder;
import IslandFurniture.Entities.ProcuredStockSupplier;
import IslandFurniture.Enums.PurchaseOrderStatus;
import java.util.Calendar;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author Benjamin
 */
public class ManagePurchaseOrderTest {

    ManagePurchaseOrderRemote managePurchaseOrder = lookupManagePurchaseOrderRemote();

    public ManagePurchaseOrderTest() {
    }

    @BeforeClass
    public static void setUpClass() {
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
    public void testCreateNewPurchaseOrder() throws Exception {
        System.out.println("createNewPurchaseOrder");
        PurchaseOrderStatus status = null;
        ProcuredStockSupplier supplier = null;
        ManufacturingFacility mf = null;
        Plant shipsTo = null;
        Calendar orderDate = null;
        ProcuredStockPurchaseOrder expResult = null;
        ProcuredStockPurchaseOrder result = managePurchaseOrder.createNewPurchaseOrder(status, supplier, mf, shipsTo, orderDate);
        assertNotNull(result);
    }

    @Test
    public void testGetPurchaseOrder() throws Exception {
        System.out.println("getPurchaseOrder");
        Long id = new Long(57351);
        ProcuredStockPurchaseOrder expResult = new ProcuredStockPurchaseOrder();
        expResult.setId(id);
        ProcuredStockPurchaseOrder result = managePurchaseOrder.getPurchaseOrder(id);
        assertEquals(expResult, result);
    }
    /*@Test
     public void testCreateNewPurchaseOrderDetail() throws Exception {
     System.out.println("createNewPurchaseOrderDetail");
     Long poId = null;
     Long stockId = null;
     int numberOfLots = 0;
     EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
     ManagePurchaseOrderLocal instance = (ManagePurchaseOrderLocal)container.getContext().lookup("java:global/classes/ManagePurchaseOrder");
     instance.createNewPurchaseOrderDetail(poId, stockId, numberOfLots);
     container.close();
     // TODO review the generated test code and remove the default call to fail.
     fail("The test case is a prototype.");
     }

     @Test
     public void testUpdatePurchaseOrder() throws Exception {
     System.out.println("updatePurchaseOrder");
     Long poId = null;
     PurchaseOrderStatus status = null;
     Calendar orderDate = null;
     EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
     ManagePurchaseOrderLocal instance = (ManagePurchaseOrderLocal)container.getContext().lookup("java:global/classes/ManagePurchaseOrder");
     instance.updatePurchaseOrder(poId, status, orderDate);
     container.close();
     // TODO review the generated test code and remove the default call to fail.
     fail("The test case is a prototype.");
     }

     @Test
     public void testUpdatePurchaseOrderDetail() throws Exception {
     System.out.println("updatePurchaseOrderDetail");
     ProcuredStockPurchaseOrderDetail pod = null;
     EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
     ManagePurchaseOrderLocal instance = (ManagePurchaseOrderLocal)container.getContext().lookup("java:global/classes/ManagePurchaseOrder");
     instance.updatePurchaseOrderDetail(pod);
     container.close();
     // TODO review the generated test code and remove the default call to fail.
     fail("The test case is a prototype.");
     }

     @Test
     public void testViewPurchaseOrderDetails() throws Exception {
     System.out.println("viewPurchaseOrderDetails");
     Long orderId = null;
     EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
     ManagePurchaseOrderLocal instance = (ManagePurchaseOrderLocal)container.getContext().lookup("java:global/classes/ManagePurchaseOrder");
     List<ProcuredStockPurchaseOrderDetail> expResult = null;
     List<ProcuredStockPurchaseOrderDetail> result = instance.viewPurchaseOrderDetails(orderId);
     assertEquals(expResult, result);
     container.close();
     // TODO review the generated test code and remove the default call to fail.
     fail("The test case is a prototype.");
     }

     @Test
     public void testViewSupplierProcuredStocks() throws Exception {
     System.out.println("viewSupplierProcuredStocks");
     Long orderId = null;
     ManufacturingFacility mf = null;
     EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
     ManagePurchaseOrderLocal instance = (ManagePurchaseOrderLocal)container.getContext().lookup("java:global/classes/ManagePurchaseOrder");
     List<ProcuredStock> expResult = null;
     List<ProcuredStock> result = instance.viewSupplierProcuredStocks(orderId, mf);
     assertEquals(expResult, result);
     container.close();
     // TODO review the generated test code and remove the default call to fail.
     fail("The test case is a prototype.");
     }

     @Test
     public void testGetLotSize() throws Exception {
     System.out.println("getLotSize");
     ProcuredStock stock = null;
     ManufacturingFacility mf = null;
     EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
     ManagePurchaseOrderLocal instance = (ManagePurchaseOrderLocal)container.getContext().lookup("java:global/classes/ManagePurchaseOrder");
     Integer expResult = null;
     Integer result = instance.getLotSize(stock, mf);
     assertEquals(expResult, result);
     container.close();
     // TODO review the generated test code and remove the default call to fail.
     fail("The test case is a prototype.");
     }

     @Test
     public void testDeletePurchaseOrder() throws Exception {
     System.out.println("deletePurchaseOrder");
     Long poId = null;
     EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
     ManagePurchaseOrderLocal instance = (ManagePurchaseOrderLocal)container.getContext().lookup("java:global/classes/ManagePurchaseOrder");
     instance.deletePurchaseOrder(poId);
     container.close();
     // TODO review the generated test code and remove the default call to fail.
     fail("The test case is a prototype.");
     }

     @Test
     public void testDeletePurchaseOrderDetail() throws Exception {
     System.out.println("deletePurchaseOrderDetail");
     Long podId = null;
     EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
     ManagePurchaseOrderLocal instance = (ManagePurchaseOrderLocal)container.getContext().lookup("java:global/classes/ManagePurchaseOrder");
     instance.deletePurchaseOrderDetail(podId);
     container.close();
     // TODO review the generated test code and remove the default call to fail.
     fail("The test case is a prototype.");
     }

     @Test
     public void testViewContractedSuppliers() throws Exception {
     System.out.println("viewContractedSuppliers");
     ManufacturingFacility mf = null;
     EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
     ManagePurchaseOrderLocal instance = (ManagePurchaseOrderLocal)container.getContext().lookup("java:global/classes/ManagePurchaseOrder");
     List<ProcuredStockSupplier> expResult = null;
     List<ProcuredStockSupplier> result = instance.viewContractedSuppliers(mf);
     assertEquals(expResult, result);
     container.close();
     // TODO review the generated test code and remove the default call to fail.
     fail("The test case is a prototype.");
     }

     @Test
     public void testViewPlannedPurchaseOrders() throws Exception {
     System.out.println("viewPlannedPurchaseOrders");
     Plant staffPlant = null;
     EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
     ManagePurchaseOrderLocal instance = (ManagePurchaseOrderLocal)container.getContext().lookup("java:global/classes/ManagePurchaseOrder");
     List<ProcuredStockPurchaseOrder> expResult = null;
     List<ProcuredStockPurchaseOrder> result = instance.viewPlannedPurchaseOrders(staffPlant);
     assertEquals(expResult, result);
     container.close();
     // TODO review the generated test code and remove the default call to fail.
     fail("The test case is a prototype.");
     }

     @Test
     public void testViewConfirmedPurchaseOrders() throws Exception {
     System.out.println("viewConfirmedPurchaseOrders");
     Plant staffPlant = null;
     EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
     ManagePurchaseOrderLocal instance = (ManagePurchaseOrderLocal)container.getContext().lookup("java:global/classes/ManagePurchaseOrder");
     List<ProcuredStockPurchaseOrder> expResult = null;
     List<ProcuredStockPurchaseOrder> result = instance.viewConfirmedPurchaseOrders(staffPlant);
     assertEquals(expResult, result);
     container.close();
     // TODO review the generated test code and remove the default call to fail.
     fail("The test case is a prototype.");
     }

     @Test
     public void testViewDeliveredPurchaseOrders() throws Exception {
     System.out.println("viewDeliveredPurchaseOrders");
     Plant staffPlant = null;
     EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
     ManagePurchaseOrderLocal instance = (ManagePurchaseOrderLocal)container.getContext().lookup("java:global/classes/ManagePurchaseOrder");
     List<ProcuredStockPurchaseOrder> expResult = null;
     List<ProcuredStockPurchaseOrder> result = instance.viewDeliveredPurchaseOrders(staffPlant);
     assertEquals(expResult, result);
     container.close();
     // TODO review the generated test code and remove the default call to fail.
     fail("The test case is a prototype.");
     }

     @Test
     public void testViewPaidPurchaseOrders() throws Exception {
     System.out.println("viewPaidPurchaseOrders");
     Plant staffPlant = null;
     EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
     ManagePurchaseOrderLocal instance = (ManagePurchaseOrderLocal)container.getContext().lookup("java:global/classes/ManagePurchaseOrder");
     List<ProcuredStockPurchaseOrder> expResult = null;
     List<ProcuredStockPurchaseOrder> result = instance.viewPaidPurchaseOrders(staffPlant);
     assertEquals(expResult, result);
     container.close();
     // TODO review the generated test code and remove the default call to fail.
     fail("The test case is a prototype.");
     }*/
    private ManagePurchaseOrderRemote lookupManagePurchaseOrderRemote() {
        try {
            Context c = new InitialContext();
            return (ManagePurchaseOrderRemote) c.lookup("java:global/IslandFurniture/IslandFurniture-ejb/ManagePurchaseOrder!IslandFurniture.EJB.Purchasing.ManagePurchaseOrderRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}
