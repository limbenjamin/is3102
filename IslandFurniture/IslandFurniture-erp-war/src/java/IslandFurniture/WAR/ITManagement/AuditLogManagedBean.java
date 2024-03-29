/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.ITManagement;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.EJB.ITManagement.ManageSystemAuditLogBeanLocal;
import IslandFurniture.Entities.GlobalHQ;
import IslandFurniture.Entities.LogEntry;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;



/**
 *
 * @author Benjamin
 */
@ManagedBean
@ViewScoped
public class AuditLogManagedBean {

    String username;
    List<LogEntry> logList;
    private List<LogEntry> filteredLogList;
    private String timezone;
    private JasperPrint jp;
    
    @EJB
    private ManageSystemAuditLogBeanLocal msalb;
    @EJB
    private ManageUserAccountBeanLocal muabl;
    
    
    @PostConstruct
    public void init(){
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        timezone = muabl.getStaff(username).getPlant().getTimeZoneID();
        if (muabl.getStaff(username).getPlant() instanceof GlobalHQ){
            logList = msalb.getLog();
        }else{
            logList = msalb.GetLogForPlant(muabl.getStaff(username).getPlant());
        }
        Collections.reverse(logList);
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

    public List<LogEntry> getFilteredLogList() {
        return filteredLogList;
    }

    public void setFilteredLogList(List<LogEntry> filteredLogList) {
        this.filteredLogList = filteredLogList;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
    
    public void prepareExport() throws JRException{
        JRBeanCollectionDataSource jbcd = new JRBeanCollectionDataSource(logList);
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        String path = externalContext.getRealPath("/WEB-INF/jasper/logreport.jasper");
        jp = JasperFillManager.fillReport(path, new HashMap(),jbcd);
        
    }
    
    public void PDF(ActionEvent actionEvent) throws JRException, IOException{
        prepareExport();
        HttpServletResponse resp = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        ServletOutputStream stream = resp.getOutputStream();
        JasperExportManager.exportReportToPdfStream(jp, stream);
        FacesContext.getCurrentInstance().responseComplete();
    }

}
