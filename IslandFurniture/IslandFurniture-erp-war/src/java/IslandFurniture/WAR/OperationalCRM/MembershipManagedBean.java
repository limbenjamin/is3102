/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.OperationalCRM;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.EJB.OperationalCRM.ManageMembershipLocal;
import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.Customer;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Staff;
import IslandFurniture.Entities.Store;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import IslandFurniture.WAR.Util.NFCMethods;
import java.io.IOException;
import java.io.Serializable;
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

    @EJB
    public ManageMembershipLocal membershipBean;
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
            customerList = membershipBean.viewCustomers();
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

    public ManageMembershipLocal getMembershipBean() {
        return membershipBean;
    }

    public void setMembershipBean(ManageMembershipLocal membershipBean) {
        this.membershipBean = membershipBean;
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
}
