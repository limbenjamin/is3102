/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.InventoryManagement;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.Entities.GoodsIssuedDocument;
import IslandFurniture.Entities.GoodsIssuedDocumentDetail;
import IslandFurniture.Entities.GoodsReceiptDocument;
import IslandFurniture.Entities.GoodsReceiptDocumentDetail;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Staff;
import IslandFurniture.EJB.InventoryManagement.ManageGoodsReceiptLocal;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
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
public class GoodsReceiptManagedBean implements Serializable {

    private Long plantId;
    private Long goodsReceiptDocumentId;
    private Long goodsIssuedDocumentId;

    private boolean ifGoodsReceiptDocumentListEmpty;
    private boolean ifgoodsReceiptDocumentPostedListEmpty;
    private boolean ifInboundShipmentListEmpty;
    private boolean ifShipmentReceived;

    private String username;
    private String deliverynote;

    private Calendar postingDate;
    private Calendar receiptDate;

    private List<GoodsReceiptDocument> goodsReceiptDocumentList;
    private List<GoodsReceiptDocument> goodsReceiptDocumentPostedList;
    private List<GoodsIssuedDocument> inboundShipmentList;

    private GoodsReceiptDocument goodsReceiptDocument;
    private GoodsIssuedDocumentDetail goodsIssuedDocumentDetail;
    private Staff staff;
    private Plant plant;

