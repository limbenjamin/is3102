/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.CommonInfrastructure;

import IslandFurniture.Entities.Announcement;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Benjamin
 */
@Local
public interface ManageAnnouncementsBeanLocal {

    Long addAnnouncement(String username, String title, String content, Calendar activeDate, Calendar expireDate);

    void deleteAnnouncement(Long id);

    void editAnnouncement(Long id, String title, String content, Calendar activeDate, Calendar expireDate);

    List<Announcement> getActiveAnnouncements(String username);

    List<Announcement> getMyAnnouncements(String username);
    
    Announcement getAnnouncement(Long id);
}
