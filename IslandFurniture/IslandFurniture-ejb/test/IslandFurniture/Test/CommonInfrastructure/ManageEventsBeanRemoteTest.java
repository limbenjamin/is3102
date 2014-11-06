/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.Test.CommonInfrastructure;

import IslandFurniture.EJB.CommonInfrastructure.ManageEventsBeanRemote;
import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanRemote;
import IslandFurniture.Entities.Event;
import IslandFurniture.Entities.Staff;
import static IslandFurniture.Test.CommonInfrastructure.ManageAnnouncementsBeanRemoteTest.staff;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
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

public class ManageEventsBeanRemoteTest {
    
    ManageEventsBeanRemote manageEventsBeanRemote = lookupManageEventsBeanRemote();
    
    static Staff staff;
    static Staff staff2;
    static Calendar cal;
    static Long eventId;
    static Long eventId2;
    
    public ManageEventsBeanRemoteTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        ManageUserAccountBeanRemote manageUserAccountBeanRemote = lookupManageUserAccountBeanRemote();
        staff = manageUserAccountBeanRemote.getStaff("rose");
        staff2 = manageUserAccountBeanRemote.getStaff("tommy");
        cal = new GregorianCalendar(2014,0,31);
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
    public void test01AddEvent() {
        System.out.println("addEvent");
        eventId = manageEventsBeanRemote.addEvent("name", "description", cal, staff.getUsername());
        assertNotNull(eventId);
        System.out.println("Case 2");
        eventId2 = manageEventsBeanRemote.addEvent("name2", "description2", cal, staff.getUsername());
        assertNotNull(eventId2);
        System.out.println("Case 3");
        Long result;
        try {
            result = manageEventsBeanRemote.addEvent("name2", "description2", cal, "Non existent");
            fail("Exception Not Thrown");
        } catch (Exception ex) {
            
        }
    }

    @Test
    public void test02EditEvent() {
        System.out.println("editEvent");
        try {
            manageEventsBeanRemote.editEvent("name1", "description1", cal, eventId);            
        } catch (Exception ex) {
            fail("Exception Thrown");
        }
        System.out.println("Case 2");
        try {
            manageEventsBeanRemote.editEvent("name3", "description3", cal, eventId2);            
        } catch (Exception ex) {
            fail("Exception Thrown");
        }
        System.out.println("Case 3");
        try {
            manageEventsBeanRemote.editEvent("name3", "description3", cal, Long.parseLong("1"));
            fail("Exception Not Thrown");
        } catch (Exception ex) {
            
        }
    }
    
    @Test
    public void test03GetMyEvents() {
        System.out.println("getMyEvents");
        List<Event> result = manageEventsBeanRemote.getMyEvents(staff.getUsername());
        assertNotNull(result);
        System.out.println("Case 2");
        result = manageEventsBeanRemote.getMyEvents(staff2.getUsername());
        assertNotNull(result);
        System.out.println("Case 3");
        try {
            manageEventsBeanRemote.getMyEvents(null);
            fail("Exception Not Thrown");
        } catch (Exception ex) {
            
        }
    }

    @Test
    public void test04GetEvents() {
        System.out.println("getEvents");
        List<Event> result = manageEventsBeanRemote.getEvents(staff.getUsername());
        assertNotNull(result);
        System.out.println("Case 2");
        result = manageEventsBeanRemote.getEvents(staff2.getUsername());
        assertNotNull(result);
        System.out.println("Case 3");
        try {
            manageEventsBeanRemote.getEvents(null);
            fail("Exception Not Thrown");
        } catch (Exception ex) {
            
        }
    }

    @Test
    public void test05DeleteEvent() {
        System.out.println("deleteEvent");
        try {
            manageEventsBeanRemote.deleteEvent(eventId);
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Exception Thrown");
        }
        System.out.println("Case 2");
        try {
            manageEventsBeanRemote.deleteEvent(Long.parseLong("1"));
            fail("Exception Not Thrown");
        } catch (Exception ex) {
            
        }
        System.out.println("Case 3");
        try {
            manageEventsBeanRemote.deleteEvent(null);
            fail("Exception Not Thrown");
        } catch (Exception ex) {
            
        }
    }

    private static ManageEventsBeanRemote lookupManageEventsBeanRemote() {
        try {
            Context c = new InitialContext();
            return (ManageEventsBeanRemote) c.lookup("java:global/IslandFurniture/IslandFurniture-ejb/ManageEventsBean!IslandFurniture.EJB.CommonInfrastructure.ManageEventsBeanRemote");
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
