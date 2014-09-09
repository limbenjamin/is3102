/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.CommonInfrastructure;

import IslandFurniture.EJB.CommonInfrastructure.ManageMessagesBean;
import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountInformationBean;
import IslandFurniture.EJB.Entities.Message;
import IslandFurniture.EJB.Entities.MessageThread;
import IslandFurniture.EJB.Entities.Staff;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
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
public class MessagingManaged2Bean implements Serializable {

    private Long id = null;
    private String username = null;
    private List<MessageThread> inbox;
    private Message message;
    private String title = null;
    private String recipients = null;
    private Staff staff;
    private MessageThread messageThread;
    private List<Message> messageList;
    private String content;
    private Integer messageListSize;
    
    @EJB
    private ManageMessagesBean messageBean;
    @EJB
    private ManageUserAccountInformationBean staffBean;
    
    @PostConstruct
    public void init(){
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        try{
            id = new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
            session.setAttribute("threadid", id);
        }catch (Exception e){
            
        }
        if (!session.getAttribute("threadid").equals(null)){
            id = (Long) session.getAttribute("threadid");
            messageThread = messageBean.getMessageThread(id);
            messageList = messageThread.getMessages();
            messageListSize = messageList.size();
        }
    }
    
    public String displayMessages() {
      HttpSession session = Util.getSession();
      id = (Long) session.getAttribute("threadid");
      messageThread = messageBean.getMessageThread(id);
      messageList = messageThread.getMessages();
      return "messaging2";
    }
    
    public String addMessage() {
      HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
      HttpSession session = Util.getSession();
      id = (Long) session.getAttribute("threadid");
      content = request.getParameter("AddMessageForm:content");
      messageBean.sendMessage(username, id, content);
      return "messaging2";
    }
 
    public void pullMessage() {
        HttpSession session = Util.getSession();
        id = (Long) session.getAttribute("threadid");
        messageThread = messageBean.getMessageThread(id);
        messageList = messageThread.getMessages();     
        if (messageList.size() != messageListSize){
            message = messageList.get(messageList.size()-1);
            System.out.println(message.getContent());
            this.setMessage(message);
        }
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
        System.err.print("getting " +message);
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

    public ManageMessagesBean getMessageBean() {
        return messageBean;
    }

    public void setMessageBean(ManageMessagesBean messageBean) {
        this.messageBean = messageBean;
    }

    public ManageUserAccountInformationBean getStaffBean() {
        return staffBean;
    }

    public void setStaffBean(ManageUserAccountInformationBean staffBean) {
        this.staffBean = staffBean;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MessageThread getMessageThread() {
        return messageThread;
    }

    public void setMessageThread(MessageThread messageThread) {
        this.messageThread = messageThread;
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getMessageListSize() {
        return messageListSize;
    }

    public void setMessageListSize(Integer messageListSize) {
        this.messageListSize = messageListSize;
    }
    
    
    
}
