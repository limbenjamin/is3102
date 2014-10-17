/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR3.POS;

import IslandFurniture.EJB.CustomerWebService.ManageCatalogueBeanLocal;
import IslandFurniture.EJB.CustomerWebService.ManageMemberAuthenticationBeanLocal;
import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.EJB.Manufacturing.StockManagerLocal;
import IslandFurniture.EJB.Purchasing.SupplierManagerLocal;
import IslandFurniture.Entities.Customer;
import IslandFurniture.Entities.FurnitureModel;
import IslandFurniture.Entities.RetailItem;
import IslandFurniture.Entities.Staff;
import IslandFurniture.Entities.Store;
import java.util.Iterator;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 *
 * @author Benjamin
 */

@Stateless //For some reason, need to make this stateless else cannot inject
@Path("stock")
public class StocklistWS {
    
    private List<FurnitureModel> furnitureList;
    private List<RetailItem> retailList;
    private FurnitureModel furnitureModel;
    private RetailItem retailItem;
    private JsonArrayBuilder builder;
    
    @EJB
    ManageUserAccountBeanLocal muabl;
    @EJB
    SupplierManagerLocal ssm;
    @EJB
    ManageMemberAuthenticationBeanLocal mmab;
    @EJB
    ManageCatalogueBeanLocal mcbl;
    
    @POST
    @Path("furniturelist")
    public String getFurnitureList(@FormParam("cardId") String cardId) {
        System.err.println(cardId);
        Staff staff = muabl.getStaffFromCardId(cardId);
        if (staff == null){
            return "Error";
        }else{
            Store store = (Store) staff.getPlant();
            furnitureList = mcbl.getStoreFurniture(store);
            builder = Json.createArrayBuilder();
            Iterator<FurnitureModel> iterator = furnitureList.iterator();
            while(iterator.hasNext()){
                furnitureModel = iterator.next();
                String id = String.valueOf(furnitureModel.getId());
                String price = String.valueOf(ssm.getPriceForStock(store.getCountryOffice(), furnitureModel));
                String category = String.valueOf(furnitureModel.getCategory().name());
                builder.add(Json.createObjectBuilder().add("id",id).add("name", furnitureModel.getName())
                        .add("price", price).add("category", category).build());
            }
        }
        return builder.build().toString();
    }
    
    @POST
    @Path("retaillist")
    public String getRetailList(@FormParam("cardId") String cardId) {
        Staff staff = muabl.getStaffFromCardId(cardId);
        if (staff == null){
            return "Error";
        }else{
            Store store = (Store) staff.getPlant();
            retailList = mcbl.getStoreRetailItems(store);
            builder = Json.createArrayBuilder();
            Iterator<RetailItem> iterator = retailList.iterator();
            while(iterator.hasNext()){
                retailItem = iterator.next();
                String id = String.valueOf(retailItem.getId());
                String price = String.valueOf(ssm.getPriceForStock(store.getCountryOffice(), retailItem));
                //String category = String.valueOf(retailItem.);
                builder.add(Json.createObjectBuilder().add("id", id).add("name", retailItem.getName())
                        .add("price", price).build());
            }
        }
        return builder.build().toString();
    }
    
    @POST
    @Path("checkpromo")
    public String checkPromo(@FormParam("cardId") String cardId,
                                @FormParam("customerCardId") String customerCardId,
                                @FormParam("coupon") String coupon,
                                @FormParam("stock") String stock,
                                @FormParam("stockCoupon") String stockCoupon) {
        Staff staff = muabl.getStaffFromCardId(cardId);
        if (staff == null){
            return "Error";
        }else{
            System.err.println(cardId+"   "+customerCardId+"    "+coupon+"    "+stock+"    "+stockCoupon);
        }
        JsonObject object = Json.createObjectBuilder().add("price", "1.0").add("promo", "Sample").build();
        return object.toString();
    }
    
    @POST
    @Path("getcustomername")
    public String getCustomerName(@FormParam("cardId") String cardId,
                                @FormParam("customerCardId") String customerCardId){
        Customer customer;
        Staff staff = muabl.getStaffFromCardId(cardId);
        if (staff == null){
            return "Error";
        }else{
            customer = mmab.getCustomerFromLoyaltyCardId(customerCardId);
        }
        return customer.getName();
    }
    
}
