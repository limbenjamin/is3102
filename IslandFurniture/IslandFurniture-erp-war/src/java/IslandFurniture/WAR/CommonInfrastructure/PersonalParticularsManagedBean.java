/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.CommonInfrastructure;

import IslandFurniture.EJB.CommonInfrastructure.ManageAuthenticationBeanLocal;
import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.EJB.Entities.Staff;
import static IslandFurniture.EJB.Entities.Staff.SHA1Hash;
import IslandFurniture.WAR.CommonInfrastructure.Exceptions.*;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Benjamin
 */
@ManagedBean
@ViewScoped
public class PersonalParticularsManagedBean implements Serializable {
    private String username = null;
    private String phoneNo = null;
    private String emailAddress = null;
    private Staff staff;
    private String hashedPassword = null;
    private String oldPassword = null;
    private String newPassword = null;
    private String confirmNewPassword = null;
    private String hashedOldPassword = null;
    
    @EJB
    private ManageUserAccountBeanLocal staffBean;
    @EJB
    private ManageAuthenticationBeanLocal authBean;
    
    @PostConstruct
    public void init(){
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        staff = staffBean.getStaff(username);
        phoneNo = staff.getPhoneNo();
        emailAddress = staff.getEmailAddress();
    }
    
    public String modifyPersonalParticulars() {
      HttpSession session = Util.getSession();
      username = (String) session.getAttribute("username");
      HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
      phoneNo = request.getParameter("particularsForm:phoneNo");
      emailAddress = request.getParameter("particularsForm:emailAddress");
      staffBean.modifyPersonalParticulars(username, phoneNo, emailAddress);
      return "modifyparticulars";
    }
    
    public String changePassword() throws NewPasswordsNotTheSameException, WrongPasswordException {
      HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
      oldPassword = request.getParameter("passwordForm:oldPassword");
      newPassword = request.getParameter("passwordForm:newPassword");
      confirmNewPassword = request.getParameter("passwordForm:confirmNewPassword");
      if (!newPassword.equals(confirmNewPassword)){
        throw new NewPasswordsNotTheSameException();
      }
      HttpSession session = Util.getSession();
      username = (String) session.getAttribute("username");
      staff = staffBean.getStaff(username);
      hashedPassword = staff.getPassword();
      hashedOldPassword = SHA1Hash(staff.getSalt()+ oldPassword);
      if (!hashedOldPassword.equals(hashedPassword)){
          throw new WrongPasswordException();
      }
      authBean.changePassword(username, newPassword);
      return "changepassword";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public ManageUserAccountBeanLocal getStaffBean() {
        return staffBean;
    }

    public void setStaffBean(ManageUserAccountBeanLocal staffBean) {
        this.staffBean = staffBean;
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

    public ManageAuthenticationBeanLocal getAuthBean() {
        return authBean;
    }

    public void setAuthBean(ManageAuthenticationBeanLocal authBean) {
        this.authBean = authBean;
    }
    
    
    
}
