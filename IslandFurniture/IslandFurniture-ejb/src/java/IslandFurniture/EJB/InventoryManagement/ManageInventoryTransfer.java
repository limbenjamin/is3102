/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.EJB.InventoryManagement;

import IslandFurniture.EJB.CommonInfrastructure.ManageNotificationsBeanLocal;
import IslandFurniture.EJB.ITManagement.ManagePrivilegesBeanLocal;
import IslandFurniture.Entities.ExternalTransferOrder;
import IslandFurniture.Entities.ExternalTransferOrderDetail;
import IslandFurniture.Entities.GoodsIssuedDocument;
import IslandFurniture.Entities.Plant;
import IslandFurniture.Entities.ReplenishmentTransferOrder;
import IslandFurniture.Entities.Stock;
import IslandFurniture.Entities.StockUnit;
import IslandFurniture.Entities.StorageBin;
import IslandFurniture.Entities.StorefrontInventory;
import IslandFurniture.Entities.StorefrontInventoryPK;
import IslandFurniture.Enums.TransferOrderStatus;
import IslandFurniture.StaticClasses.SendSMSBean;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * @author Kamilul Ashraf
 *
 */
@Stateful
public class ManageInventoryTransfer implements ManageInventoryTransferLocal, ManageInventoryTransferRemote {

    @PersistenceContext
    EntityManager em;

    private Stock stock;
    private StockUnit stockUnit;
    private StorageBin storageBin;
    private ReplenishmentTransferOrder replenishmentTransferOrder;
    private ExternalTransferOrder externalTransferOrder;
    private ExternalTransferOrderDetail externalTransferOrderDetail;
    private StorefrontInventory storefrontInventory;
    private StorefrontInventoryPK storefrontInventoryPK;

    @EJB
    private ManageNotificationsBeanLocal manageNotificationsBean;
    @EJB
    private ManagePrivilegesBeanLocal managePrivilegesBean;

    @Override
    public StockUnit getStockUnit(Long stockUnitId) {
        stockUnit = (StockUnit) em.find(StockUnit.class, stockUnitId);
        return stockUnit;
    }

    @Override
    public Stock getStock(Long stockId) {
        stock = (Stock) em.find(Stock.class, stockId);
        return stock;
    }

    @Override
    public ExternalTransferOrder getExternalTransferOrder(Long id) {
        externalTransferOrder = (ExternalTransferOrder) em.find(ExternalTransferOrder.class, id);
        return externalTransferOrder;
    }

    @Override
    public ReplenishmentTransferOrder getReplenishmentTransferOrder(Long id) {
        replenishmentTransferOrder = (ReplenishmentTransferOrder) em.find(ReplenishmentTransferOrder.class, id);
        return replenishmentTransferOrder;
    }

    @Override
    public void updateBatchNumber(Long id, String batchNumber) {
        stockUnit = getStockUnit(id);
        stockUnit.setBatchNo(batchNumber);
        em.merge(stockUnit);
        em.flush();
    }

    @Override
    public void createStockUnit(Stock stock, String batchNo, Long quantity, StorageBin storageBin) {
        stockUnit = new StockUnit();
        stockUnit.setStock(stock);
        stockUnit.setBatchNo(batchNo);
        stockUnit.setQty(quantity);
        stockUnit.setLocation(storageBin);
        stockUnit.setAvailable(true);
        em.persist(stockUnit);
        em.flush();
    }

    @Override
    public void createStockUnit2(Stock stock, Long stockUnitId, String batchNo, Long quantity, StorageBin storageBin, GoodsIssuedDocument gid) {
        stockUnit = new StockUnit();
        stockUnit.setStock(stock);
        stockUnit.setBatchNo(batchNo);
        stockUnit.setQty(quantity);
        stockUnit.setLocation(storageBin);
        stockUnit.setAvailable(false);
        stockUnit.setCommitStockUnitId(stockUnitId);
        stockUnit.setGoodsIssuedDocument(gid);
        em.persist(stockUnit);
        em.flush();
    }

