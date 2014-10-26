/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.OperationalCRM;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.EJB.OperationalCRM.ManageMembershipLocal;
import IslandFurniture.EJB.OperationalCRM.ManageRedeemableItemLocal;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Staff;
import IslandFurniture.EJB.OperationalCRM.ManageRedemptionLocal;
import IslandFurniture.Entities.Customer;
import IslandFurniture.Entities.Redemption;
import IslandFurniture.Entities.Voucher;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;

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
    private Long redeemableItemId;
    private String username;
    private Staff staff;
    private Plant plant;

    private int cashValue;
    private Date redemptionDateType;
    private Calendar redemptionDateCal;

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
        redemptionBean.createRedemption(staff, getCalendar(), customerId, redeemableItemId);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Redemption has sucessfully been created", ""));
//        } else {
//            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
//                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "There is an existing Redemption with the same Cash Value and Expiry Date. Creation of Redemption was unsuccessful.", ""));
//        }
    }

    //  Function: To get current Time in Calendar type
    Calendar getCalendar() {
        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        cal.setTime(date);
        Calendar calDate = cal;
        return calDate;
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
