/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.ITManagement;

import IslandFurniture.EJB.CommonInfrastructure.ManageAuthenticationBeanLocal;
import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.EJB.Entities.Country;
import IslandFurniture.EJB.Entities.Plant;
import IslandFurniture.EJB.Entities.Staff;
import IslandFurniture.EJB.ITManagement.ManageOrganizationalHierarchyBeanLocal;
import IslandFurniture.EJB.ITManagement.ManageStaffAccountsBeanLocal;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.Dependent;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
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
    
    @EJB
    private ManageStaffAccountsBeanLocal msabl;
    @EJB
    private ManageOrganizationalHierarchyBeanLocal mohBean;
    @EJB
    private ManageUserAccountBeanLocal muab;
    @EJB
    private ManageAuthenticationBeanLocal mabl;
    
    @PostConstruct
    public void init(){
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        staffList = msabl.displayStaffAccountsFromPlant(username);
        countryList = mohBean.getCountries();
        plantList = mohBean.displayPlant();
    }
    
    public String createStaff(){
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        username = request.getParameter("staffForm:username");
        name = request.getParameter("staffForm:name");
        password = Long.toHexString(Double.doubleToLongBits(Math.random()));
        emailAddress = request.getParameter("staffForm:emailAddress");
        phoneNo = request.getParameter("staffForm:phoneNo");
        HttpSession session = Util.getSession();
        staff = muab.getStaff((String) session.getAttribute("username"));
        countryName = staff.getPlant().getCountry().getName();
        plantName = staff.getPlant().getName();
        msabl.createStaffAccount(username, password, name, emailAddress, phoneNo, countryName, plantName);
        return "managestaff";
    }
    
    public String deleteStaff(){
        id = new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
        msabl.deleteStaffAccount(id);
        return "managestaff";
    }
    
    public String resetpassword(){
        id = new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
        mabl.resetPasswordByAdmin(id);
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
    
    
    
}
