/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.CommonInfrastructure;

import IslandFurniture.EJB.Entities.Staff;
import javax.annotation.Resource;
import javax.ejb.Stateful;
import javax.ejb.SessionContext;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Benjamin
 */
@Stateful
public class ManageUserAccountInformationBean {
    
    @PersistenceContext
    EntityManager em;
    
    @Resource SessionContext ctx;
    private Staff staff;

    public ManageUserAccountInformationBean(){
        
    }
    
    public Staff getStaff(String username){
        Query query = em.createQuery("FROM Staff s where s.username=:username");
        query.setParameter("username", username);
        staff = (Staff) query.getSingleResult();
        return staff;
    }
    
    public void modifyNote(String username, String notes){
        Query query = em.createQuery("FROM Staff s where s.username=:username");
        query.setParameter("username", username);
        staff = (Staff) query.getSingleResult();
        staff.setNotes(notes);
        em.merge(staff);
        em.flush();
    }
    
    public void modifyPersonalParticulars(String username, String phoneNo, String emailAddress){
        Query query = em.createQuery("FROM Staff s where s.username=:username");
        query.setParameter("username", username);
        staff = (Staff) query.getSingleResult();
        staff.setPhoneNo(phoneNo);
        staff.setEmailAddress(emailAddress);
        em.merge(staff);
        em.flush();
    }
}
