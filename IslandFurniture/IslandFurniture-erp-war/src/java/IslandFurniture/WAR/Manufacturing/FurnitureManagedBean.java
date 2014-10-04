/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.Manufacturing;

import IslandFurniture.EJB.Entities.FurnitureModel;
import IslandFurniture.EJB.Entities.Material;
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
public class FurnitureManagedBean implements Serializable {
    @EJB
    private StockManagerLocal stockManager;
    private FurnitureModel furniture = null;
    private List<FurnitureModel> furnitureList = null;
    private List<FurnitureModel> filteredList = null;
    private List<Material> BOMList = null;
    private Long furnitureID = null;

    public Long getFurnitureID() {
        return furnitureID;
    }

    public void setFurnitureID(Long furnitureID) {
        this.furnitureID = furnitureID;
    }

    public List<Material> getBOMList() {        return BOMList;    }
    public void setBOMList(List<Material> BOMList) {        this.BOMList = BOMList;    }
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
        HttpSession session = Util.getSession();
        furnitureList = stockManager.displayFurnitureList();
        System.out.println("init:FurnitureManagedBean");
    }
    public String addFurnitureModel() {
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        System.out.println("FurnitureManagedBean.addFurnitureModel()");
        String name = request.getParameter("addFurnitureForm:name");
        String price = request.getParameter("addFurnitureForm:price");
        if(name.isEmpty() || price.isEmpty()) { 
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: Invalid input", ""));      
            return "furniture";
        }
        String output = stockManager.addFurnitureModel(name, Double.parseDouble(price));
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
        if(!(furniture.getPrice() instanceof Double)) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid input. Please insert a number", ""));
            return "furniture";            
        } 
        String msg = stockManager.editFurnitureModel(furniture.getId(), furniture.getName(), furniture.getPrice());
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
    public String addFurnitureColour(ActionEvent event) throws IOException {
        System.out.println("FurnitureManagedBean.addFurnitureColour()");
//        Long id = (Long) event.getComponent().getAttributes().get("addColour");
//        System.out.println("Furniture ID is " + id);
        stockManager.addFurnitureColour(Long.parseLong("46"), "000");
        return "furniture";
    }
    public void bomActionListener(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("fID", event.getComponent().getAttributes().get("fID"));
        furnitureID = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("GRDid");
        FacesContext.getCurrentInstance().getExternalContext().redirect("bom.xhtml");
    }   
}
