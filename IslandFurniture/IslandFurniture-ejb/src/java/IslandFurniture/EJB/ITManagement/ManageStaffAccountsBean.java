/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.ITManagement;


import IslandFurniture.EJB.Entities.*;
import IslandFurniture.ITManagementModule.ManageStaffAccountRemote;
import IslandFurniture.StaticClasses.Helper.QueryMethods;
import IslandFurniture.EJB.Exceptions.InvalidCountryException;
import IslandFurniture.EJB.Exceptions.InvalidPlantException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Benjamin
 */
@Stateless
public class ManageStaffAccountsBean implements ManageStaffAccountRemote, ManageStaffAccountsBeanLocal{
    
    @PersistenceContext
    EntityManager em;
    
    @Resource SessionContext ctx;
    private Staff staff;
    private LogEntry logEntry;
    private List<Notification> notifications;
    private List<Todo> todolist;
    private List<MessageThread> thread;
    private Preference preference;
    private List<Announcement> announcements;
    private List<Event> events;
    private Plant plant;
    private Country country;
    


    @Override
    public void createStaffAccount(String username, String password, String name, String emailAddress, String phoneNo, String countryName, String plantName) {
        staff = new Staff();
        staff.setUsername(username);
        staff.setNotes("");
        staff.setInvalidPasswordCount(0);
        staff.setActive(Boolean.TRUE);
        //generate salt
        String salt = Long.toHexString(Double.doubleToLongBits(Math.random()));
        staff.setSalt(salt);
        staff.setPassword(password);
        staff.setName(name);
        staff.setEmailAddress(emailAddress);
        staff.setPhoneNo(phoneNo);
        preference = new Preference();
        notifications = new ArrayList<Notification>();
        todolist = new ArrayList<Todo>();
        announcements = new ArrayList<Announcement>();
        events = new ArrayList<Event>();
        staff.setPreference(preference);
        staff.setNotifications(notifications);
        staff.setTodoList(todolist);
        staff.setAnnouncements(announcements);
        staff.setEvents(events);
        country = (Country) QueryMethods.findCountryByName(em, countryName);
        if (country == null) {
            try {
                throw new InvalidCountryException();
            } catch (InvalidCountryException ex) {
                Logger.getLogger(ManageStaffAccountsBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            plant = (Plant) QueryMethods.findPlantByName(em, country, plantName);
            if (plant == null) {
                try {
                    throw new InvalidPlantException();
                } catch (InvalidPlantException ex) {
                    Logger.getLogger(ManageStaffAccountsBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else{
                staff.setPlant(plant);
            }
        }
        em.persist(staff);
        //em.flush();
    }
    
    @Override
    public List<Vector> displayAllStaffAccounts(){
        Query query = em.createQuery("SELECT s FROM Staff S");
        List<Vector> staffList = new ArrayList();
        for (Object o: query.getResultList()) {
            Staff s = (Staff)o;
            Vector vector = new Vector();
            vector.add(s.getId());
            vector.add(s.getName());
            vector.add(s.getEmailAddress());
            vector.add(s.getPhoneNo());
            vector.add(s.getPassword());
            staffList.add(vector);
        }
        return staffList;
    }

}
