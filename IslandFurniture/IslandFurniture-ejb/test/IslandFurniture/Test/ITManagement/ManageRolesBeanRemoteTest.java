/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.Test.ITManagement;

import IslandFurniture.EJB.ITManagement.ManagePrivilegesBeanRemote;
import IslandFurniture.EJB.ITManagement.ManageRolesBeanRemote;
import IslandFurniture.Entities.Privilege;
import IslandFurniture.Entities.Role;
import IslandFurniture.Exceptions.DuplicateEntryException;
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

public class ManageRolesBeanRemoteTest {
    
    ManageRolesBeanRemote manageRolesBeanRemote = lookupManageRolesBeanRemote();
    
    static Privilege priv;
    static Privilege priv2;
    static Role role;
    static Role role2;
    
    public ManageRolesBeanRemoteTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        ManagePrivilegesBeanRemote managePrivilegesBeanRemote = lookupManagePrivilegesBeanRemote();
        priv = managePrivilegesBeanRemote.displayPrivilege().get(0);
        priv2 = managePrivilegesBeanRemote.displayPrivilege().get(1);
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
    public void test01CreateRole() {
        System.out.println("createRole");
        try{
            manageRolesBeanRemote.createRole("new role");
        }catch(Exception e){
            fail("Exception Thrown");
        }
        System.out.println("Case 2");
        try{
            manageRolesBeanRemote.createRole("new role2");
        }catch(Exception e){
            fail("Exception Thrown");
        }
        System.out.println("Case 3");
        try{
            manageRolesBeanRemote.createRole("new role");
            fail("Exception Not Thrown");
        }catch(DuplicateEntryException e){
            
        }
    }
    
    @Test
    public void test02GetRoleFromName() {
        System.out.println("getRoleFromName");
        role = manageRolesBeanRemote.getRoleFromName("new role");
        assertNotNull(role);
        System.out.println("Case 2");
        role2 = manageRolesBeanRemote.getRoleFromName("new role2");
        assertNotNull(role2);
        System.out.println("Case 3");
        try{
            Role result = manageRolesBeanRemote.getRoleFromName("non existent");
            fail("Exception Not Thrown");
        }catch(Exception e){
            
        }
    }

    @Test
    public void test03DisplayRole() {
        System.out.println("displayRole");
        try{
            manageRolesBeanRemote.displayRole();
        }catch(Exception e){
            fail("Exception Thrown");
        }
    }
    
    @Test
    public void test04GetRole() {
        System.out.println("getRole");
        Role result = manageRolesBeanRemote.getRole(role.getId());
        assertEquals(result,role);
        System.out.println("Case 2");
        result = manageRolesBeanRemote.getRole(role2.getId());
        assertEquals(result,role2);
        System.out.println("Case 3");
        result = manageRolesBeanRemote.getRole(Long.parseLong("1"));
        assertNull(result);

    }
    
    @Test
    public void test05AddPrivilegeToRole() {
        System.out.println("addPrivilegeToRole");
        try{
            manageRolesBeanRemote.addPrivilegeToRole(role.getId(), priv.getName());
        }catch(Exception e){
            fail("Exception Thrown");
        }
        System.out.println("Case 2");
        try{
            manageRolesBeanRemote.addPrivilegeToRole(Long.parseLong("1"), priv.getName());
            fail("Exception Not Thrown");
        }catch(Exception e){
            
        }
        System.out.println("Case 3");
        try{
            manageRolesBeanRemote.addPrivilegeToRole(role.getId(), "non existent");
            fail("Exception Not Thrown");
        }catch(Exception e){
            
        }
    }

    @Test
    public void test06GetPrivileges() {
        System.out.println("getPrivileges");
        List<Privilege> result = manageRolesBeanRemote.getPrivileges(role.getId());
        assertNotNull(result);
        System.out.println("Case 2");
        result = manageRolesBeanRemote.getPrivileges(role2.getId());
        assertNotNull(result);
        System.out.println("Case 3");
        try{
            manageRolesBeanRemote.getPrivileges(Long.parseLong("1"));
            fail("Exception Not Thrown");
        }catch(Exception e){
            
        }
        
    }

    @Test
    public void test07RemovePrivilegeFromRole() {
        System.out.println("removePrivilegeFromRole");
        try{
            manageRolesBeanRemote.removePrivilegeFromRole(role.getId(), priv.getId());
        }catch(Exception e){
            fail("Exception Thrown");
        }
        System.out.println("Case 2");
        try{
            manageRolesBeanRemote.removePrivilegeFromRole(Long.parseLong("1"), priv.getId());
            fail("Exception Not Thrown");
        }catch(Exception e){
        }
        System.out.println("Case 3");
        try{
            manageRolesBeanRemote.removePrivilegeFromRole(role.getId(), Long.parseLong("1"));
            fail("Exception Not Thrown");
        }catch(Exception e){
        }
    }

    @Test
    public void test08RemoveRole() {
        System.out.println("removeRole");
        try{
            manageRolesBeanRemote.removeRole(role.getId());
        }catch(Exception e){
            fail("Exception Thrown");
        }
        System.out.println("Case 2");
        try{
            manageRolesBeanRemote.removeRole(role2.getId());
        }catch(Exception e){
            fail("Exception Thrown");
        }
        System.out.println("Case 3");
        try{
            manageRolesBeanRemote.removeRole(Long.parseLong("1"));
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
    
}
