/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.Marketing;

import IslandFurniture.EJB.SalesPlanning.CurrencyManagerLocal;
import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.Customer;
import IslandFurniture.Entities.FurnitureModel;
import IslandFurniture.Entities.MembershipTier;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.PromotionCampaign;
import IslandFurniture.Entities.PromotionCoupon;
import IslandFurniture.Entities.PromotionDetail;
import IslandFurniture.Entities.PromotionDetailByProduct;
import IslandFurniture.Entities.PromotionDetailByProductCategory;
import IslandFurniture.Entities.PromotionDetailByProductSubCategory;
import IslandFurniture.Entities.Stock;
import IslandFurniture.Entities.StockSupplied;
import IslandFurniture.StaticClasses.SendEmailByPost;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
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

    @EJB
    public CurrencyManagerLocal cmbean;

    @Override
    public int EmailCustomer(PromotionCampaign pc, CountryOffice co) {
        ArrayList<Customer> c = new ArrayList();

        for (PromotionDetail pd : pc.getPromotionDetails()) {

            if (pd.getMembershiptier() == null) {
                Query l = em.createQuery("select c from Customer c where c.country=:c");
                l.setParameter("c", co.getCountry());
                c.addAll((List<Customer>) l.getResultList());
            } else {
                Query l = em.createQuery("select c from Customer c where c.country=:c and c.membershipTier=:mt");
                l.setParameter("c", co.getCountry());
                l.setParameter("mt", pd.getMembershiptier());
                c.addAll((List<Customer>) l.getResultList());

            }

        }
        
        c.stream().distinct().forEach(cc->{
            try {
                SendEmailByPost.sendEmail("marketing@islandfurniture.com", cc.getEmailAddress(), "Island Furniture Promotion:" + pc.getTitle(), pc.getRemark());
            } catch (Exception ex) {
            }
        });


        return c.stream().distinct().toArray().length;

    }

    @Override
    public void CommitNewCampaign(PromotionCampaign pc) throws Exception {

        //JPA delete query
        Query q = em.createQuery("DELETE FROM PromotionDetail pcd where pcd.promotionCampaign=:pc");
        q.setParameter("pc", pc);
        q.executeUpdate();

        for (PromotionDetail pd : pc.getPromotionDetails()) {

            if (pd.getId() < 0) {
                pd.setId(null);
            }

            pd.setPromotionCampaign(pc);
            em.merge(pd);

            if (pd.getPromotionCoupons().size() > 0) {
                if (pd.getPromotionCoupons().get(0).getOneTimeUsage() == false) {
                    pd.getPromotionCoupons().get(0).setPromotionDetail(pd);
                    Query l = em.createQuery("DELETE FROM PromotionCoupon pc where pc.promotionDetail=:pd");
                    l.setParameter("pd", pd);
                    l.executeUpdate();
                    em.merge(pd.getPromotionCoupons().get(0));
                } else {

                    Query l = em.createQuery("SELECT pc FROM PromotionCoupon pc where pc.promotionDetail=:pd");
                    l.setParameter("pd", pd);

                    if (l.getResultList().size() > pd.getUsageCount().intValue()) {
                        throw new Exception("You cannot set usagelimit to be less than the current available no of coupons !");
                    }

                    for (int i = l.getResultList().size(); i <= pd.getUsageCount().intValue(); i++) {
                        PromotionCoupon new_pc = new PromotionCoupon();
                        new_pc.setOneTimeUsage(false);
                        new_pc.setPromotionDetail(pd);
                        pd.getPromotionCoupons().add(new_pc);
                        em.merge(new_pc);

                    }
                }

            }

        }

        em.merge(pc);

        //Clean up
        Query l = em.createQuery("select pd from PromotionDetail pd where pd.promotionCampaign is null");
        if (l.getResultList().isEmpty()) {
            return;
        }
        for (PromotionDetail pd : (List<PromotionDetail>) l.getResultList()) {
            try {
                em.remove(pd);
            } catch (Exception ex) {
            }
        }

    }

    @Override
    public double getPrice(Stock s, CountryOffice co) {
        Query q = em.createQuery("select ss from StockSupplied ss where ss.stock=:s and ss.countryOffice=:co");
        q.setParameter("s", s);
        q.setParameter("co", co);
        return ((StockSupplied) (q.getResultList().get(0))).getPrice();
    }

    @Override
    public List<Plant> getStoresInCo(CountryOffice co) {
        Query q = em.createQuery("select s from Store s where s.countryOffice=:co");
        q.setParameter("co", co);
        return (List<Plant>) q.getResultList();

    }

    @Override
    public List<Stock> getProductInCo(CountryOffice co) {
        Query q = em.createQuery("select s from Stock s where EXISTS(SELECT ss from StockSupplied ss where ss.stock.id=s.id and ss.countryOffice=:co)");
        q.setParameter("co", co);
        return (List<Stock>) q.getResultList();
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

    public String whatpromotiondetail(PromotionDetail pd) {
        return pd.getClass().getName();
    }

    public double getDiscountedPrice(Stock s, CountryOffice co, Customer c) {
        Query q = em.createQuery("select pcd from PromotionDetail pcd where (pcd.membershiptier=:mt or pcd.membershiptier is null) and EXISTS(select s from Store s where s.countryOffice=:co and s.id=pcd.applicablePlant.id)");
        q.setParameter("mt", c.getMembershipTier());
        q.setParameter("co", co);
        double minprice = this.getPrice(s, co);

        for (PromotionDetail pd : (List<PromotionDetail>) q.getResultList()) {
            if (calcDiscount(s, co, pd) < minprice) {
                minprice = calcDiscount(s, co, pd);
            }
        }

        return minprice;

    }

    @Override
    public double calcDiscount(Stock s, CountryOffice co, PromotionDetail pd) {
        Double op = this.getPrice(s, co);
        Double newprice = op;
        //agnostic about membershipTier

        if (pd.getPromotionCampaign() == null) {
            return op;
        }

        if (pd.getUsageCount() <= 0 || pd.getPromotionCampaign().getLocked() || pd.getPromotionCampaign().getExpired()) {
            return (op);
        }

        if (pd instanceof PromotionDetailByProduct) {
            PromotionDetailByProduct pdd = (PromotionDetailByProduct) pd;
            if (pdd.getStock().equals(s)) {
                if (pdd.getPercentageDiscount() > 0) {
                    newprice = (1 - pdd.getPercentageDiscount()) * op;
                }
                if (pdd.getAbsoluteDiscount() > 0) {
                    newprice = op - pdd.getAbsoluteDiscount();
                }

            }
        } else if (pd instanceof PromotionDetailByProductCategory) {
            PromotionDetailByProductCategory pdbpc = (PromotionDetailByProductCategory) pd;
            if (s instanceof FurnitureModel) {
                FurnitureModel fm = (FurnitureModel) s;
                if (fm.getCategory().equals(pdbpc.getCategory())) {
                    if (pd.getPercentageDiscount() > 0) {
                        newprice = (1 - pd.getPercentageDiscount()) * op;
                    }
                    if (pd.getAbsoluteDiscount() > 0) {
                        newprice = op - pd.getAbsoluteDiscount();
                    }
                }
            }

        } else if (pd instanceof PromotionDetailByProductSubCategory) {
            PromotionDetailByProductSubCategory pdbpsc = (PromotionDetailByProductSubCategory) pd;
            if (s instanceof FurnitureModel) {
                FurnitureModel fm = (FurnitureModel) s;
                if (fm.getCategory().equals(pdbpsc.getCategory())) {
                    if (pd.getPercentageDiscount() > 0) {
                        newprice = (1 - pd.getPercentageDiscount()) * op;
                    }
                    if (pd.getAbsoluteDiscount() > 0) {
                        newprice = op - pd.getAbsoluteDiscount();
                    }
                }
            }
        }

        return newprice;

    }

    public double getDiscountedPrice(Stock s, CountryOffice co) {
        Query q = em.createQuery("select pcd from PromotionDetail pcd where EXISTS(select s from Store s where s.countryOffice=:co and s.id=pcd.applicablePlant.id)");
        q.setParameter("co", co);
        double minprice = this.getPrice(s, co);

        for (PromotionDetail pd : (List<PromotionDetail>) q.getResultList()) {
            if (calcDiscount(s, co, pd) < minprice) {
                minprice = calcDiscount(s, co, pd);
            }
        }

        return minprice;

    }

}
