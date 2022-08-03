package com.bigshen.learningDemo.demo.mapstruct.many2one;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;


/**
 * @ClassName ItemConverterTest
 * @Description: TODO
 *  MapStruct 注解的关键词
 *  @Mapper 只有在接口加上这个注解， MapStruct 才会去实现该接口
 *     @Mapper 里有个 componentModel 属性，主要是指定实现类的类型，一般用到两个
 *     default：默认，可以通过 Mappers.getMapper(Class) 方式获取实例对象
 *     spring：在接口的实现类上自动添加注解 @Component，可通过 @Autowired 方式注入
 * @Mapping：属性映射，若源对象属性与目标对象名字一致，会自动映射对应属性
 *     source：源属性
 *     target：目标属性
 *     dateFormat：String 到 Date 日期之间相互转换，通过 SimpleDateFormat，该值为 SimpleDateFormat              的日期格式
 *     ignore: 忽略这个字段
 * @Mappings：配置多个@Mapping
 * @MappingTarget 用于更新已有对象
 * @InheritConfiguration 用于继承配置
 *
 *
 * @Author BYJ
 * @Date 2020/9/15
 * @Version V1.0
 **/
public class ItemConverterTest {
    @Test
    public void test() {
        Item item = new Item(1L, "iPhone X");
        Sku sku = new Sku(2L, "phone12345", 1000000);
        SkuDTO skuDTO = ItemConverter.INSTANCE.domain2dto(item, sku);
        assertNotNull(skuDTO);
        assertEquals(skuDTO.getSkuId(),sku.getId());
        assertEquals(skuDTO.getSkuCode(),sku.getCode());
        assertEquals(skuDTO.getSkuPrice(),sku.getPrice());
        assertEquals(skuDTO.getItemId(),item.getId());
        assertEquals(skuDTO.getItemName(),item.getTitle());
    }
}
