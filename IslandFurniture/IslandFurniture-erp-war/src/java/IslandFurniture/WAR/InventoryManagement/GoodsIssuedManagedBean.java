/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.InventoryManagement;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.Entities.GoodsIssuedDocument;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Staff;
import IslandFurniture.Entities.StockUnit;
import IslandFurniture.EJB.InventoryManagement.ManageGoodsIssuedLocal;
import IslandFurniture.EJB.InventoryManagement.ManageInventoryTransferLocal;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
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
public class GoodsIssuedManagedBean implements Serializable {

    private Long goodsIssuedDocumentId;

    private String username;

    private Calendar postingDate;

    private List<GoodsIssuedDocument> goodsIssuedDocumentList;
    private List<GoodsIssuedDocument> goodsIssuedDocumentPostedList;

    private GoodsIssuedDocument goodsIssuedDocument;
    private Staff staff;
    private Plant plant;

    @EJB
    public ManageGoodsIssuedLocal issuedBean;
    @EJB
    private ManageUserAccountBeanLocal staffBean;
    @EJB
    public ManageInventoryTransferLocal transferBean;

    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        staff = staffBean.getStaff(username);
        plant = staff.getPlant();

//      Load both GID and GID Posted Lists
        goodsIssuedDocumentList = issuedBean.viewGoodsIssuedDocument(plant);
        goodsIssuedDocumentPostedList = issuedBean.viewGoodsIssuedDocumentPosted(plant);

        System.out.println("Init");
    }

//  Function: To create Goods Issued Document    
    public String addGoodsIssuedDocument() {
        goodsIssuedDocument = issuedBean.createGoodsIssuedDocument(plant);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", goodsIssuedDocument.getId());
        return "goodsissueddocument";
    }
    
//  Function: To view Goods Issued Document
    public void viewGoodsIssuedDocument(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", event.getComponent().getAttributes().get("GRDid"));
        goodsIssuedDocumentId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("GRDid");
        FacesContext.getCurrentInstance().getExternalContext().redirect("goodsissueddocument.xhtml");
    }

//  Function: To view Goods Issued Document Posted    
    public void viewGoodsIssuedDocumentPosted(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", event.getComponent().getAttributes().get("GRDid"));
        goodsIssuedDocumentId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("GRDid");
        FacesContext.getCurrentInstance().getExternalContext().redirect("goodsissueddocumentposted.xhtml");
    }

//  Function: To delete Goods Issued Document    
    public String deleteGoodsIssuedDocument(ActionEvent event) {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", event.getComponent().getAttributes().get("GRDid"));
        goodsIssuedDocumentId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("GRDid");
        goodsIssuedDocument = issuedBean.getGoodsIssuedDocument(goodsIssuedDocumentId);

        for (StockUnit g : issuedBean.viewStockUnitPendingMovementAtGoodsIssuedDocument(goodsIssuedDocument)) {
            transferBean.editStockUnitQuantity(g.getCommitStockUnitId(), transferBean.getStockUnit(g.getCommitStockUnitId()).getQty() + g.getQty());
            transferBean.deleteStockUnit(g.getId());
        }

        issuedBean.deleteGoodsIssuedDocument(goodsIssuedDocumentId);
        goodsIssuedDocumentList = issuedBean.viewGoodsIssuedDocument(plant);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "The Goods Issued Document was successfully deleted", ""));
        return "goodsissued";
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
        return issuedBean;
    }

    public void setMgrl(ManageGoodsIssuedLocal mgrl) {
        this.issuedBean = mgrl;
    }

    public ManageUserAccountBeanLocal getStaffBean() {
        return staffBean;
    }

    public void setStaffBean(ManageUserAccountBeanLocal staffBean) {
        this.staffBean = staffBean;
    }

    public ManageInventoryTransferLocal getMsul() {
        return transferBean;
    }

    public void setMsul(ManageInventoryTransferLocal msul) {
        this.transferBean = msul;
    }

}
