package IslandFurniture.FW.Entities;

import IslandFurniture.FW.Entities.GoodsIssuedDocumentDetail;
import IslandFurniture.FW.Entities.Store;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-08-29T14:05:51")
@StaticMetamodel(GoodsIssuedDocument.class)
public class GoodsIssuedDocument_ { 

    public static volatile ListAttribute<GoodsIssuedDocument, GoodsIssuedDocumentDetail> goodsIssuedDocumentDetails;
    public static volatile SingularAttribute<GoodsIssuedDocument, Long> id;
    public static volatile SingularAttribute<GoodsIssuedDocument, Store> store;

}