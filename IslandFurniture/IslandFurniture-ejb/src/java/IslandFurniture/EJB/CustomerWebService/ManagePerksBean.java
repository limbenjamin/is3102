/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.CustomerWebService;

import IslandFurniture.EJB.SalesPlanning.CurrencyManagerLocal;
import IslandFurniture.Entities.Customer;
import IslandFurniture.Entities.MembershipTier;
import IslandFurniture.Entities.PromotionDetail;
import IslandFurniture.Entities.PromotionDetailByProduct;
import IslandFurniture.Entities.PromotionDetailByProductCategory;
import IslandFurniture.Entities.PromotionDetailByProductSubCategory;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Zee
 */
@Stateless
public class ManagePerksBean implements ManagePerksBeanLocal {

    @PersistenceContext(unitName = "IslandFurniture")
    private EntityManager em;

    @EJB
    private CurrencyManagerLocal cmbean;
    
    @Override
    public List<PromotionDetail> getPromotionDetailsByTier(Customer customer) {
        MembershipTier tier = customer.getMembershipTier();
        Query q = em.createQuery("select pcd from PromotionDetail pcd where pcd.membershiptier=:mt or pcd.membershiptier is null");
        if (tier == null) {
            q.setParameter("mt", null);
        } else {
            q.setParameter("mt", tier);
        }
        return (List<PromotionDetail>) q.getResultList();
    }
    
    @Override
    public PromotionDetail getPerk(Long id) {
        return (PromotionDetail) em.find(PromotionDetail.class, id);
    }
    
    @Override
    public List<PromotionDetailByProduct> getPDP(Customer customer) {
        MembershipTier tier = customer.getMembershipTier();
        Query query = em.createQuery("select pdpc from PromotionDetailByProduct pdpc, PromotionDetail pcd where "
                + "(pcd.membershiptier=:mt or pcd.membershiptier is null)"
                + "and (pcd.id=pdpc.id)");
        if (tier == null) {
            query.setParameter("mt", null);
        } else {
            query.setParameter("mt", tier);
        }
        return (List<PromotionDetailByProduct>) query.getResultList();
    }   
    
    @Override
    public List<PromotionDetailByProductCategory> getPDPC(Customer customer) {
        MembershipTier tier = customer.getMembershipTier();
        Query query = em.createQuery("select pdpc from PromotionDetailByProductCategory pdpc, PromotionDetail pcd where "
                + "(pcd.membershiptier=:mt or pcd.membershiptier is null) "
                + "and (pcd.id=pdpc.id)");
        if (tier == null) {
            query.setParameter("mt", null);
        } else {
            query.setParameter("mt", tier);
        }
        return (List<PromotionDetailByProductCategory>) query.getResultList();
    }
    
    @Override
    public List<PromotionDetailByProductSubCategory> getPDPSC(Customer customer) {
        MembershipTier tier = customer.getMembershipTier();
        Query query = em.createQuery("select pdpc from PromotionDetailByProductSubCategory pdpc, PromotionDetail pcd where "
                + "(pcd.membershiptier=:mt or pcd.membershiptier is null) "
                + "and (pcd.id=pdpc.id)");
        if (tier == null) {
            query.setParameter("mt", null);
        } else {
            query.setParameter("mt", tier);
        }
        return (List<PromotionDetailByProductSubCategory>) query.getResultList();
    }    
}
