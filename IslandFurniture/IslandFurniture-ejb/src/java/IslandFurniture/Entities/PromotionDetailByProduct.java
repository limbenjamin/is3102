/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.Entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 *
 * @author James
 */
@Entity
public class PromotionDetailByProduct extends PromotionDetail implements Serializable {

    private static final long serialVersionUID = 1L;
    @ManyToOne
    private Stock stock;

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "ProductPromotionDetail[ id=" + id + " ]";
    }

}
