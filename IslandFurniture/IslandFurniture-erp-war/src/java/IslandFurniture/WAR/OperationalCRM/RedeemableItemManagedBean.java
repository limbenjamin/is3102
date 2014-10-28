/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.OperationalCRM;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Staff;
import IslandFurniture.EJB.OperationalCRM.ManageRedeemableItemLocal;
import IslandFurniture.Entities.Voucher;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
public class RedeemableItemManagedBean implements Serializable {

    private List<Voucher> redeemableItemList;

    private String username;
    private Staff staff;
    private Plant plant;
    
    private int pointsReq;
    private int cashValue;
    private Date expiryDateType;
    private Calendar expiryDateCal;
    private String expiryDateString;

    @EJB
    private ManageUserAccountBeanLocal staffBean;
    @EJB
    public ManageRedeemableItemLocal itemBean;

    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        staff = staffBean.getStaff(username);
        plant = staff.getPlant();
        redeemableItemList = itemBean.viewRedeemableItem(plant);
    }

//  Function: To create a Redeemable Item
    public void addRedeemableItem(ActionEvent event) throws IOException, ParseException {
        expiryDateType = new SimpleDateFormat("yyyy-MM-dd").parse(expiryDateString);
        expiryDateCal = Calendar.getInstance();
        Date date = expiryDateType;
        expiryDateCal.setTime(date);

        if (itemBean.checkIfNoRedeemableItemWithSameCashValueAndExpiryDate(plant, cashValue, expiryDateCal)) {
            itemBean.createRedeemableItem(plant, cashValue, expiryDateCal, pointsReq);
            redeemableItemList = itemBean.viewRedeemableItem(plant);

            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Redeemable Item has sucessfully been created", ""));
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "There is an existing Redeemable Item with the same Cash Value and Expiry Date. Creation of Redeemable Item was unsuccessful.", ""));
        }
    }

//  Function: To edit a Redeemable Item
    public void editRedeemableItem(ActionEvent event) throws IOException {
        Voucher ss = (Voucher) event.getComponent().getAttributes().get("redeemableItem");
        if (itemBean.checkIfNoRedeemableItemWithSameCashValueAndExpiryDate(plant, ss.getCashValue(), ss.getExpiryDate())) {
            itemBean.editRedeemableItem(ss);
            redeemableItemList = itemBean.viewRedeemableItem(plant);
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Redeemable Item has sucessfully been created", ""));
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "There is an existing Redeemable Item with that title. Editing of Redeemable Item was unsuccessful.", ""));
        }
    }

//  Function: To delete a Redeemable Item
    public void deleteRedeemableItem(ActionEvent event) throws IOException {
        Voucher v = (Voucher) event.getComponent().getAttributes().get("redeemableItem");

        if (itemBean.checkIfNoRedemptionWithRedeemableItem(v)) {
            itemBean.deleteRedeemableItem(v);
            redeemableItemList = itemBean.viewRedeemableItem(plant);
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Redeemable Item has sucessfully been deleted", ""));
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "There are Customers associated with this Redeemable Item. Deletion of Redeemable Item was unsuccessful.", ""));
        }
    }

    public int getPointsReq() {
        return pointsReq;
    }

    public void setPointsReq(int pointsReq) {
        this.pointsReq = pointsReq;
    }
    
    public List<Voucher> getRedeemableItemList() {
        return redeemableItemList;
    }

    public void setRedeemableItemList(List<Voucher> redeemableItemList) {
        this.redeemableItemList = redeemableItemList;
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

    public Date getExpiryDateType() {
        return expiryDateType;
    }

    public void setExpiryDateType(Date expiryDateType) {
        this.expiryDateType = expiryDateType;
    }

    public Calendar getExpiryDateCal() {
        return expiryDateCal;
    }

    public void setExpiryDateCal(Calendar expiryDateCal) {
        this.expiryDateCal = expiryDateCal;
    }

    public String getExpiryDateString() {
        return expiryDateString;
    }

    public void setExpiryDateString(String expiryDateString) {
        this.expiryDateString = expiryDateString;
    }

    public ManageUserAccountBeanLocal getStaffBean() {
        return staffBean;
    }

    public void setStaffBean(ManageUserAccountBeanLocal staffBean) {
        this.staffBean = staffBean;
    }

    public ManageRedeemableItemLocal getItemBean() {
        return itemBean;
    }

    public void setItemBean(ManageRedeemableItemLocal itemBean) {
        this.itemBean = itemBean;
    }

    

}
