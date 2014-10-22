/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.Entities;

import IslandFurniture.Enums.Month;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

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

    private Month month;
    private Integer week;
    private Integer year;

    @OneToMany(mappedBy = "companyPeriodAnalysisReport")
    private List<CountryPeriodAnalysisReport> countryPeriodAnalysisReports;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(Double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public Integer getWeek() {
        return week;
    }

    public void setWeek(Integer week) {
        this.week = week;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
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
