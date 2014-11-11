/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.Purchasing;

import IslandFurniture.Entities.Country;
import IslandFurniture.Entities.ProcuredStockSupplier;
import IslandFurniture.EJB.ITManagement.ManageOrganizationalHierarchyBeanRemote;
import IslandFurniture.EJB.Purchasing.SupplierManagerLocal;
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
public class SupplierManagedBean implements Serializable {
    @EJB
    private ManageOrganizationalHierarchyBeanRemote manageOrganizationalHierarchyBean;
    @EJB
    private SupplierManagerLocal supplierManager;
    
    private List<ProcuredStockSupplier> supplierList;
    private Long supplierID;
    private ProcuredStockSupplier supplier;
    private List<Country> countryList;
    
    public List<Country> getCountryList() {
        return countryList;
    }

    public void setCountryList(List<Country> countryList) {
        this.countryList = countryList;
    }

    public Long getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(Long supplierID) {
        this.supplierID = supplierID;
    }

    public List<ProcuredStockSupplier> getSupplierList() {
        return supplierList;
    }

    public void setSupplierList(List<ProcuredStockSupplier> supplierList) {
        this.supplierList = supplierList;
    }

    public ProcuredStockSupplier getSupplier() {
        return supplier;
    }

    public void setSupplier(ProcuredStockSupplier supplier) {
        this.supplier = supplier;
    }
    
    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        supplierList = supplierManager.displaySupplierList();
        countryList = manageOrganizationalHierarchyBean.getCountries();
        System.out.println("init:SupplierManagedBean");
    }
    
    public String addSupplier() {
        System.out.println("SupplierManagedBean.addSupplier()");
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String supplierName = request.getParameter("addSupplierForm:name");
        String supplierCountry = request.getParameter("addSupplierForm:country");
        String phoneNo = request.getParameter("addSupplierForm:phoneNo");
        String email = request.getParameter("addSupplierForm:email");
        String output = supplierManager.addSupplier(supplierName, supplierCountry, phoneNo, email);
        System.out.println("Output is " + output); 
        String id = output.split("#")[0];
        String msg = output.split("#")[1];
        supplier = supplierManager.getSupplier(Long.parseLong(id));
        if(msg.length() > 1) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, ""));              
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Procured Stock Supplier " + supplier.getName() + " successfully created ", ""));             
        }
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("supplierID", supplier.getId());
        return "procurementcontract?faces-redirect=true";
    }
    public String editSupplier(ActionEvent event) throws IOException {
        System.out.println("SupplierManagedBean.editSupplier()"); 
        supplier = (ProcuredStockSupplier) event.getComponent().getAttributes().get("toEdit");
        
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String c = (String) request.getParameter("editSupplierForm:updateCountry");
        System.out.println("Country changed to " + c);
        
        System.out.println("Supplier country is " + supplier.getCountry().getName());
        if(supplierManager.editSupplier(supplier.getId(), supplier.getName(), supplier.getCountry().getName(), supplier.getPhoneNumber(), supplier.getEmail())) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Procured Stock Supplier " + supplier.getName() + " has been updated", ""));
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unexpected error occured", ""));            
        }
        return "supplier";
    }
    public String deleteSupplier() {
        System.out.println("SupplierManagedBean.deleteSupplier()");
        Long id = new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("supplierID"));
        String msg = supplierManager.deleteSupplier(id);
        if(msg != null) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, ""));
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Procured Stock Supplier has been successfully deleted", ""));
        }
        return "supplier";
    }
    public void pcActionListener(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("supplierID", event.getComponent().getAttributes().get("sID"));
        supplierID = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("supplierID");
        System.out.println("While inside SupplierManagedBean, the supplierID is " + supplierID);
        FacesContext.getCurrentInstance().getExternalContext().redirect("procurementcontract.xhtml");
    }
    
    public void updateCountry(ActionEvent event) {
        String updateCountry = (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("updateCountry");
        System.out.println("Update Country is " + updateCountry); 
    }
}
