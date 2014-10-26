/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR3.POS;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.EJB.OperationalCRM.ManagePOSLocal;
import IslandFurniture.Entities.ShoppingList;
import IslandFurniture.Entities.ShoppingListDetail;
import IslandFurniture.Entities.Staff;
import IslandFurniture.Entities.Store;
import java.util.Iterator;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 *
 * @author Benjamin
 */
@Stateless
@Path("shopping")
public class ShoppingListWS {

    private List<ShoppingList> shoppingListList;
    private JsonArrayBuilder builder;
    private JsonArrayBuilder builder2;
    private ShoppingList shoppingList;
    private ShoppingListDetail shoppingListDetail;
    
    @EJB
    ManageUserAccountBeanLocal muabl;
    @EJB
    ManagePOSLocal mpl;
    
    
    @POST
    @Path("list")
    public String getShoppingList(@FormParam("cardId") String cardId,
                                    @FormParam("customerCardId") String customerCardId) {
        System.err.println(cardId);
        Staff staff = muabl.getStaffFromCardId(cardId);
        if (staff == null){
            return "Error";
        }else{
            shoppingListList = mpl.getShoppingListList(customerCardId);
            builder = Json.createArrayBuilder();
            Iterator<ShoppingList> iterator = shoppingListList.iterator();
            while(iterator.hasNext()){
                shoppingList = iterator.next();
                if (shoppingList.getStore().getId() == staff.getPlant().getId()){
                    String id = String.valueOf(shoppingList.getId());
                    String totalPrice = String.valueOf(shoppingList.getTotalPrice());
                    List<ShoppingListDetail> shoppingListDetailList = shoppingList.getShoppingListDetails();
                    builder2 = Json.createArrayBuilder();
                    Iterator<ShoppingListDetail> iterator2 = shoppingListDetailList.iterator();
                    while(iterator2.hasNext()){
                        shoppingListDetail = iterator2.next();
                        String model = String.valueOf(shoppingListDetail.getFurnitureModel().getId());
                        String qty = String.valueOf(shoppingListDetail.getQty());
                        builder2.add(Json.createObjectBuilder().add("model",model).add("qty", qty).build());
                    }
                    builder.add(Json.createObjectBuilder().add("id",id).add("name", shoppingList.getName())
                            .add("price", totalPrice).add("items", builder2.build()).build());
                }
            }
        }
        return builder.build().toString();
    }
}
