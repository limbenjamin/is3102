/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.OperationalCRM;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.EJB.OperationalCRM.FurnitureReturnBeanLocal;
import IslandFurniture.Entities.FurnitureTransaction;
import IslandFurniture.Entities.Staff;
import IslandFurniture.Exceptions.InvalidInputException;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.convert.NumberConverter;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@ManagedBean
@ViewScoped
public class FurnitureReturnManagedBean implements Serializable {

    @EJB
    private ManageUserAccountBeanLocal manageUserAccountBean;

    @EJB
    private FurnitureReturnBeanLocal furnitureReturnBean;

    private Long transId;
    private FurnitureTransaction furnTrans;
    private NumberConverter converter = new NumberConverter();
    private Staff staff;
    boolean returning = false;
    boolean issueing = false;
    boolean validForClaim = false;

    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        staff = manageUserAccountBean.getStaff((String) session.getAttribute("username"));
        String transIdParam = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("transId");

        if (transIdParam != null) {
            transId = Long.parseLong(transIdParam);
            furnTrans = furnitureReturnBean.findTransaction(transId);
            if (furnTrans == null) {

            } else if (!furnTrans.getStore().equals(staff.getPlant())) {
                furnTrans = null;
            } else {
                // Returning state?
                if (FacesContext.getCurrentInstance().getExternalContext().getFlash().get("returning") != null) {
                    returning = true;
                }

                // Issueing state?
                if (FacesContext.getCurrentInstance().getExternalContext().getFlash().get("issueing") != null) {
                    issueing = true;
                } else {
                    // Set issueing status
                    if (furnitureReturnBean.checkCanIssue(furnTrans)) {
                        validForClaim = true;
                    }
                }

                // Set converter to transaction currency
                converter = new NumberConverter();
                converter.setCurrencyCode(furnTrans.getStore().getCountry().getCurrency().getCurrencyCode());
                converter.setType("currency");
            }
        }
    }

    public void checkTrans() {
        if (transId == null) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please enter the receipt number of the transaction for furniture return", ""));
        } else {
            furnTrans = furnitureReturnBean.findTransaction(transId);
            if (furnTrans == null) {
                FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Given receipt is not a valid receipt from the Furniture Hall", ""));
            } else if (!furnTrans.getStore().equals(staff.getPlant())) {
                FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Given receipt is not from " + staff.getPlant().getName() + " store", ""));
            }
        }
    }

    public void processReturn() {
        if (furnitureReturnBean.checkValid(furnTrans)) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("returning", "1");
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Given transaction is no longer eligible for furniture return", ""));
        }
    }

    public void processIssue() {
        if (furnitureReturnBean.checkCanIssue(furnTrans)) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("issueing", "1");
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Given transaction has no claimable furniture", ""));
        }
    }

    public void saveReturns() throws IOException {
        try {
            furnitureReturnBean.saveReturns(furnTrans, staff);
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Furniture returns saved successfully", ""));
        } catch (InvalidInputException ex) {
            processReturn();
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), ""));
        }
    }

    public void saveClaims() {
        try {
            furnitureReturnBean.saveClaims(furnTrans, staff);
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Furniture issue saved successfully", ""));
        } catch (InvalidInputException ex) {
            processIssue();
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), ""));
        }
    }

    /**
     * Creates a new instance of FurnitureReturnManagedBean
     */
    public FurnitureReturnManagedBean() {
    }

    public Long getTransId() {
        return transId;
    }

    public void setTransId(Long transId) {
        this.transId = transId;
    }

    public FurnitureTransaction getFurnTrans() {
        return furnTrans;
    }

    public void setFurnTrans(FurnitureTransaction furnTrans) {
        this.furnTrans = furnTrans;
    }

    public NumberConverter getConverter() {
        return converter;
    }

    public void setConverter(NumberConverter converter) {
        this.converter = converter;
    }

    public boolean isReturning() {
        return returning;
    }

    public void setReturning(boolean returning) {
        this.returning = returning;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public boolean isIssueing() {
        return issueing;
    }

    public void setIssueing(boolean issueing) {
        this.issueing = issueing;
    }

    public boolean isValidForClaim() {
        return validForClaim;
    }

    public void setValidForClaim(boolean validForClaim) {
        this.validForClaim = validForClaim;
    }

}
