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
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Named
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

    private List<Announcement> announcementList = null;
    
    @EJB
    private ManageAuthenticationBean authBean;
    @EJB
    private ManageUserAccountInformationBean staffBean;
    @EJB
    private ManageTodoBean todoBean;
    @EJB
    private ManageAnnouncementsBean announcementBean; 
    @EJB
    private ManageEventsBean eventBean;  
    
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

    public ManageTodoBean getTodoBean() {
        return todoBean;
    }

    public void setTodoBean(ManageTodoBean todoBean) {
        this.todoBean = todoBean;
    }

    public ManageAnnouncementsBean getAnnouncementBean() {
        return announcementBean;
    }

    public void setAnnouncementBean(ManageAnnouncementsBean announcementBean) {
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

    public ManageEventsBean getEventBean() {
        return eventBean;
    }

    public void setEventBean(ManageEventsBean eventBean) {
        this.eventBean = eventBean;
    }
    

}
