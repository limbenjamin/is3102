/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.Purchasing;

import IslandFurniture.EJB.Purchasing.SupplierManagerLocal;
import IslandFurniture.EJB.SalesPlanning.CurrencyManagerLocal;
import IslandFurniture.Entities.Currency;
import IslandFurniture.Entities.ManufacturingFacility;
import IslandFurniture.Entities.ProcuredStock;
import IslandFurniture.Entities.ProcuredStockContractDetail;
import IslandFurniture.Entities.ProcuredStockSupplier;
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
public class ProcurementContractManagedBean implements Serializable {
    @EJB
    private CurrencyManagerLocal currencyManager;
    @EJB
    private SupplierManagerLocal supplierManager;
    
    private Long supplierID;
    private ProcuredStockSupplier supplier;
    private ProcuredStockContractDetail pcd;
    private List<ProcuredStockSupplier> supplierList;
    private List<ProcuredStockContractDetail> detailList = null;
    private List<ProcuredStock> stockList;
    private List<ManufacturingFacility> mfList;
    private List<Currency> currencyList;

    public ProcuredStockContractDetail getPcd() {
        return pcd;
    }
    public void setPcd(ProcuredStockContractDetail pcd) {
        this.pcd = pcd;
    }
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
    public List<ProcuredStockContractDetail> getDetailList() {
        return detailList;
    }
    public void setDetailList(List<ProcuredStockContractDetail> detailList) {
        this.detailList = detailList;
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
    public List<Currency> getCurrencyList() {
        return currencyList;
    }

    public void setCurrencyList(List<Currency> currencyList) {
        this.currencyList = currencyList;
    }
    
    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession(); 
        System.out.println("init:ProcurementContractManagedBean");   
        this.supplierID = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("supplierID");
        try {
            if(supplierID == null) {
                    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                    ec.redirect("supplier.xhtml");
            } else { 
                System.out.println("SupplierID is " + supplierID);

                supplier = supplierManager.getSupplier(supplierID);
                supplierList = supplierManager.displaySupplierList();
                stockList = supplierManager.displayProcuredStock();
                mfList = supplierManager.displayManufacturingFacility();
                currencyList = currencyManager.getAllCurrency();

                detailList = supplier.getProcuredStockContract().getProcuredStockContractDetails();
                System.out.println("Supplier " + supplier.getName() + " has " + detailList.size() + " items");
            }
        } catch(IOException ex) {
            
        }
    }
    
    public String displayContractDetails() {
        System.out.println("ProcurementContractManagedBean.displayContractDetail()");
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String supplierId = request.getParameter("selectSupplier:supplierID");
        detailList = supplierManager.displayProcurementContractDetails(supplierId);
        this.setSupplierID(Long.parseLong(supplierId));
        System.out.println(this.getSupplierID());
        return "procurementcontract";
    }
    public String deleteProcurementContractDetail() {
        System.out.println("ProcurementContractManagedBean.deleteProcurementContractDetail()");
        Long id = new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("pcdID"));
        String msg = supplierManager.deleteProcurementContractDetail(id, this.supplierID);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("supplierID", supplier.getId());
        if(msg != null) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, "")); 
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Procurement Contract Detail has been successfully deleted", ""));    
        }    
        return "procurementcontract"; 
    }
    public String addProcurementContractDetail() {
        System.out.println("ProcurementContractManagedBean.addProcurementContractDetail()");
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String supplierId = request.getParameter("addPCD:supplierID");
        String stockID = request.getParameter("addPCD:stockID");
        String mfID = request.getParameter("addPCD:mfID");
        String size = request.getParameter("addPCD:size");
        String leadTime = request.getParameter("addPCD:leadTime"); 
        String lotPrice = request.getParameter("addPCD:lotPrice");
        String currencyID = request.getParameter("addPCD:currencyID");
        System.out.println("supplierID is " + this.getSupplierID() + ".supplierId is " + supplierId + ". stockID is " + stockID + ". mfID is " + mfID);
        String msg = supplierManager.addProcurementContractDetail(Long.parseLong(supplierId), Long.parseLong(mfID), Long.parseLong(stockID), Integer.parseInt(size), Integer.parseInt(leadTime), Double.parseDouble(lotPrice), Long.parseLong(currencyID));
        if(msg != null) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, "")); 
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Procurement Contract Detail has been successfully added", ""));    
        }    
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("supplierID", supplier.getId());
        return "procurementcontract";
    }
    public String editProcurementContractDetail(ActionEvent event) throws IOException { 
        System.out.println("ProcurementContractManagedBean.editProcurementContractDetail()"); 
        pcd = (ProcuredStockContractDetail) event.getComponent().getAttributes().get("toEdit"); 
        System.out.println("toEdit: Currency is " + pcd.getCurrency().getName());
        String msg = supplierManager.editProcurementContractDetail(pcd.getId(), pcd.getLotSize(), pcd.getLeadTimeInDays(), pcd.getLotPrice(), Long.parseLong(pcd.getCurrency().getName()));  
        if(msg != null) { 
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, ""));
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Procurement Contract Detail has been updated", ""));
        }
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("supplierID", supplier.getId());
        return "procurementcontract";
    }
    
}
