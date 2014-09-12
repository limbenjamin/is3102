/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.StaticClasses.Helper;

import IslandFurniture.EJB.Entities.Month;
import IslandFurniture.EJB.Entities.Store;
import IslandFurniture.EJB.SalesPlanning.SalesForecastBeanLocal;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Stateless
public class LoadSalesForecastBean implements LoadSalesForecastBeanRemote {

    @PersistenceContext(unitName = "IslandFurniture")
    private EntityManager em;
    @EJB
    private SalesForecastBeanLocal salesForecastBean;

    @Override
    public boolean loadSampleData() {
        Store storeToForecast = (Store) QueryMethods.findPlantByName(em, QueryMethods.findCountryByName(em, "Singapore"), "Alexandra");
        System.out.println(storeToForecast);
        salesForecastBean.generateSalesFigures(storeToForecast, Month.NOV, 2013, Month.JAN, 2014);

        return false;
    }

}
