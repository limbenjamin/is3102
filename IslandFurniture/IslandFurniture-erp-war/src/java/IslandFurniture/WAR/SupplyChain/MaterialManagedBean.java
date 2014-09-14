/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.SupplyChain;

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
public class MaterialManagedBean implements Serializable {
    private List<Material> materialList = null;
    private int rowCount = 1;

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
    }
    
    public boolean addMaterial() {
        System.out.println("MaterialManagedBean: 1");
        stockManager.addMaterial();
        return true;
    }
    public boolean editMaterial() {
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        Long id = Long.parseLong(request.getParameter("editMaterialForm:id"));
        stockManager.updateMaterial(id, "here", null);
        return true;
    }
    public List<Material> displayMaterialList() {
        System.out.println("MaterialManagedBean.displayMaterialList(): 1");
        materialList = stockManager.displayMaterialList();
        return materialList;
    }
    public int rowCounter() {
        System.out.println(rowCount);
        return rowCount++;
    }
}
