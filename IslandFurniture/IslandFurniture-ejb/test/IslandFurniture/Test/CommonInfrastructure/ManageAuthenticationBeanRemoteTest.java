/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.Test.CommonInfrastructure;

import IslandFurniture.EJB.CommonInfrastructure.ManageAuthenticationBeanRemote;
import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanRemote;
import IslandFurniture.Entities.Staff;
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

public class ManageAuthenticationBeanRemoteTest {
    
    ManageAuthenticationBeanRemote manageAuthenticationBeanRemote = lookupManageAuthenticationBeanRemote();
    ManageUserAccountBeanRemote ManageUserAccountBeanRemote = lookupManageUserAccountBeanRemote();
    
    static Staff staff;
    
    public ManageAuthenticationBeanRemoteTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        ManageUserAccountBeanRemote staticManageUserAccountBeanRemote = lookupManageUserAccountBeanRemote();
        staff = staticManageUserAccountBeanRemote.getStaff("rose");
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
    public void test01Authenticate() {
        Boolean result = manageAuthenticationBeanRemote.authenticate("deanna", "pass");
        assertTrue(result);
        System.out.println("Case 2");
        result = manageAuthenticationBeanRemote.authenticate("Non existent", "pass");
        assertFalse(result);
        System.out.println("Case 3");
        result = manageAuthenticationBeanRemote.authenticate("rose", "pass1");
        assertFalse(result);
    }

    @Test
    public void test02ChangePassword() {
        try{
            manageAuthenticationBeanRemote.changePassword("rose", "newpass");
        }catch(Exception e){
            fail("Exception Thrown");
        }
        System.out.println("Case 2");
        try{
            manageAuthenticationBeanRemote.changePassword("non existent", "newpass");
            fail("Exception Not Thrown");
        }catch(Exception e){
            
        }
        System.out.println("Case 3");
        try{
            manageAuthenticationBeanRemote.changePassword("rose", "pass");
            fail("Exception Not Thrown");
        }catch(Exception e){
            
        }
    }

    @Test
    public void test03ForgetPassword() {
        Boolean result = manageAuthenticationBeanRemote.forgetPassword("rose");
        assertTrue(result);
        System.out.println("Case 2");
        result = manageAuthenticationBeanRemote.forgetPassword("Non existent");
        assertFalse(result);
        System.out.println("Case 3");
        result = manageAuthenticationBeanRemote.forgetPassword("rose"); 
        assertTrue(result);
    }
    
    @Test
    public void test04ResetPassword() {
        staff = ManageUserAccountBeanRemote.getStaff("rose");
        Boolean result = manageAuthenticationBeanRemote.resetPassword(staff.getForgottenPasswordCode(), "newpass");
        assertTrue(result);
        System.out.println("Case 2");
        result =  manageAuthenticationBeanRemote.resetPassword(staff.getForgottenPasswordCode(), "newpass");
        assertFalse(result);
        System.out.println("Case 3");
        try{
            manageAuthenticationBeanRemote.resetPassword("1", "newpass");
        }catch(Exception e){
            fail("Exception Thrown");
        }
        
    }

    @Test
    public void test05ResetPasswordByAdmin() {
        try{
            manageAuthenticationBeanRemote.resetPasswordByAdmin(staff.getId());        
        }catch(Exception e){
            fail("Exception Thrown");
        }
        System.out.println("Case 2");
        try{
            manageAuthenticationBeanRemote.resetPasswordByAdmin(Long.parseLong("1"));
            fail("Exception Not Thrown");
        }catch(Exception e){
            
        }
        System.out.println("Case 3");
        try{
            manageAuthenticationBeanRemote.resetPasswordByAdmin(null);
            fail("Exception Not Thrown");
        }catch(Exception e){
            
        }
    }
    
    

    private static ManageAuthenticationBeanRemote lookupManageAuthenticationBeanRemote() {
        try {
            Context c = new InitialContext();
            return (ManageAuthenticationBeanRemote) c.lookup("java:global/IslandFurniture/IslandFurniture-ejb/ManageAuthenticationBean!IslandFurniture.EJB.CommonInfrastructure.ManageAuthenticationBeanRemote");
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
