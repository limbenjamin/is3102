/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.ITManagement;

import IslandFurniture.EJB.Entities.Country;
import IslandFurniture.EJB.Entities.CountryOffice;
import IslandFurniture.EJB.Entities.ManufacturingFacility;
import IslandFurniture.EJB.Entities.Store;
import IslandFurniture.EJB.ITManagement.ManageOrganizationalHierarchyBeanLocal;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Benjamin
 */
@ManagedBean
@ViewScoped
public class OrganizationalHierarchyManagedBean implements Serializable  {

    private Long id = null;
    private String username = null;
    private List<Store> storeList;
    private List<ManufacturingFacility> mfList;
    private List<CountryOffice> coList;
    private String plantName; 
    private String countryString;
    private Country country;
    private String plantType;
    private List<Country> countryList;
    
    @EJB
    private ManageOrganizationalHierarchyBeanLocal mohBean;
    
    @PostConstruct
    public void init(){
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        storeList = mohBean.displayStore();
        mfList = mohBean.displayManufacturingFacility();
        coList = mohBean.displayCountryOffice();
        countryList = mohBean.getCountries();
    }
    
    public String addPlant(){
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        plantName = request.getParameter("plantForm:plantName");
        countryString = request.getParameter("plantForm:country");
        country = mohBean.findCountryByName(countryString);
        plantType = request.getParameter("plantForm:plantType");
        switch (plantType) {
            case "Store":
                mohBean.addStore(plantName, country, "Asia/Singapore");
                break;
            case "Manufacturing Facility":
                mohBean.addManufacturingFacility(plantName, country, "Asia/Singapore");
                break;
            case "Country Office":
                mohBean.addCountryOffice(plantName, country, "Asia/Singapore");
                break;
            default:
                break;
        }
        
        return "manageplant";
    }
    
    public String deletePlant(){
        id = new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
        plantType = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("plantType");
        switch (plantType) {
            case "Store":
                mohBean.deleteStore(id);
                break;
            case "Manufacturing Facility":
                mohBean.deleteManufacturingFacility(id);
                break;
            case "Country Office":
                mohBean.deleteCountryOffice(id);
                break;
            default:
                break;
        }
        
        return "manageplant";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Store> getStoreList() {
        return storeList;
    }

    public void setStoreList(List<Store> storeList) {
        this.storeList = storeList;
    }

    public List<ManufacturingFacility> getMfList() {
        return mfList;
    }

    public void setMfList(List<ManufacturingFacility> mfList) {
        this.mfList = mfList;
    }

    public List<CountryOffice> getCoList() {
        return coList;
    }

    public void setCoList(List<CountryOffice> coList) {
        this.coList = coList;
    }

    public ManageOrganizationalHierarchyBeanLocal getMohBean() {
        return mohBean;
    }

    public void setMohBean(ManageOrganizationalHierarchyBeanLocal mohBean) {
        this.mohBean = mohBean;
    }

    public String getPlantType() {
        return plantType;
    }

    public void setPlantType(String plantType) {
        this.plantType = plantType;
    }

    public String getPlantName() {
        return plantName;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public String getCountryString() {
        return countryString;
    }

    public void setCountryString(String countryString) {
        this.countryString = countryString;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public List<Country> getCountryList() {
        return countryList;
    }

    public void setCountryList(List<Country> countryList) {
        this.countryList = countryList;
    }
    
    
    
}
