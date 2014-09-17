/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.CommonInfrastructure;

import IslandFurniture.EJB.Entities.Event;
import java.util.Calendar;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Benjamin
 */
@Local
public interface ManageEventsBeanLocal {

    void addEvent(String name, String description, Calendar eventTime, String username);

    void deleteEvent(Long id);

    void editEvent(String name, String description, Calendar eventTime, Long id);

    List<Event> getEvents();

    List<Event> getMyEvents(String username);
    
}