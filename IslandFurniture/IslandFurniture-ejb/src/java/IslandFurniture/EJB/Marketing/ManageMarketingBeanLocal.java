/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.Marketing;

import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.PromotionCampaign;
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
}
