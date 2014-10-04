/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.Manufacturing;

import IslandFurniture.Entities.PurchaseOrder;
import IslandFurniture.EJB.Manufacturing.ManageProcurementPlanLocal;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Benjamin
 */
@ManagedBean
@javax.faces.bean.ViewScoped
public class PurchaseOrderMfgManagedBean {

    /**
     * Creates a new instance of PurchaseOrderManagedBean
     */
    public PurchaseOrderMfgManagedBean() {
    }
    
    private List<PurchaseOrder> poList;
    private String username;
    
    @EJB
    private ManageProcurementPlanLocal mppl;
    
    @PostConstruct
    public void init(){
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        mppl.viewPurchaseOrder();
    }

    public List<PurchaseOrder> getPoList() {
        return poList;
    }

    public void setPoList(List<PurchaseOrder> poList) {
        this.poList = poList;
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
