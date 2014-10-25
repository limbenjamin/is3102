/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR3.MOBILEAPP;

import IslandFurniture.EJB.CustomerWebService.ManageCatalogueBean;
import IslandFurniture.EJB.OperationalCRM.MobileAppServiceLocal;
import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.Store;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.json.Json;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.json.*;

/**
 *
 * @author James
 */
@Named(value = "customerService")
@Stateless
@Path("cs")
public class CustomerService {

    @EJB
    private MobileAppServiceLocal masl;
    
    @EJB
    private ManageCatalogueBean mcb;

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
            jab.add(Json.createArrayBuilder().add(s.getName()).add(s.getLatitude()).add(s.getLongitude()).add(s.getAddress()).add(s.getCountryOffice().getId()));
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
    public String getCatalogue(@QueryParam("CO") String CO)
    {
        return "";
        //mcb.getStoreFurniture(CO);
    }
    

}
