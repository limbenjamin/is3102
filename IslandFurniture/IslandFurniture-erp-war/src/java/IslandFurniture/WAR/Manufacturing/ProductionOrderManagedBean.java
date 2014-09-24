/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.Manufacturing;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.EJB.Entities.ManufacturingFacility;
import IslandFurniture.EJB.Entities.Plant;
import IslandFurniture.EJB.Entities.ProdOrderStatus;
import IslandFurniture.EJB.Entities.ProductionOrder;
import IslandFurniture.EJB.Entities.Staff;
import IslandFurniture.EJB.Manufacturing.ProductionManagerLocal;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpSession;
/**
 *
 * @author a0101774
 */
@ManagedBean
@ViewScoped
public class ProductionOrderManagedBean implements Serializable {
    @EJB
    private ManageUserAccountBeanLocal staffBean;
    @EJB
    private ProductionManagerLocal productionManager;
    
    private Staff staff;
    private ManufacturingFacility mf;
    private ProductionOrder po;
    private List<ProductionOrder> poList;
    private List<ProductionOrder> plannedPOList = new ArrayList<ProductionOrder>();
    private List<ProductionOrder> startedPOList = new ArrayList<ProductionOrder>();
    private List<ProductionOrder> completedPOList = new ArrayList<ProductionOrder>();

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public ManufacturingFacility getMf() {
        return mf;
    }

    public void setMf(ManufacturingFacility mf) {
        this.mf = mf;
    }

    public List<ProductionOrder> getPoList() {
        return poList;
    }

    public void setPoList(List<ProductionOrder> poList) {
        this.poList = poList;
    }

    public List<ProductionOrder> getPlannedPOList() {
        return plannedPOList;
    }

    public void setPlannedPOList(List<ProductionOrder> plannedPOList) {
        this.plannedPOList = plannedPOList;
    }

    public List<ProductionOrder> getStartedPOList() {
        return startedPOList;
    }

    public void setStartedPOList(List<ProductionOrder> startedPOList) {
        this.startedPOList = startedPOList;
    }

    public List<ProductionOrder> getCompletedPOList() {
        return completedPOList;
    }

    public void setCompletedPOList(List<ProductionOrder> completedPOList) {
        this.completedPOList = completedPOList;
    }
    
    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        this.staff = staffBean.getStaff((String) session.getAttribute("username"));
        Plant plant = staff.getPlant();
        if(plant instanceof ManufacturingFacility) {
            mf = (ManufacturingFacility) plant;
            poList = mf.getProdOrders();
            for(int i=0; i<poList.size(); i++) {
                if(poList.get(i).getStatus() == ProdOrderStatus.PLANNED)
                    plannedPOList.add(poList.get(i));
                else if(poList.get(i).getStatus() == ProdOrderStatus.STARTED)
                    startedPOList.add(poList.get(i));
                else
                    completedPOList.add(poList.get(i));
            }
        } else {
            try {
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect(ec.getRequestContextPath());
            } catch (IOException ex) {

            }
        }
    }
    
    public void editPO(ActionEvent event) throws IOException {
        System.out.println("ProductionOrderManagedBean.editPO()");
        po = (ProductionOrder) event.getComponent().getAttributes().get("toEdit");
        productionManager.editPO(po.getBatchNo(), po.getProdOrderDate(), po.getQty());
    }
    public void editCompletedQty(AjaxBehaviorEvent event) throws IOException {
        System.out.println("ProductionOrderManagedBean.editCompletedQty()");
        po = (ProductionOrder) event.getComponent().getAttributes().get("toEdit");
        productionManager.editCompletedQty(po.getBatchNo(), po.getCompletedQty());
    }
    public void deletePO(AjaxBehaviorEvent event) {
        System.out.println("ProductionOrderManagedBean.deletePO()");
        Long batchNo = new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("batchNo"));
        po = productionManager.deletePO(batchNo);
        plannedPOList.remove(po);
    }
    public void commencePO(AjaxBehaviorEvent event) {
        System.out.println("ProductionOrderManagedBean.commencePO()");
        Long batchNo = new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("batchNo"));
        po = productionManager.commencePO(batchNo);
        plannedPOList.remove(po);
        startedPOList.add(po);        
    }
    public void completePO(AjaxBehaviorEvent event) {
        System.out.println("ProductionOrderManagedBean.completePO()");
        Long batchNo = new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("batchNo"));
        po = productionManager.completePO(batchNo);
        startedPOList.remove(po);
        completedPOList.add(po);        
    }
}
