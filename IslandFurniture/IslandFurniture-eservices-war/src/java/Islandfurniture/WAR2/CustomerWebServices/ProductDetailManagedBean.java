/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Islandfurniture.WAR2.CustomerWebServices;

import IslandFurniture.EJB.CustomerWebService.ManageCatalogueBeanLocal;
import IslandFurniture.EJB.CustomerWebService.ManageLocalizationBeanLocal;
import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.FurnitureModel;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Zee
 */
@ManagedBean
@ViewScoped
public class ProductDetailManagedBean {

    private Long id = null;
    private FurnitureModel furniture = null;
    
    @EJB
    private ManageLocalizationBeanLocal manageLocalizationBean;    
    @EJB
    private ManageCatalogueBeanLocal mcbl;
    
    @PostConstruct
    public void init() {
        HttpSession session = Util.getSession();
        HttpServletRequest httpReq = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        CountryOffice co = manageLocalizationBean.findCoByCode((String) httpReq.getAttribute("coCode"));
        
        try{
            id = new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
            if (id != null) {
                System.out.println("id is " + id);
                session.setAttribute("id", id);
            }
            else {
                //id = Long.parseLong("436");
                id = (Long) session.getAttribute("id");
            }
        }catch (Exception e){
            
        }
        
        furniture = mcbl.getFurnitureModel(id);
        System.out.println("Got furniture model " + furniture.getName());
    }    
    
    public String displayProductDetails() {
      HttpServletRequest httpReq = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();  
      String coCode = (String) httpReq.getAttribute("coCode");
      HttpSession session = Util.getSession();
      id = (Long) session.getAttribute("id");        
      //id = new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
      furniture = mcbl.getFurnitureModel(id);
      return "/coCode" + "/productdetail";
    }    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FurnitureModel getFurniture() {
        return furniture;
    }

    public void setFurniture(FurnitureModel furniture) {
        this.furniture = furniture;
    }

    public ManageLocalizationBeanLocal getManageLocalizationBean() {
        return manageLocalizationBean;
    }

    public void setManageLocalizationBean(ManageLocalizationBeanLocal manageLocalizationBean) {
        this.manageLocalizationBean = manageLocalizationBean;
    }

    public ManageCatalogueBeanLocal getMcbl() {
        return mcbl;
    }

    public void setMcbl(ManageCatalogueBeanLocal mcbl) {
        this.mcbl = mcbl;
    }
    
}
