/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.CommonInfrastructure;

import IslandFurniture.Entities.Notification;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Privilege;
import IslandFurniture.Entities.Role;
import IslandFurniture.Entities.Staff;
import IslandFurniture.Exceptions.NullException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Benjamin
 */
@Stateful
public class ManageNotificationsBean implements ManageNotificationsBeanLocal, ManageNotificationsBeanRemote {

    @PersistenceContext
    EntityManager em;

    @Resource
    SessionContext ctx;
    private Staff staff;
    private Notification notification;
    private List<Notification> notificationList;
    private List<Staff> staffList;
    private List<Role> roleList;
    private List<Privilege> privilegeList;
    private Role role;
    private HashSet<Staff> staffHash;

    @Override
    public void createNewNotificationForStaff(String title, String content, String link, String linkText, Staff staff) throws NullException {
        if (staff == null) {
            throw new NullException();
        }
        notification = new Notification();
        notification.setTitle(title);
        notification.setContent(content);
        notification.setLink(link);
        notification.setLinkText(linkText);
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        notification.setTime(now);
        notification.setIsread(false);
        notification.setStaff(staff);
        staff.getNotifications().add(notification);
        em.merge(staff);
        em.flush();
    }

    @Override
    public void createNewNotificationForPrivilege(String title, String content, String link, String linkText, Privilege privilege) {
        if (privilege != null) {
            roleList = privilege.getRoles();
            staffHash = new HashSet();
            Iterator<Role> iterator = roleList.iterator();
            while (iterator.hasNext()) {
                role = iterator.next();
                staffList = role.getStaffs();
                Iterator<Staff> iterator2 = staffList.iterator();
                while (iterator2.hasNext()) {
                    staff = iterator2.next();
                    staffHash.add(staff);
                }
            }
            Iterator<Staff> iterator2 = staffHash.iterator();
            while (iterator2.hasNext()) {
                staff = iterator2.next();
                try {
                    createNewNotificationForStaff(title, content, link, linkText, staff);
                } catch (NullException ex) {
                    Logger.getLogger(ManageNotificationsBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    public void createNewNotificationForPrivilegeFromPlant(String title, String content, String link, String linkText, Privilege privilege, Plant plant) {
        if (privilege != null && plant != null) {
            roleList = privilege.getRoles();
            staffHash = new HashSet();
            Iterator<Role> iterator = roleList.iterator();
            while (iterator.hasNext()) {
                role = iterator.next();
                staffList = role.getStaffs();
                Iterator<Staff> iterator2 = staffList.iterator();
                while (iterator2.hasNext()) {
                    staff = iterator2.next();
                    staffHash.add(staff);
                }
            }
            Iterator<Staff> iterator2 = staffHash.iterator();
            while (iterator2.hasNext()) {
                staff = iterator2.next();
                if (staff.getPlant().equals(plant)) {
                    try {
                        createNewNotificationForStaff(title, content, link, linkText, staff);
                    } catch (NullException ex) {
                        Logger.getLogger(ManageNotificationsBean.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    @Override
    public List<Notification> displayNotificationForStaff(Staff staff) {
        notificationList = staff.getNotifications();
        Collections.reverse(notificationList);
        return notificationList;
    }

    @Override
    public Integer getUnreadForStaff(Staff staff) {
        Query query = em.createQuery("SELECT n FROM Notification n WHERE n.staff=:staff AND n.isread=:false");
        query.setParameter("staff", staff);
        query.setParameter("false", Boolean.FALSE);
        return query.getResultList().size();
    }

    @Override
    public Notification getNotification(Long id) {
        notification = em.find(Notification.class, id);
        return notification;
    }

    @Override
    public void setNotificationToRead(Notification notification) {
        notification.setIsread(true);
        em.merge(notification);
    }

    @Override
    public void deleteNotification(Long id) {
        notification = em.find(Notification.class, id);
        staff = notification.getStaff();
        staff.getNotifications().remove(notification);
        em.merge(staff);
        em.remove(notification);
    }

    @Override
    public List<Notification> getAllNotifications() {
        Query query = em.createQuery("SELECT n FROM Notification n");
        return query.getResultList();
    }
}
