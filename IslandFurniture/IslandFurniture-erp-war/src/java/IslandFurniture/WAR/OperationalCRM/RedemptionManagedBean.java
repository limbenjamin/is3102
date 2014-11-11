/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.OperationalCRM;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.EJB.OperationalCRM.ManageMembershipLocal;
import IslandFurniture.EJB.OperationalCRM.ManageRedeemableItemLocal;
import IslandFurniture.EJB.OperationalCRM.ManageRedemptionLocal;
import IslandFurniture.Entities.Customer;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.RedeemableItem;
import IslandFurniture.Entities.Redemption;
import IslandFurniture.Entities.Staff;
import IslandFurniture.Entities.Voucher;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import IslandFurniture.WAR.Util.NFCMethods;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpSession;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.TerminalFactory;

/**
 *
 * @author KamilulAshraf
 */
@ManagedBean
@ViewScoped
public class RedemptionManagedBean implements Serializable {

    private List<Redemption> redemptionList;
    private List<Voucher> redeemableItemList;
    private List<Customer> customerList;

    private Long customerId;
    private Long loyaltyCardId;
    private Long redeemableItemId;
    private String username;
    private Staff staff;
    private Plant plant;

    private int cashValue;
    private Integer pointsReq;
    private Date redemptionDateType;
    private Calendar redemptionDateCal;
    private Customer customer;
    private RedeemableItem redeemableItem;

    @EJB
    private ManageUserAccountBeanLocal staffBean;
    @EJB
    public ManageRedemptionLocal redemptionBean;
    @EJB
    public ManageRedeemableItemLocal itemBean;
    @EJB
    public ManageMembershipLocal membershipBean;

    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        staff = staffBean.getStaff(username);
        plant = staff.getPlant();
        redemptionList = redemptionBean.viewRedemption();
        customerList = membershipBean.viewCustomers();
        redeemableItemList = itemBean.viewRedeemableItemFromStore(plant, getCalendar());
    }

//  Function: To create a Redemption
    public void addRedemption(ActionEvent event) throws IOException, ParseException {
        customer = membershipBean.getCustomer(customerId);
        if (customer.getCurrentPoints().intValue() == redeemableItem.getPointsReq()
                || customer.getCurrentPoints() > redeemableItem.getPointsReq()) {
            redemptionBean.createRedemption(staff, getCalendar(), customerId, redeemableItemId);           
            membershipBean.editCustomerAccountPoints(customer, (customer.getCurrentPoints() - redeemableItem.getPointsReq()));
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Redemption has sucessfully been created", ""));
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Customer's current points (" + customer.getCurrentPoints().intValue() + " points) is not enough to redeem the item (" + redeemableItem.getPointsReq() + " points). Redemption failed", ""));
        }
    }

    //  Function: To get current Time in Calendar type
    Calendar getCalendar() {
        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        cal.setTime(date);
        Calendar calDate = cal;
        return calDate;
    }
    
    //  Function: To display Storage Bins in the particular Storage Area -- For AJAX    
    public void changeRedeemableItem(AjaxBehaviorEvent event) {
        System.out.println("redeemableItem ID " + redeemableItemId);
        if (redeemableItemId != null) {
            this.redeemableItem = itemBean.getRedeemableItem(redeemableItemId);
            System.out.println("redeemableItem" + redeemableItem);
        }
    }
    
    public void scanNFC(AjaxBehaviorEvent event){
        String customerCardId = "";
        CardTerminal acr122uCardTerminal = null;
        try {
            TerminalFactory terminalFactory = TerminalFactory.getDefault();
            if (!terminalFactory.terminals().list().isEmpty()) {
                for (CardTerminal cardTerminal : terminalFactory.terminals().list()) {
                    if (cardTerminal.getName().contains("ACS ACR122")) {
                        acr122uCardTerminal = cardTerminal;
                        break;
                    }
                }
                if (acr122uCardTerminal != null) {
                    try {
                        if (acr122uCardTerminal.isCardPresent()) {
                            NFCMethods nfc = new NFCMethods();
                            customerCardId = (nfc.getID(acr122uCardTerminal)).substring(0, 8);
                            this.customer = membershipBean.getCustomerByCard(customerCardId);
                            customerId = this.customer.getId();
                        }
                    } catch (CardException ex) {
                        Logger.getLogger(MembershipManagedBean.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (Exception ex) {
                        Logger.getLogger(MembershipManagedBean.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                }
            } else {
            }
        } catch (Exception ex) {
        }
    }

    public List<Redemption> getRedemptionList() {
        return redemptionList;
    }

    public void setRedemptionList(List<Redemption> redemptionList) {
        this.redemptionList = redemptionList;
    }

    public List<Voucher> getRedeemableItemList() {
        return redeemableItemList;
    }

    public void setRedeemableItemList(List<Voucher> redeemableItemList) {
        this.redeemableItemList = redeemableItemList;
    }

    public List<Customer> getCustomerList() {
        return customerList;
    }

    public void setCustomerList(List<Customer> customerList) {
        this.customerList = customerList;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getLoyaltyCardId() {
        return loyaltyCardId;
    }

    public void setLoyaltyCardId(Long loyaltyCardId) {
        this.loyaltyCardId = loyaltyCardId;
    }

    public Long getRedeemableItemId() {
        return redeemableItemId;
    }

    public void setRedeemableItemId(Long redeemableItemId) {
        this.redeemableItemId = redeemableItemId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    public int getCashValue() {
        return cashValue;
    }

    public void setCashValue(int cashValue) {
        this.cashValue = cashValue;
    }

    public Integer getPointsReq() {
        return pointsReq;
    }

    public void setPointsReq(Integer pointsReq) {
        this.pointsReq = pointsReq;
    }

    public Date getRedemptionDateType() {
        return redemptionDateType;
    }

    public void setRedemptionDateType(Date redemptionDateType) {
        this.redemptionDateType = redemptionDateType;
    }

    public Calendar getRedemptionDateCal() {
        return redemptionDateCal;
    }

    public void setRedemptionDateCal(Calendar redemptionDateCal) {
        this.redemptionDateCal = redemptionDateCal;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public RedeemableItem getRedeemableItem() {
        return redeemableItem;
    }

    public void setRedeemableItem(RedeemableItem redeemableItem) {
        this.redeemableItem = redeemableItem;
    }

    public ManageUserAccountBeanLocal getStaffBean() {
        return staffBean;
    }

    public void setStaffBean(ManageUserAccountBeanLocal staffBean) {
        this.staffBean = staffBean;
    }

    public ManageRedemptionLocal getRedemptionBean() {
        return redemptionBean;
    }

    public void setRedemptionBean(ManageRedemptionLocal redemptionBean) {
        this.redemptionBean = redemptionBean;
    }

    public ManageRedeemableItemLocal getItemBean() {
        return itemBean;
    }

    public void setItemBean(ManageRedeemableItemLocal itemBean) {
        this.itemBean = itemBean;
    }

    public ManageMembershipLocal getMembershipBean() {
        return membershipBean;
    }

    public void setMembershipBean(ManageMembershipLocal membershipBean) {
        this.membershipBean = membershipBean;
    }

   

}
