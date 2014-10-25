/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.Kitchen;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.EJB.ITManagement.ManageOrganizationalHierarchyBeanLocal;
import IslandFurniture.EJB.Kitchen.ManageIngredientGoodsReceiptLocal;
import IslandFurniture.EJB.Kitchen.ManageIngredientInventoryLocal;
import IslandFurniture.Entities.IngredientGoodsReceiptDocument;
import IslandFurniture.Entities.IngredientGoodsReceiptDocumentDetail;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.Staff;
import IslandFurniture.Entities.Ingredient;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
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
public class IngredientGoodsReceiptPostedManagedBean implements Serializable {

    private Long id;
    private List<IngredientGoodsReceiptDocumentDetail> ingredientGoodsReceiptDetailList;

    private String username;
    private String dateString;
    private Date dateType;
    private DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    private Calendar dateCal;

    private Staff staff;
    private Plant plant;
    private Ingredient ingredient;

    private IngredientGoodsReceiptDocument ingredientGoodsReceipt;

    @EJB
    private ManageUserAccountBeanLocal staffBean;
    @EJB
    public ManageIngredientGoodsReceiptLocal receiptBean;
    @EJB
    public ManageIngredientInventoryLocal ingredientInventoryBean;
    @EJB
    private ManageOrganizationalHierarchyBeanLocal orgBean;

    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        staff = staffBean.getStaff(username);
        plant = staff.getPlant();

        try {
            id = new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
            session.setAttribute("ingredientgoodsreceiptpostedid", id);
        } catch (Exception e) {
            id = (Long) session.getAttribute("ingredientgoodsreceiptpostedid");
        }
        ingredientGoodsReceipt = receiptBean.getIngredientGoodsReceiptDocument(id);
        ingredientGoodsReceiptDetailList = receiptBean.viewIngredientGooodsReceiptDocumentDetail(ingredientGoodsReceipt);

        // start: To display date properly
        if (ingredientGoodsReceipt.getReceiptDate() != null) {
            dateString = df.format(ingredientGoodsReceipt.getReceiptDate().getTime());
        }
        // end: To display date properly
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<IngredientGoodsReceiptDocumentDetail> getIngredientGoodsReceiptDetailList() {
        return ingredientGoodsReceiptDetailList;
    }

    public void setIngredientGoodsReceiptDetailList(List<IngredientGoodsReceiptDocumentDetail> ingredientGoodsReceiptDetailList) {
        this.ingredientGoodsReceiptDetailList = ingredientGoodsReceiptDetailList;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public Date getDateType() {
        return dateType;
    }

    public void setDateType(Date dateType) {
        this.dateType = dateType;
    }

    public DateFormat getDf() {
        return df;
    }

    public void setDf(DateFormat df) {
        this.df = df;
    }

    public Calendar getDateCal() {
        return dateCal;
    }

    public void setDateCal(Calendar dateCal) {
        this.dateCal = dateCal;
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

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public IngredientGoodsReceiptDocument getIngredientGoodsReceipt() {
        return ingredientGoodsReceipt;
    }

    public void setIngredientGoodsReceipt(IngredientGoodsReceiptDocument ingredientGoodsReceipt) {
        this.ingredientGoodsReceipt = ingredientGoodsReceipt;
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

    public ManageIngredientInventoryLocal getIngredientInventoryBean() {
        return ingredientInventoryBean;
    }

    public void setIngredientInventoryBean(ManageIngredientInventoryLocal ingredientInventoryBean) {
        this.ingredientInventoryBean = ingredientInventoryBean;
    }

    public ManageOrganizationalHierarchyBeanLocal getOrgBean() {
        return orgBean;
    }

    public void setOrgBean(ManageOrganizationalHierarchyBeanLocal orgBean) {
        this.orgBean = orgBean;
    }

}
