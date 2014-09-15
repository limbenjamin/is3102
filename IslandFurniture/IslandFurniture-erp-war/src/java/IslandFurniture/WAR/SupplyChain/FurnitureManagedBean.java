/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.SupplyChain;

import IslandFurniture.EJB.Entities.FurnitureModel;
import IslandFurniture.EJB.SupplyChain.StockManagerLocal;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.io.Serializable;
import java.util.List;
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
        stockManager.editFurnitureModel(name);
        displayFurnitureList();
    }
    public void addToBOM() {
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        System.out.println("FurnitureManagedBean:addToBOM()");
        Long materialID = null;
        Long furnitureID = null;
        try {
            String fID = request.getParameter("addBOMForm:furnitureID");
            furnitureID = Long.parseLong(fID);
            
            String mID = request.getParameter("addBOMForm:materialID");
            materialID  = Long.parseLong(mID);
            stockManager.addToBOM(materialID, furnitureID);
        } catch(Exception ex) {
            System.err.println("Something went wrong");
        }
    }
}
