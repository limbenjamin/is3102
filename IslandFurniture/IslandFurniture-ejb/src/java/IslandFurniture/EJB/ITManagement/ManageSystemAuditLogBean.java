/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.ITManagement;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.EJB.Entities.LogEntry;
import IslandFurniture.EJB.Entities.LogEntryAction;
import IslandFurniture.EJB.Entities.Plant;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Benjamin
 */
@Stateless
@LocalBean
public class ManageSystemAuditLogBean implements ManageSystemAuditLogBeanLocal {

    @PersistenceContext
    private EntityManager em;
    
    private LogEntry log;
    private List<LogEntry> logList;
    
    @EJB
    private ManageUserAccountBeanLocal muab;

    @Override
    public void log(String entityName, long entityId, String userAction, String changeMessage, String username) {
        log = new LogEntry();
        log.setEntityName(entityName);
        log.setEntityId(entityId);
        log.setLogEntryAction(LogEntryAction.valueOf(userAction));
        log.setChangeMessage(changeMessage);
        log.setStaff(muab.getStaff(username));
        log.setLogTime(Calendar.getInstance());
        em.persist(log);
    }
    
    @Override
    public List<LogEntry> getLog() {
        Query q = em.createQuery("SELECT l from LogEntry l");
        return q.getResultList();
    }
    
    
    @Override
    public List<LogEntry> GetLogForPlant(Plant plant) {
        Query q = em.createQuery("SELECT l from LogEntry l WHERE l.staff.plant=:plant");
        q.setParameter("plant", plant);
        return q.getResultList();
    }
    
}
