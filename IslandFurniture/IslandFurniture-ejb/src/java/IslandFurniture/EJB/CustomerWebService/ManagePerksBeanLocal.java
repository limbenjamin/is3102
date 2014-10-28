/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.CustomerWebService;

import IslandFurniture.Entities.Customer;
import IslandFurniture.Entities.PromotionDetail;
import IslandFurniture.Entities.PromotionDetailByProduct;
import IslandFurniture.Entities.PromotionDetailByProductCategory;
import IslandFurniture.Entities.PromotionDetailByProductSubCategory;
import java.util.List;

/**
 *
 * @author Zee
 */
public interface ManagePerksBeanLocal {

    List<PromotionDetail> getPromotionDetailsByTier(Customer customer);
    
    List<PromotionDetailByProduct> getPDP(Customer customer);
    
    PromotionDetail getPerk(Long id);
    
    List<PromotionDetailByProductCategory> getPDPC(Customer customer);
    
    List<PromotionDetailByProductSubCategory> getPDPSC(Customer customer);
    
}
