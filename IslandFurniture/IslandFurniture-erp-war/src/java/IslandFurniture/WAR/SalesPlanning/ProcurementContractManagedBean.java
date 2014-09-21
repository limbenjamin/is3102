/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.SalesPlanning;

import IslandFurniture.EJB.Entities.ManufacturingFacility;
import IslandFurniture.EJB.Entities.ProcuredStock;
import IslandFurniture.EJB.Entities.ProcurementContractDetail;
import IslandFurniture.EJB.Entities.Supplier;
import IslandFurniture.EJB.SalesPlanning.SupplierManagerLocal;
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
public class ProcurementContractManagedBean implements Serializable {
    @EJB
    private SupplierManagerLocal supplierManager;
    private List<Supplier> supplierList;
    private Supplier supplier;
    private List<ProcurementContractDetail> detailList;
    private List<ProcuredStock> stockList;
    private List<ManufacturingFacility> mfList;
    private Long supplierID;

    public Long getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(Long supplierID) {
        this.supplierID = supplierID;
    }

    public List<ProcuredStock> getStockList() {
        return stockList;
    }

    public void setStockList(List<ProcuredStock> stockList) {
        this.stockList = stockList;
    }

    public List<ManufacturingFacility> getMfList() {
        return mfList;
    }

    public void setMfList(List<ManufacturingFacility> mfList) {
        this.mfList = mfList;
    }

    public List<ProcurementContractDetail> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<ProcurementContractDetail> detailList) {
        this.detailList = detailList;
    }
    
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
    
    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();        
        this.supplierID = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("supplierID");
        System.out.println("FurnitureID is " + supplierID);
        supplier = supplierManager.getSupplier(supplierID);
        supplierList = supplierManager.displaySupplierList();
        stockList = supplierManager.displayProcuredStock();
        mfList = supplierManager.displayManufacturingFacility();
        System.out.println("init()");
    }
    
    public String displayContractDetails() {
        System.out.println("ProcurementContractManagedBean.displayContractDetail()");
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String supplierId = request.getParameter("selectSupplier:supplierID");
        detailList = supplierManager.displayProcurementContractDetails(supplierId);
        this.setSupplierID(Long.parseLong(supplierId));
        System.out.println(this.getSupplierID());
        return "procurementContract";
    }
    public String deleteProcurementContractDetail() {
        System.out.println("ProcurementContractManagedBean.deleteProcurementContractDetail()");
        Long id = new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("pcdID"));
        supplierManager.deleteProcurementContractDetail(id);
        return "procurementContract";
    }
    public String addProcurementContractDetail() {
        System.out.println("ProcurementContractManagedBean.addProcurementContractDetail()");
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String supplierId = request.getParameter("addPCD:supplierID");
        String stockID = request.getParameter("addPCD:stockID");
        String mfID = request.getParameter("addPCD:mfID");
        System.out.println("supplierID is " + this.getSupplierID() + ".supplierId is " + supplierId + ". stockID is " + stockID + ". mfID is " + mfID);
        supplierManager.addProcurementContractDetail(Long.parseLong("201"), Long.parseLong(mfID), Long.parseLong(stockID));
        return "procurementContract";
    }
    
}
