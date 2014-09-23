/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.Manufacturing;

import IslandFurniture.EJB.Entities.PurchaseOrderDetail;
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
public class PurchaseOrderMfgDetailManagedBean {

    private List<PurchaseOrderDetail> poDetailList;
    private String username;
    
    @EJB
    private ManageProcurementPlanLocal mppl;
    
    @PostConstruct
    public void init(){
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        poDetailList = mppl.viewPurchaseOrderDetail();
    }

    public List<PurchaseOrderDetail> getPoDetailList() {
        return poDetailList;
    }

    public void setPoDetailList(List<PurchaseOrderDetail> poDetailList) {
        this.poDetailList = poDetailList;
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

    
    
}
