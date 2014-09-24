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
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
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
    private String countryOfficeString;
    private CountryOffice countryOffice;
    private String plantType;
    private List<CountryOffice> countryOfficeList;
    private List<Country> countryList;
    private Country country;
    private String countryString;
    private Store store;
    private ManufacturingFacility mf;
    private String[] timezoneList;
    private Integer timezonelistlength;
    private String timezone;
    
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
        timezoneList = TimeZone.getAvailableIDs();
    }
    
    public String addPlant(){
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        plantName = request.getParameter("plantForm:plantName");
        countryOfficeString = request.getParameter("plantForm:countryOffice");
        countryOffice = mohBean.findCountryOfficeByName(countryOfficeString);
        timezone = request.getParameter("plantForm:timezone");
        plantType = request.getParameter("plantForm:plantType");
        switch (plantType) {
            case "Store":
                mohBean.addStore(plantName, timezone, countryOffice);
                storeList = mohBean.displayStore();
                break;
            case "Manufacturing Facility":
                mohBean.addManufacturingFacility(plantName, timezone, countryOffice);
                mfList = mohBean.displayManufacturingFacility();
                break;
            default:
                break;
        }
        
        return "manageplant";
    }
    
    public String editStore(ActionEvent event) throws IOException {
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        store = (Store) event.getComponent().getAttributes().get("toEdit");
        String temp = store.getCountryOffice().getName();
        System.err.println("herehere "+temp);
        countryOffice = mohBean.findCountryOfficeByName(temp);
        System.err.println("herehere "+countryOffice.getName());
        mohBean.editStore(store.getId(), store.getName(), store.getTimeZoneID(), countryOffice);
        storeList = mohBean.displayStore();
        return "manageplant";
    }
    
    public String editMF(ActionEvent event) throws IOException {
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        mf = (ManufacturingFacility) event.getComponent().getAttributes().get("toEdit");
        String temp = mf.getCountryOffice().getName();
        countryOffice = mohBean.findCountryOfficeByName(temp);
        mohBean.editManufacturingFacility(mf.getId(), mf.getName(), mf.getTimeZoneID(), countryOffice);
        mfList = mohBean.displayManufacturingFacility();
        return "manageplant";
    }
    
    public String editCO(ActionEvent event) throws IOException {
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        countryOffice = (CountryOffice) event.getComponent().getAttributes().get("toEdit");
        String temp = countryOffice.getCountry().getName();
        country = mohBean.findCountryByName(temp);
        mohBean.editCountryOffice(countryOffice.getId(), countryOffice.getName(), country, countryOffice.getTimeZoneID());
        coList = mohBean.displayCountryOffice();
        return "manageplant";
    }
    
    public String addCountryOffice(){
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        plantName = request.getParameter("coForm:plantName");
        countryString = request.getParameter("coForm:country");
        country = mohBean.findCountryByName(countryString);
        timezone = request.getParameter("coForm:timezone");
        mohBean.addCountryOffice(plantName, country, timezone);
        coList = mohBean.displayCountryOffice();
        return "manageplant";
    }
    
    public String deleteStore(ActionEvent event) throws IOException {
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        store = (Store) event.getComponent().getAttributes().get("toEdit");
        mohBean.deleteStore(store.getId());
        storeList = mohBean.displayStore();
        return "manageplant";
    }
    
    public String deleteMF(ActionEvent event) throws IOException {
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        mf = (ManufacturingFacility) event.getComponent().getAttributes().get("toEdit");
        mohBean.deleteManufacturingFacility(mf.getId());
        mfList = mohBean.displayManufacturingFacility();
        return "manageplant";
    }
    
    public String deleteCO(ActionEvent event) throws IOException {
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        countryOffice = (CountryOffice) event.getComponent().getAttributes().get("toEdit");
        mohBean.deleteCountryOffice(countryOffice.getId());
        coList = mohBean.displayCountryOffice();
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

    public String getCountryOfficeString() {
        return countryOfficeString;
    }

    public void setCountryOfficeString(String countryOfficeString) {
        this.countryOfficeString = countryOfficeString;
    }

    public CountryOffice getCountryOffice() {
        return countryOffice;
    }

    public void setCountryOffice(CountryOffice countryOffice) {
        this.countryOffice = countryOffice;
    }

    public List<CountryOffice> getCountryOfficeList() {
        return countryOfficeList;
    }

    public void setCountryOfficeList(List<CountryOffice> countryOfficeList) {
        this.countryOfficeList = countryOfficeList;
    }

    public List<Country> getCountryList() {
        return countryList;
    }

    public void setCountryList(List<Country> countryList) {
        this.countryList = countryList;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getCountryString() {
        return countryString;
    }

    public void setCountryString(String countryString) {
        this.countryString = countryString;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public ManufacturingFacility getMf() {
        return mf;
    }

    public void setMf(ManufacturingFacility mf) {
        this.mf = mf;
    }

    public String[] getTimezoneList() {
        return timezoneList;
    }

    public void setTimezoneList(String[] timezoneList) {
        this.timezoneList = timezoneList;
    }

    public Integer getTimezonelistlength() {
        return timezonelistlength;
    }

    public void setTimezonelistlength(Integer timezonelistlength) {
        this.timezonelistlength = timezonelistlength;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
    
    
}
