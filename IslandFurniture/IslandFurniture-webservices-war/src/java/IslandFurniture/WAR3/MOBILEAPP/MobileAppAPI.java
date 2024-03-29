/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR3.MOBILEAPP;

import IslandFurniture.EJB.CustomerWebService.ManageCatalogueBeanLocal;
import IslandFurniture.EJB.CustomerWebService.ManageMemberAuthenticationBeanLocal;
import IslandFurniture.EJB.CustomerWebService.ManageShoppingListBeanLocal;
import IslandFurniture.EJB.ITManagement.ManageOrganizationalHierarchyBeanLocal;
import IslandFurniture.EJB.InventoryManagement.ManageStorefrontInventoryLocal;
import IslandFurniture.EJB.OperationalCRM.ManageMarketingBeanLocal;
import IslandFurniture.EJB.OperationalCRM.ManageMembershipLocal;
import IslandFurniture.EJB.OperationalCRM.MobileAppServiceLocal;
import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.Customer;
import IslandFurniture.Entities.FurnitureModel;
import IslandFurniture.Entities.Picture;
import IslandFurniture.Entities.PromotionDetail;
import IslandFurniture.Entities.ShoppingList;
import IslandFurniture.Entities.ShoppingListDetail;
import IslandFurniture.Entities.Store;
import IslandFurniture.Enums.FurnitureCategory;
import IslandFurniture.Exceptions.DuplicateEntryException;
import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.json.*;
import javax.json.Json;
import javax.servlet.ServletContext;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author James
 */
@Named(value = "customerService")
@Stateless
@Path("cs")
public class MobileAppAPI {

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

    @EJB
    private ManageMemberAuthenticationBeanLocal mmab;

    @EJB
    private ManageStorefrontInventoryLocal msfil;

    @EJB
    private ManageMembershipLocal cmu;

    @EJB
    private ManageOrganizationalHierarchyBeanLocal mohb;

