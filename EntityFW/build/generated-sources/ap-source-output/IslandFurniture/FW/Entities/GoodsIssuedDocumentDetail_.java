package IslandFurniture.FW.Entities;

import IslandFurniture.FW.Entities.GoodsIssuedDocument;
import IslandFurniture.FW.Entities.MonthlyStockSupplyReq;
import IslandFurniture.FW.Entities.Stock;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-08-29T14:05:51")
@StaticMetamodel(GoodsIssuedDocumentDetail.class)
public class GoodsIssuedDocumentDetail_ { 

    public static volatile SingularAttribute<GoodsIssuedDocumentDetail, GoodsIssuedDocument> goodsIssuedDocument;
    public static volatile SingularAttribute<GoodsIssuedDocumentDetail, Long> id;
    public static volatile SingularAttribute<GoodsIssuedDocumentDetail, Stock> stock;
    public static volatile SingularAttribute<GoodsIssuedDocumentDetail, MonthlyStockSupplyReq> monthlyStockSupplyReq;

}