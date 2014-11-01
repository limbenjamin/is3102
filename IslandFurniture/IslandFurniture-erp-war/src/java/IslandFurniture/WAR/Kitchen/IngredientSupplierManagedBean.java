/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.Kitchen;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.EJB.ITManagement.ManageOrganizationalHierarchyBeanRemote;
import IslandFurniture.EJB.Kitchen.IngredientSupplierManagerLocal;
import IslandFurniture.Entities.Country;
import IslandFurniture.Entities.CountryOffice;
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
public class IngredientSupplierManagedBean implements Serializable {
    @EJB
    private ManageOrganizationalHierarchyBeanRemote manageOrganizationalHierarchyBean;
    @EJB
    private IngredientSupplierManagerLocal supplierManager;
    @EJB
    private ManageUserAccountBeanLocal staffBean;
    
    private String username;
    private Staff staff;
    private Plant plant;
    private CountryOffice co;
    private IngredientSupplier supplier;
    private List<IngredientSupplier> supplierList;
    private List<Country> countryList;
    
    @PostConstruct
    public void init() {
        System.out.println("init:IngredientSupplierManagedBean");
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        staff = staffBean.getStaff(username);
        plant = staff.getPlant();

        if (plant instanceof CountryOffice) {
            this.co = (CountryOffice) plant;
            this.countryList = manageOrganizationalHierarchyBean.getCountries();
            supplierList = supplierManager.displaySupplierList(co);
        } else {
            try {
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect(ec.getRequestContextPath());
            } catch (IOException ex) {

            }
        }
    }
    
    public void addSupplier() throws IOException {
        System.out.println("IngredientSupplierManagedBean.addSupplier()");
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String supplierName = request.getParameter("addSupplierForm:name");
        String supplierCountry = request.getParameter("addSupplierForm:country");
        String phoneNo = request.getParameter("addSupplierForm:phoneNo");
        String email = request.getParameter("addSupplierForm:email");
        String output = supplierManager.addSupplier(supplierName, supplierCountry, phoneNo, email, this.co);
        System.out.println("Output is " + output); 
        String id = output.split("#")[0];
        String msg = output.split("#")[1];
        supplier = supplierManager.getSupplier(Long.parseLong(id));
        if(msg.length() > 1) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, ""));              
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Ingredient Supplier " + supplier.getName() + " successfully created ", ""));             
        }
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("supplierID", supplier.getId());
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect("ingredientcontract.xhtml");
    }
    public void deleteSupplier() throws IOException {
        System.out.println("IngredientSupplierManagedBean.deleteSupplier()");
        Long id = new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("supplierID"));
        String msg = supplierManager.deleteSupplier(id);
        if(msg != null) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, ""));
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Ingredient Supplier has been successfully deleted", ""));
        }
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect("ingredientsupplier.xhtml");
    }
    public void editSupplier(ActionEvent event) throws IOException {
        System.out.println("IngredientSupplierManagedBean.editSupplier()");
        supplier = (IngredientSupplier) event.getComponent().getAttributes().get("toEdit");
        System.out.println("Supplier country is " + supplier.getCountry().getName());
        String msg = supplierManager.editSupplier(supplier.getId(), supplier.getName(), supplier.getCountry().getName(), supplier.getPhoneNumber(), supplier.getEmail());
        if(msg == null) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Ingredient Supplier " + supplier.getName() + " has been updated", ""));
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, ""));            
        }
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect("ingredientsupplier.xhtml");
    }    
    public void cActionListener(ActionEvent event) throws IOException{
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("supplierID", event.getComponent().getAttributes().get("sID"));
        Long supplierID = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("supplierID");
        FacesContext.getCurrentInstance().getExternalContext().redirect("ingredientcontract.xhtml");
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public List<Country> getCountryList() {
        return countryList;
    }

    public void setCountryList(List<Country> countryList) {
        this.countryList = countryList;
    }

    public List<IngredientSupplier> getSupplierList() {
        return supplierList;
    }

    public void setSupplierList(List<IngredientSupplier> supplierList) {
        this.supplierList = supplierList;
    }
    
    
}
