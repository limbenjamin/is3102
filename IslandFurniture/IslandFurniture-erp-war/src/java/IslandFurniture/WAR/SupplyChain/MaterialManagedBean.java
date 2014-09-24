/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.SupplyChain;

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
public class MaterialManagedBean implements Serializable {
    private List<Material> materialList = null;
    private int rowCount = 1;
    private List<Material> filteredList = null;
    private Material material = null;

    public List<Material> getFilteredList() {
        return filteredList;
    }

    public void setFilteredList(List<Material> filteredList) {
        this.filteredList = filteredList;
    }

    public List<Material> getMaterialList() {
        return materialList;
    }

    public void setMaterialList(List<Material> materialList) {
        this.materialList = materialList;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }
    @EJB
    private StockManagerLocal stockManager;
    
    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        materialList = stockManager.displayMaterialList();
        System.out.println("init:MaterialManagedBean");
    }
    
    public String addMaterial() {
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        System.out.println("MaterialManagedBean.addMaterial()");
        String name = request.getParameter("addMaterialForm:name");
        String temp = request.getParameter("addMaterialForm:weight");
        
        Double weight;
        if(!temp.isEmpty()) {
            weight = Double.parseDouble(temp);
        }
        else 
            weight = 0.0;
        stockManager.addMaterial(name, weight);
        return "material";
    }
    public String editMaterial(ActionEvent event) throws IOException {
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        System.out.println("MaterialManagedBean.editMaterial()");
        material = (Material) event.getComponent().getAttributes().get("toEdit");
        stockManager.updateMaterial(material.getId(), material.getName(), material.getMaterialWeight());
        return "material";
    }
    public List<Material> displayMaterialList() {
        System.out.println("MaterialManagedBean.displayMaterialList(): 1");
        materialList = stockManager.displayMaterialList();
        return materialList;
    }

    public String deleteMaterial() {
        System.out.println("MaterialManagedBean.deleteMaterial()");
        Long id = new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("materialID"));
        stockManager.deleteMaterial(id);
        return "material";
    } 
}
