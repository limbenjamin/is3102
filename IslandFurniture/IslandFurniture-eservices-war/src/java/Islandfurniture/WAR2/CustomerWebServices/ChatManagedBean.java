/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Islandfurniture.WAR2.CustomerWebServices;

import IslandFurniture.EJB.CustomerWebService.ManageLocalizationBeanLocal;
import IslandFurniture.EJB.CustomerWebService.ManageShoppingListBeanLocal;
import IslandFurniture.EJB.OperationalCRM.CustomerCommunicationBeanLocal;
import IslandFurniture.EJB.OperationalCRM.ManageMarketingBeanLocal;
import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.CustChatMessage;
import IslandFurniture.Entities.CustChatThread;
import IslandFurniture.Entities.Customer;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
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
public class ChatManagedBean {


    
    private CustChatMessage ccm;
    private CustChatThread cct;
    private List<CustChatThread> activeCCT;
    private List<CustChatMessage> listCCM;
    private String emailAddress = null;
    private Customer customer;
    private String coDir;
    private CountryOffice co;
    private Long threadId;
    
    @EJB
    private ManageLocalizationBeanLocal manageLocalizationBean;    
    @EJB
    private ManageShoppingListBeanLocal mslbl;
    @EJB
    private ManageMarketingBeanLocal mmbl;
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
    
    public void startChatSession(){
        if(customer == null){
            
            threadId = ccb.createAnonymousThread(co);
        }else{

        }
    }
    
    public void sendMessage(String content){
        if(customer == null){
            ccb.postMessage(threadId, content, Boolean.FALSE);
        }else{
   
        }
    }

    public CustChatMessage getCcm() {
        return ccm;
    }

    public void setCcm(CustChatMessage ccm) {
        this.ccm = ccm;
    }

    public CustChatThread getCct() {
        return cct;
    }

    public void setCct(CustChatThread cct) {
        this.cct = cct;
    }

    public List<CustChatThread> getActiveCCT() {
        return activeCCT;
    }

    public void setActiveCCT(List<CustChatThread> activeCCT) {
        this.activeCCT = activeCCT;
    }

    public List<CustChatMessage> getListCCM() {
        return listCCM;
    }

    public void setListCCM(List<CustChatMessage> listCCM) {
        this.listCCM = listCCM;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getCoDir() {
        return coDir;
    }

    public void setCoDir(String coDir) {
        this.coDir = coDir;
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

    public ManageMarketingBeanLocal getMmbl() {
        return mmbl;
    }

    public void setMmbl(ManageMarketingBeanLocal mmbl) {
        this.mmbl = mmbl;
    }

    public CountryOffice getCo() {
        return co;
    }

    public void setCo(CountryOffice co) {
        this.co = co;
    }

    public Long getThreadId() {
        return threadId;
    }

    public void setThreadId(Long threadId) {
        this.threadId = threadId;
    }

    public CustomerCommunicationBeanLocal getCcb() {
        return ccb;
    }

    public void setCcb(CustomerCommunicationBeanLocal ccb) {
        this.ccb = ccb;
    }
    
    
    
    
}
