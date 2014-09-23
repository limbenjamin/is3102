/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.Manufacturing;

import IslandFurniture.EJB.Entities.MonthlyProcurementPlan;
import IslandFurniture.EJB.Manufacturing.ManageProcurementPlanLocal;
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
public class ProcurementPlanManagedBean {

    private String username;
    private List<MonthlyProcurementPlan> mppList;
    
    @EJB
    private ManageProcurementPlanLocal mppl;
    
    
    @PostConstruct
    public void init(){
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        mppList = mppl.viewMonthlyProcurementPlan();
    }
    
    public String generateProcurementPlan(){
        mppl.createMonthlyProcumentPlan();
        return "procurementplan";
    }
    
    public String createPurchaseOrder(){
        mppl.createPurchaseOrder();
        return "procurementplan";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ManageProcurementPlanLocal getMppl() {
        return mppl;
    }

    public void setMppl(ManageProcurementPlanLocal mppl) {
        this.mppl = mppl;
    }

    public List<MonthlyProcurementPlan> getMppList() {
        return mppList;
    }

    public void setMppList(List<MonthlyProcurementPlan> mppList) {
        this.mppList = mppList;
    }
    
    
    
}
