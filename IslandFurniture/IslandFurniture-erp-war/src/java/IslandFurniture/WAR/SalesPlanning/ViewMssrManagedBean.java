/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.SalesPlanning;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountInformationBean;
import IslandFurniture.EJB.Entities.CountryOffice;
import IslandFurniture.EJB.Entities.MonthlyStockSupplyReq;
import IslandFurniture.EJB.Entities.Plant;
import IslandFurniture.EJB.Entities.Staff;
import IslandFurniture.EJB.Entities.Stock;
import IslandFurniture.EJB.SalesPlanning.SalesForecastBeanLocal;
import IslandFurniture.StaticClasses.Helper.Couple;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
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

    private int yearOfMssr;
    private List<Integer> yearsOfMssr = new ArrayList();

    private Staff staff;
    private CountryOffice co;

    private List<Couple<Stock, List<MonthlyStockSupplyReq>>> mssrList;

    public ViewMssrManagedBean() {
    }

    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        this.staff = staffBean.getStaff((String) session.getAttribute("username"));

        Plant plant = staff.getPlant();

        if (plant instanceof CountryOffice) {
            this.co = (CountryOffice) plant;
            this.yearsOfMssr = salesForecastBean.getYearsOfMssr(co);
            this.yearOfMssr = this.yearsOfMssr.get(this.yearsOfMssr.size() - 1);
            this.updateMssrList();
        } else {
            try {
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect(ec.getRequestContextPath());
            } catch (IOException ex) {

            }
        }

        System.out.println("Init()");
    }

    public void updateMssr(AjaxBehaviorEvent event) {
        System.out.println("updateMssr");
        this.updateMssrList();
    }

    public void updateMssrList() {
        this.mssrList = salesForecastBean.retrieveMssrForCo(this.co, this.yearOfMssr);
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

    public List<Couple<Stock, List<MonthlyStockSupplyReq>>> getMssrList() {
        return mssrList;
    }

    public void setMssrList(List<Couple<Stock, List<MonthlyStockSupplyReq>>> mssrList) {
        this.mssrList = mssrList;
    }

}
