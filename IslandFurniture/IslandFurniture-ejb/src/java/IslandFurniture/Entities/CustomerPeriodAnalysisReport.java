/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.Entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 *
 * @author James
 */
@Entity
public class CustomerPeriodAnalysisReport implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne
    private CountryPeriodAnalysisReport countryPeriodAnalysisReport;

    @OneToOne
    private CustomerSegment customerSegment;

    private Long qty;
    private Double revenue;

    public Long getQty() {
        return qty;
    }

    public void setQty(Long qty) {
        this.qty = qty;
    }

    public Double getRevenue() {
        return revenue;
    }

    public void setRevenue(Double revenue) {
        this.revenue = revenue;
    }

    
    public CustomerSegment getCustomerSegment() {
        return customerSegment;
    }

    public void setCustomerSegment(CustomerSegment customerSegment) {
        this.customerSegment = customerSegment;
    }

    public CountryPeriodAnalysisReport getCountryPeriodAnalysisReport() {
        return countryPeriodAnalysisReport;
    }

    public void setCountryPeriodAnalysisReport(CountryPeriodAnalysisReport countryPeriodAnalysisReport) {
        this.countryPeriodAnalysisReport = countryPeriodAnalysisReport;
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
        if (!(object instanceof CustomerPeriodAnalysisReport)) {
            return false;
        }
        CustomerPeriodAnalysisReport other = (CustomerPeriodAnalysisReport) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "IslandFurniture.Entities.StorePeriodAnalysisReport[ id=" + id + " ]";
    }

}
