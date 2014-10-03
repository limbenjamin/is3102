/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.ITManagement;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.EJB.Entities.LogEntry;
import java.util.Calendar;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
    
    @EJB
    private ManageUserAccountBeanLocal muab;

    @Override
    public void log(String entityName, long entityId, String userAction, String changeMessage, String username) {
        log = new LogEntry();
        log.setEntityName(entityName);
        log.setEntityId(entityId);
        log.setUserAction(userAction);
        log.setChangeMessage(changeMessage);
        log.setStaff(muab.getStaff(username));
        log.setLogTime(Calendar.getInstance());
        em.persist(log);
    }
}
