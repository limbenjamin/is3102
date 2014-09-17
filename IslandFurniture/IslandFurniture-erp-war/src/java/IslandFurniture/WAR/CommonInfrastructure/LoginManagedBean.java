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
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.SessionScoped;
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
    private List<Notification> notificationList;
    private Notification notification;
    private Integer notificationListSize;

    @EJB
    private ManageAuthenticationBeanLocal authBean;
    @EJB
    private ManageUserAccountInformationBean muaib;

    public String login() {
        boolean result = authBean.authenticate(username, password);
        if (result) {
            HttpSession session = Util.getSession();
            session.setAttribute("username", username);
            staff = muaib.getStaff(username);
            notificationList = staff.getNotifications();
            notificationListSize = notificationList.size();
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
    
    public void pullNotification() {
        notificationList = staff.getNotifications();
        if (notificationList.size() != notificationListSize){
            notification = notificationList.get(notificationList.size()-1);
            this.setNotification(notification);
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

    public List<Notification> getNotificationList() {
        return notificationList;
    }

    public void setNotificationList(List<Notification> notificationList) {
        this.notificationList = notificationList;
    }

    public ManageUserAccountInformationBean getMuaib() {
        return muaib;
    }

    public void setMuaib(ManageUserAccountInformationBean muaib) {
        this.muaib = muaib;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public Integer getNotificationListSize() {
        return notificationListSize;
    }

    public void setNotificationListSize(Integer notificationListSize) {
        this.notificationListSize = notificationListSize;
    }

    
    
}
