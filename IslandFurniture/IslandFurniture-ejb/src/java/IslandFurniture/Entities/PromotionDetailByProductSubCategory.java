/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.Entities;

import IslandFurniture.Enums.FurnitureCategory;
import IslandFurniture.Enums.FurnitureSubcategory;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author James
 */
@Entity
public class PromotionDetailByProductSubCategory extends PromotionDetail {

    private static final long serialVersionUID = 1L;
    private FurnitureSubcategory category;

    public FurnitureSubcategory getCategory() {
        return category;
    }

    public void setCategory(FurnitureSubcategory category) {
        this.category = category;
    }


    @Override
    public String toString() {
        return "IslandFurniture.Entities.ProductPromotionDetail[ id=" + id+ " ]";
    }

}
