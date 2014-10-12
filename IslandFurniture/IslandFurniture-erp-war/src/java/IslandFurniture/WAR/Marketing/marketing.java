/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.Marketing;

import IslandFurniture.EJB.Marketing.ManageMarketingBeanLocal;
import IslandFurniture.Entities.MembershipTier;
import IslandFurniture.Entities.PromotionCampaign;
import IslandFurniture.Entities.Staff;
import IslandFurniture.StaticClasses.UtilityBeanLocal;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;

/**
 *
 * @author James
 */
@SessionScoped
@ManagedBean(name = "marketing")
public class marketing implements Serializable {

    @EJB
    ManageMarketingBeanLocal mbean;

    @EJB
    public UtilityBeanLocal ubean;

    public String error_msg = "";

    public String success_msg = "";

    public PromotionCampaign newPromotionCampaign;

    public Staff currentUser;

    public List<MembershipTier> membershipTiers;

    public PromotionCampaign.campaignGoal[] campaignGoals;

    public List<PromotionCampaign> promotionCampaigns;

    public List<PromotionCampaign> getPromotionCampaigns() {
        return promotionCampaigns;
    }

    public void setPromotionCampaigns(List<PromotionCampaign> promotionCampaigns) {
        this.promotionCampaigns = promotionCampaigns;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public String getSuccess_msg() {
        return success_msg;
    }

    public void setSuccess_msg(String success_msg) {
        this.success_msg = success_msg;
    }

    public PromotionCampaign.campaignGoal[] getCampaignGoals() {
        return PromotionCampaign.campaignGoal.values();
    }

    public Staff getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Staff currentUser) {
        this.currentUser = currentUser;
    }

    public List<MembershipTier> getMembershipTiers() {
        return membershipTiers;
    }

    public void setMembershipTiers(List<MembershipTier> membershipTiers) {
        this.membershipTiers = membershipTiers;
    }

    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        String username = (String) session.getAttribute("username");
        currentUser = ubean.getStaffByUsername(username);
        newPromotionCampaign = new PromotionCampaign();
        newPromotionCampaign.setTitle("");
        newPromotionCampaign.setCountryOffice(ubean.getCountryOffice(currentUser.getPlant()));
        membershipTiers = ubean.getAllTiers();
        newPromotionCampaign.setValidFrom(Calendar.getInstance());
        newPromotionCampaign.setValidUntil(Calendar.getInstance());
        setPromotionCampaigns(mbean.getCampaigns(ubean.getCountryOffice(currentUser.getPlant())));

    }

    public PromotionCampaign getNewPromotionCampaign() {
        return newPromotionCampaign;
    }

    public void setNewPromotionCampaign(PromotionCampaign newPromotionCampaign) {
        this.newPromotionCampaign = newPromotionCampaign;
    }

    public marketing() {
    }

    public void newpromo(ActionEvent event) {
        try {
            mbean.CommitNewCampaign(newPromotionCampaign);
        } catch (Exception ex) {
            error_msg = "ERROR:" + ex.getMessage();
            System.out.println("(Marketing) Failed Campaign Creation !");
            return;
        }
        success_msg = "Success: Started Campaign:" + newPromotionCampaign.getTitle();

        init();
        System.out.println("(Marketing) New Promotion Campaign");

    }

}
