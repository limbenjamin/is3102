/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.Entities;

import IslandFurniture.Enums.FurnitureCategory;
import java.io.Serializable;
import javax.persistence.Entity;

/**
 *
 * @author James
 */
@Entity
public class PromotionDetailByProductCategory extends PromotionDetail implements Serializable {

    private static final long serialVersionUID = 1L;
    private FurnitureCategory category;

    public FurnitureCategory getCategory() {
        return category;
    }

    public void setCategory(FurnitureCategory category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "ProductPromotionDetail[ id=" + id + " ]";
    }

}
