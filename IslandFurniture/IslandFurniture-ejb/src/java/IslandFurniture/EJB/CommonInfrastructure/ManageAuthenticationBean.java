/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.CommonInfrastructure;

import IslandFurniture.EJB.Entities.*;
import static IslandFurniture.EJB.Entities.Staff.SHA1Hash;
import java.util.Date;
import javax.annotation.*;
import javax.ejb.*;
import javax.persistence.*;

/**
 *
 * @author Benjamin and the IS3102 team
 */
@Stateful
public class ManageAuthenticationBean implements ManageAuthenticationBeanLocal {

    @PersistenceContext
    EntityManager em;
    
    @Resource SessionContext ctx;
    private Staff staff;
    private LogEntry logEntry;
    
    @EJB
    private ManageUserAccountInformationBean staffbean;
    
    public ManageAuthenticationBean(){
        
    }
    
    @Override
    public boolean authenticate(String username, String password){
        Query query = em.createQuery("FROM Staff s where s.username=:username");
        query.setParameter("username", username);
        try{
            staff = (Staff) query.getSingleResult();
        }catch(NoResultException | NonUniqueResultException nre){
            return false;
        }
        String correctPassword = staff.getPassword();
        String hashedPassword = SHA1Hash(staff.getSalt() + password);
        if (!correctPassword.equals(hashedPassword)){
            return false;
        }else{
            staff.setLastLogon(new Date());
            return true;
        }
    }
    
    @Override
    public void log(String EntityName,Long EntityId, String UserAction, String ChangeMessage, Long StaffId){
        logEntry = new LogEntry();
        logEntry.setEntityName(EntityName);
        logEntry.setEntityId(EntityId);
        logEntry.setUserAction(UserAction);
//        java.util.Date today = new java.util.Date();
        logEntry.setLogTime(java.util.Calendar.getInstance());
        logEntry.setChangeMessage(ChangeMessage);
        logEntry.setStaff(em.find(Staff.class, StaffId));
        em.persist(logEntry);
        em.flush();
    }
    
    @Override
    public void changePassword(String username, String newPassword){
        staff = staffbean.getStaff(username);
        staff.setPassword(newPassword);
        em.merge(staff);
        em.flush();
    }
}
