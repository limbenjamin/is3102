/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.Manufacturing;

import IslandFurniture.Entities.FurnitureModel;
import IslandFurniture.EJB.Manufacturing.StockManagerLocal;
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

/**
 *
 * @author a0101774
 */
@ManagedBean
@ViewScoped
public class FurnitureManagedBean implements Serializable {
    @EJB
    private StockManagerLocal stockManager;
    private FurnitureModel furniture = null;
    private List<FurnitureModel> furnitureList = null;
    private List<FurnitureModel> filteredList = null;

    public FurnitureModel getFurniture() {        return furniture;    }
    public void setFurniture(FurnitureModel furniture) {        this.furniture = furniture;    }
    public List<FurnitureModel> getFurnitureList() {        return furnitureList;    }
    public void setFurnitureList(List<FurnitureModel> furnitureList) {        this.furnitureList = furnitureList;    }
    public List<FurnitureModel> getFilteredList() {        return filteredList;    }
    public void setFilteredList(List<FurnitureModel> filteredList) {        this.filteredList = filteredList;    }
    
    // Testing area

    // End Testing Area
    
    @PostConstruct
    public void init() {
        System.out.println("init:FurnitureManagedBean");
        furnitureList = stockManager.displayFurnitureList();
    }
    public String addFurnitureModel() {
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        System.out.println("FurnitureManagedBean.addFurnitureModel()");
        String name = request.getParameter("addFurnitureForm:name");
        String output = stockManager.addFurnitureModel(name);
        System.out.println("Output is " + output); 
        String id = output.split("#")[0];
        String msg = output.split("#")[1];
        if(msg.length() > 1) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, ""));              
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Furniture " + name + " successfully created. ", ""));             
        }
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("fID", Long.parseLong(id));
        return "bom?faces-redirect=true";
    }
    public List<FurnitureModel> displayFurnitureList() {
        System.out.println("FurnitureManagedBean.displayFurnitureList()");
        furnitureList = stockManager.displayFurnitureList();
        return furnitureList;
    }
    public String editFurnitureModel(ActionEvent event) throws IOException {
        System.out.println("FurnitureManagedBean.editFurnitureModel()");
        furniture = (FurnitureModel) event.getComponent().getAttributes().get("toEdit");
        String msg = stockManager.editFurnitureModel(furniture.getId(), furniture.getName());
        if(msg == null) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Furniture " + furniture.getName() + " has been updated", ""));            
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, ""));
        }
        return "furniture";
    }
    public String deleteFurnitureModel() {
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        System.out.println("FurnitureManagedBean.deleteFurnitureModel()");
        Long id = new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("fID"));
        String msg = stockManager.deleteFurnitureModel(id);
        System.out.println("Message is " + msg);
        if(msg != null) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, ""));
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Furniture has been successfully deleted", ""));
        }
        return "furniture";
    }
    public void bomActionListener(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("fID", event.getComponent().getAttributes().get("fID"));
        FacesContext.getCurrentInstance().getExternalContext().redirect("bom.xhtml");
    }   
}
