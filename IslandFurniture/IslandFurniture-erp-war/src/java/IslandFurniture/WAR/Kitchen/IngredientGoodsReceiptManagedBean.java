/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.Kitchen;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.EJB.Kitchen.ManageIngredientGoodsReceiptLocal;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Staff;
import IslandFurniture.Entities.IngredientGoodsReceiptDocument;
import IslandFurniture.Entities.IngredientGoodsReceiptDocumentDetail;
import IslandFurniture.Entities.Store;
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
public class IngredientGoodsReceiptManagedBean implements Serializable {

    private List<IngredientGoodsReceiptDocument> ingredientGoodsReceiptPendingList;
    private List<IngredientGoodsReceiptDocument> ingredientGoodsReceiptPostedList;
    private IngredientGoodsReceiptDocument ingredientGoodsReceipt;

    private String username;
    private Staff staff;
    private Plant plant;

    @EJB
    private ManageUserAccountBeanLocal staffBean;
    @EJB
    public ManageIngredientGoodsReceiptLocal receiptBean;

    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        staff = staffBean.getStaff(username);
        plant = staff.getPlant();
        ingredientGoodsReceiptPendingList = receiptBean.viewIngredientGooodsReceiptDocument((Store) plant, false);
        ingredientGoodsReceiptPostedList = receiptBean.viewIngredientGooodsReceiptDocument((Store) plant, true);
    }

//  Function: To create a Ingredient Goods Receipt
    public void createIngredientGoodsReceipt(ActionEvent event) throws IOException {
        ingredientGoodsReceipt = receiptBean.createIngredientGoodsReceiptDocument(staff, getCalendar(), (Store) plant);
        FacesContext.getCurrentInstance().getExternalContext().redirect("ingredientgoodsreceiptdocument.xhtml?id=" + ingredientGoodsReceipt.getId().toString());
    }

//  Function: To delete a Ingredient Goods Receipt  
    public void deleteIngredientGoodsReceipt(ActionEvent event) throws IOException {
        IngredientGoodsReceiptDocument grd = (IngredientGoodsReceiptDocument) event.getComponent().getAttributes().get("grd");
        for (IngredientGoodsReceiptDocumentDetail g : receiptBean.viewIngredientGooodsReceiptDocumentDetail(grd)) {
            receiptBean.deleteIngredientGoodsReceiptDocumentDetail(g);
        }
        receiptBean.deleteIngredientGoodsReceiptDocument(grd);
        ingredientGoodsReceiptPendingList = receiptBean.viewIngredientGooodsReceiptDocument((Store) plant, false);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "The Ingredient Goods Receipt was successfully deleted", ""));
    }

//  Function: To get current Time in Calendar type
    Calendar getCalendar() {
        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        cal.setTime(date);
        Calendar calDate = cal;
        return calDate;
    }

    public List<IngredientGoodsReceiptDocument> getIngredientGoodsReceiptPendingList() {
        return ingredientGoodsReceiptPendingList;
    }

    public void setIngredientGoodsReceiptPendingList(List<IngredientGoodsReceiptDocument> ingredientGoodsReceiptPendingList) {
        this.ingredientGoodsReceiptPendingList = ingredientGoodsReceiptPendingList;
    }

    public List<IngredientGoodsReceiptDocument> getIngredientGoodsReceiptPostedList() {
        return ingredientGoodsReceiptPostedList;
    }

    public void setIngredientGoodsReceiptPostedList(List<IngredientGoodsReceiptDocument> ingredientGoodsReceiptPostedList) {
        this.ingredientGoodsReceiptPostedList = ingredientGoodsReceiptPostedList;
    }

    public IngredientGoodsReceiptDocument getIngredientGoodsReceipt() {
        return ingredientGoodsReceipt;
    }

    public void setIngredientGoodsReceipt(IngredientGoodsReceiptDocument ingredientGoodsReceipt) {
        this.ingredientGoodsReceipt = ingredientGoodsReceipt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public ManageUserAccountBeanLocal getStaffBean() {
        return staffBean;
    }

    public void setStaffBean(ManageUserAccountBeanLocal staffBean) {
        this.staffBean = staffBean;
    }

    public ManageIngredientGoodsReceiptLocal getReceiptBean() {
        return receiptBean;
    }

    public void setReceiptBean(ManageIngredientGoodsReceiptLocal receiptBean) {
        this.receiptBean = receiptBean;
    }

}
