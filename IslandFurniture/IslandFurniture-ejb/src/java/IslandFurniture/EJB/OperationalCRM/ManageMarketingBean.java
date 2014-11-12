/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.OperationalCRM;

import IslandFurniture.EJB.SalesPlanning.CurrencyManagerLocal;
import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.Customer;
import IslandFurniture.Entities.FurnitureModel;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.PromotionCampaign;
import IslandFurniture.Entities.PromotionCoupon;
import IslandFurniture.Entities.PromotionDetail;
import IslandFurniture.Entities.PromotionDetailByProduct;
import IslandFurniture.Entities.PromotionDetailByProductCategory;
import IslandFurniture.Entities.PromotionDetailByProductSubCategory;
import IslandFurniture.Entities.Stock;
import IslandFurniture.Entities.StockSupplied;
import IslandFurniture.Entities.Store;
import IslandFurniture.StaticClasses.SendEmailByPost;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.codec.binary.Base64;

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

        System.out.println("Emailing customers... ");

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

        for (Customer cc : (Customer[]) c.stream().distinct().toArray()) {
            try {
                SendEmailByPost.sendEmail("marketing@islandfurniture.com", cc.getEmailAddress(), "Island Furniture Promotion:" + pc.getTitle(), pc.getRemark());
            } catch (Exception ex) {
            }
            // To send only 1 email for demo purposes, remove break to send to all customers
            break;
        }

        return c.stream().distinct().toArray().length;

    }

    @Override
    public PromotionCampaign CommitNewCampaign(PromotionCampaign pc) throws Exception {

        //clear the persistence cache and sync
        //JPA delete query
        Query q = em.createQuery("DELETE FROM PromotionCoupon zz where zz.promotionDetail.promotionCampaign=:pc");
        q.setParameter("pc", pc);
        q.executeUpdate();

        q = em.createQuery("DELETE FROM PromotionDetail pcd where pcd.promotionCampaign=:pc");
        q.setParameter("pc", pc);
        q.executeUpdate();

        q = em.createQuery("DELETE FROM PromotionCoupon pc where pc.promotionDetail.promotionCampaign=:pc");
        q.setParameter("pc", pc);
        q.executeUpdate();

        for (PromotionDetail pd : pc.getPromotionDetails()) {

            if (pd.getId() != null) {

                if (pd.getId() < 0) {
                    pd.setId(null);
                }
            }

            pd.setPromotionCampaign(pc);
//            em.merge(pd); 

            if (pd.getPromotionCoupons().size() > 0) {

                if (pd.getPromotionCoupons().get(0).getOneTimeUsage() == false) {
                    if (pd.getPromotionCoupons().get(0).getId() < 0) {
                        PromotionCoupon new_pc = pd.getPromotionCoupons().get(0);
                        new_pc.setPromotionDetail(pd);
                        new_pc.setId(null);
                        pd.getPromotionCoupons().clear();
                        pd.getPromotionCoupons().add(new_pc);
                        Query l = em.createQuery("DELETE FROM PromotionCoupon pc where pc.promotionDetail=:pd");
                        l.setParameter("pd", pd);
                        l.executeUpdate();
                        pd.getPromotionCoupons().get(0).setPromotionDetail(pd);
//                    em.merge(pd.getPromotionCoupons().get(0));
                    }
                } else {

                    for (int i = 0; i < pd.getPromotionCoupons().size(); i++) {
                        if (pd.getPromotionCoupons().get(i).getOneTimeUsage() == false && pd.getPromotionCoupons().get(i).getId() > 0) {
                            pd.getPromotionCoupons().get(i).setPromotionDetail(null);
                            pd.getPromotionCoupons().remove(pd.getPromotionCoupons().get(i));
                        }
                    }

                    if (pd.getPromotionCoupons().size() > pd.getUsageCount().intValue()) {
                        throw new Exception("You cannot set usagelimit to be less than the current available no of coupons !");
                    }

                    if (pd.getPromotionCoupons().get(0).getId() < 0) {
                        pd.getPromotionCoupons().remove(pd.getPromotionCoupons().get(0)); //just a signal

                        for (int i = pd.getPromotionCoupons().size() + 1; i <= pd.getUsageCount().intValue(); i++) {
                            System.out.println("Creating new Coupons No:" + i);
                            PromotionCoupon new_pc = new PromotionCoupon();
                            new_pc.setOneTimeUsage(true);
                            new_pc.setPromotionDetail(pd);
                            pd.getPromotionCoupons().add(new_pc);
//                        em.merge(new_pc);

                        }
                    }
                }

            }
        }

        em.merge(pc);

        try {
            em.flush();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }

        //Clean up
        Query l = em.createQuery("select pd from PromotionDetail pd where pd.promotionCampaign is null");
        if (l.getResultList().isEmpty()) {
            return pc;
        }
        for (PromotionDetail pd : (List<PromotionDetail>) l.getResultList()) {
            try {
                em.remove(pd);
            } catch (Exception ex) {
            }
        }

        return pc;

    }

    @Override
    public double getPrice(Stock s, CountryOffice co) {
        Query q = em.createQuery("select ss from StockSupplied ss where ss.stock=:s and ss.countryOffice=:co");
        q.setParameter("s", s);
        q.setParameter("co", co);
        try {
            return ((StockSupplied) (q.getResultList().get(0))).getPrice();
        } catch (Exception ex) {
            return 0.0;
        }
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

    @Override
    public void expand_promotion(PromotionDetail pd, PromotionCoupon pc) {
        if (pc.getOneTimeUsage() == true) {
            pd.getPromotionCoupons().remove(pc);
            pc.setPromotionDetail(null);
            System.out.println("expand_promotion(): Expanded" + pc);
        }
        if (pd.getUsageCount() < Integer.MAX_VALUE) {
            pd.setUsageCount((pd.getUsageCount() - 1));
        }
        System.out.println("expand_promotion(): Expanded" + pd.getPromotionCampaign() + " Count Now: " + pd.getUsageCount());

    }

    @Override
    public PromotionCoupon getCouponFromID(Long id) {

        return (em.find(PromotionCoupon.class, id));
    }

    //the no coupon version
    @Override
    public HashMap<String, Object> getDiscountedPrice(Stock s, Store ss, Customer c) {
        ArrayList<PromotionCoupon> n = new ArrayList<>();
        return (getDiscountedPrice(s, ss, c, n));
    }

    @Override
    public HashMap<String, Object> getDiscountedPrice(Stock s, Store ss, Customer c, List<PromotionCoupon> couponLists) {
        Query q = em.createQuery("select pcd from PromotionDetail pcd where (pcd.promotionCampaign.countryOffice is null or pcd.promotionCampaign.countryOffice=:co) and (pcd.membershiptier=:mt or pcd.membershiptier is null) and (pcd.applicablePlant is null or EXISTS(select s from Store s where s=:ss and (s.id=pcd.applicablePlant.id)))");
        if (c == null) {
            q.setParameter("mt", null);
        } else {
            q.setParameter("mt", c.getMembershipTier());
        }
        q.setParameter("ss", ss);
        q.setParameter("co", ss.getCountryOffice());
        double minprice = this.getPrice(s, ss.getCountryOffice());
        PromotionCoupon effective_coupon = null;
        HashMap<String, Object> returnobj = new HashMap<>();
        PromotionDetail successful_pd = null;

        for (PromotionDetail pd : (List<PromotionDetail>) q.getResultList()) {

            if (pd.getPromotionCoupons().size() > 0) {
                effective_coupon = null;

                for (PromotionCoupon pc : pd.getPromotionCoupons()) {
                    if (couponLists.contains(pc)) {
                        effective_coupon = pc;
                        break;
                    }
                }

                if (effective_coupon == null) {
                    continue; //Skip this since dont have coupon
                }

            } else {
                effective_coupon = null;
            }

            if (calcDiscount(s, ss.getCountryOffice(), pd) < minprice) {
                minprice = calcDiscount(s, ss.getCountryOffice(), pd);
                successful_pd = pd;
            }
        }

        returnobj.put("O_PRICE", Math.round(this.getPrice(s, ss.getCountryOffice()) * 100.0) / 100.0);
        returnobj.put("D_PRICE", Math.round(minprice * 100.0) / 100.0);
        if (effective_coupon != null) {
            returnobj.put("USED_COUPON", effective_coupon);
        }
        if (successful_pd != null) {
            returnobj.put("Successful_promotion", successful_pd);
        }

        return returnobj;

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
                    newprice = (1 - pd.getPercentageDiscount()) * op;
                }
                if (pdd.getAbsoluteDiscount() > 0) {
                    newprice = op - pd.getAbsoluteDiscount();
                }

            }
        } else if (pd instanceof PromotionDetailByProductCategory) {
            PromotionDetailByProductCategory pdbpc = (PromotionDetailByProductCategory) pd;
            if (s instanceof FurnitureModel) {
                FurnitureModel fm = (FurnitureModel) s;
                if (pdbpc.getCategory() == null || fm.getCategory().equals(pdbpc.getCategory())) {
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
                if (fm.getSubcategory().equals(pdbpsc.getCategory())) {
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

    @Override
    public String encodeCouponID(Long l) {

        if (l == null) {
            return null;
        }

        RC4 cipher = new RC4("ISLANDFURNITURE_81273798".getBytes());
        Base64 b64 = new Base64();

        Double dl = Math.log(l);
        String encrypted = b64.encodeToString(cipher.encrypt(ByteBuffer.allocate(Float.BYTES).putFloat(dl.floatValue()).array()));
        BigInteger x = new BigInteger(encrypted.getBytes());
        encrypted = x.toString();

        System.out.println("encrypted=" + encrypted);
        System.out.println("Decrypted=" + decodeCouponID(encrypted));
        return (encrypted);

    }

    @Override
    public Long decodeCouponID(String s) {

        if (s == null) {
            return null;
        }
        System.out.println("Attempting to decrypt:" + s);
        RC4 cipher = new RC4("ISLANDFURNITURE_81273798".getBytes());
        Base64 b64 = new Base64();
        
        BigInteger x = new BigInteger(s);
        s=new String(x.toByteArray());

        
        
        byte[] todecrypt = b64.decode(s);
        ByteBuffer buffer = ByteBuffer.allocate(Float.BYTES);
        buffer.put(cipher.decrypt(todecrypt)).flip();
        Float result = buffer.getFloat();
        Double dresult = Math.exp(result);
        result = dresult.floatValue();
        Long result_ID = result.longValue();
        System.out.println("Decrypted:" + result_ID);
        return result_ID;

    }

    public class RC4 {

        private final byte[] S = new byte[256];
        private final byte[] T = new byte[256];
        private final int keylen;

        public RC4(final byte[] key) {
            if (key.length < 1 || key.length > 256) {
                throw new IllegalArgumentException(
                        "key must be between 1 and 256 bytes");
            } else {
                keylen = key.length;
                for (int i = 0; i < 256; i++) {
                    S[i] = (byte) i;
                    T[i] = key[i % keylen];
                }
                int j = 0;
                byte tmp;
                for (int i = 0; i < 256; i++) {
                    j = (j + S[i] + T[i]) & 0xFF;
                    tmp = S[j];
                    S[j] = S[i];
                    S[i] = tmp;
                }
            }
        }

        public byte[] encrypt(final byte[] plaintext) {
            final byte[] ciphertext = new byte[plaintext.length];
            int i = 0, j = 0, k, t;
            byte tmp;
            for (int counter = 0; counter < plaintext.length; counter++) {
                i = (i + 1) & 0xFF;
                j = (j + S[i]) & 0xFF;
                tmp = S[j];
                S[j] = S[i];
                S[i] = tmp;
                t = (S[i] + S[j]) & 0xFF;
                k = S[t];
                ciphertext[counter] = (byte) (plaintext[counter] ^ k);
            }

            return ciphertext;
        }

        public byte[] decrypt(final byte[] ciphertext) {
            return encrypt(ciphertext);
        }
    }
}
