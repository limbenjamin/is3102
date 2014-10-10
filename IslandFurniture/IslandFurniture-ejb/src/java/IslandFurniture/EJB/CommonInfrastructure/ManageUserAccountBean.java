/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.CommonInfrastructure;

import IslandFurniture.Entities.Staff;
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
public class ManageUserAccountBean implements ManageUserAccountBeanLocal{
    
    @PersistenceContext
    EntityManager em;
    
    @Resource SessionContext ctx;
    private Staff staff;

    public ManageUserAccountBean(){
        
    }
    
    @Override
    public Staff getStaff(String username){
        Query query = em.createQuery("FROM Staff s where s.username=:username");
        query.setParameter("username", username);
        staff = (Staff) query.getSingleResult();
        return staff;
    }
    
    @Override
    public Staff getStaffFromCardId(String cardId){
        Query query = em.createQuery("FROM Staff s where s.cardId=:cardId");
        query.setParameter("cardId", cardId);
        try{
            staff = (Staff) query.getSingleResult();
        }catch(NoResultException nre){
            return null;
        }catch(NonUniqueResultException nre){
            return null;
        }
        return staff;
    }
    
    @Override
    public Staff getStaffFromId(Long staffId){
        staff = (Staff) em.find(Staff.class, staffId);
        return staff;
    }
    
    @Override
    public void modifyNote(String username, String notes){
        staff = getStaff(username);
        staff.setNotes(notes);
    }

    @Override
    public void modifyPersonalParticulars(String username, String phoneNo, String emailAddress){
        staff = getStaff(username);
        staff.setPhoneNo(phoneNo);
        staff.setEmailAddress(emailAddress);
    }
}
