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
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

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

    @EJB
    public ManageMembershipLocal membershipBean;

    @PostConstruct
    public void init() {
        customerList = membershipBean.viewCustomers();
    }

//  Function: To create a Customer
    public void addCustomer(ActionEvent event) throws IOException {
//        if (storeSectionBean.checkIfNoStoreSectionWithSameNameAndLevel(plant, name, level)) {
            membershipBean.createCustomerAccount(emailAddress, null, name, phoneNo, address, dateOfBirth);
            customerList = membershipBean.viewCustomers();

            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Customer has sucessfully been created", ""));
//        } else {
//            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
//                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "There is an existing Store Section with that name and level. Creation of Store Section was unsuccessful.", ""));
//        }
    }

//  Function: To edit a Customer
    public void editCustomer(ActionEvent event) throws IOException {
        Customer customer = (Customer) event.getComponent().getAttributes().get("customer");
        membershipBean.editCustomerAccount(customer);
        customerList = membershipBean.viewCustomers();
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
}
