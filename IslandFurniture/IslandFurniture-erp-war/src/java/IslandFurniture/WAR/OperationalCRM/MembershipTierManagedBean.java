/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.OperationalCRM;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Staff;
import IslandFurniture.EJB.OperationalCRM.ManageMembershipLocal;
import IslandFurniture.Entities.MembershipTier;
import IslandFurniture.WAR.CommonInfrastructure.Util;
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
import javax.servlet.http.HttpSession;

/**
 *
 * @author KamilulAshraf
 */
@ManagedBean
@ViewScoped
public class MembershipTierManagedBean implements Serializable {

    private List<MembershipTier> membershipTierList;

    private String username;
    private Staff staff;
    private Plant plant;

    private String title;
    private int points;

    @EJB
    private ManageUserAccountBeanLocal staffBean;
    @EJB
    private ManageMembershipLocal membershipBean;

    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        staff = staffBean.getStaff(username);
        plant = staff.getPlant();
        membershipTierList = membershipBean.viewMembershipTier();
    }

//  Function: To create a Membership Tier
    public void addMembershipTier(ActionEvent event) throws IOException {

        System.out.println("The title is " + title);
        System.out.println("The points is " + points);

        if (membershipBean.checkIfNoMembershipTierSameName(title)) {
            if (membershipBean.checkIfNoMembershipTierSamePoints(points)) {
                membershipBean.createMembershipTier(title, points);
                membershipTierList = membershipBean.viewMembershipTier();
                FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Membership Tier has sucessfully been created", ""));
            } else {
                FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "There is an existing Membership Tier the same number of points. Creation of Membership Tier was unsuccessful.", ""));
            }
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "There is an existing Membership Tier with that title. Creation of Membership Tier was unsuccessful.", ""));
        }
    }

//  Function: To edit a Membership Tier
    public void editMembershipTier(ActionEvent event) throws IOException {
        MembershipTier ss = (MembershipTier) event.getComponent().getAttributes().get("membershipTier");
        membershipBean.editMembershipTier(ss);
        membershipTierList = membershipBean.viewMembershipTier();
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Membership Tier has sucessfully been edited", ""));

    }

    //  Function: To delete a Membership Tier
    public void deleteMembershipTier(ActionEvent event) throws IOException {
        MembershipTier ss = (MembershipTier) event.getComponent().getAttributes().get("membershipTier");

        if (membershipBean.checkIfNoCustomersInTheTier(ss)) {
            membershipBean.deleteMembershipTier(ss);
            membershipTierList = membershipBean.viewMembershipTier();
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Membership Tier has sucessfully been deleted", ""));
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "There are Customers associated with this Membership Tier. Deletion of Membership Tier was unsuccessful.", ""));
        }
    }

    public List<MembershipTier> getMembershipTierList() {
        return membershipTierList;
    }

    public void setMembershipTierList(List<MembershipTier> membershipTierList) {
        this.membershipTierList = membershipTierList;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public ManageUserAccountBeanLocal getStaffBean() {
        return staffBean;
    }

    public void setStaffBean(ManageUserAccountBeanLocal staffBean) {
        this.staffBean = staffBean;
    }

    public ManageMembershipLocal getMembershipBean() {
        return membershipBean;
    }

    public void setMembershipBean(ManageMembershipLocal membershipBean) {
        this.membershipBean = membershipBean;
    }

}
