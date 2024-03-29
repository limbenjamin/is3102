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
    public ManageGoodsReceiptLocal receiptBean;
    @EJB
    private ManageUserAccountBeanLocal staffBean;

    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        staff = staffBean.getStaff(username);
        plant = staff.getPlant();
        goodsReceiptDocumentList = receiptBean.viewGoodsReceiptDocument(plant);
        goodsReceiptDocumentPostedList = receiptBean.viewGoodsReceiptDocumentPosted(plant);
        inboundShipmentList = receiptBean.viewInboundShipment(plant);
    }

//  Function: To create new Goods Receipt Document
    public String addGoodsReceiptDocument() {
        goodsReceiptDocument = receiptBean.createGoodsReceiptDocument(plant, getCalendar());
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", goodsReceiptDocument.getId());
        return "goodsreceiptdocument";
    }

//  Function: To create a new Goods Receipt Document from Goods Issued Document (@Inbound Shipments)    
    public void addGoodsReceiptDocumentfromGoodsIssuedDocument(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GIDid", event.getComponent().getAttributes().get("GIDid"));
        goodsIssuedDocumentId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("GIDid");

        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        cal.setTime(date);
        receiptDate = cal;

        goodsReceiptDocument = receiptBean.createGoodsReceiptDocumentfromGoodsIssuedDocument(plant, receiptDate);
        for (GoodsIssuedDocumentDetail g : receiptBean.viewInboundShipmentByDetail(goodsIssuedDocumentId)) {
            receiptBean.createGoodsReceiptDocumentDetail(goodsReceiptDocument.getId(), g.getStock().getId(), g.getQuantity().intValue());
        }
        goodsReceiptDocumentList = receiptBean.viewGoodsReceiptDocument(plant);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", goodsReceiptDocument.getId());
        FacesContext.getCurrentInstance().getExternalContext().redirect("goodsreceiptdocument.xhtml");
    }

//  Function: To update Incoming Shipment Status as Delivered   
    public String updateIncomingShipmentStatusToDelivered(ActionEvent event) {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GIDid", event.getComponent().getAttributes().get("GIDid"));
        goodsIssuedDocumentId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("GIDid");
        receiptBean.updateIncomingShipmentStatusToDelivered(goodsIssuedDocumentId);
        inboundShipmentList = receiptBean.viewInboundShipment(plant);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "The shipment status has been updated", ""));
        return "goodsreceipt";
    }

//  Function: To view Goods Receipt Document    
    public void viewGoodsReceiptDocument(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", event.getComponent().getAttributes().get("GRDid"));
        goodsReceiptDocumentId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("GRDid");
        FacesContext.getCurrentInstance().getExternalContext().redirect("goodsreceiptdocument.xhtml");
    }

//  Function: To view Goods Receipt Document Posted   
    public void viewGoodsReceiptDocumentPosted(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", event.getComponent().getAttributes().get("GRDid"));
        goodsReceiptDocumentId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("GRDid");
        FacesContext.getCurrentInstance().getExternalContext().redirect("goodsreceiptdocumentposted.xhtml");
    }

//  Function: To delete Goods Receipt Document
    public String deleteGoodsReceiptDocument(ActionEvent event) {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", event.getComponent().getAttributes().get("GRDid"));
        goodsReceiptDocumentId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("GRDid");

        for (GoodsReceiptDocumentDetail g : receiptBean.viewGoodsReceiptDocumentDetail(receiptBean.getGoodsReceiptDocument(goodsReceiptDocumentId))) {
            receiptBean.deleteGoodsReceiptDocumentDetail(g.getId());
        }
        receiptBean.deleteGoodsReceiptDocument(goodsReceiptDocumentId);
        goodsReceiptDocumentList = receiptBean.viewGoodsReceiptDocument(plant);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "The Goods Receipt Document was successfully deleted", ""));

        return "goodsreceipt";
    }

    //  Function: To get current Time in Calendar type
    Calendar getCalendar() {
        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        cal.setTime(date);
        Calendar calDate = cal;
        return calDate;
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

    public GoodsIssuedDocumentDetail getGoodsIssuedDocumentDetail() {
        return goodsIssuedDocumentDetail;
    }

    public void setGoodsIssuedDocumentDetail(GoodsIssuedDocumentDetail goodsIssuedDocumentDetail) {
        this.goodsIssuedDocumentDetail = goodsIssuedDocumentDetail;
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

    public ManageGoodsReceiptLocal getReceiptBean() {
        return receiptBean;
    }

    public void setReceiptBean(ManageGoodsReceiptLocal receiptBean) {
        this.receiptBean = receiptBean;
    }

    public ManageUserAccountBeanLocal getStaffBean() {
        return staffBean;
    }

    public void setStaffBean(ManageUserAccountBeanLocal staffBean) {
        this.staffBean = staffBean;
    }

}
