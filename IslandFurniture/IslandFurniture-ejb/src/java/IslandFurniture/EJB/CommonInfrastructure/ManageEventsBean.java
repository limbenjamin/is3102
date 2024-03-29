/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.CommonInfrastructure;

import IslandFurniture.Entities.Event;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Staff;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
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
public class ManageEventsBean implements ManageEventsBeanLocal,ManageEventsBeanRemote {

    @PersistenceContext
    EntityManager em;
    
    @Resource SessionContext ctx;
    private Staff creator;
    private Event event;
    private List<Event> eventList;
    private Plant plant;
    
    @EJB
    private ManageUserAccountBeanLocal staffbean;
    
    @Override
    public Long addEvent(String name, String description, Calendar eventTime, String username){
        creator = staffbean.getStaff(username);
        event = new Event();
        event.setName(name);
        event.setDescription(description);
        event.setEventTime(eventTime);
        event.setCreator(creator);
        event.setPlant(creator.getPlant());
        em.persist(event);
        eventList = creator.getEvents();
        eventList.add(event);
        em.merge(creator);
        return event.getId();
    }
    
    @Override
    public void editEvent(String name, String description, Calendar eventTime, Long id){
        event = em.find(Event.class, id);
        event.setName(name);
        event.setDescription(description);
        event.setEventTime(eventTime);
        event.setCreator(creator);
     
    }
    
    @Override
    public void deleteEvent(Long id){
        event = em.find(Event.class, id);
        try{
            eventList = event.getCreator().getEvents();
            eventList.remove(event);
            em.merge(event.getCreator());
        }catch(Exception e){
            
        }
        em.remove(event);
        em.flush();
     
    }
    
    @Override
    public List<Event> getEvents(String username){
        creator = staffbean.getStaff(username);
        plant = creator.getPlant();
        Query query = em.createQuery("SELECT e FROM Event e WHERE e.plant=:plant");
        query.setParameter("plant", plant);
        return query.getResultList();
    }
    
    @Override
    public List<Event> getMyEvents(String username){
        creator = staffbean.getStaff(username);
        return creator.getEvents();
    }
    
    @Override
    public Event getEvent(Long id){
        event = em.find(Event.class, id);
        return event;
    }
}
