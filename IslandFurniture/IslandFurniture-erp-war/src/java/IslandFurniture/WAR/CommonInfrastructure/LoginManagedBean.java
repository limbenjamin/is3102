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
import IslandFurniture.EJB.Entities.*;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@ManagedBean
@SessionScoped
public class LoginManagedBean implements Serializable {

    private static final long serialVersionUID = 5443351151396868724L;
    private String absolutepath = "/IslandFurniture-erp-war/";
    private Staff staff;
    private String username = null;
    private String password = null;

    @EJB
    private ManageAuthenticationBeanLocal authBean;

    public String login() {
        boolean result = authBean.authenticate(username, password);
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

    public ManageAuthenticationBeanLocal getAuthBean() {
        return authBean;
    }

    public void setAuthBean(ManageAuthenticationBeanLocal authBean) {
        this.authBean = authBean;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getAbsolutepath() {
        return absolutepath;
    }

    public void setAbsolutepath(String absolutepath) {
        this.absolutepath = absolutepath;
    }

}
