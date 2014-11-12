/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.Kitchen;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.EJB.Kitchen.IngredientSupplierManagerLocal;
import IslandFurniture.EJB.Kitchen.KitchenStockManagerLocal;
import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.Ingredient;
import IslandFurniture.Entities.IngredientContractDetail;
import IslandFurniture.Entities.IngredientSupplier;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Staff;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
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
public class IngredientContractManagedBean implements Serializable {
    @EJB
    private KitchenStockManagerLocal kitchenStockManager;
    @EJB
    private IngredientSupplierManagerLocal supplierManager;
    @EJB
    private ManageUserAccountBeanLocal staffBean;
    
    private String username;
    private Long supplierID;
    private Staff staff;
    private Plant plant;
    private CountryOffice co;
    private IngredientSupplier supplier;
    private IngredientContractDetail icd;
    private List<Ingredient> ingredientList;
    private List<IngredientContractDetail> icList;
    
    @PostConstruct
    public void init() {
        System.out.println("init:IngredientContractManagedBean");
        HttpSession session = Util.getSession();
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        username = (String) session.getAttribute("username");
        staff = staffBean.getStaff(username);
        plant = staff.getPlant();

        if (plant instanceof CountryOffice) {
            this.co = (CountryOffice) plant;
            this.supplierID = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("supplierID");
            if(supplierID == null) {
                try {
                    ec.redirect("ingredientsupplier.xhtml");
                } catch(IOException ex) { 
                    
                }
            } else {
                supplier = supplierManager.getSupplier(supplierID);
                ingredientList = kitchenStockManager.getIngredientList(co);
                icList = supplier.getIngredContract().getIngredContractDetails();
            }
        } else {
            try {
                ec.redirect(ec.getRequestContextPath());
            } catch (IOException ex) {

            }
        }
    }
    public void addIngredientContractDetail() throws IOException {
        System.out.println("IngredientContractManagedBean.addProcurementContractDetail()");
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String supplierId = request.getParameter("addICD:supplierID");
        String ingredientID = request.getParameter("addICD:ingredientID");
        String size = request.getParameter("addICD:size");
        String leadTime = request.getParameter("addICD:leadTime"); 
        String lotPrice = request.getParameter("addICD:lotPrice");
        String msg = supplierManager.addIngredientContractDetail(Long.parseLong(supplierId), Long.parseLong(ingredientID), Integer.parseInt(size), Integer.parseInt(leadTime), Double.parseDouble(lotPrice), co);
        if(msg != null) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, "")); 
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Ingredient Contract Detail has been successfully added", ""));    
        }    
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("supplierID", supplier.getId());
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect("ingredientcontract.xhtml"); 
    }
    public void editIngredientContractDetail(ActionEvent event) throws IOException {
        System.out.println("IngredientContractManagedBean.editIngredientContractDetail()");
        icd = (IngredientContractDetail) event.getComponent().getAttributes().get("toEdit"); 
        String msg = supplierManager.editIngredientContractDetail(icd.getId(), icd.getLotSize(), icd.getLeadTimeInDays(), icd.getLotPrice());  
        if(msg != null) { 
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, ""));
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Ingredient Contract Detail has been updated", ""));
        }
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("supplierID", supplier.getId());
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect("ingredientcontract.xhtml");  
    }
    public void deleteIngredientContractDetail() throws IOException {
        System.out.println("IngredientContractManagedBean.deleteIngredientContractDetail()");
        Long id = new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("icdID"));
        String msg = supplierManager.deleteIngredientContractDetail(id, this.supplierID);
        if(msg != null) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, "")); 
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Ingredient Contract Detail has been successfully deleted", ""));    
        } 
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("supplierID", supplier.getId());
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect("ingredientcontract.xhtml"); 
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(Long supplierID) {
        this.supplierID = supplierID;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    public CountryOffice getCo() {
        return co;
    }

    public void setCo(CountryOffice co) {
        this.co = co;
    }

    public IngredientSupplier getSupplier() {
        return supplier;
    }

    public void setSupplier(IngredientSupplier supplier) {
        this.supplier = supplier;
    }

    public List<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public void setIngredientList(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    public List<IngredientContractDetail> getIcList() {
        return icList;
    }

    public void setIcList(List<IngredientContractDetail> icList) {
        this.icList = icList;
    }

    public IngredientContractDetail getIcd() {
        return icd;
    }

    public void setIcd(IngredientContractDetail icd) {
        this.icd = icd;
    }
    
}
