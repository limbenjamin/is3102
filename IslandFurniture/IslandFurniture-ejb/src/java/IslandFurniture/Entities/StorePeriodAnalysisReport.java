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
public class StorePeriodAnalysisReport implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Double totalRevenue;

    @ManyToOne
    private Store store;

    @ManyToOne
    private CountryPeriodAnalysisReport countryPeriodAnalysisReport;

    @OneToMany(mappedBy = "storePeriodAnalysisReport", cascade = CascadeType.PERSIST)
    private List<ProductAnalysisReport> productAnalysisReports = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTotalRevenue() {
        Double ttotalrevenue = 0.0;
        for (ProductAnalysisReport par : this.getProductAnalysisReports()) {
            ttotalrevenue += par.getTotalRevenue();
        }

        return ttotalrevenue;
    }

    public void setTotalRevenue(Double totalRevenue) {
        //this.totalRevenue = totalRevenue;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public CountryPeriodAnalysisReport getCountryPeriodAnalysisReport() {
        return countryPeriodAnalysisReport;
    }

    public void setCountryPeriodAnalysisReport(CountryPeriodAnalysisReport countryPeriodAnalysisReport) {
        this.countryPeriodAnalysisReport = countryPeriodAnalysisReport;
    }

    public List<ProductAnalysisReport> getProductAnalysisReports() {
        return productAnalysisReports;
    }

    public void setProductAnalysisReports(List<ProductAnalysisReport> productAnalysisReports) {
        this.productAnalysisReports = productAnalysisReports;
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
        if (!(object instanceof StorePeriodAnalysisReport)) {
            return false;
        }
        StorePeriodAnalysisReport other = (StorePeriodAnalysisReport) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "StorePeriodAnalysisReport[ id=" + id + " ]";
    }

}