    @Override
    public void createStockUnitMovement1(Stock stock, Long stockUnitId, String batchNo, Long quantity, StorageBin storageBin, StorageBin newStorageBin) {
        stockUnit = new StockUnit();
        stockUnit.setStock(stock);
        stockUnit.setBatchNo(batchNo);
        stockUnit.setQty(quantity);
        stockUnit.setLocation(storageBin);
        stockUnit.setPendingLocation(newStorageBin);
        stockUnit.setAvailable(false);
        stockUnit.setCommitStockUnitId(stockUnitId);
        stockUnit.setGoodsIssuedDocument(null);
        em.persist(stockUnit);
        em.flush();
    }

    @Override
    public List<StockUnit> viewStockUnitMovement(Plant plant, Stock stock) {
        Query q = em.createQuery("SELECT s FROM StockUnit s WHERE s.location.storageArea.plant.id=:plantId AND s.stock.id=:stockId AND s.available=FALSE AND s.goodsIssuedDocument=NULL");
        q.setParameter("plantId", plant.getId());
        q.setParameter("stockId", stock.getId());
        return q.getResultList();
    }

    @Override
    public List<StockUnit> viewStockUnitMovementbyStorageBin(StorageBin storageBin) {
        Query q = em.createQuery("SELECT s FROM StockUnit s WHERE s.location.id=:id AND s.available=FALSE AND s.goodsIssuedDocument=NULL");
        q.setParameter("id", storageBin.getId());
        return q.getResultList();
    }

    @Override
    public List<StockUnit> viewStockUnitMovementAll(Plant plant) {
        Query q = em.createQuery("SELECT s FROM StockUnit s WHERE s.location.storageArea.plant.id=:plantId AND s.available=FALSE AND s.goodsIssuedDocument=NULL");
        q.setParameter("plantId", plant.getId());
        return q.getResultList();
    }

    @Override
    public List<StockUnit> viewStockUnitMovementCheck(StorageBin storageBin, Stock stock, String batchNo) throws Exception {
        Query q = em.createQuery("SELECT s FROM StockUnit s WHERE s.location.id=:storageBinId AND s.stock.id=:stockId AND s.batchNo=:batchNo AND s.available=TRUE");
        q.setParameter("storageBinId", storageBin.getId());
        q.setParameter("stockId", stock.getId());
        q.setParameter("batchNo", batchNo);
        return q.getResultList();
    }

    @Override
    public void confirmStockUnitMovement(Long stockUnitId) {
        stockUnit = getStockUnit(stockUnitId);
        stockUnit.setLocation(stockUnit.getPendingLocation());
        stockUnit.setPendingLocation(null);
        stockUnit.setAvailable(true);
        em.merge(stockUnit);
        em.flush();
    }

    @Override
    public void editStockUnitLocationDefault(Long stockUnitId, Long storageBinId) {
        stockUnit = getStockUnit(stockUnitId);
        storageBin = (StorageBin) em.find(StorageBin.class, storageBinId);
        stockUnit.setLocation(storageBin);
        em.merge(stockUnit);
        em.flush();
    }

    @Override
    public void deleteStockUnit(Long stockUnitId) {
        stockUnit = getStockUnit(stockUnitId);
        em.remove(stockUnit);
        em.flush();
    }

    @Override
    public void editStockUnitQuantity(Long stockUnitId, Long qty) {
        stockUnit = getStockUnit(stockUnitId);
        stockUnit.setQty(qty);
        em.merge(stockUnit);
        em.flush();
    }

    @Override
    public List<StockUnit> viewStockUnit(Plant plant) {
        Query q = em.createQuery("SELECT s FROM StockUnit s WHERE s.location.storageArea.plant.id=" + plant.getId());
        return q.getResultList();
    }

    @Override
    public List<StockUnit> viewStockUnitDistinctName(Plant plant) {
        Query q = em.createQuery("SELECT s FROM StockUnit s WHERE s.location.storageArea.plant.id=:plantId GROUP BY s.stock.name");
        q.setParameter("plantId", plant.getId());
        return q.getResultList();
    }

    @Override
    public List<StorageBin> viewStorageBin(Plant plant) {
        Query q = em.createQuery("SELECT s FROM StorageBin s WHERE s.storageArea.plant.id=" + plant.getId());
        return q.getResultList();
    }

    @Override
    public List<StockUnit> viewStockUnitByStorageBin(Plant plant, StorageBin storageBin) {
        Query q = em.createQuery("SELECT s FROM StockUnit s WHERE (s.location.storageArea.plant.id=:plantId AND s.location.id=:storageBinId AND s.available=TRUE)");
        q.setParameter("plantId", plant.getId());
        q.setParameter("storageBinId", storageBin.getId());
        return q.getResultList();
    }

