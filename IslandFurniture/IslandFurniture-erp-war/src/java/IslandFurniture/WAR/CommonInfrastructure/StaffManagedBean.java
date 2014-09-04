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
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Named
@SessionScoped
public class StaffManagedBean implements Serializable {
    private static final long serialVersionUID = 5443351151396868724L;
    private Staff staff;
    private String username = null;
    private String password = null;
    private String name = null;
    private String notes = null;
    private String phoneNo = null;
    private String emailAddress = null;
    private String description = null;
    private Date lastLogon = null;
    private List<Todo> todoList;
    private List<MessageThread> inbox;
    private Message message;
    private String title = null;
    private String recipients = null;
    
    @EJB
    private ManageAuthenticationBean authBean;
    @EJB
    private ManageUserAccountInformationBean staffBean;
    @EJB
    private ManageTodoBean todoBean;
    @EJB
    private ManageMessagesBean messageBean;
    
    public StaffManagedBean() {
    }
    
    public String login() {
        boolean result = authBean.authenticate(username, password);
        if (result) {
            HttpSession session = Util.getSession();
            session.setAttribute("username", username);
            dash();
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
    
    public void dash() {
        this.staff = staffBean.getStaff(username);
        this.notes = staff.getNotes();
        this.name = staff.getName();
        this.lastLogon = staff.getLastLogon();
        this.phoneNo = staff.getPhoneNo();
        this.emailAddress = staff.getEmailAddress();
        this.todoList = staff.getTodoList();
        this.inbox = staff.getInbox();
    }
    
    public String modifyNotes() {
      HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
      notes = request.getParameter("notesForm:notes");
      staffBean.modifyNote(username, notes);
      dash();
      return "dash";
    }
    
    public String modifyPersonalParticulars() {
      staffBean.modifyPersonalParticulars(username, phoneNo, emailAddress);
      return "modifyparticulars";
    }
    
    public String addTodo() {
      HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
      description = request.getParameter("todoForm:description");
      todoBean.addTodoItem(username, description);
      dash();
      return "dash";
    }
    
    public String addThread() {
      HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
      title = request.getParameter("threadForm:title");
      recipients = request.getParameter("threadForm:recipients");
      recipients += ","+username;
      messageBean.createNewThread(title, recipients);
      dash();
      return "messaging";
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

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
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

    public List<MessageThread> getInbox() {
        return inbox;
    }

    public void setInbox(List<MessageThread> inbox) {
        this.inbox = inbox;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRecipient() {
        return recipients;
    }

    public void setRecipient(String recipient) {
        this.recipients = recipient;
    }

    public ManageMessagesBean getMessageBean() {
        return messageBean;
    }

    public void setMessageBean(ManageMessagesBean messageBean) {
        this.messageBean = messageBean;
    }

    public String getRecipients() {
        return recipients;
    }

    public void setRecipients(String recipients) {
        this.recipients = recipients;
    }

    

}
