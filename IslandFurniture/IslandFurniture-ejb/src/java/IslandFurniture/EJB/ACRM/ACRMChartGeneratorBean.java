/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.ACRM;

import IslandFurniture.Entities.CompanyPeriodAnalysisReport;
import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.Customer;
import IslandFurniture.Entities.GlobalHQ;
import IslandFurniture.Entities.ManufacturingFacility;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Store;
import java.util.Calendar;
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
public class ACRMChartGeneratorBean implements ACRMChartGeneratorBeanLocal {

    @PersistenceContext(unitName = "IslandFurniture")
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    //for the sake of abstraction between view layer and model layer -.-
    public List<CompanyPeriodAnalysisReport> getCompanyReport(Calendar from, Calendar to) {
        Query q = em.createQuery("select cpar from CompanyPeriodAnalysisReport cpar where cpar.analysis_date>=:from and cpar.analysis_date<=:to");
        q.setParameter("from", from);
        q.setParameter("to", to);

        return ((List<CompanyPeriodAnalysisReport>) q.getResultList());

    }

    public CountryOffice getCountryOffice(String username) {
        Query q = em.createQuery("select st.plant from Staff st where st.username=:un");
        q.setParameter("un", username);
        Plant temp = (Plant) q.getResultList().get(0);
        if (temp instanceof CountryOffice) {
            return (CountryOffice) temp;
        }
        if (temp instanceof GlobalHQ) {
            return (CountryOffice) em.createQuery("select co from CountryOffice co").getResultList().get(0);
        }

        if (temp instanceof Store) {
            return ((Store) temp).getCountryOffice();
        }

        if (temp instanceof ManufacturingFacility) {

            return ((ManufacturingFacility) temp).getCountryOffice();
        }

        return null;

    }

    public Boolean hasHQPermission(String username) {

        Query q = em.createQuery("select st.plant from Staff st where st.username=:un");
        q.setParameter("un", username);
        Plant temp = (Plant) q.getResultList().get(0);
        if (temp instanceof GlobalHQ) {
            return true;
        }

        return false;

    }

    public List<Customer> filterCustomer(String Country) { //since our data dont have country data... this basically grab all
        Query l = em.createQuery("select c from Customer c ORDER BY c.lifetimeVal desc"); //and co.country=c.country
        return (List<Customer>) l.getResultList();

    }

}
