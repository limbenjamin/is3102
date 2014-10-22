/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.OperationalCRM;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Staff;
import IslandFurniture.EJB.OperationalCRM.ManageWebBannerLocal;
import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.WebBanner;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;

/**
 *
 * @author KamilulAshraf
 */
@ManagedBean
@ViewScoped
public class WebBannerManagedBean implements Serializable {

    private List<WebBanner> webBannerList;
    private WebBanner webBanner;

    private String username;
    private Staff staff;
    private Plant plant;

    @EJB
    private ManageUserAccountBeanLocal staffBean;
    @EJB
    public ManageWebBannerLocal bannerBean;

    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        staff = staffBean.getStaff(username);
        plant = staff.getPlant();
        webBannerList = bannerBean.viewWebBanner(plant);
    }

//  Function: To create a Web Banner
    public void createWebBanner(ActionEvent event) throws IOException {
        webBanner = bannerBean.createWebBanner(plant);
        FacesContext.getCurrentInstance().getExternalContext().redirect("webbannerdetail.xhtml?id=" + webBanner.getId().toString());
    }

//  Function: To delete a Web Banner  
    public void deleteWebBanner(ActionEvent event) throws IOException {
        webBanner = (WebBanner) event.getComponent().getAttributes().get("wb");
        bannerBean.deleteWebBanner(webBanner);
        webBannerList = bannerBean.viewWebBanner((CountryOffice) plant);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "The Web Banner was successfully deleted", ""));
    }

    public List<WebBanner> getWebBannerList() {
        return webBannerList;
    }

    public void setWebBannerList(List<WebBanner> webBannerList) {
        this.webBannerList = webBannerList;
    }

    public WebBanner getWebBanner() {
        return webBanner;
    }

    public void setWebBanner(WebBanner webBanner) {
        this.webBanner = webBanner;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    public ManageUserAccountBeanLocal getStaffBean() {
        return staffBean;
    }

    public void setStaffBean(ManageUserAccountBeanLocal staffBean) {
        this.staffBean = staffBean;
    }

    public ManageWebBannerLocal getBannerBean() {
        return bannerBean;
    }

    public void setBannerBean(ManageWebBannerLocal bannerBean) {
        this.bannerBean = bannerBean;
    }
    
}
