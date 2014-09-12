/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.SupplyChain;

import IslandFurniture.EJB.Entities.StorageLocation;
import IslandFurniture.EJB.SupplyChain.ManageStorageLocationLocal;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author KamilulAshraf
 */
@ManagedBean
@ViewScoped
public class storageLocationManagedBean {

    /**
     * Creates a new instance of storageLocationManagedBean
     */
   
    private String rackNumber;
    private List<StorageLocation> storageLocation;
    
    @EJB
    public ManageStorageLocationLocal mslr;
    
    @PostConstruct
    public void init(){
       storageLocation = mslr.viewStorageLocation();
    }
    
    public String addStorageLocation() {
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        rackNumber = request.getParameter("storageForm:rackNumber");
        mslr.createStorageLocation(rackNumber);
        return "storagelocation";
    }

    public String getRackNumber() {
        return rackNumber;
    }

    public void setRackNumber(String rackNumber) {
        this.rackNumber = rackNumber;
    }

    public ManageStorageLocationLocal getMslr() {
        return mslr;
    }

    public void setMslr(ManageStorageLocationLocal mslr) {
        this.mslr = mslr;
    }

    public List<StorageLocation> getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(List<StorageLocation> storageLocation) {
        this.storageLocation = storageLocation;
    }


    
    
    
}
