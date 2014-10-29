/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.OperationalCRM;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.EJB.InventoryManagement.ManageStorefrontInventoryLocal;
import IslandFurniture.EJB.OperationalCRM.ManageMarketingBeanLocal;
import IslandFurniture.EJB.OperationalCRM.ProductInfoBeanLocal;
import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.FurnitureModel;
import IslandFurniture.Entities.PromotionDetail;
import IslandFurniture.Entities.RetailItem;
import IslandFurniture.Entities.Staff;
import IslandFurniture.Entities.Stock;
import IslandFurniture.Entities.Store;
import IslandFurniture.Entities.StoreSection;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.convert.NumberConverter;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@ManagedBean
@ViewScoped
public class ProductInfoManagedBean implements Serializable {
    @EJB
    private ManageStorefrontInventoryLocal manageStorefrontInventory;

    @EJB
    private ManageMarketingBeanLocal manageMarketingBean;

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
    private Map<Store, Map<String, Object>> priceMap;

    private Staff staff;
    private CountryOffice co;

    private NumberConverter converter = new NumberConverter();

    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        staff = manageUserAccountBean.getStaff((String) session.getAttribute("username"));
        String stockIdParam = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("stockId");
        String stockNameParam = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("stockName");

        if (staff.getPlant() != null && (staff.getPlant() instanceof Store || staff.getPlant() instanceof CountryOffice)) {
            if (stockIdParam != null) {
                if (staff.getPlant() instanceof Store) {
                    co = ((Store) staff.getPlant()).getCountryOffice();
                } else {
                    co = (CountryOffice) staff.getPlant();
                }

                stockId = Long.parseLong(stockIdParam);
                product = productInfoBean.findProductById(stockId, co);
                if (product != null) {
                    if (product instanceof FurnitureModel) {
                        furniture = (FurnitureModel) product;
                    } else if (product instanceof RetailItem) {
                        retailItem = (RetailItem) product;
                    } else {
                        product = null;
                    }

                    // Find StockSupplied to grab price in CountryOffice
                    // Set Number Converter to display currency
                    converter = new NumberConverter();
                    converter.setCurrencyCode(co.getCountry().getCurrency().getCurrencyCode());
                    converter.setType("currency");
                    
                    // Load price and promotion content for product
                    priceMap = new HashMap();
                    for(Store store: co.getStores()){
                        priceMap.put(store, manageMarketingBean.getDiscountedPrice(product, store, null));
                    }
                    
                }
            } else if (stockNameParam != null) {
                stockName = stockNameParam;
                productList = productInfoBean.searchProductByName(stockName);
                if (productList == null || productList.isEmpty()) {
                    productList = null;
                }
            }
        } else {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("../dash.xhtml");
            } catch (IOException ex) {

            }
        }
    }

    public void searchProductById() {
        if (productInfoBean.findProductById(stockId, co) == null) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "No such product ID exists in " + co + " Country Office", ""));
        }
    }

    public void searchProductByName() {
        productList = productInfoBean.searchProductByName(stockName);
        if (productList == null || productList.isEmpty()) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Search returned no results", ""));
        }
    }

    public Double findPrice(Store store) {
        return (Double) priceMap.get(store).get("O_PRICE");
    }

    public Double findDiscPrice(Store store) {
        return (Double) priceMap.get(store).get("D_PRICE");
    }

    public String findPromo(Store store) {
        PromotionDetail promoUsed = (PromotionDetail) priceMap.get(store).get("Successful_promotion");
        if (promoUsed == null) {
            return "-None-";
        } else {
            return promoUsed.getPromotionCampaign().getTitle();
        }
    }
    
    public Integer findWarehouseStockLvl(Store store){
        return manageStorefrontInventory.viewStockUnitStockQty(store, product);
    }
    
    public Integer findStorefrontStockLvl(Store store){
        return manageStorefrontInventory.viewStorefrontInventoryStockQty(store, product);
    }

    public String findStockLocation(Store store){
        StoreSection section = store.findStorefrontInventory(product).getLocationInStore();
        return section.getName() + " (Lvl " + section.getStoreLevel() + ")";
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

    public CountryOffice getCo() {
        return co;
    }

    public void setCo(CountryOffice co) {
        this.co = co;
    }

    public NumberConverter getConverter() {
        return converter;
    }

    public void setConverter(NumberConverter converter) {
        this.converter = converter;
    }

}