    @Override
    public List<StockUnit> viewStockUnitByStockId(Long id, Long storageBinId) {
        Query q = em.createQuery("SELECT s FROM StockUnit s WHERE s.stock.id=:id AND s.location.id=:storageBinId");
        q.setParameter("id", id);
        q.setParameter("storageBinId", storageBinId);
        return q.getResultList();
    }

    @Override
    public List<Stock> viewStock() {
        Query q = em.createQuery("SELECT s " + "FROM Stock s");
        return q.getResultList();
    }

    @Override
    public List<StockUnit> viewStockUnitsOfAStock(Plant plant, Stock stock) {
        Query q = em.createQuery("SELECT s FROM StockUnit s WHERE (s.location.storageArea.plant.id=:plantId AND s.stock.id=:stockId AND s.available=TRUE)");
        q.setParameter("plantId", plant.getId());
        q.setParameter("stockId", stock.getId());
        return q.getResultList();
    }

//  Function: To display list of Replenishment Transfer Order (Requested)    
    @Override
    public List<ReplenishmentTransferOrder> viewReplenishmentTransferOrderRequested(Plant plant) {
        Query q = em.createQuery("SELECT s FROM ReplenishmentTransferOrder s WHERE s.requestingPlant.id=:plantId AND s.status=:status");
        q.setParameter("plantId", plant.getId());
        q.setParameter("status", TransferOrderStatus.REQUESTED);
        return q.getResultList();
    }

    //  Function: To display list of Replenishment Transfer Order (Requested)    
    @Override
    public ReplenishmentTransferOrder updateReplenishmentTransferOrder(Plant plant, Stock stock) {
        Query q = em.createQuery("SELECT s FROM ReplenishmentTransferOrder s WHERE s.requestingPlant.id=:plantId AND s.status=:status AND s.stock.id=:stockId");
        q.setParameter("plantId", plant.getId());
        q.setParameter("stockId", stock.getId());
        q.setParameter("status", TransferOrderStatus.REQUESTED);
        return (ReplenishmentTransferOrder) q.getSingleResult();
    }

//  Function: To display list of Replenishment Transfer Order (Requested) -- For a particular Stock  
    @Override
    public List<ReplenishmentTransferOrder> viewReplenishmentTransferOrderRequestedForAParticularStock(Plant plant, Stock stock) {
        Query q = em.createQuery("SELECT s FROM ReplenishmentTransferOrder s WHERE s.requestingPlant.id=:plantId AND s.status=:status AND s.stock.id=:stockId");
        q.setParameter("plantId", plant.getId());
        q.setParameter("stockId", stock.getId());
        q.setParameter("status", TransferOrderStatus.REQUESTED);
        return q.getResultList();
    }

//  Function: To display list of Replenishment Transfer Order (Fulfilled)    
    @Override
    public List<ReplenishmentTransferOrder> viewReplenishmentTransferOrderFulfilled(Plant plant) {
        Query q = em.createQuery("SELECT s FROM ReplenishmentTransferOrder s WHERE s.requestingPlant.id=:plantId AND s.status=:status");
        q.setParameter("plantId", plant.getId());
        q.setParameter("status", TransferOrderStatus.FULFILLED);
        return q.getResultList();
    }

//  Function: To create a Replenishment Transfer Order (Status: Requested)
    @Override
    public void createReplenishmentTransferOrder(Plant plant, Stock stock, Integer quantity) {
        replenishmentTransferOrder = new ReplenishmentTransferOrder();
        replenishmentTransferOrder.setRequestingPlant(plant);
        replenishmentTransferOrder.setStock(stock);
        replenishmentTransferOrder.setQty(quantity);
        replenishmentTransferOrder.setStatus(TransferOrderStatus.REQUESTED);
        em.persist(replenishmentTransferOrder);
        em.flush();

        storefrontInventoryPK = new StorefrontInventoryPK(plant.getId(), stock.getId());
        storefrontInventory = (StorefrontInventory) em.find(StorefrontInventory.class, storefrontInventoryPK);

        manageNotificationsBean.createNewNotificationForPrivilegeFromPlant("Pending Replenishment", "New replenishment transfer order", "/inventorymgt/inventorytransfer_replenish.xhtml", "Fulfill Replenishment", managePrivilegesBean.getPrivilegeFromName("Inventory Replenishment"), plant);
        SendSMSBean.sendSMSByPrivilegeFromPlant("New Action: Pending Replenishment for " + stock.getName() + " at " + storefrontInventory.getLocationInStore().getName(), managePrivilegesBean.getPrivilegeFromName("Inventory Replenishment"), plant);
    }

//  Function: To delete a Replenishment Transfer Order  
    @Override
    public void deleteReplenishmentTransferOrder(Long id) {
        replenishmentTransferOrder = (ReplenishmentTransferOrder) em.find(ReplenishmentTransferOrder.class, id);
        em.remove(replenishmentTransferOrder);
        em.flush();
    }

//  Function: To edit the Quantity of a Replenishment Transfer Order (Requested)
    @Override
    public void editReplenishmentTransferOrderQuantity(Long id, Integer qty) {
        replenishmentTransferOrder = (ReplenishmentTransferOrder) em.find(ReplenishmentTransferOrder.class, id);
        replenishmentTransferOrder.setQty(qty);
        em.merge(replenishmentTransferOrder);
        em.flush();
    }

