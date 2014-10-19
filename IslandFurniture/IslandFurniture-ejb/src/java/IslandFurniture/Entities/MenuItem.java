/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.Entities;

import IslandFurniture.Enums.MenuType;
import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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
            name = "findMenuItemByName",
            query = "SELECT a FROM MenuItem a WHERE a.name = :name"),
    @NamedQuery(name = "getMenuItemListByCountryOffice",
            query = "SELECT a FROM MenuItem a WHERE a.countryOffice = :countryOffice"),
    @NamedQuery(name = "getMenuItemByCountryOfficeAndName",
            query = "SELECT a FROM MenuItem a WHERE a.countryOffice = :countryOffice AND a.name = :name"),
    @NamedQuery(name = "getMenuItemListByCountryOfficeAndMenuType",
            query = "SELECT a FROM MenuItem a WHERE a.countryOffice = :countryOffice AND a.menuType = :menuType")
})
public class MenuItem implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private Double price;
    private Long pointsWorth;
    
    private MenuType menuType;
    private boolean alaCarte;
    
    @ManyToOne
    private CountryOffice countryOffice;
    
    @OneToMany(mappedBy="menuItem", cascade={CascadeType.PERSIST})
    private List<MenuItemDetail> menuItemDetails;
    

    public MenuItem() {
        
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getPointsWorth() {
        return pointsWorth;
    }

    public void setPointsWorth(Long pointsWorth) {
        this.pointsWorth = pointsWorth;
    }



    public CountryOffice getCountryOffice() {
        return countryOffice;
    }

    public void setCountryOffice(CountryOffice countryOffice) {
        this.countryOffice = countryOffice;
    }

    public List<MenuItemDetail> getMenuItemDetails() {
        return menuItemDetails;
    }

    public void setMenuItemDetails(List<MenuItemDetail> menuItemDetails) {
        this.menuItemDetails = menuItemDetails;
    }

    public MenuType getMenuType() {
        return menuType;
    }

    public void setMenuType(MenuType menuType) {
        this.menuType = menuType;
    }

    public boolean isAlaCarte() {
        return alaCarte;
    }

    public void setAlaCarte(boolean alaCarte) {
        this.alaCarte = alaCarte;
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
        if (!(object instanceof MenuItem)) {
            return false;
        }
        MenuItem other = (MenuItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MenuItem[ id=" + id + " ]";
    }
    
}
