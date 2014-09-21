/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.SupplyChain;

import IslandFurniture.EJB.Entities.BOMDetail;
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
public class BOMManagedBean implements Serializable {
    @EJB
    private StockManagerLocal stockManager;
    
    private FurnitureModel furniture = null;
    private BOMDetail BOMdetail = null;
    private Long furnitureID;
    private List<BOMDetail> bomList = null;
    private List<Material> materialList;

    public BOMDetail getBOMdetail() {
        return BOMdetail;
    }

    public void setBOMdetail(BOMDetail BOMdetail) {
        this.BOMdetail = BOMdetail;
    }

    public List<Material> getMaterialList() {
        return materialList;
    }

    public void setMaterialList(List<Material> materialList) {
        this.materialList = materialList;
    }

    public List<BOMDetail> getBomList() {
        return bomList;
    }

    public void setBomList(List<BOMDetail> bomList) {
        this.bomList = bomList;
    }

    public FurnitureModel getFurniture() {
        return furniture;
    }

    public void setFurniture(FurnitureModel furniture) {
        this.furniture = furniture;
    }

    public Long getFurnitureID() {
        return furnitureID;
    }

    public void setFurnitureID(Long furnitureID) {
        this.furnitureID = furnitureID;
    }
    
    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        this.furnitureID = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("fID");
        System.out.println("FurnitureID is " + furnitureID);
        this.furniture = stockManager.getFurniture(furnitureID);
        this.materialList = stockManager.displayMaterialList();
        this.bomList = stockManager.displayBOM(furnitureID);
        System.out.println("BOMDetailList has " + this.bomList.size() + " items");
        System.out.println("init");
    }
    
    public String addToBOM(ActionEvent event) {
        System.out.println("BOMManagedBean.addBOM()");
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String furID = request.getParameter("addToBOMForm:fID");
        String mID = request.getParameter("addToBOMForm:materialID");
        String mQuantity = request.getParameter("addToBOMForm:materialQuantity");
        System.out.println("FurnitureID is " + furID + ". materialID is " + mID + ". materialQuantity is " + mQuantity);
        stockManager.addToBOM(Long.parseLong(furID), Long.parseLong(mID), Integer.parseInt(mQuantity));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("fID", furniture.getId());
        return "bom";
    }
    
    public void editBOM(ActionEvent event) throws IOException {
        System.out.println("BOMManagedBean.editBOM()");
        BOMdetail = (BOMDetail) event.getComponent().getAttributes().get("toEdit");
        stockManager.editBOMDetail(BOMdetail.getId(), BOMdetail.getQuantity());
    }
    public void deleteBOM() {
        System.out.println("BOMManagedBean.deleteBOM()");
        String ID = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("bomID");
        System.out.println("ID is " + ID);
        Long id = new Long(ID);
        stockManager.deleteBOMDetail(id);
        this.bomList = stockManager.displayBOM(furnitureID);
        System.out.println("After deletion, BOMDetailList has " + bomList.size() + " items");
    }
}
