/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.CustomerWebService;

import IslandFurniture.Entities.CountryOffice;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Stateless
public class ManageLocalizationBean implements ManageLocalizationBeanLocal {

    @PersistenceContext(unitName = "IslandFurniture")
    private EntityManager em;

    @Override
    public boolean isValidCoCode(String coCode) {
        if (this.findCoByCode(coCode) == null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public CountryOffice findCoByCode(String coCode) {
        try {
            Query q = em.createQuery("SELECT co FROM CountryOffice co WHERE co.urlCode = :code");
            q.setParameter("code", coCode);

            return (CountryOffice) q.getSingleResult();
        } catch (NoResultException nrex) {
            return null;
        }
    }

}
