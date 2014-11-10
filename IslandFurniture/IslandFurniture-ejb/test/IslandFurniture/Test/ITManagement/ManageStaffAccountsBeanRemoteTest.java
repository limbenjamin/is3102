/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.Test.ITManagement;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanRemote;
import IslandFurniture.EJB.ITManagement.ManageOrganizationalHierarchyBeanRemote;
import IslandFurniture.EJB.ITManagement.ManagePrivilegesBeanRemote;
import IslandFurniture.EJB.ITManagement.ManageRolesBeanRemote;
import IslandFurniture.EJB.ITManagement.ManageStaffAccountsBeanRemote;
import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.Privilege;
import IslandFurniture.Entities.Role;
import IslandFurniture.Entities.Staff;
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

public class ManageStaffAccountsBeanRemoteTest {
    
    ManageStaffAccountsBeanRemote manageStaffAccountsBeanRemote = lookupManageStaffAccountsBeanRemote();
    ManageOrganizationalHierarchyBeanRemote manageOrganizationalHierarchyBeanRemote = lookupManageOrganizationalHierarchyBeanRemote();
    ManageUserAccountBeanRemote manageUserAccountBeanRemote = lookupManageUserAccountBeanRemote();
        
    static Privilege priv;
    static Privilege priv2;
    static Role role;
    static Store store;
    static CountryOffice co;
    static Staff staff;
    
    public ManageStaffAccountsBeanRemoteTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        ManageOrganizationalHierarchyBeanRemote staticManageOrganizationalHierarchyBeanRemote = lookupManageOrganizationalHierarchyBeanRemote();
        ManagePrivilegesBeanRemote managePrivilegesBeanRemote = lookupManagePrivilegesBeanRemote();
        ManageRolesBeanRemote manageRolesBeanRemote = lookupManageRolesBeanRemote();
        priv = managePrivilegesBeanRemote.displayPrivilege().get(0);
        priv2 = managePrivilegesBeanRemote.displayPrivilege().get(1);
        role = manageRolesBeanRemote.displayRole().get(0);
        //System.err.println("herehere "+ role.getName()+"  "+priv.getName());
        try{
            manageRolesBeanRemote.addPrivilegeToRole(role.getId(), priv.getName());
        }catch(Exception e){
            
        }
        store = staticManageOrganizationalHierarchyBeanRemote.displayStore().get(0);
        co = staticManageOrganizationalHierarchyBeanRemote.displayCountryOffice().get(0);
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
    public void test01CreateStaffAccountinBulk() {
        System.out.println("createStaffAccountinBulk");       
        try{
            manageStaffAccountsBeanRemote.createStaffAccountinBulk("user", "pass", "name", "a@a.com", "9123456", store.getCountry().getName(), store.getName(), "123456");
        }catch(Exception e){
            fail("Exception Thrown");
        }
        System.out.println("Case 2");
        try{
            manageStaffAccountsBeanRemote.createStaffAccountinBulk("user", "pass", "name", "a@a.com", "9123456", "Non existent", store.getName(), "123456");
            fail("Exception Not Thrown");
        }catch(Exception e){
            
        }
        System.out.println("Case 3");
        try{
            manageStaffAccountsBeanRemote.createStaffAccountinBulk("user", "pass", "name", "a@a.com", "9123456", store.getCountry().getName(), "Non existent", "123456");
            fail("Exception Not Thrown");
        }catch(Exception e){
            
        }
    }

    @Test
    public void test02DisplayAllStaffAccounts() {
        System.out.println("displayAllStaffAccounts");
        List<Staff> result = manageStaffAccountsBeanRemote.displayAllStaffAccounts();
        assertNotNull(result);
    }
    
    @Test
    public void test03AddRoleToStaffByUsername() {
        System.out.println("addRoleToStaffByUsername");
        try{
            manageStaffAccountsBeanRemote.addRoleToStaffByUsername("user", role.getName());
        }catch(Exception e){
            fail("Exception Thrown");
        }
        System.out.println("Case 2");
        try{
            manageStaffAccountsBeanRemote.addRoleToStaffByUsername("non existent", role.getName());
            fail("Exception Not Thrown");
        }catch(Exception e){
            
        }
        System.out.println("Case 3");
        try{
            manageStaffAccountsBeanRemote.addRoleToStaffByUsername("user", "non existent");
            fail("Exception Not Thrown");
        }catch(Exception e){
            
        }
    }

