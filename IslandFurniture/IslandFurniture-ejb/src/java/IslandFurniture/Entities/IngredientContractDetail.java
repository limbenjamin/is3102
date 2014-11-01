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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Entity
@NamedQueries({
    @NamedQuery(
            name = "getIngredientList",
            query = "SELECT a.ingredient FROM IngredientContractDetail a WHERE a.ingredContract.ingredSupplier = :supplier AND a.ingredContract.ingredSupplier.co = :co"),
    @NamedQuery(
            name = "getICDByIngredAndCo",
            query = "SELECT a FROM IngredientContractDetail a WHERE a.ingredient = :ingredient AND a.ingredContract.ingredSupplier.co = :co")    
})
public class IngredientContractDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer leadTimeInDays;
    private Integer lotSize;
    private Double lotPrice;
    
    @ManyToOne
    private IngredientContract ingredContract;
    
    @ManyToOne
    private Ingredient ingredient;
    
    public IngredientContractDetail() {
        
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLeadTimeInDays() {
        return leadTimeInDays;
    }

    public void setLeadTimeInDays(Integer leadTimeInDays) {
        this.leadTimeInDays = leadTimeInDays;
    }

    public Integer getLotSize() {
        return lotSize;
    }

    public void setLotSize(Integer lotSize) {
        this.lotSize = lotSize;
    }

    public IngredientContract getIngredContract() {
        return ingredContract;
    }

    public void setIngredContract(IngredientContract ingredContract) {
        this.ingredContract = ingredContract;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public Double getLotPrice() {
        return lotPrice;
    }

    public void setLotPrice(Double lotPrice) {
        this.lotPrice = lotPrice;
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
        if (!(object instanceof IngredientContractDetail)) {
            return false;
        }
        IngredientContractDetail other = (IngredientContractDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "IngredientContractDetail[ id=" + id + " ]";
    }
    
}
