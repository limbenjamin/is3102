/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.Test.CommonInfrastructure;

import IslandFurniture.EJB.CommonInfrastructure.ManageNotificationsBeanRemote;
import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanRemote;
import IslandFurniture.EJB.ITManagement.ManageOrganizationalHierarchyBeanRemote;
import IslandFurniture.EJB.ITManagement.ManagePrivilegesBeanRemote;
import IslandFurniture.Entities.Notification;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Privilege;
import IslandFurniture.Entities.Staff;
import IslandFurniture.Entities.Store;
import static IslandFurniture.Test.CommonInfrastructure.ManageAuthenticationBeanRemoteTest.staff;
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

public class ManageNotificationsBeanRemoteTest {
    
    ManageNotificationsBeanRemote manageNotificationsBeanRemote = lookupManageNotificationsBeanRemote();
    
    static Staff staff;
    static Privilege privilege;
    static Store store;
    static Notification notification;
    static Notification notification2;
    
    public ManageNotificationsBeanRemoteTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        ManageUserAccountBeanRemote manageUserAccountBeanRemote = lookupManageUserAccountBeanRemote();
        ManagePrivilegesBeanRemote managePrivilegesBeanRemote = lookupManagePrivilegesBeanRemote();
        ManageOrganizationalHierarchyBeanRemote manageOrganizationalHierarchyBeanRemote = lookupManageOrganizationalHierarchyBeanRemote();
        staff = manageUserAccountBeanRemote.getStaff("rose");
        privilege = managePrivilegesBeanRemote.getPrivilegeFromName("View Audit Log");
        store = manageOrganizationalHierarchyBeanRemote.displayStore().get(0);
        
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
    public void test01CreateNewNotificationForStaff() {
        try{
            manageNotificationsBeanRemote.createNewNotificationForStaff("title", "content", "link.xhtml", "linkText", staff);        
        }catch(Exception e){
            fail("Exception Thrown");
        }
        try{
            manageNotificationsBeanRemote.createNewNotificationForStaff("title", "content", "link.xhtml", "linkText", null);
            fail("Exception Not Thrown");
        }catch(Exception e){            
        }
        try{
            manageNotificationsBeanRemote.createNewNotificationForStaff("title2", "content2", "link.xhtml", "linkText", staff);        
        }catch(Exception e){
            fail("Exception Thrown");
        }
        
    }

    @Test
    public void test02CreateNewNotificationForPrivilege() {
        try{
            manageNotificationsBeanRemote.createNewNotificationForPrivilege("title", "content", "link.xhtml", "linkText", privilege);        
        }catch(Exception e){
            fail("Exception Thrown");
        }
        try{
            manageNotificationsBeanRemote.createNewNotificationForPrivilege("title", "content", "link.xhtml", "linkText", null);
            fail("Exception Not Thrown");
        }catch(Exception e){            
        }
        try{
            manageNotificationsBeanRemote.createNewNotificationForPrivilege("title2", "content2", "link.xhtml", "linkText", privilege);
            fail("Exception Not Thrown");
        }catch(Exception e){
            
        }
    }

    @Test
    public void test03CreateNewNotificationForPrivilegeFromPlant() {
        try{
            manageNotificationsBeanRemote.createNewNotificationForPrivilegeFromPlant("title", "content", "link.xhtml", "linkText", privilege, store);        
        }catch(Exception e){
            fail("Exception Thrown");
        }
        try{
            manageNotificationsBeanRemote.createNewNotificationForPrivilegeFromPlant("title", "content", "link.xhtml", "linkText", null, store);
            fail("Exception Not Thrown");
        }catch(Exception e){            
        }
        try{
            manageNotificationsBeanRemote.createNewNotificationForPrivilegeFromPlant("title2", "content2", "link.xhtml", "linkText", privilege, null);
            fail("Exception Not Thrown");
        }catch(Exception e){
            
        }
    }
    
    @Test
    public void test04GetAllNotifications() {
        List<Notification> result = manageNotificationsBeanRemote.getAllNotifications();
        assertNotNull(result);
        notification = result.get(0);
        notification2 = result.get(1);
    }
    
    @Test
    public void test05GetNotification() {
        try{
            Notification result = manageNotificationsBeanRemote.getNotification(notification.getId());        
        }catch(Exception e){
            fail("Exception Thrown");
        }
        try{
            Notification result = manageNotificationsBeanRemote.getNotification(Long.parseLong("1"));
        }catch(Exception e){
            fail("Exception Thrown");
        }
        try{
            Notification result = manageNotificationsBeanRemote.getNotification(null);
            fail("Exception Not Thrown");
        }catch(Exception e){
            
        }
    }

    @Test
    public void test06GetUnreadForStaff() {
        Integer result = manageNotificationsBeanRemote.getUnreadForStaff(staff);
        assertNotNull(result);
        try{
            result = manageNotificationsBeanRemote.getUnreadForStaff(null);
        }catch(Exception e){
            fail("Exception Thrown");
        }
    }
    
    @Test
    public void test07DisplayNotificationForStaff() {
        List<Notification> result = manageNotificationsBeanRemote.displayNotificationForStaff(staff);
        assertNotNull(result);
        try{
            result = manageNotificationsBeanRemote.displayNotificationForStaff(null);
            fail("Exception Not Thrown");
        }catch(Exception e){
            
        }
    }

    @Test
    public void test08SetNotificationToRead() {
        try{
            manageNotificationsBeanRemote.setNotificationToRead(notification);
        }catch(Exception e){
            fail("Exception Thrown");
        }
        try{
            manageNotificationsBeanRemote.setNotificationToRead(notification2);
        }catch(Exception e){
            fail("Exception Thrown");
        }
        try{
            manageNotificationsBeanRemote.setNotificationToRead(null);
            fail("Exception Not Thrown");
        }catch(Exception e){
            
        }
    }

    @Test
    public void test09DeleteNotification() {
        try{
            manageNotificationsBeanRemote.deleteNotification(notification.getId());
        }catch(Exception e){
            fail("Exception Thrown");
        }
        try{
            manageNotificationsBeanRemote.deleteNotification(notification2.getId());
        }catch(Exception e){
            fail("Exception Thrown");
        }
        try{
            manageNotificationsBeanRemote.deleteNotification(null);
            fail("Exception Not Thrown");
        }catch(Exception e){
            
        }
    }


    private static ManageNotificationsBeanRemote lookupManageNotificationsBeanRemote() {
        try {
            Context c = new InitialContext();
            return (ManageNotificationsBeanRemote) c.lookup("java:global/IslandFurniture/IslandFurniture-ejb/ManageNotificationsBean!IslandFurniture.EJB.CommonInfrastructure.ManageNotificationsBeanRemote");
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
    
    private static ManagePrivilegesBeanRemote lookupManagePrivilegesBeanRemote() {
        try {
            Context c = new InitialContext();
            return (ManagePrivilegesBeanRemote) c.lookup("java:global/IslandFurniture/IslandFurniture-ejb/ManagePrivilegesBean!IslandFurniture.EJB.ITManagement.ManagePrivilegesBeanRemote");
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
    
    
}
