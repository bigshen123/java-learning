package com.bigshen.learningDemo.design.factory.CommodityFactory.store;

import java.util.Map;

/**
 * @author byj
 * @date 2022/10/9
 */
public interface Commodity {
    /**
     * 发商品卡
     * @param uId
     * @param commodityId
     * @param bizId
     * @param extMap
     * @throws Exception
     */
    void sendCommodity(String uId, String commodityId, String bizId, Map<String, String> extMap) throws Exception;
}
