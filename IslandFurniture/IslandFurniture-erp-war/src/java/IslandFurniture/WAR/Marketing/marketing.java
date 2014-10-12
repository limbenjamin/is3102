/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.Marketing;

import IslandFurniture.Entities.MembershipTier;
import IslandFurniture.Entities.PromotionCampaign;
import IslandFurniture.Entities.Staff;
import IslandFurniture.StaticClasses.UtilityBeanLocal;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.bean.SessionScoped;
import javax.servlet.http.HttpSession;

/**
 *
 * @author James
 */
@Named(value = "marketing")
@SessionScoped
public class marketing {

    
    
    @EJB
    public UtilityBeanLocal ubean;

    public PromotionCampaign newPromotionCampaign = new PromotionCampaign();

    public Staff currentUser;

    public List<MembershipTier> membershipTiers;
    
    public PromotionCampaign.campaignGoal[] campaignGoals;

    public PromotionCampaign.campaignGoal[] getCampaignGoals() {
        return PromotionCampaign.campaignGoal.values();
    }

    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        String username = (String) session.getAttribute("username");
        currentUser = ubean.getStaffByUsername(username);
        newPromotionCampaign.setCountryOffice(ubean.getCountryOffice(currentUser.getPlant()));
        membershipTiers=ubean.getAllTiers();

    }

    public PromotionCampaign getNewPromotionCampaign() {
        return newPromotionCampaign;
    }

    public void setNewPromotionCampaign(PromotionCampaign newPromotionCampaign) {
        this.newPromotionCampaign = newPromotionCampaign;
    }

    /**
     * Creates a new instance of marketing
     */
    public marketing() {
    }

}