    //  Function: To check Replenishment Transfer Order (Requested) already exists  
    @Override
    public boolean checkIfReplenishmentTransferOrderforStockDoNotExists(Plant plant, Stock stock) {
        Query q = em.createQuery("SELECT s FROM ReplenishmentTransferOrder s WHERE (s.requestingPlant.id=:plantId AND s.stock.id=:stockId AND s.status=:status)");
        q.setParameter("plantId", plant.getId());
        q.setParameter("stockId", stock.getId());
        q.setParameter("status", TransferOrderStatus.REQUESTED);
        return q.getResultList().isEmpty();
    }

//  Function: To create a External Transfer Order (Status: Requested)
    @Override
    public ExternalTransferOrder createExternalTransferOrder(Plant plant, Calendar cal) {
        externalTransferOrder = new ExternalTransferOrder();
        externalTransferOrder.setRequestingPlant(plant);
        externalTransferOrder.setStatus(TransferOrderStatus.REQUESTED_PENDING);
        externalTransferOrder.setTransferDate(cal);
        em.persist(externalTransferOrder);
        em.flush();
        return externalTransferOrder;
    }

    //  Function: To create a External Transfer Order (Status: Requested) from Manufacturing
    @Override
    public ExternalTransferOrder createExternalTransferOrderFromManufacturing(Plant fulfillingPlant, Plant requestingPlant) {
        externalTransferOrder = new ExternalTransferOrder();
        externalTransferOrder.setRequestingPlant(requestingPlant);
        externalTransferOrder.setFulfillingPlant(fulfillingPlant);
        externalTransferOrder.setStatus(TransferOrderStatus.REQUESTED);
        em.persist(externalTransferOrder);
        em.flush();
        em.refresh(externalTransferOrder);
        return externalTransferOrder;
    }

    //  Function: To delete a External Transfer Order  
    @Override
    public void deleteExternaTransferOrder(Long id) {
        externalTransferOrder = (ExternalTransferOrder) em.find(ExternalTransferOrder.class, id);
        em.remove(externalTransferOrder);
        em.flush();
    }

    //  Function: To delete a External Transfer Order Detail
    @Override
    public void deleteExternaTransferOrderDetail(ExternalTransferOrderDetail externalTransferOrderDetail) {
        externalTransferOrderDetail = (ExternalTransferOrderDetail) em.find(ExternalTransferOrderDetail.class, externalTransferOrderDetail.getId());
        em.remove(externalTransferOrderDetail);
        em.flush();
    }

    //  Function: To edit the Quantity of a External Transfer Order Detail (Requested)
    @Override
    public void editExternalTransferOrderDetailQuantity(Long id, Integer qty) {
        externalTransferOrderDetail = (ExternalTransferOrderDetail) em.find(ExternalTransferOrderDetail.class, id);
        externalTransferOrderDetail.setQty(qty);
        em.merge(externalTransferOrderDetail);
        em.flush();
    }

//  Function: To display list of External Transfer Order (Requested) Pending   
    @Override
    public List<ExternalTransferOrder> viewExternalTransferOrderRequestedPending(Plant plant) {
        Query q = em.createQuery("SELECT s FROM ExternalTransferOrder s WHERE s.requestingPlant.id=:plantId AND s.status=:status");
        q.setParameter("plantId", plant.getId());
        q.setParameter("status", TransferOrderStatus.REQUESTED_PENDING);
        return q.getResultList();
    }