    @GET
    @Path("memberlogin")
    @Produces(MediaType.APPLICATION_JSON)
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
    @Produces(MediaType.APPLICATION_JSON)
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
    @Produces(MediaType.APPLICATION_JSON)
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
    @Produces(MediaType.APPLICATION_JSON)
    public String getCatalogue(@QueryParam("CO") String CO, @QueryParam("STOREID") String StoreID) {

        JsonObjectBuilder object = Json.createObjectBuilder();
        JsonArrayBuilder jab = Json.createArrayBuilder();
        HashMap<FurnitureCategory, JsonArrayBuilder> map = new HashMap<>();
        map.put(null, Json.createArrayBuilder());
        for (FurnitureModel fm : mcb.getStoreFurniture(masl.getCOFromID(CO))) {
            if (map.get(fm.getCategory()) == null) {
                map.put(fm.getCategory(), Json.createArrayBuilder());

            }
            Store s = masl.getStoreFromID(StoreID);
            double price = (double) mmb.getDiscountedPrice(fm, s, new Customer()).get("D_PRICE");
            //msfil
            map.get(fm.getCategory()).add(Json.createArrayBuilder().add(fm.getId()).add(fm.getName()).add(fm.getFurnitureDescription()).add(price).add(msfil.viewStorefrontInventoryStockLevelPerPlant(s, fm)));
            map.get(null).add(Json.createArrayBuilder().add(fm.getId()).add(fm.getName()).add(fm.getFurnitureDescription()).add(price).add(msfil.viewStorefrontInventoryStockLevelPerPlant(s, fm)));
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
    @Produces(MediaType.APPLICATION_JSON)
    public String getShopList(@QueryParam("cust_id") String cust_id, @QueryParam("store_id") String store_id, @QueryParam("sl_name") String sl_name) {

        ShoppingList sl = (masl.getShoppingList(cust_id, store_id, sl_name));
        JsonObjectBuilder object = Json.createObjectBuilder();
        JsonArrayBuilder jab = Json.createArrayBuilder();

        object.add("name", sl.getName());
        Double total = 0.0;
        Integer c = 0;
        for (ShoppingListDetail sld : sl.getShoppingListDetails()) {

            c += sld.getQty();
            double price = (double) mmb.getDiscountedPrice(sld.getFurnitureModel(), masl.getStoreFromID(store_id), masl.getcustomerFromid(cust_id)).get("D_PRICE");
            double oprice = (double) mmb.getDiscountedPrice(sld.getFurnitureModel(), masl.getStoreFromID(store_id), masl.getcustomerFromid(cust_id)).get("O_PRICE");

            total += price * sld.getQty();
            if (mmb.getDiscountedPrice(sld.getFurnitureModel(), masl.getStoreFromID(store_id), masl.getcustomerFromid(cust_id)).keySet().contains("Successful_promotion")) {
                PromotionDetail pd = (PromotionDetail) mmb.getDiscountedPrice(sld.getFurnitureModel(), masl.getStoreFromID(store_id), masl.getcustomerFromid(cust_id)).get("Successful_promotion");
                jab.add(Json.createObjectBuilder().add("fm", sld.getFurnitureModel().getName()).add("qty", sld.getQty()).add("oprice", oprice).add("uprice", price).add("price", Math.floor(price * sld.getQty() * 100) / 100.0).add("fid", sld.getFurnitureModel().getId()).add("delete_id", sld.getId()).add("PromoTxt", pd.getPromotionCampaign().getTitle()).add("inv_txt", msfil.viewStorefrontInventoryStockLevelPerPlant(sld.getShoppingList().getStore(), sld.getFurnitureModel())));
            } else {
                jab.add(Json.createObjectBuilder().add("fm", sld.getFurnitureModel().getName()).add("qty", sld.getQty()).add("oprice", oprice).add("uprice", price).add("price", Math.floor(price * sld.getQty() * 100) / 100.0).add("fid", sld.getFurnitureModel().getId()).add("delete_id", sld.getId()).add("PromoTxt", "").add("inv_txt", msfil.viewStorefrontInventoryStockLevelPerPlant(sld.getShoppingList().getStore(), sld.getFurnitureModel())));
            }

        }

        object.add("details", jab);
        object.add("count", c);
        object.add("store", sl.getStore().getId());
        object.add("totalprice", Math.floor(total * 100) / 100.0);
        object.add("shoplistid", sl.getId());
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

    public String addItem(@QueryParam("cust_id") String cust_id, @QueryParam("store_id") String store_id, @QueryParam("fm_id") String fm_id, @QueryParam("qty") Integer qty, @QueryParam("sl_name") String sl_name) {
        ShoppingList sl = (masl.getShoppingList(cust_id, store_id, sl_name));

        double price = (double) mmb.getDiscountedPrice(masl.getfmFromID(fm_id), masl.getStoreFromID(store_id), masl.getcustomerFromid(cust_id)).get("D_PRICE");
        try {
            mslb.createShoppingListDetail(sl.getId(), Long.parseLong(fm_id), qty, price);
        } catch (DuplicateEntryException ex) {
        }

        return masl.getfmFromID(fm_id).getName() + " added to " + masl.getStoreFromID(store_id).getName() + " Shopping List";

    }

    @GET
    @Path("getShoppingList")
    @Produces(MediaType.APPLICATION_JSON)
    public String getShoppingList(@QueryParam("cust_id") String cust_id, @QueryParam("store_id") String store_id) {

        JsonObjectBuilder object = Json.createObjectBuilder();
        JsonArrayBuilder jab = Json.createArrayBuilder();
        List<ShoppingList> sl = masl.getShoppingList(cust_id, store_id);

        for (ShoppingList s : sl) {
            jab.add(Json.createObjectBuilder().add("ID", s.getId()).add("name", s.getName()).add("total_price", s.getTotalPrice()).add("count", s.getShoppingListDetails().size()));
        }

        object.add("ShoppingLists", jab);

        return object.build().toString();
    }

    @GET
    @Path("additembynfc")
    public String addItemByNFC(@QueryParam("cust_id") String cust_id, @QueryParam("store_id") String store_id, @QueryParam("NFC_TAG") String NFC_TAG, @QueryParam("sl_name") String sl_name) {
        FurnitureModel fm;
        try {
            fm = masl.getFurnitureModelByNFCID(NFC_TAG);
        } catch (Exception ex) {
            System.out.println("addItemByNFC(): " + ex.getMessage());
            return "Invalid NFC Tag";
        }

        return addItem(cust_id, store_id, fm.getId().toString(), 1, sl_name);

    }

    @GET
    @Path("deleteshoplist")
    public Boolean deleteShopList(@QueryParam("sID") String shoplistID) {
        masl.DeleteShopList(Long.parseLong(shoplistID));

        return true;
    }

    @GET
    @Path("moveshoplist")
    public Boolean ShiftShopList(@QueryParam("sLID") String shoplistID, @QueryParam("stID") String destinationID) {

        masl.ShiftShopList(Long.parseLong(shoplistID), Long.parseLong(destinationID));
        return true;
    }

    @POST
    @Path("register")
    @Produces(MediaType.APPLICATION_JSON)
    public String getRegister(@FormParam("email") String email, @FormParam("password") String password, @FormParam("password2") String password2, @FormParam("name") String name, @FormParam("address") String address, @FormParam("phone") String phone, @FormParam("dob") String dob, @FormParam("country") String country) {

        try {
            mmab.createCustomerAccount(email, password, name, phone, address, name, mohb.findCountryOfficeByName(country).getCountry());
        } catch (Exception ex) {
            return (Json.createObjectBuilder().add("success", false).add("error", ex.getMessage()).build().toString());
        }
        return (Json.createObjectBuilder().add("success", false).add("data", Json.createArrayBuilder()).build().toString());
    }

    @GET
    @Path("promote")
    @Produces(MediaType.APPLICATION_JSON)
    public String PromoteMember(@QueryParam("cust_id") Long custID, @QueryParam("transc_id") Long Transc_ID) {
        String status = "";
        try {
            status = cmu.checkMembershipUpgrade(custID, Transc_ID);
            System.out.println("PromoteMember(): " + status);

        } catch (Exception ex) {
            System.out.println("PromoteMember(): " + ex.getMessage());

            return (Json.createObjectBuilder().add("success", false).add("error", "ERROR" + ex.getMessage()).build().toString());
        }

        if (status.equals("fail")) {
            return (Json.createObjectBuilder().add("success", false).add("error", "Invalid ID").build().toString());
        }

        if (status.equals("exist")) {
            return (Json.createObjectBuilder().add("success", false).add("error", "Already Used !").build().toString());
        }

        return (Json.createObjectBuilder().add("success", true).add("added", status.split(",")[0]).add("current", status.split(",")[1]).add("promote", status.split(",")[2]).build().toString());

    }
}
