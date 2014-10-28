/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.OperationalCRM;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.EJB.OperationalCRM.ProductInfoBeanLocal;
import IslandFurniture.Entities.FurnitureModel;
import IslandFurniture.Entities.RetailItem;
import IslandFurniture.Entities.Staff;
import IslandFurniture.Entities.Stock;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@ManagedBean
@ViewScoped
public class ProductInfoManagedBean implements Serializable {

    @EJB
    private ProductInfoBeanLocal productInfoBean;

    @EJB
    private ManageUserAccountBeanLocal manageUserAccountBean;

    private Long stockId;
    private String stockName;
    private List<Stock> productList;
    private Stock product;
    private FurnitureModel furniture;
    private RetailItem retailItem;
    private Staff staff;

    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        staff = manageUserAccountBean.getStaff((String) session.getAttribute("username"));
        String stockIdParam = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("stockId");
        String stockNameParam = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("stockName");

        if (stockIdParam != null) {
            stockId = Long.parseLong(stockIdParam);
            product = productInfoBean.findProductById(stockId);
            if (product != null) {
                if(product instanceof FurnitureModel){
                    furniture = (FurnitureModel) product;
                } else if(product instanceof RetailItem){
                    retailItem = (RetailItem) product;
                } else{
                    product = null;
                }
            }
        } else if (stockNameParam != null) {
            stockName = stockNameParam;
            productList = productInfoBean.searchProductByName(stockName);
            if (productList == null || productList.isEmpty()) {
                productList = null;
            }
        }
    }

    public void searchProductById() {
        if (productInfoBean.findProductById(stockId) == null) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "No such product ID exists", ""));
        }
    }

    public void searchProductByName() {
        productList = productInfoBean.searchProductByName(stockName);
        if (productList == null || productList.isEmpty()) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Search returned no results", ""));
        }
    }

    /**
     * Creates a new instance of ProductInfoManagedBean
     */
    public ProductInfoManagedBean() {
    }

    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public List<Stock> getProductList() {
        return productList;
    }

    public void setProductList(List<Stock> productList) {
        this.productList = productList;
    }

    public Stock getProduct() {
        return product;
    }

    public void setProduct(Stock product) {
        this.product = product;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public FurnitureModel getFurniture() {
        return furniture;
    }

    public void setFurniture(FurnitureModel furniture) {
        this.furniture = furniture;
    }

    public RetailItem getRetailItem() {
        return retailItem;
    }

    public void setRetailItem(RetailItem retailItem) {
        this.retailItem = retailItem;
    }

}
