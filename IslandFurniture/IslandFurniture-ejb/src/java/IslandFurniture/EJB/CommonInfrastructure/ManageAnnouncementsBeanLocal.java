/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.CommonInfrastructure;

import IslandFurniture.EJB.Entities.Announcement;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Benjamin
 */
@Local
public interface ManageAnnouncementsBeanLocal {

    void addAnnouncement(String username, String title, String content, Date activeDate, Date expireDate);

    void deleteAnnouncement(Long id);

    void editAnnouncement(Long id, String title, String content, Date activeDate, Date expireDate);

    List<Announcement> getActiveAnnouncements(String username);

    List<Announcement> getMyAnnouncements(String username);
    
}
