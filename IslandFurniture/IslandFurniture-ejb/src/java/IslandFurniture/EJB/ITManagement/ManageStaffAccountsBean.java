/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.ITManagement;


import IslandFurniture.Entities.Role;
import IslandFurniture.Entities.Country;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Notification;
import IslandFurniture.Entities.Privilege;
import IslandFurniture.Entities.Event;
import IslandFurniture.Entities.MessageThread;
import IslandFurniture.Entities.Announcement;
import IslandFurniture.Entities.Preference;
import IslandFurniture.Entities.Staff;
import IslandFurniture.Entities.LogEntry;
import IslandFurniture.Entities.Todo;
import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.Exceptions.InvalidCountryException;
import IslandFurniture.Exceptions.InvalidPlantException;
import IslandFurniture.ITManagementModule.ManageStaffAccountRemote;
import IslandFurniture.StaticClasses.QueryMethods;
import IslandFurniture.StaticClasses.SendEmailByPost;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
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
    private MessageThread messageThread;
    private Role role;
    private List<Role> roleList;
    private List<Privilege> privilegeList;
    private List<Privilege> rolePrivilegeList;
    private Privilege privilege;
    private String password;
    
    @EJB
    private ManageUserAccountBeanLocal muab;
    


    @Override
    public Long createStaffAccount(String username, String password, String name, String emailAddress, String phoneNo, String countryName, String plantName, String cardId) {
        staff = new Staff();
        staff.setUsername(username);
        staff.setNotes("");
        staff.setInvalidPasswordCount(0);
        staff.setActive(Boolean.TRUE);
        //generate salt
        String salt = Long.toHexString(Double.doubleToLongBits(Math.random())).substring(2);
        staff.setSalt(salt);
        staff.setPassword(password);
        staff.setName(name);
        staff.setEmailAddress(emailAddress);
        staff.setPhoneNo(phoneNo);
        staff.setCardId(cardId);
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
        em.flush();
        try {
            SendEmailByPost.sendEmail("techsupport", staff.getEmailAddress(), "Account created", "Your new account has been created with password: "+password);
        } catch (Exception ex) {
            Logger.getLogger(ManageStaffAccountsBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return staff.getId();
    }
    
    //This method does not send emails for bulk created accounts
    @Override
    public void createStaffAccountinBulk(String username, String password, String name, String emailAddress, String phoneNo, String countryName, String plantName, String cardId) {
        staff = new Staff();
        staff.setUsername(username);
        staff.setNotes("");
        staff.setInvalidPasswordCount(0);
        staff.setActive(Boolean.TRUE);
        //generate salt
        String salt = Long.toHexString(Double.doubleToLongBits(Math.random())).substring(2);
        staff.setSalt(salt);
        staff.setPassword(password);
        staff.setName(name);
        staff.setEmailAddress(emailAddress);
        staff.setPhoneNo(phoneNo);
        staff.setCardId(cardId);
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
        em.flush();
    }
    
    @Override
    public List<Staff> displayAllStaffAccounts(){
        Query query = em.createQuery("SELECT s FROM Staff S");
        return query.getResultList();
    }
    
    @Override
    public List<Staff> displayStaffAccountsFromPlant(String username){
        Query query = em.createQuery("SELECT s FROM Staff S WHERE S.plant=:plant");
        staff = muab.getStaff(username);
        query.setParameter("plant", staff.getPlant());
        return query.getResultList();
    }
    
    @Override
    public void deleteStaffAccount(Long id){
        staff = (Staff) em.find(Staff.class, id);
        plant = (Plant) staff.getPlant();
        plant.getEmployees().remove(staff);
        em.merge(plant);
        thread = staff.getInbox();
        Iterator<MessageThread> iterator = thread.iterator();
        while (iterator.hasNext()) {
		messageThread = iterator.next();
                messageThread.getRecipient().remove(staff);
                em.merge(messageThread);
	}
        em.remove(staff);
    }
    
    @Override
    public void removeRoleFromStaff(Long staffId, Long roleId){
        staff = (Staff) em.find(Staff.class, staffId);
        role = (Role) em.find(Role.class, roleId);
        staff.getRoles().remove(role);
        role.getStaffs().remove(staff);
        em.merge(role);
        em.merge(staff);
    }
    
    @Override
    public void addRoleToStaff(Long staffId, String roleName){
        staff = (Staff) em.find(Staff.class, staffId);
        Query query = em.createQuery("FROM Role r WHERE r.name=:name");
        query.setParameter("name", roleName);
        role = (Role) query.getSingleResult();
        staff.getRoles().add(role);
        role.getStaffs().add(staff);
        em.merge(role);
        em.merge(staff);
    }
    
    @Override
    public void addRoleToStaffByUsername(String staffName, String roleName){
        Query query = em.createQuery("FROM Staff s where s.username=:name");
        query.setParameter("name", staffName);
        staff = (Staff) query.getSingleResult();
        query = em.createQuery("FROM Role r WHERE r.name=:name");
        query.setParameter("name", roleName);
        role = (Role) query.getSingleResult();
        staff.getRoles().add(role);
        role.getStaffs().add(staff);
        em.merge(role);
        em.merge(staff);
    }
    
    @Override
    public boolean checkIfStaffHasPrivilege(String staffName, Privilege privilege){
        Query query = em.createQuery("FROM Staff s where s.username=:name");
        query.setParameter("name", staffName);
        staff = (Staff) query.getSingleResult();
        roleList = staff.getRoles();
        Iterator<Role> iterator = roleList.iterator();
        while (iterator.hasNext()) {
            role = iterator.next();
            if (role.getPrivileges().contains(privilege)){
                return true;
            }
	}
        return false;
    }
    
    @Override
    public List<Privilege> getPrivilegeListforStaff(String staffName){
        Query query = em.createQuery("FROM Staff s where s.username=:name");
        query.setParameter("name", staffName);
        staff = (Staff) query.getSingleResult();
        roleList = staff.getRoles();
        Iterator<Role> iterator = roleList.iterator();
        Iterator<Privilege> iterator2;
        privilegeList = new ArrayList<Privilege>();
        while (iterator.hasNext()) {
            role = iterator.next();
            rolePrivilegeList = role.getPrivileges();
            iterator2 = rolePrivilegeList.iterator();
            while (iterator2.hasNext()){
                privilege = iterator2.next();
                privilegeList.add(privilege);
            }
        }
        return privilegeList;
    }
}
