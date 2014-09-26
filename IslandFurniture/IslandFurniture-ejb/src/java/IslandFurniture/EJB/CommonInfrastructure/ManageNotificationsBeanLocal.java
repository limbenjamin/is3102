/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.CommonInfrastructure;

import IslandFurniture.EJB.Entities.Notification;
import IslandFurniture.EJB.Entities.Plant;
import IslandFurniture.EJB.Entities.Privilege;
import IslandFurniture.EJB.Entities.Role;
import IslandFurniture.EJB.Entities.Staff;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Benjamin
 */
@Local
public interface ManageNotificationsBeanLocal {

    void createNewNotificationForStaff(String title, String content, String link, String linkText, Staff staff);

    List<Notification> displayNotificationForStaff(Staff staff);
    
    Notification getNotification(Long id);
    
    void setNotificationToRead(Notification notification);
    
    void deleteNotification(Long id);
    
    List<Notification> getAllNotifications();
    
    Integer getUnreadForStaff(Staff staff);
    
    void createNewNotificationForPrivilege(String title, String content, String link, String linkText, Privilege privilege);

    void createNewNotificationForPrivilegeFromPlant(String title, String content, String link, String linkText, Privilege privilege, Plant plant);
    
}
