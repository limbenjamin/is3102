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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author James
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"COUNTRY_ID", "NAME"})
})
@NamedQueries({
    @NamedQuery(
            name = "findPlantByName",
            query = "SELECT a FROM Plant a WHERE a.country = :country AND a.name = :name"),
    @NamedQuery(
            name = "getAllPlants",
            query = "SELECT a FROM Plant a"),
    @NamedQuery(
            name = "getAllCountryWithOperations",
            query = "SELECT DISTINCT a.country FROM Plant a")
})
@XmlRootElement
public abstract class Plant implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
    protected String name;
    protected String timeZoneID;
    
    // Added after System Analysis & Design Report submission. To be included
    // in class diagram via report addendum
    protected Double longitude;
    protected Double latitude;
    protected String address;

    @ManyToOne
    protected Country country;
    @OneToMany(mappedBy = "plant")
    protected List<Staff> employees;
    @OneToMany(mappedBy = "plant",fetch=FetchType.EAGER)
    protected List<StorageArea> storageAreas;
    @OneToMany(mappedBy = "plant")
    protected List<Announcement> announcementList;
    @OneToMany(mappedBy = "plant")
    protected List<Event> eventList;

    @OneToMany(mappedBy = "requestingPlant")
    protected List<TransferOrder> requestedTransferOrders=new ArrayList<>();

    @OneToMany(mappedBy = "fulfillingPlant")
    protected List<ExternalTransferOrder> transferOrdersToFufill=new ArrayList<>();

    public List<TransferOrder> getRequestedTransferOrders() {
        return requestedTransferOrders;
    }

    public void setRequestedTransferOrders(List<TransferOrder> requestedTransferOrders) {
        this.requestedTransferOrders = requestedTransferOrders;
    }

    public List<ExternalTransferOrder> getTransferOrdersToFufill() {
        return transferOrdersToFufill;
    }

    public void setTransferOrdersToFufill(List<ExternalTransferOrder> transferOrdersToFufill) {
        this.transferOrdersToFufill = transferOrdersToFufill;
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

    public String getTimeZoneID() {
        return timeZoneID;
    }

    public void setTimeZoneID(String timeZoneID) {
        this.timeZoneID = timeZoneID;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public List<Staff> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Staff> employees) {
        this.employees = employees;
    }

    public List<StorageArea> getStorageAreas() {
        return storageAreas;
    }

    public void setStorageAreas(List<StorageArea> storageAreas) {
        this.storageAreas = storageAreas;
    }

    public List<Announcement> getAnnouncementList() {
        return announcementList;
    }

    public void setAnnouncementList(List<Announcement> announcementList) {
        this.announcementList = announcementList;
    }

    public List<Event> getEventList() {
        return eventList;
    }

    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
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
        if (!(object instanceof Plant)) {
            return false;
        }
        Plant other = (Plant) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Plant[ id=" + id + " ]";
    }

}
