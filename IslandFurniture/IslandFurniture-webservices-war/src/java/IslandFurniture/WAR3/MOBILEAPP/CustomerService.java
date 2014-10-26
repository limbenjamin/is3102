/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR3.MOBILEAPP;

import IslandFurniture.EJB.CustomerWebService.ManageCatalogueBeanLocal;
import IslandFurniture.EJB.CustomerWebService.ManageShoppingListBeanLocal;
import IslandFurniture.EJB.OperationalCRM.ManageMarketingBeanLocal;
import IslandFurniture.EJB.OperationalCRM.MobileAppServiceLocal;
import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.Customer;
import IslandFurniture.Entities.FurnitureModel;
import IslandFurniture.Entities.Picture;
import IslandFurniture.Entities.ShoppingList;
import IslandFurniture.Entities.ShoppingListDetail;
import IslandFurniture.Entities.Store;
import IslandFurniture.Enums.FurnitureCategory;
import IslandFurniture.Exceptions.DuplicateEntryException;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.json.Json;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.json.*;
import javax.servlet.ServletContext;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author James
 */


@Named(value = "customerService")
@Stateless
@Path("cs")
public class CustomerService {

    @Context
    ServletContext context;

    @EJB
    private MobileAppServiceLocal masl;

    @EJB
    private ManageCatalogueBeanLocal mcb;

    @EJB
    private ManageMarketingBeanLocal mmb;

    @EJB
    private ManageShoppingListBeanLocal mslb;

    @GET
    @Path("memberlogin")
    public String getCustomerInfo(@QueryParam("custid") String Cust_ID) {

        if (Cust_ID == null) {
            return "";
        }

        Long custid = Long.parseLong(Cust_ID);
        System.out.println("getCustomerInfo(): " + Cust_ID);
        JsonObjectBuilder object = Json.createObjectBuilder();
        object.add("point", masl.getCustomerCurrentPoint(custid));
        object.add("tier", masl.getCustomerMemberTier(custid));
        object.add("name", masl.getCustomerName(custid));

        return object.build().toString();
    }

    @GET
    @Path("category")
    public String getFurnitureCategory() {
        return "";
    }

    @GET
    @Path("allstores")
    public String getallStores(@QueryParam("CO_CODE") String CO) {
        JsonObjectBuilder object = Json.createObjectBuilder();

        JsonArrayBuilder jab = Json.createArrayBuilder();

        for (Store s : masl.getAllStores(CO)) {
            jab.add(Json.createArrayBuilder().add(s.getName()).add(s.getLatitude()).add(s.getLongitude()).add(s.getAddress()).add(s.getCountryOffice().getId()).add(s.getCountryOffice().getName()).add(s.getId()));
        }
        object.add("Stores", jab);

        return object.build().toString();

    }

    @GET
    @Path("allCO")
    public String getAllCountryOffices() {

        JsonObjectBuilder object = Json.createObjectBuilder();

        JsonArrayBuilder jab = Json.createArrayBuilder();

        for (CountryOffice c : masl.getAllCO()) {

            jab.add(Json.createArrayBuilder().add(c.getName()).add(c.getCountry().getCode()));

        }

        object.add("Country", jab);

        return object.build().toString();

    }

    @GET
    @Path("prepcatalogue")
    public String getCatalogue(@QueryParam("CO") String CO) {

        JsonObjectBuilder object = Json.createObjectBuilder();
        JsonArrayBuilder jab = Json.createArrayBuilder();

        HashMap<FurnitureCategory, JsonArrayBuilder> map = new HashMap<>();
        map.put(null, Json.createArrayBuilder());
        for (FurnitureModel fm : mcb.getStoreFurniture(masl.getCOFromID(CO))) {
            if (map.get(fm.getCategory()) == null) {
                map.put(fm.getCategory(), Json.createArrayBuilder());

            }
            Store s = new Store();
            s.setCountryOffice(masl.getCOFromID(CO));
            double price = (double) mmb.getDiscountedPrice(fm, s, new Customer()).get("D_PRICE");

            map.get(fm.getCategory()).add(Json.createArrayBuilder().add(fm.getId()).add(fm.getName()).add(fm.getFurnitureDescription()).add(price));
            map.get(null).add(Json.createArrayBuilder().add(fm.getId()).add(fm.getName()).add(fm.getFurnitureDescription()).add(price));
        }

        JsonArrayBuilder cata = Json.createArrayBuilder();

        for (FurnitureCategory fc : map.keySet()) {

            if (fc != null) {
                cata.add(fc.toString());
                object.add(fc.toString(), map.get(fc).build());
            }
        }
        object.add("full", map.get(null).build());
        object.add("count", mcb.getStoreFurniture(masl.getCOFromID(CO)).size());

        object.add("category", cata.build());

        return object.build().toString();

    }

