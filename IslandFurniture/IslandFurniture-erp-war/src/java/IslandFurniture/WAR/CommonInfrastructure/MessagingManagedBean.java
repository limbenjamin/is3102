/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.CommonInfrastructure;

import IslandFurniture.EJB.CommonInfrastructure.ManageMessagesBeanLocal;
import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.Entities.Message;
import IslandFurniture.Entities.MessageThread;
import IslandFurniture.Entities.Staff;
import IslandFurniture.EJB.ITManagement.ManageStaffAccountsBeanLocal;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Benjamin
 */
@ManagedBean
@ViewScoped
public class MessagingManagedBean implements Serializable {

    private Long id = null;
    private String username = null;
    private List<MessageThread> inbox;
    private Message message;
    private String title = null;
    private String recipients = "";
    private Staff staff;
    private List<Staff> staffList;
    private String[] selectedRecipients;

    @EJB
    private ManageMessagesBeanLocal messageBean;
    @EJB
    private ManageUserAccountBeanLocal staffBean;
    @EJB
    private ManageStaffAccountsBeanLocal msaBean;

    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        this.inbox = messageBean.displayAllThreads(username);
        staffList = msaBean.displayAllStaffAccounts();
    }

    public String addThread() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        title = request.getParameter("threadForm:title");

        for (String selectedItem : selectedRecipients) {
            System.out.println("Selected item: " + selectedItem);
            recipients += selectedItem + ",";
        }

        System.out.println("selected recipients: " + recipients);
        recipients += username;
        messageBean.createNewThread(title, recipients);
        recipients = "";
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Thread added", ""));
        return "messaging";
    }

    public String unsubThread() {
        id = new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
        messageBean.unsubscribeFromThread(username, id);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Unsubscribed from thread", ""));
        return "messaging";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getRecipients() {
        return recipients;
    }

    public void setRecipients(String recipients) {
        this.recipients = recipients;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public ManageMessagesBeanLocal getMessageBean() {
        return messageBean;
    }

    public void setMessageBean(ManageMessagesBeanLocal messageBean) {
        this.messageBean = messageBean;
    }

    public ManageUserAccountBeanLocal getStaffBean() {
        return staffBean;
    }

    public void setStaffBean(ManageUserAccountBeanLocal staffBean) {
        this.staffBean = staffBean;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Staff> getStaffList() {
        return staffList;
    }

    public void setStaffList(List<Staff> staffList) {
        this.staffList = staffList;
    }

    public ManageStaffAccountsBeanLocal getMsaBean() {
        return msaBean;
    }

    public void setMsaBean(ManageStaffAccountsBeanLocal msaBean) {
        this.msaBean = msaBean;
    }

    public String[] getSelectedRecipients() {
        return selectedRecipients;
    }

    public void setSelectedRecipients(String[] selectedRecipients) {
        this.selectedRecipients = selectedRecipients;
    }

}
