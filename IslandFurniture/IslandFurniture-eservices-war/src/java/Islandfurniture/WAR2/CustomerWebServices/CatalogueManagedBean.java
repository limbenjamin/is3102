/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Islandfurniture.WAR2.CustomerWebServices;

import IslandFurniture.EJB.CustomerWebService.ManageCatalogueBeanLocal;
import IslandFurniture.Entities.FurnitureModel;
import IslandFurniture.Enums.FurnitureCategory;
import IslandFurniture.Enums.FurnitureSubcategory;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Zee
 */
@ManagedBean
@ViewScoped
public class CatalogueManagedBean implements Serializable{

    private List<FurnitureModel> furnitureList;
    private String name;
    private String furnitureDescription;
    private Double price;
    private FurnitureCategory category;
    private FurnitureSubcategory subcategory;   
    
    @EJB
    private ManageCatalogueBeanLocal mcbl;
    
    @PostConstruct
    public void init() {
        furnitureList = mcbl.getAllFurniture();
        System.out.println("loaded furniture models");
    }

    public List<FurnitureModel> getFurnitureList() {
        return furnitureList;
    }

    public void setFurnitureList(List<FurnitureModel> furnitureList) {
        this.furnitureList = furnitureList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFurnitureDescription() {
        return furnitureDescription;
    }

    public void setFurnitureDescription(String furnitureDescription) {
        this.furnitureDescription = furnitureDescription;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public FurnitureCategory getCategory() {
        return category;
    }

    public void setCategory(FurnitureCategory category) {
        this.category = category;
    }

    public FurnitureSubcategory getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(FurnitureSubcategory subcategory) {
        this.subcategory = subcategory;
    }

    public ManageCatalogueBeanLocal getMcbl() {
        return mcbl;
    }

    public void setMcbl(ManageCatalogueBeanLocal mcbl) {
        this.mcbl = mcbl;
    }
    
}