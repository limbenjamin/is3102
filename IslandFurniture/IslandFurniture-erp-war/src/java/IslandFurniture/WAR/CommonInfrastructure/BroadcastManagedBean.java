/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.CommonInfrastructure;

import IslandFurniture.EJB.CommonInfrastructure.ManageAnnouncementsBeanLocal;
import IslandFurniture.EJB.CommonInfrastructure.ManageEventsBeanLocal;
import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.Entities.Announcement;
import IslandFurniture.Entities.Event;
import IslandFurniture.Entities.Plant;
import IslandFurniture.EJB.ITManagement.ManageSystemAuditLogBeanLocal;
import IslandFurniture.StaticClasses.TimeMethods;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Benjamin
 */
@ManagedBean
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
    private Announcement announcement;
    private Event event;
    private String timeZone;
    private Plant plant;
    
    @EJB
    private ManageAnnouncementsBeanLocal announcementBean;
    @EJB
    private ManageEventsBeanLocal eventBean;
    @EJB
    private ManageUserAccountBeanLocal muaib;
    @EJB
    private ManageSystemAuditLogBeanLocal msalb;
    
    @PostConstruct
    public void init(){
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        announcementList = announcementBean.getMyAnnouncements(username);
        eventList = eventBean.getMyEvents(username);
        timeZone = muaib.getStaff(username).getPlant().getTimeZoneID();
        plant = muaib.getStaff(username).getPlant();
    }
    
    public String addAnnouncement() throws ParseException {
      HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
      title = request.getParameter("announcementForm:title");
      content = request.getParameter("announcementForm:content");
      activeDateString = request.getParameter("announcementForm:activeDateString");
      expireDateString = request.getParameter("announcementForm:expireDateString");
      activeDate = new SimpleDateFormat("yyyy-MM-dd").parse(activeDateString);
      expireDate = new SimpleDateFormat("yyyy-MM-dd").parse(expireDateString);
      Calendar activecal=Calendar.getInstance();
      activecal.setTime(activeDate);
      Calendar expirecal=Calendar.getInstance();
      expirecal.setTime(expireDate);
      id = announcementBean.addAnnouncement(username, title, content, activecal, expirecal);
      msalb.log("Announcement", id, "CREATE", "Title: "+title+" Content: "+content, username);
      announcementList = announcementBean.getMyAnnouncements(username);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Announcement added",""));
      return "broadcast";
    }
    
    public String editAnnouncement(ActionEvent event) throws IOException, ParseException {
      HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
      announcement = (Announcement) event.getComponent().getAttributes().get("toEdit");
      announcementBean.editAnnouncement(announcement.getId(),announcement.getTitle(),announcement.getContent(),announcement.getActiveDate(),announcement.getExpireDate());
      msalb.log("Announcement", announcement.getId(), "MODIFY", "Title: "+announcement.getTitle()+" Content: "+announcement.getContent(), username);
      announcementList = announcementBean.getMyAnnouncements(username);
          FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
              new FacesMessage(FacesMessage.SEVERITY_INFO, "Announcement edited",""));
      return "broadcast";
    }
    
    public String deleteAnnouncement(){
      id = new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
      announcement = announcementBean.getAnnouncement(id);
      announcementBean.deleteAnnouncement(id);
      msalb.log("Announcement", announcement.getId(), "DELETE", "Title: "+announcement.getTitle()+" Content: "+announcement.getContent(), username);
      announcementList = announcementBean.getMyAnnouncements(username);
          FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
              new FacesMessage(FacesMessage.SEVERITY_INFO, "Announcement deleted",""));
      return "broadcast";
    }
    
    public String addEvent() throws ParseException{
      HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
      name = request.getParameter("eventForm:name");
      description = request.getParameter("eventForm:description");
      eventTimeString = request.getParameter("eventForm:eventTimeString");
      DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
      Date date = (Date)formatter.parse(eventTimeString); 
      Calendar cal=Calendar.getInstance();
      cal.setTime(date);
      cal = TimeMethods.convertToUtcTime(plant, cal);
      eventTime = cal;
      id = eventBean.addEvent(name, description, eventTime, username);
      msalb.log("Event", id, "CREATE", "Name: "+name+" Description: "+description, username);
      eventList = eventBean.getMyEvents(username);
          FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
              new FacesMessage(FacesMessage.SEVERITY_INFO, "Event added",""));
      return "broadcast";
    }
    
    //Aevent used for action event since event already used 
    public String editEvent(ActionEvent Aevent) throws IOException, ParseException{
      HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
      event = (Event) Aevent.getComponent().getAttributes().get("toEdit");
      eventBean.editEvent(event.getName(), event.getDescription(), event.getEventTime(), event.getId());
      msalb.log("Event", event.getId(), "MODIFY", "Name: "+event.getName()+" Description: "+event.getDescription(), username);
      eventList = eventBean.getMyEvents(username);
          FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
              new FacesMessage(FacesMessage.SEVERITY_INFO, "Event edited",""));
      return "broadcast";
    }
    
    public String deleteEvent(){
      id = new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
      event = eventBean.getEvent(id);
      eventBean.deleteEvent(id);
      msalb.log("Event", id, "DELETE", "Name: "+event.getName()+" Description: "+event.getDescription(), username);
      eventList = eventBean.getMyEvents(username);
          FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
              new FacesMessage(FacesMessage.SEVERITY_INFO, "Event deleted",""));
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

    public ManageAnnouncementsBeanLocal getAnnouncementBean() {
        return announcementBean;
    }

    public void setAnnouncementBean(ManageAnnouncementsBeanLocal announcementBean) {
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

    public ManageEventsBeanLocal getEventBean() {
        return eventBean;
    }

    public void setEventBean(ManageEventsBeanLocal eventBean) {
        this.eventBean = eventBean;
    }

    public Announcement getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(Announcement announcement) {
        this.announcement = announcement;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public ManageUserAccountBeanLocal getMuaib() {
        return muaib;
    }

    public void setMuaib(ManageUserAccountBeanLocal muaib) {
        this.muaib = muaib;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

 
    


    
    
    
}
