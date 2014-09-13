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
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@ManagedBean
@ViewScoped
public class DashManagedBean implements Serializable {
    private static final long serialVersionUID = 5443351151396868724L;
    private Staff staff;
    private String username = null;
    private String password = null;
    private String name = null;
    private String notes = null;
    private String description = null;
    private Date lastLogon = null;
    private List<Todo> todoList;
    private List<Event> eventList;
    private Event event;
    private LocalDateTime localDateTime;
    private Instant instant;
    private List<LocalDateTime> localDateTimeList;

    private List<Announcement> announcementList = null;
    
    @EJB
    private ManageAuthenticationBeanLocal authBean;
    @EJB
    private ManageUserAccountInformationBean staffBean;
    @EJB
    private ManageTodoBeanLocal todoBean;
    @EJB
    private ManageAnnouncementsBeanLocal announcementBean; 
    @EJB
    private ManageEventsBeanLocal eventBean;  
    
    @PostConstruct
    public void init(){
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        this.staff = staffBean.getStaff(username);
        this.notes = staff.getNotes();
        this.name = staff.getName();
        this.lastLogon = staff.getLastLogon();
        this.todoList = staff.getTodoList();
        this.announcementList = announcementBean.getActiveAnnouncements();
        this.eventList = eventBean.getEvents();
        Iterator<Event> iterator = eventList.iterator();
        localDateTimeList = new ArrayList();
        while (iterator.hasNext()) {
            event = iterator.next();
            instant = event.getEventTime().toInstant();
            localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            localDateTimeList.add(localDateTime);
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
    
    public String addTodo() {
      HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
      description = request.getParameter("todoForm:description");
      todoBean.addTodoItem(username, description);
      return "dash";
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

    public Date getLastLogon() {
        return lastLogon;
    }

    public void setLastLogon(Date lastLogon) {
        this.lastLogon = lastLogon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Todo> getTodoList() {
        return todoList;
    }

    public void setTodoList(List<Todo> todoList) {
        this.todoList = todoList;
    }

    public ManageTodoBeanLocal getTodoBean() {
        return todoBean;
    }

    public void setTodoBean(ManageTodoBeanLocal todoBean) {
        this.todoBean = todoBean;
    }

    public ManageAnnouncementsBeanLocal getAnnouncementBean() {
        return announcementBean;
    }

    public void setAnnouncementBean(ManageAnnouncementsBeanLocal announcementBean) {
        this.announcementBean = announcementBean;
    }

    public List<Announcement> getAnnouncementList() {
        return announcementList;
    }

    public void setAnnouncementList(List<Announcement> announcementList) {
        this.announcementList = announcementList;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public List<Event> getEventList() {
        return eventList;
    }

    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }

    public ManageEventsBeanLocal getEventBean() {
        return eventBean;
    }

    public void setEventBean(ManageEventsBeanLocal eventBean) {
        this.eventBean = eventBean;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
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
    
    
    
}
