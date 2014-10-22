/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.OperationalCRM;

import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.RedeemableItem;
import IslandFurniture.Entities.Voucher;
import java.util.Calendar;
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
public class ManageRedeemableItem {

    @PersistenceContext
    EntityManager em;

    Voucher voucher;

//    // Function: Create Redeemable Item
//    @Override
//    public void createRedeemableItem(CountryOffice countryOffice, int cashValue, Calendar expiryDate) {
//        Voucher voucher = new Voucher();
//        voucher.setCountryOffice(countryOffice);
//        voucher.setCashValue(Integer.MIN_VALUE);
//        voucher.setExpiryDate(null);
//        
//        
//        
//        redeemableItem.setCountryOffice(countryOffice);
//        redeemableItem.setHeaderText(headerText);
//        redeemableItem.setSubheaderText(subheaderText);
//        redeemableItem.setBodyText(bodyText);
//        redeemableItem.setButtonText(buttonText);
//        redeemableItem.setButtonUrl(buttonUrl);
//        redeemableItem.setPicture(picture);
//        em.persist(redeemableItem);
//        em.flush();
//    }
//
//    // Function: Edit Redeemable Item
//    @Override
//    public void editRedeemableItem(RedeemableItem updatedRedeemableItem) {
//        redeemableItem = (RedeemableItem) em.find(RedeemableItem.class, updatedRedeemableItem.getId());
//        redeemableItem.setHeaderText(updatedRedeemableItem.getHeaderText());
//        redeemableItem.setSubheaderText(updatedRedeemableItem.getSubheaderText());
//        redeemableItem.setBodyText(updatedRedeemableItem.getBodyText());
//        redeemableItem.setButtonText(updatedRedeemableItem.getButtonText());
//        redeemableItem.setButtonUrl(updatedRedeemableItem.getButtonUrl());
//        redeemableItem.setPicture(updatedRedeemableItem.getPicture());
//        em.merge(redeemableItem);
//        em.flush();
//    }
//
//    // Function: Delete  Redeemable Item
//    @Override
//    public void deleteRedeemableItem(RedeemableItem redeemableItem) {
//        redeemableItem = (RedeemableItem) em.find(RedeemableItem.class, redeemableItem.getId());
//        em.remove(redeemableItem);
//        em.flush();
//    }
//
//    //  Function: View list of Redeemable Item
//    @Override
//    public List<RedeemableItem> viewRedeemableItem(CountryOffice countryOffice) {
//        Query q = em.createQuery("SELECT s FROM RedeemableItem s WHERE s.countryOffice.id=:plantId");
//        q.setParameter("plantId", countryOffice.getId());
//        return q.getResultList();
//    }
}
