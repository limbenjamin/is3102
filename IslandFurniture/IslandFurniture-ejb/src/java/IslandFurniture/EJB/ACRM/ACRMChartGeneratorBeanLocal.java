/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.ACRM;

import IslandFurniture.Entities.CompanyPeriodAnalysisReport;
import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.Customer;
import java.util.Calendar;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author James
 */
@Local
public interface ACRMChartGeneratorBeanLocal {

    public List<CompanyPeriodAnalysisReport> getCompanyReport(Calendar from, Calendar to);

    public CountryOffice getCountryOffice(String username);

    public Boolean hasHQPermission(String username);

    public List<Customer> filterCustomer(String Country);
    
}
