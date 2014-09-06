/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.CommonInfrastructure;

import IslandFurniture.EJB.CommonInfrastructure.ManageAnnouncementsBean;
import IslandFurniture.EJB.CommonInfrastructure.ManageEventsBean;
import IslandFurniture.EJB.Entities.Announcement;
import IslandFurniture.EJB.Entities.Event;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
public class BroadcastManagedBean implements Serializable {
    
    private Long id = null;
    private String username = null;
    private String title = null;
    private String content = null;
    private Date activeDate = null;
    private Date expireDate = null;
    private String activeDateString = null;
    private String expireDateString = null;
    private List<Announcement> announcementList = null;
    private String name = null;
    private String description = null;
    private Calendar eventTime;
    private String eventTimeString = null;
    private String idString = null;
    private List<Event> eventList = null;
    
    @EJB
    private ManageAnnouncementsBean announcementBean;
    @EJB
    private ManageEventsBean eventBean;
    
    @PostConstruct
    public void init(){
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        announcementList = announcementBean.getMyAnnouncements(username);
        eventList = eventBean.getMyEvents(username);
    }
    
    public String addAnnouncement() throws ParseException {
      HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
      title = request.getParameter("announcementForm:title");
      content = request.getParameter("announcementForm:content");
      activeDateString = request.getParameter("announcementForm:activeDateString");
      expireDateString = request.getParameter("announcementForm:expireDateString");
      activeDate = new SimpleDateFormat("dd-MM-yy").parse(activeDateString);
      expireDate = new SimpleDateFormat("dd-MM-yy").parse(expireDateString);
      announcementBean.addAnnouncement(username, title, content, activeDate, expireDate);
      announcementList = announcementBean.getMyAnnouncements(username);
      return "broadcast";
    }
    
    public String editAnnouncement() throws ParseException {
      HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
      idString = request.getParameter("editAnnouncementForm:id");
      id = Long.valueOf(idString);
      title = request.getParameter("editAnnouncementForm:title");
      content = request.getParameter("editAnnouncementForm:content");
      activeDateString = request.getParameter("editAnnouncementForm:activeDateString");
      expireDateString = request.getParameter("editAnnouncementForm:expireDateString");
      activeDate = new SimpleDateFormat("dd-MM-yy").parse(activeDateString);
      expireDate = new SimpleDateFormat("dd-MM-yy").parse(expireDateString);
      announcementBean.editAnnouncement(id, title, content, activeDate, expireDate);
      announcementList = announcementBean.getMyAnnouncements(username);
      return "broadcast";
    }
    
    public String deleteAnnouncement(){
      id = new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
      announcementBean.deleteAnnouncement(id);
      announcementList = announcementBean.getMyAnnouncements(username);
      return "broadcast";
    }
    
    public String addEvent() throws ParseException{
      HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
      name = request.getParameter("eventForm:name");
      description = request.getParameter("eventForm:description");
      eventTimeString = request.getParameter("eventForm:eventTimeString");
      DateFormat formatter = new SimpleDateFormat("dd-MM-yy HH:mm");
      Date date = (Date)formatter.parse(eventTimeString); 
      Calendar cal=Calendar.getInstance();
      cal.setTime(date);
      eventTime = cal;
      eventBean.addEvent(name, description, eventTime, username);
      eventList = eventBean.getMyEvents(username);
      return "broadcast";
    }
    
    public String editEvent() throws ParseException{
      HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
      idString = request.getParameter("editAnnouncementForm:id");
      id = Long.valueOf(idString);
      name = request.getParameter("editEventForm:name");
      description = request.getParameter("editEventForm:description");
      eventTimeString = request.getParameter("editEventForm:eventTimeString");
      DateFormat formatter = new SimpleDateFormat("dd-MM-yy HH:mm");
      Date date = (Date)formatter.parse(eventTimeString); 
      Calendar cal=Calendar.getInstance();
      cal.setTime(date);
      eventTime = cal;
      eventBean.editEvent(name, description, eventTime, id);
      eventList = eventBean.getMyEvents(username);
      return "broadcast";
    }
    
    public String deleteEvent(){
      id = new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
      eventBean.deleteEvent(id);
      eventList = eventBean.getMyEvents(username);
      return "broadcast";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getActiveDate() {
        return activeDate;
    }

    public void setActiveDate(Date activeDate) {
        this.activeDate = activeDate;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public String getActiveDateString() {
        return activeDateString;
    }

    public void setActiveDateString(String activeDateString) {
        this.activeDateString = activeDateString;
    }

    public String getExpireDateString() {
        return expireDateString;
    }

    public void setExpireDateString(String expireDateString) {
        this.expireDateString = expireDateString;
    }

    public List<Announcement> getAnnouncementList() {
        return announcementList;
    }

    public void setAnnouncementList(List<Announcement> announcementList) {
        this.announcementList = announcementList;
    }

    public ManageAnnouncementsBean getAnnouncementBean() {
        return announcementBean;
    }

    public void setAnnouncementBean(ManageAnnouncementsBean announcementBean) {
        this.announcementBean = announcementBean;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Calendar getEventTime() {
        return eventTime;
    }

    public void setEventTime(Calendar eventTime) {
        this.eventTime = eventTime;
    }

    public List<Event> getEventList() {
        return eventList;
    }

    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventTimeString() {
        return eventTimeString;
    }

    public void setEventTimeString(String eventTimeString) {
        this.eventTimeString = eventTimeString;
    }

    public String getIdString() {
        return idString;
    }

    public void setIdString(String idString) {
        this.idString = idString;
    }

    public ManageEventsBean getEventBean() {
        return eventBean;
    }

    public void setEventBean(ManageEventsBean eventBean) {
        this.eventBean = eventBean;
    }
    
    
    
}
