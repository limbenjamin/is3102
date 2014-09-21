/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.SupplyChain;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.EJB.Entities.GoodsIssuedDocument;
import IslandFurniture.EJB.Entities.Plant;
import IslandFurniture.EJB.Entities.Staff;
import IslandFurniture.EJB.Entities.StockUnit;
import IslandFurniture.EJB.SupplyChain.ManageGoodsIssuedLocal;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
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
public class GoodsIssuedManagedBean implements Serializable {

    private Long plantId;
    private Long goodsIssuedDocumentId;

    private String username;
    private String deliverynote;

    private Calendar postingDate;

    private List<GoodsIssuedDocument> goodsIssuedDocumentList;
    private List<GoodsIssuedDocument> goodsIssuedDocumentPostedList;
    private List<StockUnit> stockUnitMainList;

    private GoodsIssuedDocument goodsIssuedDocument;
    private Staff staff;
    private Plant plant;

    @EJB
    public ManageGoodsIssuedLocal mgrl;
    @EJB
    private ManageUserAccountBeanLocal staffBean;

    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        staff = staffBean.getStaff(username);
        plant = staff.getPlant();
        goodsIssuedDocumentList = mgrl.viewGoodsIssuedDocument();
        goodsIssuedDocumentPostedList = mgrl.viewGoodsIssuedDocumentPosted();
        System.out.println("Init");
    }

    public String addGoodsIssuedDocument() {
//        Calendar cal = Calendar.getInstance();
//        Date date = new Date();
//        cal.setTime(date);
//        postingDate = cal;
        goodsIssuedDocument = mgrl.createGoodsIssuedDocument(plant, null);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", goodsIssuedDocument.getId());
        return "goodsissueddocument?faces-redirect=true";

    }

    public void goodsIssuedDocumentDetailActionListener(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", event.getComponent().getAttributes().get("GRDid"));
        goodsIssuedDocumentId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("GRDid");
        FacesContext.getCurrentInstance().getExternalContext().redirect("goodsissueddocument.xhtml");
    }

    public void goodsIssuedDocumentDetailActionListener2(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", event.getComponent().getAttributes().get("GRDid"));
        goodsIssuedDocumentId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("GRDid");
        FacesContext.getCurrentInstance().getExternalContext().redirect("goodsissueddocumentposted.xhtml");
    }

    public String deleteGoodsIssuedDocument(ActionEvent event) {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", event.getComponent().getAttributes().get("GRDid"));
        goodsIssuedDocumentId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("GRDid");
        goodsIssuedDocument = mgrl.getGoodsIssuedDocument(plantId);

        for (StockUnit g : mgrl.viewStockUnitByIdMain(plant, goodsIssuedDocument)) {
//            mgrl.createGoodsIssuedDocumentDetail(goodsIssuedDocumentId, g.getStock().getId(), g.getQty());
//            mgrl.editGoodsIssuedDocument2(goodsIssuedDocumentId, postingDate);
//            msul.deleteStockUnit(g.getId());
//
//            msul.editStockUnitQuantity(stockUnit.getCommitStockUnitId(), msul.getStockUnit(stockUnit.getCommitStockUnitId()).getQty() + stockUnit.getQty());
//            msul.deleteStockUnit(stockUnit.getId());

        }

        mgrl.deleteGoodsIssuedDocument(goodsIssuedDocumentId);
        goodsIssuedDocumentList = mgrl.viewGoodsIssuedDocument();
        return "goodsreceiptdocument";
    }

    public Long getPlantId() {
        return plantId;
    }

    public void setPlantId(Long plantId) {
        this.plantId = plantId;
    }

    public Long getGoodsIssuedDocumentId() {
        return goodsIssuedDocumentId;
    }

    public void setGoodsIssuedDocumentId(Long goodsIssuedDocumentId) {
        this.goodsIssuedDocumentId = goodsIssuedDocumentId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDeliverynote() {
        return deliverynote;
    }

    public void setDeliverynote(String deliverynote) {
        this.deliverynote = deliverynote;
    }

    public Calendar getPostingDate() {
        return postingDate;
    }

    public void setPostingDate(Calendar postingDate) {
        this.postingDate = postingDate;
    }

    public List<GoodsIssuedDocument> getGoodsIssuedDocumentList() {
        return goodsIssuedDocumentList;
    }

    public void setGoodsIssuedDocumentList(List<GoodsIssuedDocument> goodsIssuedDocumentList) {
        this.goodsIssuedDocumentList = goodsIssuedDocumentList;
    }

    public List<GoodsIssuedDocument> getGoodsIssuedDocumentPostedList() {
        return goodsIssuedDocumentPostedList;
    }

    public void setGoodsIssuedDocumentPostedList(List<GoodsIssuedDocument> goodsIssuedDocumentPostedList) {
        this.goodsIssuedDocumentPostedList = goodsIssuedDocumentPostedList;
    }

    public GoodsIssuedDocument getGoodsIssuedDocument() {
        return goodsIssuedDocument;
    }

    public void setGoodsIssuedDocument(GoodsIssuedDocument goodsIssuedDocument) {
        this.goodsIssuedDocument = goodsIssuedDocument;
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

    public ManageGoodsIssuedLocal getMgrl() {
        return mgrl;
    }

    public void setMgrl(ManageGoodsIssuedLocal mgrl) {
        this.mgrl = mgrl;
    }

    public ManageUserAccountBeanLocal getStaffBean() {
        return staffBean;
    }

    public void setStaffBean(ManageUserAccountBeanLocal staffBean) {
        this.staffBean = staffBean;
    }

}