    //  Function: To display list of External Transfer Order (Requested) Posted   
    @Override
    public List<ExternalTransferOrder> viewExternalTransferOrderRequestedPosted(Plant plant) {
        Query q = em.createQuery("SELECT s FROM ExternalTransferOrder s WHERE s.requestingPlant.id=:plantId AND s.status=:status");
        q.setParameter("plantId", plant.getId());
        q.setParameter("status", TransferOrderStatus.REQUESTED);
        return q.getResultList();
    }

//  Need to edit this one!
//  Function: To display list of External Transfer Order (Requested) -- For a particular Stock  
    @Override
    public List<ExternalTransferOrder> viewExternalTransferOrderDetailRequestedForAParticularStock(Plant plant, Stock stock) {
        Query q = em.createQuery("SELECT s FROM ExternalTransferOrder s WHERE s.requestingPlant.id=:plantId AND s.status=:status AND s.stock.id=:stockId");
        q.setParameter("plantId", plant.getId());
        q.setParameter("stockId", stock.getId());
        q.setParameter("status", TransferOrderStatus.REQUESTED);
        return q.getResultList();
    }

//  Function: To display list of External Transfer Order (Fulfilled) Pending   
    @Override
    public List<ExternalTransferOrder> viewExternalTransferOrderFulfilledPending(Plant plant) {
        Query q = em.createQuery("SELECT s FROM ExternalTransferOrder s WHERE s.fulfillingPlant.id=:plantId AND s.status=:status");
        q.setParameter("plantId", plant.getId());
        q.setParameter("status", TransferOrderStatus.REQUESTED);
        return q.getResultList();
    }

    //  Function: To display list of External Transfer Order (Fulfilled) Posted   
    @Override
    public List<ExternalTransferOrder> viewExternalTransferOrderFulfilledPosted(Plant plant) {
        Query q = em.createQuery("SELECT s FROM ExternalTransferOrder s WHERE s.fulfillingPlant.id=:plantId AND s.status=:status");
        q.setParameter("plantId", plant.getId());
        q.setParameter("status", TransferOrderStatus.FULFILLED);
        return q.getResultList();
    }

    //  Function: To display list of External Transfer Order (Fulfilled) Posted   
    @Override
    public List<ExternalTransferOrder> viewExternalTransferOrderFulfilledPostedFromRequesting(Plant plant) {
        Query q = em.createQuery("SELECT s FROM ExternalTransferOrder s WHERE s.requestingPlant.id=:plantId AND s.status=:status");
        q.setParameter("plantId", plant.getId());
        q.setParameter("status", TransferOrderStatus.FULFILLED);
        return q.getResultList();
    }

    //  Function: To display list of External Transfer Order Details in a External Transfer Order    
    @Override
    public List<ExternalTransferOrderDetail> viewExternalTransferOrderDetail(Long id) {
        Query q = em.createQuery("SELECT s FROM ExternalTransferOrderDetail s WHERE s.extTransOrder.id=:id");
        q.setParameter("id", id);
        return q.getResultList();
    }

