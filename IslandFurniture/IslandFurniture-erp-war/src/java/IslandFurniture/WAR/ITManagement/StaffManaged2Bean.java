/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.ITManagement;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.EJB.Entities.Role;
import IslandFurniture.EJB.Entities.Staff;
import IslandFurniture.EJB.ITManagement.ManageRolesBeanLocal;
import IslandFurniture.EJB.ITManagement.ManageStaffAccountsBeanLocal;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
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
public class StaffManaged2Bean  implements Serializable  {

    private String username;
    private Staff staff;
    private List<Role> roleList;
    private List<Role> globalRoleList;
    private String name;
    private Long staffId;
    private Long roleId;
    private String roleName;
    private Role role;
    
    @EJB
    private ManageUserAccountBeanLocal muaib;
    @EJB
    private ManageRolesBeanLocal mrbl;
    @EJB
    private ManageStaffAccountsBeanLocal msabl;
    
    @PostConstruct
    public void init(){
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        try{
            staffId = new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
            session.setAttribute("staffid", staffId);
        }catch (Exception e){
            staffId = (Long) session.getAttribute("staffid");
        }
        staff = muaib.getStaffFromId(staffId);
        name = staff.getName();
        roleList = staff.getRoles();
        globalRoleList = mrbl.displayRole();
        //should not be able to add role that already exists
        //TODO: add this
    }
    
    public String displayRoles(){
        return "managestaff2";
    }
    
    public String removeRoleFromStaff(){
        staffId = new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("staffId"));
        roleId = new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("roleId"));
        msabl.removeRoleFromStaff(staffId, roleId);
        return "managestaff2";
    }
    
   public String addRoleToStaff(){
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        roleName = request.getParameter("staffRoleForm:roleName");
        HttpSession session = Util.getSession();
        staffId = (Long) session.getAttribute("staffid");
        msabl.addRoleToStaff(staffId, roleName);
        return "managestaff2";
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

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ManageUserAccountBeanLocal getMuaib() {
        return muaib;
    }

    public void setMuaib(ManageUserAccountBeanLocal muaib) {
        this.muaib = muaib;
    }


    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public ManageStaffAccountsBeanLocal getMsabl() {
        return msabl;
    }

    public void setMsabl(ManageStaffAccountsBeanLocal msabl) {
        this.msabl = msabl;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public ManageRolesBeanLocal getMrbl() {
        return mrbl;
    }

    public void setMrbl(ManageRolesBeanLocal mrbl) {
        this.mrbl = mrbl;
    }

    public List<Role> getGlobalRoleList() {
        return globalRoleList;
    }

    public void setGlobalRoleList(List<Role> globalRoleList) {
        this.globalRoleList = globalRoleList;
    }
    
    
    
}
