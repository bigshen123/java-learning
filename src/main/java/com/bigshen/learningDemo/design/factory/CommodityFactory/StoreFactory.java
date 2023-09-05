package com.bigshen.learningDemo.design.factory.CommodityFactory;

import com.bigshen.learningDemo.design.factory.CommodityFactory.impl.CardCommodityService;
import com.bigshen.learningDemo.design.factory.CommodityFactory.impl.CouponCommodityService;
import com.bigshen.learningDemo.design.factory.CommodityFactory.impl.GoodsCommodityService;

/**
 * @author byj
 * @date 2022/10/9
 */
public class StoreFactory {

    public Commodity getCommodityService(Integer commodityType) {
        if (commodityType == null) {
            return null;
        }
        if (commodityType == 1) {
            return new CouponCommodityService();
        }
        if (2 == commodityType) {
            return new GoodsCommodityService();
        }
        if (3 == commodityType) {
            return new CardCommodityService();
        }
        throw new RuntimeException("不存在的商品服务类型");
    }
}
