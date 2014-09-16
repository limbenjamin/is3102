/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.SalesPlanning;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountInformationBean;
import IslandFurniture.EJB.Entities.Country;
import IslandFurniture.EJB.Entities.MonthlyStockSupplyReq;
import IslandFurniture.EJB.Entities.Plant;
import IslandFurniture.EJB.Entities.Staff;
import IslandFurniture.EJB.Entities.Stock;
import IslandFurniture.EJB.Entities.Store;
import IslandFurniture.EJB.SalesPlanning.SalesForecastBeanLocal;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@ManagedBean(name = "viewMssrManagedBean")
@ViewScoped
public class ViewMssrManagedBean implements Serializable {

    @EJB
    private SalesForecastBeanLocal salesForecastBean;

    @EJB
    private ManageUserAccountInformationBean staffBean;

    private long storeId;
    private List<Store> storeListing = new ArrayList();

    private int yearOfMssr;
    private List<Integer> yearsOfMssr = new ArrayList();

    private Staff staff;
    private Country country;

    private Map<Stock, List<MonthlyStockSupplyReq>> mssrMap;

    public ViewMssrManagedBean() {
    }

    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        this.staff = staffBean.getStaff((String) session.getAttribute("username"));

        System.out.println(staff);

        this.country = staff.getPlant().getCountry();
        for (Plant eachPlant : this.country.getPlants()) {
            if (eachPlant instanceof Store) {
                storeListing.add((Store) eachPlant);
            }
        }
    }

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    public int getYearOfMssr() {
        return yearOfMssr;
    }

    public void setYearOfMssr(int yearOfMssr) {
        this.yearOfMssr = yearOfMssr;
    }

    public List<Integer> getYearsOfMssr() {
        return yearsOfMssr;
    }

    public void setYearsOfMssr(List<Integer> yearsOfMssr) {
        this.yearsOfMssr = yearsOfMssr;
    }

    public List<Store> getStoreListing() {
        return storeListing;
    }

    public void setStoreListing(List<Store> storeListing) {
        this.storeListing = storeListing;
    }

    public Map<Stock, List<MonthlyStockSupplyReq>> getMssrMap() {
        return mssrMap;
    }

    public void setMssrMap(Map<Stock, List<MonthlyStockSupplyReq>> mssrMap) {
        this.mssrMap = mssrMap;
    }

    public void updateMssr(AjaxBehaviorEvent event) {
        System.out.println(this.yearOfMssr + " | " + this.storeId);

        if (this.yearOfMssr != 0 && this.storeId != 0) {
            this.mssrMap = salesForecastBean.retrieveMssrForStore(this.storeId, this.yearOfMssr);
        } else if (this.storeId != 0) {
            this.yearsOfMssr = salesForecastBean.getYearsOfMssr(this.storeId);
        }
    }

    public List<Map.Entry<Stock, List<MonthlyStockSupplyReq>>> getMssrList() {
        List<Map.Entry<Stock, List<MonthlyStockSupplyReq>>> mssrList = new ArrayList();
        if (this.mssrMap != null) {
            mssrList.addAll(this.mssrMap.entrySet());
        }

        return mssrList;
    }

}
