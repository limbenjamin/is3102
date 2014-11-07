/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.Kitchen;

import IslandFurniture.DataStructures.Couple;
import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.EJB.Kitchen.FoodForecastBeanLocal;
import IslandFurniture.Entities.MonthlyStockSupplyReq;
import IslandFurniture.Entities.Staff;
import IslandFurniture.Entities.Stock;
import IslandFurniture.Entities.Store;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@ManagedBean
@ViewScoped
public class ViewMmsfManagedBean {

    @EJB
    private FoodForecastBeanLocal foodForecastBean;

    @EJB
    private ManageUserAccountBeanLocal staffBean;

    private int yearOfMmsf;
    private List<Integer> yearsOfMmsf = new ArrayList();

    private Staff staff;
    private Store store;

    private List<Couple<String, String>> mmsfLabels = new ArrayList();
    private List<Couple<Stock, List<MonthlyStockSupplyReq>>> mmsfList;

    public ViewMmsfManagedBean() {
    }

//    @PostConstruct
//    public void init() {
//        HttpSession session = Util.getSession();
//        this.staff = staffBean.getStaff((String) session.getAttribute("username"));
//
//        Plant plant = staff.getPlant();
//
//        if (plant instanceof Store) {
//            // Populate MSSR labels and formatted labels
//            this.mmsfLabels = MonthlyStockSupplyReq.getLabels();
//
//            this.store = (Store) store;
//
//            this.yearsOfMmsf = foodForecastBean.getYearsOfMmsf(store);
//            if (!this.yearsOfMmsf.isEmpty()) {
//                this.yearOfMmsf = this.yearsOfMmsf.get(this.yearsOfMmsf.size() - 1);
//                this.updateMmsfList();
//            }
//        } else {
//            try {
//                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
//                ec.redirect(ec.getRequestContextPath());
//            } catch (IOException ex) {
//
//            }
//        }
//    }
//
//    public void updateMmsf(AjaxBehaviorEvent event) {
//        this.updateMmsfList();
//    }
//
//    public void updateMmsfList() {
//        this.mmsfList = new ArrayList();
//
//        for (MenuItem mi : store.getCountryOffice().getMenuItems()) {
//            this.mmsfList.add(new Couple(mi, foodForecastBean.retrieveMmsfForStoreMi(store, mi, this.yearOfMmsf)));
//        }
//        
//    }
    
    //
    // Getters & Setters
    //

 
}
