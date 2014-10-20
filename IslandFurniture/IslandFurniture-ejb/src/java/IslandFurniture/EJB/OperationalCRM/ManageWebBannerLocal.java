/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.EJB.OperationalCRM;

import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.Picture;
import IslandFurniture.Entities.WebBanner;
import java.util.List;

/**
 *
 * @author KamilulAshraf
 */
public interface ManageWebBannerLocal {

    // Function: Create Web Banner
    void createWebBanner(CountryOffice countryOffice, String headerText, String subheaderText, String bodyText, String buttonText, String buttonUrl, Picture picture);

    // Function: Delete  Web Banner
    void deleteWebBanner(WebBanner webBanner);

    // Function: Edit Web Banner
    void editWebBanner(WebBanner updatedWebBanner);

    //  Function: View list of Web Banner
    List<WebBanner> viewWebBanner(CountryOffice countryOffice);
    
}
