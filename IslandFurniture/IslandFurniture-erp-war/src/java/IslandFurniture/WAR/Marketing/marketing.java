/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.Marketing;

import IslandFurniture.EJB.Marketing.ManageMarketingBeanLocal;
import IslandFurniture.Entities.MembershipTier;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.PromotionCampaign;
import IslandFurniture.Entities.PromotionCoupon;
import IslandFurniture.Entities.PromotionDetail;
import IslandFurniture.Entities.PromotionDetailByProduct;
import IslandFurniture.Entities.PromotionDetailByProductCategory;
import IslandFurniture.Entities.PromotionDetailByProductSubCategory;
import IslandFurniture.Entities.Staff;
import IslandFurniture.Entities.Stock;
import IslandFurniture.Enums.FurnitureCategory;
import IslandFurniture.Enums.FurnitureSubcategory;
import IslandFurniture.StaticClasses.UtilityBeanLocal;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpSession;
import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.context.RequestContext;
import org.primefaces.model.menu.MenuItem;

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

    private Long idTrick = 1L;

    public String success_msg = "";

    public PromotionCampaign newPromotionCampaign;

    public Staff currentUser;

    public List<MembershipTier> membershipTiers;

    public PromotionCampaign.campaignGoal[] campaignGoals;

    public List<PromotionCampaign> promotionCampaigns;

    public Calendar currentDate;

    private PromotionCampaign currentEdit = null;

    public List<Plant> listOfStores;

    public List<Stock> listOfStocks;

    public class dummy {

        private String s = "";

        public String getS() {
            return s;
        }

        public void setS(String s) {
            this.s = s;
        }

    }

    public HashMap<PromotionDetail, dummy> hack = new HashMap<>();

    public HashMap<PromotionDetail, dummy> getHack() {
        return hack;
    }

    public void setHack(HashMap<PromotionDetail, dummy> hack) {
        this.hack = hack;
    }

    public dummy gHack(PromotionDetail pd) {

        if (hack.get(pd) == null) {
            dummy d = new dummy();
            d.setS(pd.getCouponStatus());

            hack.put(pd, d);
        }
        return (hack.get(pd));
    }

    public List<Stock> getListOfStocks() {
        return listOfStocks;
    }

    public void setListOfStocks(List<Stock> listOfStocks) {
        this.listOfStocks = listOfStocks;
    }

    public List<Plant> getListOfStores() {
        return listOfStores;
    }

    public void setListOfStores(List<Plant> listOfStores) {
        this.listOfStores = listOfStores;
    }

    public PromotionCampaign getCurrentEdit() {
        return currentEdit;
    }

    public void setCurrentEdit(PromotionCampaign currentEdit) {
        this.currentEdit = currentEdit;
    }

    public Calendar getCurrentDate() {
        return Calendar.getInstance();
    }

    public void setCurrentDate(Calendar currentDate) {
        this.currentDate = currentDate;
    }

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

    public FurnitureCategory[] getFurnitureCategory() {
        return FurnitureCategory.values();
    }

    public FurnitureSubcategory[] getFurnitureSubCategory() {
        return FurnitureSubcategory.values();
    }

    public Staff getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Staff currentUser) {
        this.currentUser = currentUser;
    }

    public List<MembershipTier> getMembershipTiers() {
        return membershipTiers;
        //
    }

    public void setMembershipTiers(List<MembershipTier> membershipTiers) {
        this.membershipTiers = membershipTiers;
    }

    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        String username = (String) session.getAttribute("username");
        System.out.println("PromotionManager(): " + username);
        currentUser = ubean.getStaffByUsername(username);
        System.out.println("PromotionManager() Country Office: " + ubean.getCountryOffice(currentUser.getPlant()));

        newPromotionCampaign = new PromotionCampaign();
        newPromotionCampaign.setTitle("");
        newPromotionCampaign.setCountryOffice(ubean.getCountryOffice(currentUser.getPlant()));
        membershipTiers = ubean.getAllTiers();
        newPromotionCampaign.setValidFrom(Calendar.getInstance());
        newPromotionCampaign.setValidUntil(Calendar.getInstance());
        setPromotionCampaigns(mbean.getCampaigns(ubean.getCountryOffice(currentUser.getPlant())));
        listOfStores = mbean.getStoresInCo(ubean.getCountryOffice(currentUser.getPlant()));
        listOfStocks = mbean.getProductInCo(ubean.getCountryOffice(currentUser.getPlant()));

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
        error_msg = "";
        success_msg = "";

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

    public void newpromodetail(ActionEvent event) {
        error_msg = "";
        success_msg = "";
        MenuItem mi = (MenuItem) event.getComponent();

        switch ((String) mi.getValue()) {
            case "New By Products":
                PromotionDetailByProduct pdbp = new PromotionDetailByProduct();
                pdbp.setId(-idTrick++);
                currentEdit.getPromotionDetails().add(pdbp);
                pdbp.setPromotionCampaign(currentEdit);
                success_msg = "Campaign: " + currentEdit.getTitle() + " New Promotion By Specified Product";
                break;
            case "New By Products Category":
                PromotionDetailByProductCategory pdbpc = new PromotionDetailByProductCategory();
                pdbpc.setId(-idTrick++);
                pdbpc.setPromotionCampaign(currentEdit);

                currentEdit.getPromotionDetails().add(pdbpc);
                success_msg = "Campaign: " + currentEdit.getTitle() + " New Promotion By Product Category";
                break;
            case "New By Products Sub Category":
                PromotionDetailByProductSubCategory pdbpsc = new PromotionDetailByProductSubCategory();
                pdbpsc.setId(-idTrick++);
                pdbpsc.setPromotionCampaign(currentEdit);
                currentEdit.getPromotionDetails().add(pdbpsc);
                success_msg = "Campaign: " + currentEdit.getTitle() + " New Promotion By Product SubCategory";
                break;
            default:
                try {
                    mbean.CommitNewCampaign(currentEdit);
                    setPromotionCampaigns(mbean.getCampaigns(ubean.getCountryOffice(currentUser.getPlant())));
                    currentEdit = null;
                    success_msg = "Saved and Exited";

                } catch (Exception ex) {
                    error_msg = "ERORR: " + ex.getMessage();
                }
                break;
        }

    }

    public void campaignbutton(ActionEvent event) {
        error_msg = "";
        success_msg = "";

        CommandButton button = (CommandButton) event.getComponent();
        Long ID = Long.valueOf(button.getAlt());
        String command = button.getLabel();
        PromotionCampaign thepc = null;
        for (PromotionCampaign pc : promotionCampaigns) {
            if (pc.getId().equals(ID)) {
                thepc = pc;
                break;
            }
        }

        switch (command) {
            case "Modify":
                System.out.println("(campaignbutton): Modifying");
                try {
                    mbean.CommitNewCampaign(thepc);
                    success_msg = "Campaign: " + thepc.getTitle() + " Modified";
                } catch (Exception ex) {
                    error_msg = "Error:" + ex.getMessage();
                }
                break;

            case "Stop":
                System.out.println("(campaignbutton): Locking");
                try {
                    thepc.setLocked(true);
                    mbean.CommitNewCampaign(thepc);
                    success_msg = "Campaign: " + thepc.getTitle() + " Locked!";
                } catch (Exception ex) {
                    error_msg = "Error:" + ex.getMessage();
                }
                break;
            case "Unstop":
                System.out.println("(campaignbutton): unlocking");
                try {
                    thepc.setLocked(false);
                    mbean.CommitNewCampaign(thepc);
                    success_msg = "Campaign: " + thepc.getTitle() + " unlocked!";
                } catch (Exception ex) {
                    error_msg = "Error:" + ex.getMessage();
                }
                break;
            case "Manage":
                System.out.println("(campaignbutton): Manage Mode");
                currentEdit = thepc;
                success_msg = "Managing campaign: " + thepc.getTitle();
                RequestContext.getCurrentInstance().scrollTo("manage_campaign");
                break;

            case "Email":
                System.out.println("(campaignbutton): Email");
                currentEdit = thepc;
                success_msg = "Emailed campaign: " + thepc.getTitle() + " TOTAL EMAIL SENT=" + mbean.EmailCustomer(thepc, ubean.getCountryOffice(currentUser.getPlant()));
                break;

        }
    }

    public String whatpromotiondetail(PromotionDetail pd) {

        if (pd == null) {
            return "";
        }
        return (pd.getClass().getSimpleName());

    }

    public PromotionDetailByProduct typeCastPromoDetail(PromotionDetail pd) {
        return (PromotionDetailByProduct) pd;
    }

    public PromotionDetailByProductCategory typeCastPromoDetailtoPC(PromotionDetail pd) {

        return (PromotionDetailByProductCategory) pd;
    }

    public PromotionDetailByProductSubCategory typeCastPromoDetailtoPSC(PromotionDetail pd) {

        return (PromotionDetailByProductSubCategory) pd;
    }

    public double getPrice(Stock s) {
        return mbean.getPrice(s, ubean.getCountryOffice(currentUser.getPlant()));

    }

    public void usecouponlisten(AjaxBehaviorEvent event) {
        SelectOneMenu som = (SelectOneMenu) event.getComponent();
        error_msg = "";
        success_msg = "";

        PromotionDetail target = null;

        for (PromotionDetail pd : currentEdit.getPromotionDetails()) {
            if (pd.getId().equals(Long.valueOf(som.getRequiredMessage().split("_")[1]))) {
                target = pd;
                break;
            }
        }

        if (target.getPromotionCoupons().size() > 0) {
            if (target.getPromotionCoupons().get(0).getId() == -1L) {
                target.getPromotionCoupons().remove(target.getPromotionCoupons().get(0));
                target.getPromotionCoupons().get(0).setPromotionDetail(null);
            }
        }

        if (som.getValue().equals("No Coupon")) {
            success_msg = "Set: No Coupon !";
            return;

        } else if (som.getValue().equals("Coupon")) {
            PromotionCoupon pc = new PromotionCoupon();
            pc.setId(-1L);
            target.getPromotionCoupons().add(0, pc);
            success_msg = "Using Coupon ! remember to set limited Quantity";
            return;
        } else {
            PromotionCoupon pc = new PromotionCoupon();
            pc.setId(-1L);
            pc.setOneTimeUsage(false);
            target.getPromotionCoupons().add(0, pc);
            success_msg = "Using Coupon ! Non-Perishable";
            return;

        }

    }

    public String evalcouponstatus(PromotionDetail pd) {
        if (pd.getPromotionCoupons().size() == 0) {
            return "No Coupon";
        }

        if (pd.getPromotionCoupons().get(0).getOneTimeUsage() == false) {
            return "Non-Perishable Coupon";
        }

        return "Coupon";

    }

    public void removepd(ActionEvent event) {
        System.out.println("(Remove PromotionDetail)");
        CommandButton cb = (CommandButton) event.getComponent();

        for (PromotionDetail pd : currentEdit.getPromotionDetails()) {
            if (pd.getId().equals(Long.valueOf(cb.getAlt()))) {
                currentEdit.getPromotionDetails().remove(pd);
                pd.setPromotionCampaign(null);
                break;
            }
        }

        try {
            mbean.CommitNewCampaign(currentEdit);
            error_msg = "";
            success_msg = "Promotion Detail Removed !";
        } catch (Exception ex) {
            error_msg = "Error: Cannot Delete Promotion Detail !";
        }

    }

    public double calcDiscount(Stock s, PromotionDetail pd) {
        return Math.round(mbean.calcDiscount(s, ubean.getCountryOffice(currentUser.getPlant()), pd) * 100) / 100.0;
    }

}
