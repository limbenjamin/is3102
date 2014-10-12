/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.Marketing;

import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.PromotionCampaign;
import IslandFurnitures.DataStructures.JDataTable;
import java.util.List;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author James
 */
@Stateful
public class ManageMarketingBean implements ManageMarketingBeanLocal {

    @PersistenceContext(unitName = "IslandFurniture")
    private EntityManager em;

    @Override
    public void CommitNewCampaign(PromotionCampaign pc) throws Exception {
        em.persist(pc);
    }

    public void persist(Object object) {
        em.persist(object);
    }

    @Override
    public List<PromotionCampaign> getCampaigns(CountryOffice co) {
        Query q = em.createQuery("select pc from PromotionCampaign pc where pc.countryOffice=:co ORDER BY PC.validUntil desc");
        q.setParameter("co", co);
        return (List<PromotionCampaign>) q.getResultList();

    }
}
