/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Islandfurniture.WAR2.CustomerWebServices;

import IslandFurniture.EJB.CustomerWebService.ManageCatalogueBeanLocal;
import IslandFurniture.EJB.CustomerWebService.ManageHomeLocal;
import IslandFurniture.EJB.CustomerWebService.ManageLocalizationBeanLocal;
import IslandFurniture.EJB.CustomerWebService.ManageMemberAuthenticationBeanLocal;
import IslandFurniture.EJB.OperationalCRM.ManageMarketingBeanLocal;
import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.Customer;
import IslandFurniture.Entities.FurnitureModel;
import IslandFurniture.Entities.Picture;
import IslandFurniture.Entities.RetailItem;
import IslandFurniture.Entities.Stock;
import IslandFurniture.Entities.Store;
import IslandFurniture.Entities.WebBanner;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Zee
 */
@ManagedBean
@ViewScoped
public class HomeManagedBean {
    
    private CountryOffice co;
    private List<WebBanner> webBanners;
    private List<Stock> featuredProducts;
    private List<FurnitureModel> featuredFurniture = new ArrayList<>();
    private List<RetailItem> featuredRetailItems;
    private Customer customer;
    private String emailAddress = null;
    private boolean loggedIn = false;

    @EJB
    private ManageLocalizationBeanLocal manageLocalizationBean;
    @EJB
    private ManageHomeLocal homeBean;
    @EJB
    private ManageMarketingBeanLocal mmbl;    
    @EJB
    private ManageCatalogueBeanLocal mcbl;
    @EJB
    private ManageMemberAuthenticationBeanLocal mmab;    
    
    @PostConstruct
    public void init() {
        HttpServletRequest httpReq = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        co = manageLocalizationBean.findCoByCode((String) httpReq.getAttribute("coCode"));
        
        webBanners = co.getWebBanners();
        featuredFurniture = mcbl.getCountryFeaturedFurniture(co);
        
        loggedIn = checkLoggedIn();
        if (loggedIn)
            customer = mmab.getCustomer(emailAddress);        
        System.out.println("loaded " + co.getName() + " web banners");
    }
    
    public boolean checkLoggedIn() {
        HttpSession session = Util.getSession();  
        if (session == null)
            return false;
        else {
            emailAddress = (String) session.getAttribute("emailAddress");            
            if (emailAddress == null)
                return false;
            else 
                return true;
        }
    }    

    public Double getDiscountedPrice(Stock s) {
        Store st = new Store();
        st.setCountryOffice(co);
        if (customer != null)
            return (Double)mmbl.getDiscountedPrice(s, st, customer).get("D_PRICE");
        else
            return (Double)mmbl.getDiscountedPrice(s, st, new Customer()).get("D_PRICE");
    }
    
    public String checkForActive(int index) {
        if (index == 0)
            return "active";
        else
            return "";
    }

    public StreamedContent getImage() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            // So, we're rendering the HTML. Return a stub StreamedContent so that it will generate right URL.
            return new DefaultStreamedContent();
        }
        else {
            // So, browser is requesting the image. Return a real StreamedContent with the image bytes.
            String pictureId = context.getExternalContext().getRequestParameterMap().get("pictureId");
            Picture picture = homeBean.getPicture(Long.valueOf(pictureId));
            return new DefaultStreamedContent(new ByteArrayInputStream(picture.getContent()));
        }
    }    
    
    public CountryOffice getCo() {
        return co;
    }

    public void setCo(CountryOffice co) {
        this.co = co;
    }

    public List<WebBanner> getWebBanners() {
        return webBanners;
    }

    public void setWebBanners(List<WebBanner> webBanners) {
        this.webBanners = webBanners;
    }

    public List<Stock> getFeaturedProducts() {
        return featuredProducts;
    }

    public void setFeaturedProducts(List<Stock> featuredProducts) {
        this.featuredProducts = featuredProducts;
    }

    public List<FurnitureModel> getFeaturedFurniture() {
        return featuredFurniture;
    }

    public void setFeaturedFurniture(List<FurnitureModel> featuredFurniture) {
        this.featuredFurniture = featuredFurniture;
    }

    public List<RetailItem> getFeaturedRetailItems() {
        return featuredRetailItems;
    }

    public void setFeaturedRetailItems(List<RetailItem> featuredRetailItems) {
        this.featuredRetailItems = featuredRetailItems;
    }
}
