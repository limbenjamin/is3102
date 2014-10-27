/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.OperationalCRM;

import IslandFurniture.EJB.OperationalCRM.FurnitureReturnBeanLocal;
import IslandFurniture.Entities.FurnitureTransaction;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@ManagedBean
@ViewScoped
public class FurnitureReturnManagedBean implements Serializable{

    @EJB
    private FurnitureReturnBeanLocal furnitureReturnBean;

    private Long transId;
    private FurnitureTransaction furnTrans;

    @PostConstruct
    public void init() {
        String transIdParam = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("transId");

        if (transIdParam != null) {
            transId = Long.parseLong(transIdParam);
            furnTrans = furnitureReturnBean.findTransaction(transId);
        }
    }
    
    /**
     * Creates a new instance of FurnitureReturnManagedBean
     */
    public FurnitureReturnManagedBean() {
    }

    public Long getTransId() {
        return transId;
    }

    public void setTransId(Long transId) {
        this.transId = transId;
    }

    public FurnitureTransaction getFurnTrans() {
        return furnTrans;
    }

    public void setFurnTrans(FurnitureTransaction furnTrans) {
        this.furnTrans = furnTrans;
    }

}