    @GET
    @Produces("image/jpg")
    @Path("furnitureimage")
    public Response getImage(@QueryParam("id") String id, @Context UriInfo uriInfo) {
        Picture pic = masl.getPicture(id);
        byte[] data = null;

        if (pic == null) {
            System.out.println("getImage(): " + context.getRealPath("/nopic.jpg"));
            java.nio.file.Path p = Paths.get(context.getRealPath("/nopic.jpg"));
            try {
                data = Files.readAllBytes(p);
            } catch (Exception ex) {
            }
        } else {
            data = pic.getContent();
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream(data.length);
        out.write(data, 0, data.length);

        return Response.ok(out.toByteArray()).build();

    }

    @GET
    @Path("shoplist")
    public String getShopList(@QueryParam("cust_id") String cust_id, @QueryParam("store_id") String store_id) {

        ShoppingList sl = (masl.getShoppingList(cust_id, store_id));
        JsonObjectBuilder object = Json.createObjectBuilder();
        JsonArrayBuilder jab = Json.createArrayBuilder();

        object.add("name", sl.getName());
        object.add("totalprice", sl.getTotalPrice());
        Double total = 0.0;
        for (ShoppingListDetail sld : sl.getShoppingListDetails()) {

            double price = (double) mmb.getDiscountedPrice(sld.getFurnitureModel(), masl.getStoreFromID(store_id), new Customer()).get("D_PRICE") * sld.getQty();
            total += price;
            jab.add(Json.createObjectBuilder().add("fm", sld.getFurnitureModel().getName()).add("qty", sld.getQty()).add("price", price).add("fid", sld.getFurnitureModel().getId()).add("delete_id", sld.getId()));
        }
        object.add("details", jab);
        object.add("totalprice", total);
        return object.build().toString();
    }

    @GET
    @Path("deleteitemfromshoplist")
    public String deleteItem(@QueryParam("detailID") String DetailID) {
        mslb.deleteShoppingListDetail(Long.parseLong(DetailID));
        return "TRUE";
    }

    @GET
    @Path("additemtoshoplist")
    public String addItem(@QueryParam("cust_id") String cust_id, @QueryParam("store_id") String store_id, @QueryParam("fm_id") String fm_id, @QueryParam("qty") Integer qty) {
        ShoppingList sl = (masl.getShoppingList(cust_id, store_id));

        double price = (double) mmb.getDiscountedPrice(masl.getfmFromID(fm_id), masl.getStoreFromID(store_id), new Customer()).get("D_PRICE");
        try {
            mslb.createShoppingListDetail(sl.getId(), Long.parseLong(fm_id), qty, price);
        } catch (DuplicateEntryException ex) {
        }

        return masl.getfmFromID(fm_id).getName() + " added to " + masl.getStoreFromID(store_id).getName() + " Shopping List";

    }

    @GET
    @Path("additembynfc")
    public String addItemByNFC(@QueryParam("cust_id") String cust_id, @QueryParam("store_id") String store_id, @QueryParam("NFC_TAG") String NFC_TAG) {
        FurnitureModel fm;
        try {
            fm = masl.getFurnitureModelByNFCID(NFC_TAG);
        } catch (Exception ex) {
            System.out.println("addItemByNFC(): "+ex.getMessage());
            return "Invalid NFC Tag";
        }
        
        return addItem(cust_id, store_id, fm.getId().toString(), 1);

    }

}
