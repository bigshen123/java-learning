package com.bigshen.learningDemo.javaSE.copy.deapCopy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author byj
 * @date 2022/10/13
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    private String name;
    private Address address;

    // constructors, getters and setters

}
