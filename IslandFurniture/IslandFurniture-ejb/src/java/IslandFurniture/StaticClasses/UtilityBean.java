/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.StaticClasses;

import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.MembershipTier;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Staff;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author James
 */
@Stateless
public class UtilityBean implements UtilityBeanLocal {

    @PersistenceContext(unitName = "IslandFurniture")
    private EntityManager em;

    @Override
    public Staff getStaffByUsername(String username) {
        Query q = em.createQuery("Select s from Staff s where s.username=:un");
        q.setParameter("un", username);
        return (Staff) (q.getResultList().get(0));

    }

    public void persist(Object object) {
        em.persist(object);
    }

    @Override
    public CountryOffice getCountryOffice(Plant plant) {

        if (plant instanceof CountryOffice) {
            return (CountryOffice) plant;
        }
        Query q = em.createQuery("select p from CountryOffice p where p.country=:c");
        q.setParameter("c", plant.getCountry());

        return (CountryOffice) q.getResultList().get(0);

    }
    @Override
    public List<MembershipTier> getAllTiers(){
       Query q=em.createQuery("SELECT mt from MembershipTier");
       return ((List<MembershipTier>) q.getResultList());
    }
    
    
    
}
