/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beans;

import entities.Staff;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Benjamin
 */
@Stateful
public class StaffBean {

    @PersistenceContext
    EntityManager em;
    
    private Staff staff;
    
    public StaffBean(){
        
    }

    public void createStaff(){
        staff = new Staff();
        staff.setUsername("test");
        staff.setPassword("test");
        em.persist(staff);
    }
}
