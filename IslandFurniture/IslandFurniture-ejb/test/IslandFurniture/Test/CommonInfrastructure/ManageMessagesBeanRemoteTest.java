/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.Test.CommonInfrastructure;

import IslandFurniture.EJB.CommonInfrastructure.ManageMessagesBeanRemote;
import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanRemote;
import IslandFurniture.Entities.MessageThread;
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

public class ManageMessagesBeanRemoteTest {
    
    ManageMessagesBeanRemote manageMessagesBeanRemote = lookupManageMessagesBeanRemote();
    
    static Staff sender;
    static Staff recipient;
    static MessageThread thread;
    
    public ManageMessagesBeanRemoteTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        ManageUserAccountBeanRemote manageUserAccountBeanRemote = lookupManageUserAccountBeanRemote();
        sender = manageUserAccountBeanRemote.getStaff("rose");
        recipient = manageUserAccountBeanRemote.getStaff("deanna");
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
    public void test01CreateNewThread() {      
        try {
            manageMessagesBeanRemote.createNewThread("title", sender.getUsername()+","+recipient.getUsername());
        } catch (Exception ex) {
            fail("Exception Thrown");
        }
        System.out.println("Case 2");
        try {
            manageMessagesBeanRemote.createNewThread("title", sender.getUsername()+","+sender.getUsername());
        } catch (Exception ex) {
            fail("Exception Thrown");
        }
        System.out.println("Case 3");
        try {
            manageMessagesBeanRemote.createNewThread("title", "Non existent");
            fail("Exception Not Thrown");
        } catch (Exception ex) {
            
        }
    }

    @Test
    public void test02DisplayAllThreads() {
        List<MessageThread> result = manageMessagesBeanRemote.displayAllThreads("rose");
        assertNotNull(result);
        thread = result.get(0);
        System.out.println("Case 2");
        result = manageMessagesBeanRemote.displayAllThreads("deanna");
        assertNotNull(result);
        System.out.println("Case 3");
        try {
            manageMessagesBeanRemote.displayAllThreads("Non existent");
            fail("Exception Not Thrown");
        } catch (Exception ex) {
            
        }
    }

    @Test
    public void test03SendMessage() {       
        try {
            manageMessagesBeanRemote.sendMessage(sender.getUsername(), thread.getId(), "content");
        } catch (Exception ex) {
            fail("Exception Thrown");
        }
        System.out.println("Case 2");
        try {
            manageMessagesBeanRemote.sendMessage("non existent", thread.getId(), "content2");
            fail("Exception Not Thrown");
        } catch (Exception ex) {
            
        }
        System.out.println("Case 3");
        try {
            manageMessagesBeanRemote.sendMessage(sender.getUsername(), Long.valueOf("1"), "content2");
            fail("Exception Not Thrown");
        } catch (Exception ex) {
            
        }
    }

    @Test
    public void test04GetMessageThread() {
        MessageThread result = manageMessagesBeanRemote.getMessageThread(thread.getId());
        assertNotNull(result);
        System.out.println("Case 2");
        try {
            manageMessagesBeanRemote.getMessageThread(Long.parseLong("1"));
            fail("Exception Not Thrown");
        } catch (Exception ex) {
            
        }
        System.out.println("Case 3");
        try {
            manageMessagesBeanRemote.getMessageThread(null);
            fail("Exception Not Thrown");
        } catch (Exception ex) {
            
        }
    }


    @Test
    public void test05UnsubscribeFromThread() {
        try {
            manageMessagesBeanRemote.unsubscribeFromThread("rose", thread.getId());
        } catch (Exception ex) {
            fail("Exception Thrown");
        }
        System.out.println("Case 2");
        try {
            manageMessagesBeanRemote.unsubscribeFromThread("non existent", thread.getId());
            fail("Exception Not Thrown");
        } catch (Exception ex) {
            
        }
        System.out.println("Case 3");
        try {
            manageMessagesBeanRemote.unsubscribeFromThread("rose", Long.parseLong("1"));
            fail("Exception Not Thrown");
        } catch (Exception ex) {
            
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
    
    private static ManageMessagesBeanRemote lookupManageMessagesBeanRemote() {
        try {
            Context c = new InitialContext();
            return (ManageMessagesBeanRemote) c.lookup("java:global/IslandFurniture/IslandFurniture-ejb/ManageMessagesBean!IslandFurniture.EJB.CommonInfrastructure.ManageMessagesBeanRemote");
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }
    
}
