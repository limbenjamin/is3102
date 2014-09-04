/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.CommonInfrastructure;

/**
 *
 * @author Benjamin
 */


import IslandFurniture.EJB.CommonInfrastructure.*;
import IslandFurniture.EJB.Entities.Staff;
import java.io.Serializable;
import java.util.Date;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Named
@SessionScoped
public class StaffManagedBean implements Serializable {
    private static final long serialVersionUID = 5443351151396868724L;
    private Staff staff;
    private String message;
    private String username = null;
    private String password = null;
    private String name = null;
    private String notes = null;
    private String phoneNo = null;
    private String emailAddress = null;
    private Date lastLogon = null;
    @EJB
    private ManageAuthenticationBean authBean;
    @EJB
    private ManageUserAccountInformationBean staffBean;
    
    public StaffManagedBean() {
    }
    
    public String login() {
        boolean result = authBean.authenticate(username, password);
        if (result) {
            HttpSession session = Util.getSession();
            session.setAttribute("username", username);
            this.staff = staffBean.getStaff(username);
            this.notes = staff.getNotes();
            this.name = staff.getName();
            this.lastLogon = staff.getLastLogon();
            this.phoneNo = staff.getPhoneNo();
            this.emailAddress = staff.getEmailAddress();
            return "dash";
        } else {
 
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Invalid Login!",
                    "Please Try Again!"));
 
            // invalidate session, and redirect to other pages
 
            //message = "Invalid Login. Please Try Again!";
            return "login";
        }
    }
    
    public String logout() {
      HttpSession session = Util.getSession();
      session.invalidate();
      return "login";
    }
    
    public String modifyNotes() {
      HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
      notes = request.getParameter("notesForm:notes");
      staffBean.modifyNote(username, notes);
      return "dash";
    }
    
    public String modifyPersonalParticulars() {
      staffBean.modifyPersonalParticulars(username, phoneNo, emailAddress);
      return "modifyparticulars";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ManageAuthenticationBean getAuthBean() {
        return authBean;
    }

    public void setAuthBean(ManageAuthenticationBean authBean) {
        this.authBean = authBean;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public ManageUserAccountInformationBean getStaffBean() {
        return staffBean;
    }

    public void setStaffBean(ManageUserAccountInformationBean staffBean) {
        this.staffBean = staffBean;
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

    public Date getLastLogon() {
        return lastLogon;
    }

    public void setLastLogon(Date lastLogon) {
        this.lastLogon = lastLogon;
    }

    

}
