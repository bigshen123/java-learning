package com.bigshen.learningDemo.javaSE.mapstruct;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @ClassName Person
 * @Description: TODO
 * @Author BYJ
 * @Date 2020/9/15
 * @Version V1.0
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Person {
    private Long id;
    private String name;
    private String email;
    private Date birthday;
    private User user;
}
