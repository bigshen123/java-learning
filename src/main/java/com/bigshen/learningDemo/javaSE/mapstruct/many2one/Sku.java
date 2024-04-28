package com.bigshen.learningDemo.javaSE.mapstruct.many2one;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName Sku
 * @Description: TODO
 * @Author BYJ
 * @Date 2020/9/15
 * @Version V1.0
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Sku {
    private Long id;
    private String code;
    private Integer price;
}
