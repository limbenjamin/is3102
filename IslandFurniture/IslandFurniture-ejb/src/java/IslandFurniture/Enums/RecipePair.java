/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.Enums;

import IslandFurniture.Entities.Ingredient;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author a0101774
 */
@Entity
public class RecipePair implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id 
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne 
    private Ingredient ingredient;
    private Double quantity;
    
    public RecipePair() {
        
    }
    public RecipePair(Ingredient ingredient, Double quantity) {
        this.ingredient = ingredient;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object o) {
      if (o == null) return false;
      if (!(o instanceof RecipePair)) return false;
      RecipePair pairo = (RecipePair) o;
      return this.ingredient.equals(pairo.getIngredient()) &&
             this.quantity.equals(pairo.getQuantity());
    }
}
