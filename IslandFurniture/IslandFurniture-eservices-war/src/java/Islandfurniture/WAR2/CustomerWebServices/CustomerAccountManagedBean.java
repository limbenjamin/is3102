/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Islandfurniture.WAR2.CustomerWebServices;

import IslandFurniture.EJB.CustomerWebService.ManageLocalizationBeanLocal;
import IslandFurniture.EJB.CustomerWebService.ManageMemberAuthenticationBeanLocal;
import IslandFurniture.EJB.CustomerWebService.ManagePerksBeanLocal;
import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.Customer;
import IslandFurniture.Entities.PromotionDetail;
import IslandFurniture.Entities.PromotionDetailByProduct;
import IslandFurniture.Entities.PromotionDetailByProductCategory;
import IslandFurniture.Entities.PromotionDetailByProductSubCategory;
import static IslandFurniture.StaticClasses.EncryptMethods.SHA1Hash;
import Islandfurniture.WAR2.Exceptions.NewPasswordsNotTheSameException;
import Islandfurniture.WAR2.Exceptions.WrongPasswordException;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
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
    private String address = null;
    private String emailAddress = null;
    private Customer customer;
    private String hashedPassword = null;
    private String oldPassword = null;
    private String newPassword = null;
    private String confirmNewPassword = null;
    private String hashedOldPassword = null;
    private String coDir;
    private CountryOffice co;
    private List<PromotionDetail> perks;
    private List<PromotionDetailByProduct> pdpPerks;
    private List<PromotionDetailByProductCategory> pdpcPerks;
    private List<PromotionDetailByProductSubCategory> pdpscPerks;
    
    @EJB
    private ManageMemberAuthenticationBeanLocal mmab;
    @EJB
    private ManageLocalizationBeanLocal manageLocalizationBean;
    @EJB
    private ManagePerksBeanLocal perksBean;
    
    @PostConstruct
    public void init(){
        HttpSession session = Util.getSession();
        emailAddress = (String) session.getAttribute("emailAddress");
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest httpReq = (HttpServletRequest) ec.getRequest();
        coDir = (String) httpReq.getAttribute("coCode");
        if(coDir !=null && !coDir.isEmpty()){
            coDir = "/"+ coDir;
        }
        
        if (emailAddress == null) {
            try {
                ec.redirect(ec.getRequestContextPath() + "/login.xhtml");
            } catch (IOException ex) {
                
            }
        }   
        else {
            customer = mmab.getCustomer(emailAddress);
            pdpcPerks = perksBean.getPDPC(customer);
            pdpPerks = perksBean.getPDP(customer);
            pdpscPerks = perksBean.getPDPSC(customer);
            phoneNo = customer.getPhoneNo();
            name = customer.getName();
            co = manageLocalizationBean.findCoByCode((String) httpReq.getAttribute("coCode"));
        }
    }
    
    public double getPercentageDiscount(Long id) {
        return perksBean.getPerk(id).getPercentageDiscount();
    }
    
    public double getAbsoluteDiscount(Long id) {
        return perksBean.getPerk(id).getAbsoluteDiscount();
    }
    
    public void modifyPersonalParticulars() throws IOException{
        HttpSession session = Util.getSession();
        emailAddress = (String) session.getAttribute("emailAddress");
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        phoneNo = request.getParameter("particularsForm:phoneNo");
        name = request.getParameter("particularsForm:name");
        address = request.getParameter("particularsForm:address");
        mmab.modifyPersonalParticulars(emailAddress, phoneNo, name, address);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().putNow("message",
             new FacesMessage(FacesMessage.SEVERITY_INFO, "Your details have been updated!",""));        
        ec.redirect(ec.getRequestContextPath() + coDir + "/member/account.xhtml");
    }
    
    public void changePassword() throws NewPasswordsNotTheSameException, WrongPasswordException, IOException {
      ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
      HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
      oldPassword = request.getParameter("passwordForm:oldPassword");
      newPassword = request.getParameter("passwordForm:newPassword");
      confirmNewPassword = request.getParameter("passwordForm:confirmNewPassword");
      if (!newPassword.equals(confirmNewPassword)){
        FacesContext.getCurrentInstance().getExternalContext().getFlash().putNow("message",
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "New passwords are not the same",""));
        ec.redirect(ec.getRequestContextPath() + coDir + "/member/account.xhtml");
      }
      else {
          hashedPassword = customer.getPassword();
          hashedOldPassword = SHA1Hash(customer.getSalt()+ oldPassword);
          if (!hashedOldPassword.equals(hashedPassword)){
            FacesContext.getCurrentInstance().getExternalContext().getFlash().putNow("message",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Old password is wrong",""));
            ec.redirect(ec.getRequestContextPath() + coDir + "/member/account.xhtml");
          }
          else {
          mmab.changePassword(emailAddress, newPassword);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().putNow("message",
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Your password has been successfully changed",""));
          ec.redirect(ec.getRequestContextPath() + coDir + "/member/account.xhtml");
          }
      }
    }
    
    public void removeAccount() throws WrongPasswordException, IOException{
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String existingPassword = request.getParameter("removeCustomerAccount:password");
        // check if password is correct
          hashedPassword = customer.getPassword();
          hashedOldPassword = SHA1Hash(customer.getSalt()+ existingPassword);
          if (!hashedOldPassword.equals(hashedPassword)){
            FacesContext.getCurrentInstance().getExternalContext().getFlash().putNow("message",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "entered invalid password",""));
            ec.redirect(ec.getRequestContextPath() + coDir + "/member/account.xhtml");
          }
          else {
            // remove customer account and logout
            mmab.removeCustomerAccount(emailAddress);
            HttpSession session = Util.getSession();
            session.setAttribute("", emailAddress);
            session.invalidate();
            ec.redirect(ec.getRequestContextPath() + coDir + "/home.xhtml");
          }
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

    public CountryOffice getCo() {
        return co;
    }

    public void setCo(CountryOffice co) {
        this.co = co;
    }

    public List<PromotionDetail> getPerks() {
        return perks;
    }

    public void setPerks(List<PromotionDetail> perks) {
        this.perks = perks;
    }

    public List<PromotionDetailByProduct> getPdpPerks() {
        return pdpPerks;
    }

    public void setPdpPerks(List<PromotionDetailByProduct> pdpPerks) {
        this.pdpPerks = pdpPerks;
    }

    public List<PromotionDetailByProductCategory> getPdpcPerks() {
        return pdpcPerks;
    }

    public void setPdpcPerks(List<PromotionDetailByProductCategory> pdpcPerks) {
        this.pdpcPerks = pdpcPerks;
    }

    public List<PromotionDetailByProductSubCategory> getPdpscPerks() {
        return pdpscPerks;
    }

    public void setPdpscPerks(List<PromotionDetailByProductSubCategory> pdpscPerks) {
        this.pdpscPerks = pdpscPerks;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
