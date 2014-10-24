/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Islandfurniture.WAR2.CustomerWebServices;

import IslandFurniture.EJB.CustomerWebService.ManageLocalizationBeanLocal;
import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.WebBanner;
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
public class HomeManagedBean {
    
    private CountryOffice co;
    private List<WebBanner> webBanners;

    @EJB
    private ManageLocalizationBeanLocal manageLocalizationBean;
    
    @PostConstruct
    public void init() {
        HttpServletRequest httpReq = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        co = manageLocalizationBean.findCoByCode((String) httpReq.getAttribute("coCode"));
        
        webBanners = co.getWebBanners();
        System.out.println("loaded " + co.getName() + " web banners");
    }    

    public CountryOffice getCo() {
        return co;
    }

    public void setCo(CountryOffice co) {
        this.co = co;
    }

    public List<WebBanner> getWebBanners() {
        return webBanners;
    }

    public void setWebBanners(List<WebBanner> webBanners) {
        this.webBanners = webBanners;
    }
}
