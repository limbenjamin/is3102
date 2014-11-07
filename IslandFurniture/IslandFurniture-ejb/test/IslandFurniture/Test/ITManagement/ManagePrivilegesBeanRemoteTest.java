/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.Test.ITManagement;

import IslandFurniture.EJB.ITManagement.ManagePrivilegesBeanRemote;
import IslandFurniture.Entities.Privilege;
import IslandFurniture.Entities.Url;
import IslandFurniture.Exceptions.NullException;
import java.util.ArrayList;
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

public class ManagePrivilegesBeanRemoteTest {
    
    ManagePrivilegesBeanRemote managePrivilegesBeanRemote = lookupManagePrivilegesBeanRemote();
    
    static Url url;
    static Url url2;
    static Url url3;
    static Privilege priv;
    static Privilege priv2;
    
    public ManagePrivilegesBeanRemoteTest() {
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
    public void test01CreateUrl() {
        System.out.println("createUrl");
        url = managePrivilegesBeanRemote.createUrl("/test.xhmtml", "", "", Boolean.FALSE, 0);
        assertNotNull(url);
        System.out.println("Case 2");
        url2 = managePrivilegesBeanRemote.createUrl("/test2.xhmtml", "fa-icon", "menu", Boolean.TRUE, 5);
        assertNotNull(url2);
        System.out.println("Case 3");
        url3 = managePrivilegesBeanRemote.createUrl(null, "fa-icon", "menu", Boolean.TRUE, 5);
        assertNull(url3);
    }
    
    @Test
    public void test02CreatePrivilege() {
        System.out.println("createPrivilege");
        List<Url> urlList = new ArrayList();
        urlList.add(url);
        urlList.add(url2);
        try{
            managePrivilegesBeanRemote.createPrivilege("new privilege", "description", urlList);
        }catch(Exception e){
            fail("Exception Thrown");
        }
        System.out.println("Case 2");
        try{
            managePrivilegesBeanRemote.createPrivilege("new privilege2", "description", new ArrayList());
        }catch(Exception e){
            fail("Exception Thrown");
        }
        System.out.println("Case 3");
        try{
            managePrivilegesBeanRemote.createPrivilege(null, "description", urlList);
            fail("Exception Not Thrown");
        }catch(NullException e){
            
        }
        
    }
    
    @Test
    public void test03DisplayPrivilege() {
        System.out.println("displayPrivilege");
        List<Privilege> privList = managePrivilegesBeanRemote.displayPrivilege();
        assertNotNull(privList);
        
    }

    @Test
    public void test04GetPrivilegeFromName() {
        System.out.println("getPrivilegeFromName");
        priv =  managePrivilegesBeanRemote.getPrivilegeFromName("new privilege");
        assertNotNull(priv);
        System.out.println("Case 2");
        priv2 = managePrivilegesBeanRemote.getPrivilegeFromName("new privilege2");
        assertNotNull(priv2);
        System.out.println("Case 3");
        try{
            Privilege result =  managePrivilegesBeanRemote.getPrivilegeFromName("non existent");
            fail("Exception Not Thrown");
        }catch(Exception e){
            
        }
    }

    @Test
    public void test05RemovePrivilege() {
        System.out.println("removePrivilege");
        try{
            managePrivilegesBeanRemote.removePrivilege(priv.getId());
        }catch(Exception e){
            fail("Exception Thrown");
        }
        System.out.println("Case 2");
        try{
            managePrivilegesBeanRemote.removePrivilege(priv2.getId());
        }catch(Exception e){
            fail("Exception Thrown");
        }
        System.out.println("Case 3");
        try{
            managePrivilegesBeanRemote.removePrivilege(Long.parseLong("1"));
            fail("Exception Not Thrown");
        }catch(Exception e){
            
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
