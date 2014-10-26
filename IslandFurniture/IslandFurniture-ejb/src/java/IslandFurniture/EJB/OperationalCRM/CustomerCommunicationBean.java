/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.OperationalCRM;

import IslandFurniture.Entities.Country;
import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.CustChatMessage;
import IslandFurniture.Entities.CustChatThread;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Benjamin
 */
@Stateless
public class CustomerCommunicationBean implements CustomerCommunicationBeanLocal {
    
    @PersistenceContext
    EntityManager em;
    
    private CustChatMessage ccm;
    private CustChatThread cct;
    private List<CustChatThread> activeCCT;
    private List<CustChatMessage> listCCM;
    
    @Override
    public List<CustChatThread> getActiveThreadFromCountry(CountryOffice co){
        Query query = em.createQuery("SELECT c FROM CustChatThread c WHERE c.isActive=:True AND c.countryOffice=:co");
        query.setParameter("co", co);
        return query.getResultList();
    }
    
    @Override
    public Long createAnonymousThread(CountryOffice co){
        cct = new CustChatThread();
        cct.setCountryOffice(co);
        cct.setTitle("Anonymous customer");
        cct.setIsActive(Boolean.TRUE);
        em.persist(cct);
        return cct.getId();
    }
    
    @Override
    public void postMessage(Long threadId, String content, Boolean isStaff){
        
    }
    
}
