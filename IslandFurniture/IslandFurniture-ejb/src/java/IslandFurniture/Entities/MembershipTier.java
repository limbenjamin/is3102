/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
            name = "findMembershipTierByTitle",
            query = "SELECT a FROM MembershipTier a WHERE a.title = :title")
})
public class MembershipTier implements Serializable, Comparable<MembershipTier> {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToMany(mappedBy = "membershipTier")
    private List<Customer> members = new ArrayList();

    private String title;
    private Integer points;

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Customer> getMembers() {
        return members;
    }

    public void setMembers(List<Customer> members) {
        this.members = members;
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
        if (!(object instanceof MembershipTier)) {
            return false;
        }
        MembershipTier other = (MembershipTier) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {

        //Dont change this. i need this. JAMES
        return "[MembershipTier]" + id;

    }

    // Implement Comparable
    @Override
    public int compareTo(MembershipTier other) {
        if (this.points > other.getPoints()) {
            return 1;
        }
        
        if (this.points < other.getPoints()) {
            return -1;
        }

        return 0;
    }

}
