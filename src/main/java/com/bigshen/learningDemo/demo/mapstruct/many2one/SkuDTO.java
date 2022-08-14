package com.bigshen.learningDemo.demo.mapstruct.many2one;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName SkuDTO
 * @Description: TODO
 * @Author BYJ
 * @Date 2020/9/15
 * @Version V1.0
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SkuDTO {
    private Long skuId;
    private String skuCode;
    private Integer skuPrice;
    private Long itemId;
    private String itemName;
}
