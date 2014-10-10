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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Entity
public class IngredientContract implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @OneToOne
    private IngredientSupplier ingredSupplier;
    
    @OneToMany(mappedBy="ingredContract", cascade={CascadeType.PERSIST})
    private List<IngredientContractDetail> ingredContractDetails = new ArrayList();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public IngredientSupplier getIngredSupplier() {
        return ingredSupplier;
    }

    public void setIngredSupplier(IngredientSupplier ingredSupplier) {
        this.ingredSupplier = ingredSupplier;
    }

    public List<IngredientContractDetail> getIngredContractDetails() {
        return ingredContractDetails;
    }

    public void setIngredContractDetails(List<IngredientContractDetail> ingredContractDetails) {
        this.ingredContractDetails = ingredContractDetails;
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
        if (!(object instanceof IngredientContract)) {
            return false;
        }
        IngredientContract other = (IngredientContract) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "IngredientContract[ id=" + id + " ]";
    }
    
}
