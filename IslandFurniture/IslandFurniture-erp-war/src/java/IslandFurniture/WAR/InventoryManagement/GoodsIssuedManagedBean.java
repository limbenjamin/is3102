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
import IslandFurniture.EJB.InventoryManagement.ManageInventoryMovementLocal;
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

    private Long plantId;
    private Long goodsIssuedDocumentId;

    private String username;
    private String deliverynote;

    private Calendar postingDate;

    private List<GoodsIssuedDocument> goodsIssuedDocumentList;
    private List<GoodsIssuedDocument> goodsIssuedDocumentPostedList;
    private List<StockUnit> stockUnitMainList;

    private boolean ifGoodsIssuedDocumentListEmpty;
    private boolean ifGoodsIssuedDocumentPostedListEmpty;

    private GoodsIssuedDocument goodsIssuedDocument;
    private Staff staff;
    private Plant plant;

    @EJB
    public ManageGoodsIssuedLocal mgrl;
    @EJB
    private ManageUserAccountBeanLocal staffBean;
    @EJB
    public ManageInventoryMovementLocal msul;

    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        staff = staffBean.getStaff(username);
        plant = staff.getPlant();
        goodsIssuedDocumentList = mgrl.viewGoodsIssuedDocument(plant);
        goodsIssuedDocumentPostedList = mgrl.viewGoodsIssuedDocumentPosted(plant);
        ifGoodsIssuedDocumentListEmpty = goodsIssuedDocumentList.isEmpty();
        ifGoodsIssuedDocumentPostedListEmpty = goodsIssuedDocumentPostedList.isEmpty();

        System.out.println("Init");
    }

    public String addGoodsIssuedDocument() {
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
        goodsIssuedDocument = mgrl.getGoodsIssuedDocument(goodsIssuedDocumentId);

        for (StockUnit g : mgrl.viewStockUnitByIdMain(plant, goodsIssuedDocument)) {
            msul.editStockUnitQuantity(g.getCommitStockUnitId(), msul.getStockUnit(g.getCommitStockUnitId()).getQty() + g.getQty());
            msul.deleteStockUnit(g.getId());
        }

        mgrl.deleteGoodsIssuedDocument(goodsIssuedDocumentId);
        goodsIssuedDocumentList = mgrl.viewGoodsIssuedDocument(plant);
        ifGoodsIssuedDocumentListEmpty = goodsIssuedDocumentList.isEmpty();
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "The Goods Issued Document was successfully deleted", ""));
        return "goodsissued";
    }

    public boolean isIfGoodsIssuedDocumentListEmpty() {
        return ifGoodsIssuedDocumentListEmpty;
    }

    public void setIfGoodsIssuedDocumentListEmpty(boolean ifGoodsIssuedDocumentListEmpty) {
        this.ifGoodsIssuedDocumentListEmpty = ifGoodsIssuedDocumentListEmpty;
    }

    public boolean isIfGoodsIssuedDocumentPostedListEmpty() {
        return ifGoodsIssuedDocumentPostedListEmpty;
    }

    public void setIfGoodsIssuedDocumentPostedListEmpty(boolean ifGoodsIssuedDocumentPostedListEmpty) {
        this.ifGoodsIssuedDocumentPostedListEmpty = ifGoodsIssuedDocumentPostedListEmpty;
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

    public List<StockUnit> getStockUnitMainList() {
        return stockUnitMainList;
    }

    public void setStockUnitMainList(List<StockUnit> stockUnitMainList) {
        this.stockUnitMainList = stockUnitMainList;
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

    public ManageInventoryMovementLocal getMsul() {
        return msul;
    }

    public void setMsul(ManageInventoryMovementLocal msul) {
        this.msul = msul;
    }

}