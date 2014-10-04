/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.ITManagement;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.Entities.GlobalHQ;
import IslandFurniture.Entities.LogEntry;
import IslandFurniture.EJB.ITManagement.ManageSystemAuditLogBeanLocal;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpSession;



/**
 *
 * @author Benjamin
 */
@ManagedBean
@ViewScoped
public class AuditLogBean {

    String username;
    List<LogEntry> logList;
    
    @EJB
    private ManageSystemAuditLogBeanLocal msalb;
    @EJB
    private ManageUserAccountBeanLocal muabl;
    
    
    @PostConstruct
    public void init(){
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        if (muabl.getStaff(username).getPlant() instanceof GlobalHQ){
            logList = msalb.getLog();
        }else{
            logList = msalb.GetLogForPlant(muabl.getStaff(username).getPlant());
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<LogEntry> getLogList() {
        return logList;
    }

    public void setLogList(List<LogEntry> logList) {
        this.logList = logList;
    }

    public ManageSystemAuditLogBeanLocal getMsalb() {
        return msalb;
    }

    public void setMsalb(ManageSystemAuditLogBeanLocal msalb) {
        this.msalb = msalb;
    }

    public ManageUserAccountBeanLocal getMuabl() {
        return muabl;
    }

    public void setMuabl(ManageUserAccountBeanLocal muabl) {
        this.muabl = muabl;
    }
    
    
    
}
