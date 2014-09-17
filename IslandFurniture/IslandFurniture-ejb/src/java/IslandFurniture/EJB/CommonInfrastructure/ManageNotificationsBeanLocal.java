/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.CommonInfrastructure;

import IslandFurniture.EJB.Entities.Notification;
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
    
}