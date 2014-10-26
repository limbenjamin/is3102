/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.DataLoading;

import IslandFurniture.EJB.CustomerWebService.ManageMemberAuthenticationBeanLocal;
import IslandFurniture.EJB.ITManagement.ManageOrganizationalHierarchyBeanLocal;
import IslandFurniture.EJB.Manufacturing.StockManagerLocal;
import IslandFurniture.EJB.OperationalCRM.ManageMarketingBean;
import IslandFurniture.EJB.OperationalCRM.ManageMarketingBeanLocal;
import IslandFurniture.EJB.OperationalCRM.ManageMembershipLocal;
import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.Customer;
import IslandFurniture.Entities.MembershipTier;
import IslandFurniture.Entities.PromotionCampaign;
import IslandFurniture.Entities.PromotionDetailByProductCategory;
import IslandFurniture.Entities.Redemption;
import IslandFurniture.Entities.Voucher;
import IslandFurniture.StaticClasses.QueryMethods;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.REQUIRED;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Benjamin
 */
@Stateless
public class LoadCustomerAndVoucherBean implements LoadCustomerAndVoucherBeanRemote {

    @PersistenceContext(unitName = "IslandFurniture")
    private EntityManager em;

    @EJB
    ManageMembershipLocal mml;

    @EJB
    ManageMemberAuthenticationBeanLocal mmabl;

    @EJB
    ManageOrganizationalHierarchyBeanLocal mohb;

    @EJB
    ManageMarketingBeanLocal mmb;
    


    private MembershipTier addMembershipTier(String title) {
        MembershipTier tier = QueryMethods.findMembershipTierByTitle(em, title);

        if (tier == null) {
            tier = new MembershipTier();
            tier.setTitle(title);
            em.persist(tier);
        }

        return tier;
    }

    @Override
    @TransactionAttribute(REQUIRED)
    public boolean loadSampleData(){
        // Create Membership Tiers
        MembershipTier bronze = this.addMembershipTier("Bronze");
        MembershipTier silver = this.addMembershipTier("Silver");
        MembershipTier Gold = this.addMembershipTier("Gold");

        // Load Promotion for bronze membershiptier
        PromotionCampaign pc = new PromotionCampaign();
        pc.setTitle("Bronze Membership Discount 5%");
        pc.setCountryOffice(null); //global
        Calendar now = Calendar.getInstance();
        now.add(Calendar.YEAR, 999);
        pc.setValidUntil(now);
        pc.setValidFrom(Calendar.getInstance());
        PromotionDetailByProductCategory pdbpc = new PromotionDetailByProductCategory();
        pdbpc.setId(-1L);        
        pdbpc.setPercentageDiscount(0.05);
        pdbpc.setApplicablePlant(null);
        pdbpc.setUsageCount(Integer.MAX_VALUE);
        pdbpc.setCategory(null);
        pdbpc.setMembershiptier(bronze);
        pc.getPromotionDetails().add(pdbpc);
        pdbpc.setPromotionCampaign(pc);

        try {
            mmb.CommitNewCampaign(pc);
        } catch (Exception ex) {
            Logger.getLogger(LoadCustomerAndVoucherBean.class.getName()).log(Level.SEVERE, null, ex);
        }


        // Load Promotion for silver membershiptier
        pc = new PromotionCampaign();
        pc.setTitle("Silver Membership Discount 10%");
        pc.setCountryOffice(null); //global
        now = Calendar.getInstance();
        now.add(Calendar.YEAR, 999);
        pc.setValidUntil(now);
        pc.setValidFrom(Calendar.getInstance());
        pdbpc = new PromotionDetailByProductCategory();
        pdbpc.setId(-1L);
        pdbpc.setPercentageDiscount(0.1);
        pdbpc.setApplicablePlant(null);
        pdbpc.setUsageCount(Integer.MAX_VALUE);
        pdbpc.setCategory(null);
        pdbpc.setMembershiptier(silver);
        pc.getPromotionDetails().add(pdbpc);
        pdbpc.setPromotionCampaign(pc);

        try {
            mmb.CommitNewCampaign(pc);
        } catch (Exception ex) {
            Logger.getLogger(LoadCustomerAndVoucherBean.class.getName()).log(Level.SEVERE, null, ex);
        }


        // Load Promotion for gold membershiptier
        pc = new PromotionCampaign();
        pc.setTitle("Gold Membership Discount 15%");
        pc.setCountryOffice(null); //global
        now = Calendar.getInstance();
        now.add(Calendar.YEAR, 999);
        pc.setValidUntil(now);
        pc.setValidFrom(Calendar.getInstance());
        pdbpc = new PromotionDetailByProductCategory();
        pdbpc.setId(-1L);
        pdbpc.setPercentageDiscount(0.15);
        pdbpc.setApplicablePlant(null);
        pdbpc.setUsageCount(Integer.MAX_VALUE);
        pdbpc.setCategory(null);
        pdbpc.setMembershiptier(Gold);
        pc.getPromotionDetails().add(pdbpc);
        pdbpc.setPromotionCampaign(pc);

        try {
            mmb.CommitNewCampaign(pc);
        } catch (Exception ex) {
            Logger.getLogger(LoadCustomerAndVoucherBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Create Customers
        mmabl.createCustomerAccountNoEmail("martha@limbenjamin.com", "pass", "Martha R. Coffman", "214-814-6054", "579 Traction Street Greenville, SC 29601", "15-06-1989");
        mmabl.createCustomerAccountNoEmail("stella@limbenjamin.com", "pass", "Stella J. Collier", "925-940-7302", "2901 Brown Street, CA 94612", "11-02-1958");
        mmabl.createCustomerAccountNoEmail("craig@limbenjamin.com", "pass", "Craig H. Cotter", "210-967-1644", "2703 Bell Street San Antonio, TX 78233", "11-04-1985");
        Customer c = mmabl.getCustomer("martha@limbenjamin.com");
        mmabl.setCustomerLoyaltyCardId(c, "B00DBD31");
        c = mmabl.getCustomer("stella@limbenjamin.com");
        mmabl.setCustomerLoyaltyCardId(c, "92CEA65D");
        mmabl.setCustomerMembershipTier(c, "Silver");
        c = mmabl.getCustomer("craig@limbenjamin.com");
        mmabl.setCustomerLoyaltyCardId(c, "2234A75D");
        mmabl.setCustomerMembershipTier(c, "Gold");
        
        Voucher v = new Voucher();
        CountryOffice co = mohb.findCountryOfficeByName("Singapore");
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.YEAR, 2015);
        ca.set(Calendar.MONTH, 0);
        ca.set(Calendar.DATE, 1);
        v.setCashValue(10);
        v.setCountryOffice(co);
        v.setExpiryDate(ca);
        em.persist(v);
        em.flush();
        Redemption r;
        for (int i = 0; i < 20; i++) {
            r = new Redemption();
            r.setClaimed(Boolean.FALSE);
            r.setCustomer(c);
            r.setRedeemableItem(v);
            em.persist(r);
        }
        return true;
    }

}
