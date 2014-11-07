/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.Test.ITManagement;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanRemote;
import IslandFurniture.EJB.ITManagement.ManageOrganizationalHierarchyBeanRemote;
import IslandFurniture.EJB.ITManagement.ManageStaffAccountsBeanRemote;
import IslandFurniture.EJB.ITManagement.ManageSystemAuditLogBeanRemote;
import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.LogEntry;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Staff;
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

public class ManageSystemAuditLogBeanRemoteTest {
    
    ManageSystemAuditLogBeanRemote manageSystemAuditLogBeanRemote = lookupManageSystemAuditLogBeanRemote();
    
    static Staff staff;
    static CountryOffice co;
    
    public ManageSystemAuditLogBeanRemoteTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        ManageStaffAccountsBeanRemote manageStaffAccountsBeanRemote = lookupManageStaffAccountsBeanRemote();
        ManageOrganizationalHierarchyBeanRemote manageOrganizationalHierarchyBeanRemote = lookupManageOrganizationalHierarchyBeanRemote();
        ManageUserAccountBeanRemote manageUserAccountBeanRemote = lookupManageUserAccountBeanRemote();
        co = manageOrganizationalHierarchyBeanRemote.displayCountryOffice().get(0);
        staff = manageUserAccountBeanRemote.getStaff("rose");
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
    public void testLog() {
        System.out.println("log");   
        try{
            manageSystemAuditLogBeanRemote.log("entity", Long.parseLong("1"), "ACCESS", "change", staff.getUsername());
        }catch(Exception e){
            fail("Exception Thrown");
        }
        System.out.println("Case 2");
        try{
            manageSystemAuditLogBeanRemote.log("entity", Long.parseLong("1"), "ACCESS", "change", "Non existent");
            fail("Exception Not Thrown");
        }catch(Exception e){
            
        }
        System.out.println("Case 3");
        try{
            manageSystemAuditLogBeanRemote.log("entity", Long.parseLong("1"), "MODIFY", "change", staff.getUsername());
        }catch(Exception e){
            fail("Exception Thrown");
        }
    }

    @Test
    public void testGetLog() {
        System.out.println("getLog");
        List<LogEntry> result = manageSystemAuditLogBeanRemote.getLog();
        assertNotNull(result);
    }

    @Test
    public void testGetLogForPlant() {
        System.out.println("GetLogForPlant");
        List<LogEntry> result = manageSystemAuditLogBeanRemote.GetLogForPlant(staff.getPlant());
        assertNotNull(result);
        System.out.println("Case 2");
        result = manageSystemAuditLogBeanRemote.GetLogForPlant(co);
        assertNotNull(result);
        System.out.println("Case 3");
        result = manageSystemAuditLogBeanRemote.GetLogForPlant(null);
        assertNotNull(result);
    }

    private static ManageStaffAccountsBeanRemote lookupManageStaffAccountsBeanRemote() {
        try {
            Context c = new InitialContext();
            return (ManageStaffAccountsBeanRemote) c.lookup("java:global/IslandFurniture/IslandFurniture-ejb/ManageStaffAccountsBean!IslandFurniture.EJB.ITManagement.ManageStaffAccountsBeanRemote");
        } catch (NamingException ne) {
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
    
    private static ManageSystemAuditLogBeanRemote lookupManageSystemAuditLogBeanRemote() {
        try {
            Context c = new InitialContext();
            return (ManageSystemAuditLogBeanRemote) c.lookup("java:global/IslandFurniture/IslandFurniture-ejb/ManageSystemAuditLogBean!IslandFurniture.EJB.ITManagement.ManageSystemAuditLogBeanRemote");
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }
    
    private static ManageUserAccountBeanRemote lookupManageUserAccountBeanRemote() {
        try {
            Context c = new InitialContext();
            return (ManageUserAccountBeanRemote) c.lookup("java:global/IslandFurniture/IslandFurniture-ejb/ManageUserAccountBean!IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanRemote");
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }
}
