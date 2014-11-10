/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Islandfurniture.WAR2.CustomerWebServices;

import IslandFurniture.EJB.CustomerWebService.ManageMemberAuthenticationBeanLocal;
import IslandFurniture.EJB.CustomerWebService.ManageShoppingListBeanLocal;
import IslandFurniture.Entities.Customer;
import IslandFurniture.Exceptions.DuplicateEntryException;
import IslandFurniture.Exceptions.InvalidInputException;
import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
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
public class ShoppingListInviteManagedBean implements Serializable {

    String hashId;
    String title;
    String emailAddress;
    Customer customer;
    String coDir;

    @EJB
    private ManageMemberAuthenticationBeanLocal mmab;
    @EJB
    private ManageShoppingListBeanLocal mslbl;

    /**
     * Creates a new instance of ShoppingListInviteManagedBean
     */
    public ShoppingListInviteManagedBean() {
    }

    @PostConstruct
    public void init() {
        title = "Shopping list invite";
        HttpSession session = Util.getSession();
        emailAddress = (String) session.getAttribute("emailAddress");
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest httpReq = (HttpServletRequest) ec.getRequest();
        coDir = (String) httpReq.getAttribute("coCode");
        if (coDir != null && !coDir.isEmpty()) {
            coDir = "/" + coDir;
        }
        customer = mmab.getCustomer(emailAddress);
        hashId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");

    }

    public void addToList() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        coDir = ec.getRequestParameterMap().get("coCode");
        hashId = ec.getRequestParameterMap().get("hashId");
        if (coDir != null && !coDir.isEmpty()) {
            coDir = "/" + coDir;
        }

        System.out.println(hashId + " | " + customer);
        try {
            mslbl.addCustomerToShoppingList(hashId, customer);
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "You have been added to the shopping list", ""));
        } catch (InvalidInputException | DuplicateEntryException invex) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, invex.getMessage(), ""));
        } finally {
            try {
                ec.redirect(ec.getRequestContextPath() + coDir + "/member/shoppinglist.xhtml");
            } catch (IOException ex) {

            }
        }
    }

    public String getHashId() {
        return hashId;
    }

    public void setHashId(String hashId) {
        this.hashId = hashId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public ManageMemberAuthenticationBeanLocal getMmab() {
        return mmab;
    }

    public void setMmab(ManageMemberAuthenticationBeanLocal mmab) {
        this.mmab = mmab;
    }

    public ManageShoppingListBeanLocal getMslbl() {
        return mslbl;
    }

    public void setMslbl(ManageShoppingListBeanLocal mslbl) {
        this.mslbl = mslbl;
    }

}
