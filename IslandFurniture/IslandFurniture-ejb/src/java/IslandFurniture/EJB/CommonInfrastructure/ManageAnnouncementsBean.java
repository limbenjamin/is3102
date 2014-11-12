/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.CommonInfrastructure;

import IslandFurniture.Entities.Announcement;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Staff;
import IslandFurniture.Exceptions.InvalidDateException;
import IslandFurniture.Exceptions.NullException;
import java.util.Calendar;
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
public class ManageAnnouncementsBean implements ManageAnnouncementsBeanLocal, ManageAnnouncementsBeanRemote {

    @PersistenceContext
    EntityManager em;
    
    @Resource SessionContext ctx;
    private Staff staff;
    private Announcement announcement;
    private List<Announcement> announcementList;
    private Plant plant;
    
    @EJB
    private ManageUserAccountBeanLocal staffbean;
    
    @Override
    public Long addAnnouncement(String username, String title, String content, Calendar activeDate, Calendar expireDate) throws InvalidDateException{
        if (activeDate.after(expireDate)){
            throw new InvalidDateException();
        }
        staff = staffbean.getStaff(username);
        announcement = new Announcement();
        announcement.setTitle(title);
        announcement.setContent(content);
        announcement.setActiveDate(activeDate);
        // need to add one day because it should be valid for whole of today, until midnight of tomorrow.
        expireDate.add(Calendar.DAY_OF_MONTH, 1);
        announcement.setExpireDate(expireDate);
        announcement.setCreator(staff);
        announcement.setPlant(staff.getPlant());
        em.persist(announcement);
        announcementList = staff.getAnnouncements();
        announcementList.add(announcement);
        em.merge(staff);
        return announcement.getId();
    }
    
    @Override
    public void editAnnouncement(Long id, String title, String content, Calendar activeDate, Calendar expireDate) throws InvalidDateException{
        if (activeDate.after(expireDate)){
            throw new InvalidDateException();
        }
        announcement = em.find(Announcement.class, id);
        announcement.setTitle(title);
        announcement.setContent(content);
        announcement.setActiveDate(activeDate);
        expireDate.add(Calendar.DAY_OF_MONTH, 1);
        announcement.setExpireDate(expireDate);
        announcement.setCreator(staff);
    }
    
    @Override
    public void deleteAnnouncement(Long id) throws NullException{
        announcement = em.find(Announcement.class, id);
        System.err.println("herehere "+announcement);
        if (announcement == null){
            throw new NullException();
        }
        announcementList = announcement.getCreator().getAnnouncements();
        announcementList.remove(announcement);
        em.merge(announcement.getCreator());
        em.remove(announcement);
    }
    
    @Override
    public List<Announcement> getActiveAnnouncements(String username){
        Date today = new Date(); 
        staff = staffbean.getStaff(username);
        plant = staff.getPlant();
        Query query = em.createQuery("SELECT a FROM Announcement a WHERE a.plant=:plant AND :today BETWEEN a.activeDate AND a.expireDate");
        query.setParameter("today", today, TemporalType.TIMESTAMP);
        query.setParameter("plant", plant);
        return query.getResultList();
    }
    
    @Override
    public List<Announcement> getMyAnnouncements(String username){
        staff = staffbean.getStaff(username);
        return staff.getAnnouncements();
    }
    
    @Override
    public Announcement getAnnouncement(Long id){
        announcement = em.find(Announcement.class, id);
        return announcement;
    }

}
