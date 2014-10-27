/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Islandfurniture.WAR2.Util;

import IslandFurniture.EJB.OperationalCRM.ManageWebBannerLocal;
import IslandFurniture.Entities.Picture;
import java.io.ByteArrayInputStream;
import java.io.IOException;
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
public class PictureManagedBean {
    
    @EJB
    private ManageWebBannerLocal bannerBean;
    
    public StreamedContent getImage() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            return new DefaultStreamedContent();
        } else {
            String pictureId = context.getExternalContext().getRequestParameterMap().get("pictureId");
            System.out.println(pictureId);
            Picture picture = bannerBean.getPicture(Long.valueOf(pictureId)); 
            return new DefaultStreamedContent(new ByteArrayInputStream(picture.getContent()));
        }
    } 
}
