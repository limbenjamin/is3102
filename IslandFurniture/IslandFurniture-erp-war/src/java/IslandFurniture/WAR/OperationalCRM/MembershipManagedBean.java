/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.OperationalCRM;

import IslandFurniture.EJB.OperationalCRM.ManageMembershipLocal;
import IslandFurniture.Entities.Customer;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;

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

    @EJB
    public ManageMembershipLocal membershipBean;

    @PostConstruct
    public void init() {
        System.err.println("init:MembershipManagedBean");
        customerList = membershipBean.viewCustomers();
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
}
