
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.SupplyChain;

import IslandFurniture.EJB.Entities.GoodsReceiptDocument;
import IslandFurniture.EJB.SupplyChain.ManageGoodsReceiptLocal;
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
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author KamilulAshraf
 */
@ManagedBean
@ViewScoped
public class GoodsReceiptManagedBean implements Serializable {

    private Long id;
    private Calendar postingDate;
    private Calendar documentDate;

    private List<GoodsReceiptDocument> goodsReceiptDocumentList;
    private GoodsReceiptDocument goodsReceiptDocument;

    @EJB
    public ManageGoodsReceiptLocal mgrl;

    @PostConstruct
    public void init() {
        goodsReceiptDocumentList = mgrl.viewGoodsReceiptDocument();
        System.out.println("Init");
        id = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("locationId");
        if (id != null) {
            goodsReceiptDocument = mgrl.getGoodsReceiptDocument(id);
        }
    }

    public String addGoodsReceiptDocument() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
//        postingDate = 
//        documentDate = 

//        plantNumber = Integer.parseInt(request.getParameter("storageForm:plantNumber"));
//        storageAreaNumber = Integer.parseInt(request.getParameter("storageForm:storageAreaNumber"));
//        storageAreaName = request.getParameter("storageForm:storageAreaName");
//        storageID = request.getParameter("storageForm:storageID");
//        storageType = request.getParameter("storageForm:storageType");
//        storageDescription = request.getParameter("storageForm:storageDescription");
//        mgrl.createGoodsReceiptDocument(postingDate, documentDate);
        return "goodsreceipt";
    }

    public String deleteGoodsReceiptDocument() {
        id = new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
        mgrl.deleteGoodsReceiptDocument(id);
        return "goodsreceipt";
    }

    public void loadStorageLocation(ActionEvent event) {
        id = (Long) event.getComponent().getAttributes().get("id");
        goodsReceiptDocument = mgrl.getGoodsReceiptDocument(id);
    }

    public String editStorageLocation(ActionEvent event) throws IOException {
        GoodsReceiptDocument gr = (GoodsReceiptDocument) event.getComponent().getAttributes().get("slid");

        id = gr.getId();
        postingDate = gr.getPostingDate();
        documentDate = gr.getDocumentDate();

        mgrl.editGoodsReceiptDocument(id, postingDate, documentDate);
        FacesContext.getCurrentInstance().getExternalContext().redirect("goodsreceipt.xhtml");
        return "storagelocation";
    }

    public void storagelocationdetailActionListener(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("locationId", event.getComponent().getAttributes().get("locationId"));
        FacesContext.getCurrentInstance().getExternalContext().redirect("goodsreceiptdocument.xhtml");
    }
    
}

