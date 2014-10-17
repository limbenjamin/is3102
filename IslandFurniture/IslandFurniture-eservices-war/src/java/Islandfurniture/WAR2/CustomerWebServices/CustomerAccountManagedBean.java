/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Islandfurniture.WAR2.CustomerWebServices;

import IslandFurniture.EJB.CustomerWebService.ManageMemberAuthenticationBeanLocal;
import IslandFurniture.Entities.Customer;
import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Zee
 */
@ManagedBean
@ViewScoped
public class CustomerAccountManagedBean implements Serializable{

    private String name = null;
    private String phoneNo = null;
    private String emailAddress = null;
    private Customer customer;
    private String hashedPassword = null;
    private String oldPassword = null;
    private String newPassword = null;
    private String confirmNewPassword = null;
    private String hashedOldPassword = null;
    
    @EJB
    private ManageMemberAuthenticationBeanLocal mmab;
    
    @PostConstruct
    public void init(){
        HttpSession session = Util.getSession();
        emailAddress = (String) session.getAttribute("emailAddress");
        if (emailAddress == null) {
            try {              
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect("login.xhtml");
            } catch (IOException ex) {   
            }
        }   
        else {
            customer = mmab.getCustomer(emailAddress);
            phoneNo = customer.getPhoneNo();
            name = customer.getName();
        }
    }    
    
    public String modifyPersonalParticulars() {
        HttpSession session = Util.getSession();
        emailAddress = (String) session.getAttribute("emailAddress");
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        phoneNo = request.getParameter("particularsForm:phoneNo");
        name = request.getParameter("particularsForm:name");
        mmab.modifyPersonalParticulars(emailAddress, phoneNo, name);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
             new FacesMessage(FacesMessage.SEVERITY_INFO, "Your details have been updated!",""));        
        return "account";
    }    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }

    public String getHashedOldPassword() {
        return hashedOldPassword;
    }

    public void setHashedOldPassword(String hashedOldPassword) {
        this.hashedOldPassword = hashedOldPassword;
    }

    public ManageMemberAuthenticationBeanLocal getMmab() {
        return mmab;
    }

    public void setMmab(ManageMemberAuthenticationBeanLocal mmab) {
        this.mmab = mmab;
    }
    
}
