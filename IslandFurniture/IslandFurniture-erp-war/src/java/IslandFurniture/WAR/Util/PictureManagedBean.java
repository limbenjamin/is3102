/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.Util;

import IslandFurniture.EJB.OperationalCRM.ManageWebBannerLocal;
import IslandFurniture.Entities.WebBanner;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Zee
 */
@ManagedBean
@ApplicationScoped
public class PictureManagedBean implements Serializable {
    
    private WebBanner webBanner;   
    private StreamedContent image;
    
    @EJB
    private ManageWebBannerLocal bannerBean;
    
    public StreamedContent getImage() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
//        if(context == null) {return null;}
        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            // So, we're rendering the HTML. Return a stub StreamedContent so that it will generate right URL.
            return new DefaultStreamedContent();
        } else {
            String pictureId = context.getExternalContext().getRequestParameterMap().get("pictureId");
            //webBanner = bannerBean.getWebBanner(pictureId);
            return new DefaultStreamedContent(new ByteArrayInputStream(webBanner.getPicture().getContent()));
        }
    }    
}
