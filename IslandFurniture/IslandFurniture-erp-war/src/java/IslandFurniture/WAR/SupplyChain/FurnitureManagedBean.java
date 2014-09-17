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
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
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

    public List<Material> getBOMList() {        return BOMList;    }
    public void setBOMList(List<Material> BOMList) {        this.BOMList = BOMList;    }
    public FurnitureModel getFurniture() {        return furniture;    }
    public void setFurniture(FurnitureModel furniture) {        this.furniture = furniture;    }
    public List<FurnitureModel> getFurnitureList() {        return furnitureList;    }
    public void setFurnitureList(List<FurnitureModel> furnitureList) {        this.furnitureList = furnitureList;    }
    public List<FurnitureModel> getFilteredList() {        return filteredList;    }
    public void setFilteredList(List<FurnitureModel> filteredList) {        this.filteredList = filteredList;    }
    
    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        furnitureList = stockManager.displayFurnitureList();
    }
    public void addFurnitureModel() {
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        System.out.println("FurnitureManagedBean.addFurnitureModel()");
        String name = request.getParameter("addFurnitureForm:name");
        stockManager.addFurnitureModel(name);
    }
    public List<FurnitureModel> displayFurnitureList() {
        System.out.println("FurnitureManagedBean.displayFurnitureList()");
        furnitureList = stockManager.displayFurnitureList();
        return furnitureList;
    }
    public void editFurnitureModel() {
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        System.out.println("FurnitureManagedBean.editFurnitureModel()");
        String name = request.getParameter("editFurnitureForm:name");
        String price = request.getParameter("editFurnitureForm:price");
        stockManager.editFurnitureModel(name, price);
        displayFurnitureList();
    }
    public String addFurnitureColour() {
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        System.out.println("FurnitureManagedBean.addFurnitureColour()");
        String name = request.getParameter("addColourForm:name");
        String colour = request.getParameter("addColourForm:colour");
        stockManager.addFurnitureColour(name, colour);
        return "furniture";
    }
    public String addToBOM() {
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        System.out.println("FurnitureManagedBean:addToBOM()");
        try {
            String furnitureName = request.getParameter("addBOMForm:furnitureName");
            String materialName= request.getParameter("addBOMForm:materialName");
            stockManager.addToBOM(materialName, furnitureName);
            return "furniture";
        } catch(Exception ex) {
            System.err.println("Something went wrong");
            return "furniture";
        }
    }
    public void displayBOM() {
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        System.out.println("FurnitureManagedBean:displayBOM()");
        String furnitureName = null;
        try {
            furnitureName = request.getParameter("displayBOMForm:name");
            BOMList = stockManager.displayBOM(furnitureName);
            System.out.println(BOMList.size());
  //          return "furniture";
        } catch(Exception ex) {
            System.err.println("Something went wrong");
  //          return "furniture";
        }
    }
    public List<Material> displayBOMList() {
        System.out.println("FurnitureManagedBean:displayBOMList()");
        if(BOMList != null) {
            for(int i=0; i<BOMList.size(); i++) {
                System.out.println(BOMList.get(i).getName());
            }
        } else
            System.out.println("BOMList is null");
        return BOMList;
    }
}
