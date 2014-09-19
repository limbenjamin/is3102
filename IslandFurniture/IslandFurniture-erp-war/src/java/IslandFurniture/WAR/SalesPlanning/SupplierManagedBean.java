/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.SalesPlanning;

import IslandFurniture.EJB.Entities.Supplier;
import IslandFurniture.EJB.SalesPlanning.SupplierManagerLocal;
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
public class SupplierManagedBean implements Serializable {
    @EJB
    private SupplierManagerLocal supplierManager;
    
    private List<Supplier> supplierList;

    public List<Supplier> getSupplierList() {
        return supplierList;
    }

    public void setSupplierList(List<Supplier> supplierList) {
        this.supplierList = supplierList;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }
    private Supplier supplier;
    
    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        supplierList = supplierManager.displaySupplierList();
        System.out.println("Init");
    }
    
    public String addSupplier() {
        System.out.println("SupplierManagedBean.addSupplier()");
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String supplierName = request.getParameter("addSupplierForm:name");
        String supplierCountry = request.getParameter("addSupplierForm:country");
        supplierManager.addSupplier(supplierName, supplierCountry);
        return "supplier";
    }
    public String editSupplier(ActionEvent event) throws IOException {
        System.out.println("SupplierManagedBean.editSupplier()");
        supplier = (Supplier) event.getComponent().getAttributes().get("toEdit");
        supplierManager.editSupplier(supplier.getId(), supplier.getName(), supplier.getCountry().getName());
        return "supplier";
    }
    public String deleteSupplier() {
        System.out.println("SupplierManagedBean.deleteSupplier()");
        Long id = new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("supplierID"));
        supplierManager.deleteSupplier(id);
        return "supplier";
    }
}
