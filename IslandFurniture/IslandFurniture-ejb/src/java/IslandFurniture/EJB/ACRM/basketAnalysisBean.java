/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.ACRM;

import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.FurnitureModel;
import IslandFurniture.Entities.MarketBasketAnalysis;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author James
 */
@Stateless
public class basketAnalysisBean implements basketAnalysisBeanLocal {
    @PersistenceContext(unitName = "IslandFurniture")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    
    public List<FurnitureModel> findListOfrelated(FurnitureModel fm, CountryOffice co){
        
       Query q=em.createQuery("select mba from MarketBasketAnalysis mba where :fm MEMBER OF mba.furnituremodels and mba.countryOffice=:co");
       q.setParameter("fm", fm);
       q.setParameter("co", co);
       HashMap<FurnitureModel,Object> data=new HashMap();
       
       for (MarketBasketAnalysis mba : (List<MarketBasketAnalysis>)q.getResultList())
       {
           
       }
       
       data.remove(fm);
       
       return (new ArrayList(data.keySet()));
       
    
    }

    public void persist(Object object) {
        em.persist(object);
    }
    
    
}
