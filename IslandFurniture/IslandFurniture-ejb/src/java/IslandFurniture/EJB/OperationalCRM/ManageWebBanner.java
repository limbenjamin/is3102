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
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Benjamin
 */
@Stateless
public class ManageWebBanner implements ManageWebBannerLocal {

    @PersistenceContext
    EntityManager em;

    WebBanner webBanner;

    // Function: Create Web Banner
    @Override
    public void createWebBanner(CountryOffice countryOffice, String headerText, String subheaderText, String bodyText, String buttonText, String buttonUrl, Picture picture) {
        WebBanner webBanner = new WebBanner();
        webBanner.setCountryOffice(countryOffice);
        webBanner.setHeaderText(headerText);
        webBanner.setSubheaderText(subheaderText);
        webBanner.setBodyText(bodyText);
        webBanner.setButtonText(buttonText);
        webBanner.setButtonUrl(buttonUrl);
        webBanner.setPicture(picture);
        em.persist(webBanner);
        em.flush();
    }

    // Function: Edit Web Banner
    @Override
    public void editWebBanner(WebBanner updatedWebBanner) {
        webBanner = (WebBanner) em.find(WebBanner.class, updatedWebBanner.getId());
        webBanner.setHeaderText(updatedWebBanner.getHeaderText());
        webBanner.setSubheaderText(updatedWebBanner.getSubheaderText());
        webBanner.setBodyText(updatedWebBanner.getBodyText());
        webBanner.setButtonText(updatedWebBanner.getButtonText());
        webBanner.setButtonUrl(updatedWebBanner.getButtonUrl());
        webBanner.setPicture(updatedWebBanner.getPicture());
        em.merge(webBanner);
        em.flush();
    }

    // Function: Delete  Web Banner
    @Override
    public void deleteWebBanner(WebBanner webBanner) {
        webBanner = (WebBanner) em.find(WebBanner.class, webBanner.getId());
        em.remove(webBanner);
        em.flush();
    }

    //  Function: View list of Web Banner
    @Override
    public List<WebBanner> viewWebBanner(CountryOffice countryOffice) {
        Query q = em.createQuery("SELECT s FROM WebBanner s WHERE s.countryOffice.id=:plantId");
        q.setParameter("plantId", countryOffice.getId());
        return q.getResultList();
    }
}
