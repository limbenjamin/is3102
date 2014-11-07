/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.Test.CommonInfrastructure;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanRemote;
import IslandFurniture.Entities.Staff;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

/**
 *
 * @author Benjamin
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class ManageUserAccountBeanRemoteTest {
    
    ManageUserAccountBeanRemote manageUserAccountBeanRemote = lookupManageUserAccountBeanRemote();
    
    static Staff staff;
    
    public ManageUserAccountBeanRemoteTest() {
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
    public void test01GetStaff() {
        staff = manageUserAccountBeanRemote.getStaff("rose");
        assertNotNull(staff);
        Staff result;
        try{
            result = manageUserAccountBeanRemote.getStaff("non existent");
            fail("Exception Not Thrown");
        }catch(Exception e){

        }
        try{
            result = manageUserAccountBeanRemote.getStaff(null);
            fail("Exception Not Thrown");
        }catch(Exception e){

        }
    }

    @Test
    public void test02GetStaffFromCardId() {
        staff = manageUserAccountBeanRemote.getStaffFromCardId(staff.getCardId());
        assertNotNull(staff);
        Staff result;
        try{
            result = manageUserAccountBeanRemote.getStaffFromCardId("1");
        }catch(Exception e){
            fail("Exception Thrown");
        }
        try{
            result = manageUserAccountBeanRemote.getStaffFromCardId(null);
        }catch(Exception e){
            fail("Exception Thrown");
        }
    }

    @Test
    public void test03GetStaffFromId() {
        staff = manageUserAccountBeanRemote.getStaffFromId(staff.getId());
        assertNotNull(staff);
        Staff result;
        try{
            result = manageUserAccountBeanRemote.getStaffFromId(Long.parseLong("1"));
        }catch(Exception e){
            fail("Exception Thrown");
        }
        try{
            result = manageUserAccountBeanRemote.getStaffFromId(null);
            fail("Exception Not Thrown");
        }catch(Exception e){

        }
    }

    @Test
    public void test04ModifyNote() {
        try{
            manageUserAccountBeanRemote.modifyNote("rose", "Notes");
        }catch(Exception e){
            fail("Exception Thrown");
        }
        try{
            manageUserAccountBeanRemote.modifyNote("non existent", "Notes");
            fail("Exception Not Thrown");
        }catch(Exception e){
        }
        try{
            manageUserAccountBeanRemote.modifyNote(null, "Notes");
            fail("Exception Not Thrown");
        }catch(Exception e){
        }
    }

    @Test
    public void test05ModifyPersonalParticulars() {
        try{
            manageUserAccountBeanRemote.modifyPersonalParticulars("rose", "91234567", "a@a.com");
        }catch(Exception e){
            fail("Exception Thrown");
        }
        try{
            manageUserAccountBeanRemote.modifyPersonalParticulars("non existent", "91234567", "a@a.com");
            fail("Exception Not Thrown");
        }catch(Exception e){
            
        }
        try{
            manageUserAccountBeanRemote.modifyPersonalParticulars(null, "91234567", "a@a.com");
            fail("Exception Not Thrown");
        }catch(Exception e){
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
