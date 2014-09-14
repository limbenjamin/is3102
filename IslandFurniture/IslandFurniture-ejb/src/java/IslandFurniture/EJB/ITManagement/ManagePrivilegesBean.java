/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.ITManagement;

import IslandFurniture.EJB.Entities.Privilege;
import IslandFurniture.EJB.Entities.Role;
import IslandFurniture.EJB.Entities.Staff;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
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
public class ManagePrivilegesBean implements ManagePrivilegesBeanLocal {

    @PersistenceContext
    EntityManager em;
    
    @Resource SessionContext ctx;
    private Staff staff;
    private Privilege privilege;
    private List<Privilege> privilegeList;
    private Role role;
    private List<Role> roleList;
    
    @Override
    public void createPrivilege(String name) {
        privilege = new Privilege();
        privilege.setName(name);
        em.persist(privilege);
    }
    
    @Override
    public void removePrivilege(Long id){
        privilege = em.find(Privilege.class, id);
        roleList =privilege.getRoles();
        Iterator<Role> iterator = roleList.iterator();
        while (iterator.hasNext()) {
		role = iterator.next();
                role.getPrivileges().remove(privilege);
                em.merge(role);
	}
        em.remove(privilege);
        
    }
    
    @Override
    public List<Privilege> displayPrivilege(){
        Query q = em.createQuery("SELECT p " + "FROM Privilege p");
        return q.getResultList();
    }
    
}
