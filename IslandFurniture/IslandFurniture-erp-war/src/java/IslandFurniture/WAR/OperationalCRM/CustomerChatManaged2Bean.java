/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.OperationalCRM;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.EJB.OperationalCRM.CustomerCommunicationBeanLocal;
import IslandFurniture.Entities.CustChatMessage;
import IslandFurniture.Entities.CustChatThread;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Staff;
import IslandFurniture.WAR.CommonInfrastructure.Util;
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
public class CustomerChatManaged2Bean implements Serializable {
    
    private Long id = null;
    private String username = null;
    private Staff staff;
    private Plant plant;
    private List<CustChatMessage> messageList;
    private CustChatThread cct;
    private String timezone;
    
    @EJB
    ManageUserAccountBeanLocal staffBean;
    @EJB
    CustomerCommunicationBeanLocal ccb;
    
    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        staff = staffBean.getStaff(username);
        plant = staff.getPlant();
        timezone = plant.getTimeZoneID();
        try {
            id = new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
            session.setAttribute("customerthreadid", id);
        } catch (Exception e) {
            id = (Long) session.getAttribute("customerthreadid");
        }
        cct = ccb.getThread(id);
        messageList = cct.getMessages();
    }
    
    public String displayMessages() {
        HttpSession session = Util.getSession();
        id = (Long) session.getAttribute("customerthreadid");
        cct = ccb.getThread(id);
        messageList = cct.getMessages();
        ccb.readThread(cct);
        return "customerchat2";
    }
    
    public String sendMessage(){
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String content = request.getParameter("AddMessageForm:content");
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        staff = staffBean.getStaff(username);
        ccb.postMessage(id, content, Boolean.TRUE, staff);
        return "customerchat";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    public List<CustChatMessage> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<CustChatMessage> messageList) {
        this.messageList = messageList;
    }

    public CustChatThread getCct() {
        return cct;
    }

    public void setCct(CustChatThread cct) {
        this.cct = cct;
    }

    public ManageUserAccountBeanLocal getStaffBean() {
        return staffBean;
    }

    public void setStaffBean(ManageUserAccountBeanLocal staffBean) {
        this.staffBean = staffBean;
    }

    public CustomerCommunicationBeanLocal getCcb() {
        return ccb;
    }

    public void setCcb(CustomerCommunicationBeanLocal ccb) {
        this.ccb = ccb;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
    
    
    
}
