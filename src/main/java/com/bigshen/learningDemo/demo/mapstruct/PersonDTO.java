package com.bigshen.learningDemo.demo.mapstruct;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @ClassName PersonDTO
 * @Description: TODO
 * @Author BYJ
 * @Date 2020/9/15
 * @Version V1.0
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PersonDTO {
    private Long id;
    private String name;
    /**
     * 对应 Person.user.age
     */
    private Integer age;
    private String email;
    /**
     * 与 DO 里面的字段名称(birthDay)不一致
     */
    private Date birth;
    /**
     * 对 DO 里面的字段(birthDay)进行拓展,dateFormat 的形式
     */
    private String birthDateFormat;
    /**
     * 对 DO 里面的字段(birthDay)进行拓展,expression 的形式
     */
    private String birthExpressionFormat;
}
