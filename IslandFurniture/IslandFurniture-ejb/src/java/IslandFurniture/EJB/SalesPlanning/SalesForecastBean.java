/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.SalesPlanning;

import IslandFurniture.EJB.Entities.FurnitureTransaction;
import IslandFurniture.EJB.Entities.Month;
import IslandFurniture.EJB.Entities.MonthlyStockSupplyReq;
import IslandFurniture.EJB.Entities.Store;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Stateless
public class SalesForecastBean implements SalesForecastBeanLocal {

    @PersistenceContext(unitName = "IslandFurniture")
    private EntityManager em;

    private List<FurnitureTransaction> getStoreTransactions(Store store, Month startMonth, int startYear, Month endMonth, int endYear){
        Calendar start = Calendar.getInstance(TimeZone.getTimeZone(store.getCountry().getTimeZoneID()));
        Calendar end = Calendar.getInstance(TimeZone.getTimeZone(store.getCountry().getTimeZoneID()));
        //start.
        
        Query q = em.createNamedQuery("getStoreTransactions");
        q.setParameter("store", store);
        q.setParameter("startDate", start, TemporalType.DATE);
        q.setParameter("endDate", end, TemporalType.DATE);
        
        return null;
    }
    
    @Override
    public List<MonthlyStockSupplyReq> generateSalesFigures(Store store, Month startMonth, int startYear, Month endMonth, int endYear) {

        return null;
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
