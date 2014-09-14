/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.ITManagement;

import IslandFurniture.EJB.Entities.Privilege;
import IslandFurniture.EJB.Entities.Role;
import IslandFurniture.EJB.ITManagement.ManagePrivilegesBeanLocal;
import IslandFurniture.EJB.ITManagement.ManageRolesBeanLocal;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
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
public class RolePrivilegeManaged2Bean {

    
    private String username;
    private Long id;
    private List<Privilege> privilegeList;
    private List<Privilege> globalPrivilegeList;
    private String privilegeName;
    private Role role;
    
    @EJB
    private ManageRolesBeanLocal mrbl;
    
    @EJB
    private ManagePrivilegesBeanLocal mpbl;
    private Long privilegeId;
    
    @PostConstruct
    public void init(){
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        try{
            id = new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
            session.setAttribute("roleid", id);
        }catch (Exception e){
            id = (Long) session.getAttribute("roleid");
        }
        globalPrivilegeList = mpbl.displayPrivilege();
        privilegeList = mrbl.getPrivileges(id);
        role = mrbl.getRole(id);
        //should not be able to add privilege that already exists
        //TODO: fix this, currently broken
        /*for(Iterator<Privilege> i = globalPrivilegeList.iterator(); i.hasNext(); ) {
            Privilege p = i.next();
            if (privilegeList.contains(p)){
                globalPrivilegeList.remove(p);
            }
        }*/
    }
    
    public String displayPrivileges() {
      HttpSession session = Util.getSession();
      id = (Long) session.getAttribute("roleid");
      privilegeList = mrbl.getPrivileges(id);
      return "roleprivilege2";
    }
    
    public String addPrivilegeToRole() {
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        privilegeName = request.getParameter("privilegeForm:privilegeName");
        HttpSession session = Util.getSession();
        id = (Long)  session.getAttribute("roleid");
        mrbl.addPrivilegeToRole(id, privilegeName);
        return "roleprivilege2";
    }
    
    public String removePrivilegeFromRole() {
        privilegeId = new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("privilegeId"));
        HttpSession session = Util.getSession();
        id = (Long)  session.getAttribute("roleid");
        mrbl.removePrivilegeFromRole(id, privilegeId);
        return "roleprivilege2";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Privilege> getPrivilegeList() {
        return privilegeList;
    }

    public void setPrivilegeList(List<Privilege> privilegeList) {
        this.privilegeList = privilegeList;
    }

    public ManageRolesBeanLocal getMrbl() {
        return mrbl;
    }

    public void setMrbl(ManageRolesBeanLocal mrbl) {
        this.mrbl = mrbl;
    }

    public List<Privilege> getGlobalPrivilegeList() {
        return globalPrivilegeList;
    }

    public void setGlobalPrivilegeList(List<Privilege> globalPrivilegeList) {
        this.globalPrivilegeList = globalPrivilegeList;
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

    public Long getPrivilegeId() {
        return privilegeId;
    }

    public void setPrivilegeId(Long privilegeId) {
        this.privilegeId = privilegeId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
    
    
    
}
