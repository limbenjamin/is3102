/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package managedbean;

/**
 *
 * @author Benjamin
 */


import IslandFurniture.EJB.CommonInfrastructure.*;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;


@Named
@SessionScoped
public class StaffManagedBean implements Serializable {
    private static final long serialVersionUID = 5443351151396868724L;
    private String message;
    private String username = null;
    private String password = null;
    private String status = null;
    @EJB
    private ManageAuthenticationBean StaffBean;
    
    public StaffManagedBean() {
    }
    
    public String login() {
        boolean result = StaffBean.authenticate(username, password);
        if (result) {
            HttpSession session = Util.getSession();
            session.setAttribute("username", username);
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ManageAuthenticationBean getStaffBean() {
        return StaffBean;
    }

    public void setStaffBean(ManageAuthenticationBean StaffBean) {
        this.StaffBean = StaffBean;
    }

    

}
