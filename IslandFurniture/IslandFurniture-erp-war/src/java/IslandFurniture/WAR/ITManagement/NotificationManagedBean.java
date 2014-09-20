/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.ITManagement;

import IslandFurniture.EJB.CommonInfrastructure.ManageNotificationsBeanLocal;
import IslandFurniture.EJB.Entities.Notification;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Benjamin
 */
@ManagedBean
@ViewScoped
public class NotificationManagedBean {

    private String username;
    private List<Notification> notificationList;
    private Notification notification;
    private LocalDateTime localDateTime;
    private Instant instant;
    private List<LocalDateTime> localDateTimeList;
    private Long id;
    
    @EJB
    private ManageNotificationsBeanLocal mnb;
    
    @PostConstruct
    public void init(){
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        notificationList = mnb.getAllNotifications();
        localDateTimeList = new ArrayList();
        Iterator<Notification> iterator = notificationList.iterator();
        while (iterator.hasNext()) {
            notification = iterator.next();
            instant = notification.getTime().toInstant();
            localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            localDateTimeList.add(localDateTime);
        }
    }
    
    public String deleteNotification(){
        id = new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
        mnb.deleteNotification(id);
        return "notification";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ManageNotificationsBeanLocal getMnb() {
        return mnb;
    }

    public void setMnb(ManageNotificationsBeanLocal mnb) {
        this.mnb = mnb;
    }

    public List<Notification> getNotificationList() {
        return notificationList;
    }

    public void setNotificationList(List<Notification> notificationList) {
        this.notificationList = notificationList;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
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

    public List<LocalDateTime> getLocalDateTimeList() {
        return localDateTimeList;
    }

    public void setLocalDateTimeList(List<LocalDateTime> localDateTimeList) {
        this.localDateTimeList = localDateTimeList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    
    
}
