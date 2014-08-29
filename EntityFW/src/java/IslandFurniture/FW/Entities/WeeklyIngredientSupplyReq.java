/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.FW.Entities;

import java.io.Serializable;
import java.util.List;
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
public class WeeklyIngredientSupplyReq implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long period;
    private Long qty;

    @ManyToOne
    private Ingredient ingredient;

    @OneToMany(mappedBy = "weeklyIngredientSupplyReq")
    private List<DailyIngredientSupplyReq> dailyReq;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPeriod() {
        return period;
    }

    public void setPeriod(Long period) {
        this.period = period;
    }

    public Long getQty() {
        return qty;
    }

    public void setQty(Long qty) {
        this.qty = qty;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public List<DailyIngredientSupplyReq> getDailyReq() {
        return dailyReq;
    }

    public void setDailyReq(List<DailyIngredientSupplyReq> dailyReq) {
        this.dailyReq = dailyReq;
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
        if (!(object instanceof WeeklyIngredientSupplyReq)) {
            return false;
        }
        WeeklyIngredientSupplyReq other = (WeeklyIngredientSupplyReq) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FW.IslandFurniture.Entities.STORE.DailyIngredientSupplyReq[ id=" + id + " ]";
    }
    
}
