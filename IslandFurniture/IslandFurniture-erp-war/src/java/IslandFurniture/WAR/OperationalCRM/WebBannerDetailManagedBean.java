/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.OperationalCRM;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.EJB.OperationalCRM.ManageWebBannerLocal;
import IslandFurniture.Entities.WebBanner;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.io.IOException;
import java.io.Serializable;
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
public class WebBannerDetailManagedBean implements Serializable {

    private Long id;
    private String headerText;
    private String subHeaderText;
    private String bodyText;
    private String buttonText;
    private String buttonURL;
        
    private WebBanner webBanner;

    @EJB
    private ManageUserAccountBeanLocal staffBean;
    @EJB
    public ManageWebBannerLocal bannerBean;

    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        try {
            id = new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
            session.setAttribute("webbannerid", id);
        } catch (Exception e) {
            id = (Long) session.getAttribute("webbannerid");
        }

        webBanner = bannerBean.getWebBanner(id);
    }

//  Function: To edit a Web Banner
    public void editWebBanner(ActionEvent event) throws IOException {
//        HttpSession session = Util.getSession();
//        id = (Long) session.getAttribute("webbannerid");
//        WebBanner wb = (WebBanner) event.getComponent().getAttributes().get("wb");
//        wb.setHeaderText(event.getComponent().getAttributes().get("headerText");
//        wb.setSubheaderText(subHeaderText);
//        wb.setBodyText(bodyText);
//        wb.setButtonText(buttonText);
//        wb.setButtonUrl(buttonURL);
//        wb.setPicture(null);
//       
//        System.out.println("The header text is " + wb.getHeaderText());
//        
//        bannerBean.editWebBanner(wb);
//        webBanner = bannerBean.getWebBanner(id);
//        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
//                new FacesMessage(FacesMessage.SEVERITY_INFO, "The Web Banner was successfully edited", ""));
    }

    public String getHeaderText() {
        return headerText;
    }

    public void setHeaderText(String headerText) {
        this.headerText = headerText;
    }

    public String getSubHeaderText() {
        return subHeaderText;
    }

    public void setSubHeaderText(String subHeaderText) {
        this.subHeaderText = subHeaderText;
    }

    public String getBodyText() {
        return bodyText;
    }

    public void setBodyText(String bodyText) {
        this.bodyText = bodyText;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public String getButtonURL() {
        return buttonURL;
    }

    public void setButtonURL(String buttonURL) {
        this.buttonURL = buttonURL;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WebBanner getWebBanner() {
        return webBanner;
    }

    public void setWebBanner(WebBanner webBanner) {
        this.webBanner = webBanner;
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
