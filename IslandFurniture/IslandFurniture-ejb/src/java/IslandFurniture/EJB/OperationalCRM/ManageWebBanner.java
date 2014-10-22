/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.OperationalCRM;

import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.WebBanner;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Kamilul Ashraf
 */
@Stateless
public class ManageWebBanner implements ManageWebBannerLocal {

    @PersistenceContext
    EntityManager em;

    WebBanner webBanner;

    // Function: Create Web Banner
    @Override
    public WebBanner createWebBanner(Plant plant) {
        WebBanner webBanner = new WebBanner();
        webBanner.setCountryOffice((CountryOffice) plant);
        em.persist(webBanner);
        em.flush();
        return webBanner;
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
    public List<WebBanner> viewWebBanner(Plant plant) {
        Query q = em.createQuery("SELECT s FROM WebBanner s WHERE s.countryOffice.id=:plantId");
        q.setParameter("plantId", plant.getId());
        return q.getResultList();
    }

    //  Function: Return Web Banner entity
    @Override
    public WebBanner getWebBanner(Long id) {
        webBanner = (WebBanner) em.find(WebBanner.class, id);
        return webBanner;
    }
}
