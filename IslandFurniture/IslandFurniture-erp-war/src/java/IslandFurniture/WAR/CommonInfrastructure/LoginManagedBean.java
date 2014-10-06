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

import IslandFurniture.Entities.Staff;
import IslandFurniture.Entities.Url;
import IslandFurniture.Entities.Privilege;
import IslandFurniture.Entities.Notification;
import IslandFurniture.EJB.CommonInfrastructure.ManageAuthenticationBeanLocal;
import IslandFurniture.EJB.CommonInfrastructure.ManageNotificationsBeanLocal;
import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.EJB.ITManagement.ManagePrivilegesBeanLocal;
import IslandFurniture.EJB.ITManagement.ManageStaffAccountsBeanLocal;
import IslandFurniture.EJB.ITManagement.ManageSystemAuditLogBeanLocal;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
    private Staff staff;
    private String username = null;
    private String password = null;
    private String confirmPassword = null;
    private List<Notification> notificationList;
    private List<Privilege> privilegeList;
    private Notification notification;
    private Integer notificationListSize;
    private String menu;
    private Privilege privilege;
    private Integer count;
    private Long nid;
    private LocalDateTime localDateTime;
    private Instant instant;
    private List<LocalDateTime> localDateTimeList;
    private List<Url> urlList;
    private List<Url> existingUrlList;
    private Url url;
    private String code;
    private String timezone;

    @EJB
    private ManageAuthenticationBeanLocal authBean;
    @EJB
    private ManageUserAccountBeanLocal muab;
    @EJB
    private ManageStaffAccountsBeanLocal msab;
    @EJB
    private ManagePrivilegesBeanLocal mpb;
    @EJB
    private ManageNotificationsBeanLocal mnb;
    @EJB
    private ManageSystemAuditLogBeanLocal msalb;
    
    
    public String login() {
        boolean result = authBean.authenticate(username, password);
        if (result) {
            msalb.log("Staff", muab.getStaff(username).getId(), "ACCESS", "Login Successfully", username);
            HttpSession session = Util.getSession();
            session.setAttribute("username", username);
            staff = muab.getStaff(username);
            notificationList = mnb.displayNotificationForStaff(staff);
            notificationListSize = notificationList.size();
            count = mnb.getUnreadForStaff(staff);
            localDateTimeList = new ArrayList();
            timezone = staff.getPlant().getTimeZoneID();
            Iterator<Notification> iterator = notificationList.iterator();
            while (iterator.hasNext()) {
                notification = iterator.next();
                instant = notification.getTime().toInstant();
                localDateTime = LocalDateTime.ofInstant(instant, ZoneId.of(timezone));
                System.err.println(localDateTime);
                localDateTimeList.add(localDateTime);
            }
            privilegeList = msab.getPrivilegeListforStaff(username);
            String absoluteWebPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
            menu = new String();
            existingUrlList = new ArrayList<>();
            Iterator<Privilege> iterator2 = privilegeList.iterator();
            while (iterator2.hasNext()) {
                privilege = iterator2.next();
                urlList = privilege.getMenuLink();
                Iterator<Url> iterator3 = urlList.iterator();
                while (iterator3.hasNext()) {
                    url = iterator3.next();
                    if (url.isVisible() == true && !existingUrlList.contains(url)){
                       existingUrlList.add(url);
                    }
                }
            }
            Collections.sort(existingUrlList);
            Iterator<Url> iterator3 = existingUrlList.iterator();
            while (iterator3.hasNext()) {
                url = iterator3.next();
                menu += "<li><a href="+ absoluteWebPath +url.getLink()+"><i class=\"fa "+url.getIcon()+"\"></i><span>"+url.getMenuItemName()+"</span></a></li>";
            }            
            return "dash";
        } else {
                FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid username or password",""));
            // invalidate session, and redirect to other pages
            //message = "Invalid Login. Please Try Again!";
            return "login";
        }
    }
    
    public void pullNotification() {
        staff = muab.getStaff(username);
        notificationList = mnb.displayNotificationForStaff(staff);
        if (notificationList.size() != notificationListSize){
            count = mnb.getUnreadForStaff(staff);
            notificationListSize = notificationList.size();
            this.setNotificationList(notificationList);
            localDateTimeList = new ArrayList();
            Iterator<Notification> iterator = notificationList.iterator();
            while (iterator.hasNext()) {
                notification = iterator.next();
                instant = notification.getTime().toInstant();
                localDateTime = LocalDateTime.ofInstant(instant, ZoneId.of(timezone));
                localDateTimeList.add(localDateTime);
            }
        }
    }
    
    public void pullCount() {
        count = mnb.getUnreadForStaff(staff);
    }
    
    public void read() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String,String> params = context.getExternalContext().getRequestParameterMap();
        nid = Long.valueOf(params.get("nid"));
        notification = mnb.getNotification(nid);
        if (notification.isIsread() == false){
            count = mnb.getUnreadForStaff(staff);
            mnb.setNotificationToRead(notification);
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

    public List<Notification> getNotificationList() {
        return notificationList;
    }

    public void setNotificationList(List<Notification> notificationList) {
        this.notificationList = notificationList;
    }

    public ManageUserAccountBeanLocal getMuaib() {
        return muab;
    }

    public void setMuaib(ManageUserAccountBeanLocal muab) {
        this.muab = muab;
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

    public Privilege getPrivilege() {
        return privilege;
    }

    public void setPrivilege(Privilege privilege) {
        this.privilege = privilege;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public ManagePrivilegesBeanLocal getMpb() {
        return mpb;
    }

    public void setMpb(ManagePrivilegesBeanLocal mpb) {
        this.mpb = mpb;
    }

    public ManageUserAccountBeanLocal getMuab() {
        return muab;
    }

    public void setMuab(ManageUserAccountBeanLocal muab) {
        this.muab = muab;
    }

    public ManageNotificationsBeanLocal getMnb() {
        return mnb;
    }

    public void setMnb(ManageNotificationsBeanLocal mnb) {
        this.mnb = mnb;
    }

    public Long getNid() {
        return nid;
    }

    public void setNid(Long nid) {
        this.nid = nid;
    }

    public List<LocalDateTime> getLocalDateTimeList() {
        return localDateTimeList;
    }

    public void setLocalDateTimeList(List<LocalDateTime> localDateTimeList) {
        this.localDateTimeList = localDateTimeList;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public Instant getInstant() {
        return instant;
    }

    public void setInstant(Instant instant) {
        this.instant = instant;
    }

    public List<Url> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<Url> urlList) {
        this.urlList = urlList;
    }

    public Url getUrl() {
        return url;
    }

    public void setUrl(Url url) {
        this.url = url;
    }

    public List<Url> getExistingUrlList() {
        return existingUrlList;
    }

    public void setExistingUrlList(List<Url> existingUrlList) {
        this.existingUrlList = existingUrlList;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    
    
}
