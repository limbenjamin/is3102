/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.Entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 *
 * @author James
 */
@Entity
@NamedQueries({
    @NamedQuery(
            name = "findDishByName",
            query = "SELECT a FROM Dish a WHERE a.name = :name"),
    @NamedQuery(name = "getDishListByCountryOffice",
            query = "SELECT a FROM Dish a WHERE a.countryOffice = :countryOffice"),
    @NamedQuery(name = "getDishByCountryOfficeAndName",
            query = "SELECT a FROM Dish a WHERE a.countryOffice = :countryOffice AND a.name = :name")
})
public class Dish implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id 
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private CountryOffice countryOffice;
    
    @ManyToMany(mappedBy = "dishes")
    private List<MenuItem> menuItem;
    
    @OneToMany
    private List<Ingredient> ingredients;

    public Dish() {
        
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CountryOffice getCountryOffice() {
        return countryOffice;
    }

    public void setCo(CountryOffice countryOffice) {
        this.countryOffice = countryOffice;
    }

    public List<MenuItem> getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(List<MenuItem> menuItem) {
        this.menuItem = menuItem;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
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
        if (!(object instanceof Dish)) {
            return false;
        }
        Dish other = (Dish) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FW.IslandFurniture.Entities.STORE.Dish[ id=" + id + " ]";
    }
    
}
