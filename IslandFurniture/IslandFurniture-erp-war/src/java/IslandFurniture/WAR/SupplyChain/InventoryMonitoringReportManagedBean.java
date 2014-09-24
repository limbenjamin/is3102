/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.SupplyChain;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.EJB.Entities.CountryOffice;
import IslandFurniture.EJB.Entities.ManufacturingFacility;
import IslandFurniture.EJB.Entities.Plant;
import IslandFurniture.EJB.Entities.Staff;
import IslandFurniture.EJB.Entities.Stock;
import IslandFurniture.EJB.Entities.StockSupplied;
import IslandFurniture.EJB.Entities.StockUnit;
import IslandFurniture.EJB.Entities.Store;
import IslandFurniture.EJB.SupplyChain.ManageInventoryMonitoringLocal;
import IslandFurniture.StaticClasses.Helper.Couple;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpSession;

/**
 *
 * @author KamilulAshraf
 */
@ManagedBean
@ViewScoped
public class InventoryMonitoringReportManagedBean implements Serializable {

    private String username;
    private String plantType;
    private Staff staff;
    private Plant plant;

    private List<Couple<Stock, List<StockUnit>>> invList;
    private List<Stock> stockList;

    @EJB
    private ManageUserAccountBeanLocal staffBean;
    @EJB
    private ManageInventoryMonitoringLocal miml;

    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        staff = staffBean.getStaff(username);
        plant = staff.getPlant();
        plantType = (String) staff.getPlant().getClass().getSimpleName();
        //For spacing purpose when displaying in front end
        if (plantType.equals("ManufacturingFacility")) {
            plantType = "Manufacturing Facility";
        } else if (plantType.equals("CountryOffice")) {
            plantType = "Country Office";
        } else if (plantType.equals("GlobalHQ")) {
            plantType = ""; //no need cos global HQ global HQ looks ugly
        }
        
        invList = new ArrayList();
        stockList = new ArrayList();

        if (plant instanceof CountryOffice) {
            CountryOffice co = (CountryOffice) plant;
            for (StockSupplied ss : co.getSuppliedWithFrom()) {
                stockList.add(ss.getStock());
            }
        }

        if (plant instanceof ManufacturingFacility) {
            ManufacturingFacility mf = (ManufacturingFacility) plant;
            for (StockSupplied ss : mf.getSupplyingWhatTo()) {
                stockList.add(ss.getStock());
            }
        }
        
        if (plant instanceof Store) {
            Store store = (Store) plant;
            stockList = store.getSells();
        }
        
        for (Stock eachStock : stockList) {
            List<StockUnit> suList = miml.viewStockUnitbyStock(eachStock);
            invList.add(new Couple(eachStock,suList));
        }

    }

    public List<Couple<Stock, List<StockUnit>>> getInvList() {
        return invList;
    }

    public void setInvList(List<Couple<Stock, List<StockUnit>>> invList) {
        this.invList = invList;
    }

    public List<Stock> getStockList() {
        return stockList;
    }

    public void setStockList(List<Stock> stockList) {
        this.stockList = stockList;
    }

    public ManageInventoryMonitoringLocal getMiml() {
        return miml;
    }

    public void setMiml(ManageInventoryMonitoringLocal miml) {
        this.miml = miml;
    }
        
    public String getPlantType() {
        return plantType;
    }

    public void setPlantType(String plantType) {
        this.plantType = plantType;
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

    public ManageUserAccountBeanLocal getStaffBean() {
        return staffBean;
    }

    public void setStaffBean(ManageUserAccountBeanLocal staffBean) {
        this.staffBean = staffBean;
    }

}