    @Test
    public void test04RemoveRoleFromStaff() {
        System.out.println("removeRoleFromStaff");
        staff = manageUserAccountBeanRemote.getStaff("user");
        try{
            manageStaffAccountsBeanRemote.removeRoleFromStaff(staff.getId(), role.getId());
        }catch(Exception e){
            fail("Exception Thrown");
        }
        System.out.println("Case 2");
        try{
            manageStaffAccountsBeanRemote.removeRoleFromStaff(Long.parseLong("1"), role.getId());
            fail("Exception Not Thrown");
        }catch(Exception e){
            
        }
        System.out.println("Case 3");
        try{
            manageStaffAccountsBeanRemote.removeRoleFromStaff(staff.getId(), Long.parseLong("1"));
            fail("Exception Not Thrown");
        }catch(Exception e){

        }
    }
    
    @Test
    public void test05AddRoleToStaff() {
        System.out.println("addRoleToStaff");
        staff = manageUserAccountBeanRemote.getStaff("user");
        try{
            manageStaffAccountsBeanRemote.addRoleToStaff(staff.getId(), role.getName());
        }catch(Exception e){
            fail("Exception Thrown");
        }
        System.out.println("Case 2");
        try{
            manageStaffAccountsBeanRemote.addRoleToStaff(Long.parseLong("1"), role.getName());
            fail("Exception Not Thrown");
        }catch(Exception e){
            
        }
        System.out.println("Case 3");
        try{
            manageStaffAccountsBeanRemote.addRoleToStaff(staff.getId(), "non existent");
            fail("Exception Not Thrown");
        }catch(Exception e){
            
        }
    }
    
    @Test
    public void test06CheckIfStaffHasPrivilege() {
        System.out.println("checkIfStaffHasPrivilege");
        Boolean result = manageStaffAccountsBeanRemote.checkIfStaffHasPrivilege(staff.getUsername(), priv);
        assertTrue(result);
        System.out.println("Case 2");
        try{
            manageStaffAccountsBeanRemote.checkIfStaffHasPrivilege("non existent", priv);
            fail("Exception Not Thrown");
        }catch(Exception e){
            
        }
        System.out.println("Case 3");
        result = manageStaffAccountsBeanRemote.checkIfStaffHasPrivilege(staff.getUsername(), priv2);
        assertFalse(result);
    }
    
    @Test
    public void test07GetPrivilegeListforStaff() {
        System.out.println("getPrivilegeListforStaff");
        List<Privilege> result = manageStaffAccountsBeanRemote.getPrivilegeListforStaff(staff.getUsername());
        assertNotNull(result);
        System.out.println("Case 2");
        try{
            manageStaffAccountsBeanRemote.getPrivilegeListforStaff("non existent");
            fail("Exception Not Thrown");
        }catch(Exception e){
            
        }
        System.out.println("Case 3");
        try{
            manageStaffAccountsBeanRemote.getPrivilegeListforStaff(null);
            fail("Exception Not Thrown");
        }catch(Exception e){
            
        }

    }    
    
    
    @Test
    public void test08DeleteStaffAccount() {
        System.out.println("deleteStaffAccount");
        try{
            manageStaffAccountsBeanRemote.deleteStaffAccount(staff.getId());
        }catch(Exception e){
            fail("Exception Thrown");
        }
        System.out.println("Case 2");
        try{
            manageStaffAccountsBeanRemote.deleteStaffAccount(Long.parseLong("1"));
            fail("Exception Not Thrown");
        }catch(Exception e){
            
        }
        System.out.println("Case 3");
        try{
            manageStaffAccountsBeanRemote.deleteStaffAccount(null);
            fail("Exception Not Thrown");
        }catch(Exception e){
            
        }
    }




    private static ManageRolesBeanRemote lookupManageRolesBeanRemote() {
        try {
            Context c = new InitialContext();
            return (ManageRolesBeanRemote) c.lookup("java:global/IslandFurniture/IslandFurniture-ejb/ManageRolesBean!IslandFurniture.EJB.ITManagement.ManageRolesBeanRemote");
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
    
    private static ManageUserAccountBeanRemote lookupManageUserAccountBeanRemote() {
        try {
            Context c = new InitialContext();
            return (ManageUserAccountBeanRemote) c.lookup("java:global/IslandFurniture/IslandFurniture-ejb/ManageUserAccountBean!IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanRemote");
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }
    
}
