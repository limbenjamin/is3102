/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.CommonInfrastructure;

import IslandFurniture.EJB.Entities.Notification;
import IslandFurniture.EJB.Entities.Plant;
import IslandFurniture.EJB.Entities.Role;
import IslandFurniture.EJB.Entities.Staff;
import com.google.common.collect.Lists;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
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
public class ManageNotificationsBean implements ManageNotificationsBeanLocal {

    @PersistenceContext
    EntityManager em;
    
    @Resource SessionContext ctx;
    private Staff staff;
    private Notification notification;
    private List<Notification> notificationList;
    private List<Staff> staffList;
    
    @Override
    public void createNewNotificationForStaff(String title, String content, String link, String linkText, Staff staff){
        notification = new Notification();
        notification.setTitle(title);
        notification.setContent(content);
        notification.setLink(link);
        notification.setLinkText(linkText);
        notification.setTime(Calendar.getInstance());
        notification.setIsread(false);
        notification.setStaff(staff);
        staff.getNotifications().add(notification);
        em.merge(staff);
        em.flush();
    }
    
    @Override
    public void createNewNotificationForRole(String title, String content, String link, String linkText, Role role){
        staffList = role.getStaffs();
        Iterator<Staff> iterator = staffList.iterator();
            while (iterator.hasNext()) {
                staff = iterator.next();
                createNewNotificationForStaff(title, content, link, linkText, staff);
            }
        em.flush();
    }
    
    @Override
    public void createNewNotificationForRoleFromPlant(String title, String content, String link, String linkText, Role role, Plant plant){
        staffList = role.getStaffs();
        Iterator<Staff> iterator = staffList.iterator();
            while (iterator.hasNext()) {
                staff = iterator.next();
                if (staff.getPlant().getId() == plant.getId()){
                    createNewNotificationForStaff(title, content, link, linkText, staff);
                }
            }
        em.flush();
    }
    
    @Override
    public List<Notification> displayNotificationForStaff(Staff staff){
        notificationList = staff.getNotifications();
        Collections.reverse(notificationList);
        return notificationList;
    }
    
    @Override
    public Notification getNotification(Long id){
        notification = em.find(Notification.class, id);
        return notification;
    }
    
    @Override
    public void setNotificationToRead(Notification notification){
        notification.setIsread(true);
        em.merge(notification);
        em.flush();
    }
    
    @Override
    public void deleteNotification(Long id){
        notification = em.find(Notification.class, id);
        staff = notification.getStaff();
        staff.getNotifications().remove(notification);
        em.merge(staff);
        em.remove(notification);
        em.flush();
    }
    
    @Override
    public List<Notification> getAllNotifications(){
        Query query = em.createQuery("SELECT n FROM Notification n");
        return query.getResultList();
    }
}
