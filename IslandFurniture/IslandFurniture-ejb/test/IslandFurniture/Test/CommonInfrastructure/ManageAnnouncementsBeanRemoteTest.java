/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.Test.CommonInfrastructure;

import IslandFurniture.EJB.CommonInfrastructure.ManageAnnouncementsBeanRemote;
import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanRemote;
import IslandFurniture.Entities.Announcement;
import IslandFurniture.Entities.Staff;
import IslandFurniture.Exceptions.InvalidDateException;
import IslandFurniture.Exceptions.NullException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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

public class ManageAnnouncementsBeanRemoteTest {
    
    ManageAnnouncementsBeanRemote manageAnnouncementsBeanRemote = lookupManageAnnouncementsBeanRemote();
    
    static Staff staff;
    static Staff staff2;
    static Calendar cal;
    static Calendar cal2;
    static Announcement announcement;
    
    public ManageAnnouncementsBeanRemoteTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        ManageUserAccountBeanRemote manageUserAccountBeanRemote = lookupManageUserAccountBeanRemote();
        staff = manageUserAccountBeanRemote.getStaff("rose");
        staff2 = manageUserAccountBeanRemote.getStaff("tommy");
        cal = new GregorianCalendar(2013,0,31);
        cal2 = Calendar.getInstance();
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
    public void test01AddAnnouncement() {
        System.out.println("addAnnouncement");
        Long result;
        try {
            result = manageAnnouncementsBeanRemote.addAnnouncement(staff.getUsername(), "title", "content", cal, cal2);
            assertNotNull(result);
        } catch (InvalidDateException ex) {
            ex.printStackTrace();
            fail("Exception Thrown");
        }
        System.out.println("Case 2");
        try {
            result = manageAnnouncementsBeanRemote.addAnnouncement(staff.getUsername(), "title", "content", cal2, cal);
            fail("Exception Not Thrown");
        } catch (InvalidDateException ex) {
            
        }
        System.out.println("Case 3");
        try {
            result = manageAnnouncementsBeanRemote.addAnnouncement("Non Existent", "title", "content", cal, cal2);
            fail("Exception Not Thrown");
        } catch (Exception ex) {
            
        }
        
        
    }

    @Test
    public void test02EditAnnouncement() {
        System.out.println("editAnnouncement");
        announcement = manageAnnouncementsBeanRemote.getMyAnnouncements(staff.getUsername()).get(0);
        try {
            manageAnnouncementsBeanRemote.editAnnouncement(announcement.getId(), "title2", "content", cal, cal2);
        } catch (InvalidDateException ex) {
            fail("Exception Thrown");
        }
        System.out.println("Case 2");
        Long result;
        try {
            manageAnnouncementsBeanRemote.editAnnouncement(Long.parseLong("1"), "title2", "content", cal, cal2);
            fail("Exception Not Thrown");
        } catch (Exception ex) {
            
        }
        System.out.println("Case 3");
        try {
            manageAnnouncementsBeanRemote.editAnnouncement(announcement.getId(), "title2", "content", cal2, cal);
            fail("Exception Not Thrown");
        } catch (Exception ex) {
            
        }
        
    }
    
    @Test
    public void test03GetMyAnnouncements() {
        System.out.println("getMyAnnouncements");
        List<Announcement> announcementList = manageAnnouncementsBeanRemote.getMyAnnouncements(staff.getUsername());
        assertNotNull(announcementList);
        System.out.println("Case 2");
        announcementList = manageAnnouncementsBeanRemote.getMyAnnouncements(staff2.getUsername());
        assertNotNull(announcementList);
        System.out.println("Case 3");
        try {
            announcementList = manageAnnouncementsBeanRemote.getMyAnnouncements(null);
            fail("Exception Not Thrown");
        } catch (Exception ex) {
            
        }
    }
    
    @Test
    public void test04GetActiveAnnouncements() {
        System.out.println("getActiveAnnouncements");
        List<Announcement> announcementList = manageAnnouncementsBeanRemote.getActiveAnnouncements(staff.getUsername());
        assertNotNull(announcementList);
        System.out.println("Case 2");
        announcementList = manageAnnouncementsBeanRemote.getActiveAnnouncements(staff2.getUsername());
        assertNotNull(announcementList);
        System.out.println("Case 3");
        try {
            announcementList = manageAnnouncementsBeanRemote.getActiveAnnouncements(null);
            fail("Exception Not Thrown");
        } catch (Exception ex) {
            
        }
    }
    
    @Test
    public void test05DeleteAnnouncement() {
        System.out.println("deleteAnnouncement");
        try {
            manageAnnouncementsBeanRemote.deleteAnnouncement(announcement.getId());
        } catch (Exception ex) {
            fail("Exception Thrown");
        }
        System.out.println("Case 2");
        try {
            manageAnnouncementsBeanRemote.deleteAnnouncement(Long.parseLong("1"));
            fail("Exception Not Thrown");
        } catch (Exception ex) {
            
        }
        System.out.println("Case 3");
        try {
            manageAnnouncementsBeanRemote.deleteAnnouncement(null);
            fail("Exception Not Thrown");
        } catch (Exception ex) {
            
        }
    }


    private static ManageAnnouncementsBeanRemote lookupManageAnnouncementsBeanRemote() {
        try {
            Context c = new InitialContext();
            return (ManageAnnouncementsBeanRemote) c.lookup("java:global/IslandFurniture/IslandFurniture-ejb/ManageAnnouncementsBean!IslandFurniture.EJB.CommonInfrastructure.ManageAnnouncementsBeanRemote");
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
