/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.ITManagement;

import IslandFurniture.EJB.CommonInfrastructure.ManageAuthenticationBeanLocal;
import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.EJB.ITManagement.ManageOrganizationalHierarchyBeanLocal;
import IslandFurniture.EJB.ITManagement.ManageRolesBeanLocal;
import IslandFurniture.EJB.ITManagement.ManageStaffAccountsBeanLocal;
import IslandFurniture.Entities.Country;
import IslandFurniture.Entities.GlobalHQ;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Role;
import IslandFurniture.Entities.Staff;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
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
public class StaffManagedBean  implements Serializable  {
    
    private String username;
    private List<Staff> staffList;
    private String password;
    private String name;
    private String emailAddress; 
    private String phoneNo;
    private String countryName;
    private String plantName;
    private List<Country> countryList;
    private List<Plant> plantList;
    private Long id;
    private Staff staff;
    private Plant plant;
    private boolean isGlobalHq = Boolean.FALSE;
    private String[] selectedRoles;
    private List<Role> roleList;
    private Role role;
    private String cardId;
    
    @EJB
    private ManageStaffAccountsBeanLocal msabl;
    @EJB
    private ManageOrganizationalHierarchyBeanLocal mohBean;
    @EJB
    private ManageUserAccountBeanLocal muab;
    @EJB
    private ManageAuthenticationBeanLocal mabl;
    @EJB
    private ManageRolesBeanLocal mrbl;
    
    @PostConstruct
    public void init(){
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        staff = muab.getStaff(username);
        //Global HQ can manage staff in all plants
        if (staff.getPlant() instanceof GlobalHQ){
            staffList = msabl.displayAllStaffAccounts();
            isGlobalHq = Boolean.TRUE;
        }else{
            staffList = msabl.displayStaffAccountsFromPlant(username);
        }
        countryList = mohBean.getCountries();
        plantList = mohBean.displayPlant();
        roleList = mrbl.displayRole();
    }
    
    public String createStaff(){
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        username = request.getParameter("staffForm:username");
        name = request.getParameter("staffForm:name");
        password = Long.toHexString(Double.doubleToLongBits(Math.random())).substring(2);
        emailAddress = request.getParameter("staffForm:emailAddress");
        phoneNo = request.getParameter("staffForm:phoneNo");
        cardId = request.getParameter("staffForm:cardId");
        HttpSession session = Util.getSession();
        staff = muab.getStaff((String) session.getAttribute("username"));
        countryName = staff.getPlant().getCountry().getName();
        plantName = staff.getPlant().getName();
        id = msabl.createStaffAccount(username, password, name, emailAddress, phoneNo, countryName, plantName, cardId);
        for (String selected : selectedRoles) {
            System.out.println("Selected item: " + selected); 
            msabl.addRoleToStaffByUsername(username,selected);
        }
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Staff created",""));
        return "managestaff";
    }
    
    public String createStaffWithPlant(){
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        username = request.getParameter("globalStaffForm:username");
        name = request.getParameter("globalStaffForm:name");
        password = Long.toHexString(Double.doubleToLongBits(Math.random())).substring(2);
        emailAddress = request.getParameter("globalStaffForm:emailAddress");
        phoneNo = request.getParameter("globalStaffForm:phoneNo");
        plantName = request.getParameter("globalStaffForm:plantName");
        cardId = request.getParameter("globalStaffForm:cardId");
        plant = mohBean.findPlantByNameOnly(plantName);
        countryName = plant.getCountry().getName();
        msabl.createStaffAccount(username, password, name, emailAddress, phoneNo, countryName, plantName, cardId);
        for (String selected : selectedRoles) {
            System.out.println("Selected item: " + selected); 
            msabl.addRoleToStaffByUsername(username,selected);
        }
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Staff created",""));
        return "managestaff";
    }
    
    public String deleteStaff(){
        id = new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
        msabl.deleteStaffAccount(id);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Staff deleted",""));
        return "managestaff";
    }
    
    public String resetpassword(){
        id = new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
        mabl.resetPasswordByAdmin(id);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Password reseted",""));
        return "managestaff";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Staff> getStaffList() {
        return staffList;
    }

    public void setStaffList(List<Staff> staffList) {
        this.staffList = staffList;
    }

    public ManageStaffAccountsBeanLocal getMsabl() {
        return msabl;
    }

    public void setMsabl(ManageStaffAccountsBeanLocal msabl) {
        this.msabl = msabl;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getPlantName() {
        return plantName;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public List<Country> getCountryList() {
        return countryList;
    }

    public void setCountryList(List<Country> countryList) {
        this.countryList = countryList;
    }

    public List<Plant> getPlantList() {
        return plantList;
    }

    public void setPlantList(List<Plant> plantList) {
        this.plantList = plantList;
    }

    public ManageOrganizationalHierarchyBeanLocal getMohBean() {
        return mohBean;
    }

    public void setMohBean(ManageOrganizationalHierarchyBeanLocal mohBean) {
        this.mohBean = mohBean;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public ManageUserAccountBeanLocal getMuab() {
        return muab;
    }

    public void setMuab(ManageUserAccountBeanLocal muab) {
        this.muab = muab;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    public ManageAuthenticationBeanLocal getMabl() {
        return mabl;
    }

    public void setMabl(ManageAuthenticationBeanLocal mabl) {
        this.mabl = mabl;
    }

    public boolean isIsGlobalHq() {
        return isGlobalHq;
    }

    public void setIsGlobalHq(boolean isGlobalHq) {
        this.isGlobalHq = isGlobalHq;
    }

    public String[] getSelectedRoles() {
        return selectedRoles;
    }

    public void setSelectedRoles(String[] selectedRoles) {
        this.selectedRoles = selectedRoles;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    public ManageRolesBeanLocal getMrbl() {
        return mrbl;
    }

    public void setMrbl(ManageRolesBeanLocal mrbl) {
        this.mrbl = mrbl;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }
    
    
    
}
