/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.SupplyChain;

import IslandFurniture.EJB.Entities.StorageLocation;
import IslandFurniture.EJB.SupplyChain.ManageStorageLocationLocal;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author KamilulAshraf
 */
@ManagedBean
@ViewScoped
public class storageLocationManagedBean implements Serializable {

    /**
     * Creates a new instance of storageLocationManagedBean
     */
    private Long id;
    private Integer plantNumber;
    private Integer storageAreaNumber;
    private String storageAreaName;
    private String storageID;
    private String storageType;
    private String storageDescription;
    private List<StorageLocation> storageLocationList;
    private StorageLocation storageLocation;

    @EJB
    public ManageStorageLocationLocal mslr;

    @PostConstruct
    public void init() {
        storageLocationList = mslr.viewStorageLocation();
        System.out.println("Init");
    }

    public String addStorageLocation() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        plantNumber = Integer.parseInt(request.getParameter("storageForm:plantNumber"));
        storageAreaNumber = Integer.parseInt(request.getParameter("storageForm:storageAreaNumber"));
        storageAreaName = request.getParameter("storageForm:storageAreaName");
        storageID = request.getParameter("storageForm:storageID");
        storageType = request.getParameter("storageForm:storageType");
        storageDescription = request.getParameter("storageForm:storageDescription");
        mslr.createStorageLocation(plantNumber, storageAreaNumber, storageAreaName, storageID, storageType, storageDescription);
        return "storagelocation";
    }

    public String deleteStorageLocation() {
        id = new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
        mslr.deleteStorageLocation(id);
        return "storagelocation";
    }

    public void loadStorageLocation(ActionEvent event) {
        System.out.println("This is the id: "+ id);
        // id = new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
        //id = Long.parseLong(event.getComponent().getAttributes().get("id").toString());
        id = (Long)event.getComponent().getAttributes().get("id");
        System.out.println("the id is" + id);
       //storageLocation = mslr.getStorageLocation(id);
    }
    
    
     public String editStorageLocation() {
     id = new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
     HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
     plantNumber = Integer.parseInt(request.getParameter("storageForm:plantNumber"));
     storageAreaNumber = Integer.parseInt(request.getParameter("storageForm:storageAreaNumber"));
     storageAreaName = request.getParameter("storageForm:storageAreaName");
     storageID = request.getParameter("storageForm:storageID");
     storageType = request.getParameter("storageForm:storageType");
     storageDescription = request.getParameter("storageForm:storageDescription");

     mslr.editStorageLocation(id, plantNumber, storageAreaNumber, storageAreaName, storageID, storageType, storageDescription);
     return "storagelocation";
     }
     
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPlantNumber() {
        return plantNumber;
    }

    public void setPlantNumber(Integer plantNumber) {
        this.plantNumber = plantNumber;
    }

    public Integer getStorageAreaNumber() {
        return storageAreaNumber;
    }

    public void setStorageAreaNumber(Integer storageAreaNumber) {
        this.storageAreaNumber = storageAreaNumber;
    }

    public String getStorageAreaName() {
        return storageAreaName;
    }

    public void setStorageAreaName(String storageAreaName) {
        this.storageAreaName = storageAreaName;
    }

    public String getStorageID() {
        return storageID;
    }

    public void setStorageID(String storageID) {
        this.storageID = storageID;
    }

    public String getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    public String getStorageDescription() {
        return storageDescription;
    }

    public void setStorageDescription(String storageDescription) {
        this.storageDescription = storageDescription;
    }

    public ManageStorageLocationLocal getMslr() {
        return mslr;
    }

    public void setMslr(ManageStorageLocationLocal mslr) {
        this.mslr = mslr;
    }

    public List<StorageLocation> getStorageLocationList() {
        return storageLocationList;
    }

    public void setStorageLocationList(List<StorageLocation> storageLocationList) {
        this.storageLocationList = storageLocationList;
    }

    public StorageLocation getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(StorageLocation storageLocation) {
        this.storageLocation = storageLocation;
    }

}
