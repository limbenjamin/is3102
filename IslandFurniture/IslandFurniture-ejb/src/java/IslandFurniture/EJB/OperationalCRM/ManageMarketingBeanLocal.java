/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.OperationalCRM;

import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.Customer;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.PromotionCampaign;
import IslandFurniture.Entities.PromotionCoupon;
import IslandFurniture.Entities.PromotionDetail;
import IslandFurniture.Entities.Stock;
import IslandFurniture.Entities.Store;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author James
 */
@Local
public interface ManageMarketingBeanLocal {

    public void CommitNewCampaign(PromotionCampaign pc) throws Exception;

    public List<PromotionCampaign> getCampaigns(CountryOffice co);

    public List<Plant> getStoresInCo(CountryOffice co);

    public List<Stock> getProductInCo(CountryOffice co);

    public double getPrice(Stock s, CountryOffice co);

    public int EmailCustomer(PromotionCampaign pc, CountryOffice co);

    public double calcDiscount(Stock s, CountryOffice co, PromotionDetail pd);

    public void expand_promotion(PromotionDetail pd, PromotionCoupon pc);

    public HashMap<String, Object> getDiscountedPrice(Stock s,Store ss, Customer c, List<PromotionCoupon> couponLists);

    public HashMap<String, Object> getDiscountedPrice(Stock s,Store ss, Customer c);
    
    public PromotionCoupon getCouponFromID(Long id);
}
