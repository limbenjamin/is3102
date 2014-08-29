/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.CommonInfrastructureModule;

import FW.IslandFurniture.Entities.INFRA.*;
import java.util.Date;
import javax.annotation.*;
import javax.ejb.*;
import javax.persistence.*;

/**
 *
 * @author Benjamin and the IS3102 team
 */
@Stateful
public class ManageAuthenticationBean {

    @PersistenceContext
    EntityManager em;
    
    @Resource SessionContext ctx;
    private Staff staff;
    private LogEntry logEntry;
    
    public ManageAuthenticationBean(){
        
    }
    
    public boolean authenticate(String username, String password){
        Query query = em.createQuery("FROM Staff s where s.username=:username");
        query.setParameter("username", username);
        try{
            staff = (Staff) query.getSingleResult();
        }catch(NoResultException | NonUniqueResultException nre){ 
            return false;
        }
        String correctpassword = staff.getPassword();
        if (!correctpassword.equals(password)){
            log("Staff",staff.getId(),"Access","Invalid Login Attempt",null);
            return false;
        }else{
            log("Staff",staff.getId(),"Access","Logged in",staff.getId());
            staff.setLastLogon(new Date());
            return true;
        }
    }
    
    public void log(String EntityName,Long EntityId, String UserAction, String ChangeMessage, Long StaffId){
        logEntry = new LogEntry();
        logEntry.setEntityName(EntityName);
        logEntry.setEntityId(EntityId);
        logEntry.setUserAction(UserAction);
        java.util.Date today = new java.util.Date();
        logEntry.setLogTime(new java.sql.Time(today.getTime()));
        logEntry.setChangeMessage(ChangeMessage);
        logEntry.setStaff(em.find(Staff.class, StaffId));
        em.persist(logEntry);
    }
}
