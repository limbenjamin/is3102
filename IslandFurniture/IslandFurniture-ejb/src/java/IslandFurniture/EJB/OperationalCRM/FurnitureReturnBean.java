/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.OperationalCRM;

import IslandFurniture.Entities.Customer;
import IslandFurniture.Entities.FurnitureTransaction;
import IslandFurniture.Entities.FurnitureTransactionDetail;
import IslandFurniture.Entities.Staff;
import IslandFurniture.Entities.Store;
import IslandFurniture.Exceptions.InvalidInputException;
import static IslandFurniture.StaticClasses.SystemConstants.FURNITURE_RETURN_CUTOFF;
import IslandFurniture.StaticClasses.TimeMethods;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
@Stateless
public class FurnitureReturnBean implements FurnitureReturnBeanLocal {

    @PersistenceContext(unitName = "IslandFurniture")
    private EntityManager em;

    @Override
    public FurnitureTransaction findTransaction(long transId) {
        return em.find(FurnitureTransaction.class, transId);
    }

    @Override
    public boolean checkValid(FurnitureTransaction furnTrans) {
        Calendar validCal = TimeMethods.getPlantCurrTime(furnTrans.getStore());
        validCal.add(Calendar.DATE, FURNITURE_RETURN_CUTOFF * -1);
        if (furnTrans.getTransTime().before(validCal)) {
            System.out.println("Transaction " + furnTrans.getId() + " is not valid for return");
            return false;
        } else {
            System.out.println("Transaction " + furnTrans.getId() + " is valid for return");
            return true;
        }
    }

    @Override
    public void saveReturns(FurnitureTransaction furnTrans, Staff staff) throws InvalidInputException {
        FurnitureTransactionDetail localTransDetail;
        int pointsDeducted = 0;

        for (FurnitureTransactionDetail detail : furnTrans.getFurnitureTransactionDetails()) {
            localTransDetail = em.find(FurnitureTransactionDetail.class, detail.getId());
            if (detail.getNumReturned() < localTransDetail.getNumReturned()) {
                throw new InvalidInputException("Returned quantity must not be less than previous returned quantity");
            }
            if (detail.getNumReturned() > detail.getQty()) {
                throw new InvalidInputException("Returned quantity must not be more than total purchased");
            }
        }

        for (FurnitureTransactionDetail detail : furnTrans.getFurnitureTransactionDetails()) {
            localTransDetail = em.find(FurnitureTransactionDetail.class, detail.getId());
            pointsDeducted += (detail.getNumReturned() - localTransDetail.getNumReturned()) * detail.getUnitPoints();

            FurnitureTransaction savedTrans = em.merge(detail).getFurnitureTransaction();
            savedTrans.setLastModBy(staff);
        }

        Customer member = furnTrans.getMember();
        if (member != null) {
            member.setCumulativePoints(member.getCumulativePoints() - pointsDeducted);
            member.setCurrentPoints(member.getCurrentPoints() - pointsDeducted);
            em.merge(member);
        }
    }

    @Override
    public void saveClaims(FurnitureTransaction furnTrans, Staff staff) throws InvalidInputException {
        FurnitureTransactionDetail localTransDetail;
        FurnitureTransaction claimingTrans = new FurnitureTransaction();
        List<FurnitureTransactionDetail> claimDetails = new ArrayList();
        double grandTotal = 0.0;
        int pointsAdded = 0;

        for (FurnitureTransactionDetail detail : furnTrans.getFurnitureTransactionDetails()) {
            localTransDetail = em.find(FurnitureTransactionDetail.class, detail.getId());
            if (detail.getNumClaimed() < localTransDetail.getNumClaimed()) {
                throw new InvalidInputException("Claimed quantity must not be less than or equal to previous claimed quantity");
            }
            if (detail.getNumReturned() < detail.getNumClaimed()) {
                throw new InvalidInputException("Claimed quantity must not be more than returned quantity");
            }
        }

        for (FurnitureTransactionDetail detail : furnTrans.getFurnitureTransactionDetails()) {
            localTransDetail = em.find(FurnitureTransactionDetail.class, detail.getId());

            if (detail.getNumClaimed() > localTransDetail.getNumClaimed()) {
                FurnitureTransactionDetail claimDetail = new FurnitureTransactionDetail();
                claimDetail.setFurnitureModel(detail.getFurnitureModel());
                claimDetail.setFurnitureTransaction(claimingTrans);
                claimDetail.setQty(detail.getNumClaimed() - localTransDetail.getNumClaimed());
                claimDetail.setUnitPrice(detail.getUnitPrice());
                claimDetail.setUnitPoints(detail.getUnitPoints());
                claimDetail.setNumClaimed(0);
                claimDetail.setNumReturned(0);

                pointsAdded += claimDetail.getTotalPoints();
                grandTotal += claimDetail.getSubtotal();
                claimDetails.add(claimDetail);
                em.merge(detail);
            }
        }

        if (!claimDetails.isEmpty()) {
            Customer member = furnTrans.getMember();
            if (member != null) {
                member.setCumulativePoints(member.getCumulativePoints() + pointsAdded);
                member.setCurrentPoints(member.getCurrentPoints() + pointsAdded);
                member = em.merge(member);
                claimingTrans.setMember(member);
            }

            claimingTrans.setCreatedBy(staff);
            claimingTrans.setFurnitureTransactionDetails(claimDetails);
            claimingTrans.setTransTime(TimeMethods.getPlantCurrTime(staff.getPlant()));
            claimingTrans.setGrandTotal(Math.floor(grandTotal * 100) / 100);
            claimingTrans.setReturnedTrans(furnTrans);
            claimingTrans.setReturnedCreditsUsed(Math.floor(grandTotal * 100) / 100);
            claimingTrans.setStore((Store) staff.getPlant());
            claimingTrans.setVoucherTotal(0.0);

            em.persist(claimingTrans);
        }
    }

    @Override
    public boolean checkCanIssue(FurnitureTransaction furnTrans) {
        for (FurnitureTransactionDetail detail : furnTrans.getFurnitureTransactionDetails()) {
            if (detail.getNumReturned() > detail.getNumClaimed()) {
                return true;
            }
        }

        return false;
    }

}
