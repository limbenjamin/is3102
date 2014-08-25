/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beans;

import entities.*;
import java.security.Principal;
import java.util.Date;
import javax.annotation.*;
import javax.ejb.*;
import javax.persistence.*;

/**
 *
 * @author Benjamin and the IS3102 team
 */
@Stateful
public class StaffBean {

    @PersistenceContext
    EntityManager em;
    
    @Resource SessionContext ctx;
    private Staff staff;
    private Log log;
    
    public StaffBean(){
        
    }
    
    public boolean authenticate(String username, String password){
        Query query = em.createQuery("FROM Staff s where s.Username=:username");
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
        log = new Log();
        log.setEntityName(EntityName);
        log.setEntityId(EntityId);
        log.setUserAction(UserAction);
        log.setTimestamp(new Date());
        log.setChangeMessage(ChangeMessage);
        log.setStaff(em.find(Staff.class, StaffId));
        em.persist(log);
    }
}
