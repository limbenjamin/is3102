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
public class IngredientGoodsReceiptDocumentManagedBean implements Serializable {

    private Long ingredientId;
    private Integer quantity;
    private Long id;

    private List<Ingredient> ingredientList;
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
        ingredientList = ingredientInventoryBean.viewIngredient(plant);
        try {
            id = new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
            session.setAttribute("ingredientgoodsreceiptid", id);
        } catch (Exception e) {
            id = (Long) session.getAttribute("ingredientgoodsreceiptid");
        }
        ingredientGoodsReceipt = receiptBean.getIngredientGoodsReceiptDocument(id);
        ingredientGoodsReceiptDetailList = receiptBean.viewIngredientGooodsReceiptDocumentDetail(ingredientGoodsReceipt);

        // start: To display date properly
        if (ingredientGoodsReceipt.getReceiptDate() != null) {
            dateString = df.format(ingredientGoodsReceipt.getReceiptDate().getTime());
        }
        // end: To display date properly
    }

//  Function: To get current Time in Calendar type
    Calendar getCalendar() {
        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        cal.setTime(date);
        Calendar calDate = cal;
        return calDate;
    }

//  Function: To create a Ingredient Goods Receipt Detail (Request)
    public void addIngredientGoodsReceiptDocumentDetail(ActionEvent event) {
        HttpSession session = Util.getSession();
        id = (Long) session.getAttribute("ingredientgoodsreceiptid");
        ingredient = ingredientInventoryBean.getIngredient(ingredientId);
        receiptBean.createIngredientGoodsReceiptDocumentDetail(staff, getCalendar(), ingredientGoodsReceipt, ingredient, quantity);
        ingredientGoodsReceiptDetailList = receiptBean.viewIngredientGooodsReceiptDocumentDetail(ingredientGoodsReceipt);
    }

//  Function: To edit a Ingredient Goods Receipt (Request)    
    public void editIngredientGoodsReceiptDocument(ActionEvent event) throws ParseException {
        HttpSession session = Util.getSession();
        id = (Long) session.getAttribute("ingredientgoodsreceiptid");
        dateType = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        dateCal = Calendar.getInstance();
        Date date = dateType;
        dateCal.setTime(date);
        receiptBean.editIngredientGoodsReceiptDocument(staff, dateCal, ingredientGoodsReceipt);
    }

//  Function: To edit a Ingredient Goods Receipt Detail (Request)    
    public void editIngredientGoodsReceiptDocumentDetail(ActionEvent event) throws IOException {
        HttpSession session = Util.getSession();
        id = (Long) session.getAttribute("ingredientgoodsreceiptid");
        IngredientGoodsReceiptDocumentDetail grdd = (IngredientGoodsReceiptDocumentDetail) event.getComponent().getAttributes().get("grdd");
        receiptBean.editIngredientGoodsReceiptDocumentDetail(staff, getCalendar(), grdd);
        ingredientGoodsReceiptDetailList = receiptBean.viewIngredientGooodsReceiptDocumentDetail(ingredientGoodsReceipt);
    }

//  Function: To delete a Ingredient Goods Receipt Detail (Request) 
    public void deleteIngredientGoodsReceiptDocumentDetail(ActionEvent event) throws IOException {
        HttpSession session = Util.getSession();
        id = (Long) session.getAttribute("ingredientgoodsreceiptid");
        IngredientGoodsReceiptDocumentDetail ingredientGoodsReceiptDetail = (IngredientGoodsReceiptDocumentDetail) event.getComponent().getAttributes().get("tod");
        receiptBean.deleteIngredientGoodsReceiptDocumentDetail(ingredientGoodsReceiptDetail);
        ingredientGoodsReceiptDetailList = receiptBean.viewIngredientGooodsReceiptDocumentDetail(ingredientGoodsReceipt);
    }

//  Function: To post Ingredient Goods Receipt Document
    public void postIngredientGoodsReceiptDocument(ActionEvent event) throws IOException {
        HttpSession session = Util.getSession();
        id = (Long) session.getAttribute("ingredientgoodsreceiptid");
        receiptBean.postIngredientGoodsReceiptDocument(staff, getCalendar(), ingredientGoodsReceipt);
    }

    public Long getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(Long ingredientId) {
        this.ingredientId = ingredientId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public void setIngredientList(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
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
