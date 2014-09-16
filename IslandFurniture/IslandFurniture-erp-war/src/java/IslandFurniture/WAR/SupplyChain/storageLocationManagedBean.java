/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.SupplyChain;

import IslandFurniture.EJB.Entities.StorageBin;
import IslandFurniture.EJB.SupplyChain.ManageStorageLocationLocal;
import java.io.IOException;
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
    private List<StorageBin> storageLocationList;
    private StorageBin storageLocation;

    @EJB
    public ManageStorageLocationLocal mslr;

    @PostConstruct
    public void init() {
        storageLocationList = mslr.viewStorageLocation();
        System.out.println("Init");
        
        id = (Long)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("locationId");
        
        if(id != null) storageLocation = mslr.getStorageLocation(id);
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
       storageLocation = mslr.getStorageLocation(id);
    }
    
    
     public String editStorageLocation(ActionEvent event) throws IOException {
     StorageBin sl =  (StorageBin)event.getComponent().getAttributes().get("slid");
      
     id = sl.getId();
     plantNumber = sl.getPlantNumber();
     storageAreaNumber = sl.getStorageAreaNumber();
     storageAreaName = sl.getStorageAreaName();
     storageID = sl.getStorageID();
     storageType = sl.getStorageType();
     storageDescription = sl.getStorageDescription();
     
     mslr.editStorageLocation(id, plantNumber, storageAreaNumber, storageAreaName, storageID, storageType, storageDescription);
     FacesContext.getCurrentInstance().getExternalContext().redirect("storagelocation.xhtml");
     return "storagelocation";
     }
     
     public void storagelocationdetailActionListener(ActionEvent event) throws IOException
     {
         FacesContext.getCurrentInstance().getExternalContext().getFlash().put("locationId", event.getComponent().getAttributes().get("locationId"));
         FacesContext.getCurrentInstance().getExternalContext().redirect("storagelocationdetail.xhtml");
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

    public List<StorageBin> getStorageLocationList() {
        return storageLocationList;
    }

    public void setStorageLocationList(List<StorageBin> storageLocationList) {
        this.storageLocationList = storageLocationList;
    }

    public StorageBin getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(StorageBin storageLocation) {
        this.storageLocation = storageLocation;
    }

}
