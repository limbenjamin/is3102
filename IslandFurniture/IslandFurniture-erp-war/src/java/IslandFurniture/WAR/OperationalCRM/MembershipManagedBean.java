/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.OperationalCRM;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.EJB.OperationalCRM.ManageMembershipLocal;
import IslandFurniture.Entities.Customer;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Staff;
import IslandFurniture.Entities.Store;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import IslandFurniture.WAR.Util.NFCMethods;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.TerminalFactory;

/**
 *
 * @author KamilulAshraf
 */
@ManagedBean
@ViewScoped
public class MembershipManagedBean implements Serializable {

    private List<Customer> customerList;
    private String customerCardID;
    private String emailAddress;           
    private String name;
    private String phoneNo;
    private String address;
    private String dateOfBirth;
    private Customer customer;
    private String username;
    private Staff staff;
    private Plant plant;
    private Store store;
    
    private boolean emailForm;
    private boolean cardForm;
    
    

    @EJB
    private ManageMembershipLocal membershipBean;
    @EJB
    private ManageUserAccountBeanLocal staffBean;

    @PostConstruct
    public void init() {
        System.err.println("init:MembershipManagedBean");
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        staff = staffBean.getStaff(username);
        plant = staff.getPlant();
        
        if (plant instanceof Store) {
            this.store = (Store) plant;
            this.emailForm = false; 
            this.cardForm = false;
            String param = (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("param");
            if(param == null) 
                customerList = null;
            else {
                String input = param.substring(0, 4);
                if(input.equals("mail"))
                    customerList = membershipBean.searchMemberByEmail(param.substring(4));
                else if(input.equals("card")) {
                    customerList = new ArrayList<>();
                    customerList.add(membershipBean.getCustomerByCard(param.substring(4)));
                } else
                    customerList = membershipBean.viewCustomers();
            }
        } else {
            try {
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect(ec.getRequestContextPath());
            } catch (IOException ex) {

            }
        }
    }

//  Function: To edit a Customer 
    public void editCustomer(ActionEvent event) throws IOException { 
        System.out.println("MembershipManagedBean.editCustomer()");
        membershipBean.editCustomerAccount(customer);         
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Account details of " + customer.getName() + " has been successfully updated", ""));
        ec.redirect("membership.xhtml"); 
    } 
    
    // Function: To view the member details of a selected customer
    public void viewCustomer(AjaxBehaviorEvent event) {
        System.out.println("MembershipManagedBean.viewCustomer()");
        Long customerID = (Long) event.getComponent().getAttributes().get("customerID");
        this.customer = membershipBean.getCustomer(customerID);
    }
    
    public void scanNFC(AjaxBehaviorEvent event){
        String customerCardId = "";
        CardTerminal acr122uCardTerminal = null;
        Long customerID = (Long) event.getComponent().getAttributes().get("customerID");
        System.err.println(customerID);
        this.customer = membershipBean.getCustomer(customerID);
        try {
            TerminalFactory terminalFactory = TerminalFactory.getDefault();
            if (!terminalFactory.terminals().list().isEmpty()) {
                for (CardTerminal cardTerminal : terminalFactory.terminals().list()) {
                    if (cardTerminal.getName().contains("ACS ACR122")) {
                        acr122uCardTerminal = cardTerminal;
                        break;
                    }
                }
                if (acr122uCardTerminal != null) {
                    try {
                        if (acr122uCardTerminal.isCardPresent()) {
                            NFCMethods nfc = new NFCMethods();
                            customerCardId = (nfc.getID(acr122uCardTerminal)).substring(0, 8);
                            this.customer.setLoyaltyCardId(customerCardId);
                        }
                    } catch (CardException ex) {
                        Logger.getLogger(MembershipManagedBean.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (Exception ex) {
                        Logger.getLogger(MembershipManagedBean.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                }
            } else {
            }
        } catch (Exception ex) {
        }
    }
    
    public void scanNFCWithoutCustomerID(AjaxBehaviorEvent event){
        CardTerminal acr122uCardTerminal = null;
        try {
        System.out.println("MembershipManagedBean.scanNFCWithoutCustomerID()");
            TerminalFactory terminalFactory = TerminalFactory.getDefault();
            if (!terminalFactory.terminals().list().isEmpty()) {
                for (CardTerminal cardTerminal : terminalFactory.terminals().list()) {
                    if (cardTerminal.getName().contains("ACS ACR122")) {
                        acr122uCardTerminal = cardTerminal;
                        break;
                    }
                }
                if (acr122uCardTerminal != null) {
                    try {
                        if (acr122uCardTerminal.isCardPresent()) {
                            NFCMethods nfc = new NFCMethods();
                            this.customerCardID = (nfc.getID(acr122uCardTerminal)).substring(0, 8);
                        }
                    } catch (CardException ex) {
                        Logger.getLogger(MembershipManagedBean.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (Exception ex) {
                        Logger.getLogger(MembershipManagedBean.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                }
            } else {
            }
        } catch (Exception ex) {
        }
    }
    
    public void searchByEmail(AjaxBehaviorEvent event) {
        System.out.println("MembershipManagedBean.searchByEmail()");
        this.cardForm = false;
        this.emailForm = true; 
    } 
    public void searchByCard(AjaxBehaviorEvent event) {
        System.out.println("MembershipManagedBean.searchByCard()");
        this.cardForm = true;
        this.emailForm = false;
    } 
    public void searchEmail(ActionEvent event) throws IOException {
        System.out.println("MembershipManagedBean.searchEmail()");
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        String thisEmail = request.getParameter("searchByEmailForm:emailSearch");
        this.customerList = membershipBean.searchMemberByEmail(thisEmail);
        if(customerList == null || customerList.isEmpty()) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "No such email address exists", ""));  
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("param", null);
        } else { 
            thisEmail = "mail" + thisEmail;
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("param", thisEmail);
        }
        ec.redirect("membership.xhtml");
    } 
    public void searchCard(ActionEvent event) throws IOException {
        System.out.println("MembershipManagedBean.searchCard()");
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        customerCardID = (String) event.getComponent().getAttributes().get("customerCardID");
        if(customerCardID == null || customerCardID.isEmpty()) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "No input detected. Please scan card until Loyalty Card ID appears on the screen", ""));  
        } else {
            this.customer = membershipBean.getCustomerByCard(customerCardID); 
            if(customer == null) {
                FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "No such Card ID exists", ""));  
                FacesContext.getCurrentInstance().getExternalContext().getFlash().put("param", null);
            } else { 
                String thisCard = "card" + customerCardID;
                FacesContext.getCurrentInstance().getExternalContext().getFlash().put("param", thisCard);
            }
        }
        ec.redirect("membership.xhtml");
    }
    
    public void searchAll(ActionEvent event) throws IOException {
        System.out.println("MembershipManagedBean.searchAll()");
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("param", "full");
        ec.redirect("membership.xhtml");        
    }
    

    public List<Customer> getCustomerList() {
        return customerList;
    }

    public void setCustomerList(List<Customer> customerList) {
        this.customerList = customerList;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public String getCustomerCardID() {
        return customerCardID;
    }

    public void setCustomerCardID(String customerCardId) {
        this.customerCardID = customerCardId;
    }

    public boolean isEmailForm() {
        return emailForm;
    }

    public void setEmailForm(boolean emailForm) {
        this.emailForm = emailForm;
    }

    public boolean isCardForm() {
        return cardForm;
    }

    public void setCardForm(boolean cardForm) {
        this.cardForm = cardForm;
    }
    
}
