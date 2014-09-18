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
import IslandFurniture.EJB.ITManagement.ManagePrivilegesBeanLocal;
import IslandFurniture.EJB.ITManagement.ManageStaffAccountsBeanLocal;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
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
    private List<Privilege> privilegeList;
    private Notification notification;
    private Integer notificationListSize;
    private String menu;
    private Privilege privilege;

    @EJB
    private ManageAuthenticationBeanLocal authBean;
    @EJB
    private ManageUserAccountInformationBean muaib;
    @EJB
    private ManageStaffAccountsBeanLocal msab;
    @EJB
    private ManagePrivilegesBeanLocal mpb;

    public String login() {
        boolean result = authBean.authenticate(username, password);
        if (result) {
            HttpSession session = Util.getSession();
            session.setAttribute("username", username);
            staff = muaib.getStaff(username);
            notificationList = staff.getNotifications();
            notificationListSize = notificationList.size();
            privilegeList = msab.getPrivilegeListforStaff(username);
            String absoluteWebPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
            menu = new String();
            menu += "<li><a href="+ absoluteWebPath +"/dash.xhtml"+"><i class=\"fa fa-home\"></i><span>Dashboard</span></a></li>";
            menu += "<li><a href="+ absoluteWebPath +"/common/messaging.xhtml"+"><i class=\"fa fa-envelope\"></i><span>Messaging</span></a></li>";
            privilege = mpb.getPrivilegeFromName("Broadcast");
            if (privilegeList.contains(privilege)){
                menu += "<li><a href="+ absoluteWebPath +"/common/broadcast.xhtml"+"><i class=\"fa fa-home\"></i><span>Broadcast</span></a></li>";
            }
            privilege = mpb.getPrivilegeFromName("Manage Plant");
            if (privilegeList.contains(privilege)){
                menu += "<li><a href="+ absoluteWebPath +"/it/manageplant.xhtml"+"><i class=\"fa fa-desktop\"></i><span>Manage Org. Hierarchy</span></a></li>";
            }
            privilege = mpb.getPrivilegeFromName("Manage Staff");
            if (privilegeList.contains(privilege)){
                menu += "<li><a href="+ absoluteWebPath +"/it/managestaff.xhtml"+"><i class=\"fa fa-calendar\"></i><span>Manage Staff</span></a></li>";
            }
            privilege = mpb.getPrivilegeFromName("Manage Roles");
            if (privilegeList.contains(privilege)){
                menu += "<li><a href="+ absoluteWebPath +"/it/roleprivilege.xhtml"+"><i class=\"fa fa-pencil\"></i><span>Manage Privileges</span></a></li>";
            }
            privilege = mpb.getPrivilegeFromName("MSSR");
            if (privilegeList.contains(privilege)){
                menu += "<li><a href="+ absoluteWebPath +"/salesplanning/viewmssr.xhtml"+"><i class=\"fa fa-home\"></i><span>View Monthly Stock Supply Requirements</span></a></li>";
            }
            privilege = mpb.getPrivilegeFromName("Material");
            if (privilegeList.contains(privilege)){
                menu += "<li><a href="+ absoluteWebPath +"/knowledge/material.xhtml"+"><i class=\"fa fa-home\"></i><span>Material</span></a></li>";
            }
            privilege = mpb.getPrivilegeFromName("Furniture");
            if (privilegeList.contains(privilege)){
                menu += "<li><a href="+ absoluteWebPath +"/knowledge/furniture.xhtml"+"><i class=\"fa fa-home\"></i><span>Furniture</span></a></li>";
            }
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

    public List<Privilege> getPrivilegeList() {
        return privilegeList;
    }

    public void setPrivilegeList(List<Privilege> privilegeList) {
        this.privilegeList = privilegeList;
    }

    public ManageStaffAccountsBeanLocal getMsab() {
        return msab;
    }

    public void setMsab(ManageStaffAccountsBeanLocal msab) {
        this.msab = msab;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    
    
}
