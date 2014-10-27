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
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.PhaseId;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import org.apache.commons.io.IOUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

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
        if (photo != null) {
            photo = webBanner.getPicture().getContent();

//            InputStream in = new ByteArrayInputStream(photo);
//            BufferedImage bImageFromConvert = null;
//            try {
//                bImageFromConvert = ImageIO.read(in);
//            } catch (IOException ex) {
//                Logger.getLogger(WebBannerDetailManagedBean.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//            try {
//                ImageIO.write(bImageFromConvert, "jpg", new File(
//                        "../image.jpg"));
//            } catch (IOException ex) {
//                Logger.getLogger(WebBannerDetailManagedBean.class.getName()).log(Level.SEVERE, null, ex);
//            }

        }

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

    public StreamedContent getImage() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
//        if(context == null) {return null;}
        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            // So, we're rendering the HTML. Return a stub StreamedContent so that it will generate right URL.
            return new DefaultStreamedContent();
        } else {
            return new DefaultStreamedContent(new ByteArrayInputStream(webBanner.getPicture().getContent()));
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
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
