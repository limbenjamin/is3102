/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.CommonInfrastructure;

import IslandFurniture.EJB.Entities.Event;
import IslandFurniture.EJB.Entities.Staff;
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

/**
 *
 * @author Benjamin
 */
@Stateful
@LocalBean
public class ManageEventsBean {

    @PersistenceContext
    EntityManager em;
    
    @Resource SessionContext ctx;
    private Staff creator;
    private Event event;
    private List<Event> eventList;
    
    @EJB
    private ManageUserAccountInformationBean staffbean;
    
    public void addEvent(String name, String description, Calendar eventTime, String username){
        creator = staffbean.getStaff(username);
        event = new Event();
        event.setName(name);
        event.setDescription(description);
        event.setEventTime(eventTime);
        event.setCreator(creator);
        em.persist(event);
        eventList = creator.getEvents();
        eventList.add(event);
        em.merge(creator);
        em.flush();
     
    }
    
    public List<Event> getEvents(){
        Query query = em.createQuery("SELECT e " + "FROM Event e ");
        return query.getResultList();
    }
    
    public List<Event> getMyEvents(String username){
        creator = staffbean.getStaff(username);
        return creator.getEvents();
    }
}
