/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.CustomerWebService;

import IslandFurniture.Entities.CountryOffice;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Zee
 */
@Stateless
public class ManageCountryBean implements ManageCountryBeanLocal {
    
    @PersistenceContext
    EntityManager em;
    
    @Resource SessionContext ctx;
    CountryOffice countryOffice;

    @Override
    public CountryOffice getCountry(Long id) {
        Query query = em.createQuery("SELECT c FROM CountryOffice c where c.id=:id");
        query.setParameter("id", id);
        return (CountryOffice) query.getSingleResult();
    }
    
    @Override
    public List<CountryOffice> getCountries() {
        Query query = em.createQuery("SELECT c FROM CountryOffice c");
        return (List<CountryOffice>) query.getResultList();
    }
}
