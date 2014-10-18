/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Islandfurniture.WAR2.CustomerWebServices;

import IslandFurniture.EJB.CustomerWebService.ManageCountryBeanLocal;
import IslandFurniture.Entities.CountryOffice;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Zee
 */
@ManagedBean
@ViewScoped
public class CountryManagedBean implements Serializable{
    
    private CountryOffice countryOffice;
    private List<CountryOffice> countryList;
    private Long countryId;
    private Long singaporeId = Long.parseLong("409");
    
    @EJB
    private ManageCountryBeanLocal mcbl;
    
    @PostConstruct
    public void init() {
        countryList = mcbl.getCountries();
        System.out.println("loaded countries");
        try{
            countryId = new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("countryId"));
            countryOffice = mcbl.getCountry(countryId);
        }catch (Exception e){
            countryOffice = mcbl.getCountry(singaporeId);
            System.out.println("country office set to Singapore");
        }        
    }    

    public void goCountry() {
        try{
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            countryId = Long.parseLong(request.getParameter("selectContry:countryId"));
            countryOffice = mcbl.getCountry(countryId);
            System.out.println("Country Set!");
        }catch (Exception e){

        }
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public CountryOffice getCountryOffice() {
        return countryOffice;
    }

    public void setCountryOffice(CountryOffice countryOffice) {
        this.countryOffice = countryOffice;
    }

    public List<CountryOffice> getCountryList() {
        return countryList;
    }

    public void setCountryList(List<CountryOffice> countryList) {
        this.countryList = countryList;
    }

    public ManageCountryBeanLocal getMcbl() {
        return mcbl;
    }

    public void setMcbl(ManageCountryBeanLocal mcbl) {
        this.mcbl = mcbl;
    }
    
}
