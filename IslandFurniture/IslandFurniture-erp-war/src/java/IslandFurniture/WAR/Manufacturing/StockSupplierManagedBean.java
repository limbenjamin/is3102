/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.Manufacturing;

import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.ManufacturingFacility;
import IslandFurniture.Entities.Stock;
import IslandFurniture.Entities.StockSupplied;
import IslandFurniture.EJB.Purchasing.SupplierManagerLocal;
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
    public String deleteStockSupplyRequest(ActionEvent event) { 
        System.out.println("StockSupplierManagedBean.deleteStockSupplyRequest()");
        String sID = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("sID");
        String mfID = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("mfID");
        String cID = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("cID");
        System.out.println("CountryID is " + cID + ". MF ID is " + mfID + ". StockID is " + sID);
        String msg = supplierManager.deleteStockSupplyRequest(Long.parseLong(sID), Long.parseLong(mfID), Long.parseLong(cID));
        stockSuppliedList = supplierManager.getAllStockSupplied();
        if(msg != null) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, "")); 
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Stock Supply Request has been successfully deleted", ""));    
        }
        return "stocksupplier#newSS";
    }
    public String addStockSupplyRequest(ActionEvent event) {
        System.out.println("StockSupplierManagedBean.addStockSupplyRequest()");
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String cID = request.getParameter("addSSRequestForm:cID");
        String mfID = request.getParameter("addSSRequestForm:mfID");
        String sID = request.getParameter("addSSRequestForm:sID");
        System.out.println("CountryID is " + cID + ". MF ID is " + mfID + ". StockID is " + sID);
        List<Stock> list = supplierManager.checkForValidPCD(Long.parseLong(sID), Long.parseLong(mfID));
        if(list == null) {
            String msg = supplierManager.addStockSupplyRequest(Long.parseLong(sID), Long.parseLong(mfID), Long.parseLong(cID));
            if(msg != null) {
                FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, "")); 
            } else {
                FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Stock Supply Request has been successfully added", ""));    
            }
            return "stocksupplier";
        }
        else {
            System.out.print("Missing ProcuredStockContractDetail of the following");
            String errorOutput = "";
            for(int i=0; i<list.size(); i++) 
                System.out.println("\t " + list.get(i).getName());
            if(list.size() > 1)
                errorOutput = "" + "Unable to add Stock Supply Request due to the missing Procured Stock Contract Details of the following items: ";
            else 
                errorOutput = "" + "Unable to add the Stock Supply Request due to the missing Procured Stock Contract Detail of the item: ";
            for(int i=1; i<=list.size(); i++) {  
                errorOutput = errorOutput + "<br />" + i + ") " + list.get(i-1).getName() + " ";
            }
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, errorOutput, "")); 
        }
        return "stocksupplier";
    } 
}
