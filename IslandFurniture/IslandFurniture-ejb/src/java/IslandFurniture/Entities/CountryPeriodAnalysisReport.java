/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.Entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import IslandFurniture.Entities.CustomerPeriodAnalysisReport;

/**
 *
 * @author James
 */
@Entity
public class CountryPeriodAnalysisReport implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private CountryOffice countryOffice;

    private CompanyPeriodAnalysisReport companyPeriodAnalysisReport;

    @OneToMany(mappedBy = "countryPeriodAnalysisReport")
    private List<StorePeriodAnalysisReport> storePeriodAnalysisReports;

    @OneToMany(mappedBy = "countryPeriodAnalysisReport")
    private List<CustomerPeriodAnalysisReport> customerPeriodAnalysisReport;

    public List<CustomerPeriodAnalysisReport> getCustomerPeriodAnalysisReport() {
        return customerPeriodAnalysisReport;
    }

    public void setCustomerPeriodAnalysisReport(List<CustomerPeriodAnalysisReport> customerPeriodAnalysisReport) {
        this.customerPeriodAnalysisReport = customerPeriodAnalysisReport;
    }

    
    
    
    public List<StorePeriodAnalysisReport> getStorePeriodAnalysisReports() {
        return storePeriodAnalysisReports;
    }

    public void setStorePeriodAnalysisReports(List<StorePeriodAnalysisReport> storePeriodAnalysisReports) {
        this.storePeriodAnalysisReports = storePeriodAnalysisReports;
    }

    public CompanyPeriodAnalysisReport getCompanyPeriodAnalysisReport() {
        return companyPeriodAnalysisReport;
    }

    public void setCompanyPeriodAnalysisReport(CompanyPeriodAnalysisReport companyPeriodAnalysisReport) {
        this.companyPeriodAnalysisReport = companyPeriodAnalysisReport;
    }

    public CountryOffice getCountryOffice() {
        return countryOffice;
    }

    public void setCountryOffice(CountryOffice countryOffice) {
        this.countryOffice = countryOffice;
    }

    @OneToMany(mappedBy = "countryPeriodAnalysisReport")
    private List<StorePeriodAnalysisReport> storeperiodAnalysisReports;

    public List<StorePeriodAnalysisReport> getStoreperiodAnalysisReports() {
        return storeperiodAnalysisReports;
    }

    public void setStoreperiodAnalysisReports(List<StorePeriodAnalysisReport> storeperiodAnalysisReports) {
        this.storeperiodAnalysisReports = storeperiodAnalysisReports;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CountryPeriodAnalysisReport)) {
            return false;
        }
        CountryPeriodAnalysisReport other = (CountryPeriodAnalysisReport) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "IslandFurniture.Entities.CountryPeriodAnalysisReport[ id=" + id + " ]";
    }

}
