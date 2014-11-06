/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.Manufacturing;

import IslandFurniture.EJB.Manufacturing.StockManagerLocal;
import IslandFurniture.Entities.FurnitureModel;
import IslandFurniture.Entities.Picture;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.IOUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author a0101774
 */
@ManagedBean
@ViewScoped
public class FurniturePhotoManagedBean implements Serializable {
    
    @EJB
    private StockManagerLocal stockManager;
    
    private Long furnitureID;
    private int spriteCount;
    private FurnitureModel furniture;
    private List<Picture> galleryList;
    private List<Picture> spriteList;
    private Picture[] uploadList = new Picture[36];
    private byte[] photo;
    
    
    @PostConstruct
    public void init() {
        System.out.println("init:FurniturePhotoManagedBean");
        this.furnitureID = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("fID");
        try {
            if (furnitureID == null) {
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect("furniture.xhtml");
            } else {
                System.out.println("FurnitureID is " + furnitureID);
                this.furniture = stockManager.getFurniture(furnitureID);
                galleryList = furniture.getGalleryPictures();
                spriteList = furniture.getPlanningSprites();
                spriteCount = 0;
            } 
        } catch (IOException ex) {

        }
    }
    public void bomActionListener(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("fID", event.getComponent().getAttributes().get("fID"));
        FacesContext.getCurrentInstance().getExternalContext().redirect("bom.xhtml");
    }

    public void handleFileUpload(FileUploadEvent event) throws IOException {
        System.out.println("FurniturePhotoManagedBean.handleFileUpload()");
        UploadedFile file = event.getFile();
        this.photo = IOUtils.toByteArray(file.getInputstream());
    } 
    
    public void handleMultipleFileUpload(FileUploadEvent event) throws IOException {
        System.out.println("FurniturePhotoManagedBean.handleMultipleFileUpload()");
        UploadedFile file = event.getFile();
        this.photo = IOUtils.toByteArray(file.getInputstream());
        spriteCount++;
        Picture picture = new Picture();
        picture.setContent(photo);
        picture.setDescription(getFileName(file.getFileName()));
        
        int index = Integer.parseInt(picture.getDescription()) - 1;
        uploadList[index] = picture;
        
    }
    
    public void deleteGalleryImage(AjaxBehaviorEvent event) {
        System.out.println("FurniturePhotoManagedBean.deleteGalleryImage()");
        Long imageID = (Long) event.getComponent().getAttributes().get("imageID");
        System.out.println("Image ID is " + imageID);
        stockManager.deleteGalleryImage(furnitureID, imageID);
        this.furniture = stockManager.getFurniture(furnitureID);
    }
    
    public void addGalleryImage(ActionEvent event) throws IOException {
        System.out.println("FurniturePhotoManagedBean.addGalleryImage()");
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String title = request.getParameter("addImageForm:imageTitle");
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        if(this.photo == null) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Empty Gallery Image. Remember to select upload to complete image selection", ""));
        } else {
            String msg = stockManager.addGalleryImage(furnitureID, photo, title);
            if (msg != null) {
                FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, ""));
            } else {
                FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Gallery image has been successfully added", ""));
            }
        }
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("fID", furniture.getId());
        ec.redirect("furniturephoto.xhtml");
    }
    public void addSpriteImage() throws IOException {
        System.out.println("FurniturePhotoManagedBean.addSpriteImage()");
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        if(spriteCount != 36) {
            System.out.println("Not enough images. Please upload 36 images");
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please upload 36 images for Photo Sprite", ""));
        } else { 
            stockManager.deleteSpriteImage(furnitureID);
            for(int i=0; i<uploadList.length; i++) 
                stockManager.addSpriteImage(furnitureID, uploadList[i].getContent(), uploadList[i].getDescription());
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Photo Sprite has been successfully added", ""));
        }
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("fID", furniture.getId());
        ec.redirect("furniturephoto.xhtml");
    }
    public String getFileName(String str) {
        int p = str.lastIndexOf(".");
        return str.substring(0, p);
    }

    public Long getFurnitureID() {
        return furnitureID;
    }

    public void setFurnitureID(Long furnitureID) {
        this.furnitureID = furnitureID;
    }

    public FurnitureModel getFurniture() {
        return furniture;
    }

    public void setFurniture(FurnitureModel furniture) {
        this.furniture = furniture;
    }

    public List<Picture> getGalleryList() {
        return galleryList;
    }

    public void setGalleryList(List<Picture> galleryList) {
        this.galleryList = galleryList;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public int getSpriteCount() {
        return spriteCount;
    }

    public void setSpriteCount(int spriteCount) {
        this.spriteCount = spriteCount;
    }

    public List<Picture> getSpriteList() {
        return spriteList;
    }

    public void setSpriteList(List<Picture> spriteList) {
        this.spriteList = spriteList;
    }
}
