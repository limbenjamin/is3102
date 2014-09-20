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
public class BOMManagedBean {
    @EJB
    private StockManagerLocal stockManager;
    
    private FurnitureModel furniture;
    private BOMDetail BOMdetail;
    private Long furnitureID;
    private List<BOMDetail> bomList;
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
        if(this.furniture.getBom() == null) {
            this.bomList = null;
        } else if(this.furniture.getBom().getBomDetails() == null) {
            this.bomList = null;
        } else
            this.bomList = this.furniture.getBom().getBomDetails();
        this.materialList = stockManager.displayMaterialList();
        System.out.println("init");
    }
    
    public void addToBOM(ActionEvent event) {
        System.out.println("BOMManagedBean.addBOM()");
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String furID = request.getParameter("addToBOMForm:fID");
        String mID = request.getParameter("addToBOMForm:materialID");
        String mQuantity = request.getParameter("addToBOMForm:materialQuantity");
        System.out.println("FurnitureID is " + furID + ". materialID is " + mID + ". materialQuantity is " + mQuantity);
        stockManager.addToBOM(Long.parseLong(furID), Long.parseLong(mID), Integer.parseInt(mQuantity));
        bomList = stockManager.displayBOM(furnitureID);
    }
    
    public void editBOM(ActionEvent event) throws IOException {
        System.out.println("BOMManagedBean.editBOM()");
        BOMdetail = (BOMDetail) event.getComponent().getAttributes().get("toEdit");
        stockManager.editBOMDetail(BOMdetail.getId(), BOMdetail.getQuantity());
    }
    public void deleteBOM() {
        System.out.println("BOMManagedBean.deleteBOM()");
        Long id = new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("itemID"));
        stockManager.deleteBOMDetail(id);
    }
}
