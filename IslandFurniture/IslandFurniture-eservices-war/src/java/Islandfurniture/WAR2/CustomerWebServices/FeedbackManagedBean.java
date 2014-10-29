/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Islandfurniture.WAR2.CustomerWebServices;

import IslandFurniture.EJB.CustomerWebService.ManageLocalizationBeanLocal;
import IslandFurniture.EJB.CustomerWebService.ManageShoppingListBeanLocal;
import IslandFurniture.EJB.OperationalCRM.CustomerCommunicationBeanLocal;
import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.Customer;
import IslandFurniture.StaticClasses.SendEmailByPost;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Benjamin
 */
@ManagedBean
@ViewScoped
public class FeedbackManagedBean {

    private String emailAddress = null;
    private String name = null;
    private String phoneNo = null;
    private String content;
    private Long id;
    private String coDir;
    private CountryOffice co;
    private Customer customer;
    
    @EJB
    private ManageLocalizationBeanLocal manageLocalizationBean;
    @EJB
    private ManageShoppingListBeanLocal mslbl;
    @EJB
    private CustomerCommunicationBeanLocal ccb;
    
    @PostConstruct
    public void init(){
        HttpSession session = Util.getSession();
        emailAddress = (String) session.getAttribute("emailAddress");
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest httpReq = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        co = manageLocalizationBean.findCoByCode((String) httpReq.getAttribute("coCode"));
        coDir = (String) httpReq.getAttribute("coCode");
        if(coDir !=null && !coDir.isEmpty()){
            coDir = "/"+ coDir;
        }
        
        if (emailAddress == null) {
            
        }   
        else {
            customer = mslbl.getCustomer(emailAddress);
        }
    }
    
    public void submitFeedback() throws IOException{
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) ec.getRequest();
        content = request.getParameter("feedbackForm:content");
        if(customer == null){
            name = request.getParameter("feedbackForm:name");
            emailAddress = request.getParameter("feedbackForm:emailAddress");
            if (!emailAddress.isEmpty()){
                try {
                    SendEmailByPost.sendEmail("customerservice", emailAddress, "Feedback Received", "We have received your feedback and will get in touch with you shortly");
                } catch (Exception ex) {
                    Logger.getLogger(FeedbackManagedBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            phoneNo = request.getParameter("feedbackForm:phoneNo");
            ccb.createAnonymousFeedback(name, emailAddress, phoneNo, content, co);
        }else{
            ccb.createFeedback(customer, content, co);
            try {
                SendEmailByPost.sendEmail("customerservice", customer.getEmailAddress(), "Feedback Received", "We have received your feedback and will get in touch with you shortly");
            } catch (Exception ex) {
                Logger.getLogger(FeedbackManagedBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
        coDir = ec.getRequestParameterMap().get("coCode");
        if (coDir == null || coDir.isEmpty()) {
            coDir = "";
        } else {
            coDir = "/" + coDir;
        }
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
             new FacesMessage(FacesMessage.SEVERITY_INFO, "Feedback sent. We will get back to you shortly",""));
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCoDir() {
        return coDir;
    }

    public void setCoDir(String coDir) {
        this.coDir = coDir;
    }

    public CountryOffice getCo() {
        return co;
    }

    public void setCo(CountryOffice co) {
        this.co = co;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public ManageLocalizationBeanLocal getManageLocalizationBean() {
        return manageLocalizationBean;
    }

    public void setManageLocalizationBean(ManageLocalizationBeanLocal manageLocalizationBean) {
        this.manageLocalizationBean = manageLocalizationBean;
    }

    public ManageShoppingListBeanLocal getMslbl() {
        return mslbl;
    }

    public void setMslbl(ManageShoppingListBeanLocal mslbl) {
        this.mslbl = mslbl;
    }

    public CustomerCommunicationBeanLocal getCcb() {
        return ccb;
    }

    public void setCcb(CustomerCommunicationBeanLocal ccb) {
        this.ccb = ccb;
    }
    
    
    
}
