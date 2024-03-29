/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.InventoryManagement;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Staff;
import IslandFurniture.Entities.StockUnit;
import IslandFurniture.EJB.InventoryManagement.ManageInventoryMonitoringLocal;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

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
    private JasperPrint jp;

    private List<StockUnit> stockUnitList;

    @EJB
    private ManageUserAccountBeanLocal staffBean;
    @EJB
    private ManageInventoryMonitoringLocal monitoringBean;

    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        staff = staffBean.getStaff(username);
        plant = staff.getPlant();
        plantType = (String) staff.getPlant().getClass().getSimpleName();
        
        if (plantType.equals("ManufacturingFacility")) {
            plantType = "Manufacturing Facility";
        } else if (plantType.equals("CountryOffice")) {
            plantType = "Country Office";
        } else if (plantType.equals("GlobalHQ")) {
            plantType = ""; //no need cos global HQ global HQ looks ugly
        }

        stockUnitList = monitoringBean.viewStockUnitAll(plant);
    }
    
    public void prepareExport() throws JRException {
        JRBeanCollectionDataSource jbcd  = new JRBeanCollectionDataSource(stockUnitList);
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        String path = externalContext.getRealPath("/WEB-INF/jasper/report1.jasper");
        this.jp = JasperFillManager.fillReport(path, new HashMap(),jbcd);
    }
    
    public void PDF(ActionEvent actionEvent) throws JRException, IOException {
        prepareExport();
        HttpServletResponse resp = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        ServletOutputStream stream = resp.getOutputStream();
        JasperExportManager.exportReportToPdfStream(jp, stream);
        FacesContext.getCurrentInstance().responseComplete();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPlantType() {
        return plantType;
    }

    public void setPlantType(String plantType) {
        this.plantType = plantType;
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

    public List<StockUnit> getStockUnitList() {
        return stockUnitList;
    }

    public void setStockUnitList(List<StockUnit> stockUnitList) {
        this.stockUnitList = stockUnitList;
    }

    public ManageUserAccountBeanLocal getStaffBean() {
        return staffBean;
    }

    public void setStaffBean(ManageUserAccountBeanLocal staffBean) {
        this.staffBean = staffBean;
    }

    public ManageInventoryMonitoringLocal getMonitoringBean() {
        return monitoringBean;
    }

    public void setMonitoringBean(ManageInventoryMonitoringLocal monitoringBean) {
        this.monitoringBean = monitoringBean;
    }

}

//        ---- Old Code by Chen Tong ----
//
//    private List<Couple<Stock, List<StockUnit>>> invList;
//    private List<Stock> stockList;
//
//        invList = new ArrayList();
//        stockList = new ArrayList();
//
//        if (plant instanceof CountryOffice) {
//            CountryOffice co = (CountryOffice) plant;
//            for (StockSupplied ss : co.getSuppliedWithFrom()) {
//                stockList.add(ss.getStock());
//            }
//        }
//
//        if (plant instanceof ManufacturingFacility) {
//            ManufacturingFacility mf = (ManufacturingFacility) plant;
//            for (StockSupplied ss : mf.getSupplyingWhatTo()) {
//                stockList.add(ss.getStock());
//            }
//        }
//        
//        if (plant instanceof Store) {
//            Store store = (Store) plant;
//            stockList = store.getSells();
//        }
//        
//        for (Stock eachStock : stockList) {
//            List<StockUnit> suList = miml.viewStockUnitbyStock(eachStock);
//            invList.add(new Couple(eachStock,suList));
//        }