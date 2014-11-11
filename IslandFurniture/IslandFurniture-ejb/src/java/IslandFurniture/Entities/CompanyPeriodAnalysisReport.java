/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author James
 */
@Entity
public class CompanyPeriodAnalysisReport implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Double totalRevenue;

    @Temporal(TemporalType.DATE)
    private Calendar analysis_date;

    @OneToMany(mappedBy = "companyPeriodAnalysisReport", cascade = CascadeType.PERSIST)
    private List<CountryPeriodAnalysisReport> countryPeriodAnalysisReports = new ArrayList<>();

    public Calendar getAnalysis_date() {
        return analysis_date;
    }

    public void setAnalysis_date(Calendar analysis_date) {
        this.analysis_date = analysis_date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTotalRevenue() {
        Double ttotalrevenue = 0.0;
        for (CountryPeriodAnalysisReport par : this.getCountryPeriodAnalysisReports()) {
            ttotalrevenue += par.getTotalRevenue();
        }
        return ttotalrevenue;
    }

    public void setTotalRevenue(Double totalRevenue) {
        //this.totalRevenue = totalRevenue;
    }

    public List<CountryPeriodAnalysisReport> getCountryPeriodAnalysisReports() {
        return countryPeriodAnalysisReports;
    }

    public void setCountryPeriodAnalysisReports(List<CountryPeriodAnalysisReport> countryPeriodAnalysisReports) {
        this.countryPeriodAnalysisReports = countryPeriodAnalysisReports;
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
        if (!(object instanceof CompanyPeriodAnalysisReport)) {
            return false;
        }
        CompanyPeriodAnalysisReport other = (CompanyPeriodAnalysisReport) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CompanyPeriodAnalysisReport[ id=" + id + " ]";
    }

}
