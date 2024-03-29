/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.ITManagement;

import IslandFurniture.Entities.Privilege;
import IslandFurniture.Entities.Role;
import IslandFurniture.EJB.ITManagement.ManagePrivilegesBeanLocal;
import IslandFurniture.EJB.ITManagement.ManageRolesBeanLocal;
import IslandFurniture.Exceptions.DuplicateEntryException;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.io.Serializable;
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
public class RolePrivilegeManagedBean  implements Serializable  {


    @EJB
    private ManagePrivilegesBeanLocal mpbl;
    
    @EJB
    private ManageRolesBeanLocal mrbl;
    
    private String privilegeName;
    private String username;
    private List<Privilege> privilegeList;
    private Long privilegeId;
    private List<Role> roleList;
    private String roleName;
    private Long roleId;
    
    @PostConstruct
    public void init(){
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        privilegeList = mpbl.displayPrivilege();
        roleList = mrbl.displayRole();
    }
    
    public String deletePrivilege(){
        privilegeId = new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
        mpbl.removePrivilege(privilegeId);
        return "roleprivilege";
    }
    
    public String addRole() throws DuplicateEntryException{
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        roleName = request.getParameter("roleForm:roleName");
        mrbl.createRole(roleName);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Role added",""));
        return "roleprivilege";
    }
    
    public String deleteRole(){
        roleId = new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
        mrbl.removeRole(roleId);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Role deleted",""));
        return "roleprivilege";
    }

    public ManagePrivilegesBeanLocal getMpbl() {
        return mpbl;
    }

    public void setMpbl(ManagePrivilegesBeanLocal mpbl) {
        this.mpbl = mpbl;
    }

    public String getPrivilegeName() {
        return privilegeName;
    }

    public void setPrivilegeName(String privilegeName) {
        this.privilegeName = privilegeName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Privilege> getPrivilegeList() {
        return privilegeList;
    }

    public void setPrivilegeList(List<Privilege> privilegeList) {
        this.privilegeList = privilegeList;
    }

    public Long getPrivilegeId() {
        return privilegeId;
    }

    public void setPrivilegeId(Long privilegeId) {
        this.privilegeId = privilegeId;
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

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
    
    
    
    
}
