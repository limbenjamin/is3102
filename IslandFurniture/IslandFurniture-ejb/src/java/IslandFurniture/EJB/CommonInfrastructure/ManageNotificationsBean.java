/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.CommonInfrastructure;

import IslandFurniture.EJB.Entities.Notification;
import IslandFurniture.EJB.Entities.Staff;
import java.util.Calendar;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
    
    @Override
    public void createNewNotificationForStaff(String title, String content, String link, String linkText, Staff staff){
        notification = new Notification();
        notification.setTitle(title);
        notification.setContent(content);
        notification.setLink(link);
        notification.setLinkText(linkText);
        notification.setTime(Calendar.getInstance());
        staff.getNotifications().add(notification);
        em.merge(staff);
    }
    
    @Override
    public List<Notification> displayNotificationForStaff(Staff staff){
        return staff.getNotifications();
    }
}
