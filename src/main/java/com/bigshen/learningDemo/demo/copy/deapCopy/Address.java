package com.bigshen.learningDemo.demo.copy.deapCopy;

import lombok.*;

import java.io.Serializable;

/**
 * @author byj
 * @date 2022/10/13
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address implements Serializable {

    private String city;
    private String country;

    // constructors, getters and setters

}
