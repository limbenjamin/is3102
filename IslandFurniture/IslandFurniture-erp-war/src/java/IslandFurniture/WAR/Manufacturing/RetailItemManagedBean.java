/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.Manufacturing;

import IslandFurniture.EJB.Manufacturing.StockManagerLocal;
import IslandFurniture.Entities.Picture;
import IslandFurniture.Entities.RetailItem;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.IOUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

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
    private byte[] photo;
    private Long itemID;

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

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public Long getItemID() {
        return itemID;
    }

    public void setItemID(Long itemID) {
        this.itemID = itemID;
    }
    
    @PostConstruct
    public void init() {
        retailItemList = stockManager.displayItemList();
        System.out.println("init:RetailItemManagedBean");
    }
    public void editRetailItem(ActionEvent event) throws IOException {
        System.out.println("RetailItemManagedBean.editRetailItem()");
        if(photo != null) {
            Picture picture = new Picture();
            picture.setContent(photo);
            retailItem.setThumbnail(picture);
        }
        String msg = stockManager.editRetailItem(retailItem);
        if(msg != null) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, "")); 
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Retail Item \"" + retailItem.getName() + "\" has been successfully updated", ""));    
        }
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect("retailitem.xhtml"); 
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
    public void viewRetailItem(AjaxBehaviorEvent event) {
        System.out.println("RetailItemManagedBean.viewRetailItem()");
        itemID = (Long) event.getComponent().getAttributes().get("itemID");
        retailItem = (RetailItem) stockManager.getStock(itemID);
    }
    public void handleFileUpload(FileUploadEvent event) throws IOException {
        System.out.println("RetailItemManagedBean.handleFileUpload()");
        UploadedFile file = event.getFile();
        this.photo = IOUtils.toByteArray(file.getInputstream());
    } 
}
