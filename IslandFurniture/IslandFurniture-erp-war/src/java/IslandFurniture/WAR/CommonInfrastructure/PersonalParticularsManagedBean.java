/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.CommonInfrastructure;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountInformationBean;
import IslandFurniture.EJB.Entities.Staff;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Benjamin
 */
@Named
@ViewScoped
public class PersonalParticularsManagedBean implements Serializable {
    private String username = null;
    private String phoneNo = null;
    private String emailAddress = null;
    private Staff staff;
    
    @EJB
    private ManageUserAccountInformationBean staffBean;
    
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

    public ManageUserAccountInformationBean getStaffBean() {
        return staffBean;
    }

    public void setStaffBean(ManageUserAccountInformationBean staffBean) {
        this.staffBean = staffBean;
    }
    
    
    
}
