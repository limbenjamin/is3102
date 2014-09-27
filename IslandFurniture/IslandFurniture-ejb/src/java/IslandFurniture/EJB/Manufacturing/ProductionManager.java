/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.Manufacturing;

import IslandFurniture.EJB.Entities.BOMDetail;
import IslandFurniture.EJB.Entities.ManufacturingFacility;
import IslandFurniture.EJB.Entities.ProdOrderStatus;
import IslandFurniture.EJB.Entities.ProductionOrder;
import IslandFurniture.EJB.Entities.StockUnit;
import IslandFurniture.EJB.Entities.StorageAreaType;
import IslandFurniture.EJB.Entities.StorageBin;
import IslandFurniture.EJB.Exceptions.InsufficientMaterialException;
import IslandFurniture.EJB.Exceptions.InvalidInputException;
import IslandFurniture.EJB.SupplyChain.ManageInventoryMovementLocal;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.REQUIRED;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author a0101774
 */
@Stateful
public class ProductionManager implements ProductionManagerLocal {
    @EJB
    private ManageInventoryMovementLocal manageInventoryMovement;

    @PersistenceContext
    EntityManager em;
    
    

    @Override
    public ProductionOrder deletePO(Long batchNo) {
        ProductionOrder po;

        try {
            System.out.println("ProductionManager.deletePO()");
            po = em.find(ProductionOrder.class, batchNo);
            System.out.println("Perform deletion");
            em.remove(po);
            return po;
        } catch (Exception ex) {
            System.err.println("Error");
            return null;
        }
    }

    @Override
    public ProductionOrder editPO(Long batchNo, Calendar date, Integer qty) {
        ProductionOrder po;
        try {
            System.out.println("ProductionManager.editPO()");
            System.out.println("Date is " + date.getTime().toString() + ". Quantity is " + qty);
            po = em.find(ProductionOrder.class, batchNo);
            if (po == null) {
                System.out.println("PO is null");
            }
            if (date != null) {
                po.setProdOrderDate(date);
            }
            if (qty != null) {
                po.setQty(qty);
            }

            return po;
        } catch (Exception ex) {
            System.err.println("Error");
            return null;
        }
    }

    @Override
    public ProductionOrder editCompletedQty(Long batchNo, Integer completedQty) {
        ProductionOrder po;
        try {
            System.out.println("ProductionManager.editCompletedQty()");
            System.out.println("Editing batchNo: " + batchNo + ". Changing completedQty to " + completedQty); 
            po = em.find(ProductionOrder.class, batchNo);
            if (completedQty != null) {
                if (completedQty > po.getQty()) {
                    System.out.println("Invalid. Completed Quantity cannot be greater than planned quantity");
                    return null;
                } else {
                    po.setCompletedQty(completedQty);
                    em.persist(po);
                }
            }
            return po;
        } catch (Exception ex) {
            System.err.println("Error");
            return null;
        }
    }

    @Override
    @TransactionAttribute(REQUIRED)
    public ProductionOrder commencePO(Long batchNo, Long binId) throws InvalidInputException, InsufficientMaterialException {
        ProductionOrder po;
        StorageBin sb;
        System.out.println("ProductionManager.commencePO()");

        po = em.find(ProductionOrder.class, batchNo);
        sb = em.find(StorageBin.class, binId);

        if (sb != null && po != null) {
            if (this.checkSufficientMaterials(po, sb)) {
                for (BOMDetail bomd : po.getFurnitureModel().getBom().getBomDetails()) {
                    for (Iterator itr = sb.getStockUnits().iterator(); itr.hasNext();) {
                        StockUnit su = (StockUnit) itr.next();

                        if (su.getStock().equals(bomd.getMaterial())) {
                            su.setQty(su.getQty() - bomd.getQuantity() * po.getQty());

                            if (su.getQty() <= 0) {
                                em.remove(su);

                            }

                            break;
                        }
                    }
                }
                po.setStatus(ProdOrderStatus.STARTED);
            } else {
                throw new InsufficientMaterialException("Insufficient Materials in stated bin");
            }
        } else {
            throw new InvalidInputException("No such Production Order or Storage Bin exists!");
        }

        return po;
    }

    @Override
    public ProductionOrder completePO(Long batchNo, Long binId) throws InvalidInputException {
        ProductionOrder po;
        StorageBin sb;
        System.out.println("ProductionManager.completePO()");
        
        po = em.find(ProductionOrder.class, batchNo);
        sb = em.find(StorageBin.class, binId);
        if(po != null && sb != null){
            manageInventoryMovement.createStockUnit(po.getFurnitureModel(), po.getBatchNo().toString(), (long) po.getQty(), sb);
            po.setStatus(ProdOrderStatus.COMPLETED);
            
            return po;
        } else{
            throw new InvalidInputException("Target Storage Bin does not exist!");
        }
    }

    @Override
    public List<StorageBin> viewProductionBins(ManufacturingFacility mf) {
        Query q = em.createNamedQuery("findStorageBinByAreaType");
        q.setParameter("plant", mf);
        q.setParameter("saType", StorageAreaType.PRODUCTION);

        return (List<StorageBin>) q.getResultList();
    }

    private boolean checkSufficientMaterials(ProductionOrder po, StorageBin bin) {
        for (BOMDetail bomd : po.getFurnitureModel().getBom().getBomDetails()) {
            boolean hasMaterial = false;
            for (StockUnit su : bin.getStockUnits()) {
                if (bomd.getMaterial().equals(su.getStock())) {
                    if (bomd.getQuantity() * po.getQty() > su.getQty()) {
                        return false;
                    } else {
                        hasMaterial = true;
                        break;
                    }
                }
            }
            if (!hasMaterial) {
                return false;
            }
        }
        return true;
    }
}
