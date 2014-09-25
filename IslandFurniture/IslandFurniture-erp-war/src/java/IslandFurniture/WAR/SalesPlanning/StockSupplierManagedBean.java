/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.SalesPlanning;

import IslandFurniture.EJB.Entities.CountryOffice;
import IslandFurniture.EJB.Entities.ManufacturingFacility;
import IslandFurniture.EJB.Entities.Stock;
import IslandFurniture.EJB.Entities.StockSupplied;
import IslandFurniture.EJB.SalesPlanning.SupplierManagerLocal;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author a0101774
 */
@ManagedBean
@ViewScoped
public class StockSupplierManagedBean implements Serializable {
    @EJB
    private SupplierManagerLocal supplierManager;
    private List<StockSupplied> stockSuppliedList = null;
    private List<CountryOffice> countryList = null;
    private List<ManufacturingFacility> mfList = null;
    private Long mfID = null;
    private List<Stock> stockList = null;

    public Long getMfID() {
        return mfID;
    }

    public void setMfID(Long mfID) {
        this.mfID = mfID;
    }

    public List<CountryOffice> getCountryList() {
        return countryList;
    }

    public void setCountryList(List<CountryOffice> countryList) {
        this.countryList = countryList;
    }

    public List<ManufacturingFacility> getMfList() {
        return mfList;
    }

    public void setMfList(List<ManufacturingFacility> mfList) {
        this.mfList = mfList;
    }

    public List<Stock> getStockList() {
        return stockList;
    }

    public void setStockList(List<Stock> stockList) {
        this.stockList = stockList;
    }

    public List<StockSupplied> getStockSuppliedList() {
        return stockSuppliedList;
    }

    public void setStockSuppliedList(List<StockSupplied> stockSuppliedList) {
        this.stockSuppliedList = stockSuppliedList;
    }
    
    @PostConstruct
    public void init() {
        System.out.println("init: StockSupplierManagedBean");
        stockSuppliedList = supplierManager.getAllStockSupplied();
        countryList = supplierManager.getListOfCountryOffice();
        mfList = supplierManager.getListOfMF();
        stockList = supplierManager.getListOfStock();
    }
    public void deleteStockSupplyRequest(AjaxBehaviorEvent event) {
        System.out.println("StockSupplierManagedBean.deleteStockSupplyRequest()");
        String sID = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("sID");
        String mfID = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("mfID");
        String cID = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("cID");
        System.out.println("CountryID is " + cID + ". MF ID is " + mfID + ". StockID is " + sID);
        supplierManager.deleteStockSupplyRequest(Long.parseLong(sID), Long.parseLong(mfID), Long.parseLong(cID));
        stockSuppliedList = supplierManager.getAllStockSupplied();
   //     return "stockSupplier";
    }
    public String addStockSupplyRequest(ActionEvent event) {
        System.out.println("StockSupplierManagedBean.addStockSupplyRequest()");
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String cID = request.getParameter("addSSRequestForm:cID");
        String mfID = request.getParameter("addSSRequestForm:mfID");
        String sID = request.getParameter("addSSRequestForm:sID");
        System.out.println("CountryID is " + cID + ". MF ID is " + mfID + ". StockID is " + sID);
        supplierManager.addStockSupplyRequest(Long.parseLong(sID), Long.parseLong(mfID), Long.parseLong(cID));
        return "stockSupplier";
    }
}
