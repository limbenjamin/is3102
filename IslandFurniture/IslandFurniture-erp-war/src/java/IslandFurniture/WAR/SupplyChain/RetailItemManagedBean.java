/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.SupplyChain;

import IslandFurniture.EJB.Entities.RetailItem;
import IslandFurniture.EJB.SupplyChain.StockManagerLocal;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
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
public class RetailItemManagedBean {
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
        System.out.println("Init");
    }
    public String editRetailItem(ActionEvent event) throws IOException {
        System.out.println("RetailItemManagedBean.editRetailItem()");
        retailItem = (RetailItem) event.getComponent().getAttributes().get("toEdit");
        stockManager.editRetailItem(retailItem.getId(), retailItem.getName(), retailItem.getPrice());
        return "retailItem";
    }
    public String deleteRetailItem() {
        System.out.println("RetailItemManagedBean.deleteRetailItem()");
        Long id = new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("itemID"));
        stockManager.deleteRetailItem(id);
        return "retailItem";
    }    
    public String addRetailItem() {
        System.out.println("RetailItemManagedBean.addRetailItem()");
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String itemName = request.getParameter("addRetailItemForm:name");
        String itemPrice = request.getParameter("addRetailItemForm:price");
        if(itemPrice.isEmpty())
            itemPrice = "0";
        stockManager.addRetailItem(itemName, Double.parseDouble(itemPrice));
        
        return "retailItem";
    }
}
