/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

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
    private Double totalRevenue;

    @ManyToOne
    private CountryOffice countryOffice;

    @ManyToOne
    private CompanyPeriodAnalysisReport companyPeriodAnalysisReport;

    @OneToMany(mappedBy = "countryPeriodAnalysisReport",cascade = CascadeType.PERSIST)
    private List<StorePeriodAnalysisReport> storePeriodAnalysisReports  = new ArrayList<>();

    @OneToMany(mappedBy = "countryPeriodAnalysisReport",cascade = CascadeType.PERSIST)
    private List<CustomerPeriodAnalysisReport> customerPeriodAnalysisReport = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTotalRevenue() {
        Double ttotalrevenue=0.0;
        for (StorePeriodAnalysisReport spar : this.getStorePeriodAnalysisReports()){
            ttotalrevenue+=spar.getTotalRevenue();
        }
        
        
        return ttotalrevenue;
    }

    public void setTotalRevenue(Double totalRevenue) {
        //this.totalRevenue = totalRevenue;
    }

    public CountryOffice getCountryOffice() {
        return countryOffice;
    }

    public void setCountryOffice(CountryOffice countryOffice) {
        this.countryOffice = countryOffice;
    }

    public CompanyPeriodAnalysisReport getCompanyPeriodAnalysisReport() {
        return companyPeriodAnalysisReport;
    }

    public void setCompanyPeriodAnalysisReport(CompanyPeriodAnalysisReport companyPeriodAnalysisReport) {
        this.companyPeriodAnalysisReport = companyPeriodAnalysisReport;
    }

    public List<StorePeriodAnalysisReport> getStorePeriodAnalysisReports() {
        return storePeriodAnalysisReports;
    }

    public void setStorePeriodAnalysisReports(List<StorePeriodAnalysisReport> storePeriodAnalysisReports) {
        this.storePeriodAnalysisReports = storePeriodAnalysisReports;
    }

    public List<CustomerPeriodAnalysisReport> getCustomerPeriodAnalysisReport() {
        return customerPeriodAnalysisReport;
    }

    public void setCustomerPeriodAnalysisReport(List<CustomerPeriodAnalysisReport> customerPeriodAnalysisReport) {
        this.customerPeriodAnalysisReport = customerPeriodAnalysisReport;
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
        return "CountryPeriodAnalysisReport[ id=" + id + " ]";
    }

}
