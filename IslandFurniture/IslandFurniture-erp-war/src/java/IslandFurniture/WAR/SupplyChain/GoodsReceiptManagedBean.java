/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.SupplyChain;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.EJB.Entities.GoodsIssuedDocument;
import IslandFurniture.EJB.Entities.GoodsIssuedDocumentDetail;
import IslandFurniture.EJB.Entities.GoodsReceiptDocument;
import IslandFurniture.EJB.Entities.GoodsReceiptDocumentDetail;
import IslandFurniture.EJB.Entities.Plant;
import IslandFurniture.EJB.Entities.Staff;
import IslandFurniture.EJB.SupplyChain.ManageGoodsReceiptLocal;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import static com.sun.corba.se.spi.presentation.rmi.StubAdapter.request;
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
    public ManageGoodsReceiptLocal mgrl;
    @EJB
    private ManageUserAccountBeanLocal staffBean;

    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        staff = staffBean.getStaff(username);
        plant = staff.getPlant();
        goodsReceiptDocumentList = mgrl.viewGoodsReceiptDocument();
        goodsReceiptDocumentPostedList = mgrl.viewGoodsReceiptDocumentPosted();
        inboundShipmentList = mgrl.viewInboundShipment(plant);
        System.out.println("Init");
    }

    public String addGoodsReceiptDocument() {
//        Calendar cal = Calendar.getInstance();
//        Date date = new Date();
//        cal.setTime(date);
//        postingDate = cal;
        goodsReceiptDocument = mgrl.createGoodsReceiptDocument(plant, null);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", goodsReceiptDocument.getId());
        return "goodsreceiptdocument?faces-redirect=true";

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
        goodsReceiptDocumentList = mgrl.viewGoodsReceiptDocument();
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
        
        goodsReceiptDocumentList = mgrl.viewGoodsReceiptDocument();
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("GRDid", goodsReceiptDocument.getId());
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
