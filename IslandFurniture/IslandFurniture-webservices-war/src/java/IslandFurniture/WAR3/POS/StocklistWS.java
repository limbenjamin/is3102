/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR3.POS;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.EJB.CustomerWebService.ManageCatalogueBeanLocal;
import IslandFurniture.EJB.CustomerWebService.ManageMemberAuthenticationBeanLocal;
import IslandFurniture.EJB.InventoryManagement.ManageStorefrontInventoryLocal;
import IslandFurniture.EJB.Kitchen.MenuManagerLocal;
import IslandFurniture.EJB.Manufacturing.StockManagerLocal;
import IslandFurniture.EJB.OperationalCRM.ManageMarketingBeanLocal;
import IslandFurniture.EJB.OperationalCRM.ManagePOSLocal;
import IslandFurniture.EJB.Purchasing.SupplierManagerLocal;
import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.Customer;
import IslandFurniture.Entities.FurnitureModel;
import IslandFurniture.Entities.FurnitureTransaction;
import IslandFurniture.Entities.FurnitureTransactionDetail;
import IslandFurniture.Entities.MenuItem;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.PromotionCoupon;
import IslandFurniture.Entities.PromotionDetail;
import IslandFurniture.Entities.RestaurantTransaction;
import IslandFurniture.Entities.RestaurantTransactionDetail;
import IslandFurniture.Entities.RetailItem;
import IslandFurniture.Entities.RetailItemTransaction;
import IslandFurniture.Entities.RetailItemTransactionDetail;
import IslandFurniture.Entities.Staff;
import IslandFurniture.Entities.Stock;
import IslandFurniture.Entities.Store;
import IslandFurniture.Entities.Transaction;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonReader;
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
    private List<MenuItem> restaurantList;
    private FurnitureModel furnitureModel;
    private RetailItem retailItem;
    private JsonArrayBuilder builder;
    private MenuItem menuItem;
    private Long transactionId;
    
    @EJB
    ManageUserAccountBeanLocal muabl;
    @EJB
    SupplierManagerLocal ssm;
    @EJB
    ManageMemberAuthenticationBeanLocal mmab;
    @EJB
    ManageCatalogueBeanLocal mcbl;
    @EJB
    StockManagerLocal sm;
    @EJB
    ManageMarketingBeanLocal mmb;
    @EJB
    ManagePOSLocal mpl;
    @EJB
    ManageStorefrontInventoryLocal msfl;
    @EJB
    MenuManagerLocal mml;
    
    @POST
    @Path("furniturelist")
    public String getFurnitureList(@FormParam("cardId") String cardId) {
        System.err.println(cardId);
        Staff staff = muabl.getStaffFromCardId(cardId);
        if (staff == null){
            return "Error";
        }else{
            Store store = (Store) staff.getPlant();
            furnitureList = mcbl.getStoreFurniture(store.getCountryOffice());
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
            retailList = mcbl.getStoreRetailItems(store.getCountryOffice());
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
    @Path("restaurantlist")
    public String getRestaurantList(@FormParam("cardId") String cardId) {
        Staff staff = muabl.getStaffFromCardId(cardId);
        if (staff == null){
            return "Error";
        }else{
            Store store = (Store) staff.getPlant();
            restaurantList = mml.getMenuItemList(store.getCountryOffice());
            builder = Json.createArrayBuilder();
            Iterator<MenuItem> iterator = restaurantList.iterator();
            while(iterator.hasNext()){
                menuItem = iterator.next();
                String id = String.valueOf(menuItem.getId());
                String price = String.valueOf(menuItem.getPrice());
                //String category = String.valueOf(retailItem.);
                builder.add(Json.createObjectBuilder().add("id", id).add("name", menuItem.getName())
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
        PromotionDetail Successful_promotion = null;
        String d_price = "";
        String title = null;
        List<PromotionCoupon> couponList = new ArrayList();
        HashMap<String, Object> hash;
        if (staff == null){
            return "Error";
        }else{
            Customer c;
            if (customerCardId.equals("")){
                c = new Customer();
            }
            else{
                c = mmab.getCustomerFromLoyaltyCardId(customerCardId);
            }
            Stock s = mpl.getStock(Long.parseLong(stock));
            Store store = (Store) staff.getPlant();
            CountryOffice co = store.getCountryOffice();
            if (coupon.equals("") && stockCoupon.equals("null")){
               hash = mmb.getDiscountedPrice(s, store, c);
            }else{
                if (!coupon.equals("")){
                    Long couponId = mmb.decodeCouponID(coupon);
                    PromotionCoupon pc = mmb.getCouponFromID(couponId);
                    couponList.add(pc);
                }
                if (!stockCoupon.equals("null")){
                    Long couponId = mmb.decodeCouponID(stockCoupon);
                    PromotionCoupon pc = mmb.getCouponFromID(couponId);
                    couponList.add(pc);
                }
                hash = mmb.getDiscountedPrice(s, store, c, couponList);
            }
            d_price = String.valueOf(hash.get("D_PRICE"));
            Successful_promotion = (PromotionDetail) hash.get("Successful_promotion");
            if (Successful_promotion == null){
                title = "";
            }else{
                title = Successful_promotion.getPromotionCampaign().getTitle();
            }

        }
        JsonObject object = Json.createObjectBuilder().add("price", d_price)
                .add("promo",title ).build();
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
    
    @POST
    @Path("checkvoucher")
    public String checkVoucher(@FormParam("cardId") String cardId,
                                @FormParam("voucher") String voucherId){
        int value;
        Staff staff = muabl.getStaffFromCardId(cardId);
        if (staff == null){
            return "Error";
        }else{
            value = mpl.getVoucher(voucherId);
        }
        return String.valueOf(value);
    }
    
    @POST
    @Path("checkreceipt")
    public String checkReceipt(@FormParam("cardId") String cardId,
                                @FormParam("receipt") String receiptId){
        int value;
        Staff staff = muabl.getStaffFromCardId(cardId);
        if (staff == null){
            return "Error";
        }else{
            value = mpl.getReceipt(receiptId);
        }
        return String.valueOf(value);
    }
    
    @POST
    @Path("maketransaction")
    public String makeTransaction(@FormParam("cardId") String cardId,
                                    @FormParam("transaction") String transaction,
                                    @FormParam("voucher") String voucher,
                                    @FormParam("receipt") String receipt,
                                    @FormParam("customerCardId") String customerCardId,
                                    @FormParam("storeType") String storeType,
                                    @FormParam("grandTotal") Double grandTotalAmt,
                                    @FormParam("voucherAmt") Double voucherAmt,
                                    @FormParam("receiptAmt") Double receiptAmt
                                    ){
        System.err.println("--------------------------------");
        System.err.println(cardId);
        System.err.println(transaction);
        System.err.println(voucher);
        System.err.println(receipt);
        System.err.println(customerCardId);
        System.err.println(storeType);
        System.err.println(grandTotalAmt);
        System.err.println(voucherAmt);
        System.err.println(receiptAmt);
        System.err.println("--------------------------------");
        Staff staff = muabl.getStaffFromCardId(cardId);
        Customer customer = null;
        Stock stock = null;
        if (staff == null){
            return "Error";
        }else{
            Plant plant = staff.getPlant();
            TimeZone tz = TimeZone.getTimeZone(plant.getTimeZoneID());
            Calendar now=Calendar.getInstance(tz);
            now.setTime(new Date());           
            System.err.println(transaction);
            if (!customerCardId.trim().isEmpty()){
                    customer = mmab.getCustomerFromLoyaltyCardId(customerCardId);
                }
            if (storeType.equals("restaurant")){
                makeRestaurantTransaction(staff, plant, now, transaction, customerCardId, voucher, grandTotalAmt, voucherAmt);
            }
            else{
                FurnitureTransaction ft = new FurnitureTransaction();
                ft.setCreatedBy(staff);
                ft.setStore((Store) plant);
                ft.setCreationTime(now);
                ft.setTransTime(now);
                ft.setGrandTotal(grandTotalAmt);
                ft.setVoucherTotal(voucherAmt);
                ft.setReturnedCreditsUsed(receiptAmt);
                RetailItemTransaction rt = new RetailItemTransaction();
                rt.setCreatedBy(staff);
                rt.setStore((Store) plant);
                rt.setCreationTime(now);
                rt.setTransTime(now);
                rt.setGrandTotal(grandTotalAmt);
                rt.setVoucherTotal(voucherAmt);
                if (!customerCardId.trim().isEmpty()){
                    ft.setMember(customer);
                    rt.setMember(customer);
                }
                long totalPoints = 0;
                List<String> couponExpendedList = new ArrayList();
                JsonReader reader = Json.createReader(new StringReader(transaction));
                JsonArray arr = reader.readArray();
                for (JsonValue jsonValue : arr) {
                    JsonObject jo = (JsonObject) jsonValue;
                    Long id = Long.parseLong(jo.getString("id"));
                    stock = mpl.getStock(id);
                    int qty = Integer.parseInt(jo.getString("qty"));
                    Double price = Double.parseDouble(jo.getString("price"));
                    System.err.println(jo);
                    if (stock instanceof FurnitureModel){
                        transactionId = mpl.persistFT(ft);
                        FurnitureTransactionDetail ftd = new FurnitureTransactionDetail();
                        ftd.setNumClaimed(0);
                        ftd.setNumReturned(0);
                        ftd.setQty(qty);
                        ftd.setUnitPrice(price);
                        FurnitureModel fm = (FurnitureModel) stock;
                        ftd.setFurnitureModel(fm);
                        Long points = fm.getPointsWorth();
                        totalPoints += points;
                        ftd.setUnitPoints(points);
                        ftd.setFurnitureTransaction(ft);
                        ft.getFurnitureTransactionDetails().add(ftd);
                        mpl.persistFTD(ftd);
                        if(!(jo.getString("disc").equals("null")||jo.getString("disc").equals("")))
                            if (!couponExpendedList.contains(jo.getString("disc"))){
                                String couponId = String.valueOf(mmb.decodeCouponID(jo.getString("disc")));
                                mpl.expendCoupon(couponId);
                                couponExpendedList.add(couponId);
                            }
                        if (!receipt.isEmpty()){
                            mpl.linkReceipt(receipt, ft);
                        }
                    }else if (stock instanceof RetailItem){
                        transactionId = mpl.persistRT(rt);
                        RetailItemTransactionDetail rtd = new RetailItemTransactionDetail();
                        rtd.setQty(qty);
                        rtd.setUnitPrice(price);
                        RetailItem ri = (RetailItem) stock;
                        rtd.setRetailItem(ri);
                        Long points = ri.getPointsWorth();
                        totalPoints += points;
                        rtd.setUnitPoints(points);
                        rtd.setRetailItemTransaction(rt);
                        rt.getRetailItemTransactionDetails().add(rtd);
                        mpl.persistRTD(rtd);
                    }
                    msfl.reduceStockfrontInventoryFromTransaction(plant, stock, qty);
                }
                if (!customerCardId.trim().isEmpty()){
                    customer.setCumulativePoints(customer.getCumulativePoints()+ (int) totalPoints);
                    customer.setCurrentPoints(customer.getCurrentPoints()+ (int) totalPoints);
                }
                String vouchers = voucher.substring(1, voucher.length()-1);
                System.err.println(vouchers);
                String[] voucherArr = vouchers.split(",");
                for (String v : voucherArr){
                    if (!v.trim().isEmpty()){
                        mpl.useVoucher(v.trim());
                    }
                }
            }
            
        }
        JsonObject object;
        if(customer != null)
            object = Json.createObjectBuilder().add("transactionId", transactionId).add("points",customer.getCurrentPoints() ).build();
        else
            object = Json.createObjectBuilder().add("transactionId", transactionId).add("points","null" ).build();
        return object.toString();
    }

    private void makeRestaurantTransaction(Staff staff, Plant plant, Calendar now, String transaction, String customerCardId, String voucher, Double grandTotalAmt, Double voucherAmt) {
        RestaurantTransaction rt = new  RestaurantTransaction();
        rt.setCreatedBy(staff);
        rt.setStore((Store) plant);
        rt.setCreationTime(now);
        rt.setTransTime(now);
        rt.setGrandTotal(grandTotalAmt);
        rt.setVoucherTotal(voucherAmt);
        Customer customer = null;
        long totalPoints = 0;
        if (!customerCardId.trim().isEmpty()){
                customer = mmab.getCustomerFromLoyaltyCardId(customerCardId);
                rt.setMember(customer);
            }
        JsonReader reader = Json.createReader(new StringReader(transaction));
        JsonArray arr = reader.readArray();
        for (JsonValue jsonValue : arr) {
            JsonObject jo = (JsonObject) jsonValue;
            Long id = Long.parseLong(jo.getString("id"));
            menuItem = mml.getMenuItem(id);
            int qty = Integer.parseInt(jo.getString("qty"));
            Double price = Double.parseDouble(jo.getString("price"));
            System.err.println(id+"  "+qty);
            transactionId = mpl.persistRST(rt);
            RestaurantTransactionDetail rtd = new RestaurantTransactionDetail();
            rtd.setQty(qty);
            rtd.setUnitPrice(price);
            rtd.setMenuItem(menuItem);
            Long points = menuItem.getPointsWorth();
            totalPoints += points;
            rtd.setUnitPoints(points);
            rtd.setRestaurantTransaction(rt);
            rt.getRestaurantTransactionDetails().add(rtd);
            mpl.persistRSTD(rtd);
            }
        if (!customerCardId.trim().isEmpty()){
            customer.setCumulativePoints(customer.getCumulativePoints()+ (int) totalPoints);
            customer.setCurrentPoints(customer.getCurrentPoints()+ (int) totalPoints);
            //TODO add in ashraff's function checkmembershipupgradeatpos
        }
        String vouchers = voucher.substring(1, voucher.length()-1);
        System.err.println(vouchers);
        String[] voucherArr = vouchers.split(",");
        for (String v : voucherArr){
            if (!v.trim().isEmpty()){
                mpl.useVoucher(v.trim());
            }
        }
    }
    
}