    //  Function: To edit/add the quantity of a External Transfer Order Detail, when a same Stock is added to the External Transfer Order
    @Override
    public void editExternalTransferOrderDetailQtyWhenSameStockIdIsAdded(Long id, Integer qty) {
        externalTransferOrderDetail = (ExternalTransferOrderDetail) em.find(ExternalTransferOrderDetail.class, id);
        externalTransferOrderDetail.setQty(qty);
        em.merge(externalTransferOrderDetail);
        em.flush();
        em.refresh(externalTransferOrderDetail);
    }

//  Function: To create External Transfer Order Detail    
    @Override
    public void createExternalTransferOrderDetail(Long id, Long stockId, Integer quantity) {
        externalTransferOrderDetail = new ExternalTransferOrderDetail();
        externalTransferOrder = (ExternalTransferOrder) em.find(ExternalTransferOrder.class, id);
        stock = (Stock) em.find(Stock.class, stockId);
        externalTransferOrderDetail.setExtTransOrder(externalTransferOrder);
        externalTransferOrderDetail.setStock(stock);
        externalTransferOrderDetail.setQty(quantity);
        externalTransferOrder.getExtTransOrderDetails().add(externalTransferOrderDetail);
        em.persist(externalTransferOrderDetail);
        em.merge(externalTransferOrder);
        em.flush();
    }

//  Function: To edit External Transfer Order Detail    
    @Override
    public void editExternalTransferOrderDetail(ExternalTransferOrderDetail externalTransferOrderDetail, Long stockId) {
        stock = (Stock) em.find(Stock.class, stockId);
        externalTransferOrderDetail.setStock(stock);
        externalTransferOrderDetail.setQty(externalTransferOrderDetail.getQty());
        em.merge(externalTransferOrderDetail);
        em.flush();
    }

//  Function: To edit External Transfer Order  
    @Override
    public void editExternalTransferOrder(ExternalTransferOrder externalTransferOrder, Calendar cal, Plant plant) {
        externalTransferOrder = (ExternalTransferOrder) em.find(ExternalTransferOrder.class, externalTransferOrder.getId());
        externalTransferOrder.setTransferDate(cal);
        externalTransferOrder.setFulfillingPlant(plant);
        em.merge(externalTransferOrder);
        em.flush();
    }

    //  Function: To edit External Transfer Order Request to Posted  
    @Override
    public void editExternalTransferOrderStatusToRequestPosted(ExternalTransferOrder externalTransferOrder) {
        externalTransferOrder = (ExternalTransferOrder) em.find(ExternalTransferOrder.class, externalTransferOrder.getId());
        externalTransferOrder.setStatus(TransferOrderStatus.REQUESTED);
        em.merge(externalTransferOrder);
        em.flush();

        manageNotificationsBean.createNewNotificationForPrivilegeFromPlant("Pending External Transfer", "New external transfer order", "/inventorymgt/inventorytransfer_external.xhtml", "Fulfill External Transfer Order", managePrivilegesBean.getPrivilegeFromName("External Inventory Transfer"), externalTransferOrder.getFulfillingPlant());
    }

//  Function: To edit External Transfer Order Request to Fulfilled 
    @Override
    public void editExternalTransferOrderStatusToRequestFulfilled(ExternalTransferOrder externalTransferOrder, Plant plant) {
        externalTransferOrder = (ExternalTransferOrder) em.find(ExternalTransferOrder.class, externalTransferOrder.getId());
        externalTransferOrder.setFulfillingPlant(plant);
        externalTransferOrder.setStatus(TransferOrderStatus.FULFILLED);
        em.merge(externalTransferOrder);
        em.flush();
    }

    //  Function: To edit Replenishment Transfer Order Request to Fulfilled 
    @Override
    public void editReplenishmentTransferOrderStatusToRequestFulfilled(ReplenishmentTransferOrder replenishmentTransferOrder) {
        replenishmentTransferOrder = (ReplenishmentTransferOrder) em.find(ReplenishmentTransferOrder.class, replenishmentTransferOrder.getId());
        replenishmentTransferOrder.setStatus(TransferOrderStatus.FULFILLED);
        em.merge(replenishmentTransferOrder);
        em.flush();
    }

    //  Function: To display list of External Transfer Order for Plant  
    @Override
    public List<ExternalTransferOrder> viewAllExternalTransferOrderRequesting(Plant plant) {
        Query q = em.createQuery("SELECT s FROM ExternalTransferOrder s WHERE s.requestingPlant.id=:plantId");
        q.setParameter("plantId", plant.getId());
        return q.getResultList();
    }

    //  Function: To display list of External Transfer Order for Plant  
    @Override
    public List<ExternalTransferOrder> viewAllExternalTransferOrderFulFilling(Plant plant) {
        Query q = em.createQuery("SELECT s FROM ExternalTransferOrder s WHERE s.fulfillingPlant.id=:plantId");
        q.setParameter("plantId", plant.getId());
        return q.getResultList();
    }

//        @Override
//    public List<FurnitureModel> viewFurnitureModel(CountryOffice countryOffice) {
//        Query q1 = em.createQuery("SELECT f FROM FurnitureModel f");
//        
//        Query q2 = em.createQuery("SELECT s FROM stocksupplied s WHERE s.");
//        
//        q2.setParameter("countryOffice", countryOffice.getId());
//        return q2.getResultList();
//    }
}
