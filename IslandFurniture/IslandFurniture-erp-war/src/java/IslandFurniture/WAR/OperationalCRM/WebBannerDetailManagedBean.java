/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.OperationalCRM;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.EJB.OperationalCRM.ManageWebBannerLocal;
import IslandFurniture.Entities.Picture;
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
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.apache.commons.io.IOUtils;
import org.primefaces.model.StreamedContent;

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
    private byte[] photo;
    private StreamedContent image;

    private WebBanner webBanner;

    @EJB
    private ManageUserAccountBeanLocal staffBean;
    @EJB
    public ManageWebBannerLocal bannerBean;
    @EJB
    private Picture picture;

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
        photo = webBanner.getPicture().getContent();
    }

//  Function: To edit a Web Banner
    public void editWebBanner(ActionEvent event) throws IOException {
        HttpSession session = Util.getSession();
        id = (Long) session.getAttribute("webbannerid");
        Picture picture = new Picture();
        picture.setContent(photo);
        webBanner.setPicture(picture);;
        bannerBean.editWebBanner(webBanner);
        webBanner = bannerBean.getWebBanner(id);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "The Web Banner was successfully edited", ""));
    }

    public void handleFileUpload(FileUploadEvent event) throws IOException {
        UploadedFile file = event.getFile();     
        photo = IOUtils.toByteArray(file.getInputstream());
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
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
