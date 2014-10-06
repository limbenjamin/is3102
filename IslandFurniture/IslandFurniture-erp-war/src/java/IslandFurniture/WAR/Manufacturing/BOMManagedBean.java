/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.Manufacturing;

import IslandFurniture.EJB.Manufacturing.StockManagerLocal;
import IslandFurniture.EJB.Purchasing.SupplierManagerLocal;
import IslandFurniture.Entities.BOMDetail;
import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.FurnitureModel;
import IslandFurniture.Entities.Material;
import IslandFurniture.Enums.FurnitureCategory;
import IslandFurniture.Enums.FurnitureSubcategory;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumSet;
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
import javax.servlet.http.HttpSession;

/**
 *
 * @author a0101774
 */
@ManagedBean
@ViewScoped
public class BOMManagedBean implements Serializable {
    @EJB
    private SupplierManagerLocal supplierManager;
    @EJB
    private StockManagerLocal stockManager;
    
    private FurnitureModel furniture = null;
    private BOMDetail BOMdetail = null;
    private Long furnitureID;
    private List<BOMDetail> bomList = null;
    private List<Material> materialList;
    private Integer listSize = null;
    private boolean uneditable;
    private List<FurnitureCategory> categoryList;
    private List<FurnitureSubcategory> subcategoryList;
    private List<CountryOffice> countryList;
    private List<Double> pricingList;

    public List<FurnitureSubcategory> getSubcategoryList() {
        return subcategoryList;
    }

    public void setSubcategoryList(List<FurnitureSubcategory> subcategoryList) {
        this.subcategoryList = subcategoryList;
    }

    public List<FurnitureCategory> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<FurnitureCategory> categoryList) {
        this.categoryList = categoryList;
    }

    public boolean isUneditable() {
        return uneditable;
    }

    public void setUneditable(boolean uneditable) {
        this.uneditable = uneditable;
    }

    public Integer getListSize() {
        return listSize;
    }

    public void setListSize(Integer listSize) {
        this.listSize = listSize;
    }

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
        System.out.println("init:BOMManagedBean");
        this.furnitureID = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("fID");
        try {
            if(furnitureID == null) {
                    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                    ec.redirect("furniture.xhtml"); 
            }
        } catch(IOException ex) {
            
        }
        System.out.println("FurnitureID is " + furnitureID);
        this.furniture = stockManager.getFurniture(furnitureID);
        this.materialList = stockManager.displayMaterialList();
        this.bomList = stockManager.displayBOM(furnitureID);
        this.uneditable = this.furniture.getBom().isUneditable();
        this.subcategoryList = new ArrayList<FurnitureSubcategory>(EnumSet.allOf(FurnitureSubcategory.class));
        this.categoryList = new ArrayList<FurnitureCategory>(EnumSet.allOf(FurnitureCategory.class));
        this.countryList = supplierManager.getListOfCountryOffice();
        if(uneditable) 
            System.out.println("Furniture's BOM cannot be edited");
        else
            System.out.println("Furniture's BOM can be edited");
        System.out.println("BOMDetailList has " + this.bomList.size() + " items");
        this.listSize = this.bomList.size();
    }
    
    public String addToBOM(ActionEvent event) {
        System.out.println("BOMManagedBean.addBOM()");
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String furID = request.getParameter("addToBOMForm:fID");
        String mID = request.getParameter("addToBOMForm:materialID");
        String mQuantity = request.getParameter("addToBOMForm:materialQuantity");
        if(mID.isEmpty() || mQuantity.isEmpty()) { 
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: Incomplete form", ""));    
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("fID", furniture.getId());  
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("fID", furniture.getId());
        return "bom";
        }
        System.out.println("FurnitureID is " + furID + ". materialID is " + mID + ". materialQuantity is " + mQuantity);
        String msg = stockManager.addToBOM(Long.parseLong(furID), Long.parseLong(mID), Integer.parseInt(mQuantity));
        if(msg != null) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, ""));   
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Material successfully added into BOM", ""));        
        } 
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("fID", furniture.getId());
        return "bom";
    }
    
    public String editBOM(ActionEvent event) throws IOException {
        System.out.println("BOMManagedBean.editBOM()");
        BOMdetail = (BOMDetail) event.getComponent().getAttributes().get("toEdit");
        String msg = stockManager.editBOMDetail(BOMdetail.getId(), BOMdetail.getQuantity());       
        if(msg != null) { 
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, ""));
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "BOM Detail has been updated", ""));
        }
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("fID", furniture.getId());
        return "bom";
    }
    public String deleteBOM() {
        System.out.println("BOMManagedBean.deleteBOM()");
        String ID = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("bomID");
        System.out.println("ID is " + ID);
        Long id = new Long(ID);
        String msg = stockManager.deleteBOMDetail(id);        
        if(msg != null) { 
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, ""));
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "BOM Detail has been successfully deleted", ""));
        }
        System.out.println("After deletion, BOMDetailList has " + bomList.size() + " items");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("fID", furniture.getId());
        return "bom";
    }
    public void addMaterial(AjaxBehaviorEvent event) {
        System.out.println("BOMManagedBean.addMaterial()");
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String name = null;
        String weight = null;
        name = request.getParameter("addNewMaterialForm:materialName");
        weight = request.getParameter("addNewMaterialForm:materialWeight"); 
        System.out.println("Name is " + name + ". Weight is " + weight);
        if(name.isEmpty() || weight.isEmpty() || name == null || weight == null) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Incomplete form", "")); 
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("fID", furniture.getId());
        }
        if(stockManager.addMaterial(name, Double.parseDouble(weight))) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully created Material \"" + name + "\"", ""));
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Material \"" + name + "\" already exists in database", ""));
        }
        this.materialList = stockManager.displayMaterialList();
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("fID", furniture.getId());
    }
    public void editFurnitureCategory(AjaxBehaviorEvent event) {
        System.out.println("BOMManagedBean.editFurnitureCategory()");
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String category = request.getParameter("categoryForm:categoryField");
        FurnitureCategory fc = FurnitureCategory.valueOf(category);
        stockManager.editFurnitureCategory(furnitureID, fc);
        this.furniture.setCategory(fc); 
    }
    public void editFurnitureSubcategory(AjaxBehaviorEvent event) {
        System.out.println("BOMManagedBean.editFurnitureSubcategory()");
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String category = request.getParameter("categoryForm:subcategoryField");
        FurnitureSubcategory fc = FurnitureSubcategory.valueOf(category);
        stockManager.editFurnitureSubcategory(furnitureID, fc);
        this.furniture.setSubcategory(fc); 
    }
    public String editPriceList() {
        System.out.println("BOMManagedBean.editPriceList()");
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        Long fID = Long.parseLong(request.getParameter("pricingForm:fID"));
        return "bom";
        
        
    }
}