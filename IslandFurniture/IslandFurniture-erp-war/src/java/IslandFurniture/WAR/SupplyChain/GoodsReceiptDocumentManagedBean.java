/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.SupplyChain;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountInformationBean;
import IslandFurniture.EJB.Entities.GoodsReceiptDocument;
import IslandFurniture.EJB.Entities.Plant;
import IslandFurniture.EJB.Entities.Staff;
import IslandFurniture.EJB.SupplyChain.ManageGoodsReceiptLocal;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
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
public class GoodsReceiptDocumentManagedBean implements Serializable {

    private Long plantId;
    private Long goodsReceiptDocumentId;

    private String username;

    private Calendar postingDate;
    private Calendar documentDate;

    private List<GoodsReceiptDocument> goodsReceiptDocumentList;

    private GoodsReceiptDocument goodsReceiptDocument;
    private Staff staff;
    private Plant plant;

    @EJB
    public ManageGoodsReceiptLocal mgrl;
    @EJB
    private ManageUserAccountInformationBean staffBean;

    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        staff = staffBean.getStaff(username);

        goodsReceiptDocumentId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("GRDid");
        if (goodsReceiptDocumentId != null) {
            goodsReceiptDocument = mgrl.getGoodsReceiptDocument(goodsReceiptDocumentId);
        }
        System.out.println("HERE IT GOES! this is the docomentid" + goodsReceiptDocumentId);
        System.out.println("Init");
    }

    public void goodsReceiptDocumentDetailActionListener(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", event.getComponent().getAttributes().get("GRDid"));
        goodsReceiptDocumentId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("GRDid");
        System.out.println("this is the docomentid" + goodsReceiptDocumentId);
        FacesContext.getCurrentInstance().getExternalContext().redirect("goodsreceiptdocument.xhtml");
    }

    public Long getPlantId() {
        return plantId;
    }

    public void setPlantId(Long plantId) {
        this.plantId = plantId;
    }

    public Long getGoodsReceiptDocumentId() {
        return goodsReceiptDocumentId;
    }

    public void setGoodsReceiptDocumentId(Long goodsReceiptDocumentId) {
        this.goodsReceiptDocumentId = goodsReceiptDocumentId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Calendar getPostingDate() {
        return postingDate;
    }

    public void setPostingDate(Calendar postingDate) {
        this.postingDate = postingDate;
    }

    public Calendar getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(Calendar documentDate) {
        this.documentDate = documentDate;
    }

    public List<GoodsReceiptDocument> getGoodsReceiptDocumentList() {
        return goodsReceiptDocumentList;
    }

    public void setGoodsReceiptDocumentList(List<GoodsReceiptDocument> goodsReceiptDocumentList) {
        this.goodsReceiptDocumentList = goodsReceiptDocumentList;
    }

    public GoodsReceiptDocument getGoodsReceiptDocument() {
        return goodsReceiptDocument;
    }

    public void setGoodsReceiptDocument(GoodsReceiptDocument goodsReceiptDocument) {
        this.goodsReceiptDocument = goodsReceiptDocument;
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

    public ManageGoodsReceiptLocal getMgrl() {
        return mgrl;
    }

    public void setMgrl(ManageGoodsReceiptLocal mgrl) {
        this.mgrl = mgrl;
    }

    public ManageUserAccountInformationBean getStaffBean() {
        return staffBean;
    }

    public void setStaffBean(ManageUserAccountInformationBean staffBean) {
        this.staffBean = staffBean;
    }

}