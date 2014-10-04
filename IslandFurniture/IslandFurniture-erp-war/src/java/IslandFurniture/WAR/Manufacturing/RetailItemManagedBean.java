/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.Manufacturing;

import IslandFurniture.Entities.RetailItem;
import IslandFurniture.EJB.Manufacturing.StockManagerLocal;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author a0101774
 */
@ManagedBean
@ViewScoped
public class RetailItemManagedBean implements Serializable {
    @EJB
    private StockManagerLocal stockManager;
    
    private List<RetailItem> retailItemList;
    private RetailItem retailItem;

    public RetailItem getRetailItem() {
        return retailItem;
    }

    public void setRetailItem(RetailItem retailItem) {
        this.retailItem = retailItem;
    }

    public List<RetailItem> getRetailItemList() {
        return retailItemList;
    }

    public void setRetailItemList(List<RetailItem> retailItemList) {
        this.retailItemList = retailItemList;
    }
    
    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        retailItemList = stockManager.displayItemList();
        System.out.println("init:RetailItemManagedBean");
    }
    public String editRetailItem(ActionEvent event) throws IOException {
        System.out.println("RetailItemManagedBean.editRetailItem()");
        retailItem = (RetailItem) event.getComponent().getAttributes().get("toEdit");
        String msg = stockManager.editRetailItem(retailItem.getId(), retailItem.getName(), retailItem.getPrice());
        if(msg != null) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, "")); 
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Retail Item \"" + retailItem.getName() + "\" has been successfully updated", ""));    
        }
        return "retailitem";
    }
    public String deleteRetailItem() {
        System.out.println("RetailItemManagedBean.deleteRetailItem()");
        Long id = new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("itemID"));
        String msg = stockManager.deleteRetailItem(id);
        if(msg != null) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, "")); 
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Retail Item has been successfully deleted", ""));    
        }
        return "retailitem";
    }    
    public String addRetailItem() {
        System.out.println("RetailItemManagedBean.addRetailItem()");
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String itemName = request.getParameter("addRetailItemForm:name");
        String itemPrice = request.getParameter("addRetailItemForm:price");
        if(itemPrice.isEmpty())
            itemPrice = "0";
        String msg = stockManager.addRetailItem(itemName, Double.parseDouble(itemPrice));
        if(msg != null) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, "")); 
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Retail Item has been successfully added", ""));    
        }        
        return "retailitem";
    }
}
