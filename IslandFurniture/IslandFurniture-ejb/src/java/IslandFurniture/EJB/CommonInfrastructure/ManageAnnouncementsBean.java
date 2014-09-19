/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.CommonInfrastructure;

import IslandFurniture.EJB.Entities.Announcement;
import IslandFurniture.EJB.Entities.Staff;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

/**
 *
 * @author Benjamin
 */
@Stateful
public class ManageAnnouncementsBean implements ManageAnnouncementsBeanLocal {

    @PersistenceContext
    EntityManager em;
    
    @Resource SessionContext ctx;
    private Staff staff;
    private Announcement announcement;
    private List<Announcement> announcementList;
    
    @EJB
    private ManageUserAccountBeanLocal staffbean;
    
    @Override
    public void addAnnouncement(String username, String title, String content, Date activeDate, Date expireDate){
        staff = staffbean.getStaff(username);
        announcement = new Announcement();
        announcement.setTitle(title);
        announcement.setContent(content);
        announcement.setActiveDate(activeDate);
        announcement.setExpireDate(expireDate);
        announcement.setCreator(staff);
        em.persist(announcement);
        announcementList = staff.getAnnouncements();
        announcementList.add(announcement);
        em.merge(staff);
        em.flush();
    }
    
    @Override
    public void editAnnouncement(Long id, String title, String content, Date activeDate, Date expireDate){
        announcement = em.find(Announcement.class, id);
        announcement.setTitle(title);
        announcement.setContent(content);
        announcement.setActiveDate(activeDate);
        announcement.setExpireDate(expireDate);
        announcement.setCreator(staff);
        em.merge(announcement);
        em.flush();
    }
    
    @Override
    public void deleteAnnouncement(Long id){
        announcement = em.find(Announcement.class, id);
        announcementList = announcement.getCreator().getAnnouncements();
        announcementList.remove(announcement);
        em.merge(announcement.getCreator());
        em.remove(announcement);
        em.flush();
    }
    
    @Override
    public List<Announcement> getActiveAnnouncements(){
        Date today = new Date(); 
        Query query = em.createQuery("SELECT a " + "FROM Announcement a " + "WHERE :today BETWEEN a.activeDate AND a.expireDate")
        .setParameter("today", today, TemporalType.DATE);
        return query.getResultList();
    }
    
    @Override
    public List<Announcement> getMyAnnouncements(String username){
        staff = staffbean.getStaff(username);
        return staff.getAnnouncements();
    }

}
