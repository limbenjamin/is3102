/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.ITManagement;

import IslandFurniture.Entities.Privilege;
import IslandFurniture.Entities.Role;
import IslandFurniture.Entities.Staff;
import IslandFurniture.Exceptions.DuplicateEntryException;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Benjamin
 */
@Stateful
public class ManageRolesBean implements ManageRolesBeanLocal, ManageRolesBeanRemote {

    @PersistenceContext
    EntityManager em;
    
    @Resource SessionContext ctx;
    private Staff staff;
    private Privilege privilege;
    private Role role;
    private List<Role> roleList;
    private List<Privilege> privilegeList;
    private List<Staff> staffList;
    
    @Override
    public void createRole(String name) throws DuplicateEntryException  {
        Query query = em.createQuery("FROM Role r where r.name=:name");
        query.setParameter("name", name);
        try{
            Role r = (Role) query.getSingleResult();
            throw new DuplicateEntryException();
        }catch(NoResultException nre){
            role = new Role();
            role.setName(name);
            em.persist(role);
        }
        
    }
    
    @Override
    public void removeRole(Long id){
        role = em.find(Role.class, id);
        staffList = role.getStaffs();
        Iterator<Staff> iterator = staffList.iterator();
        while (iterator.hasNext()) {
		staff = iterator.next();
                staff.getRoles().remove(role);
                em.merge(staff);
	}
        em.remove(role);
        
    }
    
    @Override
    public List<Role> displayRole(){
        Query q = em.createQuery("SELECT r " + "FROM Role r");
        return q.getResultList();
    }
    
    @Override
    public Role getRole(Long roleId){
        role = em.find(Role.class, roleId);
        return role;
    }
    
    @Override
    public Role getRoleFromName(String roleName){
        Query query = em.createQuery("FROM Role r where r.name=:name");
        query.setParameter("name", roleName);
        return (Role) query.getSingleResult();
    }
    
    @Override
    public void addPrivilegeToRole(Long roleId, String privilegeName){
        role = em.find(Role.class, roleId);
        Query query = em.createQuery("FROM Privilege p where p.name=:name");
        query.setParameter("name", privilegeName);
        privilege =  (Privilege) query.getSingleResult();
        role.getPrivileges().add(privilege);
        privilege.getRoles().add(role);
        em.persist(role);
        em.persist(privilege);
    }
    
    @Override
    public void removePrivilegeFromRole(Long roleId, Long privilegeId){
        role = em.find(Role.class, roleId);
        privilege =  em.find(Privilege.class, privilegeId);
        role.getPrivileges().remove(privilege);
        privilege.getRoles().remove(role);
        em.persist(role);
        em.persist(privilege);
    }
    
    @Override
    public List<Privilege> getPrivileges(Long roleId){
        role = em.find(Role.class, roleId);
        return role.getPrivileges();
    }
    
}
