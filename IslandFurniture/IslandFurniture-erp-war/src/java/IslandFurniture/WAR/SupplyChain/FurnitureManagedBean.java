/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.SupplyChain;

import IslandFurniture.EJB.Entities.FurnitureModel;
import IslandFurniture.EJB.Entities.Material;
import IslandFurniture.EJB.SupplyChain.StockManagerLocal;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.io.IOException;
import java.io.Serializable;
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
    private String colorPopup;
 
    public String getColorPopup() {
        return colorPopup;
    }
 
    public void setColorPopup(String colorPopup) {
        this.colorPopup = colorPopup;
    } 
    
    // End Testing Area
    
    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        furnitureList = stockManager.displayFurnitureList();
        System.out.println("init");
    }
    public String addFurnitureModel() {
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        System.out.println("FurnitureManagedBean.addFurnitureModel()");
        String name = request.getParameter("addFurnitureForm:name");
        String price = request.getParameter("addFurnitureForm:price");
        furniture = stockManager.addFurnitureModel(name, Double.parseDouble(price));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("fID", furniture.getId());
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
        stockManager.editFurnitureModel(furniture.getId(), furniture.getName(), furniture.getPrice());
        return "retailItem";
    }
    public String deleteFurnitureModel() {
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        System.out.println("FurnitureManagedBean.deleteFurnitureModel()");
        Long id = new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("fID"));
        stockManager.deleteFurnitureModel(id);
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