    @EJB
    public ManageGoodsReceiptLocal mgrl;
    @EJB
    private ManageUserAccountBeanLocal staffBean;

    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        staff = staffBean.getStaff(username);
        plant = staff.getPlant();
        goodsReceiptDocumentList = mgrl.viewGoodsReceiptDocument(plant);
        ifGoodsReceiptDocumentListEmpty = goodsReceiptDocumentList.isEmpty();
        goodsReceiptDocumentPostedList = mgrl.viewGoodsReceiptDocumentPosted(plant);
        ifgoodsReceiptDocumentPostedListEmpty = goodsReceiptDocumentPostedList.isEmpty();
        inboundShipmentList = mgrl.viewInboundShipment(plant);
        ifInboundShipmentListEmpty = inboundShipmentList.isEmpty();
        System.out.println("Init");
    }

    public String addGoodsReceiptDocument() {
        goodsReceiptDocument = mgrl.createGoodsReceiptDocument(plant, null);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", goodsReceiptDocument.getId());
        return "goodsreceiptdocument?faces-redirect=true";
    }

    public String updateIncomingShipmentStatus(ActionEvent event) {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GIDid", event.getComponent().getAttributes().get("GIDid"));
        goodsIssuedDocumentId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("GIDid");
        
        System.out.println("The goodsIssuedDocumentId is null, it is: " + goodsIssuedDocumentId);
        
        mgrl.updateIncomingShipmentStatus(goodsIssuedDocumentId);
        inboundShipmentList = mgrl.viewInboundShipment(plant);
        ifInboundShipmentListEmpty = inboundShipmentList.isEmpty();
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "The shipment status has been updated", ""));
        
        return "goodsreceipt?faces-redirect=true";
    }

    public void goodsReceiptDocumentDetailActionListener(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", event.getComponent().getAttributes().get("GRDid"));
        goodsReceiptDocumentId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("GRDid");
        FacesContext.getCurrentInstance().getExternalContext().redirect("goodsreceiptdocument.xhtml");
    }

    public void goodsReceiptDocumentDetailActionListener2(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", event.getComponent().getAttributes().get("GRDid"));
        goodsReceiptDocumentId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("GRDid");
        FacesContext.getCurrentInstance().getExternalContext().redirect("goodsreceiptdocumentposted.xhtml");
    }

    public String deleteGoodsReceiptDocument(ActionEvent event) {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", event.getComponent().getAttributes().get("GRDid"));
        goodsReceiptDocumentId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("GRDid");
        goodsReceiptDocument = mgrl.getGoodsReceiptDocument(goodsReceiptDocumentId);

        for (GoodsReceiptDocumentDetail g : mgrl.viewGoodsReceiptDocumentDetail(goodsReceiptDocument)) {
            mgrl.deleteGoodsReceiptDocumentDetail(g.getId());
        }

        mgrl.deleteGoodsReceiptDocument(goodsReceiptDocumentId);
        goodsReceiptDocumentList = mgrl.viewGoodsReceiptDocument(plant);
        ifGoodsReceiptDocumentListEmpty = goodsReceiptDocumentList.isEmpty();
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "The Goods Receipt Document was successfully deleted", ""));
        return "goodsreceiptdocument";
    }

    public void addGoodsReceiptDocumentfromInbound(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GIDid", event.getComponent().getAttributes().get("GIDid"));
        goodsIssuedDocumentId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("GIDid");

        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        cal.setTime(date);
        receiptDate = cal;

        goodsReceiptDocument = mgrl.createGoodsReceiptDocumentfromInbound(plant, receiptDate);

        for (GoodsIssuedDocumentDetail g : mgrl.viewInboundShipmentByDetail(goodsIssuedDocumentId)) {
            mgrl.createGoodsReceiptDocumentDetail(goodsReceiptDocument.getId(), g.getStock().getId(), g.getQuantity().intValue());
        }

        goodsReceiptDocumentList = mgrl.viewGoodsReceiptDocument(plant);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", goodsReceiptDocument.getId());
        FacesContext.getCurrentInstance().getExternalContext().redirect("goodsreceiptdocument.xhtml");
    }

    public boolean isIfShipmentReceived() {
        return ifShipmentReceived;
    }

    public void setIfShipmentReceived(boolean ifShipmentReceived) {
        this.ifShipmentReceived = ifShipmentReceived;
    }
    
    public Long getGoodsIssuedDocumentId() {
        return goodsIssuedDocumentId;
    }

    public void setGoodsIssuedDocumentId(Long goodsIssuedDocumentId) {
        this.goodsIssuedDocumentId = goodsIssuedDocumentId;
    }

    public boolean isIfGoodsReceiptDocumentListEmpty() {
        return ifGoodsReceiptDocumentListEmpty;
    }

    public void setIfGoodsReceiptDocumentListEmpty(boolean ifGoodsReceiptDocumentListEmpty) {
        this.ifGoodsReceiptDocumentListEmpty = ifGoodsReceiptDocumentListEmpty;
    }

    public boolean isIfgoodsReceiptDocumentPostedListEmpty() {
        return ifgoodsReceiptDocumentPostedListEmpty;
    }

    public void setIfgoodsReceiptDocumentPostedListEmpty(boolean ifgoodsReceiptDocumentPostedListEmpty) {
        this.ifgoodsReceiptDocumentPostedListEmpty = ifgoodsReceiptDocumentPostedListEmpty;
    }

    public boolean isIfInboundShipmentListEmpty() {
        return ifInboundShipmentListEmpty;
    }

    public void setIfInboundShipmentListEmpty(boolean ifInboundShipmentListEmpty) {
        this.ifInboundShipmentListEmpty = ifInboundShipmentListEmpty;
    }

    public GoodsIssuedDocumentDetail getGoodsIssuedDocumentDetail() {
        return goodsIssuedDocumentDetail;
    }

    public void setGoodsIssuedDocumentDetail(GoodsIssuedDocumentDetail goodsIssuedDocumentDetail) {
        this.goodsIssuedDocumentDetail = goodsIssuedDocumentDetail;
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

    public Calendar getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(Calendar receiptDate) {
        this.receiptDate = receiptDate;
    }

    public List<GoodsReceiptDocument> getGoodsReceiptDocumentList() {
        return goodsReceiptDocumentList;
    }

    public void setGoodsReceiptDocumentList(List<GoodsReceiptDocument> goodsReceiptDocumentList) {
        this.goodsReceiptDocumentList = goodsReceiptDocumentList;
    }

    public List<GoodsReceiptDocument> getGoodsReceiptDocumentPostedList() {
        return goodsReceiptDocumentPostedList;
    }

    public void setGoodsReceiptDocumentPostedList(List<GoodsReceiptDocument> goodsReceiptDocumentPostedList) {
        this.goodsReceiptDocumentPostedList = goodsReceiptDocumentPostedList;
    }

    public List<GoodsIssuedDocument> getInboundShipmentList() {
        return inboundShipmentList;
    }

    public void setInboundShipmentList(List<GoodsIssuedDocument> inboundShipmentList) {
        this.inboundShipmentList = inboundShipmentList;
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

    public ManageUserAccountBeanLocal getStaffBean() {
        return staffBean;
    }

    public void setStaffBean(ManageUserAccountBeanLocal staffBean) {
        this.staffBean = staffBean;
    }

}
